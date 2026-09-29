# VoxSentinel AI Backend (Pretrained Voice Anti-Spoofing & Deepfake Detector)

This backend runs the **AASIST** (Audio Anti-Spoofing Integration with SpecTral and Spatial Features) and **Wav2Vec2** pretrained neural model for VoxSentinel.

## Pretrained AI Model Information
- **Model Name:** `sub20/aasist-anti-spoofing` / `microsoft/wav2vec2-base`
- **Purpose:** Analyzes temporary audio chunks for synthetic, cloned, or deepfake speech characteristics.
- **Output:** Raw logits, sigmoid probability, VoxSentinel 0-100 risk score, and 3-level classification.

## Missing Dependencies / Environment Setup
To run this Python backend server locally or in a cloud instance:

1. **System Dependencies Required:**
   - Python 3.10 or 3.11
   - `python3-pip` / `python3-venv`
   - `ffmpeg` or `libsndfile1` (for audio decoding)

2. **Installation Steps:**
   ```bash
   cd backend
   python3 -m venv venv
   source venv/bin/activate
   pip install -r requirements.txt
   ```

3. **Running the Server:**
   ```bash
   python3 main.py
   # OR
   uvicorn main:app --host 0.0.0.0 --port 8000 --reload
   ```

4. **Connecting VoxSentinel Mobile App:**
   - Set the API Endpoint in VoxSentinel Settings screen: `http://<YOUR_SERVER_IP>:8000/api/v1/analyze-audio`
