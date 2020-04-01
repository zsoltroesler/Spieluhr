package com.example.android.spieluhr

import android.content.Context
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    /**
     * Handles media player buttons
     */
    private lateinit var playKiwi: Button
    private lateinit var stopKiwi: Button
    private lateinit var playMomo: Button
    private lateinit var stopMomo: Button

    /**
     * Handles playback of the 2 sound files
     */
    private var mMediaPlayer: MediaPlayer? = null

    /**
     * Handles audio focus changes
     */
    private lateinit var mAudioManager: AudioManager

    /**
     * This listener gets triggered whenever the audio focus changes. It is simplified as I cannot
     * preserve audio focus against other apps i.e incoming call. The system can always remove audio
     * focus, which is normally great but until the kids fall asleep it is a drawback.
     */
    private val mOnAudioFocusChangeListener = OnAudioFocusChangeListener { focusChange ->
        if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            mMediaPlayer?.start()
        } else {
            mMediaPlayer?.isPlaying
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playKiwi = findViewById(R.id.play_kiwi)
        stopKiwi = findViewById(R.id.stop_kiwi)
        playMomo = findViewById(R.id.play_momo)
        stopMomo = findViewById(R.id.stop_momo)

        // Create and setup the {@link AudioManager} to request audio focus
        mAudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        //Set OnClickListener on play button for Kiwi
        playKiwi.setOnClickListener {
            Toast.makeText(applicationContext, "Gute Nacht Kiwi!", Toast.LENGTH_SHORT).show()

            // Release the media player if it currently exists
            releaseMediaPlayer()

            // Request audio focus for playback
            val result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,  // Use the music stream.
                    AudioManager.STREAM_MUSIC,  // Request permanent focus.
                    AudioManager.AUDIOFOCUS_GAIN)
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                // Create the corresponding media player with Kiwi's song
                mMediaPlayer = MediaPlayer.create(this@MainActivity, R.raw.funkel)
                mMediaPlayer!!.start()
                playKiwi.isEnabled = false
                playMomo.isEnabled = false
                stopMomo.isEnabled = false

                // After completion the song is being restarted again-again until
                // mMediaPlayer.stop() called
                mMediaPlayer!!.setOnCompletionListener {
                    mMediaPlayer!!.start()
                    playKiwi.isEnabled = false
                    playMomo.isEnabled = false
                    stopMomo.isEnabled = false
                }
            }
        }

        //Set OnClickListener on stop button for Kiwi
        stopKiwi.setOnClickListener {
            Toast.makeText(applicationContext, "Schlaf gut!", Toast.LENGTH_SHORT).show()
            playKiwi.isEnabled = true
            playMomo.isEnabled = true
            stopMomo.isEnabled = true
            mMediaPlayer?.stop()

            // Abandon audio focus when playback complete
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener)
        }

        //Set OnClickListener on play button for Momo
        playMomo.setOnClickListener {
            Toast.makeText(applicationContext, "Gute Nacht Momo!", Toast.LENGTH_SHORT).show()

            // Release the media player if it currently exists
            releaseMediaPlayer()

            // Request audio focus for playback
            val result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,  // Use the music stream.
                    AudioManager.STREAM_MUSIC,  // Request permanent focus.
                    AudioManager.AUDIOFOCUS_GAIN)
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                // Create the corresponding media player for Momo's song
                mMediaPlayer = MediaPlayer.create(this@MainActivity, R.raw.lalelu)
                mMediaPlayer!!.start()
                playMomo.isEnabled = false
                playKiwi.isEnabled = false
                stopKiwi.isEnabled = false

                // After completion the song is being restarted again-again until
                // mMediaPlayer.stop() called
                mMediaPlayer?.setOnCompletionListener {
                    mMediaPlayer?.start()
                    playMomo.isEnabled = false
                    playKiwi.isEnabled = false
                    stopKiwi.isEnabled = false
                }
            }
        }

        //Set OnClickListener on stop button for Momo
        stopMomo.setOnClickListener {
            Toast.makeText(applicationContext, "Schlaf gut!", Toast.LENGTH_SHORT).show()
            playMomo.isEnabled = true
            playKiwi.isEnabled = true
            stopKiwi.isEnabled = true
            mMediaPlayer?.stop()

            // Abandon audio focus when playback complete
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener)
        }
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private fun releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        // Regardless of the current state of the media player, release its resources
        // because we no longer need it.
        mMediaPlayer?.release()

        // Set the media player back to null.
        mMediaPlayer = null
    }
}