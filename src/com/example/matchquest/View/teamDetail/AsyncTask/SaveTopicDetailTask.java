package com.example.matchquest.View.teamDetail.AsyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.example.matchquest.DataManipulation.topicDetail.TopicDetailDM;
import com.example.matchquest.View.ReusableViews.SaveTopicDetailInterface;
import com.example.matchquest.View.teamDetail.TeamDetailFragment;
import com.example.matchquest.View.teamDetail.TopicDetailActivity;
import com.example.matchquest.model.TopicDetails;

public class SaveTopicDetailTask  extends AsyncTask<Void, Void, TopicDetails>{

	ProgressDialog mProgressDialog;
	SaveTopicDetailInterface teamDetailFragment;
	
	public SaveTopicDetailTask(SaveTopicDetailInterface fragment)
	{
		this.teamDetailFragment = fragment;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(!isCancelled())
		{
	
		mProgressDialog = new ProgressDialog(teamDetailFragment.getActivityObject());
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
		TopicDetailDM saveOption = new TopicDetailDM();
		result = saveOption.saveDetail(teamDetailFragment.getSelectedTopicDetail(), teamDetailFragment.getSelectedSaveTopicDetail(),teamDetailFragment.getSelectedPlayerId());
		
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(TopicDetails result) {
		super.onPostExecute(result);
		mProgressDialog.dismiss();
		if(!isCancelled())
		{
			if(result != null)
			{
				if(teamDetailFragment.getSelectedTextView() != null)
				{
				teamDetailFragment.getSelectedTextView().setText(result.getOption());
				}
				teamDetailFragment.getSelectedSaveTopicDetail().getOptionOriginal().clear();
				teamDetailFragment.getSelectedSaveTopicDetail().getOptionOriginal().putAll(teamDetailFragment.getSelectedSaveTopicDetail().getOptionModified());
				teamDetailFragment.getSelectedTopicDetail().setOptions(result.getOptions());
				teamDetailFragment.getSelectedTopicDetail().setOptionIds(result.getOptionIds());
				if(teamDetailFragment.getSelectedoptionDetailLayout() != null)
				{
				teamDetailFragment.getSelectedoptionDetailLayout().setVisibility(View.GONE);
				}else{
					((TopicDetailActivity)teamDetailFragment).loadOptions();
				}
				Toast.makeText(teamDetailFragment.getActivityObject(), "saved", 1000).show();
			}else{
				Toast.makeText(teamDetailFragment.getActivityObject(), "Something went wrong please try again ", 1000).show();
			}
		}
	}
	
	

}
