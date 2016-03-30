package com.bluewolftek.www.memorygame;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    // Prepare objects and sound references

    // Initialize sounds variables
    private SoundPool soundPool;
    int sample1 = -1;
    int sample2 = -1;
    int sample3 = -1;
    int sample4 = -1;

    // For the UI
    TextView txtScore;
    TextView txtDifficulty;
    TextView txtWatchGo;

    Button btnButton1;
    Button btnButton2;
    Button btnButton3;
    Button btnButton4;
    Button btnReplay;

    // Some variables for our thread
    int difficultyLevel = 3;
    // An array to hold the randomly generated sequence
    int[] sequenceToCopy = new int[100];

    private Handler myHandler;
    // Are we playing a sequence at the moment?
    boolean playSequence = false;
    // And which element of the sequence are we on?
    int elementToPlay = 0;

    // For checking players answer
    int playerResponses;
    int playerScore;
    boolean isResponding;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        try {
            // Create objects of the 2 required classes
            AssetManager assetManager = getAssets();
            AssetFileDescriptor descriptor;

            // Create our three fx in memory ready for use
            descriptor = assetManager.openFd("sample1.ogg");
            sample1 = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("sample2.ogg");
            sample2 = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("sample3.ogg");
            sample3 = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("sample4.ogg");
            sample4 = soundPool.load(descriptor, 0);

        } catch(IOException e) {
            // Catch exceptions here
        }

        // Reference all of the elements in our UI

        // TextViews
        txtScore = (TextView)findViewById(R.id.txtScore);
        txtScore.setText("Score: " + playerScore);

        txtDifficulty = (TextView)findViewById(R.id.txtDifficulty);
        txtDifficulty.setText("Level: " + difficultyLevel);

        txtWatchGo = (TextView)findViewById(R.id.txtWatchGo);

        // Buttons
        btnButton1 = (Button)findViewById(R.id.btnButton1);
        btnButton2 = (Button)findViewById(R.id.btnButton2);
        btnButton3 = (Button)findViewById(R.id.btnButton3);
        btnButton4 = (Button)findViewById(R.id.btnButton4);
        btnReplay = (Button)findViewById(R.id.btnReplay);

        // Now set all of the buttons to listen for clicks
        btnButton1.setOnClickListener(this);
        btnButton2.setOnClickListener(this);
        btnButton3.setOnClickListener(this);
        btnButton4.setOnClickListener(this);
        btnReplay.setOnClickListener(this);

        // This is the code which will define the thread
        myHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (playSequence) {
                    // All of the thread action will go here
                    // Make sure all of the buttons are visible
                    btnButton1.setVisibility(View.VISIBLE);
                    btnButton2.setVisibility(View.VISIBLE);
                    btnButton3.setVisibility(View.VISIBLE);
                    btnButton4.setVisibility(View.VISIBLE);

                    switch (sequenceToCopy[elementToPlay]) {
                        case 1:
                            // Hide a button
                            btnButton1.setVisibility(View.INVISIBLE);
                            // Play a sound
                            soundPool.play(sample1, 1, 1, 0, 0, 1);
                            break;

                        case 2:
                            // Hide a button
                            btnButton2.setVisibility(View.INVISIBLE);
                            // Play a sound
                            soundPool.play(sample2, 1, 1, 0, 0, 1);
                            break;

                        case 3:
                            // Hide a button
                            btnButton3.setVisibility(View.INVISIBLE);
                            // Play a sound
                            soundPool.play(sample3, 1, 1, 0, 0, 1);
                            break;

                        case 4:
                            // Hide a button
                            btnButton4.setVisibility(View.INVISIBLE);
                            // Play a sound
                            soundPool.play(sample4, 1, 1, 0, 0, 1);
                            break;
                    }

                    elementToPlay++;
                    if (elementToPlay == difficultyLevel) {
                        sequenceFinished();
                    }
                }

                myHandler.sendEmptyMessageDelayed(0, 900);
            }
        }; // End of thread

        myHandler.sendEmptyMessage(0);

    } // End onCreate()

    @Override
    public void onClick(View v) {

        if(!playSequence) {

            switch (v.getId()) {

                case R.id.btnButton1:
                    soundPool.play(sample1, 1, 1, 0, 0, 1);
                    checkElement(1);
                    break;

                case R.id.btnButton2:
                    soundPool.play(sample2, 1, 1, 0, 0, 1);
                    checkElement(2);
                    break;

                case R.id.btnButton3:
                    soundPool.play(sample3, 1, 1, 0, 0, 1);
                    checkElement(3);
                    break;

                case R.id.btnButton4:
                    soundPool.play(sample4, 1, 1, 0, 0, 1);
                    checkElement(4);
                    break;

                case R.id.btnReplay:
                    difficultyLevel = 3;
                    playerScore = 0;
                    txtScore.setText("Score: " + playerScore);
                    playASequence();
                    break;

            }
        }

    } // End onClick()

    public void createSequence() {
        // For choosing a random button
        Random randInt = new Random();
        int ourRandom;
        for (int i = 0; i < difficultyLevel; i++) {
            // Get a random number between 1 and 4
            ourRandom = randInt.nextInt(4);
            ourRandom++;
            // Save that number to our array
            sequenceToCopy[i] = ourRandom;

        }
    } // End createSequence()

    public void playASequence() {
        createSequence();
        isResponding = false;
        elementToPlay = 0;
        playerResponses = 0;
        txtWatchGo.setText("WATCH!");
        playSequence = true;
    } // End playASequence()

    public void sequenceFinished() {
        playSequence = false;
        // Make sure all of the button are made visible
        btnButton1.setVisibility(View.VISIBLE);
        btnButton2.setVisibility(View.VISIBLE);
        btnButton3.setVisibility(View.VISIBLE);
        btnButton4.setVisibility(View.VISIBLE);
        txtWatchGo.setText("GO!");
        isResponding = true;
    } // End sequenceFinished()

    public void checkElement(int thisElement) {
        if (isResponding) {
            playerResponses++;
            if (sequenceToCopy[playerResponses - 1] == thisElement) {
                playerScore = playerScore + ((thisElement + 1) * 2);
                txtScore.setText("Score: " + playerScore);
                if (playerResponses == difficultyLevel) {
                    isResponding = false;
                    difficultyLevel++;
                    playASequence();
                }

            } else {
                txtWatchGo.setText("FAILED!");
                isResponding = false;
            }
        }
    }
}
