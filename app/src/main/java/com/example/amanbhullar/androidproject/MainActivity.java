package com.example.amanbhullar.androidproject;



import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onTrackerButtonClicked(View view){
        Intent intent = new Intent(this, TrackerActivity.class);

        startActivity(intent);
    }

    public void onPlayerButtonClicked(View view){
        Intent intent = new Intent(this, PlayersViewActivity.class);

        startActivity(intent);
    }


}
