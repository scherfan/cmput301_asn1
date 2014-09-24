
package com.example.cmput301_asn1;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ArchiveActivity extends Activity
{

    protected ArrayList<String> archivedList;

    protected ArrayAdapter<String> adapter;

    protected ListView archiveListView;

    public final static String EXTRA_UNARCHIVEDITEM = "com.example.cmput301_asn1";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        archivedList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, archivedList);
        archiveListView = (ListView) findViewById(R.id.archive_listview);

        archiveListView.setAdapter(adapter);
        archiveListView.setItemsCanFocus(false);
        registerForContextMenu(archiveListView);

        Intent intent = getIntent();
        String[] toArchive = intent
                .getStringArrayExtra(MainActivity.EXTRA_MESSAGE);
        if (toArchive[1].equals("true") == true)
        {
            adapter.add(toArchive[0]);
            int position = adapter.getPosition(toArchive[0]);
            archiveListView.setItemChecked(position, true);
        }
        else
            adapter.add(toArchive[0]);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.archive, menu);
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
            case R.id.unarchive_option:
                unarchiveItem(position, v);
                return true;
         //   case R.id.email_option:
                // emailItem();
           //     return true;
           // case R.id.action_settings:
                // accessSettings();
             //   return true;
            //case R.id.summary_option:
                // accessSummary();
              //thid  return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void unarchiveItem(int position, View v)
    {
        Intent backToMainIntent = new Intent(this, MainActivity.class);

        CheckedTextView item = (CheckedTextView) v;
        if (item.isChecked())
        {
            String[] unArchive = { archivedList.get(position).toString(),
                    "true" };
            backToMainIntent.putExtra(EXTRA_UNARCHIVEDITEM, unArchive);
            setResult(Activity.RESULT_OK, backToMainIntent);
            finish();
        }
        else
        {
            String[] unArchive = { archivedList.get(position).toString(),
                    "false" };
            backToMainIntent.putExtra(EXTRA_UNARCHIVEDITEM, unArchive);
            setResult(Activity.RESULT_OK, backToMainIntent);
            finish();
        }
    }

    // Adapted from student-picker
    private void deleteItem(int position)
    {
        AlertDialog.Builder deladb = new AlertDialog.Builder(
                ArchiveActivity.this);
        deladb.setMessage("Delete " + archivedList.get(position).toString()
                + "?");
        deladb.setCancelable(true);
        final int finalPosition = position;
        deladb.setPositiveButton("Delete", new OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                archivedList.remove(finalPosition);
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
