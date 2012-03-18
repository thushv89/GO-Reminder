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
	/**	This method validates the view before save is performed. If not validated 
	 * false will be returned.
	 * @return	tells whether all the necessary fields are filled
	 */
	private boolean validate(){
		if(subjectET.getText()==null || "".equals(subjectET.getText())){
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
			if(v.getId()==backB.getId()){
				startActivity(new Intent(v.getContext(),WelcomeActivity.class));
			}
			else if(v.getId()==selectLocB.getId()){
				Memo memo=makeAMemoNow();
				IOHandler.WriteObject(memo, workingDir+"/"+"temp.mem");
			}
			else if(v.getId()==saveMemoB.getId()){	
				Boolean validated=validate();
				if(validated){
					Memo memo=makeAMemoNow();
					IOHandler.WriteObject(memo, 
							workingDir+"/"+CommonUtils.getCurrentDateTimeString()+".mem");

					startActivity(new Intent(v.getContext(),WelcomeActivity.class));
				}
				else{
					Toast.makeText(v.getContext(), "Subject must be specified", Toast.LENGTH_SHORT);
				}
			}
		}

	}


	EditText subjectET;
	EditText memoDate;
	EditText memoTime;
	Button selectLocB;
	Button saveMemoB;
	Button backB;
	TabHost tabHost;
	Intent dateTimeIntent;
	Intent locationIntent;
	View locationTabView;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.creatememo);

		workingDir=getFilesDir()+"/Memos";

		subjectET=(EditText)findViewById(R.id.subjectET);
		saveMemoB=(Button)findViewById(R.id.saveB);
		backB=(Button)findViewById(R.id.backB);
		
		LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v=inflater.inflate(R.layout.datetime_tab, null, false);
		memoDate=(EditText)v.findViewById(R.id.dateET);
		memoTime=(EditText)v.findViewById(R.id.timeEt);
		locationTabView=inflater.inflate(R.layout.location_tab, null, false);
		selectLocB=(Button) locationTabView.findViewById(R.id.selectLocB);
		
		tabHost=(TabHost) findViewById(R.id.my_tabhost);
		LocalActivityManager mLocalActivityManager = new LocalActivityManager(this, false);
		mLocalActivityManager.dispatchCreate(savedInstanceState);
		tabHost.setup(mLocalActivityManager);

		dateTimeIntent=new Intent(this,DateTimeTabActivity.class);
		locationIntent=new Intent(this,LocationTabActivity.class);
		
		TabSpec tspec1 = tabHost.newTabSpec("date_time_tab");
		tspec1.setIndicator("Date & Time").setContent(dateTimeIntent);
		tabHost.addTab(tspec1);

		TabSpec tspec2 = tabHost.newTabSpec("location_tab");
		tspec2.setIndicator("Location").setContent(locationIntent);;
		tabHost.addTab(tspec2);

		subjectET.requestFocus();
		tabHost.clearFocus();
		Object temp;
		if((temp= IOHandler.ifExistDelete(workingDir, "temp.mem"))!=null){
			fillViewsWithMemo((Memo)temp);
		}

		backB.setOnClickListener(new ButtonHandler());
		//selectLocB.setOnClickListener(new ButtonHandler());
		saveMemoB.setOnClickListener(new ButtonHandler());


		Bundle extras=getIntent().getExtras();
		if(extras!=null){
			location = extras.getDoubleArray("selectedCoordinates");
			locationAddress=extras.getString("selectedLocAddress");
			if(location!=null){
				String locStr=locationAddress+" "+location[0]+" , "+location[1];
				//locV.setText(locStr);
			}
			Memo editMemo=(Memo)extras.getSerializable("editMemo");
			if(editMemo!=null){
				fillViewsWithMemo(editMemo);
			}
		}
	}


	/**This method fills the activity views with data
	 * @param m	m is the Memo used to fill the views with.
	 */
	private void fillViewsWithMemo(Memo m){
		subjectET.setText(m.getSubject());
		//memoDate.set
		location=m.getCoordinates();
	}

	/**Returns a instance of memo with currently available data on views
	 * @return	New instance of memo created
	 */
	private Memo makeAMemoNow(){
		Date date=new Date();
		String[] dateTokens=memoDate.getText().toString().split("/");
		date.setDate(Integer.parseInt(dateTokens[1]));
		date.setMonth(Integer.parseInt(dateTokens[0]));
		date.setYear(Integer.parseInt(dateTokens[2]));					

		Time time=new Time();
		String trimmedEnd=memoTime.getText().toString().replace("am","");
		trimmedEnd=trimmedEnd.replace("pm","");

		String[] timeTokens=trimmedEnd.split(":");
		time.hour= Integer.parseInt(timeTokens[0]);
		time.minute=Integer.parseInt(timeTokens[1]);

		String timeStr=CommonUtils.timeToString(time);

		Memo memo=new Memo(subjectET.getText().toString(),date,timeStr,location);

		return memo;
	}

}
