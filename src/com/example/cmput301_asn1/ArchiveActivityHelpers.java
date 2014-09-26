package com.example.cmput301_asn1;

import java.util.ArrayList;


public class ArchiveActivityHelpers extends ArchiveActivity
{
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
