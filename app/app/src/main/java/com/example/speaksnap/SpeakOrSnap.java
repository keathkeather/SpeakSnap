package com.example.speaksnap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SpeakOrSnap extends AppCompatActivity implements View.OnClickListener{

    Button btn1, btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak_or_snap);

        btn1 = (Button) findViewById(R.id.speakButton);
        btn2 = (Button) findViewById(R.id.snapButton);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.speakButton:
                Toast.makeText(this, "Speak!", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(this, Speak.class);
                startActivity(intent1);
                break;

        }
    }
}
