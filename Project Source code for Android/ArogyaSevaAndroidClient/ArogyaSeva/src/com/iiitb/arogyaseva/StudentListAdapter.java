package com.iiitb.arogyaseva;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StudentListAdapter extends ArrayAdapter<StudentInfo>
{
	private Context context;
	private List<StudentInfo> studentInfoList;

	public StudentListAdapter(Context context, int resource,
			List<StudentInfo> objects)
	{
		super(context, resource, objects);
		this.context = context;
		this.studentInfoList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.student_list_item, parent, false);

		// Display student name in the TextView widget
		final StudentInfo studentInfo = studentInfoList.get(position);
		TextView studentName = (TextView) view.findViewById(R.id.studentName);
		studentName.setText(studentInfo.getfName() + " " + studentInfo.getlName());

		// Display photo in ImageView widget
		ImageView imgView = (ImageView) view.findViewById(R.id.status);
		imgView.setImageResource(getResourceId(studentInfo.getIsUploaded()));

		return view;
	}

	private int getResourceId(int status)
	{
		switch (status)
		{
		case 0:
			return R.drawable.not_uploaded;
		case 1:
			return R.drawable.uploaded;
		default:
			return R.drawable.not_altered;
		}
	}
}
