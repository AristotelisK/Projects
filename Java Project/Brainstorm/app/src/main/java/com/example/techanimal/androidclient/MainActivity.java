package com.example.techanimal.androidclient;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.techanimal.androidconnectionchecking.ConnectionControl;
import com.example.techanimal.androidpublisher.MainPublisher;
import com.example.techanimal.androidsubscriber.MainSubscriber;
import com.example.techanimal.com.example.techanimal.androidtorch.FlashlightControl;
import com.example.techanimal.com.example.techanimal.mediaplayer.MediaPlayerControl;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MainActivity extends AppCompatActivity {


    private static final int CAMERA_REQUEST = 50;
    private Button buttonEnable, buttonDeact;
    private ImageView imageFlashlight;
    private ImageView imageMusic;
    private String topic = "mega";
    private int qos = 2;
    private String broker = "tcp://192.168.10.149:1883";
    private String clientId = "JavaSampleSubscriber";
    private String period = "2000";
    private MemoryPersistence persistence = new MemoryPersistence();
    public static MqttClient sampleClient = null;
    private FlashlightControl flashlightControl;
    public static MediaPlayerControl mediaPlayerControl = null;
    private MainSubscriber mainSub = null;
    private boolean clickedBackButton = false;

    private ConnectionControl connectionControl;
    //final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //---------Dialog-----------//

        connectionControl = new ConnectionControl(this);

        //---------Dialog-----------//

        //-------------------TIMER---------------------//
        /*
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isConnected(MainActivity.this)) buildDialog(MainActivity.this).show();{
                    handler.postDelayed(this,10000);
                }

            }
        },10000);*/

        //-------------------TIMER---------------------//


        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.brain_round);

        imageMusic = (ImageView) findViewById(R.id.imageMusic);
        imageFlashlight = (ImageView) findViewById(R.id.imageFlashlight);
        buttonEnable = (Button) findViewById(R.id.buttonEnable);
        buttonDeact = (Button) findViewById(R.id.buttonDeact);

        flashlightControl = new FlashlightControl(this);
        mediaPlayerControl = new MediaPlayerControl(this);

        buttonEnable.setEnabled(!flashlightControl.hasCameraFlash());
        imageFlashlight.setEnabled(flashlightControl.hasCameraFlash());

        //function for Deactivation music and torch
        buttonDeact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isactiveMedia = mediaPlayerControl.isActive();
                boolean isactiveFlash = flashlightControl.isActive();

                if (isactiveMedia || isactiveFlash) {
                    if (isactiveMedia) {
                        mediaPlayerControl.MusicOff();
                        updateMusicGUI();
                    }
                    if (isactiveFlash)
                        flashlightControl.deactivate();
                    updateFlashlightGUI();

                } else {
                    Toast.makeText(MainActivity.this, "Everything is already Deactivated", Toast.LENGTH_SHORT).show();
                }


            }
        });

        // onClick function for camera permission for the user
        buttonEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
            }
        });

        imageMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isactive = mediaPlayerControl.isActive();

                if (isactive) {
                    mediaPlayerControl.MusicOff();
                } else {
                    mediaPlayerControl.MusicOn();
                }
                updateMusicGUI();       //change image for speaker.on/off

            }
        });

        imageFlashlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isactive = flashlightControl.isActive();

                if (isactive) {
                    flashlightControl.deactivate();
                } else {
                    flashlightControl.activate();
                }
                updateFlashlightGUI();      //change switch photo for on/off
            }
        });

        connectionControl.start();
    }//End of onCreate


    public synchronized boolean subscribe() {
        try {
            if (sampleClient == null || (sampleClient!= null && !sampleClient.isConnected())) {
                sampleClient = new MqttClient(broker, clientId, persistence);
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(true);
                mainSub = new MainSubscriber(this, flashlightControl, mediaPlayerControl);
                sampleClient.setCallback(mainSub);

                Log.i("ConnectionControl", "Connecting to ..." + broker) ;
                sampleClient.connect(connOpts);
                sampleClient.subscribe(topic, qos);
                return true;
            } else {
                Log.i("MainActivity", "Subscribe skipped, already connected");
                return true;
            }
        } catch (Exception me) {
            me.printStackTrace();
            sampleClient = null;
            return false;
        }
    }

    public synchronized void resubscribe(String result) {
        try {
            if (sampleClient != null) {
                sampleClient.unsubscribe(topic);
                sampleClient.disconnect();
                sampleClient.close();
                sampleClient = null;
            }
        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, "**** unsubscribe ERROR **** " + result, Toast.LENGTH_SHORT).show();
        } finally {
            Log.i("MainActivity", "broker updated to: " + result);
            broker = "tcp://" + result;
        }
    }


    //---------------------WIFI ACCESS--------------------------------//
    //---------------------WIFI ACCESS--------------------------------//
    //---------------------WIFI ACCESS--------------------------------//


    // function for image music GUI
    public void updateMusicGUI() {
        boolean isactive = mediaPlayerControl.isActive();
        if (isactive) {
            imageMusic.setImageResource(R.drawable.musicon);
        } else {
            imageMusic.setImageResource(R.drawable.musicoff);
        }
    }

    //function for image switch GUI
    public void updateFlashlightGUI() {
        boolean isactive = flashlightControl.isActive();
        if (isactive) {
            imageFlashlight.setImageResource(R.drawable.fon);
        } else {
            imageFlashlight.setImageResource(R.drawable.foff);
        }
    }


    //function for user permission to camera access
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    buttonEnable.setEnabled(false);
                    buttonEnable.setText("Camera Enabled");
                    imageFlashlight.setEnabled(true);
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied for the Camera", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    //function for exit application after double press
    @Override
    public void onBackPressed() {

        if (!clickedBackButton) {
            Toast.makeText(this, "Press Back Button again to Exit", Toast.LENGTH_LONG).show();
            clickedBackButton = true;
        } else {
            // android.os.Process.killProcess(android.os.Process.myPid());
            // System.exit(0);
            finish();
        }
        new CountDownTimer(3000, 1000) {        //click limit 3seconds
            @Override
            public void onTick(long msFinish) {
            }

            @Override
            public void onFinish() {
                clickedBackButton = false;
            }

        }.start();          //start the countdown
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.popup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();      //method for getting id by press

        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);    //go to SettingsActivity after Settings pressed with result method
            startActivityForResult(intent, 1);
        }

        if (id == R.id.action_exit) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Exit Application!")
                    .setMessage("Are you sure you want to Exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            // android.os.Process.killProcess(android.os.Process.myPid());             //kill the application after Exit pressed
                            //System.exit(0);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();


        }

        return super.onOptionsItemSelected(item);
    }

    //function for get Intent results after the user changed IP broker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("broker");          //new IP getting as a String
                if (result != null && !result.isEmpty() && !result.equals(broker) && !result.equals("tcp://")) {             //checking broker  if the IP has changed
                    resubscribe(result);
                }

                period = data.getStringExtra("period");          //new period getting as a String

                if (!period.isEmpty()) {
                    MainPublisher mp = new MainPublisher(broker, "changeperiod " + period);
                    mp.start();
                }
                return;
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
        Toast.makeText(MainActivity.this, "Activity.RESULT OTHER... ", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sampleClient != null) {
            try {
                sampleClient.unsubscribe("mega");
                sampleClient.close();
            } catch (Exception ex) {

            }
        }

        try {
            mediaPlayerControl.ShutdownMusic();
        } catch (Exception ex) {

        }

        try {
            flashlightControl.ShutdownFlash();
        } catch (Exception ex) {

        }

        connectionControl.interrupt();
    }
}
