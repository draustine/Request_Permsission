package com.precise.request_permsission;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String[] PERMISSIONS;
    private Button AskPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AskPermissions = findViewById(R.id.askPermissions);

        PERMISSIONS = new String[] {
                Manifest.permission.INTERNET,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE
        };

        AskPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!hasPermissions(MainActivity.this, PERMISSIONS)) {

                    ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, 1);
                }
            }
        });

    }

    private boolean hasPermissions(Context context, String... PERMISSIONS) {

        if (context != null && PERMISSIONS != null) {

            for (String permission: PERMISSIONS){

                if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

                    return false;
                }

            }

        }
        return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Internet permission is granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Internet permission is denied!!!", Toast.LENGTH_LONG).show();
            }

            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS permission is granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "SMS permission is denied!!!", Toast.LENGTH_LONG).show();
            }

            if (grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read Phone state permission is granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Read Phone state permission is denied!!!", Toast.LENGTH_LONG).show();
            }

            if (grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Calling permission is granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Calling permission is denied!!!", Toast.LENGTH_LONG).show();
            }



        }
    }
}