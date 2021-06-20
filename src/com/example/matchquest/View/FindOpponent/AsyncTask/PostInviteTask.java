package com.example.matchquest.View.FindOpponent.AsyncTask;

import com.example.matchquest.DataManipulation.FindOpponent.FindOpponentDM;
import com.example.matchquest.DataManipulation.topicDetail.TopicDetailDM;
import com.example.matchquest.View.FindOpponent.FindOpponentFragment;
import com.example.matchquest.View.teamDetail.TeamDetailFragment;
import com.example.matchquest.common.TeamQuestConstants;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class PostInviteTask  extends AsyncTask<Void, Void, Integer> {

	FindOpponentFragment findOpponentFragment;
	ProgressDialog mProgressDialog;
	
	public PostInviteTask(FindOpponentFragment findOpponentFragment) {
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
			FindOpponentDM inviteAll = new FindOpponentDM();
			result = inviteAll.postInvite(findOpponentFragment.getSaveList().get(findOpponentFragment.getSaveList().size()-1));
		}
		
		return result;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		mProgressDialog.dismiss();
		if(!isCancelled())
		{
			if(result !=  -1)
			{
				Toast.makeText(findOpponentFragment.getActivityObject().getApplicationContext(), "An invite has bee posted", 1000).show();
			}else{
				Toast.makeText(findOpponentFragment.getActivityObject().getApplicationContext(), TeamQuestConstants.updationError_key, 1000).show();
			}
		}
	}

}
