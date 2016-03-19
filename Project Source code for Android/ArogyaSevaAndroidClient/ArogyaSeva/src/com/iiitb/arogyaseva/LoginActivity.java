package com.iiitb.arogyaseva;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.parse.ParseInstallation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity
{

	private static final String URI = "http://192.168.43.24:8083/com.arogyaseva.service/api/login";

	EditText userName, password;
	Button loginBtn;
	boolean loggedIn;
	private Map<String, String> params = new HashMap<>();

	AlertDialogManager alert = new AlertDialogManager();
	SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		session = new SessionManager(getApplicationContext());

		userName = (EditText) findViewById(R.id.userName);
		password = (EditText) findViewById(R.id.password);
		loginBtn = (Button) findViewById(R.id.login);

		if(session.isLoggedIn())
		{
			Intent intent = new Intent(LoginActivity.this,
					HomePageActivity.class);
			LoginActivity.this.startActivity(intent);
			finish();
		}
		
		Toast.makeText(getApplicationContext(),
				"User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG)
				.show();

		loginBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (isOnline())
				{
					String user_name = userName.getText().toString();
					String pass = password.getText().toString();

					params.put("uName", user_name);
					params.put("pass", pass);

					if (user_name.trim().length() > 0
							&& pass.trim().length() > 0)
					{
						String finaluri = formLoginDetailsURI(URI, user_name,
								pass);
						LoginTask lTask = new LoginTask();
						lTask.execute(finaluri);
					} else
					{
						alert.showAlertDialog(LoginActivity.this,
								"Login Failed..",
								"Please enter User Name & Password", false);
					}
				}
				else
				{
					alert.showAlertDialog(LoginActivity.this,
							"Login Failed..",
							"No network available", false);
				}
			}
		});

	}
	
	private class LoginTask extends AsyncTask<String, String, Boolean>
	{

		@Override
		protected void onPreExecute()
		{
			Log.d("hi", "hello");
		}

		@Override
		protected Boolean doInBackground(String... params)
		{
			Log.d("hi", "nisarga");

			String response = HttpManager.getData(params[0]);
			UserDetails userDetails = JSONParser.parseUserDetails(response);
			if (userDetails != null)
			{
				Log.d("hi", "helloooo");
				loggedIn = true;
				session.createLoginSession(
						userDetails.getUserName(),
						userDetails.getPassword(),
						userDetails.getUserType(),
						userDetails.getUserId());
				System.out.println(userDetails.getUserName());
				System.out.println(userDetails.getUserId());
				ParseInstallation installation = ParseInstallation.getCurrentInstallation();
				installation.add("userId", userDetails.getUserId());
				installation.saveInBackground();
				// get the events also from userDetails
				// pass it through intent or store it in shared
				// preferences
			} else
			{
				loggedIn = false;
			}
			return loggedIn;
		}

		@Override
		protected void onPostExecute(Boolean flag)
		{
			if (flag)
			{
				Intent intent = new Intent(LoginActivity.this,
						HomePageActivity.class);
				LoginActivity.this.startActivity(intent);
				finish();
				
			} else
			{
				alert.showAlertDialog(LoginActivity.this,
						"Login Failed..",
						"User Name or Password is incorrect", false);
			}
		}
	}

	private String formLoginDetailsURI(String uri, String userName, String password)
	{
		// converting basic uri with parameters to complete browser compatible uri
		StringBuilder sb = new StringBuilder();
		String value = null;
		for (String key : params.keySet())
		{
			if (sb.length() > 0)
				sb.append("&");

			try
			{
				// encoding the value before sending it through web
				value = URLEncoder.encode(params.get(key), "UTF-8");
			} catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
			sb.append(key + "=" + value);

		}
		return uri + "?" + sb.toString();

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
}
