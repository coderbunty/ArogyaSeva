package com.iiitb.arogyaseva;

public class UserDetails
{
	private String userName;
	private String password;
	private String userType;
	private int userId;	//volunteer id for volunteers
	//private Events events;
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getUserType()
	{
		return userType;
	}
	public void setUserType(String userType)
	{
		this.userType = userType;
	}
	public int getUserId()
	{
		return userId;
	}
	public void setUserId(int userId)
	{
		this.userId = userId;
	}
	/*public Events getEvents()
	{
		return events;
	}
	public void setEvents(Events events)
	{
		this.events = events;
	}*/
}
