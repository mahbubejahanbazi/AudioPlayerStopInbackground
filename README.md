# Stop Playing Audio While Application Is In Background
## audio player

Description of the application
A simple audio player that has the base player keys(play / pause and progress bar), in addition the project implemented some specific features.
- A basic audio player
- A player that detects the status of the application (Active / in background)
- The audio manager is used.

 

## Tech Stack

Java

<p align="center">
  <img src="https://github.com/mahbubejahanbazi/audio_player_stop_In_background/blob/main/images/default.jpg" />
</p>



<p align="center">
  <img src="https://github.com/mahbubejahanbazi/audio_player_stop_In_background/blob/main/images/checked.jpg" />
</p>

## Source code

ActivityListenerDefault.java
```java
public class ActivityListenerDefault implements ActivityListener {
    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onDestroy() {

    }
}
```
HeadsetReceiver.java
```java
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class HeadsetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MainActivity activity = (MainActivity) context;
        if (activity.audioPlayer.isPlaying()) {
            activity.audioPlayer.pauseAudio();
        }
    }
}
```
MyActivity.java
```java
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;

public class MyActivity extends AppCompatActivity {
    private HashSet<ActivityListenerDefault> listeners = new HashSet<>();


    @Override
    protected void onStart() {
        super.onStart();
        for (ActivityListenerDefault l : listeners) {
            l.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (ActivityListenerDefault l : listeners) {
            l.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (ActivityListenerDefault l : listeners) {
            l.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (ActivityListenerDefault l : listeners) {
            l.onDestroy();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        for (ActivityListenerDefault l : listeners) {
            l.onRestart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (ActivityListenerDefault l : listeners) {
            l.onStop();
        }
    }

    protected void AddListener(ActivityListenerDefault l) {
        listeners.add(l);
    }

    protected void RemoveListener(ActivityListenerDefault l) {
        listeners.remove(l);
    }
}
```
MainActivity.java
```java
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;


public class MainActivity extends MyActivity {
    public AudioPlayer audioPlayer;
    private ActivityListenerDefault listener = new ActivityListenerDefault() {
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
```
AudioPlayer.java
```java
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.TimeUnit;

public class AudioPlayer extends androidx.constraintlayout.widget.ConstraintLayout {
    private static boolean playing = false;
    private MediaPlayer mediaPlayer;
    private ImageButton playButton;
    private ImageButton pauseButton;
    private TextView fileName;
    private TextView fileTime;
    private SeekBar seekBar;
    private final SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            mediaPlayer.seekTo(seekBar.getProgress());
            refreshGui();
        }
    };
    private Runnable UpdateAudioTime = new Runnable() {
        public void run() {
            while (true) {
                fileTime.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshGui();
                    }
                }, 100);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    return;
                }
                if (!playing) {
                    break;
                }
            }
        }
    };
    private OnClickListener onClickListenerPauseButton = new OnClickListener() {
        public void onClick(View v) {
            pauseAudio();
        }
    };
    private OnClickListener onClickListenerPlayerButton = new OnClickListener() {
        public void onClick(View arg0) {
            playAudio();
        }
    };
    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mp) {
            playButton.setVisibility(VISIBLE);
            pauseButton.setVisibility(GONE);
            mediaPlayer.seekTo(0);
            seekBar.setProgress(0);
            mediaPlayer.pause();
        }
    };

    public AudioPlayer(@NonNull Context context) {
        super(context);
        init(context);
    }

    public AudioPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflator.inflate(R.layout.file_audio, this);
        playButton = (ImageButton) findViewById(R.id.file_audio_play);
        pauseButton = (ImageButton) findViewById(R.id.file_audio_pause);
        fileName = (TextView) findViewById(R.id.file_audio_name);
        seekBar = (SeekBar) findViewById(R.id.file_audio_seekBar);
        fileTime = (TextView) findViewById(R.id.file_audio_time);
        playButton.setOnClickListener(onClickListenerPlayerButton);
        pauseButton.setOnClickListener(onClickListenerPauseButton);
    }

    public void pauseAudio() {
        playing = false;
        pauseButton.setVisibility(INVISIBLE);
        playButton.setVisibility(VISIBLE);
        mediaPlayer.pause();
    }

    public void playAudio() {
        playing = true;
        new Thread(UpdateAudioTime).start();
        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
        mediaPlayer.start();
        pauseButton.setVisibility(VISIBLE);
        playButton.setVisibility(INVISIBLE);
        refreshGui();
    }

    private String getTimeStr(int time) {
        String minutes = "";
        String seconds = "";
        final long sec = TimeUnit.MILLISECONDS.toSeconds((long) time)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) time));
        if (sec < 10) {
            seconds = "0" + sec;
        } else {
            seconds = sec + "";
        }
        minutes = "" + TimeUnit.MILLISECONDS.toMinutes((long) time);
        return String.format("%s:%s", minutes, seconds);
    }


    private void refreshGui() {
        if (mediaPlayer == null) {
            return;
        }
        int length = mediaPlayer.getDuration();
        int current = mediaPlayer.getCurrentPosition();
        fileTime.setText(String.format("%s / %s",
                getTimeStr(current),
                getTimeStr(length)));
        seekBar.setProgress((int) current);

    }

    public void setAudio(int resid) {
        fileName.setText(getResources().getResourceEntryName(resid));
        mediaPlayer = MediaPlayer.create(getContext(), resid);
        intiMedia();
    }

    public void setAudio(Uri uri) {
        fileName.setText(getFileName(uri));
        mediaPlayer = MediaPlayer.create(getContext(), uri);
        intiMedia();
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void intiMedia() {
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        seekBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.setOnCompletionListener(onCompletionListener);
        fileTime.setText(String.format("0:00 / %s", getTimeStr(mediaPlayer.getDuration())));
    }

}
```
## Contact

mjahanbazi@protonmail.com