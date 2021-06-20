package com.example.matchquest.View.teamDetail.AsyncTask;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.matchquest.DataManipulation.topicDetail.TopicDetailDM;
import com.example.matchquest.View.teamDetail.AddTopicActivity;
import com.example.matchquest.View.teamDetail.TeamDetailActivity;
import com.example.matchquest.View.teamDetail.TeamDetailFragment;
import com.example.matchquest.common.Details;
import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.model.TopicDetails;

public class AddTopicTask extends AsyncTask<Void, Void, Integer> {

	AddTopicActivity addTopicActivity;
	ProgressDialog mProgressDialog;
	public AddTopicTask(AddTopicActivity activity) {
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
	protected Integer doInBackground(Void... params) {
		int result = -1;
		if(!isCancelled())
		{
		TopicDetailDM addTopicSaveProcess = new TopicDetailDM();
		result = addTopicSaveProcess.addTopic(addTopicActivity.getSelectedUniqueId(), addTopicActivity.getSelectedPlayerId(), addTopicActivity.getSelectedOptions(), addTopicActivity.getSelectedTopic(),addTopicActivity.getSelectedScreen());
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
				Toast.makeText(addTopicActivity.getApplicationContext(), "Topic Created", 1000).show();
				TeamDetailFragment.getTeam().getTopicIds().add(result);
				addTopicActivity.finish();

			}else{
				Toast.makeText(addTopicActivity.getApplicationContext(), TeamQuestConstants.updationError_key, 1000).show();
			}
		}
	}

}
