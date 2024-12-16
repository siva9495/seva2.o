package com.siva.seva2o;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.siva.seva2o.Base.TTSUtility;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private ImageView iconPrevious, iconCurrent, iconNext;
    private TextView iconNameTextView;
    private ArrayList<Integer> icons;
    private ArrayList<String> iconNames;
    private int currentIndex = 0;
    private MediaPlayer mediaPlayer;
    private TTSUtility ttsUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize views
        iconPrevious = findViewById(R.id.icon_previous);
        iconCurrent = findViewById(R.id.icon_current);
        iconNext = findViewById(R.id.icon_next);
        iconNameTextView = findViewById(R.id.icon_name);

        // Initialize TTS Utility
        ttsUtility = TTSUtility.getInstance(this);

        // List of icons
        icons = new ArrayList<>();
        icons.add(R.drawable.icon_describescene);
        icons.add(R.drawable.icon_navigate);
        icons.add(R.drawable.icon_findobject);
        icons.add(R.drawable.icon_facerecog);
        icons.add(R.drawable.icon_readforme);
        icons.add(R.drawable.icon_talktome);
        icons.add(R.drawable.icon_sos);
        icons.add(R.drawable.icon_settings);

        // List of icon names
        iconNames = new ArrayList<>();
        iconNames.add("Describe Scene");
        iconNames.add("Navigate");
        iconNames.add("Find Object");
        iconNames.add("Face Recognition");
        iconNames.add("Read For Me");
        iconNames.add("Talk To Me");
        iconNames.add("SOS");
        iconNames.add("Settings");

        // Initialize MediaPlayer
        mediaPlayer = new MediaPlayer();

        // Set initial state
        updateIcons();
    }

    // Handle key events for navigation
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            moveRight();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            moveLeft();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // Move to the next icon
    private void moveRight() {
        currentIndex = (currentIndex + 1) % icons.size();
        playSound();
        updateIcons();
    }

    // Move to the previous icon
    private void moveLeft() {
        currentIndex = (currentIndex - 1 + icons.size()) % icons.size();
        playSound();
        updateIcons();
    }

    // Play sound effect
    private void playSound() {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer = MediaPlayer.create(this, R.raw.iconsmovingclicksound);
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update icon visibility, opacity, and name
    private void updateIcons() {
        // Set previous icon
        int prevIndex = (currentIndex - 1 + icons.size()) % icons.size();
        if (currentIndex == 0) {
            // If at the start, hide the previous icon
            iconPrevious.setVisibility(View.INVISIBLE);
        } else {
            iconPrevious.setImageResource(icons.get(prevIndex));
            iconPrevious.setVisibility(View.VISIBLE);
            iconPrevious.setAlpha(0.3f);
        }

        // Set current icon
        iconCurrent.setImageResource(icons.get(currentIndex));
        iconCurrent.setAlpha(1.0f);

        // Update the TextView with the current icon name
        iconNameTextView.setText(iconNames.get(currentIndex));

        // Speak the current icon name
        ttsUtility.speak(iconNames.get(currentIndex));

        // Set next icon
        int nextIndex = (currentIndex + 1) % icons.size();
        iconNext.setImageResource(icons.get(nextIndex));
        iconNext.setVisibility(View.VISIBLE);
        iconNext.setAlpha(0.3f);
    }

    @Override
    protected void onDestroy() {
        // Release MediaPlayer and TTSUtility when activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (ttsUtility != null) {
            ttsUtility.shutdown();
        }
        super.onDestroy();
    }
}
