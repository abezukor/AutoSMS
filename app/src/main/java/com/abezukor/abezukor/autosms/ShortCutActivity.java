package com.abezukor.abezukor.autosms;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;
import android.telephony.SmsManager;

import java.util.Arrays;

public class ShortCutActivity extends android.app.Activity {
    private static final int SEND_SMS_PERMISSIONS_REQUEST = 1;

    protected void onCreate(Bundle savedInstanceState) {
        Context context = getApplicationContext();
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_shortcut);
            Intent intent = getIntent();
            int id = intent.getIntExtra("id", 0);

            System.out.println(id);

            //checks that the id is valid (!=0)
            if(id == 0){Toast.makeText(getApplicationContext(), "No Data for this Shortcut please create it in the AutoSMS App", Toast.LENGTH_LONG).show();stop();}
            //System.out.println("Shortcut ID is " + id);

            DBHandler dbHandler = new DBHandler(this, null, null, 1);
            String[] parts;
            parts = dbHandler.getMessageAndNumberFromId(id);
            System.out.println(Arrays.toString(parts));

            //sets to display
            //see if it will send the sms
            try {
                //request permission to send sms if needed
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.SEND_SMS)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.SEND_SMS},
                                SEND_SMS_PERMISSIONS_REQUEST);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    //System.out.println("Parts 0 = " + parts[0]);
                    SmsManager smsManager = SmsManager.getDefault();

                    smsManager.sendTextMessage(parts[0], null, parts[1], null, null);
                    stop();
                }
            } catch (NumberFormatException nfe) {
                //if not
                Toast.makeText(getApplicationContext(), intent.getStringExtra("Sorry, There Was an Error"), Toast.LENGTH_LONG).show();
                stop();

            }
        }catch (Exception e){
            stop();
        }
    }

    public void stop(){
        //goes back to home page to close
        Intent intent = new Intent(this, ListViewActivity.class);
        intent.putExtra("fromonebutton", true);
        startActivity(intent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SEND_SMS_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted. Please Re-Launch shortcut to send text", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "I cant send texts, and am therefor useless until you grand me permissionn. You can do it from the settings menu",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
