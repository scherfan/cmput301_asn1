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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class SummaryActivity extends Activity
{

	protected TextView todoTotalView;
	protected TextView todoCheckedView;
	protected TextView todoUncheckedView;
	protected TextView archiveTotalView;
	protected TextView archiveCheckedView;
	protected TextView archiveUncheckedView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summary);

		todoTotalView = (TextView) findViewById(R.id.todoTotal);
		todoCheckedView = (TextView) findViewById(R.id.todoChecked);
		todoUncheckedView = (TextView) findViewById(R.id.todoUnchecked);
		archiveTotalView = (TextView) findViewById(R.id.archiveTotal);
		archiveCheckedView = (TextView) findViewById(R.id.archiveChecked);
		archiveUncheckedView = (TextView) findViewById(R.id.archiveUnchecked);
	}

	/*
	 * Updates the stats every time the activity resumes.
	 */
	@Override
	public void onResume()
	{
		super.onResume();
		todoTotalView.setText("Total number of to do items: " + getTodoTotal());
		todoCheckedView.setText("Number of checked to do items: "
		        + getTodoChecked());
		todoUncheckedView.setText("Number of unchecked to do items: "
		        + getTodoUnchecked());
		archiveTotalView.setText("Total number of archived items: "
		        + getArchiveTotal());
		archiveCheckedView.setText("Number of checked archived items: "
		        + getArchiveChecked());
		archiveUncheckedView.setText("Number of unchecked archived items: "
		        + getArchiveUnchecked());
	}

	private int getArchiveUnchecked()
	{
		return ArchiveActivityHelpers.giveUnchecked();
	}

	private int getArchiveChecked()
	{
		return ArchiveActivityHelpers.giveChecked();
	}

	private int getArchiveTotal()
	{
		return ArchiveActivityHelpers.giveTotal();
	}

	private int getTodoUnchecked()
	{
		return MainActivityHelpers.giveUnchecked();
	}

	private int getTodoChecked()
	{
		return MainActivityHelpers.giveChecked();
	}

	private int getTodoTotal()
	{
		return MainActivityHelpers.giveTotal();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.summary, menu);
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

	public void viewArchive(MenuItem menu)
	{
		Intent intent = new Intent(SummaryActivity.this, ArchiveActivity.class);
		startActivity(intent);
	}

	public void emailItem(MenuItem menu)
	{
		Intent intent = new Intent(SummaryActivity.this, EmailActivity.class);
		startActivity(intent);
	}

	public void settings(MenuItem menu)
	{
		Intent intent = new Intent(SummaryActivity.this, SettingsActivity.class);
		startActivity(intent);
	}
}
