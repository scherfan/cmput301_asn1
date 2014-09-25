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
import android.util.Log;
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
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends Activity
{

	private static final String TAG = "DEBUG";

	// save file for the list of todos
	protected static final String TODOFILENAME = "todofile.sav";

	// save file to track items that are checked or not
	protected static final String CHECKFILENAME = "checkfile.sav";

	public final static String EXTRA_MESSAGE = "com.example.cmput301_asn1";

	protected static ListView todoListView;

	// Stores todo items
	protected static ArrayList<String> todoList;

	// Stores strings to keep track of checked items
	protected static ArrayList<String> checkListItem;

	protected static ArrayAdapter<String> todoAdapter;

	protected EditText todoText;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initializes the listview and registers it for the context menu
		todoListView = (ListView) findViewById(R.id.todo_listview);
		registerForContextMenu(todoListView);
		todoListView.setItemsCanFocus(false);

		todoText = (EditText) findViewById(R.id.addTodoText);
		Button add_button = (Button) findViewById(R.id.addButton); // Add button

		/*
		 * Waits for add button to be clicked and then converts text in the edit
		 * test to a string and puts it in the array list. CheckListItem is used
		 * as storage to keep track if a todo is checked or not this way it
		 * keeps the checks. Initializes it as unchecked then saves in correct
		 * files.
		 */
		add_button.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				String todoString = todoText.getText().toString();
				todoList.add(todoString);
				todoAdapter.notifyDataSetChanged();
				checkListItem.add("false");
				saveInTodoFile();
				saveInCheckFile();
			}
		});

		/*
		 * Listens to see if an item is checked off and updates the check
		 * tracking list.
		 */
		todoListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
			        int position, long id)
			{
				CheckedTextView item = (CheckedTextView) view;
				if (item.isChecked())
					checkListItem.set(position, "true");
				else
					checkListItem.set(position, "false");
				saveInCheckFile();
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart() Loads items and initializes adapter,
	 * also checks items if they were checked earlier. Idea and style borrowed
	 * from the work done in the lab. https://github.com/joshua2ua/lonelyTwitter
	 */
	@Override
	protected void onResume()
	{
		super.onResume();
		loadFromTodoFile();
		loadFromCheckFile();

		if (todoList == null)
			todoList = new ArrayList<String>();
		if (checkListItem == null)
			checkListItem = new ArrayList<String>();

		todoAdapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_list_item_multiple_choice, todoList);
		todoListView.setAdapter(todoAdapter);

		for (int i = 0; i < checkListItem.size(); i++)
		{
			if (checkListItem.get(i).equals("true") == true)
				todoListView.setItemChecked(i, true);
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	        ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem) If
	 * one of the context items gets clicked this method delegates to the proper
	 * method.
	 */
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
		Intent intent = new Intent(MainActivity.this, ArchiveActivity.class);

		CheckedTextView item = (CheckedTextView) v;
		if (ArchiveActivity.archivedList != null
		        && ArchiveActivity.checkArchiveItem != null)
		{
			if (item.isChecked())
			{

				String[] toArchive = { todoList.get(position).toString(),
				        "true" };
				todoList.remove(position);
				todoAdapter.notifyDataSetChanged();
				saveInTodoFile();
				checkListItem.remove(position);
				saveInCheckFile();
				ArchiveActivity.archivedList.add(toArchive[0]);
				ArchiveActivity.adapter.notifyDataSetChanged();
				saveInArchiveFile();
				ArchiveActivity.checkArchiveItem.add(toArchive[1]);
				saveInArchiveCheckFile();
				startActivity(intent);
			}
			else
			{
				String[] toArchive = { todoList.get(position).toString(),
				        "false" };
				todoList.remove(position);
				todoAdapter.notifyDataSetChanged();
				saveInTodoFile();
				checkListItem.remove(position);
				saveInCheckFile();
				ArchiveActivity.archivedList.add(toArchive[0]);
				ArchiveActivity.adapter.notifyDataSetChanged();
				saveInArchiveFile();
				ArchiveActivity.checkArchiveItem.add(toArchive[1]);
				saveInArchiveCheckFile();
				startActivity(intent);
			}
		}
		else if (ArchiveActivity.archivedList == null
		        && ArchiveActivity.checkArchiveItem == null)
		{
			Log.v(TAG, "AM i gettting here?");
			if (item.isChecked())
			{

				String[] toArchive = { todoList.get(position).toString(),
				        "true" };
				intent.putExtra(EXTRA_MESSAGE, toArchive);
				todoList.remove(position);
				todoAdapter.notifyDataSetChanged();
				saveInTodoFile();
				checkListItem.remove(position);
				saveInCheckFile();
				startActivity(intent);
			}
			else
			{
				String[] toArchive = { todoList.get(position).toString(),
				        "false" };
				intent.putExtra(EXTRA_MESSAGE, toArchive);
				todoList.remove(position);
				todoAdapter.notifyDataSetChanged();
				saveInTodoFile();
				checkListItem.remove(position);
				saveInCheckFile();
				startActivity(intent);
			}
		}
		else
			return;
	}

	private void saveInArchiveFile()
	{
		try
		{
			FileOutputStream fos = openFileOutput(
			        ArchiveActivity.ARCHIVEFILENAME, 0);
			Gson gson = new Gson();
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(ArchiveActivity.archivedList, osw);
			osw.flush();
			fos.close();
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

	private void saveInArchiveCheckFile()
	{
		try
		{
			FileOutputStream fos = openFileOutput(
			        ArchiveActivity.CHECKARCHIVEFILENAME, 0);
			Gson gson = new Gson();
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(ArchiveActivity.checkArchiveItem, osw);
			osw.flush();
			fos.close();
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
				todoAdapter.notifyDataSetChanged();
				saveInTodoFile();
				checkListItem.remove(finalPosition);
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

	// taken from lonely twitter

	private void loadFromTodoFile()
	{
		try
		{
			FileInputStream fis = openFileInput(TODOFILENAME);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			Gson gson = new Gson();
			// Following was from:
			// https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html
			Type listType = new TypeToken<ArrayList<String>>()
			{
			}.getType();
			todoList = gson.fromJson(in, listType);
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

	// taken from lonelytwitter
	private void saveInTodoFile()
	{
		try
		{
			FileOutputStream fos = openFileOutput(TODOFILENAME, 0);
			Gson gson = new Gson();
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(todoList, osw);
			osw.flush();
			fos.close();
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

	// taken from lonely twitter

	private void loadFromCheckFile()
	{
		try
		{
			FileInputStream fis = openFileInput(CHECKFILENAME);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			Gson gson = new Gson();
			// Following was from:
			// https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html
			Type listType = new TypeToken<ArrayList<String>>()
			{

			}.getType();
			checkListItem = gson.fromJson(in, listType);
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

	// taken from lonelytwitter
	private void saveInCheckFile()
	{
		try
		{
			FileOutputStream fos = openFileOutput(CHECKFILENAME, 0);
			Gson gson = new Gson();
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(checkListItem, osw);
			osw.flush();
			fos.close();
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

	public void viewArchive(MenuItem menu)
	{
		Intent intent = new Intent(MainActivity.this, ArchiveActivity.class);
		startActivity(intent);
	}

	public void emailItem(MenuItem menu)
	{
		Intent intent = new Intent(MainActivity.this, EmailActivity.class);
		startActivity(intent);
	}

	public void viewSummary(MenuItem menu)
	{
		Intent intent = new Intent(MainActivity.this, SummaryActivity.class);
		startActivity(intent);
	}

}