package com.siva.seva2o.Base;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class TTSUtility {

    private static TTSUtility instance;
    private TextToSpeech textToSpeech;

    private TTSUtility(Context context) {
        textToSpeech = new TextToSpeech(context.getApplicationContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.US); // Set default language
            } else {
                Log.e("TTSUtility", "Initialization failed!");
            }
        });
    }

    public static TTSUtility getInstance(Context context) {
        if (instance == null) {
            instance = new TTSUtility(context);
        }
        return instance;
    }

    public void speak(String text) {
        if (textToSpeech != null) {
            textToSpeech.stop(); // Stop any ongoing speech
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null); // Speak the new text
        }
    }

    public void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
    }
}

