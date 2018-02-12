package com.example.techanimal.androidclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.techanimal.androidpublisher.MainPublisher;


public class SettingsActivity extends AppCompatActivity {

    private SeekBar volumeBar = null;
    private AudioManager audioManager = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.brain_round);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        volumeControl();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void cat(View view) {
        if (MainActivity.mediaPlayerControl != null) {
            MainActivity.mediaPlayerControl.switchToCat();
        }
        Toast.makeText(this, "You selected cat ", Toast.LENGTH_LONG).show();
    }

    public void cow(View view) {
        if (MainActivity.mediaPlayerControl != null) {
            MainActivity.mediaPlayerControl.switchToCow();
        }
        Toast.makeText(this, "You selected cow ", Toast.LENGTH_LONG).show();
    }

    public void puppy(View view) {
        if (MainActivity.mediaPlayerControl != null) {
            MainActivity.mediaPlayerControl.switchToPuppy();
        }
        Toast.makeText(this, "You selected puppy ", Toast.LENGTH_LONG).show();
    }


    private void volumeControl() {
        try {

            volumeBar = (SeekBar) findViewById(R.id.volumeBar);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

            volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int volume, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            volume, 0);
                }
            });
        } catch (Exception e) {
        }


    }

    public void clickApplyChanges(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        String broker = editText.getText().toString();

        EditText editTextP = (EditText) findViewById(R.id.editTextPeriod);
        String period = editTextP.getText().toString();
        Intent returnIntent = new Intent();


        //----------------NEW--------------//
        //----------------NEW--------------//
        if((period.matches("[1-9]+")) && (period.length() < 2) && (!period.isEmpty())){
            returnIntent.putExtra("broker", broker);
            returnIntent.putExtra("period", period);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }else{
            buildDialogPeriod(SettingsActivity.this).show();
        }

    }

    public AlertDialog.Builder buildDialogPeriod(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("Invalid Period!");
        builder.setMessage("Please Select Period Between 1sec - 9sec!");


        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }

        });

        return builder;
    }
    //----------------NEW--------------//
    //----------------NEW--------------//


}

