package com.example.matchquest.View.FindOpponent.AsyncTask;

import com.example.matchquest.DataManipulation.FindOpponent.FindOpponentDM;
import com.example.matchquest.View.FindOpponent.FindOpponentFragment;
import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.model.RequestStatus;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class SaveInviteTask  extends AsyncTask<Void, Void, Integer> {

	FindOpponentFragment findOpponentFragment;
	RequestStatus requestStatus;
	RequestStatus opponentStatus;
	ProgressDialog mProgressDialog;
	
	public SaveInviteTask(RequestStatus requestStatus,RequestStatus opponentStatus,FindOpponentFragment findOpponentFragment) {
		this.requestStatus = requestStatus;
		this.opponentStatus = opponentStatus;
		this.findOpponentFragment = findOpponentFragment;
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(!isCancelled())
		{
	
		mProgressDialog = new ProgressDialog(findOpponentFragment.getActivityObject());
		mProgressDialog.setMessage("Loading...");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
		
		}
	}
	
	@Override
	protected Integer doInBackground(Void... params) {
		int result = -1;
		if(!isCancelled())
		{
			FindOpponentDM saveInvite = new FindOpponentDM();
			result = saveInvite.saveInvite(requestStatus, opponentStatus);
		}
		
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
				findOpponentFragment.postSaveInviteTask(opponentStatus);
			}else{
				Toast.makeText(findOpponentFragment.getActivityObject().getApplicationContext(), TeamQuestConstants.updationError_key, 1000).show();
			}
		}
	}

}
