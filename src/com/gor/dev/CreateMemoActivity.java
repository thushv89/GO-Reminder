package com.gor.dev;
import java.io.File;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gor.dev.entities.Memo;
import com.gor.dev.util.IOHandler;
import com.gor.dev.util.CommonUtils;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


/**	This class creates the activity, which enables the users to create new memos 
 * @author Thushan
 *
 */
public class CreateMemoActivity extends Activity{


	String subjectText;	//text disappears when select location is clicked there for text needs to be stored and loaded
	String workingDir="";

	double[] location;
	String locationAddress;
	Boolean isEditScreen=false;
	/**	This method validates the view before save is performed. If not validated 
	 * false will be returned.
	 * @return	tells whether all the necessary fields are filled
	 */
	private boolean validate(){
		if(subjectET.getText().toString()==null || "".equals(subjectET.getText().toString())){
			return false;
		}
		String dateRegExp="^(0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])[- /.](19|20)\\d\\d$";
		String timeRegExp="(1[012]|[1-9]):[0-5][0-9](\\s)?(?i)(am|pm)";
		
		Matcher matcherObj = Pattern.compile(dateRegExp).matcher(memoDate.getText().toString());

		if (!matcherObj.matches())
		{
			return false;
		}
		matcherObj = Pattern.compile(timeRegExp).matcher(memoTime.getText().toString());
		if (!matcherObj.matches())
		{
			return false;
		}
		return true;
	}
	
	/**This class takes care of events to trigger for each button click
	 * @author Thushan
	 *
	 */
	private class ButtonHandler implements View.OnClickListener
	{
		public void onClick(View v)
		{		
			
			if(v.getId()==selectLocB.getId()){
				Memo memo=makeAMemoNow("temp_id");
				IOHandler.WriteObject(memo, workingDir+"/"+"temp.mem");
				startActivity(new Intent(v.getContext(),MyLocationActivity.class));
			}
			else if(v.getId()==dateTimeCB.getId()){
				if(dateTimeCB.isChecked()){
					memoDate.setEnabled(true);
					memoTime.setEnabled(true);
				}else{
					memoDate.setEnabled(false);
					memoTime.setEnabled(false);
				}
			}
			else if(v.getId()==saveMemoB.getId()){	
				Boolean validated=validate();
				if(validated){
					String currentTimeDateStr=CommonUtils.getCurrentDateTimeString();
					Memo memo=makeAMemoNow(currentTimeDateStr);
					IOHandler.createDirIfNotExist(getFilesDir(), "Memos");
					IOHandler.WriteObject(memo, 
							workingDir+"/"+currentTimeDateStr+".mem");
					Toast.makeText(v.getContext(), "Memo saved successfully", Toast.LENGTH_SHORT).show();
					startActivity(new Intent(v.getContext(),WelcomeActivity.class));
				}
				else{
					Toast.makeText(v.getContext(), "Please enter valid information", Toast.LENGTH_SHORT).show();
				}
			}
		}

	}

	EditText subjectET;
	EditText memoDate;
	EditText memoTime;
	Button selectLocB;
	Button saveMemoB;
	TextView locV;
	CheckBox dateTimeCB;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		ScrollView sView=(ScrollView)findViewById(R.id.scrollView1);
		setContentView(R.layout.creatememo);
		workingDir=getFilesDir()+"/Memos";
		
		subjectET=(EditText)findViewById(R.id.subjectET);
		locV=(TextView)findViewById(R.id.locDetails);
		saveMemoB=(Button)findViewById(R.id.saveB);
		dateTimeCB=(CheckBox)findViewById(R.id.setDateTimeCB);
		
		memoDate=(EditText)findViewById(R.id.dateET);
		memoTime=(EditText)findViewById(R.id.timeEt);
		memoDate.setEnabled(false);
		memoTime.setEnabled(false);
		
		selectLocB=(Button)findViewById(R.id.selectLocB);
			
		Object temp;
		if((temp= IOHandler.ifExistDelete(workingDir, "temp.mem"))!=null){
			fillViewsWithMemo((Memo)temp);
		}

		selectLocB.setOnClickListener(new ButtonHandler());
		saveMemoB.setOnClickListener(new ButtonHandler());
		dateTimeCB.setOnClickListener(new ButtonHandler());

		Bundle extras=getIntent().getExtras();
		if(extras!=null){
			location = extras.getDoubleArray("selectedCoordinates");
			locationAddress=extras.getString("selectedLocAddress");
			if(location!=null){
				
				locV.setText(CommonUtils.getFormattedLocationString(location, locationAddress));
			}			
		}
		
		Memo editMemo=(Memo)IOHandler.ifExistDelete(workingDir, "temp.mem");
		if(editMemo!=null){
			fillViewsWithMemo(editMemo);
			isEditScreen=true;
		}
	}


	/**This method fills the activity views with data
	 * @param m	m is the Memo used to fill the views with.
	 */
	private void fillViewsWithMemo(Memo m){
		subjectET.setText(m.getSubject());
		memoDate.setText(m.getDate());
		memoTime.setText(m.getTime());
		
		if(m.isDateTimeEnabled()){
			memoDate.setEnabled(true);
			memoTime.setEnabled(true);
		}else{
			memoDate.setEnabled(false);
			memoTime.setEnabled(false);
		}
		
		if(m.getCoordinates()!=null){
		location=m.getCoordinates();
		locV.setText("Lat:"+location[0]+", Lon:"+location[1]);
		}
	}

	/**Returns a instance of memo with currently available data on views
	 * @return	New instance of memo created
	 */
	private Memo makeAMemoNow(String id){
		
		String timeStr=memoTime.getText().toString();
		String dateStr=memoDate.getText().toString();
		Boolean dateTimeEnabled=false;
		if(dateTimeCB.isChecked()){
			dateTimeEnabled=true;
		}
		Memo memo=new Memo(id,subjectET.getText().toString(),dateStr,timeStr,dateTimeEnabled,location);

		return memo;
	}

}
