package com.siva.seva2o;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private ImageView iconPrevious, iconCurrent, iconNext;
    private ArrayList<Integer> icons;
    private int currentIndex = 0;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize views
        iconPrevious = findViewById(R.id.icon_previous);
        iconCurrent = findViewById(R.id.icon_current);
        iconNext = findViewById(R.id.icon_next);

        // List of icons
        icons = new ArrayList<>();
        icons.add(R.drawable.ic_forward);
        icons.add(R.drawable.ic_left);
        icons.add(R.drawable.ic_right);
        icons.add(R.drawable.ic_backward);

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
        if (mediaPlayer.isPlaying()) {
            // Stop the ongoing sound if it's playing
            mediaPlayer.stop();
        }
        // Reset MediaPlayer and prepare the new sound
        mediaPlayer.reset();
        mediaPlayer = MediaPlayer.create(this, R.raw.iconsmovingclicksound);
        mediaPlayer.start();
    }

    // Update icon visibility and opacity
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

        // Set next icon
        int nextIndex = (currentIndex + 1) % icons.size();
        iconNext.setImageResource(icons.get(nextIndex));
        iconNext.setVisibility(View.VISIBLE);
        iconNext.setAlpha(0.3f);
    }

    @Override
    protected void onDestroy() {
        // Release MediaPlayer when activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
