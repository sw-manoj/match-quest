package com.example.matchquest.View;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

public abstract  class CommonFragment extends Fragment{
	
	NetworkChangeReceiver networkChangeReceiver;
	
	RemoteDataTask remoteTaskMatchStatus;
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		return null;
	}
	protected void registernetworkListener(boolean toShowProgressBarONPreExecute){
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		networkChangeReceiver = new NetworkChangeReceiver(this,toShowProgressBarONPreExecute);
		getActivity().registerReceiver(networkChangeReceiver, filter);
	}
	
	public void reGainLayout()
	{
		
	}
	
	public void onPopUpOpen()
	{
		
	}
	protected abstract int loadInBackGround();

	public abstract void updateView();

	protected void preExecute()
	{
		
	}
	
//	  @Override
//	public void onDestroy() {		
//		
//		if(networkChangeReceiver != null)
//		{
//			networkChangeReceiver.getRemoteDataTask().cancel(true);
//			if(getActivity() != null)
//			{
//			getActivity().unregisterReceiver(networkChangeReceiver);
//			}
//			networkChangeReceiver = null;
//		}
//		
//		super.onDestroy();
//	}
	  
	  @Override
	public void onDestroyView() {
		  if(networkChangeReceiver != null)
			{
			  if(networkChangeReceiver.getRemoteDataTask() != null)
			  {
				networkChangeReceiver.getRemoteDataTask().cancel(true);
			  }
				if(getActivity() != null)
				{
				getActivity().unregisterReceiver(networkChangeReceiver);
				}
				networkChangeReceiver = null;
			}
			
		super.onDestroyView();
	}
	
	@Override
	public void onStop() {
		  if(networkChangeReceiver != null)
			{
			  if(networkChangeReceiver.getRemoteDataTask() != null)
			  {
				networkChangeReceiver.getRemoteDataTask().cancel(true);
			  }
			}
		  
		  if(remoteTaskMatchStatus != null)
		  {
			  remoteTaskMatchStatus.cancel(true);
		  }
			
		super.onStop();
	}
	  
	  

	public NetworkChangeReceiver getNetworkChangeReceiver() {
		return networkChangeReceiver;
	}

	public void setNetworkChangeReceiver(NetworkChangeReceiver networkChangeReceiver) {
		this.networkChangeReceiver = networkChangeReceiver;
	}
	
	 public ActionBarActivity getActivityObject()
	  {
		  return (ActionBarActivity) getActivity();
	  }
	 
	 public void reload()
	 {
		 remoteTaskMatchStatus = new RemoteDataTask();
		 remoteTaskMatchStatus.execute();
	 }
	 
	 private class RemoteDataTask extends AsyncTask<Void, Void, Integer> {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				if(!isCancelled()){
					
					preExecute();
				}
			}
	 
			@Override
			protected Integer doInBackground(Void... params) {
				if(!isCancelled())
				{
				return loadInBackGround();
				}
				return 0;
			}
	 
			@Override
			protected void onPostExecute(Integer result) {
				if(!isCancelled())
				{
					if(result == 1)
					{
						updateView();
					}else if(result == -1){
						if(getActivity() != null)
						{
						Toast.makeText(getActivity(), "we are not able to connect to server sry for inconvience!! 12"  , 1000).show();
						}
					}
				}
				
			}
		}
}
