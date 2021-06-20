package com.example.matchquest.View.RequestStatus.AsyncTask;

import com.example.matchquest.DataManipulation.RequestStatus.RequestStatusDM;
import com.example.matchquest.View.RequestStatus.RequestStatusAdapter;
import com.example.matchquest.common.TeamQuestConstants;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class AcceptInviteTask extends AsyncTask<Void, Void, Integer> {

	ProgressDialog mProgressDialog;
	RequestStatusAdapter requestStatusAdapter;
	
	public AcceptInviteTask(RequestStatusAdapter requestStatusAdapter)
	{
		this.requestStatusAdapter = requestStatusAdapter;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(!isCancelled())
		{
	
		mProgressDialog = new ProgressDialog(requestStatusAdapter.getActivityObject());
		mProgressDialog.setMessage("Loading...");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
		
		}
	}

	@Override
	protected Integer doInBackground(Void... params) {
		int result = -1;
		RequestStatusDM saveRequestStatusDm = new RequestStatusDM();
		result = saveRequestStatusDm.acceptInvite(requestStatusAdapter.getToSaveReqestStatus(),requestStatusAdapter.getToSaveParentReqestStatus());
		
		return result;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		mProgressDialog.dismiss();
		if(!isCancelled())
		{
			if(result !=  -1)
			{
				requestStatusAdapter.postAcceptInvite(requestStatusAdapter.getToSaveReqestStatus().getTeamName());
			}else{
				Toast.makeText(requestStatusAdapter.getActivityObject().getApplicationContext(), TeamQuestConstants.updationError_key, 1000).show();
			}
		}
	}
}
