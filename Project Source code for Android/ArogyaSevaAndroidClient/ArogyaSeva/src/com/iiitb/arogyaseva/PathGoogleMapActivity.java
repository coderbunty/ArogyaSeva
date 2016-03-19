package com.iiitb.arogyaseva;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class PathGoogleMapActivity extends FragmentActivity
{
	private LatLng START_POINT;
	private LatLng FINISH_POINT;

	GoogleMap googleMap;
	final String TAG = "PathGoogleMapActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_path_google_map);
		
		Intent myIntent = getIntent();
		
		START_POINT = new LatLng(myIntent.getDoubleExtra("startPointLat", 0.00),
				myIntent.getDoubleExtra("startPointLong", 0.00));
		FINISH_POINT = new LatLng(myIntent.getDoubleExtra("finishPointLat", 0.00), 
				myIntent.getDoubleExtra("finishPointLong", 0.00));
		
		SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		googleMap = fm.getMap();

		MarkerOptions options = new MarkerOptions();
		options.position(START_POINT);
		options.position(FINISH_POINT);
		googleMap.addMarker(options);
		String url = getMapsApiDirectionsUrl();
		ReadTask downloadTask = new ReadTask();
		downloadTask.execute(url);

		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(START_POINT,
				13));
		addMarkers();

	}

	private String getMapsApiDirectionsUrl()
	{
		String origDest = "origin=" + START_POINT.latitude + "," + START_POINT.longitude + "&destination=" + FINISH_POINT.latitude + ","
				+ FINISH_POINT.longitude;
		String waypoints = origDest + "&waypoints=optimize:true|"
				+ START_POINT.latitude + "," + START_POINT.longitude
				+ "|" + FINISH_POINT.latitude + ","
				+ FINISH_POINT.longitude;

		String sensor = "sensor=false";
		String params = waypoints + "&" + sensor;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + params;
		return url;
	}

	private void addMarkers()
	{
		if (googleMap != null)
		{
			googleMap.addMarker(new MarkerOptions().position(START_POINT)
					.title("First Point"));
			googleMap.addMarker(new MarkerOptions().position(FINISH_POINT)
					.title("Second Point"));
		}
	}

	private class ReadTask extends AsyncTask<String, Void, String>
	{
		@Override
		protected String doInBackground(String... url)
		{
			String data = "";
			try
			{
				HttpConnectionForMaps http = new HttpConnectionForMaps();
				data = http.readUrl(url[0]);
			} catch (Exception e)
			{
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			new ParserTask().execute(result);
		}
	}

	private class ParserTask extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>>
	{

		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData)
		{

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try
			{
				jObject = new JSONObject(jsonData[0]);
				PathJSONParser parser = new PathJSONParser();
				routes = parser.parse(jObject);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			return routes;
		}

		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> routes)
		{
			ArrayList<LatLng> points = null;
			PolylineOptions polyLineOptions = null;

			// traversing through routes
			for (int i = 0; i < routes.size(); i++)
			{
				points = new ArrayList<LatLng>();
				polyLineOptions = new PolylineOptions();
				List<HashMap<String, String>> path = routes.get(i);

				for (int j = 0; j < path.size(); j++)
				{
					HashMap<String, String> point = path.get(j);

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);

					points.add(position);
				}

				polyLineOptions.addAll(points);
				polyLineOptions.width(2);
				polyLineOptions.color(Color.BLUE);
			}

			googleMap.addPolyline(polyLineOptions);
		}
	}
}