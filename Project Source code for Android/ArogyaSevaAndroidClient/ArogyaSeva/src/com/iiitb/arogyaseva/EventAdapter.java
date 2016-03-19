package com.iiitb.arogyaseva;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EventAdapter extends ArrayAdapter<Events>
{
	private Context context;
	private List<Events> eventList;
	private int days;
	
	public EventAdapter(Context context, int resource, List<Events> objects)
	{
		super(context, resource, objects);
		this.context = context;
		this.eventList = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.event_list_item, parent, false);
		
		//sort eventList based on dates
		Collections.sort(eventList, new Comparator<Events>() {
			@Override
			public int compare(Events lhs, Events rhs)
			{
				DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
				try
				{
					return format.parse(lhs.getDate()).compareTo(format.parse(rhs.getDate()));
				} catch (ParseException e)
				{
					e.printStackTrace();
					return 2;
				}
			}
	    });

		// Display event name in the TextView widget
		final Events event = eventList.get(position);
		TextView school = (TextView) view.findViewById(R.id.eventName);
		school.setText(event.getSchool());
		
		String evDate = event.getDate();
		TextView date = (TextView) view.findViewById(R.id.eventDate);
		date.setText(evDate);
		try
		{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String curDateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			Date curDate = format.parse(curDateStr);
			Date ed = format.parse(evDate);
			long diff = ed.getTime() - curDate.getTime();
			days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
			System.out.println(days);
			
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		
		TextView gps = (TextView) view.findViewById(R.id.gps);
		String gpsCoordinates = event.getGpsCoordinates();
		String[] latLong = gpsCoordinates.split(",");
		final double destLat = Double.parseDouble(latLong[0]);
		final double destLong = Double.parseDouble(latLong[1]);
		gps.setText(gpsCoordinates);
		gps.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				GPSTracker gpsTracker = new GPSTracker(context);
				if (gpsTracker.canGetLocation)
				{
					Intent intent = new Intent(context,
							PathGoogleMapActivity.class);
					intent.putExtra("startPointLat", gpsTracker.getLatitude());
					intent.putExtra("startPointLong", gpsTracker.getLongitude());
					intent.putExtra("finishPointLat", destLat);
					intent.putExtra("finishPointLong", destLong);
					context.startActivity(intent);
				}
				else
					gpsTracker.showSettingsAlert();
			}
		});

		// Display photo in ImageView widget
		ImageView imgView = (ImageView) view.findViewById(R.id.thumbnail);
		imgView.setImageResource(getResourceId());

		return view;
	}
	
	private int getResourceId()
	{
		switch (days)
		{	
		case 0:
			return R.drawable.number0icon;
		case 1:
			return R.drawable.number1icon;
		case 2:
			return R.drawable.number2icon;
		case 3:
			return R.drawable.number3icon;
		case 4:
			return R.drawable.number4icon;
		case 5:
			return R.drawable.number5icon;
		case 6:
			return R.drawable.number6icon;
		case 7:
			return R.drawable.number7icon;
		case 8:
			return R.drawable.number8icon;
		case 9:
			return R.drawable.number9icon;
		default:
			return R.drawable.none;
		}
	}
}
