package com.example.speaksnapfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button btn1, btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        btn1 = (Button) findViewById(R.id.speakButton);
        btn2 = (Button) findViewById(R.id.snapButton);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.snapButton:
                Toast.makeText(this, "Snap!", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(this, SnapActivity.class);
                startActivity(intent1);
                break;

        }
    }
}