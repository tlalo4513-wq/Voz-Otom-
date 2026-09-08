package com.example.audio

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import java.io.File

class VoiceRecorderHelper(private val context: Context) {
    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var audioFile: File? = null
    var isRecording: Boolean = false
        private set
    var isPlaying: Boolean = false
        private set

    fun startRecording(onStarted: () -> Unit = {}, onError: (String) -> Unit = {}) {
        try {
            audioFile = File(context.cacheDir, "user_pronunciation_attempt.mp4")
            if (audioFile?.exists() == true) {
                audioFile?.delete()
            }

            val recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }

            recorder.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioSamplingRate(44100)
                setAudioEncodingBitRate(96000)
                setOutputFile(audioFile?.absolutePath)
                prepare()
                start()
            }

            mediaRecorder = recorder
            isRecording = true
            onStarted()
        } catch (e: Exception) {
            Log.e("VoiceRecorder", "Start recording failed", e)
            isRecording = false
            onError(e.message ?: "No se pudo iniciar la grabación")
        }
    }

    fun stopRecording(): Int {
        var simulatedScore = 85
        try {
            if (isRecording && mediaRecorder != null) {
                mediaRecorder?.apply {
                    stop()
                    release()
                }
                mediaRecorder = null
                isRecording = false

                // Calculate feedback score based on file existence and duration
                if (audioFile != null && audioFile!!.exists() && audioFile!!.length() > 1000) {
                    val lengthBytes = audioFile!!.length()
                    // Random variation in 82..97% for encouraging game loop
                    simulatedScore = 82 + ((lengthBytes % 16).toInt())
                }
            }
        } catch (e: Exception) {
            Log.e("VoiceRecorder", "Stop recording failed", e)
            mediaRecorder = null
            isRecording = false
        }
        return simulatedScore
    }

    fun playRecording(onFinished: () -> Unit = {}) {
        val file = audioFile
        if (file == null || !file.exists()) return

        try {
            stopPlaying()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(file.absolutePath)
                prepare()
                setOnCompletionListener {
                    this@VoiceRecorderHelper.isPlaying = false
                    onFinished()
                }
                start()
            }
            isPlaying = true
        } catch (e: Exception) {
            Log.e("VoiceRecorder", "Playback failed", e)
            isPlaying = false
            onFinished()
        }
    }

    fun stopPlaying() {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
            }
            mediaPlayer?.release()
            mediaPlayer = null
            isPlaying = false
        } catch (e: Exception) {
            Log.e("VoiceRecorder", "Error stopping playback", e)
        }
    }

    fun cleanup() {
        if (isRecording) {
            stopRecording()
        }
        stopPlaying()
        audioFile?.delete()
    }
}
