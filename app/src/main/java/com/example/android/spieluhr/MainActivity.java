package com.example.android.spieluhr;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /**
     * Handles media player buttons
     */
    private Button playKiwi, stopKiwi, playMomo, stopMomo;

    /**
     * Handles playback of the 2 sound files
     */
    private MediaPlayer mMediaPlayer;

    /**
     * Handles audio focus changes
     */
    private AudioManager mAudioManager;

    /**
     * This listener gets triggered whenever the audio focus changes. It is simplified as I cannot
     * preserve audio focus against other apps i.e incoming call. The system can always remove audio
     * focus, which is normally great but until the kids fall asleep it is a drawback.
     */
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if(focusChange == AudioManager.AUDIOFOCUS_GAIN){
                mMediaPlayer.start();
            } else {
                mMediaPlayer.isPlaying();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the corresponding buttons
        playKiwi = findViewById(R.id.play_kiwi);
        stopKiwi = findViewById(R.id.stop_kiwi);
        playMomo = findViewById(R.id.play_momo);
        stopMomo = findViewById(R.id.stop_momo);

        // Create and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        //Set OnClickListener on play button for Kiwi
        playKiwi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Gute Nacht Kiwi!", Toast.LENGTH_SHORT).show();

                // Release the media player if it currently exists
                releaseMediaPlayer();

                // Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    // Create the corresponding media player with Kiwi's song
                    mMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.funkel);

                    mMediaPlayer.start();
                    playKiwi.setEnabled(false);
                    playMomo.setEnabled(false);
                    stopMomo.setEnabled(false);

                    // After completion the song is being restarted again-again until
                    // mMediaPlayer.stop() called
                    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mMediaPlayer.start();
                            playKiwi.setEnabled(false);
                            playMomo.setEnabled(false);
                            stopMomo.setEnabled(false);
                        }
                    });
                }
            }
        });

        //Set OnClickListener on stop button for Kiwi
        stopKiwi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Schlaf gut!", Toast.LENGTH_SHORT).show();
                playKiwi.setEnabled(true);
                playMomo.setEnabled(true);
                stopMomo.setEnabled(true);
                if(mMediaPlayer != null) mMediaPlayer.stop();

                // Abandon audio focus when playback complete
                mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
            }
        });

        //Set OnClickListener on play button for Momo
        playMomo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Gute Nacht Momo!", Toast.LENGTH_SHORT).show();

                // Release the media player if it currently exists
                releaseMediaPlayer();

                // Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    // Create the corresponding media player for Momo's song
                    mMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.lalelu);
                    mMediaPlayer.start();
                    playMomo.setEnabled(false);
                    playKiwi.setEnabled(false);
                    stopKiwi.setEnabled(false);

                    // After completion the song is being restarted again-again until
                    // mMediaPlayer.stop() called
                    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mMediaPlayer.start();
                            playMomo.setEnabled(false);
                            playKiwi.setEnabled(false);
                            stopKiwi.setEnabled(false);
                        }
                    });
                }
            }
        });

        //Set OnClickListener on stop button for Momo
        stopMomo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Schlaf gut!", Toast.LENGTH_SHORT).show();
                playMomo.setEnabled(true);
                playKiwi.setEnabled(true);
                stopKiwi.setEnabled(true);
                if(mMediaPlayer != null) mMediaPlayer.stop();

                // Abandon audio focus when playback complete
                mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
            }
        });
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null.
            mMediaPlayer = null;
        }
    }
}
