package com.bishoyosama.x_ogame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;

import static com.bishoyosama.x_ogame.R.drawable.ic_iconmonstr_x_mark_8;

public class PlayingActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    TextView p1, p2, p1score, p2score;
    String p1name, p2name;
    Button[][] cells = new Button[3][3];
    boolean player1Turn = true;
    int roundCount;
    byte p1s = 0, p2s = 0;
    TextToSpeech tts;
    ImageView soundIcon;
    boolean soundOn;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
        p1 = findViewById(R.id.p1);
        p2 = findViewById(R.id.p2);
        p1score = findViewById(R.id.p1score);
        p1score.setText(p1s + "");
        p2score = findViewById(R.id.p2score);
        p2score.setText(p2s + "");
        Intent intent = getIntent();
        p1name = intent.getStringExtra("p1name");
        p2name = intent.getStringExtra("p2name");
        p1.setText(p1name);
        p2.setText(p2name);
        tts=new TextToSpeech(this,this);
        soundIcon=findViewById(R.id.soundIcon);
        pref = getPreferences(MODE_PRIVATE);
        soundOn = pref.getBoolean("soundOn",false);
        if (soundOn == false){
            soundIcon.setImageResource(R.drawable.ic_sound_off);

        }
        else{
            soundIcon.setImageResource(R.drawable.ic_sound_on);
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "r" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                cells[i][j] = findViewById(resID);
            }
        }

    }

    @Override
    protected void onDestroy() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("soundOn", soundOn);
        editor.apply();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        Intent in = new Intent(this, playersActivity.class);
        in.putExtra("p1name",p1name);
        in.putExtra("p2name",p2name );
        in.putExtra("soundOn", soundOn);
        p1s = 0;
        p2s = 0;
        updatePointsText();
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("soundOn", soundOn);
        editor.apply();
        startActivity(in);
        super.onBackPressed();
    }

    public void click(View view) {
        if (!((Button) view).getText().toString().equals("")) {
            return;
        }
        if (player1Turn) {
            ((Button) view).setText("X");

        } else {
            ((Button) view).setText("O");

        }
        roundCount++;
        if (checkForWin()) {
            if (player1Turn) {
                player1Wins();
            } else {
                player2Wins();
            }
        } else if (roundCount == 9) {
            drow();
        } else {
            player1Turn = !player1Turn;
        }

    }

    boolean checkForWin() {
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = cells[i][j].getText().toString();
            }
        }
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }
        }
        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                return true;
            }
        }
        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }
        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }
        return false;
    }

    void player1Wins() {
        p1s++;
        Toast.makeText(this, p1name + " wins!", Toast.LENGTH_SHORT).show();
        if (soundOn)
            tts.speak(p1name+" wins!",TextToSpeech.QUEUE_FLUSH,null,null);
        YoYo.with(Techniques.RotateIn).duration(1500).playOn(p1score);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j].setClickable(false);
            }
        }
        updatePointsText();

    }

    void player2Wins() {
        p2s++;
        Toast.makeText(this, p2name + " wins!", Toast.LENGTH_SHORT).show();
        if (soundOn)
            tts.speak(p2name+" wins!",TextToSpeech.QUEUE_FLUSH,null,null);
        YoYo.with(Techniques.RotateIn).duration(1500).playOn(p2score);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j].setClickable(false);
            }
        }
        updatePointsText();
    }

    private void drow() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        if (soundOn)
            tts.speak("Two Players Is Draw!",TextToSpeech.QUEUE_FLUSH,null,null);
        YoYo.with(Techniques.Shake).duration(1500).playOn(p1score);
        YoYo.with(Techniques.Shake).duration(1500).playOn(p2score);
    }

    void updatePointsText() {
        p1score.setText(p1s + "");
        p2score.setText(p2s + "");
    }

    public void playAgain(View view) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j].setText("");
                cells[i][j].setClickable(true);
            }
        }
        roundCount = 0;
        player1Turn = true;

    }

    public void newGame(View view) {
        Intent in = new Intent(this, playersActivity.class);
        in.putExtra("p1name",p1name);
        in.putExtra("p2name",p2name );
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("soundOn", soundOn);
        editor.apply();
        p1s = 0;
        p2s = 0;
        updatePointsText();
        startActivity(in);

    }

    @Override
    public void onInit(int status) {
        if (status==TextToSpeech.SUCCESS){
            tts.setSpeechRate(0.7f);
            tts.setPitch(0.7f);

        }else{
            Toast.makeText(this, "feature not supported", Toast.LENGTH_SHORT).show();
        }
    }

    public void changeSound(View view) {
        if (soundOn){
            soundOn=false;
            soundIcon.setImageResource(R.drawable.ic_sound_off);
        }else{
            soundOn=true;
            soundIcon.setImageResource(R.drawable.ic_sound_on);
        }
    }
}