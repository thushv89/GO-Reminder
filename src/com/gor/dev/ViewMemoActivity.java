package com.gor.dev;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import com.gor.dev.entities.Memo;
import com.gor.dev.util.IOHandler;
import com.gor.dev.util.LocationOrganizer;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

/**ViewMemoActivity presents the UI for viewing all the saved memos
 * @author Thushan
 *
 */
public class ViewMemoActivity extends ListActivity {

	ArrayList<String> memoSubjs=new ArrayList<String>();	//Subjects of all the memos present
	ArrayAdapter<String> adapter;	//Adapter for the list view
	String workingDir="";
	Dialog dialog;
	Button dialog_searchB;
	Button dialog_closeB;
	EditText dialog_searchET;
	/** Called when the activity is first created. */
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		//setContentView(R.layout.test);
		workingDir=getFilesDir()+"/Memos";
		//String[] subjects=getAllMemoSubjects(IOHandler.listItemsInDir(workingDir));
		String[] test=IOHandler.listItemsInDir(workingDir);
		Toast.makeText(getBaseContext(), test.length+" Memos found", Toast.LENGTH_SHORT).show();
		memoSubjs.addAll(Arrays.asList(getFormattedFileNames(test)));

		//set up the adapter
		adapter=new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1,memoSubjs);

		setListAdapter(adapter);

		//Context menu appears when "long clicked" on an item in the list view
		registerForContextMenu(getListView());

	}

	//This is for the menu created by clicking menu button
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.view_memo_menu, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==R.id.search){
			
			dialog = new Dialog(this);
			dialog.setContentView(R.layout.view_memo_search_dialog);
			dialog_searchB=(Button)dialog.findViewById(R.id.searchB);
			dialog_closeB=(Button)dialog.findViewById(R.id.closeB);
			dialog_searchET=(EditText)dialog.findViewById(R.id.searchTermET);
			dialog_searchB.setOnClickListener(new DialogButtonHandler());
			dialog_closeB.setOnClickListener(new DialogButtonHandler());
			dialog.setTitle("Search Memos");
			dialog.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			dialog.show();
			
		}else if(item.getItemId()==R.id.delete_all){
			
			 AlertDialog deleteDialog = new AlertDialog.Builder(this)
              .setMessage("Are you sure you want to delete all the memos?")
              .setCancelable(true)
              .setNegativeButton("No", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int id) {
	                       
	                }})
              .setNeutralButton("Yes", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int id) {
                     String[] fileNames=IOHandler.listItemsInDir(workingDir);
                     for(String fileName:fileNames){
                    	 IOHandler.ifExistDelete(workingDir, fileName);
                     }
                     Toast.makeText(getBaseContext(), "All Memos deleted successfully", Toast.LENGTH_LONG).show();
              }
          }).create();
			 deleteDialog.show();
			 clearListView();
		}
		
		return true;
	}
	
	//Handles button events of the search dialog
	class DialogButtonHandler implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(v.getId()==dialog_searchB.getId()){
				//conduct the search
				adapter.clear();
				String search_term=dialog_searchET.getText().toString();
				String[] test=IOHandler.listItemsInDir(workingDir);
				String[] memoFNs=getFormattedFileNames(test);
				int size=memoFNs.length;
				for(String m:memoFNs){
					Memo currentMemo=(Memo)IOHandler.ReadObject(getFilesDir()+"/Memos",reverseFormatting(m));
					if(currentMemo != null && currentMemo.getSubject().contains(search_term)){
						adapter.add(m);
					}
				}
				dialog.dismiss();
			}else if(v.getId()==dialog_closeB.getId()){
				dialog.dismiss();
			}
		}
		
	}
	
	//File names are formatted to show in the form of
	// "Memo mm/dd/yyyy hh:mm:ss" this is more intuitive
	private String[] getFormattedFileNames(String[] fileNames){
		String[] formattedNames=new String[fileNames.length];
		for(int i=0;i<fileNames.length;i++){
			String formattedName="";
			String[] tokens=fileNames[i].split("-");		
			String lastPart=tokens[6].replace(".mem", "");
			formattedName="Memo "+tokens[1]+"/"+tokens[2]+"/"+tokens[3]
			              +" "+tokens[4]+":"+tokens[5]+":"+lastPart;
			formattedNames[i]=formattedName;
		}
		return formattedNames;		
	} 
	
	//reverse the above described string to a raw filename string
	//"Memo mm/dd/yyyy hh:mm:ss" --> Memo-mm-dd-yyyy-hh-mm-ss.mem
	private String reverseFormatting(String formatted){
		String[] tokensAfterSlash=formatted.split("/");
		String part1=tokensAfterSlash[0].split(" ")[0];
		String part2=tokensAfterSlash[0].split(" ")[1];
		String part3=tokensAfterSlash[1];
		
		String[] tokensAfterColon=tokensAfterSlash[2].split(":");
		String part4=tokensAfterColon[0].split(" ")[0];
		String part5=tokensAfterColon[0].split(" ")[1];
		String part6=tokensAfterColon[1];
		String part7=tokensAfterColon[2];
		
		String finalString=part1+"-"+part2+"-"+part3+"-"+part4+"-"+part5+"-"+part6+"-"+part7+".mem";
		return finalString;
	}
	
	//Context menu with 3 menu items which appears on a long click
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		if (v.getId()==getListView().getId()) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
			menu.setHeaderTitle(memoSubjs.get(info.position));
			String[] menuItems = {"Edit","Remove","Close"};
			for (int i = 0; i<menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		int menuItemIndex = item.getItemId();

		String[] menuItems = {"Edit","Remove","Close"};	//menu-items for the context menu

		String selectedMenuItemName = menuItems[menuItemIndex];	//currently selected context menu item name
		String selectedListItemName = memoSubjs.get(info.position); //currently selected item name

		//if user selects the first item from the menu (Edit)
		if(menuItemIndex==0){
			Memo currentMemo=(Memo)IOHandler.ReadObject(getFilesDir()+"/Memos",reverseFormatting(selectedListItemName));
			Intent createMemo=new Intent(getBaseContext(),CreateMemoActivity.class);
			createMemo.putExtra("editMemo", currentMemo);
			//createMemo.putExtra("memoFileName", )
			startActivity(createMemo);
			//if user select the second item from the menu
			//need to delete previous and save (current date n time is filename)
		}else if(menuItemIndex==1){
			IOHandler.ifExistDelete(getFilesDir()+"/Memos", selectedListItemName);
			adapter.remove(adapter.getItem(info.position));
			adapter.notifyDataSetChanged();
		}
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		Memo currentMemo=(Memo)IOHandler.ReadObject(getFilesDir()+"/Memos",reverseFormatting(item));		
		if(currentMemo==null){
			Toast.makeText(this,"Error has occured", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(this,currentMemo.getSubject(), Toast.LENGTH_LONG).show();
		}
	}
	
	private void clearListView(){
		adapter.clear();
		adapter.notifyDataSetChanged();
	}
}
