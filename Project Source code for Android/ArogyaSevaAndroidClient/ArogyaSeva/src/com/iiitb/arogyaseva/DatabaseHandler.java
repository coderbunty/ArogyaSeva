package com.iiitb.arogyaseva;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper
{

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "db";

	// Student Information table name
	private static final String TABLE_NAME = "studentInfo";

	// Contacts Table Columns names
	private static final String KEY_ID = "studentId";
	private static final String KEY_FNAME = "fName";
	private static final String KEY_LNAME = "lName";
	private static final String KEY_ROLLNO = "rollNo";
	private static final String KEY_AGE = "age";
	private static final String KEY_HEIGHT = "height";
	private static final String KEY_WEIGHT = "weight";
	private static final String KEY_CNORMAL = "cNormal";
	private static final String KEY_CEXPAND = "cExpand";
	private static final String KEY_SCHOOL = "school";
	private static final String KEY_TIMESTAMP = "timeStamp";
	private static final String KEY_ISUPLOADED = "isUploaded";
	public static final String KEY_PHOTO = "image";
	public static final String KEY_PHOTOSTRING="image_str";


	private String schoolName;
	
	public DatabaseHandler(Context context, String schoolName)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.schoolName = schoolName;
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_FNAME + " TEXT,"
				+ KEY_LNAME + " TEXT," + KEY_ROLLNO + " INTEGER," + KEY_AGE
				+ " INTEGER," + KEY_HEIGHT + " INTEGER," + KEY_WEIGHT
				+ " INTEGER," + KEY_CNORMAL + " INTEGER," + KEY_CEXPAND + " INTEGER," + KEY_SCHOOL + " TEXT," + KEY_TIMESTAMP + " TEXT," + KEY_ISUPLOADED
				+ " INTEGER,"  + KEY_PHOTO  + " blob," + KEY_PHOTOSTRING  + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new student info
	void addStudentInfo(StudentInfo studentInfo)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_FNAME, studentInfo.getfName());
		values.put(KEY_LNAME, studentInfo.getlName());
		values.put(KEY_ROLLNO, studentInfo.getRoll());
		values.put(KEY_AGE, studentInfo.getAge());
		values.put(KEY_HEIGHT, studentInfo.getHeight());
		values.put(KEY_WEIGHT, studentInfo.getWeight());
		values.put(KEY_CNORMAL, studentInfo.getcNormal());
		values.put(KEY_CEXPAND, studentInfo.getcExpand());
		values.put(KEY_SCHOOL, studentInfo.getSchool());
		values.put(KEY_TIMESTAMP, studentInfo.getDateTime());
		values.put(KEY_ISUPLOADED, studentInfo.getIsUploaded());
		values.put(KEY_PHOTO, studentInfo.getImage());
		values.put(KEY_PHOTOSTRING,studentInfo.getImage_str());

		// Inserting Row
		db.insert(TABLE_NAME, null, values);
		db.close(); // Closing database connection
	}

	/*// Getting single student's info
	public StudentInfo getStudentInfo(int id)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_NAME, new String[]
		{ KEY_ID, KEY_FNAME, KEY_FNAME, KEY_ROLLNO, KEY_AGE, KEY_HEIGHT,
				KEY_WEIGHT, KEY_CNORMAL, KEY_CEXPAND, KEY_ISUPLOADED }, KEY_ID + "=?",
				new String[]
				{ String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		StudentInfo studentInfo = new StudentInfo(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
				cursor.getString(2), Integer.parseInt(cursor.getString(3)),
				Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor
						.getString(5)), Integer.parseInt(cursor.getString(6)),
				Integer.parseInt(cursor.getString(7)), Integer.parseInt(cursor
						.getString(8)), Integer.parseInt(cursor.getString(9)));
		return studentInfo;
	}*/

	// Getting all students information
	public List<StudentInfo> getAllStudentInfo()
	{
		List<StudentInfo> studentInfoList = new ArrayList<StudentInfo>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + KEY_SCHOOL + "='" + schoolName + "';";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst())
		{
			do
			{
				StudentInfo studentInfo = new StudentInfo();
				studentInfo.setStudentId(Integer.parseInt(cursor.getString(0)));
				studentInfo.setfName(cursor.getString(1));
				studentInfo.setlName(cursor.getString(2));
				studentInfo.setRoll(Integer.parseInt(cursor.getString(3)));
				studentInfo.setAge(Integer.parseInt(cursor.getString(4)));
				studentInfo.setHeight(Integer.parseInt(cursor.getString(5)));
				studentInfo.setWeight(Integer.parseInt(cursor.getString(6)));
				studentInfo.setcNormal(Integer.parseInt(cursor.getString(7)));
				studentInfo.setcExpand(Integer.parseInt(cursor.getString(8)));
				studentInfo.setSchool(cursor.getString(9));
				studentInfo.setDateTime(cursor.getString(10));
				studentInfo.setIsUploaded(Integer.parseInt(cursor.getString(11)));
				studentInfo.setImage(cursor.getBlob(12));
				studentInfo.setImage_str(cursor.getString(13));
				studentInfoList.add(studentInfo);
			} while (cursor.moveToNext());
		}
		// return contact list
		return studentInfoList;
	}
	
	// Getting all students information
	public List<StudentInfo> getAllUploadPendingStudentInfo()
	{
		List<StudentInfo> studentInfoList = new ArrayList<StudentInfo>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + KEY_SCHOOL + "='" + schoolName + "'" + " AND " + KEY_ISUPLOADED + "=0;";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst())
		{
			do
			{
				StudentInfo studentInfo = new StudentInfo();
				studentInfo.setStudentId(Integer.parseInt(cursor.getString(0)));
				studentInfo.setfName(cursor.getString(1));
				studentInfo.setlName(cursor.getString(2));
				studentInfo.setRoll(Integer.parseInt(cursor.getString(3)));
				studentInfo.setAge(Integer.parseInt(cursor.getString(4)));
				studentInfo.setHeight(Integer.parseInt(cursor.getString(5)));
				studentInfo.setWeight(Integer.parseInt(cursor.getString(6)));
				studentInfo.setcNormal(Integer.parseInt(cursor.getString(7)));
				studentInfo.setcExpand(Integer.parseInt(cursor.getString(8)));
				studentInfo.setSchool(cursor.getString(9));
				studentInfo.setDateTime(cursor.getString(10));
				studentInfo.setIsUploaded(Integer.parseInt(cursor.getString(11)));
				studentInfo.setImage(cursor.getBlob(12));
				studentInfo.setImage_str(cursor.getString(13));
				// Adding contact to list
				studentInfoList.add(studentInfo);
			} while (cursor.moveToNext());
		}

		// return contact list
		return studentInfoList;
	}

	// Updating single contact
	public int updateStudentInfo(StudentInfo studentInfo)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_FNAME, studentInfo.getfName());
		values.put(KEY_LNAME, studentInfo.getlName());
		values.put(KEY_ROLLNO, studentInfo.getRoll());
		values.put(KEY_AGE, studentInfo.getAge());
		values.put(KEY_HEIGHT, studentInfo.getHeight());
		values.put(KEY_WEIGHT, studentInfo.getWeight());
		values.put(KEY_CNORMAL, studentInfo.getcNormal());
		values.put(KEY_CEXPAND, studentInfo.getcExpand());
		values.put(KEY_SCHOOL, studentInfo.getSchool());
		values.put(KEY_TIMESTAMP, studentInfo.getDateTime());
		values.put(KEY_ISUPLOADED, studentInfo.getIsUploaded());
		values.put(KEY_PHOTO, studentInfo.getImage());
		values.put(KEY_PHOTOSTRING, studentInfo.getImage_str());

		// updating row
		return db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[]
		{ String.valueOf(studentInfo.getStudentId()) });
	}

	// Deleting single contact
	public void deleteStudentInfo(StudentInfo studentInfo)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, KEY_ID + " = ?", new String[]
		{ String.valueOf(studentInfo.getStudentId()) });
		db.close();
	}

	public int getNoOfRecordsAddUpdCnt()
	{
		String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + KEY_SCHOOL + "='" + schoolName + "';";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int cnt = cursor.getCount();
		cursor.close();

		// return count
		return cnt;
	}
	
	public int getUplRecordsCnt()
	{
		String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + KEY_SCHOOL + "='" + schoolName + "'" + " AND " + KEY_ISUPLOADED + "=1;";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int cnt = cursor.getCount();
		cursor.close();

		// return count
		return cnt;
	}
	
	public int getUplRemainingCnt()
	{
		String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + KEY_SCHOOL + "='" + schoolName + "'" + " AND " + KEY_ISUPLOADED + "=0;";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int cnt = cursor.getCount();
		cursor.close();

		// return count
		return cnt;
	}

}
