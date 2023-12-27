package com.sec.iitr.balloon_game;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class Screenshort extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenshort);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        byte[] bytes = intent.getByteArrayExtra("BitmapImage");
        Bitmap bitmap =  BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        ImageView screen=(ImageView)findViewById(R.id.screenshot);
        screen.setImageBitmap(bitmap);
    }
}
