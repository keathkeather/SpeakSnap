package com.example.speaksnapfinal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;
public class SpeakActivity extends AppCompatActivity {

    Spinner fromSpinner,toSpinner;
    TextInputEditText EditSource;
    ImageView VoiceMic,soundBtn;
    MaterialButton btnTranslate;
    EditText idTVTranslatedLanguage;
    Button btnBack;
    String[] fromLanguage = {"From","English","Afrikaans","Arabic","Belarusian","Bengali","Catalan","Czech","Welsh","Hindi","Urdu"};
    String[] toLanguage = {"To","English","Afrikaans","Arabic","Belarusian","Bengali","Catalan","Czech","Welsh","Hindi","Urdu"};
    private static final int REQUEST_PERMISSION_CODE =1;
    String languageCode,fromLanguageCode,toLanguageCode;
    TextToSpeech textToSpeech;

    private ActivityResultLauncher<Intent> speechRecognitionLauncher;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_speak);

        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        EditSource = findViewById(R.id.EditSource);
        VoiceMic = findViewById(R.id.VoiceMic);
        btnTranslate = findViewById(R.id.btnTranslate);
        idTVTranslatedLanguage = findViewById(R.id.idTVTranslatedLanguage);
        btnBack = findViewById(R.id.backBtn);
        soundBtn = findViewById(R.id.soundBtn);
        btnBack.setOnClickListener(view -> backToMain());
        speechRecognitionLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        handleSpeechRecognitionResult(result);
                    }
                }
        );
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromLanguageCode = getLanguageCode(fromLanguage[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter fromAdapter = new ArrayAdapter(this,R.layout.spinner_item,fromLanguage);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);


        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toLanguageCode = getLanguageCode(toLanguage[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter toAdapter = new ArrayAdapter(this,R.layout.spinner_item,toLanguage);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);

        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(TextUtils.isEmpty(EditSource.getText().toString())){
                    Toast.makeText(SpeakActivity.this, "Enter Text to Translate", Toast.LENGTH_SHORT).show();
                }
                else{
                    TranslatorOptions options = new TranslatorOptions.Builder()
                            .setTargetLanguage(toLanguageCode)
                            .setSourceLanguage(fromLanguageCode).build();

                    Translator translator = Translation.getClient(options);


                    String sourceText = EditSource.getText().toString();

                    //Download progress dialog
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SpeakActivity.this);
                    alertDialogBuilder.setMessage("Downloading the Translation Model...");
                    alertDialogBuilder.setCancelable(false);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    translator.downloadModelIfNeeded().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            alertDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            alertDialog.dismiss();
                        }
                    });



                    Task<String> result = translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            idTVTranslatedLanguage.setText(s);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SpeakActivity.this,"Model Not Found" , Toast.LENGTH_SHORT).show(); //e.getMessage().toString()
                        }
                    });
                }

                // Hide the keyboard when translate button is clicked
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
         });
        VoiceMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Speak(v);
            }
        });
        textToSpeech = new TextToSpeech(SpeakActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    // TextToSpeech engine initialization successful
                    int result = textToSpeech.setLanguage(Locale.getDefault());
                    //int result = textToSpeech.setLanguage(new Locale(" "));
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(SpeakActivity.this, "Language not supported", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // TextToSpeech engine initialization failed
                    Toast.makeText(SpeakActivity.this, "TextToSpeech engine initialization failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        soundBtn.setOnClickListener(v -> {
            String text = idTVTranslatedLanguage.getText().toString();
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        });
    }

    public String getLanguageCode(String language) {
        switch(language){

            case"Afrikaans":
                languageCode = TranslateLanguage.AFRIKAANS;
                break;
            case"Arabic":
                languageCode = TranslateLanguage.ARABIC;
                break;
            case"Belarusian":
                languageCode = TranslateLanguage.BELARUSIAN;
                break;
            case"Bulgarian":
                languageCode = TranslateLanguage.BULGARIAN;
                break;
            case"Bengali":
                languageCode = TranslateLanguage.BENGALI;
                break;
            case"Chinese":
                languageCode = TranslateLanguage.CHINESE;
                break;
            case"Czech":
                languageCode = TranslateLanguage.CZECH;
                break;
            case"English":
                languageCode = TranslateLanguage.ENGLISH;
                break;
            case"French":
                languageCode = TranslateLanguage.FRENCH;
                break;
            case"German":
                languageCode = TranslateLanguage.GERMAN;
                break;
            case"Gujarati":
                languageCode = TranslateLanguage.GUJARATI;
                break;
            case"Hindi":
                languageCode = TranslateLanguage.HINDI;
                break;
            case"Japanese":
                languageCode = TranslateLanguage.JAPANESE;
                break;
            case"Kannada":
                languageCode = TranslateLanguage.KANNADA;
                break;
            case"Marathi":
                languageCode = TranslateLanguage.MARATHI;
                break;
            case"Russian":
                languageCode = TranslateLanguage.RUSSIAN;
                break;
            case"Tamil":
                languageCode = TranslateLanguage.TAMIL;
                break;
            case"Telugu":
                languageCode = TranslateLanguage.TELUGU;
                break;
            case"Urdu":
                languageCode = TranslateLanguage.URDU;
                break;

            default:
                languageCode="";
        }
        return languageCode;
        }

    private void handleSpeechRecognitionResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            ArrayList<String> matches = result.getData().getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS
            );

            // Get the first match from the list of recognized speech
            if (!matches.isEmpty()) {
                String speechText = matches.get(0);
                EditSource.setText(speechText);
            }
        }
    }


    private void Speak(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, fromLanguageCode);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Listening...");

        if (intent.resolveActivity(getPackageManager()) != null) {
            speechRecognitionLauncher.launch(intent);
        } else {
            Toast.makeText(this, "Your device does not support speech recognition.", Toast.LENGTH_LONG).show();
        }
    }
    public void backToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}


