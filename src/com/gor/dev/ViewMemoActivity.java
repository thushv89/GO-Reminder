package com.gor.dev;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.gor.dev.entities.Memo;
import com.gor.dev.util.IOHandler;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
	
	/** Called when the activity is first created. */
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		//setContentView(R.layout.viewmemo);
		//ListView lView=(ListView) findViewById(R.id.listView1);
		memoSubjs.addAll(Arrays.asList(IOHandler.listItemsInDir(getFilesDir()+"/Memos")));

		//set up the adapter
		adapter=new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1,memoSubjs);

		setListAdapter(adapter);

		//Context menu appears when "long clicked" on an item in the list view
		registerForContextMenu(getListView());

	}

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
			Memo currentMemo=(Memo)IOHandler.ReadObject(getFilesDir()+"/Memos",selectedListItemName);
			Intent createMemo=new Intent(getBaseContext(),CreateMemoActivity.class);
			createMemo.putExtra("editMemo", currentMemo);
			startActivity(createMemo);
			//if user selecte the second item from the menu
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
		Memo currentMemo=(Memo)IOHandler.ReadObject(getFilesDir()+"/Memos",item);		
		if(currentMemo==null){
			Toast.makeText(this,"Error has occured", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(this,currentMemo.getSubject(), Toast.LENGTH_LONG).show();
		}
	}
}
