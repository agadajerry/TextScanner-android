package com.jerry.textscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class TextProcessing extends AppCompatActivity implements SurfaceHolder.Callback, Detector.Processor {

    private SurfaceView cameraView;
    private TextView textView;
    private CameraSource cameraSource;
    private Button button;
    public StringBuilder stringBuild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_processing);
        cameraView = findViewById(R.id.surfaceView);
        button = findViewById(R.id.resultBtn);
        textView = findViewById(R.id.text2);

        processingUnit();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeResultToTextField();
            }
        });


    }

    private void processingUnit() {

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Log.e("Main Activity", "Detector dependencies are not yet available");
        } else {
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(this);
            textRecognizer.setProcessor(this);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.CAMERA}, 1);
                return;
            }
            cameraSource.start(cameraView.getHolder());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        cameraSource.stop();
    }

    @Override
    public void release() {

    }

    @Override
    public void receiveDetections(Detector.Detections detections) {

        SparseArray items = detections.getDetectedItems();
        stringBuild = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            TextBlock textBlock = (TextBlock) items.valueAt(i);
            stringBuild.append(textBlock.getValue());
            stringBuild.append("\n");
            // the following  process is used to show how to use lines and elements as well

        }
        Log.v("stringBuild.toString()", stringBuild.toString());


        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.setText(stringBuild.toString());

            }
        });

    }

    private void takeResultToTextField() {

        //taking  the value of the scanned text to next screen

        Intent resultIntent = new Intent(TextProcessing.this, MainActivity.class);
        resultIntent.putExtra("scanResult", textView.getText());
        startActivity(resultIntent);
         /*
    slide in transition
     */
        overridePendingTransition(R.transition.slide_out_right,R.transition.slide_in_left);


    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {

                        cameraSource.start(cameraView.getHolder());
                    } catch (Exception ex) {

                    }

                }
            }
            break;
        }
    }
}
