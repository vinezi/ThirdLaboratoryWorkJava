package com.politov.third_laboratory_work;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TextActivity extends AppCompatActivity {

    ImageButton openText, saveText;
    EditText editText;
    private final static String FILE_NAME = "document.txt";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(TextActivity.this, MainActivity.class));
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        init();
        openText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openText();
            }
        });
        saveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveText();
            }
        });
    }

    private void init() {
        openText = findViewById(R.id.btnTextIntent);
        saveText = findViewById(R.id.btnSaveText);
        editText = findViewById(R.id.editText);
    }

    private File getExternalPath() {
        return new File(getExternalFilesDir(null), FILE_NAME);
    }
    public void saveText(){

        FileOutputStream fos = null;
        try {
            String text = editText.getText().toString();
            fos = new FileOutputStream(getExternalPath());
            fos.write(text.getBytes());
            Toast.makeText(this, R.string.text_saved, Toast.LENGTH_SHORT).show();
        }
        catch(IOException ex) {

            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{
            try{
                if(fos!=null)
                    fos.close();
            }
            catch(IOException ex){

                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void openText(){
        FileInputStream fin = null;
        File file = getExternalPath();
        if(!file.exists()) return;
        try {
            fin =  new FileInputStream(file);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String (bytes);
            editText.setText(text);
        }
        catch(IOException ex) {

            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{
            try{
                if(fin!=null)
                    fin.close();
            }
            catch(IOException ex){

                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveText();
    }
}