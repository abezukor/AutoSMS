package com.abezukor.abezukor.autosms;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class ListViewActivity extends android.app.Activity implements View.OnClickListener {
    //sets class wide varybles
    boolean fromshortcut = false;
    int lastID;

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

        if (fromshortcut) {
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
    public void addautosms(View view) {
        DBHandler dbHandler = new DBHandler(this, null, null, 1);
        Intent intent = new Intent(this, RelativeLayoutActivity.class);
        //intent.putExtra("id", dbHandler.getNextID());
        //System.out.println("ListView New ID should" + dbHandler.getNextID());
        startActivity(intent);

    }

    //polpulates the list
    public void polpulatelist() {
        DBHandler dbHandler = new DBHandler(this, null, null, 1);
        //try{
        autoSMSObject[] data = dbHandler.getAllData();
        TableLayout table = (TableLayout) findViewById(R.id.table);
        int rownumber = 0;
        while ( rownumber <dbHandler.getnumberofrows()) {

            //System.out.println(rownumber + "rownumber");
            //makes the table row
            TableRow row = new TableRow(this);
            //makes the extboxes
            TextView name = new TextView(this);
            name.setText(data[rownumber].get_homescreenname());
            name.setGravity(Gravity.CENTER_HORIZONTAL);
            name.setTextSize(20);

            TextView number = new TextView(this);
            number.setText(data[rownumber].get_number());
            number.setGravity(Gravity.CENTER_HORIZONTAL);
            number.setTextSize(20);


            TextView message = new TextView(this);
            message.setText(data[rownumber].get_message());
            message.setGravity(Gravity.CENTER_HORIZONTAL);
            message.setTextSize(20);
            message.setSingleLine(false);

            //adds properties to the rows
            //System.out.println("List ID is: " + data[rownumber].get_id());

            row.addView(name);
            row.addView(number);
            row.addView(message);
            row.setId(data[rownumber].get_id());
            row.setOnClickListener(this);


            table.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
            rownumber++;
            /*}
        }catch (Exception e){
            //System.out.println(e);
        }*/


        }
        lastID = rownumber+1;

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = new Intent(this, RelativeLayoutActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("tomodify", true);
        startActivity(intent);
    }
}
