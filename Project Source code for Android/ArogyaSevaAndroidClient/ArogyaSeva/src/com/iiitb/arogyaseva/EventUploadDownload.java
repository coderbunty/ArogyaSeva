package com.iiitb.arogyaseva;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class EventUploadDownload extends Activity
{
	ProgressBar pb;
	DatabaseHandler db;
	private String schoolName;
	TextView eventName, noOfRecordsAddUpdCnt, uplRecordsCnt, uplRemainingCnt;
	Button backBtn, contBtn, uploadBtn, downloadBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_upload_download);
		
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		pb.setVisibility(View.INVISIBLE);

		Intent myIntent = getIntent();
		setSchoolName(myIntent.getExtras().getString("School Name"));

		db = new DatabaseHandler(this, getSchoolName());

		eventName = (TextView) findViewById(R.id.eventName);
		noOfRecordsAddUpdCnt = (TextView) findViewById(R.id.noOfRecordsAddUpdCnt);
		uplRecordsCnt = (TextView) findViewById(R.id.uplRecordsCnt);
		uplRemainingCnt = (TextView) findViewById(R.id.uplRemainingCnt);
		backBtn = (Button) findViewById(R.id.backBtn);
		contBtn = (Button) findViewById(R.id.continueBtn);
		uploadBtn = (Button) findViewById(R.id.upload);
		downloadBtn = (Button) findViewById(R.id.download);

		eventName.setText(getSchoolName());
		updateCounters();

		contBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(EventUploadDownload.this,
						StudentListActivity.class);
				intent.putExtra("School Name", getSchoolName());
				startActivity(intent);
			}
		});

		backBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// HomePageActivity which will be stored in task backstack will
				// be shown
				finish();
			}
		});

		uploadBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (Integer.parseInt(uplRemainingCnt.getText().toString()) == 0)
					Toast.makeText(EventUploadDownload.this,
							"All records are already uploaded",
							Toast.LENGTH_LONG).show();
				else
				{
					ArrayList<StudentInfo> studentPendingUploadList = (ArrayList<StudentInfo>) db
							.getAllUploadPendingStudentInfo();
					uploadStudentData(
							"http://192.168.43.24:8083/com.arogyaseva.service/api/medicalData/upload",
							studentPendingUploadList);
				}
			}
		});

		downloadBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				schoolName = schoolName.replace(" ", "_");
				downloadStudentData("http://192.168.43.24:8083/com.arogyaseva.service/api/medicalData/" + schoolName);
				/*DownloadStudentData downloadStudentData = new DownloadStudentData();
				downloadStudentData.execute("http://172.168.174.13:8080/com.arogyaseva.service/api/medicalData/" + schoolName);*/
			}
		});
	}
	
	private void updateCounters()
	{
		noOfRecordsAddUpdCnt.setText(String.valueOf(db
				.getNoOfRecordsAddUpdCnt()));
		uplRecordsCnt.setText(String.valueOf(db.getUplRecordsCnt()));
		uplRemainingCnt.setText(String.valueOf(db.getUplRemainingCnt()));
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
		finish();
		startActivity(getIntent());
	}

	// volley async task for uploading the student data
	private void uploadStudentData(String uri,
			final ArrayList<StudentInfo> studentPendingUploadList)
	{
		pb.setVisibility(View.VISIBLE);
		db = new DatabaseHandler(EventUploadDownload.this, getSchoolName());
		StringRequest request = new StringRequest(Request.Method.POST, uri,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String response)
					{
						Log.d("response message: ", response);
						String resp = response;
						System.out.println(resp);
						for (StudentInfo studentInfo : studentPendingUploadList)
						{
							studentInfo.setIsUploaded(1);
							db.updateStudentInfo(studentInfo);
						}
						updateCounters();
						pb.setVisibility(View.INVISIBLE);
					}
				}, new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						Toast.makeText(EventUploadDownload.this,
								error.getMessage(), Toast.LENGTH_LONG).show();
					}
				})
		{
			// sending data in the form of map in http body
			@Override
			protected Map<String, String> getParams()
			{
				Map<String, String> params = new HashMap<String, String>();

				String studentInfoList = Utility
						.constructStudentInfoListJSONObject(studentPendingUploadList);
				params.put("Student Upload Data", studentInfoList);
				return params;
			}
		};

		RequestQueue queue = Volley.newRequestQueue(this);
		queue.add(request);
	}
	
	// volley async task for uploading the student data
	private void downloadStudentData(String uri)
	{
		pb.setVisibility(View.VISIBLE);
		db = new DatabaseHandler(EventUploadDownload.this, getSchoolName());
		StringRequest request = new StringRequest(uri,
				new Response.Listener<String>()
				{
					@Override
					public void onResponse(String response)
					{
						Log.d("response message: ", response);
						String resp = response;
						ArrayList<StudentInfo> studentInfoList = Utility
								.getParsedStudentInfoList(resp);
						db = new DatabaseHandler(EventUploadDownload.this,
								getSchoolName());
						for (StudentInfo studentInfo : studentInfoList)
						{
							System.out.println("hello..." + studentInfo.getImage_str());
							studentInfo.setIsUploaded(2);
							db.addStudentInfo(studentInfo);
						}
						updateCounters();
						pb.setVisibility(View.INVISIBLE);
					}
				}, new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						Toast.makeText(EventUploadDownload.this,
								error.getMessage(), Toast.LENGTH_LONG).show();
					}
				});

		RequestQueue queue = Volley.newRequestQueue(this);
		queue.add(request);
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_upload_download, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public String getSchoolName()
	{
		return schoolName;
	}

	public void setSchoolName(String schoolName)
	{
		this.schoolName = schoolName;
	}
}
