package com.abezukor.abezukor.autosms;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by student on 7/8/15.
 */

public class RelativeLayoutActivity extends android.app.Activity {
    //sets classwide varibels
    boolean buttonpressed = false;
    int id;
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
        //gets the id
        id = intent.getIntExtra("id", -1);
        //this will run if it was from the tapped list
        if (modify){
            //sets delete button to visable
            deletebutton.setVisibility(View.VISIBLE);

            //gets contact to edit
            DBHandler dbHandler = new DBHandler(this,null,null,1);
            String[] parts;
            parts = dbHandler.getMessageAndNumberFromId(id);
            //sets the text
            EditText number = (EditText)findViewById(R.id.number);
            EditText message = (EditText)findViewById(R.id.message);
            EditText name = (EditText)findViewById(R.id.name);
            name.setText(parts[2], TextView.BufferType.EDITABLE);
            number.setText(parts[0], TextView.BufferType.EDITABLE);
            message.setText(parts[1], TextView.BufferType.EDITABLE);
            // disbles the name textbok
            name.setFocusable(false);

        }
    }
    public void savemessage(View view) {
        //to save a AutoSMS
        buttonpressed = true;
        //gets input boxes
        EditText message = (EditText)findViewById(R.id.message);
        EditText number = (EditText)findViewById(R.id.number);
        EditText name = (EditText)findViewById(R.id.name);
        if  (!message.getText().toString().isEmpty() && !number.getText().toString().isEmpty() &&  !name.getText().toString().isEmpty()) {
            //sets contact to add

            DBHandler dbHandler = new DBHandler(this,null,null,1);
            if (!modify) {
                //if you are not modifying a pre written AutoSMS

                //create the custom object
                autoSMSObject autosms = new autoSMSObject();
                autosms.set_homescreenname(name.getText().toString());
                autosms.set_message(message.getText().toString());
                autosms.set_number(number.getText().toString());
                autosms.set_id(dbHandler.getnumberofrows());


                try{
                    dbHandler.addautosms(autosms);
                    id = dbHandler.getnumberofrows() -1;
                }catch (Exception e){}
            } else {
                dbHandler.modifyautosmssms(id,number.getText().toString(),message.getText().toString());
            }

            //checks if its a modifacation so it does not delete shortcut
            if (!modify) {
                ShortcutCreatorActivity(id, name.getText().toString());
            }
            //go back to main page
            Intent intent = new Intent(this, ListViewActivity.class);
            intent.putExtra("button pressed", buttonpressed);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_LONG).show();
        }
        }
        //to create a shortcut
        protected void  ShortcutCreatorActivity(int id, String name) {
            //makes intent that is run when the shprtcut is run
            Intent shortcutIntent = new Intent(getApplicationContext(), ShortCutActivity.class);
            shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            shortcutIntent.putExtra("id", id);

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
    }*/
    public void deletebtn(View view){
        //when delete button is tapped
        Toast.makeText(getApplicationContext(), "Please remove old Shortcut from home screen. AutoSMS will no longer remember it.", Toast.LENGTH_LONG).show();

        //actually deletes the thing
        System.out.println("Id is " + id);

        DBHandler dbHandler = new DBHandler(this,null,null,1);
        dbHandler.delete(id-1);
        //goes to main page
        Intent intent = new Intent(this, ListViewActivity.class);
        intent.putExtra("button pressed", buttonpressed);
        startActivity(intent);
    }
    }


