package com.iiitb.arogyaseva;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Utility
{	
	public static String constructStudentInfoListJSONObject(ArrayList<StudentInfo> values)
	{
		JSONArray jArray = new JSONArray();
		JSONObject jObj;
		StudentInfo studentInfo;
		Iterator<StudentInfo> it = values.iterator();
		while(it.hasNext())
		{
			studentInfo = (StudentInfo)it.next();
			try
			{
				jObj = new JSONObject();
				jObj.put("id", studentInfo.getStudentId());
				jObj.put("fName", studentInfo.getfName());
				jObj.put("lName", studentInfo.getlName());
				jObj.put("rollNo", studentInfo.getRoll());
				jObj.put("age", studentInfo.getAge());
				jObj.put("height", studentInfo.getHeight());
				jObj.put("weight", studentInfo.getWeight());
				jObj.put("cNormal", studentInfo.getcNormal());
				jObj.put("cExpand", studentInfo.getcExpand());
				jObj.put("school", studentInfo.getSchool());
				jObj.put("dateTime",studentInfo.getDateTime());
		    	jObj.put("image", studentInfo.getImage_str());
			
			   String image_str=studentInfo.getImage_str();
				System.out.println("Raju" + image_str);
			//	image_str="{\"image\":\" "+ image_str + " \" }";
		//		jObj.put("image", image_str);
			//	System.out.println("hihell0"+studentInfo.getImage_str());
				
				jArray.put(jObj);
			
			} 
			
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		
		}
		try
		{
			//return new JSONObject().put("feed", jArray).toString();
			return jArray.toString();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static ArrayList<StudentInfo> getParsedStudentInfoList(String jsonString)
	{
		try
		{
			JSONArray ar = new JSONArray(jsonString);
			ArrayList<StudentInfo> studentInfoArrayList = new ArrayList<StudentInfo>();

			for (int i = 0; i < ar.length(); i++)
			{
				JSONObject obj = ar.getJSONObject(i);
				StudentInfo studentInfo = new StudentInfo();

				studentInfo.setStudentId(obj.getInt("id"));
				studentInfo.setfName(obj.getString("fName"));
				studentInfo.setlName(obj.getString("lName"));
				studentInfo.setRoll(obj.getInt("rollNo"));
				studentInfo.setAge(obj.getInt("age"));
				studentInfo.setHeight(obj.getInt("height"));
				studentInfo.setWeight(obj.getInt("weight"));
				studentInfo.setcNormal(obj.getInt("cNormal"));
				studentInfo.setcExpand(obj.getInt("cExpand"));
				studentInfo.setSchool(obj.getString("school"));
				studentInfo.setDateTime(obj.getString("dateTime"));
				studentInfo.setImage_str(obj.getString("image"));
				
					String img=obj.getString("image");
					byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
					studentInfo.setImage(decodedString);				
				studentInfoArrayList.add(studentInfo);
			}

			return studentInfoArrayList;
		} catch (JSONException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
