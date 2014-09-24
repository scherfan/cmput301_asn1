
package com.example.cmput301_asn1;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;



public class EmailActivity extends Activity
{
   // protected ArrayList<String> newList;
  //  protected ArrayList<String> newList2;


 //   protected ArrayAdapter<String> adapter1;
 //   protected ArrayAdapter<String> adapter2;

 //   protected ListView emailTodoListView;
 //   protected ListView emailArchiveListView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        
        //emailTodoListView = (ListView) findViewById(R.id.emailtodolist);
       // emailArchiveListView = (ListView) findViewById(R.id.emailarchivelist);
        
       // newList = new ArrayList<String>();
       // newList2 = new ArrayList<String>();
       // newList.addAll(todoList);
       // newList2.addAll(ArchiveActivity.giveList());
        
       // adapter1 = new ArrayAdapter<String>(this,
       //         android.R.layout.simple_list_item_multiple_choice, newList);
       // adapter2 = new ArrayAdapter<String>(this,
       //         android.R.layout.simple_list_item_multiple_choice, newList2);
       // emailTodoListView.setAdapter(adapter1);
       // emailArchiveListView.setAdapter(adapter2);
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
}
