package com.iiitb.arogyaseva;

import java.io.Serializable;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class StudentListActivity extends ListActivity
{
	DatabaseHandler db;
	private List<StudentInfo> studentInfoList;
	ListView listView;
	private String schoolName;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_list);

		Intent myIntent = getIntent();
		setSchoolName(myIntent.getExtras().getString("School Name"));
		db = new DatabaseHandler(this, getSchoolName());

		listView = getListView();

		setStudentInfoList(db.getAllStudentInfo());

		StudentListAdapter adapter = new StudentListAdapter(this,
				R.layout.student_list_item, getStudentInfoList());
		setListAdapter(adapter);
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
		// refreshing the activity to load changes
		finish();
		startActivity(getIntent());
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		Intent intent = new Intent(StudentListActivity.this,
				StudentDetailsActivity.class);
		intent.putExtra("Student Details", (Serializable) getStudentInfoList()
				.get(position));
		intent.putExtra("School Name", getSchoolName());
		startActivity(intent);
		// finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_student_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id)
		{
		case R.id.add_student:
			Intent intent = new Intent(StudentListActivity.this,
					StudentDetailsActivity.class);
			intent.putExtra("Student Details", (Serializable) null);
			intent.putExtra("School Name", getSchoolName());
			startActivity(intent);
			// finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public List<StudentInfo> getStudentInfoList()
	{
		return studentInfoList;
	}

	public void setStudentInfoList(List<StudentInfo> studentInfoList)
	{
		this.studentInfoList = studentInfoList;
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
