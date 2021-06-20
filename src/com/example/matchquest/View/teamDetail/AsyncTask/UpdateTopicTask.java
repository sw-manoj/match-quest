package com.example.matchquest.View.teamDetail.AsyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.matchquest.DataManipulation.topicDetail.TopicDetailDM;
import com.example.matchquest.View.teamDetail.AddTopicActivity;
import com.example.matchquest.View.teamDetail.TeamDetailFragment;
import com.example.matchquest.View.teamDetail.TopicDetailActivity;
import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.model.TopicDetails;

public class UpdateTopicTask extends AsyncTask<Void, Void, TopicDetails> {
	
	AddTopicActivity addTopicActivity;
	ProgressDialog mProgressDialog;
	public UpdateTopicTask(AddTopicActivity activity) {
		this.addTopicActivity = activity;
	}
	
	@Override
	protected void onPreExecute() {
		
		super.onPreExecute();
		if(!isCancelled())
		{
	
		mProgressDialog = new ProgressDialog(addTopicActivity);
		mProgressDialog.setMessage("Loading...");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
		
		}
	}
	@Override
	protected TopicDetails doInBackground(Void... params) {
		TopicDetails result = null;
		if(!isCancelled())
		{
		TopicDetailDM addTopicSaveProcess = new TopicDetailDM();
		result = addTopicSaveProcess.updateTopic(addTopicActivity.getSelectedTopicDetail());
		}
		
		return result;
	}
	
	@Override
	protected void onPostExecute(TopicDetails result) {
		
		super.onPostExecute(result);
		mProgressDialog.dismiss();
		if(!isCancelled())
		{
			if(result !=  null)
			{
				Toast.makeText(addTopicActivity.getApplicationContext(), "Topic updated", 1000).show();
				TopicDetailActivity.setTopicDetail(result);
				addTopicActivity.finish();

			}else{
				Toast.makeText(addTopicActivity.getApplicationContext(), TeamQuestConstants.updationError_key, 1000).show();
			}
		}
	}
}
