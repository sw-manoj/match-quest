package com.example.matchquest.View.MatchSchedule.AsyncTask;

import com.example.matchquest.DataManipulation.MatchSchedule.MatchScheduleDM;
import com.example.matchquest.View.MatchSchedule.MatchPlayersPopUp;
import com.example.matchquest.common.TeamQuestConstants;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class SaveSelectedPlayers  extends AsyncTask<Void, Void, Integer>{

	ProgressDialog mProgressDialog;
	MatchPlayersPopUp matchPlayersPopUp;
	
	public SaveSelectedPlayers(MatchPlayersPopUp matchPlayersPopUp) {
		this.matchPlayersPopUp = matchPlayersPopUp;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(!isCancelled())
		{
	
		mProgressDialog = new ProgressDialog(matchPlayersPopUp.getContext());
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
			MatchScheduleDM matchScheduleDM = new MatchScheduleDM();
			result = matchScheduleDM.saveSelectedPlayers(matchPlayersPopUp.getToSaveRequestStatus());
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
				matchPlayersPopUp.postAddMembers();

			}else{
				Toast.makeText(matchPlayersPopUp.getContext(), TeamQuestConstants.updationError_key, 1000).show();
			}
		}
	}

}
