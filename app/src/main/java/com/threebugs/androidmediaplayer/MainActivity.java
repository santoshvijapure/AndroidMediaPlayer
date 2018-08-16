package com.threebugs.androidmediaplayer;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    private double startTime = 0;
    private double finalTime = 0;
    private MediaPlayer mediaPlayer;
    private int forwardTime = 5000;
    private int backwardTime = 5000;

    TextView startTimeTextView;
    TextView stopTimeTextView;
    TextView songNameTextView;
    SeekBar seekBar;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer = MediaPlayer.create(this, R.raw.song);

        handler = new Handler();

        //Init buttons
        final Button pauseButton = (Button) findViewById(R.id.pause_button);
        final Button backButton = (Button) findViewById(R.id.back_button);
        Button forwardButton = (Button) findViewById(R.id.forward_button);
        Button backwardButton = (Button) findViewById(R.id.backward_button);

        //Init TextViews
        startTimeTextView = (TextView) findViewById(R.id.start_time);
        stopTimeTextView = (TextView) findViewById(R.id.end_time);
        songNameTextView= (TextView) findViewById(R.id.song_name);

        //Init seekbar
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setClickable(false);

        //Init image
        ImageView coverPhoto = (ImageView) findViewById(R.id.cover_image);

        songNameTextView.setText("Songs.mp3");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "Playing Song", Toast.LENGTH_SHORT).show();
                startTime = mediaPlayer.getCurrentPosition();
                finalTime = mediaPlayer.getDuration();

                seekBar.setMax((int) finalTime);

                mediaPlayer.start();

                startTimeTextView.setText(String.format("%d : %d", TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))));

                stopTimeTextView.setText(String.format("%d : %d",TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))));

                seekBar.setProgress((int) startTime);
                backButton.setEnabled(false);
                pauseButton.setEnabled(true);

                handler.postDelayed(UpdateSongTime,100);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                Toast.makeText(MainActivity.this, "Paused", Toast.LENGTH_SHORT).show();
                pauseButton.setEnabled(false);
                backButton.setEnabled(true);
            }
        });

        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int)startTime;

                if((temp+forwardTime)<=finalTime){
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(),"You have Jumped forward 5 seconds",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Cannot jump forward 5 seconds",Toast.LENGTH_SHORT).show();
                }
            }
        });

        backwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int)startTime;

                if((temp-backwardTime)>0){
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(),"You have Jumped backward 5 seconds",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Cannot jump backward 5 seconds",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            startTimeTextView.setText(String.format("%d : %d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekBar.setProgress((int)startTime);
            handler.postDelayed(this, 100);
        }
    };





}
