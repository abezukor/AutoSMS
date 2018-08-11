package com.abezukor.abezukor.autosms;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
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
    boolean modify;
    static final int PICK_CONTACT=1;
    private EditText contactNumber;
    private int id;

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
        id=intent.getIntExtra("id", -1);
        //System.out.println("Rel Layout id " + id);
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
            PhoneNumberUtils.formatNumber(number.getText().toString());

            EditText message = (EditText)findViewById(R.id.message);
            EditText name = (EditText)findViewById(R.id.name);
            name.setText(parts[2], TextView.BufferType.EDITABLE);
            number.setText(parts[0], TextView.BufferType.EDITABLE);
            message.setText(parts[1], TextView.BufferType.EDITABLE);
            // disbles the name textbok
            name.setFocusable(false);

        }

        contactNumber = (EditText) findViewById(R.id.number);

        Button buttonPickContact = (Button)findViewById(R.id.pickContact);
        buttonPickContact.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, 1);


            }});
    }



    //This code is from https://stackoverflow.com/questions/12123302/android-showing-phonebook-contacts-and-selecting-one

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_CONTACT){
            if(resultCode == RESULT_OK){
                Uri contactData = data.getData();
                Cursor cursor =  getContentResolver().query(contactData, null, null, null, null);
                cursor.moveToFirst();

                String number =  cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));

                //contactName.setText(name);
                contactNumber.setText(number);
                //contactEmail.setText(email);
            }
        }
    }


    public void savemessage(View view) {
        //to save a AutoSMS
        buttonpressed = true;
        Context context = getApplicationContext();
        //gets input boxes
        EditText message = findViewById(R.id.message); String messageText = message.getText().toString();
        EditText number = findViewById(R.id.number); String numberText = number.getText().toString();
        EditText name = findViewById(R.id.name); String nameText = name.getText().toString();
        if  (!messageText.isEmpty() && !numberText.isEmpty() &&  !nameText.isEmpty()) {
            //sets contact to add

            DBHandler dbHandler = new DBHandler(this,null,null,1);
            if (!modify) {
                //if you are not modifying a pre written AutoSMS

                //create the custom object
                AutoSMSObject autosms = new AutoSMSObject(nameText, numberText, messageText);

                try{
                   autosms.set_id(dbHandler.addautosms(autosms));
                }catch (Exception e){}
                DynamicShortcuts shortcutMaker = new DynamicShortcuts();
                shortcutMaker.createShortcut(shortcutMaker.makeShortCutInfo(autosms,context),context,autosms);

            } else {
                dbHandler.modifyautosmssms(id,numberText,messageText);
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
    public void deletebtn(View view){
        //when delete button is tapped
        DynamicShortcuts shortcuts = new DynamicShortcuts();
        shortcuts.removeShortcut(id,this);
        //actually deletes the thing
        //System.out.println("Id is " + id);

        DBHandler dbHandler = new DBHandler(this,null,null,1);
        dbHandler.delete(id);
        //goes to main page
        Intent intent = new Intent(this, ListViewActivity.class);
        intent.putExtra("button pressed", buttonpressed);
        startActivity(intent);
    }
    }


