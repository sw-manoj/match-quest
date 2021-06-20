package com.example.matchquest.View.teamDetail.AsyncTask;

import com.example.matchquest.DataManipulation.topicDetail.TopicDetailCommentsDM;
import com.example.matchquest.DataManipulation.topicDetail.TopicDetailDM;
import com.example.matchquest.View.teamDetail.TeamDetailFragment;
import com.example.matchquest.View.teamDetail.TopicDetailActivity;
import com.example.matchquest.common.TeamQuestConstants;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class SaveCommentTask extends AsyncTask<Void, Void, Integer>{

	TopicDetailActivity topicDetailActivity;
	ProgressDialog mProgressDialog;
	
	public SaveCommentTask(TopicDetailActivity activity) {
		this.topicDetailActivity = activity;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(!isCancelled())
		{
	
		mProgressDialog = new ProgressDialog(topicDetailActivity);
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
			TopicDetailCommentsDM topicDetailCommentDM = new TopicDetailCommentsDM();
			result = topicDetailCommentDM.saveComment( topicDetailActivity.getSelectedComment());
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
				Toast.makeText(topicDetailActivity.getApplicationContext(), "Saved your suggestion !!", 1000).show();
				topicDetailActivity.afterSuggestionSaved();

			}else{
				Toast.makeText(topicDetailActivity.getApplicationContext(), TeamQuestConstants.updationError_key, 1000).show();
			}
		}
	}
}
