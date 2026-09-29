package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.CallRecordEntity

@Database(entities = [CallRecordEntity::class], version = 1, exportSchema = false)
abstract class VoxDatabase : RoomDatabase() {

    abstract fun callRecordDao(): CallRecordDao

    companion object {
        @Volatile
        private var INSTANCE: VoxDatabase? = null

        fun getDatabase(context: Context): VoxDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VoxDatabase::class.java,
                    "voxsentinel_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
