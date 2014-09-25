
package com.example.cmput301_asn1;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;



public class EmailActivity extends Activity
{
    protected ArrayList<String> eTodoList;
    protected ArrayList<String> eArchiveList;
    protected ArrayList<String> finalList;


    protected ArrayAdapter<String> todoAdapter;
    protected ArrayAdapter<String> archiveAdapter;

    protected ListView emailTodoListView;
    protected ListView emailArchiveListView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        
        emailTodoListView = (ListView) findViewById(R.id.emailtodolist);
       emailArchiveListView = (ListView) findViewById(R.id.emailarchivelist);
        
        eTodoList = new ArrayList<String>();
        eArchiveList = new ArrayList<String>();
        
        if (MainActivity.giveList() != null)
            eTodoList = MainActivity.giveList();
        if (ArchiveActivity.giveList() != null)
            eArchiveList = ArchiveActivity.giveList();
        
        finalList = new ArrayList<String>();
        
        todoAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, eTodoList);
        archiveAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, eArchiveList);
        emailTodoListView.setAdapter(todoAdapter);
        emailArchiveListView.setAdapter(archiveAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.email, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) { return true; }
        return super.onOptionsItemSelected(item);
    }
    
    //http://stackoverflow.com/questions/2197741/how-can-i-send-emails-from-my-android-application
    public void emailItems(View view)
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
        getChecked();
        intent.putExtra(Intent.EXTRA_TEXT, finalList.toString());
        try {
            startActivity(Intent.createChooser(intent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
    
    // got from: http://stackoverflow.com/questions/4831918/how-to-get-all-checked-items-from-a-listview
    protected void getChecked()
    {
        SparseBooleanArray checkedTodo = emailTodoListView.getCheckedItemPositions();
        SparseBooleanArray checkedArchive = emailArchiveListView.getCheckedItemPositions();
        for(int i = 0; i < emailTodoListView.getAdapter().getCount(); i++)
        {
            if(checkedTodo.get(i))
                finalList.add(emailTodoListView.getItemAtPosition(i).toString());
        }
        for (int i = 0; i < emailArchiveListView.getAdapter().getCount(); i++)
        {
            if(checkedArchive.get(i))
                finalList.add(emailArchiveListView.getItemAtPosition(i).toString());
        }
    }
}
