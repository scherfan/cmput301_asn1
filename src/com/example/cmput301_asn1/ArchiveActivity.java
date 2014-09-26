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
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ArchiveActivity extends Activity
{

	// File for storing the archived items
	protected static final String ARCHIVEFILENAME = "archivefile.sav";

	// File for keeping the checks persistent
	protected static final String CHECKARCHIVEFILENAME = "checkarchivefile.sav";

	// Holds archived items
	protected static ArrayList<String> archivedList;

	// Used to keep track which archived items were checked
	protected static ArrayList<String> checkArchiveItem;

	protected static ArrayAdapter<String> adapter;

	protected static ListView archiveListView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_archive);
		archiveListView = (ListView) findViewById(R.id.archive_listview);
		archiveListView.setItemsCanFocus(false);
		registerForContextMenu(archiveListView);

		/*
		 * If an item is being passed to here to be archived from main activity
		 * where the app is being run for the first time or after calling
		 * onDestroy() this block handles the transfering and storing of the
		 * item here. This avoids the null pointer error from trying to access
		 * an array list before it is initialized from main activity.
		 */
		if (archivedList == null || checkArchiveItem == null)
		{
			// Initializes array lists if they have not been initialized yet
			archivedList = new ArrayList<String>();
			checkArchiveItem = new ArrayList<String>();

			// Initializes the adapter
			adapter = new ArrayAdapter<String>(this,
			        android.R.layout.simple_list_item_multiple_choice,
			        archivedList);
			archiveListView.setAdapter(adapter);

			Intent intent = getIntent();
			int position;
			if (intent.getStringArrayExtra(MainActivity.EXTRA_MESSAGE) != null)
			{

				// toArchive[0] is the item to be archived.
				// toArchvie[1] is whether or not it was checked before being
				// archived.
				String[] toArchive = intent
				        .getStringArrayExtra(MainActivity.EXTRA_MESSAGE);

				if (toArchive[1].equals("true") == true)
				{

					/*
					 * If it is checked in main then store the string and update
					 * listview. Update the array list that tracks checked items
					 * and save to file.
					 */
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
					// Save to file as unchecked
					archivedList.add(toArchive[0]);
					adapter.notifyDataSetChanged();
					checkArchiveItem.add(toArchive[1]);
					saveInArchiveFile();
					saveInCheckFile();

				}
			}
		}

		/*
		 * Listens for user clicking an item to check it and saves it to file.
		 */
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

	/*
	 * Handles saving the array list that tracks which items are checked. All
	 * methods that handle saving to and loading from a file was borrowed from
	 * joshua2ua and our work in the lab
	 * https://github.com/scherfan/lonelyTwitter/tree/f14iot
	 */
	private void saveInCheckFile()
	{
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

	// https://github.com/scherfan/lonelyTwitter/tree/f14iot
	@Override
	protected void onResume()
	{
		super.onResume();
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

	// https://github.com/scherfan/lonelyTwitter/tree/f14iot
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
		catch (@SuppressWarnings("hiding") IOException e)
		{ // TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// https://github.com/scherfan/lonelyTwitter/tree/f14iot
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
		catch (@SuppressWarnings("hiding") IOException e)
		{ // TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// https://github.com/scherfan/lonelyTwitter/tree/f14iot
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

	// https://github.com/scherfan/lonelyTwitter/tree/f14iot
	private void saveInTodoFile()
	{
		try
		{
			FileOutputStream fos = openFileOutput(MainActivity.TODOFILENAME, 0);
			Gson gson = new Gson();
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(MainActivity.todoList, osw);
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

	// https://github.com/scherfan/lonelyTwitter/tree/f14iot
	private void saveInTodoCheckFile()
	{
		try
		{
			FileOutputStream fos = openFileOutput(MainActivity.CHECKFILENAME, 0);
			Gson gson = new Gson();
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(MainActivity.checkListItem, osw);
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

	/*
	 * When the user clicks the unarchvie context menu button this method checks
	 * it that archived item was checked, saving that status (unArchvie[1]) in
	 * the respective todo file and updates the todo status list. It also
	 * directly saves and updates todo lists new unarchived (unArchive[0]) item
	 * into the respective lists and files and them removes the item from the
	 * archive list and save file and finishes the activity.
	 */
	private void unarchiveItem(int position, View v)
	{
		CheckedTextView item = (CheckedTextView) v;
		if (item.isChecked())
		{
			String[] unArchive = { archivedList.get(position).toString(),
			        "true" };
			MainActivity.todoList.add(unArchive[0]);
			MainActivity.todoAdapter.notifyDataSetChanged();
			MainActivity.checkListItem.add(unArchive[1]);
			saveInTodoCheckFile();
			saveInTodoFile();
			archivedList.remove(position);
			adapter.notifyDataSetChanged();
			checkArchiveItem.remove(position);
			saveInArchiveFile();
			saveInCheckFile();
			finish();
		}
		else
		{
			String[] unArchive = { archivedList.get(position).toString(),
			        "false" };
			MainActivity.todoList.add(unArchive[0]);
			MainActivity.todoAdapter.notifyDataSetChanged();
			MainActivity.checkListItem.add(unArchive[1]);
			saveInTodoCheckFile();
			saveInTodoFile();
			archivedList.remove(position);
			adapter.notifyDataSetChanged();
			checkArchiveItem.remove(position);
			saveInArchiveFile();
			saveInCheckFile();
			finish();
		}
	}

	/*
	 * Adapted from abramhindle's student-picker
	 * https://github.com/abramhindle/student-picker When the user clicks the
	 * delete button a dialog will give options to delete or cancel. When
	 * deleted the method removes the item from the list and updates the check
	 * status and save files.
	 */
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
				checkArchiveItem.remove(finalPosition);
				saveInArchiveFile();
				saveInCheckFile();
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

	// Change activity methods

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

	public void settings(MenuItem menu)
	{
		Intent intent = new Intent(ArchiveActivity.this, SettingsActivity.class);
		startActivity(intent);
	}
}
