package com.idtech.abezukor.autosms;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.SharedPreferences.Editor;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent.ShortcutIconResource;

import java.net.URISyntaxException;
import java.util.ArrayList;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by student on 7/8/15.
 */
public class RelativeLayoutActivity extends android.app.Activity {
    //sets classwide varibels
    boolean buttonpressed = false;
    Context context = this;
    int contactnumberint2;
    boolean modify;
    protected void onCreate(Bundle savedInstanceState) {
        //code here runs when the app starts
        //sets the acrivity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relative_layout);
        //sets delete button to invisable
        Button deletebutton = (Button) findViewById(R.id.deletebutton);
        deletebutton.setVisibility(View.INVISIBLE);
        //retrives intent data
        Intent intent = getIntent();
        modify = intent.getBooleanExtra("tomodify", false);
        contactnumberint2 = intent.getIntExtra("contactnumbe", -1);
        //this will run if it was from the tapped list
        if (modify == true){
            //sets delete button to visable
            deletebutton.setVisibility(View.VISIBLE);
            //gets contact to edit
            ArrayList<String> data = intent.getStringArrayListExtra("tapeditem");
            String[] parts = data.toArray(new String[data.size()]);
            //sets the text
            EditText number = (EditText)findViewById(R.id.number);
            EditText message = (EditText)findViewById(R.id.message);
            EditText name = (EditText)findViewById(R.id.name);
            number.setText(parts[0], TextView.BufferType.EDITABLE);
            message.setText(parts[1], TextView.BufferType.EDITABLE);
            name.setText(parts[2], TextView.BufferType.EDITABLE);
            //removeshortcut(parts[2], parts[0], parts[1]); se later for why

        }
    }
    public void savemessage(View view) {
        //to save a AutoSMS
        buttonpressed = true;
        //gets input boxes
        EditText message = (EditText)findViewById(R.id.message);
        EditText number = (EditText)findViewById(R.id.number);
        EditText name = (EditText)findViewById(R.id.name);
        //checks for balnc fields
        if  (message.getText().toString().isEmpty() == false && number.getText().toString().isEmpty() ==false &&  name.getText().toString().isEmpty() ==false) {
            //sets contact to add
            String contact = number.getText().toString() + " %tosplit%  " + message.getText().toString() + "  %tosplit% " + name.getText().toString();
            String contactnumberstr = "-1";

            if (modify == false) {
                //if you are not modifying a pre written AutoSMS
                SharedPreferences contacsnumberfile = context.getSharedPreferences("contacnumber", MODE_PRIVATE);
                int contactnumberint = contacsnumberfile.getInt("ContacsNumber", 0);
                contactnumberstr = String.valueOf(contactnumberint + 1);
                Editor edit = contacsnumberfile.edit();
                //edit.clear();
                edit.putInt("ContacsNumber", contactnumberint + 1);
                edit.commit();
            } else {
                //if you are modifying
                contactnumberstr = String.valueOf(contactnumberint2);
                Toast.makeText(getApplicationContext(), "Please remove old Shortcut from home screen. A new one has been created", Toast.LENGTH_LONG).show();
            }

            SharedPreferences userDetails = context.getSharedPreferences("contacs", MODE_PRIVATE);
            //do the edit
            Editor edit2 = userDetails.edit();
            edit2.putString(contactnumberstr, contact);
            edit2.commit();
            ShortcutCreatorActivity(number.getText().toString(), message.getText().toString(), name.getText().toString());
            //go back to main page
            Intent intent = new Intent(this, ListViewActivity.class);
            intent.putExtra("button pressed", buttonpressed);
            startActivity(intent);
        }
        //if any fields are blank
        else {
            Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_LONG).show();
        }
        }
        //to create a shortcut
        protected void  ShortcutCreatorActivity(String number, String message, String name) {
            /*
                Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

                // Shortcut name
                shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
                shortcut.putExtra("duplicate", false);  // Just create once

                //add my extra stuff
                shortcut.putExtra("number", number);
                shortcut.putExtra("message", message);
                // Setup current activity shoud be shortcut object
                ComponentName comp = new ComponentName(this, ShortCutActivity.class);
                shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));

                // Set shortcut icon
                ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher);
                shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

                sendBroadcast(shortcut);
              */
            //Code i know works

            //makes intent that is run when the shprtcut is run
            Intent shortcutIntent = new Intent(getApplicationContext(), ShortCutActivity.class);
            shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            shortcutIntent.putExtra("number", number);
            shortcutIntent.putExtra("message", message);

            //makes intent to make the shortcut
            Intent addIntent = new Intent();
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_launcher));


            addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            getApplicationContext().sendBroadcast(addIntent);


            //bad alternate method
            /*Intent shortcutIntent = new Intent();
            shortcutIntent.setClassName("com.idtech.abezukor.autosms", "ShortCutActivity.class");
            shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            shortcutIntent.putExtra("number", number);
            shortcutIntent.putExtra("message", message);

            Intent addIntent = new Intent();
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, R.drawable.ic_launcher));

            addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            context.sendBroadcast(addIntent);*/
        }
    //tried to remove shortcut did not work

    /*
    protected void removeshortcut(String name, String number, String message) {
        Intent shortcutIntent = new Intent();
        shortcutIntent.setClassName("com.idtech.abezukor.autosms",
                "com.idtech.abezukor.autosms.ShortCutActivity");
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent intent = new Intent();
        try {
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
                    Intent.parseUri(shortcutIntent.toUri(0), 0));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        intent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        context.sendBroadcast(intent);
    }
    private void delShortcut(){
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");

        // Shortcut name
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));

        String appClass = this.getPackageName() + ".ShortCutActivity";
        ComponentName comp = new ComponentName(this.getPackageName(), appClass);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));

        sendBroadcast(shortcut);
    }*/
    public void deletebtn(View view){
        //when delete button is tapped
        //delShortcut();
        Toast.makeText(getApplicationContext(), "Please remove old Shortcut from home screen. AutoSMS will no longer remember it.", Toast.LENGTH_LONG).show();
        //gets the saved file
        SharedPreferences contacsnumberfile = context.getSharedPreferences("contacnumber", MODE_PRIVATE);
        int contactnumberint = contacsnumberfile.getInt("ContacsNumber", 0);
        SharedPreferences userDetails = context.getSharedPreferences("contacs", MODE_PRIVATE);

        Editor edit2 = userDetails.edit();
        //moves all items after the deketed one one back
        for (int i = contactnumberint2; i< contactnumberint; i++){
            String contactnumberstr = String.valueOf(i+1);
            String contacttoreplacenumberstr = String.valueOf(i-1);
            String contact = userDetails.getString(contactnumberstr, "thisdoesnotwork");
            edit2.putString(contacttoreplacenumberstr, contact);
        }
        edit2.commit();
        //sets contactnumber int
        int contactnumberstr4 = contactnumberint - 1;
        Editor edit = contacsnumberfile.edit();
        edit.putInt("ContacsNumber", contactnumberstr4);
        edit.commit();

        //goes to main page
        Intent intent = new Intent(this, ListViewActivity.class);
        intent.putExtra("button pressed", buttonpressed);
        startActivity(intent);
    }
    }


