package com.example.techanimal.com.example.techanimal.androidtorch;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.support.v4.content.ContextCompat;

public class FlashlightControl {
    private boolean active = false;
    private CameraManager cameraManager;
    private String cameraId;

    private boolean hasCameraFlash;
    private boolean hasCamera;

    //function fot controlling the Flash
    public FlashlightControl(Activity activity) {
        hasCameraFlash = activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        hasCamera = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        try {
            cameraManager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            hasCameraFlash = false;
            hasCamera = false;
        }
    }

    public boolean isActive() {
        return active;
    }

    public boolean hasCameraFlash() {
        return hasCameraFlash;
    }

    public void activate() {
        try {
            cameraManager.setTorchMode(cameraId, true);
            active = true;
        } catch (CameraAccessException e) {
        }
    }

    public void deactivate() {
        try {
            cameraManager.setTorchMode(cameraId, false);
            active = false;
        } catch (CameraAccessException e) {
        }
    }


    public void ShutdownFlash() {

        try {
            deactivate();
        } catch (Exception ex) {

        }
    }
}
