package com.example.matchquest.View.RequestStatus.AsyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.matchquest.DataManipulation.RequestStatus.RequestStatusDM;
import com.example.matchquest.View.RequestStatus.RequestStatusAdapter;
import com.example.matchquest.common.TeamQuestConstants;

public class SavePlayerOpinionTask extends AsyncTask<Void, Void, Integer> {

	ProgressDialog mProgressDialog;
	RequestStatusAdapter requestStatusAdapter;
	
	public SavePlayerOpinionTask(RequestStatusAdapter requestStatusAdapter)
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
		int res = -1;
		
		RequestStatusDM saveRequestStatusDm = new RequestStatusDM();
		res = saveRequestStatusDm.savePlayerOpinion(requestStatusAdapter.getToSaveReqestStatus());
		
		return res;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		mProgressDialog.dismiss();
		if(!isCancelled())
		{
			if(result !=  -1)
			{
				requestStatusAdapter.postSavePlayerOpinion(requestStatusAdapter.getSelectedRequestStatus().isToRemove());

			}else{
				Toast.makeText(requestStatusAdapter.getActivityObject().getApplicationContext(), TeamQuestConstants.updationError_key, 1000).show();
			}
		}
	}
}
