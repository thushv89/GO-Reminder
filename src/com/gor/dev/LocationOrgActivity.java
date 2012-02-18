package com.gor.dev;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.gor.dev.entities.Location;
import com.gor.dev.util.LocationOrganizer;
import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

/**This class presents the views information of saved locations to display
 * @author Thushan
 * 
 */
public class LocationOrgActivity extends ExpandableListActivity {

	String groupName;
	String subItemTitleName;
	String subItemDescName;
	ArrayList<Location> locs=new ArrayList<Location>();

	/**These are the categories displayed in the ExpandableListView
	 * 
	 */
	private String categories[] = {"Family","Shopping","Friends","Work","Other"};

	/*private String locationPerCategory[][] = {
			{"Name1","desc1","Name2","desc2","Name3","desc3"},
			{"Name4","desc4","Name5","desc5","Name6","desc6"},
			{"Name7","desc7","Name8","desc8","Name9","desc9"},
	};*/

	/**This list keeps all the loaded locations from memory in a easy-to-access
	 * way for the adapter
	 * 
	 */
	private ArrayList<ArrayList<String>> locationInfo=new ArrayList<ArrayList<String>>();

	/** Called when the activity is first created. */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationorg);

		groupName="location_category";
		subItemTitleName="location_name";
		subItemDescName="location_description";

		//load all the locations saved in the memory
		locs=LocationOrganizer.getAllLocations(getBaseContext());
		makeLocationList();

		//Handles the expandable list view items and the view itself 
		SimpleExpandableListAdapter myListAdapter = new SimpleExpandableListAdapter(getBaseContext(), 
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
		System.out.println("Inside onChildClick at groupPosition = " + groupPosition +" Child clicked at position " + childPosition);
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
}
