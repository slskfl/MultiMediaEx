package com.example.multimediaex;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    Switch swPalyer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swPalyer=findViewById(R.id.swPalyer);
        mediaPlayer=MediaPlayer.create(this, R.raw.like);
        swPalyer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mediaPlayer.start();
                } else{
                    mediaPlayer.stop();
                }
            }
        });
    }
}