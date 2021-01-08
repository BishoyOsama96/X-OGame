package com.bishoyosama.x_ogame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class StartingActivity extends AppCompatActivity {
    Button playtbtn;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playtbtn = findViewById(R.id.playbtn);
        logo= findViewById(R.id.logo);

    }

    @Override
    protected void onStart() {
        super.onStart();
        YoYo.AnimationComposer with = YoYo.with(Techniques.ZoomIn);
        with.duration(1500);
        with.onEnd(animator ->{ YoYo.with(Techniques.Shake).duration(500).playOn(logo);
            playtbtn.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.BounceIn).duration(500).playOn(playtbtn);});
        with.playOn(logo);
    }

    public void play(View view) {
        Intent in=new Intent(this,playersActivity.class);
        startActivity(in);
    }
}