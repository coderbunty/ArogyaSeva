package com.iiitb.arogyaseva;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Base64;
import android.util.Log;

public class HttpManager
{

	public static String getData(String uri)
	{
		Log.d("hi", "insde getdata");
		Log.d("uri", uri);

		BufferedReader reader = null;

		try
		{
			URL url = new URL(uri);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			StringBuilder sb = new StringBuilder();
			reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}

			return sb.toString();

		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		} finally
		{
			if (reader != null)
			{
				try
				{
					reader.close();
				} catch (IOException e)
				{
					e.printStackTrace();
					return null;
				}
			}
		}

	}

}
