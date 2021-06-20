package com.example.matchquest.View.MatchSchedule.AsyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.matchquest.DataManipulation.MatchSchedule.MatchScheduleDM;
import com.example.matchquest.View.MatchSchedule.MatchDetailFragment;
import com.example.matchquest.View.ReusableViews.SavePlayerOpinionInterface;
import com.example.matchquest.common.TeamQuestConstants;

public class SavePlayerOpinion extends AsyncTask<Void, Void, Integer> {

	ProgressDialog mProgressDialog;
	SavePlayerOpinionInterface savePlayerOpinion;
	
	public SavePlayerOpinion(SavePlayerOpinionInterface savePlayerOpinion) {
		this.savePlayerOpinion = savePlayerOpinion;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if(!isCancelled())
		{
	
		mProgressDialog = new ProgressDialog(savePlayerOpinion.getActivityObject());
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
			MatchScheduleDM saveRequestStatusDm = new MatchScheduleDM();
			result = saveRequestStatusDm.savePlayerOpinion(savePlayerOpinion.getToSaveRequestStatus());
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
				savePlayerOpinion.postSavePlayerOpinion(savePlayerOpinion.getToSaveRequestStatus().isToRemove());

			}else{
				Toast.makeText(savePlayerOpinion.getActivityObject().getApplicationContext(), TeamQuestConstants.updationError_key, 1000).show();
			}
		}
	}
}
