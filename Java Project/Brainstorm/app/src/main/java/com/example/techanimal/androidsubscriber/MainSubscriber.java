package com.example.techanimal.androidsubscriber;

import android.util.Log;
import android.widget.Toast;

import com.example.techanimal.androidclient.MainActivity;
import com.example.techanimal.com.example.techanimal.androidtorch.FlashlightControl;
import com.example.techanimal.com.example.techanimal.mediaplayer.MediaPlayerControl;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class MainSubscriber implements MqttCallback {
    private MainActivity mainActivity;
    private FlashlightControl flashlightControl;
    private MediaPlayerControl mpc;

    public MainSubscriber(MainActivity mainActivity, FlashlightControl flashlightControl, MediaPlayerControl mpc) {
        this.mainActivity = mainActivity;
        this.flashlightControl = flashlightControl;
        this.mpc = mpc;
    }


    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        final String m = new String(message.getPayload());

        Log.i("BATMAN", "Message received: " + m);

        if (m.startsWith("audioplay")) {
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String token = null;
                    if (m.split(" ").length > 1) {
                        token = m.split(" ")[1];

                        if (token.equals("1")) {
                            mpc.switchToCat();
                        }
                        if (token.equals("2")) {
                            mpc.switchToCow();
                        }
                        if (token.equals("3")) {
                            mpc.switchToPuppy();
                        }
                    }

                    if (mpc.isActive()) {
                        Toast.makeText(mainActivity, "music is already active", Toast.LENGTH_SHORT).show();
                    } else {
                        mpc.MusicOn();
                        mainActivity.updateMusicGUI();
                    }
                }
            });

        }
        if (m.startsWith("audiostop")) {
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!mpc.isActive()) {
                        Toast.makeText(mainActivity, "music is already not active", Toast.LENGTH_SHORT).show();
                    } else {
                        mpc.MusicOff();
                        mainActivity.updateMusicGUI();
                    }
                }
            });
        }
        if (m.startsWith("torchon")) {
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (flashlightControl.isActive()) {
                        Toast.makeText(mainActivity, "torch is already active", Toast.LENGTH_SHORT).show();
                    } else {
                        flashlightControl.activate();
                        mainActivity.updateFlashlightGUI();
                    }
                }
            });
        }
        if (m.startsWith("torchoff")) {
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!flashlightControl.isActive()) {
                        Toast.makeText(mainActivity, "torch  is already not active", Toast.LENGTH_SHORT).show();
                    } else {
                        flashlightControl.deactivate();
                        mainActivity.updateFlashlightGUI();
                    }
                }
            });
        }
        if (m.startsWith("on")) {
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (flashlightControl.isActive()) {
                        Toast.makeText(mainActivity, "both already active", Toast.LENGTH_SHORT).show();
                    } else {
                        flashlightControl.activate();
                        mainActivity.updateFlashlightGUI();
                        mpc.MusicOn();
                        mainActivity.updateMusicGUI();
                    }
                }
            });
        }

        if (m.startsWith("off")) {
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!flashlightControl.isActive()) {
                        Toast.makeText(mainActivity, "both not active", Toast.LENGTH_SHORT).show();
                    } else {
                        flashlightControl.deactivate();
                        mainActivity.updateFlashlightGUI();
                        mpc.MusicOff();
                        mainActivity.updateMusicGUI();
                    }
                }
            });
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
