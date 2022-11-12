package com.precise.request_permsission;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String[] PERMISSIONS;
    private Button GetValues;
    private RadioGroup simSelector;
    private RadioButton sim1, sim2, selectedSim;
    private TextView display;
    private SmsManager smsManager;
    private SubscriptionManager subsManager;
    private SubscriptionInfo subsInfo1, subsInfo2;
    private String carrier1, carrier2, carrier;
    private int simCount, simId1, simId2, activeSim;

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

        subsManager = this.getSystemService(SubscriptionManager.class);
        simCount = subsManager.getActiveSubscriptionInfoCountMax();

        getPermissions();
        getSubscriptionInfo();
        sim1 = findViewById(R.id.sim1);
        sim2 = findViewById(R.id.sim2);
        simSelector = findViewById(R.id.sim_selector);
        display = findViewById(R.id.display);
        initialiseSimSelector();


        simSelector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                simChanged();
            }
        });



        GetValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void getPermissions(){

        if (!hasPermissions(MainActivity.this, PERMISSIONS)) {

            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, 1);
        }

    }


    private void initialiseSimSelector(){
        if (simCount > 1) {
            sim1.setText(carrier1);
            sim2.setText(carrier2);
        } else {
            sim1.setText(carrier);
        }
    }

    private void simChanged(){
        assert simSelector != null;
        int simId = simSelector.getCheckedRadioButtonId();
        if (simId != -1) {
            selectedSim = findViewById(simId);
            String comment = selectedSim.getText().toString();
            comment = "The selected network is \"" + comment + "\"";
            display.setText(comment);
            activeSim = parseInt((String) selectedSim.getTag());
            int simNumber = activeSim + 1;
            String temp = String.valueOf(simNumber);
            comment = comment + " in sim slot \"" + temp + "\"";
            display.setText(comment);

        } else {

            display.setText("No sim is selected");
        }
    }

    // Get the carrier names for the sim slots
    private void getSubscriptionInfo(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            getPermissions();
        }
        if(simCount > 1) {
            List localList = subsManager.getActiveSubscriptionInfoList();
            subsInfo1 = (SubscriptionInfo) localList.get(0);
            subsInfo2 = (SubscriptionInfo) localList.get(1);
            carrier1 = subsInfo1.getDisplayName().toString();
            carrier2 = subsInfo2.getDisplayName().toString();
        } else {
            TelephonyManager tManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
            carrier = tManager.getNetworkOperatorName();
        }

    }

    //Set the sms manager for selected sim slot
    private void setSmsManager(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            getPermissions();
        }
        SubscriptionInfo subsInfo = subsManager.getActiveSubscriptionInfoForSimSlotIndex(activeSim);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            smsManager = getApplicationContext().getSystemService(SmsManager.class) .createForSubscriptionId(subsInfo.getSubscriptionId());
        } else {
            smsManager = SmsManager.getSmsManagerForSubscriptionId(activeSim);
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