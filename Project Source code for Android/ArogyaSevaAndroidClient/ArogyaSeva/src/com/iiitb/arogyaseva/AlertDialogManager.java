package com.iiitb.arogyaseva;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Abhishek on 31-03-2015.
 */
public class AlertDialogManager
{
    public void showAlertDialog(Context context, String title, String message, Boolean status)
    {
        AlertDialog alDialog = new AlertDialog.Builder(context).create();
        alDialog.setTitle(title);
        alDialog.setMessage(message);
        if(status != null)
        {
            //setting alert dialog icons
            alDialog.setIcon(status ? R.drawable.success : R.drawable.failure);
        }

        //setting OK button
        alDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alDialog.show();
    }
}
