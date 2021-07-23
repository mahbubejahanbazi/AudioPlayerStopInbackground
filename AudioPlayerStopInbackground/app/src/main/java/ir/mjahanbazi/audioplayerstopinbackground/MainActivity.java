package ir.mjahanbazi.audioplayerstopinbackground;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;


public class MainActivity extends MyActivity {
    public MyAudioPlayer audioPlayer;
    private MyActivityListenerDefault listener = new MyActivityListenerDefault() {
        @Override
        public void onPause() {
            super.onPause();
            audioPlayer.pauseAudio();
        }

        @Override
        public void onResume() {
            super.onResume();
            audioPlayer.playAudio();
        }
    };
    private CompoundButton.OnCheckedChangeListener appInBackground = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                AddListener(listener);
            } else {
                RemoveListener(listener);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        audioPlayer = findViewById(R.id.activity_main_audio_player);
        audioPlayer.setAudio(R.raw.audio_file_example);
        CheckBox c = findViewById(R.id.activity_main_stop_audio_in_background);
        c.setOnCheckedChangeListener(appInBackground);
    }

}
