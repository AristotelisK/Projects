package com.example.techanimal.androidconnectionchecking;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;

import com.example.techanimal.androidclient.MainActivity;
import com.example.techanimal.com.example.techanimal.androidtorch.FlashlightControl;
import com.example.techanimal.com.example.techanimal.mediaplayer.MediaPlayerControl;

/**
 * Created by milten on 18/12/2017.
 */

//---------------------WIFI ACCESS--------------------------------//
//---------------------WIFI ACCESS--------------------------------//
//http://androidmkab.com/2017/01/24/show-no-internet-connection-dialog-android
//---------------------WIFI ACCESS--------------------------------//


public class ConnectionControl extends Thread {
    private final int CHECKING_PERIOD = 3000;
    private MainActivity mainActivity;
    private boolean showDialog = false;

    public ConnectionControl(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    @Override
    public void run() {
        super.run();

        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(CHECKING_PERIOD);
            } catch (InterruptedException e) {
                return;
            }

            Log.i("ConnectionControl", "Tick");

            if (!isConnected(mainActivity) && showDialog == false) {
                showDialog = true;

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buildDialogData(mainActivity).show();
                    }
                });
            }

            if (isConnected(mainActivity)) {
                Log.i("ConnectionControl", "Try to subscribe!!! ");
                if (mainActivity.subscribe()) {
                    showDialog = false;
                }
            }
        }


    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialogData(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("Mobile Data is Turned Off!");
        builder.setMessage("Turn on mobile data or use Wi-Fi to connect.");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }

        });

        builder.setNegativeButton("Settings", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mainActivity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }

        });

        return builder;
    }

}
