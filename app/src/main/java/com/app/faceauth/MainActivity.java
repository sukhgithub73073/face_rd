package com.app.faceauth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private TextView tvResponse;
    private static final int FACE_RD_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResponse = findViewById(R.id.tvResponse);
        Button btnCapture = findViewById(R.id.btnCapture);

        btnCapture.setOnClickListener(v -> launchFaceRD());
    }

    private void launchFaceRD() {
        // Precise XML to avoid Error 9930
        String pidOptions = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<PidOptions ver=\"1.0\">" +
                "<Opts env=\"P\" fCount=\"1\" fType=\"0\" iCount=\"0\" pCount=\"0\" format=\"0\" " +
                "pidVer=\"2.0\" timeout=\"10000\" posh=\"UNKNOWN\" />" +
                "<CustOpts>" +
                "<Param name=\"txnId\" value=\"TEST" + System.currentTimeMillis() + "\"/>" +
                "</CustOpts>" +
                "</PidOptions>";
//dd
        try {
            Intent intent = new Intent();
            // Official UIDAI Face RD Action
//            intent.setAction("in.gov.uidai.facerd.face.CAPTURE");
            intent.setAction("in.gov.uidai.rdservice.face.CAPTURE");

            intent.putExtra("request", pidOptions);
            startActivityForResult(intent, FACE_RD_CODE);
        } catch (Exception e) {
            tvResponse.setText("Error: AadhaarFaceRD App not found!\n" + e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FACE_RD_CODE && data != null) {
            // "data" extra contains the result XML
            String resultXml = data.getStringExtra("data");
            if (resultXml != null) {
                tvResponse.setText(resultXml);
            } else {
                tvResponse.setText("No data received from Face RD app.");
            }
        }
    }
}