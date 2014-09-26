cherfan-To Do List
============
```
Name: Steven Cherfan
CCID: cherfan
App Name: cherfan-To Do List
Workspace Name: cmput301_asn1
```

A simple todo list app on Android.

This app is capable of adding to do tasks and checking them off when the task is completed; it is
also capable of archiving tasks for later reference. From both activities you can also delete tasks
as well as move them back and forth between archived and in the main todo view. The app will allow you
to send a selection of tasks from the todo view and archive view in an email with an email client.
Finally you can view statistics of the tasks you have in the app.

To operate and navigate around the app the main activity allows you to add tasks to a scrollable list
by typing the task in the text box and clicking the add button. To check off the completed task just 
click once. By clicking an holding on an item the app will bring up a contextual menu allowing for
deleting or archiving of tasks. Clicking on the menu bar in the upper right corner the app will
give you access to emailing tasks, viewing the archive screen, viewing the summary screen and 
accessing settings(which haven't been implemented in this release).

After either clicking on the view archive button or long clicking on a task and selecting archive
the app will bring you to the archive screen. By selecting the archive button while long clicking
on a task the app will remove it from the main list and bring it to this screen. This screen has
the same options in the upper right corner as all other screens. Clicking on a task will check or
uncheck it while long clicking a task will give the options of deleting a task or unarchiving the
task. Unarchiving will bring the task back to the main screen list.

The email screen will allow you to select all or some tasks available in the main list or in the archive
list and sending those tasks to an email client on the device for emailing. Clicking the tasks and then
clicking the email button at the bottom of the screen will do this.  

Style and code were adapted from various sources and their URLs have been referenced. Style involving the Android
API and general Android development style (e.g., using intents, etc.) was adapted from the Android Developer tutorials:
http://developer.android.com/index.html
