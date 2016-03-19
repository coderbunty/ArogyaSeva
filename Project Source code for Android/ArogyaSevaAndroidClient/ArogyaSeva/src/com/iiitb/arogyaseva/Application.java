package com.iiitb.arogyaseva;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
 
import org.json.JSONObject;

import android.util.Log;

import com.parse.FunctionCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.PushService;


public class Application extends android.app.Application {

  public Application() {
  }

  @Override
  public void onCreate() {
    super.onCreate();
    
    Parse.initialize(this, "", "XVVHOS7MHkiNUWXGqR4Zb1bwdev9WMiloeMR1Wai");
	ParseInstallation.getCurrentInstallation().saveInBackground();
	ParseInstallation installation = ParseInstallation.getCurrentInstallation();
	installation.addAllUnique("channels", Arrays.asList("user"));
	installation.saveInBackground();
	Log.d("Jigar", "DONE");	
	PushService.setDefaultPushCallback(this, LoginActivity.class);
	}   
}