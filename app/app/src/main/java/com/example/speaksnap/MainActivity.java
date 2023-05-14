package com.example.speaksnap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button btn1, btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button) findViewById(R.id.loginButton);
        btn2 = (Button) findViewById(R.id.signupButton);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.loginButton:
                Toast.makeText(this, "Sucessfully logged in!", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(this, SpeakOrSnap.class);
                startActivity(intent1);
                break;
            case R.id.signupButton:
                Toast.makeText(this, "Exercise 1 button is clicked!", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(this, SignUp.class);
                startActivity(intent2);
                break;
        }
    }
}