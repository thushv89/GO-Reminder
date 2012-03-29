package com.gor.dev;

import com.gor.dev.entities.Location;
import com.gor.dev.util.CommonUtils;
import com.gor.dev.util.LocationOrganizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**SaveLocationActivity displays the UI to save a location of interest of user 
 * @author Thushan
 *
 */
public class SaveLocationActivity extends Activity {

	//references for various views present in the layout
	Button saveB;	//
	EditText nameET;	//
	Spinner categorySP;	//
	EditText categoryET;	//	
	EditText descriptionET;	//
	TextView locationTV;

	private class ButtonHandler implements View.OnClickListener
	{
		public void onClick(View v)
		{		
			if(v.getId()==saveB.getId()){
				if(validate()){
				LocationOrganizer.writeLocation(getBaseContext(),getCurrentLocation());
				Toast.makeText(getBaseContext(), "Location saved successfully", Toast.LENGTH_LONG).show();
				startActivity(new Intent(v.getContext(),MyLocationActivity.class));
				
				}else{
					Toast.makeText(getBaseContext(), "Please enter valid information", Toast.LENGTH_LONG).show();
				}
			}

			
		}


	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//initial setup
		super.onCreate(savedInstanceState);
		setContentView(R.layout.savelocation);
		saveB = (Button) findViewById(R.id.saveB);
		nameET = (EditText) findViewById(R.id.nameET);
		categorySP=(Spinner) findViewById(R.id.categorySP);

		//Adapter for the spinner
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this,R.array.category_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categorySP.setAdapter(adapter);

		descriptionET = (EditText) findViewById(R.id.descriptionET);
		locationTV=(TextView)findViewById(R.id.locationTV);
		Bundle bundle=getIntent().getExtras();
		double[] coordinates;
		String address;
		if(bundle!=null){
			coordinates=bundle.getDoubleArray("selectedCoordinates");
			address=bundle.getString("selectedLocAddress");
			locationTV.setText(CommonUtils.getFormattedLocationString(coordinates, address));
		}
		saveB.setOnClickListener(new ButtonHandler());
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
	
	private boolean validate(){
		if(nameET.getText()==null || "".equals(nameET.getText().toString())){
			return false;
		}
		if(locationTV.getText()==null || "".equals(locationTV.getText().toString())){
			return false;
		}
		return true;
	}
	
}
