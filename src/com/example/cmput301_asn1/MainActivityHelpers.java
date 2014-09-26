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

/*
 * Helper methods for the main activity
 */

public class MainActivityHelpers extends MainActivity
{

	public static ArrayList<String> giveList()
	{
		return todoList;
	}

	public static int giveTotal()
	{
		if (todoListView != null)
			return todoListView.getCount();
		else
			return 0;
	}

	public static int giveUnchecked()
	{
		if (todoListView != null)
			return (todoListView.getCount() - todoListView
			        .getCheckedItemCount());
		else
			return 0;
	}

	public static int giveChecked()
	{
		if (todoListView != null)
			return todoListView.getCheckedItemCount();
		else
			return 0;
	}
}
