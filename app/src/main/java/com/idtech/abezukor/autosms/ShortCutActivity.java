package com.idtech.abezukor.autosms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.telephony.SmsManager;

public class ShortCutActivity extends android.app.Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcut);
        Intent intent = getIntent();
        //Toast.makeText(getApplicationContext(), intent.getStringExtra("message") , Toast.LENGTH_LONG).show();
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(intent.getStringExtra("number"), null, intent.getStringExtra("message"), null, null);
            stop();
        } catch(NumberFormatException nfe) {
            Toast.makeText(getApplicationContext(), intent.getStringExtra("Sorry There Was an Error") , Toast.LENGTH_LONG).show();
            stop();

        }
    }

    public void stop(){
        Intent intent = new Intent(this, ListViewActivity.class);
        intent.putExtra("fromonebutton", true);
        startActivity(intent);
    }
}
