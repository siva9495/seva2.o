package com.siva.seva2o.Base;

import static android.view.KeyEvent.KEYCODE_1;
import static android.view.KeyEvent.KEYCODE_2;
import static android.view.KeyEvent.KEYCODE_3;
import static android.view.KeyEvent.KEYCODE_4;
import static android.view.KeyEvent.KEYCODE_5;
import static android.view.KeyEvent.KEYCODE_6;
import static android.view.KeyEvent.KEYCODE_A;
import static android.view.KeyEvent.KEYCODE_B;
import static android.view.KeyEvent.KEYCODE_BUTTON_2;
import static android.view.KeyEvent.KEYCODE_BUTTON_3;
import static android.view.KeyEvent.KEYCODE_BUTTON_4;
import static android.view.KeyEvent.KEYCODE_C;
import static android.view.KeyEvent.KEYCODE_D;
import static android.view.KeyEvent.KEYCODE_E;
import static android.view.KeyEvent.KEYCODE_F;
import static android.view.KeyEvent.KEYCODE_F10;
import static android.view.KeyEvent.KEYCODE_G;
import static android.view.KeyEvent.KEYCODE_H;
import static android.view.KeyEvent.KEYCODE_I;
import static android.view.KeyEvent.KEYCODE_J;
import static android.view.KeyEvent.KEYCODE_K;
import static android.view.KeyEvent.KEYCODE_L;
import static android.view.KeyEvent.KEYCODE_M;
import static android.view.KeyEvent.KEYCODE_N;
import static android.view.KeyEvent.KEYCODE_T;
import static android.view.KeyEvent.KEYCODE_U;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;


import com.siva.seva2o.R;
import com.siva.seva2o.SevaAppsActivities.DescribeSceneActivity;
import com.siva.seva2o.SevaAppsActivities.NavigateActivity;
import com.vuzix.sdk.speechrecognitionservice.VuzixSpeechClient;

import java.util.ArrayList;

public class BaseVoiceCommandsActivity extends AppCompatActivity {

    private static final String TAG = "BaseVoiceCommands";
    private static final String SHARED_PREFS_FILE = "UserPrefs";
    private static final String PREF_USER_NICKNAME = "user_nickname";
    private static final String PREF_PHONE_NUMBER1 = "phone_number1";
    private static final String PREF_PHONE_NUMBER2 = "phone_number2";

    protected VuzixSpeechClient vuzixSpeechClient;
    private TTSUtility ttsUtility;
    private final ArrayList<String> appNamesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_voice_commands);

        try {
            vuzixSpeechClient = new VuzixSpeechClient(this);
            setupVoiceCommands();
        } catch (Exception e) {
            Log.e(TAG, "Failed to set up VuzixSpeechClient: " + e.getMessage());
        }

    }

    private void setupVoiceCommands() {
        // Apps commands
        addVoiceCommand("Describe Scene", KEYCODE_3, "Describe Scene");
        addVoiceCommand("Navigate", KEYCODE_1, "Navigate");
        addVoiceCommand("Find Object", KEYCODE_2, "Find Object");
        addVoiceCommand("Recognise Face", KEYCODE_4, "Recognise Face");
        addVoiceCommand("Read For Me", KEYCODE_5, "Read For Me");
        addVoiceCommand("Talk To Me", KEYCODE_6, "Talk To Me");
        addVoiceCommand("SOS", KEYCODE_F10, "SOS");
        addVoiceCommand("QR Scanner", KEYCODE_BUTTON_2, "QR Scanner");

        // Emergency Apps commands
        addVoiceCommand("I am fine", KEYCODE_A, null);
        addVoiceCommand("I am okay", KEYCODE_A, null);
        addVoiceCommand("I am good", KEYCODE_A, null);

        // Device status commands
        addVoiceCommand("Where am I", KEYCODE_B, null);
        addVoiceCommand("Battery", KEYCODE_C, null);
        addVoiceCommand("Battery status", KEYCODE_C, null);
        addVoiceCommand("Wifi", KEYCODE_D, null);
        addVoiceCommand("Wifi status", KEYCODE_D, null);
        addVoiceCommand("Bluetooth", KEYCODE_E, null);
        addVoiceCommand("Bluetooth status", KEYCODE_E, null);
        addVoiceCommand("date", KEYCODE_BUTTON_3, null);
        addVoiceCommand("time", KEYCODE_BUTTON_4, null);

        // Wake word phrases Commands
        vuzixSpeechClient.insertWakeWordPhrase("hello seva");
        vuzixSpeechClient.insertWakeWordPhrase("Hello Seva");
        addVoiceCommand("Sleep", KEYCODE_F, null);
        addVoiceCommand("Disable Touchpad", KEYCODE_G, null);
        addVoiceCommand("Enable Touchpad", KEYCODE_H, null);

        // Repeat command
        vuzixSpeechClient.insertKeycodePhrase("Repeat", KEYCODE_I);

        // Stop command
        addVoiceCommand("Stop", KEYCODE_J, null);

        //Back command
        addVoiceCommand("Back", KEYCODE_L, null);
        addVoiceCommand("home", KEYCODE_L, null);
        addVoiceCommand("go back", KEYCODE_L, null);
        addVoiceCommand("go home", KEYCODE_L, null);

        // Seva Application Commands
        addVoiceCommand("Express", KEYCODE_K, null);
        addVoiceCommand("yes", KEYCODE_T, null);
        addVoiceCommand("no", KEYCODE_U, null);

        // Delete unnecessary phrases
        deleteUnnecessaryPhrases();
    }

    private void addVoiceCommand(String phrase, int keyCode, String appName) {
        vuzixSpeechClient.insertKeycodePhrase(phrase, keyCode);
        if (appName != null) {
            // Add the app name to the app names list
            appNamesList.add(appName);
        }
    }

    private void deleteUnnecessaryPhrases() {
        vuzixSpeechClient.deletePhrase("Okay");
        vuzixSpeechClient.deletePhrase("Open");
        vuzixSpeechClient.deletePhrase("Close");
        vuzixSpeechClient.deletePhrase("Hello Vuzix");
        vuzixSpeechClient.deletePhrase("Hello vuzix");
        vuzixSpeechClient.deletePhrase("Hello Vuz");
        vuzixSpeechClient.deletePhrase("Hello Blade");
        vuzixSpeechClient.deletePhrase("Speech Settings");
        vuzixSpeechClient.deletePhrase("Speech Commands");
        vuzixSpeechClient.deletePhrase("Command List");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KEYCODE_1:
                handleNavigate();
                return true;
            case KEYCODE_2:
                handleFindObjects();
                return true;
            case KEYCODE_3:
                handleDescribeScene();
                return true;
            case KEYCODE_4:
                handleFaceRecognition();
                return true;
            case KEYCODE_5:
                handleReadForMe();
                return true;
            case KEYCODE_6:
                handleTalkToMe();
                return true;
            case KEYCODE_BUTTON_2:
                handleUpdatesQRScanner();
                return true;
            default:
                return false;
        }
    }

    private String getUserName() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        String userNickname = sharedPreferences.getString(PREF_USER_NICKNAME, "user_nickname");
        return (userNickname != null && !userNickname.isEmpty()) ? userNickname : "user";
    }

    private void handleNavigate() {
        String userName = getUserName();ttsUtility.speak("ok " + userName + " Opening Navigate");
        startActivity(new Intent(this, NavigateActivity.class));
    }

    private void handleDescribeScene() {
        String userName = getUserName();ttsUtility.speak("ok " + userName + " Opening Describe Scene");
        startActivity(new Intent(this, DescribeSceneActivity.class));
    }

    private void handleFindObjects() {

    }

    private void handleReadForMe() {

    }

    private void handleTalkToMe() {

    }

    private void handleFaceRecognition() {

    }

    private void handleUpdatesQRScanner() {

    }

}