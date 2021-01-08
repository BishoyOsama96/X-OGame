package com.bishoyosama.x_ogame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class playersActivity extends AppCompatActivity {
    EditText player_1_Name;
    EditText player_2_Name;
    String p1name, p2name;
    ImageView x_Img, o_Img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        x_Img = findViewById(R.id.x_Img);
        o_Img = findViewById(R.id.o_Img);
        player_1_Name = findViewById(R.id.player_1_Name);
        player_2_Name = findViewById(R.id.player_2_Name);
        Intent intent = getIntent();
        p1name = intent.getStringExtra("p1name");
        p2name = intent.getStringExtra("p2name");
        player_1_Name.setText(p1name);
        player_2_Name.setText(p2name);
        o_Img.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {

        finishAffinity();
        super.onBackPressed();

    }

    @Override
    protected void onStart() {
        super.onStart();
        YoYo.with(Techniques.RollIn).duration(1500).onEnd(animator -> {
            o_Img.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.ZoomIn).duration(1500).playOn(o_Img);
        }).playOn(x_Img);
    }

    public void Start(View view) {
        if (!player_1_Name.getText().toString().isEmpty() &&
                !player_2_Name.getText().toString().isEmpty() && !player_2_Name.getText().toString().equals(player_1_Name.getText().toString())) {

            Intent in = new Intent(this, PlayingActivity.class);
            p1name = player_1_Name.getText().toString();
            p2name = player_2_Name.getText().toString();
            in.putExtra("p1name", p1name);
            in.putExtra("p2name", p2name);
            startActivity(in);
        } else {
            if (player_1_Name.getText().toString().isEmpty())
                YoYo.with(Techniques.Shake).duration(500).repeat(1).playOn(player_1_Name);
            if (player_2_Name.getText().toString().isEmpty())
                YoYo.with(Techniques.Shake).duration(500).repeat(1).playOn(player_2_Name);
            if (player_2_Name.getText().toString().equals(player_1_Name.getText().toString()))
                YoYo.with(Techniques.Shake).duration(500).repeat(1).playOn(player_2_Name);
        }

    }
}