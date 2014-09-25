
package com.example.cmput301_asn1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ArchiveActivity extends Activity
{

    private static final String ARCHIVEFILENAME = "archivefile.sav";

    private static final String CHECKARCHIVEFILENAME = "checkarchivefile.sav";

    protected static ArrayList<String> archivedList;

    protected ArrayList<String> checkArchiveItem;

    protected ArrayAdapter<String> adapter;

    protected static ListView archiveListView;

    public final static String EXTRA_MESSAGE = "com.example.cmput301_asn1";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        
        if (archivedList != null && checkArchiveItem != null)
        {
            loadFromArchiveFile();
            loadFromCheckFile();
        }
        
        if (archivedList == null)
            archivedList = new ArrayList<String>();
        if (checkArchiveItem == null)
            checkArchiveItem = new ArrayList<String>();

        archiveListView = (ListView) findViewById(R.id.archive_listview);
        archiveListView.setItemsCanFocus(false);
        registerForContextMenu(archiveListView);
        
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, archivedList);
        archiveListView.setAdapter(adapter);

        Intent intent = getIntent();
        int position;
        if (intent.getStringArrayExtra(MainActivity.EXTRA_MESSAGE) != null)
        {
            String[] toArchive = intent
                    .getStringArrayExtra(MainActivity.EXTRA_MESSAGE);
            if (toArchive[1].equals("true") == true)
            {
                archivedList.add(toArchive[0]);
                adapter.notifyDataSetChanged();
                checkArchiveItem.add(toArchive[1]);
                position = adapter.getPosition(toArchive[0]);
                archiveListView.setItemChecked(position, true);
                saveInArchiveFile();
                saveInCheckFile();
            }
            else
            {
                archivedList.add(toArchive[0]);
                adapter.notifyDataSetChanged();
                checkArchiveItem.add(toArchive[1]);
                saveInArchiveFile();
                saveInCheckFile();

            }


            for (int i = 0; i < checkArchiveItem.size(); i++)
            {
                if (checkArchiveItem.get(i).equals("true") == true)
                    archiveListView.setItemChecked(i, true);
            }
        }
        
        archiveListView.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id)
            {
                CheckedTextView item = (CheckedTextView) view;
                if (item.isChecked())
                    checkArchiveItem.set(position, "true");
                else
                    checkArchiveItem.set(position, "false");
                saveInCheckFile();
            }
        });

    }

    private void saveInCheckFile()
    {
        // TODO Auto-generated method stub
        try
        {
            FileOutputStream fos = openFileOutput(CHECKARCHIVEFILENAME, 0);
            Gson gson = new Gson();
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            gson.toJson(checkArchiveItem, osw);
            osw.flush();
            fos.close();
        }
        catch (FileNotFoundException e)
        { // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // taken from lonelyTwitter

    @Override
    protected void onStart()
    {
        super.onStart();
        loadFromArchiveFile();
        loadFromCheckFile();
        if (archivedList == null)
            archivedList = new ArrayList<String>();
        if (checkArchiveItem == null)
            checkArchiveItem = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, archivedList);
        archiveListView.setAdapter(adapter);

        for (int i = 0; i < checkArchiveItem.size(); i++)
        {
            if (checkArchiveItem.get(i).equals("true") == true)
                archiveListView.setItemChecked(i, true);
        }
    }

    // taken from lonely twitter

    private void loadFromCheckFile()
    {
        // TODO Auto-generated method stub
        try
        {
            FileInputStream fis = openFileInput(CHECKARCHIVEFILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            // Following was from:
            // https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html
            Type listType = new TypeToken<ArrayList<String>>()
            {
            }.getType();
            checkArchiveItem = gson.fromJson(in, listType);
        }
        catch (FileNotFoundException e)
        { // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        { // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void loadFromArchiveFile()
    {
        try
        {
            FileInputStream fis = openFileInput(ARCHIVEFILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            // Following was from:
            // https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html
            Type listType = new TypeToken<ArrayList<String>>()
            {
            }.getType();
            archivedList = gson.fromJson(in, listType);
        }
        catch (FileNotFoundException e)
        { // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        { // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // taken from lonelytwitter
    private void saveInArchiveFile()
    {
        try
        {
            FileOutputStream fos = openFileOutput(ARCHIVEFILENAME, 0);
            Gson gson = new Gson();
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            gson.toJson(archivedList, osw);
            osw.flush();
            fos.close();
        }
        catch (FileNotFoundException e)
        { // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu Menu, View view,
            ContextMenuInfo MenuInfo)
    {
        super.onCreateContextMenu(Menu, view, MenuInfo);
        MenuInflater Inflater = getMenuInflater();
        Inflater.inflate(R.menu.archive_context, Menu);
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
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void unarchiveItem(int position, View v)
    {
        Intent backToMainIntent = new Intent();

        CheckedTextView item = (CheckedTextView) v;
        if (item.isChecked())
        {
            String[] unArchive = { archivedList.get(position).toString(),
                    "true" };
            backToMainIntent.putExtra(EXTRA_MESSAGE, unArchive);
            setResult(Activity.RESULT_OK, backToMainIntent);
            archivedList.remove(position);
            adapter.notifyDataSetChanged();
            // saveInFile();
            finish();
        }
        else
        {
            String[] unArchive = { archivedList.get(position).toString(),
                    "false" };
            backToMainIntent.putExtra(EXTRA_MESSAGE, unArchive);
            setResult(Activity.RESULT_OK, backToMainIntent);
            archivedList.remove(position);
            adapter.notifyDataSetChanged();
            // saveInFile();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.archive, menu);
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

    public void emailItemFromArchive(MenuItem menu)
    {
        Intent intent = new Intent(ArchiveActivity.this, EmailActivity.class);
        startActivity(intent);
    }

    public void viewSummaryFromArchive(MenuItem menu)
    {
        Intent intent = new Intent(ArchiveActivity.this, SummaryActivity.class);
        startActivity(intent);
    }

    public static ArrayList<String> giveList()
    {
        return archivedList;
    }

    public static int giveUnchecked()
    {
        if (archiveListView != null)
            return (archiveListView.getCount() - archiveListView
                    .getCheckedItemCount());
        else
            return 0;
    }

    public static int giveChecked()
    {
        if (archiveListView != null)
            return archiveListView.getCheckedItemCount();
        else
            return 0;
    }

    public static int giveTotal()
    {
        if (archiveListView != null)
            return archiveListView.getCount();
        else
            return 0;
    }
}
