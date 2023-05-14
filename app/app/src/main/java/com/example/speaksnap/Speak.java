package com.example.speaksnap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Speak extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak);

        Button backbtn = findViewById(R.id.backBtn);
        backbtn.setOnClickListener(view -> openSpeakOrSnap());
    }

    public void openSpeakOrSnap(){
        Intent intent = new Intent(this, SpeakOrSnap.class);
        startActivity(intent);
    }
}
