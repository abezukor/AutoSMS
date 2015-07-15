package com.idtech.abezukor.autosms;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.content.SharedPreferences.Editor;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class ListViewActivity extends ListActivity {
    boolean fromsave = false;
    Context context = this;
    ArrayList<String> arrayList = new ArrayList<String>();
    String a2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Intent intent = getIntent();
        //fromsave = intent.getBooleanExtra("button pressed", false);
        fromsave = intent.getBooleanExtra("fromonebutton", false);

        if (fromsave == true) {
            finish();
            System.exit(0);
        }
        polpulatelist();
        //arrayList.add("Object 1");
        //arrayList.add("Object 2");
        //arrayList.add("Object 3");
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
        if (id == R.id.help) {
            Intent intent = new Intent(this, helpActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void addautosms (View view){
        Intent intent = new Intent(this, RelativeLayoutActivity.class);
        startActivity(intent);

    }
    public void polpulatelist() {
        SharedPreferences contacsnumberfile = context.getSharedPreferences("contacnumber", MODE_PRIVATE);
        int contactnumberint = contacsnumberfile.getInt("ContacsNumber", 0);
        SharedPreferences userDetails = context.getSharedPreferences("contacs", MODE_PRIVATE);

        for (int i = 0; i< contactnumberint; i++){
            String contactnumberstr = String.valueOf(i + 1);
            String contact = userDetails.getString(contactnumberstr, "thisdoesnotwork");
            String[] parts = contact.split(" %tosplit% ");
            String contacttodisplay = parts[0] + parts[1];
            arrayList.add(contacttodisplay);
        }
    }
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        a2 = arrayList.get(position);

        SharedPreferences contacsnumberfile = context.getSharedPreferences("contacnumber", MODE_PRIVATE);
        int contactnumberint = contacsnumberfile.getInt("ContacsNumber", 0);
        SharedPreferences userDetails = context.getSharedPreferences("contacs", MODE_PRIVATE);

        int i = 0;
        boolean looprun = true;
        ArrayList<String> tosend = new ArrayList<String>();
        String tosendarray[] = {"12", "34", "56"};
        int contactnumberint2 = 0;
        while (i<contactnumberint && looprun == true){
            String contactnumberstr = String.valueOf(i + 1);
            String contact = userDetails.getString(contactnumberstr, "thisdoesnotwork");
            String[] parts = contact.split(" %tosplit% ");
            String contacttodisplay = parts[0] + parts[1];
            i++;
            if (a2.equals(contacttodisplay)){
                tosendarray = new String[3];
                looprun = false;
                tosendarray[0] = parts[0];
                tosendarray[1] = parts[1];
                tosendarray[2] = parts[2];
                contactnumberint2 = i;
                //System.out.println("is this running");
        }
            else {
                //System.out.println("doesnotwork");
            }
    }

        Intent intent = new Intent(this, RelativeLayoutActivity.class);
        intent.putExtra("tomodify", true);

        Collection l = Arrays.asList(tosendarray);
        tosend.addAll(l);
        intent.putStringArrayListExtra("tapeditem", tosend);
        intent.getIntExtra("contactnumber", contactnumberint2);
        startActivity(intent);


    }


    }

