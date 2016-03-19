package com.iiitb.arogyaseva;

import java.io.Serializable;

public class StudentInfo implements Serializable
{
	private static final long serialVersionUID = 1L;
	private int studentId;
	private String fName;
	private String lName;
	private int roll;
	private int age;
	private int height;
	private int weight;
	private int cNormal;
	private int cExpand;
	private String school;
	private String dateTime;
	private int isUploaded;	//1 or 0
	private byte[] image;
	private String image_str;

	public StudentInfo(int studentId, String fName, String lName, int roll,
			int age, int height, int weight, int cNormal, int cExpand, String school, String dateTime, int isUploaded,byte[] image,String image_str)
	{
		super();
		this.studentId = studentId;
		this.fName = fName;
		this.lName = lName;
		this.roll = roll;
		this.age = age;
		this.height = height;
		this.weight = weight;
		this.cNormal = cNormal;
		this.cExpand = cExpand;
		this.school = school;
		this.dateTime = dateTime;
		this.isUploaded = isUploaded;
		this.image = image;
		this.image_str=image_str;
	
	}
	
	public StudentInfo(String fName, String lName, int roll,
			int age, int height, int weight, int cNormal, int cExpand, String school, String dateTime, int isUploaded,byte[] image,String image_str)
	{
		super();
		this.fName = fName;
		this.lName = lName;
		this.roll = roll;
		this.age = age;
		this.height = height;
		this.weight = weight;
		this.cNormal = cNormal;
		this.cExpand = cExpand;
		this.school = school;
		this.dateTime = dateTime;
		this.isUploaded = isUploaded;
		this.image = image;
		this.image_str=image_str;

	}

	public StudentInfo()
	{
		
	}
	
	/*public StudentInfo(String image_str)
	{
		super();
		this.image_str=image_str;
		System.out.println("In Constructor" + image_str);
	}*/
	
	public int getStudentId()
	{
		return studentId;
	}
	public void setStudentId(int studentId)
	{
		this.studentId = studentId;
	}
	public String getfName()
	{
		return fName;
	}
	public void setfName(String fName)
	{
		this.fName = fName;
	}
	public String getlName()
	{
		return lName;
	}
	public void setlName(String lName)
	{
		this.lName = lName;
	}
	public int getRoll()
	{
		return roll;
	}
	public void setRoll(int roll)
	{
		this.roll = roll;
	}
	public int getAge()
	{
		return age;
	}
	public void setAge(int age)
	{
		this.age = age;
	}
	public int getHeight()
	{
		return height;
	}
	public void setHeight(int height)
	{
		this.height = height;
	}
	public int getWeight()
	{
		return weight;
	}
	public void setWeight(int weight)
	{
		this.weight = weight;
	}
	public int getcNormal()
	{
		return cNormal;
	}
	public void setcNormal(int cNormal)
	{
		this.cNormal = cNormal;
	}
	public int getcExpand()
	{
		return cExpand;
	}
	public void setcExpand(int cExpand)
	{
		this.cExpand = cExpand;
	}

	public String getSchool()
	{

		System.out.println("frm res" + school);
		return school;
	}

	public void setSchool(String school)
	{
		this.school = school;
	}

	public int getIsUploaded()
	{
		return isUploaded;
	}

	public void setIsUploaded(int isUploaded)
	{
		this.isUploaded = isUploaded;
	}

	public String getDateTime()
	{
		return dateTime;
	}

	public void setDateTime(String dateTime)
	{
		this.dateTime = dateTime;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getImage_str() {
	System.out.println("qqqqq"+ image_str);
		return image_str;
	}

	public void setImage_str(String image_str) {
		System.out.println("String"+image_str);
		this.image_str = image_str;
	}	
}
