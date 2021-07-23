package ir.mjahanbazi.audioplayerstopinbackground;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;

public class MyActivity extends AppCompatActivity {
    private HashSet<MyActivityListenerDefault> listeners = new HashSet<>();


    @Override
    protected void onStart() {
        super.onStart();
        for (MyActivityListenerDefault l : listeners) {
            l.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (MyActivityListenerDefault l : listeners) {
            l.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (MyActivityListenerDefault l : listeners) {
            l.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (MyActivityListenerDefault l : listeners) {
            l.onDestroy();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        for (MyActivityListenerDefault l : listeners) {
            l.onRestart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (MyActivityListenerDefault l : listeners) {
            l.onStop();
        }
    }

    protected void AddListener(MyActivityListenerDefault l) {
        listeners.add(l);
    }

    protected void RemoveListener(MyActivityListenerDefault l) {
        listeners.remove(l);
    }
}
