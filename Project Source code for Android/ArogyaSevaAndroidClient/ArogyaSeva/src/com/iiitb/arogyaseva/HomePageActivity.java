package com.iiitb.arogyaseva;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class HomePageActivity extends ListActivity
{
	List<Events> eventList;
	ListView listView;
	
	//getting the shared preferences data
	SessionManager session;
	HashMap<String, String> userDetails;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);
		
		session = new SessionManager(getApplicationContext());
		userDetails = session.getUserDetails();
		
		listView = getListView();

		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View v,
					int position, long id)
			{
				//checking if the list item is in ACCEPTED or PENDING state
				boolean longClickEnableFlag = true;
				TextView view = (TextView) v.findViewById(R.id.eventName); 
				final String schoolName = view.getText().toString();
				for (Events event : eventList)
				{
					if(event.getSchool().equals(schoolName))
					{
						if(!event.isLongClickEnabled())
						{
							longClickEnableFlag = false;
							break;
						}
					}
				}
				//enabling and disabling long click based on condition
				if (longClickEnableFlag)
				{
					onLongClick(v, position, id);
					return true;
				} else
					return false;
				
			}

		});
		// Has previous state(event list) been saved?
		if (savedInstanceState != null)
		{
			eventList = (List<Events>) savedInstanceState.getSerializable("EVENT_LIST");
			updateDisplay();
		}
		else
		{
			if(isOnline())
				getEventsData();
			else
			{
				//get previous event list
				eventList = null;
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState)
	{
		// Save state information with a collection of key-value pairs
		savedInstanceState.putSerializable("EVENT_LIST",
				(Serializable) eventList);
	}

	private void getEventsData()
	{
		if (isOnline())
		{
			requestEventData("http://192.168.43.24:8083/com.arogyaseva.service/api/events/" + userDetails.get("userId"));
		} else
		{
			Toast.makeText(HomePageActivity.this, "Network isn't available",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.logout)
		{
			session.logoutUser();
			this.finish();			/////////////////////////////////////////////////////
		}
		return false;
	}

	//volley async task for getting the list of events for a perticular volunteer
	private void requestEventData(String uri)
	{
		StringRequest request = new StringRequest(uri,
				new Response.Listener<String>()
				{

					@Override
					public void onResponse(String response)
					{
						eventList = JSONParser.parseEventList(response);
						updateDisplay();
					}
				}, new Response.ErrorListener()
				{

					@Override
					public void onErrorResponse(VolleyError error)
					{
						Toast.makeText(HomePageActivity.this,
								error.getMessage(), Toast.LENGTH_LONG).show();
						;
					}
				});
		RequestQueue queue = Volley.newRequestQueue(this);
		queue.add(request);
	}

	//volley async task for updating ACCEPT or REJECT of events by volunteer
	private void updateEvent(String uri, final String updType)
	{
		StringRequest request = new StringRequest(Request.Method.PUT, uri,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String response)
					{
						//updateDisplay();
						Log.i("response message: ", response);
					}
				}, new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						Toast.makeText(HomePageActivity.this,
								error.getMessage(), Toast.LENGTH_LONG).show();
					}
				})
		{
			//sending data in the form of map in http body
			@Override
			protected Map<String, String> getParams()
			{
				Map<String, String> params = new HashMap<>();
				params.put("updType", updType);
				return params;
			}
		};

		RequestQueue queue = Volley.newRequestQueue(this);
		queue.add(request);
	}

	protected void updateDisplay()
	{
		// Use EventAdapter to display data
		//displays the list of events
		EventAdapter adapter = new EventAdapter(this, R.layout.event_list_item,
				eventList);
		setListAdapter(adapter);

	}

	private boolean isOnline()
	{
		//checking for network connectivity
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting())
		{
			return true;
		} else
		{
			return false;
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		/*TextView view = (TextView) v.findViewById(R.id.eventName);
		String schoolName = view.getText().toString();
		Toast.makeText(HomePageActivity.this,
				schoolName + " Clicked row " + position, Toast.LENGTH_SHORT)
				.show();*/
		Intent intent = new Intent(HomePageActivity.this, EventUploadDownload.class);
		intent.putExtra("School Name", eventList.get(position).getSchool());
		startActivity(intent);
	}

	// handler for listView.setOnItemLongClickListener
	protected void onLongClick(final View v, int position, long id)
	{
		
		TextView view = (TextView) v.findViewById(R.id.eventName); 
		final String schoolName = view.getText().toString();
		final int eventId;
		int eventIdTemp = 0;
		
		TextView v1 = (TextView) v.findViewById(R.id.eventName); 
		final String school = v1.getText().toString();
		for (Events event : eventList)
		{
			if(event.getSchool().equals(school))
			{
				eventIdTemp = event.getEventId();
				break;
			}
		}
		eventId = eventIdTemp;

		AlertDialog.Builder builder = new AlertDialog.Builder(
				HomePageActivity.this); // Read Update
		builder.setTitle("Confirmation!");
		builder.setMessage("Event invite");

		builder.setPositiveButton("Accept",
				new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						updateEvent("http://192.168.43.24:8083/com.arogyaseva.service/api/events/" + userDetails.get("userId") + "/" + eventId,"A");
						for (Events event : eventList)
						{
							if(event.getSchool().equals(schoolName))
							{
								event.setLongClickEnabled(false);
								/*TextView eventName = (TextView) v.findViewById(R.id.eventName);
								eventName.setBackgroundColor(Color.GREEN);*/
								break;
							}
						}
						
					}
				});

		builder.setNegativeButton("Reject",
				new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						updateEvent("http://192.168.43.24:8083/com.arogyaseva.service/api/events/" + userDetails.get("userId") + "/" + eventId,"R");
						getEventsData();
					}
				});

		AlertDialog alert = builder.create();
		alert.show();

	}
	
	// Lifecycle callback overrides

		@Override
		public void onStart()
		{
			super.onStart();
		}

		@Override
		public void onResume()
		{
			super.onResume();
		}

		@Override
		public void onPause()
		{
			super.onPause();
		}

		@Override
		public void onStop()
		{
			super.onStop();
		}

		@Override
		public void onRestart()
		{
			super.onRestart();
		}

		@Override
		public void onDestroy()
		{
			super.onDestroy();
		}

}
