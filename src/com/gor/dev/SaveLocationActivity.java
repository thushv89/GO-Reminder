package com.gor.dev;

import com.gor.dev.entities.Location;
import com.gor.dev.util.LocationOrganizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**SaveLocationActivity displays the UI to save a location of interest of user 
 * @author Thushan
 *
 */
public class SaveLocationActivity extends Activity {

	Button backB;	//references for various views present in the layout
	Button saveB;	//
	EditText nameET;	//
	Spinner categorySP;	//
	EditText categoryET;	//	
	EditText descriptionET;	//

	private class ButtonHandler implements View.OnClickListener
	{
		public void onClick(View v)
		{		
			if(v.getId()==saveB.getId()){
				LocationOrganizer.writeLocation(getBaseContext(),getCurrentLocation());
				startActivity(new Intent(v.getContext(),MyLocationActivity.class));
			}
			else if(v.getId()==backB.getId()){
				startActivity(new Intent(v.getContext(),MyLocationActivity.class));
			}
		}


	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//initial setup
		super.onCreate(savedInstanceState);
		setContentView(R.layout.savelocation);
		backB = (Button) findViewById(R.id.backB);
		saveB = (Button) findViewById(R.id.saveB);
		nameET = (EditText) findViewById(R.id.nameET);
		categorySP=(Spinner) findViewById(R.id.categorySP);

		//Adapter for the spinner
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this,R.array.category_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categorySP.setAdapter(adapter);

		categoryET = (EditText) findViewById(R.id.categoryET);
		descriptionET = (EditText) findViewById(R.id.descriptionET);

		saveB.setOnClickListener(new ButtonHandler());
		backB.setOnClickListener(new ButtonHandler());
	}

	/**Creates an instance of location using data present in the activity
	 * @return a location object made from data present in the activity currently 
	 */
	private Location getCurrentLocation() {
		Location loc=new Location();
		loc.setName(nameET.getText().toString());
		loc.setDescription(descriptionET.getText().toString());
		if(categorySP.getSelectedItemPosition()==0){
			loc.setCategory("Shopping");
		}else if(categorySP.getSelectedItemPosition()==1){
			loc.setCategory("Family");
		}else if(categorySP.getSelectedItemPosition()==2){
			loc.setCategory("Work");
		}else if(categorySP.getSelectedItemPosition()==3){
			loc.setCategory("Friends");
		}else if(categorySP.getSelectedItemPosition()==4){
			loc.setCategory("Other");
		}

		return loc;
	}
}
