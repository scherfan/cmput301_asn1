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

		// Link to listviews
		emailTodoListView = (ListView) findViewById(R.id.emailtodolist);
		emailArchiveListView = (ListView) findViewById(R.id.emailarchivelist);

		// Initialize array lists
		eTodoList = new ArrayList<String>();
		eArchiveList = new ArrayList<String>();

		// Grab lists if they exist
		if (MainActivityHelpers.giveList() != null)
			eTodoList = MainActivityHelpers.giveList();
		if (ArchiveActivityHelpers.giveList() != null)
			eArchiveList = ArchiveActivityHelpers.giveList();

		// Initialize list used for sending to email client
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
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * Adapted from: fiXedd on StackOverflow
	 * http://stackoverflow.com/questions/2197741/how-can-i-send-emails-from-my-
	 * android-application 
	 * Using an intent this method sends the list of todos
	 * that the user wants to email in the form of a string. It looks for
	 * suitable apps to send the information to.
	 */
	public void emailItems(View view)
	{
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("message/rfc822");
		intent.putExtra(Intent.EXTRA_EMAIL,
		        new String[] { "recipient@example.com" });
		intent.putExtra(Intent.EXTRA_SUBJECT, " Enter subject of email");
		getChecked();
		intent.putExtra(Intent.EXTRA_TEXT, finalList.toString());
		try
		{
			startActivity(Intent.createChooser(intent, "Send mail..."));
		}
		catch (android.content.ActivityNotFoundException ex)
		{
			Toast.makeText(this, "There are no email clients installed.",
			        Toast.LENGTH_SHORT).show();
		}
	}

	/*
	 * Since the position in the SparseBooleanArrays coincide with the their
	 * respective list item this method gets which items were checked and
	 * iterates through both lists and stores those checked items in a new list.
	 */
	protected void getChecked()
	{
		SparseBooleanArray checkedTodo = emailTodoListView
		        .getCheckedItemPositions();
		SparseBooleanArray checkedArchive = emailArchiveListView
		        .getCheckedItemPositions();
		for (int i = 0; i < emailTodoListView.getAdapter().getCount(); i++)
		{
			if (checkedTodo.get(i))
				finalList
				        .add(emailTodoListView.getItemAtPosition(i).toString());
		}
		for (int i = 0; i < emailArchiveListView.getAdapter().getCount(); i++)
		{
			if (checkedArchive.get(i))
				finalList.add(emailArchiveListView.getItemAtPosition(i)
				        .toString());
		}
	}

	// Change activity methods

	public void viewArchive(MenuItem menu)
	{
		Intent intent = new Intent(EmailActivity.this, ArchiveActivity.class);
		startActivity(intent);
	}

	public void viewSummary(MenuItem menu)
	{
		Intent intent = new Intent(EmailActivity.this, SummaryActivity.class);
		startActivity(intent);
	}

	public void settings(MenuItem menu)
	{
		Intent intent = new Intent(EmailActivity.this, SettingsActivity.class);
		startActivity(intent);
	}
}
