package com.gor.dev;

import com.gor.dev.util.CommonUtils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

/**	ButtonAdapter class will take care of the buttons in the grid view of the
 * Welcome screen
 * @author Thushan
 *
 */
public class ButtonAdapter extends BaseAdapter{

	private Context context;
	private LayoutInflater li;
	private int[] imageIds={R.drawable.create_memo,R.drawable.view_memo,R.drawable.location_org,R.drawable.my_location,R.drawable.about,R.drawable.gps};
	private String[] textForCells={"Create Memo","View Memos","Locations","My Location","About","Deactivate GPS"};

	class ButtonHandler implements View.OnClickListener
	{
		
		
		public void onClick(View v)
		{		
			/*if(v.getId()==0){
				if(!isActive){
					isActive=true;
					textForCells[0]="Deactivate";
					context.startService(new Intent(context,NotificationService.class));
				}else{
					isActive=false;
					textForCells[0]="Activate";
					context.stopService(new Intent(context,NotificationService.class));
				}
				
				
			}else */if(v.getId()==0){
				context.startActivity(new Intent(v.getContext(), CreateMemoActivity.class));
				
				//context.startService(new Intent(context, BackgroundWorker.class));
			}
			else if(v.getId()==1){
				context.startActivity(new Intent(v.getContext(), ViewMemoActivity.class));
				//context.stopService(new Intent(context, BackgroundWorker.class));
			}
			else if(v.getId()==2){
				context.startActivity(new Intent(v.getContext(), LocationOrgActivity.class));
			}
			else if (v.getId()==3){
				context.startActivity(new Intent(v.getContext(),MyLocationActivity.class));
			}
			else if (v.getId()==4){
				context.startActivity(new Intent(v.getContext(),AboutActivity.class));
			}
			else if (v.getId()==5){
				context.stopService(new Intent(context,NotificationService.class));
			}
		}
	}
	
	public ButtonAdapter(Context c,LayoutInflater li) {
        context = c;
        this.li=li;
        //gpsStatusTV=(TextView)li.inflate(R.layout.welcome, null);
    }
	
	@Override
	public int getCount() {
		return imageIds.length;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ImageButton b;
		TextView tv;
		View v;
		TableLayout layout = new TableLayout(context);
		
		v=li.inflate(R.layout.icon,null);
		
		b=(ImageButton) v.findViewById(R.id.imgButton);
		b.setId(position);
		b.setScaleType(ImageView.ScaleType.FIT_CENTER);
		b.setImageDrawable(context.getResources().getDrawable(imageIds[position]));
		b.setOnClickListener(new ButtonHandler());
	
		tv=(TextView)v.findViewById(R.id.icon_text);
		tv.setText(textForCells[position]);
	
		return v;

		
		
	}

}
