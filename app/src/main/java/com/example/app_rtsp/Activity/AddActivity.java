package com.example.app_rtsp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_rtsp.Database.DataHelper;
import com.example.app_rtsp.Models.Camera_Link;
import com.example.app_rtsp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddActivity extends AppCompatActivity {
    private FloatingActionButton btn_Add;
    private FloatingActionButton btn_View_By_Add;
    private EditText txtCamName;
    private EditText txtRTSPLink;
    private int _id;
    private Camera_Link cameraLink;

    private Intent intent;
    private DataHelper db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        References();
        db = new DataHelper(this);
        // Recieve data edit
        RecieveEdit();
        // Add camera
        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textName = txtCamName.getText().toString().trim();
                String textLink = txtRTSPLink.getText().toString().trim().replace("rtsp://", "");

                if (textName.length() == 0) {
                    txtCamName.setError("Camera name is empty!");
                    txtCamName.setText("");
                } else if (textLink.length() == 0) {
                    txtRTSPLink.setError("Camera link is invalid!");
                } else {
                    if (_id == -1) {
                        cameraLink = new Camera_Link(txtCamName.getText().toString().trim(), txtRTSPLink.getText().toString().trim());
                        Boolean result = db.addCam(cameraLink);

                        if (result == true) {
                            Toast.makeText(AddActivity.this, "Add camera successfully", Toast.LENGTH_SHORT).show();
                            AddActivity.this.finish();
                        } else {
                            Toast.makeText(AddActivity.this, "Fail to add camera", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        cameraLink = new Camera_Link(_id, txtCamName.getText().toString().trim(), txtRTSPLink.getText().toString().trim());
                        boolean check = db.updateCam(cameraLink);
                        if (check == true) {
                            Toast.makeText(AddActivity.this, "Update successfully...", Toast.LENGTH_SHORT).show();
                            AddActivity.this.finish();
                        } else {
                            Toast.makeText(AddActivity.this, "Update error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        // View camera live
        btn_View_By_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(AddActivity.this, VideoView.class);
                intent.putExtra("cam_link", txtRTSPLink.getText().toString().trim());
                startActivity(intent);
            }
        });
    }

    private void RecieveEdit() {
        intent = getIntent();
        _id = intent.getIntExtra("camId", -1);
        if (_id != -1) {
            cameraLink = db.getCamDetail(_id);
            if (cameraLink != null) {
                txtCamName.setText(cameraLink.getCaName());
                txtRTSPLink.setText(cameraLink.getCaLink());
            }
        }
    }

    private void References() {
        btn_Add = findViewById(R.id.btnAddCamera);
        btn_View_By_Add = findViewById(R.id.btnViewByAdd);
        txtCamName = findViewById(R.id.textCamName);
        txtRTSPLink = findViewById(R.id.textLink);
    }
}
