package com.backtolife.survey.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class RecordPermissions {
    private static final int PERMISSIONS_REQUEST_MICROPHONE_ACCESS = 0;

    public void askPermissions(final Activity activity, Context context, Context application) {

        if (hasPermissions(application)) {
            System.out.println("has permissions already");
            return;
        }
        System.out.println("asking permissions");
        Log.i(this.getClass().getSimpleName(), "Microphone permissions not granted");
        /* Code to handle getting microphone permissions on Android 6.0+ */
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)) {
                new android.app.AlertDialog.Builder(context)
                        .setTitle("Requesting Microphone Permissions")
                        .setMessage("In a moment " + application.getString(application.getApplicationInfo().labelRes) + " will request permission to access your microphone. Microphone access is used only to listen for high-frequency data tones that are used to unlock extra content and improve your experience while using the app. Your data is only processed locally on this device, and never saved on or uploaded to a server")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_MICROPHONE_ACCESS);
                                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.BLUETOOTH}, PERMISSIONS_REQUEST_MICROPHONE_ACCESS);
                                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, PERMISSIONS_REQUEST_MICROPHONE_ACCESS);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_MICROPHONE_ACCESS);
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.BLUETOOTH}, PERMISSIONS_REQUEST_MICROPHONE_ACCESS);
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, PERMISSIONS_REQUEST_MICROPHONE_ACCESS);
            }
        }
    }

    public boolean hasPermissions(Context application) {
        boolean hasRecordPermissions = ContextCompat.checkSelfPermission(application, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
        boolean hasBLEPermissions1 = ContextCompat.checkSelfPermission(application, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED;
        boolean hasBLEPermissions2 = ContextCompat.checkSelfPermission(application, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED;
        boolean granted = hasBLEPermissions1 && hasBLEPermissions2 && hasRecordPermissions;
        System.out.println("granted=" + granted);
        return granted;
    }
}
