package com.iiitb.arogyaseva;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Events implements Serializable
{
	private int eventId;
	private String volunteer;
	private String school;
	private String doctor;
	private String date;
	private String gpsCoordinates;
	private boolean longClickEnabled;
	public String getVolunteer()
	{
		return volunteer;
	}
	public void setVolunteer(String volunteer)
	{
		this.volunteer = volunteer;
	}
	public String getSchool()
	{
		return school;
	}
	public void setSchool(String school)
	{
		this.school = school;
	}
	public String getDoctor()
	{
		return doctor;
	}
	public void setDoctor(String doctor)
	{
		this.doctor = doctor;
	}
	public String getDate()
	{
		return date;
	}
	public void setDate(String date)
	{
		this.date = date;
	}
	public String getGpsCoordinates()
	{
		return gpsCoordinates;
	}
	public void setGpsCoordinates(String gpsCoordinates)
	{
		this.gpsCoordinates = gpsCoordinates;
	}
	public boolean isLongClickEnabled()
	{
		return longClickEnabled;
	}
	public void setLongClickEnabled(boolean longClickEnabled)
	{
		this.longClickEnabled = longClickEnabled;
	}
	public int getEventId()
	{
		return eventId;
	}
	public void setEventId(int eventId)
	{
		this.eventId = eventId;
	}
	
}
