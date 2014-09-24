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

    public final static String EXTRA_MESSAGE = "com.example.cmput301_asn1";

    protected ArrayList<String> todoList;

    protected ArrayAdapter<String> adapter;

    protected ListView todoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initiates new array list to hold list items
        // also an adapter that has check boxes for the list
        // and a button to add items to the list
        todoList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, todoList);
        todoListView = (ListView) findViewById(R.id.todo_listview);

        Button add_button = (Button) findViewById(R.id.addButton);
        // initiate a list view for the array list and its check boxes

        todoListView.setAdapter(adapter);
        todoListView.setItemsCanFocus(false);
        registerForContextMenu(todoListView);

        // waits for add button to be clicked and then
        // converts text in the edit test to a string and
        // puts it in the array list
        add_button.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                EditText todoText = (EditText) findViewById(R.id.addTodoText);
                String todoString = todoText.getText().toString();
                adapter.add(todoString);
            }
        });
    }

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
            // case R.id.email_option:
            // emailItem();
            // return true;
            // case R.id.action_settings:
            // accessSettings();
            // return true;
            // case R.id.summary_option:
            // accessSummary();
            // return true;
        default:
            return super.onContextItemSelected(item);
        }
    }

    private void archiveItem(int position, View v)
    {
        Intent intent = new Intent(this, ArchiveActivity.class);
        String toArchive = todoList.get(position).toString();
        CheckedTextView item = (CheckedTextView) v;
        if (item.isChecked())
        {
            toArchive = toArchive + "/";
        }
        intent.putExtra(EXTRA_MESSAGE, toArchive);
        startActivity(intent);
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
}
