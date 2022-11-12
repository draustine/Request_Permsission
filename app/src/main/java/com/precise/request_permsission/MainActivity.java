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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String[] PERMISSIONS;
    private Button GetValues;
    private RadioGroup simSelector;
    private RadioButton sim1, sim2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetValues = findViewById(R.id.getValues);

        PERMISSIONS = new String[] {
                Manifest.permission.INTERNET,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE
        };

        getPermissions();

        GetValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }

    protected void getPermissions(){

        if (!hasPermissions(MainActivity.this, PERMISSIONS)) {

            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, 1);
        }

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
            int x = 0;
            String[] line;
            String tempStr = "", comment = "";
            for (String s: PERMISSIONS) {
                int m = s.length();
                tempStr = s.substring(19, m);
                if (grantResults[x] == PackageManager.PERMISSION_GRANTED) {
                    comment = tempStr + " permission is granted";
                } else {
                    comment = tempStr + " permission is denied";
                }
                Toast.makeText(this, comment, Toast.LENGTH_LONG).show();
                x++;
            }



        }
    }
}