package com.example.sueca;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.ybq.android.spinkit.SpinKitView;

public class SecondActivity extends AppCompatActivity {
    GameView a;
    SpinKitView spin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this));


    }


}
