package com.example.matchquest.View;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.WindowManager;

public abstract class CommonActivity  extends ActionBarActivity{
	
	NetworkChangeReceiverActvity networkChangeReceiverActvity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	protected void registernetworkListener(boolean toShowProgressBarOnPreExecute){
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		networkChangeReceiverActvity = new NetworkChangeReceiverActvity(this,toShowProgressBarOnPreExecute);
		registerReceiver(networkChangeReceiverActvity, filter);
	}
	
	protected abstract int loadInBackGround();

	protected abstract void updateView();

	public NetworkChangeReceiverActvity getNetworkChangeReceiverActvity() {
		return networkChangeReceiverActvity;
	}
	
	  @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(networkChangeReceiverActvity != null)
		{
			if(networkChangeReceiverActvity.getRemoteDataTask() != null)
			  {
				networkChangeReceiverActvity.getRemoteDataTask().cancel(true);
			  }
				
				unregisterReceiver(networkChangeReceiverActvity);
				
				networkChangeReceiverActvity = null;
		}
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(networkChangeReceiverActvity != null)
		{
			if(networkChangeReceiverActvity.getRemoteDataTask() != null)
			  {
				networkChangeReceiverActvity.getRemoteDataTask().cancel(true);
			  }
		
		}
	}
	  
	  public CommonActivity getActivityObject()
	  {
		  return this;
	  }
}
