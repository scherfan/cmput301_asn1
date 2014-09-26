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
