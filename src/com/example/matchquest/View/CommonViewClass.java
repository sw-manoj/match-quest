package com.example.matchquest.View;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

import com.example.matchquest.View.Drawerlayout.MatchStatusFragment;
import com.example.matchquest.model.Players;

public class CommonViewClass {
	
	static MatchStatusFragment matchFragment;
	
	static UpdateFragment updateFragment;
	
	static ProgressDialog progressDialog; 
	
	static Context context;
	
	public static boolean isNetworkAvailable(Context context) {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public static void showdialog(Context context ,String message)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle("Alert");
		alertDialog.setMessage(message);
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
		    new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		            dialog.dismiss();
		        }
		    });
		alertDialog.show();
	}
	
	public static AlertDialog showdialogTwoButton(Context context ,String message)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle("Alert");
		alertDialog.setMessage(message);
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel",
		    new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		            dialog.dismiss();
		        }
		    });
	
//		alertDialog.show();
		return alertDialog;
	}
	
	public static void sortPlayersList(List<Players> playerList)
	{
		Collections.sort(playerList,new Comparator<Players>() {

			@Override
			public int compare(Players lhs, Players rhs) {
				if(lhs.isRegistered() == rhs.isRegistered())
				{
					return 0;
				}else if (lhs.isRegistered() == false)
				{
					return 1;
				}else if (rhs.isRegistered() == false)
				{
					return -1;
				}
				return 0;
			}
		});
	}

	public static MatchStatusFragment getMatchFragment() {
		return matchFragment;
	}

	public static void setMatchFragment(MatchStatusFragment match) {
		matchFragment = match;
	}

	public static UpdateFragment getUpdateFragment() {
		return updateFragment;
	}

	public static void setUpdateFragment(UpdateFragment updateFragment) {
		CommonViewClass.updateFragment = updateFragment;
	}
	
	public static void showProgressdialog(Context context)
	{
		progressDialog = new ProgressDialog(context);
		
		progressDialog.setMessage("Loading...");
		progressDialog.setIndeterminate(false);
		progressDialog.show();
	}
	
	public static void hideProgressDialog()
	{
		if(progressDialog != null && progressDialog.isShowing())
		{
			progressDialog.dismiss();
		}
	}

	public static Context getContext() {
		return context;
	}

	public static void setContext(Context context) {
		CommonViewClass.context = context;
	}
	
}
