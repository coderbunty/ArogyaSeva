package com.iiitb.arogyaseva;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser
{
	public static List<Events> parseEventList(String content)
	{

		try
		{
			JSONArray ar = new JSONArray(content);
			List<Events> eventList = new ArrayList<>();

			for (int i = 0; i < ar.length(); i++)
			{
				JSONObject obj = ar.getJSONObject(i);
				Events event = new Events();

				event.setSchool(obj.getString("name"));
				//event.setVolunteer(obj.getString("volunteer"));
				event.setEventId(obj.getInt("eventId"));
				event.setGpsCoordinates(obj.getString("gps"));
				event.setDate(obj.getString("evDate"));
				if(obj.getString("taskState").equals("P"))
					event.setLongClickEnabled(true);
				else
					event.setLongClickEnabled(false);
				eventList.add(event);
			}

			return eventList;
		} catch (JSONException e)
		{
			e.printStackTrace();
			return null;
		}

	}
	
	public static UserDetails parseUserDetails(String content)
	{
		UserDetails userDetails = new UserDetails();
		try
		{
			JSONObject obj = new JSONObject(content);
			userDetails = new UserDetails();

			userDetails.setUserName(obj.getString("userName"));
			userDetails.setPassword(obj.getString("password"));
			userDetails.setUserId(obj.getInt("userId"));
			userDetails.setUserType(obj.getString("userType"));
		} catch (JSONException e)
		{
			Log.d("error while parsing", e.getMessage());
			e.printStackTrace();
			return null;
		}
		
		return userDetails;
	}
}
