package com.gor.dev;
import java.io.File;
import java.util.Date;

import com.gor.dev.entities.Memo;
import com.gor.dev.util.IOHandler;
import com.gor.dev.util.CommonUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

	EditText subjectET;
	DatePicker memoDate;
	TimePicker memoTime;
	Button selectLocB;
	Button saveMemoB;
	Button backB;
	
	String subjectText;	//text disappears when select location is clicked there for text needs to be stored and loaded
	String workingDir="";
	
	double[] location;
	
	/**	This method validates the view before save is performed. If not validated 
	 * false will be returned.
	 * @return	tells whether all the necessary fields are filled
	 */
	private boolean validate(){
		if(subjectET.getText()==null || "".equals(subjectET.getText())){
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
			
				startActivity(new Intent(v.getContext(),MyLocationActivity.class));
			}
			else if(v.getId()==saveMemoB.getId()){				
				if(validate()){
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
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		workingDir=getFilesDir()+"/Memos";
		
		ScrollView sView=new ScrollView(this);
		TextView subjectV=new TextView(this);
		subjectV.setText("Subject");
		subjectET = new EditText(this);
		
		TextView dateTimeV=new TextView(this);
		dateTimeV.setText("Date and Time");
		memoDate=new DatePicker(this);
		memoTime=new TimePicker(this);
		
		selectLocB=new Button(this);
		CommonUtils.setButtonStyle(selectLocB, "Select Location", 10);
		
		TextView locV=new TextView(this);
		locV.setText("Selected Location will appear here");
		locV.setGravity(Gravity.CENTER_HORIZONTAL);
		
		saveMemoB=new Button(this);
		CommonUtils.setButtonStyle(saveMemoB, "Save", 11);
		
		backB=new Button(this);
		CommonUtils.setButtonStyle(backB, "Back",12);

		TextView empty1=new TextView(this);
		TextView empty2=new TextView(this);
		TextView empty3=new TextView(this);
		empty3.setHeight(50);
		
		TableLayout layout = new TableLayout(this);
		sView.addView(layout);
		
		TableRow r1=new TableRow(this);

		layout.setStretchAllColumns(true);
		layout.addView(subjectV);
		layout.addView(subjectET);
		
		layout.addView(empty1);
		
		layout.addView(dateTimeV);
		layout.addView(memoDate);
		layout.addView(memoTime);
		
		layout.addView(empty2);
		
		layout.addView(selectLocB);
		layout.addView(locV);
		
		layout.addView(empty3);
		
		layout.addView(r1);
		r1.addView(backB);
		r1.addView(saveMemoB);
		
		Object temp;
		if((temp= IOHandler.ifExistDelete(workingDir, "temp.mem"))!=null){
			fillViewsWithMemo((Memo)temp);
		}
		
		backB.setOnClickListener(new ButtonHandler());
		selectLocB.setOnClickListener(new ButtonHandler());
		saveMemoB.setOnClickListener(new ButtonHandler());
		setContentView(sView);
		
		Bundle extras=getIntent().getExtras();
		if(extras!=null){
			location = extras.getDoubleArray("selectedCoordinates");
			if(location!=null){
				String locStr=location[0]+" , "+location[1];
				locV.setText(locStr);
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
		date.setDate(memoDate.getDayOfMonth());
		date.setMonth(memoDate.getMonth());
		date.setYear(memoDate.getYear());					
		
		Time time=new Time();
		time.hour= memoTime.getCurrentHour();
		time.minute=memoTime.getCurrentMinute();
		String timeStr=CommonUtils.timeToString(time);
		
		Memo memo=new Memo(subjectET.getText().toString(),date,timeStr,location);
		
		return memo;
	}

}
