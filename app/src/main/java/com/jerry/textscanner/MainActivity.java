package com.jerry.textscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    private Button buttonCopy, shareB;
    private TextView myTextView;
    ClipboardManager clipboardManager;
    ClipData clipData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       Button scanButton = findViewById(R.id.scanBtn);
        myTextView = findViewById(R.id.textResult);
        buttonCopy = findViewById(R.id.copyB);
        shareB = findViewById(R.id.shareB);
        clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);




        buttonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyScannedText();
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, TextProcessing.class);
                startActivity(myIntent);
                 /*
    slide in transition
     */
                overridePendingTransition(R.transition.slide_in_right,R.transition.slide_out_left);

            }
        });

        shareB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareScannedFile();
            }
        });


       String myResult = getIntent().getStringExtra("scanResult");

       myTextView.setText(myResult);

    }

    private void shareScannedFile() {

      Intent shareIntent = new Intent();
      shareIntent.setAction(Intent.ACTION_SEND);
      shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Scanned Text");
      shareIntent.putExtra(Intent.EXTRA_TEXT,myTextView.getText());
      shareIntent.setType("text/plain");
      startActivity(shareIntent);
    }

    private void copyScannedText() {

        if(myTextView.getText().equals("")){
            Toast.makeText(getApplicationContext(), "No Text To Copy", Toast.LENGTH_SHORT).show();
        }else {
            String str;
            str = myTextView.getText().toString();
            clipData = ClipData.newPlainText("text", str);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(getApplicationContext(), "Text Copied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.copy:
                copyScannedText();
                break;
            case R.id.about:
               Intent myIntent = new Intent(MainActivity.this,AboutTextScanner.class);
               startActivity(myIntent);
               break;

        }
        return super.onOptionsItemSelected(item);
    }


}
