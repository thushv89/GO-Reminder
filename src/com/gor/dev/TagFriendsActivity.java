package com.gor.dev;

import java.util.ArrayList;

import com.gor.dev.util.CommonUtils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

public class TagFriendsActivity extends Activity {
	ArrayList<String>test=new ArrayList<String>();
	MultiAutoCompleteTextView friendsMAC;
	EditText descET;
	TextView locTV;
	Button sendB;
	String sms;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tag_friends);

		/*test.add("Thushan");
		test.add("Hiran");
		test.add("Thilan");*/
		initialize();
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			double[] coordinates=bundle.getDoubleArray("selectedCoordinates");
			String address=bundle.getString("selectedLocAddress");
			locTV.setText(CommonUtils.getFormattedLocationString(coordinates, address));
			String locURL="http://maps.google.com/?q="+coordinates[0]/1E6+","+coordinates[1]/1E6;
			descET.setText("Hi, \nI found this cool place!\n"+locURL);
		}

	}

	private void sendSMS(String phoneNumber, String message){
		//---sends an SMS message to another device---	         
		PendingIntent pi = PendingIntent.getActivity(this, 0,new Intent(this, MyLocationActivity.class), 0);                
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, pi, null);        

	}
	private ArrayList<String> getAllContacts(){
		ArrayList<String> contacts=new ArrayList<String>();

		Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null); 
		while (cursor.moveToNext()) { 
			String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)); 
			String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)); 
			if ("1".equals(hasPhone)) { 
				// You know it has a number so now query it like this
				Cursor phones = getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, null, null); 
				while (phones.moveToNext()) { 
					String phoneNumber = phones.getString(phones.getColumnIndex( ContactsContract.CommonDataKinds.Phone.NUMBER));         
					contacts.add(name+";"+phoneNumber);
				} 
				phones.close(); 
			}

		}
		return contacts;
	}
	private void initialize(){
		friendsMAC=(MultiAutoCompleteTextView)findViewById(R.id.friends_multi_ac);
		friendsMAC.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,getAllContacts());
		friendsMAC.setAdapter(adapter);

		descET=(EditText)findViewById(R.id.descET);
		locTV=(TextView)findViewById(R.id.locTV);
		sendB=(Button)findViewById(R.id.sendB);
		sendB.setOnClickListener(new ButtonHandler());
	}

	private String composeSMS(String address,double[] coordinates,String description){
		String message="";
		message+=description+"\n";
		message+=address+"\n";
		message+="http://maps.google.com/?q="+coordinates[0]/1E6+","+coordinates[1]/1E6;
		return message;
	}

	class ButtonHandler implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(v.getId()==sendB.getId()){
				String[] contacts=friendsMAC.getText().toString().split(",");
				for(int i=0;i<contacts.length-1;i++){
					sendSMS(contacts[i].split(";")[1],descET.getText().toString());
				}
			}
			startActivity(new Intent(getApplicationContext(),MyLocationActivity.class));
		}

	}
}
