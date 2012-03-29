package com.gor.dev;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gor.dev.entities.Location;
import com.gor.dev.entities.Memo;
import com.gor.dev.util.IOHandler;
import com.gor.dev.util.LocationOrganizer;

import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

/**This class presents the views information of saved locations to display
 * @author Thushan
 * 
 */
public class LocationOrgActivity extends ExpandableListActivity implements OnItemLongClickListener{

	String groupName;
	String subItemTitleName;
	String subItemDescName;
	ArrayList<Location> locs=new ArrayList<Location>();

	/**These are the categories displayed in the ExpandableListView
	 * 
	 */
	private String categories[] = {"Family","Shopping","Friends","Work","Other"};

	/**This list keeps all the loaded locations from memory in a easy-to-access
	 * way for the adapter
	 * 
	 */
	private ArrayList<ArrayList<String>> locationInfo=new ArrayList<ArrayList<String>>();

	SimpleExpandableListAdapter myListAdapter;
	/** Called when the activity is first created. */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationorg);

		locationInfo.add(new ArrayList<String>());
		locationInfo.add(new ArrayList<String>());
		locationInfo.add(new ArrayList<String>());
		locationInfo.add(new ArrayList<String>());
		locationInfo.add(new ArrayList<String>());

		groupName="location_category";
		subItemTitleName="location_name";
		subItemDescName="location_description";

		//load all the locations saved in the memory
		locs=LocationOrganizer.getAllLocations(getBaseContext());
		makeLocationList();

		//Handles the expandable list view items and the view itself 
		myListAdapter = new SimpleExpandableListAdapter(getBaseContext(), 
				createGroupList(),
				R.layout.group_row, 
				new String[] { groupName },
				new int[] { R.id.categoryTV },		// Data under "groupName" key goes into this TextView
				createChildList(),	// childData describes second-level entries
				R.layout.child_row,	// Layout for second-level entries
				new String[] { subItemTitleName, subItemDescName },	//sub item content
				new int[] { R.id.LocationNameTV, R.id.descriptionTV }	// Data under the keys above go into these TextViews
		);
		setListAdapter(myListAdapter);	
		//registerForContextMenu(getExpandableListView());
		getExpandableListView().setOnItemLongClickListener(this);
	}

	/**Obtain the saved locations from the 'locs' list
	 * and fill 'locationInfo' list with obtained data properly
	 */
	private void makeLocationList(){
		for(Location l:locs){
			//If saved location's category is equal to "family"
			if("family".equals(l.getCategory().toLowerCase())){
				locationInfo.get(0).add(l.getName());
				locationInfo.get(0).add(l.getDescription());
				//If saved location's category is equal to "shopping"	
			}else if("shopping".equals(l.getCategory().toLowerCase())){
				locationInfo.get(1).add(l.getName());
				locationInfo.get(1).add(l.getDescription());
			}else if("friends".equals(l.getCategory().toLowerCase())){
				locationInfo.get(2).add(l.getName());
				locationInfo.get(2).add(l.getDescription());
			}else if("work".equals(l.getCategory().toLowerCase())){
				locationInfo.get(3).add(l.getName());
				locationInfo.get(3).add(l.getDescription());
			}else if("other".equals(l.getCategory().toLowerCase())){
				locationInfo.get(4).add(l.getName());
				locationInfo.get(4).add(l.getDescription());
			}

		}
	}

	/**This creates a list of groups to be displayed in the ExpandableListView 
	 * @return The list of main categories
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List createGroupList() {
		ArrayList result = new ArrayList();
		for( int i = 0 ; i < categories.length ; ++i ) {
			HashMap m = new HashMap();
			m.put( groupName,categories[i] );
			result.add( m );
		}
		return (List)result;
	}

	/**This creates a list of list of sub-items to be displayed in the ExpandableListView
	 * 
	 * @return The list of sub categories
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List createChildList() {
		ArrayList result = new ArrayList();
		for( int i = 0 ; i < locationInfo.size() ; ++i ) {
			ArrayList secList = new ArrayList();
			for( int n = 0 ; n < locationInfo.get(i).size() ; n += 2 ) {
				HashMap child = new HashMap();
				//Enrich the hash map with the entries from the list of lists of location info
				child.put( subItemTitleName, locationInfo.get(i).get(n) );
				child.put( subItemDescName, locationInfo.get(i).get(n+1) );
				secList.add( child );
			}
			result.add( secList );
		}
		return result;
	}
	public void  onContentChanged  () {
		System.out.println("onContentChanged");
		super.onContentChanged();
	}


	/* This function is called on each child click */
	public boolean onChildClick( ExpandableListView parent, View v, int groupPosition,int childPosition,long id) {
		String item = ((Map<String,String>) getExpandableListAdapter().getChild(groupPosition, childPosition)).get(subItemDescName);
		//Location currentMemo=(Location)LocationOrganizer.readLocation(getBaseContext(), );		
		/*if(currentMemo==null){
			Toast.makeText(this,"Error has occured", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(this,currentMemo.getSubject(), Toast.LENGTH_LONG).show();
		}*/
		Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
		return true;
	}

	/* This function is called on expansion of the group */
	public void  onGroupExpand  (int groupPosition) {
		try{
			System.out.println("Group exapanding Listener => groupPosition = " + groupPosition);
		}catch(Exception e){
			System.out.println(" groupPosition Errrr +++ " + e.getMessage());
		}
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		if (v.getId()==getExpandableListView().getId()) {
			ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo)menuInfo;
			int type = ExpandableListView.getPackedPositionType(info.packedPosition);
			if(type==1){
				menu.setHeaderTitle("Options");
				String[] menuItems = {"Edit","Remove","Close"};
				for (int i = 0; i<menuItems.length; i++) {
					menu.add(Menu.NONE, i, i, menuItems[i]);
				}
			}
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> adapter, View arg1, int arg2,
			long id) {
		if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD){
			final String locationName=((Map <String,String>)adapter.getItemAtPosition(arg2)).get(subItemTitleName);
			 AlertDialog deleteDialog = new AlertDialog.Builder(this)
			 	                .setMessage("Are you sure you want to delete "+locationName+"?")
			 	                .setCancelable(true)
			 	                .setNegativeButton("No", new DialogInterface.OnClickListener() {
			 	                    public void onClick(DialogInterface dialog, int id) {
				 	                       
				 	                }})
			 	                .setNeutralButton("Yes", new DialogInterface.OnClickListener() {
			 	                    public void onClick(DialogInterface dialog, int id) {
			 	                       LocationOrganizer.deleteLocation(getBaseContext(),locationName);
			 	                }
			 	            }).create();
			 deleteDialog.show();
			 updateListAfterDelete();
		}
		return false;
	}

	//update the list content after the delete
	//not working still
	private void updateListAfterDelete(){
		locs=LocationOrganizer.getAllLocations(getBaseContext());
		makeLocationList();
		myListAdapter.notifyDataSetChanged();
	}

}
