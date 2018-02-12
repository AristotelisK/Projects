package com.example.techanimal.com.example.techanimal.mediaplayer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.techanimal.androidclient.MainActivity;
import com.example.techanimal.androidclient.R;


public class MediaPlayerControl {
    private boolean active = false;
    private MediaPlayer activeMediaPlayer;
    private MediaPlayer mediaPlayer1;
    private MediaPlayer mediaPlayer2;
    private MediaPlayer mediaPlayer3;
    private int selectedSound = 1;

    //create the mediaPlayers for each sound and the Looping
    public MediaPlayerControl(Activity activity) {
        mediaPlayer1 = MediaPlayer.create(activity, R.raw.catsound);
        mediaPlayer2 = MediaPlayer.create(activity, R.raw.cow);
        mediaPlayer3 = MediaPlayer.create(activity, R.raw.puppy);

        mediaPlayer1.setLooping(true);
        mediaPlayer2.setLooping(true);
        mediaPlayer3.setLooping(true);

        activeMediaPlayer = null;
    }

    public boolean isActive() {
        return active;
    }   //boolean for checking if the music is on or off


    //function for Music on
    public void MusicOn() {
        if (active == false) {              //if music off  please select one of the choices below

            switch (selectedSound) {
                case 1:
                    activeMediaPlayer = mediaPlayer1;
                    break;
                case 2:
                    activeMediaPlayer = mediaPlayer2;
                    break;
                case 3:
                    activeMediaPlayer = mediaPlayer3;
                    break;
            }

            activeMediaPlayer.start();      //starts the sound of what you choose above
            active = true;                  //now the boolean is true that means that the music is on
        }
    }

    //function tha stops the music
    public void MusicOff() {
        if (active == true) {
            activeMediaPlayer.pause();
            active = false;             //Set callback
        }
    }

    //function thas restarts the music
    public void Restart() {
        MusicOff();
        MusicOn();
    }


    //function that change and do Restart of the sound for the new selected sound=Cat
    public void switchToCat() {
        selectedSound = 1;
        if (active == true) {
            Restart();
        }
    }

    //function that change and do Restart of the sound for the new selected sound=Cow
    public void switchToCow() {
        selectedSound = 2;
        if (active == true) {
            Restart();
        }
    }

    //function that change and do Restart of the sound for the new selected sound=Puppy
    public void switchToPuppy() {
        selectedSound = 3;
        if (active == true) {
            Restart();
        }
    }

    public void ShutdownMusic() {
        MusicOff();
        mediaPlayer1.release();
        mediaPlayer2.release();
        mediaPlayer3.release();
    }
}
