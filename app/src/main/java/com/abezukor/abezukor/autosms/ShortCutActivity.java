package com.abezukor.abezukor.autosms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import android.telephony.SmsManager;

public class ShortCutActivity extends android.app.Activity {
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_shortcut);
            Intent intent = getIntent();
            int id = intent.getIntExtra("id", 0);

            //System.out.println(id);

            DBHandler dbHandler = new DBHandler(this, null, null, 1);
            String[] parts;
            parts = dbHandler.getMessageAndNumberFromId(id);

            //sets to display
            //see if it will send the sms
            try {
                System.out.println("Parts 0 = " + parts[0]);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(parts[0], null, parts[1], null, null);
                stop();
            } catch (NumberFormatException nfe) {
                //if not
                Toast.makeText(getApplicationContext(), intent.getStringExtra("Sorry There Was an Error"), Toast.LENGTH_LONG).show();
                stop();

            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "No Data for this Shortcut please Create it in the AutoSMS App", Toast.LENGTH_LONG).show();
            stop();
        }
    }

    public void stop(){
        //goes back to home page to close
        Intent intent = new Intent(this, ListViewActivity.class);
        intent.putExtra("fromonebutton", true);
        startActivity(intent);
    }
}
