package com.example.data.repository

import com.example.data.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthRepository {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _isLoggedIn = MutableStateFlow<Boolean>(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        try {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                _userProfile.value = UserProfile(
                    userId = currentUser.uid,
                    name = currentUser.displayName ?: currentUser.email?.substringBefore("@") ?: "Vox User",
                    email = currentUser.email ?: "",
                    isLoggedIn = true
                )
                _isLoggedIn.value = true
            }

            auth.addAuthStateListener { firebaseAuth ->
                val user = firebaseAuth.currentUser
                if (user != null) {
                    _userProfile.value = UserProfile(
                        userId = user.uid,
                        name = user.displayName ?: user.email?.substringBefore("@") ?: "Vox User",
                        email = user.email ?: "",
                        isLoggedIn = true
                    )
                    _isLoggedIn.value = true
                } else {
                    _isLoggedIn.value = false
                    _userProfile.value = null
                }
            }
        } catch (e: Exception) {
            _isLoggedIn.value = false
        }
    }

    fun register(name: String, email: String, pass: String, onResult: (Boolean, String?) -> Unit) {
        val trimmedEmail = email.trim()
        val trimmedPass = pass.trim()
        val trimmedName = name.trim()

        if (trimmedEmail.isBlank() || trimmedPass.isBlank()) {
            onResult(false, "Email and password cannot be empty.")
            return
        }
        if (trimmedPass.length < 6) {
            onResult(false, "Password must be at least 6 characters.")
            return
        }

        try {
            auth.createUserWithEmailAndPassword(trimmedEmail, trimmedPass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = auth.currentUser
                        if (firebaseUser != null && trimmedName.isNotBlank()) {
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(trimmedName)
                                .build()
                            firebaseUser.updateProfile(profileUpdates)
                        }
                        val userProf = UserProfile(
                            userId = firebaseUser?.uid ?: "usr_${System.currentTimeMillis()}",
                            name = if (trimmedName.isNotBlank()) trimmedName else trimmedEmail.substringBefore("@"),
                            email = trimmedEmail,
                            isLoggedIn = true
                        )
                        _userProfile.value = userProf
                        _isLoggedIn.value = true
                        onResult(true, null)
                    } else {
                        val errorMsg = task.exception?.localizedMessage ?: "Registration failed."
                        onResult(false, errorMsg)
                    }
                }
        } catch (e: Exception) {
            val errorMsg = e.localizedMessage ?: "Firebase Authentication error."
            onResult(false, errorMsg)
        }
    }

    fun login(email: String, pass: String, onResult: (Boolean, String?) -> Unit) {
        val trimmedEmail = email.trim()
        val trimmedPass = pass.trim()

        if (trimmedEmail.isBlank() || trimmedPass.isBlank()) {
            onResult(false, "Email and password cannot be empty.")
            return
        }

        try {
            auth.signInWithEmailAndPassword(trimmedEmail, trimmedPass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = auth.currentUser
                        val userProf = UserProfile(
                            userId = firebaseUser?.uid ?: "usr_${System.currentTimeMillis()}",
                            name = firebaseUser?.displayName ?: trimmedEmail.substringBefore("@"),
                            email = trimmedEmail,
                            isLoggedIn = true
                        )
                        _userProfile.value = userProf
                        _isLoggedIn.value = true
                        onResult(true, null)
                    } else {
                        val errorMsg = task.exception?.localizedMessage ?: "Invalid email or password."
                        onResult(false, errorMsg)
                    }
                }
        } catch (e: Exception) {
            val errorMsg = e.localizedMessage ?: "Firebase Authentication error."
            onResult(false, errorMsg)
        }
    }

    fun logout() {
        try {
            auth.signOut()
        } catch (e: Exception) {
            // Ignore
        }
        _isLoggedIn.value = false
        _userProfile.value = null
    }

    fun updateProfile(name: String, phone: String) {
        _userProfile.value = _userProfile.value?.copy(
            name = name,
            phone = phone
        )
    }
}
