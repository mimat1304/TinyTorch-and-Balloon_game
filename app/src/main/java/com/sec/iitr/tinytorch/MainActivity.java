package com.sec.iitr.tinytorch;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    public static boolean isEvil;
    public static String version;
    public static int sessionID;
    public static int maliciousType;
    //HashMap<String,String> contcats_List = new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        sessionID=sharedPref.getInt("loadSessionID",0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("loadSessionID",  sessionID+1);
        editor.commit();
        /*contcats_List.put("name", "phoneNumber");
        Intent intent = new
                Intent(this, BackgroundService.class);
        intent.putExtra("CONTACT_LIST", contcats_List);

        startService(intent);*/

        isEvil = false;
        version = "17";

        Clues myclues=new Clues();
        myclues.SendLog("App Mode Change","App entered onCreate()","Benign");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");

            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            }
        }

        ToggleButton toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton);
        toggleButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                        String cameraId = null; // Usually back camera is at 0 position.
                        try {

                            CameraCharacteristics cameraCharacteristics = camManager.getCameraCharacteristics(camManager.getCameraIdList()[0]);

                            cameraId = camManager.getCameraIdList()[0];
                            camManager.setTorchMode(cameraId, true);   //Turn ON
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                        String cameraId = null; // Usually back camera is at 0 position.
                        try {

                            CameraCharacteristics cameraCharacteristics = camManager.getCameraCharacteristics(camManager.getCameraIdList()[0]);

                            cameraId = camManager.getCameraIdList()[0];
                            camManager.setTorchMode(cameraId, false);
                            ;
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
        });
    }
}
