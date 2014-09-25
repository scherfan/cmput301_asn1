/**
 * cherfan-To Do List is a simple to do listing and archiving app on Android.
 * Copyright (C) 2014 Steven Cherfan - cherfan@ualberta.ca
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.example.cmput301_asn1;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity
{

    //private static final String TAG = "DEBUG";

    // private static final String FILENAME = "file.sav";

    public final static String EXTRA_MESSAGE = "com.example.cmput301_asn1";

    protected static ArrayList<String> todoList;

    // protected ArrayList<String> checkListItem;

    protected ArrayAdapter<String> adapter;

    protected EditText todoText;

    protected ListView todoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todoListView = (ListView) findViewById(R.id.todo_listview);
        registerForContextMenu(todoListView);
        // loadFromFile();
        // if (todoList == null)
        todoList = new ArrayList<String>();
        // if (checkListItem == null)
        // checkListItem = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, todoList);
        todoListView.setAdapter(adapter);

        todoText = (EditText) findViewById(R.id.addTodoText);
        // initiates new array list to hold list items
        // also an adapter that has check boxes for the list
        // and a button to add items to the list

        // todoListView.setItemsCanFocus(false);

        Button add_button = (Button) findViewById(R.id.addButton);
        // initiate a list view for the array list and its check boxes

        // waits for add button to be clicked and then
        // converts text in the edit test to a string and
        // puts it in the array list
        add_button.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // setResult(RESULT_OK);
                String todoString = todoText.getText().toString();
                todoList.add(todoString);
                adapter.notifyDataSetChanged();
                // checkListItem.add("false");
                // saveInFile();
            }
        });

        /*
         * todoListView.setOnItemClickListener(new OnItemClickListener() {
         * 
         * @Override public void onItemClick(AdapterView<?> parent, View view,
         * int position, long id) { CheckedTextView item = (CheckedTextView)
         * view; if (item.isChecked()) checkListItem.set(position, "true"); else
         * checkListItem.set(position, "false"); saveInFile(); } });
         */

    }

    // taken from lonelyTwitter
    /*
     * @Override protected void onStart() { super.onStart(); // loadFromFile();
     * if (todoList == null) todoList = new ArrayList<String>(); // if
     * (checkListItem == null) // checkListItem = new ArrayList<String>();
     * 
     * adapter = new ArrayAdapter<String>(this,
     * android.R.layout.simple_list_item_multiple_choice, todoList);
     * todoListView.setAdapter(adapter);
     * 
     * /* for(int i = 0; i < checkListItem.size();i++) {
     * if(checkListItem.get(i).equals("true") == true) {
     * //todoListView.setItemChecked(todoListView.get, true); } }
     * 
     * }
     */

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();
        int position = info.position;
        View v = info.targetView;
        switch (item.getItemId())
        {
            case R.id.delete_option:
                deleteItem(position);
                return true;
            case R.id.archive_option:
                archiveItem(position, v);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void archiveItem(int position, View v)
    {
        Intent intent = new Intent(this, ArchiveActivity.class);

        CheckedTextView item = (CheckedTextView) v;
        if (item.isChecked())
        {
            String[] toArchive = { todoList.get(position).toString(), "true" };
            intent.putExtra(EXTRA_MESSAGE, toArchive);
            startActivity(intent);
        }
        else
        {
            String[] toArchive = { todoList.get(position).toString(), "false" };
            intent.putExtra(EXTRA_MESSAGE, toArchive);
            startActivity(intent);
        }
        todoList.remove(position);
        adapter.notifyDataSetChanged();
        // saveInFile();
    }

    // Adapted from student-picker
    private void deleteItem(int position)
    {
        AlertDialog.Builder deladb = new AlertDialog.Builder(MainActivity.this);
        deladb.setMessage("Delete " + todoList.get(position).toString() + "?");
        deladb.setCancelable(true);
        final int finalPosition = position;
        deladb.setPositiveButton("Delete", new OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                todoList.remove(finalPosition);
                adapter.notifyDataSetChanged();
                // saveInFile();
            }

        });
        deladb.setNegativeButton("Cancel", new OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // Cancels.
            }
        });
        deladb.show();
    }

    public static ArrayList<String> giveList()
    {
        return todoList;
    }

    public void viewArchive(MenuItem menu)
    {
        Intent intent = new Intent(this, ArchiveActivity.class);
        startActivity(intent);
    }

    public void emailItem(MenuItem menu)
    {
        Intent intent = new Intent(MainActivity.this, EmailActivity.class);
        startActivity(intent);
    }

    public void viewSummary(MenuItem menu)
    {
        Intent intent = new Intent(this, SummaryActivity.class);
        startActivity(intent);
    }
    // taken from lonely twitter
    /*
     * private void loadFromFile() { try { FileInputStream fis =
     * openFileInput(FILENAME); BufferedReader in = new BufferedReader(new
     * InputStreamReader(fis)); Gson gson = new Gson(); // Following was from:
     * //
     * https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google
     * /gson/Gson.html Type listType = new TypeToken<ArrayList<String>>() {
     * }.getType(); todoList = gson.fromJson(in, listType); Type newlistType =
     * new TypeToken<ArrayList<String>>() { }.getType(); checkListItem =
     * gson.fromJson(in, newlistType); } catch (FileNotFoundException e) { //
     * TODO Auto-generated catch block e.printStackTrace(); } catch (IOException
     * e) { // TODO Auto-generated catch block e.printStackTrace(); } }
     * 
     * // taken from lonelytwitter private void saveInFile() { try {
     * FileOutputStream fos = openFileOutput(FILENAME, 0); Gson gson = new
     * Gson(); OutputStreamWriter osw = new OutputStreamWriter(fos);
     * gson.toJson(todoList, osw); gson.toJson(checkListItem, osw); osw.flush();
     * fos.close(); } catch (FileNotFoundException e) { // TODO Auto-generated
     * catch block e.printStackTrace(); } catch (IOException e) { // TODO
     * Auto-generated catch block e.printStackTrace(); } }
     */
}
