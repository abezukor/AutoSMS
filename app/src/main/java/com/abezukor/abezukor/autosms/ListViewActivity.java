package com.abezukor.abezukor.autosms;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


public class ListViewActivity extends ListActivity {
    //sets class wide varybles
    boolean fromshortcut = false;
    Context context = this;
    ArrayList<String> arrayList = new ArrayList<String>();
    String a2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //sets activity GUI
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_layout, arrayList);
        setListAdapter(adapter);
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
        //gets the files
        SharedPreferences contacsnumberfile = context.getSharedPreferences("contacnumber", MODE_PRIVATE);
        int contactnumberint = contacsnumberfile.getInt("ContacsNumber", 0);
        SharedPreferences userDetails = context.getSharedPreferences("contacs", MODE_PRIVATE);

        for (int i = 0; i< contactnumberint; i++){
            String contactnumberstr = String.valueOf(i + 1);
            //gtes value for key i+1
            String contact = userDetails.getString(contactnumberstr, "thisdoesnotwork");
            //splits it up
            String[] parts = contact.split(" %tosplit% ");
            //sets to display
            String contacttodisplay = parts[0] + parts[1];
            arrayList.add(contacttodisplay);
        }
    }
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        //gets the listview items
        a2 = arrayList.get(position);
        //gets the files
        SharedPreferences contacsnumberfile = context.getSharedPreferences("contacnumber", MODE_PRIVATE);
        int contactnumberint = contacsnumberfile.getInt("ContacsNumber", 0);
        SharedPreferences userDetails = context.getSharedPreferences("contacs", MODE_PRIVATE);

        int i = 0;
        boolean looprun = true;
        //makes arraylist
        ArrayList<String> tosend = new ArrayList<String>();
        //i need the next line so i dont get qa syntax error however its not nessary code wise
        String tosendarray[] = {"12", "34", "56"};
        //setting the contactnumber to send
        int contactnumberint2 = 0;
        //runes through list untill finds contact and list item that mach
        while (i<contactnumberint && looprun == true){
            String contactnumberstr = String.valueOf(i + 1);
            String contact = userDetails.getString(contactnumberstr, "thisdoesnotwork");
            //re splits it up
            String[] parts = contact.split(" %tosplit% ");
            String contacttodisplay = parts[0] + parts[1];
            i++;

            if (a2.equals(contacttodisplay)){
                //creats array to send
                tosendarray = new String[3];
                //stops loop from running
                looprun = false;
                //again getting syntax error without this
                tosendarray[0] = parts[0];
                tosendarray[1] = parts[1];
                tosendarray[2] = parts[2];
                contactnumberint2 = i;
                //System.out.println("is this running");
        }
    }
        //goes to relitivelayout.class
        Intent intent = new Intent(this, RelativeLayoutActivity.class);
        //tells relitivelayout that this is a modify
        intent.putExtra("tomodify", true);

        //converts array to arraylist
        Collection l = Arrays.asList(tosendarray);
        tosend.addAll(l);
        //puts the contact extra
        intent.putStringArrayListExtra("tapeditem", tosend);
        //and the contact number
        intent.getIntExtra("contactnumber", contactnumberint2);
        //and finnaly runs it
        startActivity(intent);


    }


    }

