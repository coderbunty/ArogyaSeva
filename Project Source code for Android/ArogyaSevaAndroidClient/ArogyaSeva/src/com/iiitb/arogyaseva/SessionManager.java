package com.iiitb.arogyaseva;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by Abhishek on 31-03-2015.
 */
public class SessionManager
{
    SharedPreferences sharPref;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN = "isLoggedIn";
    private static final String USER_NAME = "userName";
    private static final String PASSWORD = "password";
    private static final String USERTYPE = "userType";
    private static final String USERID = "userId";

    public SessionManager(Context context)
    {
        this.context = context;
        sharPref = context.getSharedPreferences("ArogyaSevaPref", 0);
        editor = sharPref.edit();
    }

    public void createLoginSession(String userName, String pass, String type, int id)
    {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(USER_NAME, userName);
        editor.putString(PASSWORD, pass);
        editor.putString(USERTYPE, type);
        editor.putInt(USERID, id);
        editor.commit();
    }

    public void checkLogin()
    {
        if(!this.isLoggedIn())
        {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public boolean isLoggedIn()
    {
        return sharPref.getBoolean(IS_LOGIN, false);
    }

    public HashMap<String, String> getUserDetails()
    {
        HashMap<String, String> user = new HashMap<>();
        user.put(USER_NAME, sharPref.getString(USER_NAME, null));
        user.put(PASSWORD, sharPref.getString(PASSWORD, null));
        user.put(USERTYPE, sharPref.getString(USERTYPE, null));
        user.put(USERID, String.valueOf(sharPref.getInt(USERID, 0)));
        return user;
    }

    public void logoutUser()
    {
        editor.clear();
        editor.commit();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}

