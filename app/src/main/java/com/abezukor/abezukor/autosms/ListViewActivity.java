package com.abezukor.abezukor.autosms;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;


public class ListViewActivity extends android.app.Activity {
    //sets class wide varybles
    boolean fromshortcut = false;
    Context context = this;
    ArrayList<String> arrayList = new ArrayList<String>();
    String a2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //sets activity GUI
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_view);
        //gets the intent
        Intent intent = getIntent();
        //fromsave = intent.getBooleanExtra("button pressed", false);
        //sees if it was from shortcut i9f so close it
        fromshortcut = intent.getBooleanExtra("fromonebutton", false);

        if (fromshortcut == true) {
            finish();
            System.exit(0);
        }
        //polpultaes the list
        polpulatelist();
        //arrayList.add("Object 1");
        //arrayList.add("Object 2");
        //arrayList.add("Object 3");
        //ads poppulated list to the GUI
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_list_view, menu);

        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if the help is pressed go to help
        if (id == R.id.help) {
            Intent intent = new Intent(this, helpActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //ad auto sms button goes to reletive layout.xml
    public void addautosms (View view){
        Intent intent = new Intent(this, RelativeLayoutActivity.class);
        startActivity(intent);

    }
    //polpulates the list
    public void polpulatelist() {
        DBHandler dbHandler = new DBHandler(this,null,null,1);
        try{
            String[][] data = dbHandler.getAllData();
            TableLayout table = (TableLayout)findViewById(R.id.table);
            int rownumber = 0;
            while (data[0].length <rownumber+1){

                TableRow row = new TableRow(this);

                TextView name = new TextView(this);
                name.setText(data[rownumber][2]);

                TextView number = new TextView(this);
                name.setText(data[rownumber][0]);

                TextView message = new TextView(this);
                number.setText(data[rownumber][1]);

                row.addView(name);
                row.addView(number);
                row.addView(message);

                table.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT));
            }
        }catch (Exception e){}


    }


    }

