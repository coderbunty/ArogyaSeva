package com.iiitb.arogyaseva;

import com.iiitb.arogyaseva.model.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class StudentDetailsActivity extends Activity implements OnClickListener
{
	private static final String TABLE_NAME = "studentInfo";
	EditText fName, lName, rollNo, age, height, weight, cNormal, cExpand;
	Button save, back,captureimage;
	private String schoolName;
	private Uri fileUri;
	ImageView imgPreview;
	byte imageInByte[]=null;
	DatabaseHandler db;
	String image_str = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_details);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		fName = (EditText) findViewById(R.id.firstName);
		lName = (EditText) findViewById(R.id.lastName);
		rollNo = (EditText) findViewById(R.id.rollNo);
		age = (EditText) findViewById(R.id.age);
		height = (EditText) findViewById(R.id.height);
		weight = (EditText) findViewById(R.id.weight);
		cNormal = (EditText) findViewById(R.id.cNormal);
		cExpand = (EditText) findViewById(R.id.cExpand);
		save = (Button) findViewById(R.id.save);
		back = (Button) findViewById(R.id.back);
		imgPreview = (ImageView) findViewById(R.id.imgPreview);
		captureimage=(Button)findViewById(R.id.image);
		save.setEnabled(true);

		StudentInfo studentInfo=new StudentInfo();

		Intent myIntent = getIntent();
		setSchoolName(myIntent.getExtras().getString("School Name"));
		studentInfo = (StudentInfo) myIntent.getSerializableExtra("Student Details");
		if(studentInfo != null)
		{
			fName.setText(studentInfo.getfName());
			lName.setText(studentInfo.getlName());
			rollNo.setText(String.valueOf(studentInfo.getRoll()));
			age.setText(String.valueOf(studentInfo.getAge()));
			height.setText(String.valueOf(studentInfo.getHeight()));
			weight.setText(String.valueOf(studentInfo.getWeight()));
			cNormal.setText(String.valueOf(studentInfo.getcNormal()));
			cExpand.setText(String.valueOf(studentInfo.getcExpand()));

        //	retrival of image 
			if(studentInfo.getImage()!=null)
			{	
				
				ByteArrayInputStream inputStream =  new ByteArrayInputStream(studentInfo.getImage());
				
				Bitmap bitmap =  BitmapFactory.decodeStream(inputStream);
				
				imgPreview.setImageBitmap(bitmap);
			}

			save.setEnabled(false);
			freezeText();
		}


		db = new DatabaseHandler(this, getSchoolName());

		captureimage.setOnClickListener(this);
		save.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		if(view == save)
		{
			if(fName.getText().toString().trim().length() == 0 || lName.getText().toString().trim().length() == 0 || rollNo.getText().toString().trim().length() == 0 || age.getText().toString().trim().length() == 0 || height.getText().toString().trim().length() == 0 || weight.getText().toString().trim().length() == 0 || cNormal.getText().toString().trim().length() == 0 || cExpand.getText().toString().trim().length() == 0)
			{
				alert("Error", "No field should be empty!");
			}
			else
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String curDateTime = sdf.format(new Date());
				db.addStudentInfo(new StudentInfo(fName.getText().toString(), lName.getText().toString(), Integer.parseInt(rollNo.getText().toString()), Integer.parseInt(age.getText().toString()), Integer.parseInt(height.getText().toString()), Integer.parseInt(weight.getText().toString()), Integer.parseInt(cNormal.getText().toString()), Integer.parseInt(cExpand.getText().toString()), getSchoolName(), curDateTime, 0,imageInByte,image_str));
				List<StudentInfo> studentInfoList = db.getAllStudentInfo();
				Iterator<StudentInfo> it = studentInfoList.iterator();
				while(it.hasNext())
				{
					StudentInfo studentInfo = (StudentInfo) it.next();
					String log = "Id: " + studentInfo.getStudentId() + " First Name: " + studentInfo.getfName() + " Last Name: " + studentInfo.getlName() + " Roll No: " + studentInfo.getRoll() + " Age: " + studentInfo.getAge() + " Height: " + studentInfo.getHeight() + " Weight: " + studentInfo.getWeight() + " Chest Normal: " + studentInfo.getcNormal() + " Chest Expand: " + studentInfo.getcExpand() + " School Name: " + studentInfo.getSchool() + " Time Stamp: " + studentInfo.getDateTime() + " Is uploaded: " + studentInfo.getIsUploaded()  + " String: " + studentInfo.getImage_str();
					Log.d("StudentInfo", log);
				}

				alert("Success", "Details saved");
				freezeText();
			}
		}
		if(view == back)
		{
			finish();
		}

		if(view== captureimage)
		{
			takePicture();
		}
	}


	private void takePicture() {
		
		captureimage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				Log.d("hi", "camera");
				if (intent.resolveActivity(getPackageManager()) != null)
					startActivityForResult(intent,1);

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1 && resultCode == RESULT_OK) {
			//	        Bundle extras = data.getExtras();
			//	        Bitmap imageBitmap = (Bitmap) extras.get("data");
			//	        mImageView.setImageBitmap(imageBitmap);

			Bundle extras = data.getExtras();

			if (extras != null) {

				Bitmap medImage = extras.getParcelable("data");
				// convert bitmap to byte
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				medImage.compress(Bitmap.CompressFormat.PNG, 10, stream);
				imageInByte = stream.toByteArray();
				image_str = Base64.encodeBytes(imageInByte);
				/*StudentInfo studentInfo= new StudentInfo(image_str);
				studentInfo.setImage_str(image_str);*/
			}
			previewCapturedImage();
		}
	}

	private void previewCapturedImage() {
		try {

			imgPreview.setVisibility(View.VISIBLE);
			Log.d("hello","captured image");
			ByteArrayInputStream inputStream =  new ByteArrayInputStream(imageInByte);
			Bitmap bitmap =  BitmapFactory.decodeStream(inputStream);
			imgPreview.setImageBitmap(bitmap);

		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	private void alert(String title, String message)
	{
		AlertDialog.Builder builder = new Builder(this);
		builder.setCancelable(true);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.show();
	}

	private void freezeText()
	{
		fName.setEnabled(false);
		lName.setEnabled(false);
		rollNo.setEnabled(false);
		age.setEnabled(false);
		height.setEnabled(false);
		weight.setEnabled(false);
		cNormal.setEnabled(false);
		cExpand.setEnabled(false);
	}

	private void unfreezeText()
	{
		fName.setEnabled(true);
		lName.setEnabled(true);
		rollNo.setEnabled(true);
		age.setEnabled(true);
		height.setEnabled(true);
		weight.setEnabled(true);
		cNormal.setEnabled(true);
		cExpand.setEnabled(true);
	}

	private void clearText()
	{
		fName.setText(null);
		lName.setText(null);
		rollNo.setText(null);
		age.setText(null);
		height.setText(null);
		weight.setText(null);
		cNormal.setText(null);
		cExpand.setText(null);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("file_uri", fileUri);
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
			unfreezeText();
			clearText();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

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
