package com.example.speaksnapfinal;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;


public class SnapActivity extends AppCompatActivity {

    Button snapButton,copyButton;
    EditText edit_text;
    Uri imageUri;
    TextRecognizer textRecognizer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_snap);

        snapButton = findViewById(R.id.snapButton);
        copyButton = findViewById(R.id.copyButton);
        edit_text  = findViewById(R.id.edit_text);
        Button backbtn = findViewById(R.id.backBtn);
        backbtn.setOnClickListener(view -> backToMain());
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        snapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(SnapActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = edit_text.getText().toString();

                if(text.isEmpty()){
                    Toast.makeText(SnapActivity.this,"There is no text to copy",Toast.LENGTH_SHORT).show();
                }
                else{
                    ClipboardManager cliboardManager = (ClipboardManager)getSystemService(SnapActivity.this.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("Data",edit_text.getText().toString());
                    cliboardManager.setPrimaryClip(clipData);
                    Toast.makeText(SnapActivity.this,"Text Copied to ClipBord",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode , int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);


        if(resultCode == Activity.RESULT_OK){
            if(data!=null){
                imageUri = data.getData();
                Toast.makeText(this, "image selected",Toast.LENGTH_SHORT).show();
                recognizeText();
            }
        }
        else{
            Toast.makeText(this, "image not selected",Toast.LENGTH_SHORT).show();
        }

    }
    private void recognizeText() {
        if(imageUri!=null){
            try {
                InputImage inputImage = InputImage.fromFilePath(SnapActivity.this,imageUri);


                Task<Text> result = textRecognizer.process(inputImage)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text text) {
                                String recognizeText = text.getText();
                                edit_text.setText(recognizeText);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SnapActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public void backToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
