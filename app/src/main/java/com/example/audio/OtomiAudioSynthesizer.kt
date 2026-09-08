package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.sin

class OtomiAudioSynthesizer(private val context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e("OtomiAudio", "Error initializing TTS", e)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.let { engine ->
                val result = engine.setLanguage(Locale("es", "MX"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    engine.setLanguage(Locale.getDefault())
                }
                engine.setSpeechRate(0.85f) // Slightly slower for language learners
                engine.setPitch(1.05f)
                isTtsReady = true
            }
        }
    }

    /**
     * Converts Otomi orthography (Hñähñu) into phonetic Spanish sounds suitable for TTS,
     * while also generating custom harmonic acoustic reinforcement.
     */
    fun speakWord(otomiWord: String, phoneticSpanish: String = "", onDone: () -> Unit = {}) {
        val targetToSpeak = if (phoneticSpanish.isNotBlank()) {
            cleanForTts(phoneticSpanish)
        } else {
            otomiToPhoneticApprox(otomiWord)
        }

        if (isTtsReady && tts != null) {
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                override fun onDone(utteranceId: String?) {
                    onDone()
                }
                override fun onError(utteranceId: String?) {
                    onDone()
                }
            })
            val params = android.os.Bundle()
            tts?.speak(targetToSpeak, TextToSpeech.QUEUE_FLUSH, params, "otomi_${System.currentTimeMillis()}")
        } else {
            // Fallback to acoustic tone pulse
            playAcousticTone(otomiWord)
            onDone()
        }
    }

    /**
     * Synthesizes an acoustic melodic tone sequence reflecting Otomí tonal contours
     * (High tone, low tone, and glottal pause).
     */
    fun playAcousticTone(word: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val sampleRate = 22050
                val durationSeconds = 0.45
                val numSamples = (sampleRate * durationSeconds).toInt()
                val samples = ShortArray(numSamples)

                val baseFreq = when {
                    word.contains("ä") || word.contains("ö") || word.contains("ë") -> 280.0 // Nasal formant
                    word.contains("'") -> 340.0 // Glottal high
                    word.endsWith("í") || word.endsWith("á") -> 380.0 // High pitch
                    else -> 300.0
                }

                for (i in 0 until numSamples) {
                    val t = i.toDouble() / sampleRate
                    // Apply glottal stop interruption if word contains saltillo
                    val envelope = if (word.contains("'") && i in (numSamples * 0.35).toInt()..(numSamples * 0.48).toInt()) {
                        0.05 // dip in amplitude
                    } else {
                        val attack = (i.toDouble() / (sampleRate * 0.05)).coerceAtMost(1.0)
                        val decay = ((numSamples - i).toDouble() / (sampleRate * 0.15)).coerceIn(0.0, 1.0)
                        attack * decay
                    }

                    // Rich fundamental with 2 harmonics for human vocal timbre
                    val wave = 0.6 * sin(2.0 * Math.PI * baseFreq * t) +
                               0.3 * sin(4.0 * Math.PI * baseFreq * t) +
                               0.1 * sin(6.0 * Math.PI * baseFreq * t)

                    samples[i] = (wave * envelope * Short.MAX_VALUE * 0.75).toInt().toShort()
                }

                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(numSamples * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.write(samples, 0, numSamples)
                audioTrack.play()
            } catch (e: Exception) {
                Log.e("OtomiAudio", "Error playing tone", e)
            }
        }
    }

    private fun cleanForTts(raw: String): String {
        return raw.replace(Regex("[()\"/]"), "")
            .replace("con golpe de glotis", "")
            .replace("vocal nasal", "")
            .replace("aire por la nariz", "")
            .replace("-", " ")
            .trim()
    }

    private fun otomiToPhoneticApprox(otomi: String): String {
        return otomi
            .replace("x", "sh")
            .replace("X", "Sh")
            .replace("hñ", "ñ")
            .replace("Hñ", "Ñ")
            .replace("tz", "ts")
            .replace("Tz", "Ts")
            .replace("ä", "a")
            .replace("ë", "e")
            .replace("ö", "o")
            .replace("ü", "u")
            .replace("'", " ")
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (e: Exception) {
            Log.e("OtomiAudio", "Error stopping TTS", e)
        }
    }
}
