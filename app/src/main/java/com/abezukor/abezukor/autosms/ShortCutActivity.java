package com.abezukor.abezukor.autosms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import android.telephony.SmsManager;

public class ShortCutActivity extends android.app.Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcut);
        Intent intent = getIntent();
        //Toast.makeText(getApplicationContext(), intent.getStringExtra("message") , Toast.LENGTH_LONG).show();
        Context context = this;
        SharedPreferences contacsnumberfile = context.getSharedPreferences("contacnumber", MODE_PRIVATE);
        int contactnumberint = contacsnumberfile.getInt("ContacsNumber", 0);
        SharedPreferences userDetails = context.getSharedPreferences("contacs", MODE_PRIVATE);
        String contactnumberstr = String.valueOf(contactnumberint);
        String contact = userDetails.getString(contactnumberstr, "thisdoesnotwork");
        //splits it up
        String[] parts = contact.split(" %tosplit% ");
        //sets to display
        //see if it will send the sms
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(parts[0], null, parts[1], null, null);
            stop();
        } catch(NumberFormatException nfe) {
            //if not
            Toast.makeText(getApplicationContext(), intent.getStringExtra("Sorry There Was an Error") , Toast.LENGTH_LONG).show();
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
