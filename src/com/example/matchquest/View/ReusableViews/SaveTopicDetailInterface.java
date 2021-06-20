package com.example.matchquest.View.ReusableViews;

import android.support.v7.app.ActionBarActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.matchquest.model.TopicDetails;

public interface SaveTopicDetailInterface {

	public TopicDetails getSelectedTopicDetail() ;

	public void setSelectedTopicDetail(TopicDetails selectedTopicDetail);

	public TopicDetails getSelectedSaveTopicDetail();

	public void setSelectedSaveTopicDetail(TopicDetails selectedSaveTopicDetail);

	public LinearLayout getSelectedoptionDetailLayout() ;
	
	public void setSelectedoptionDetailLayout(
			LinearLayout selectedoptionDetailLayout);

	public TextView getSelectedTextView() ;

	public void setSelectedTextView(TextView selectedTextView);

	public String getSelectedPlayerId();

	public void setSelectedPlayerId(String selectedPlayerId);
	
	public ActionBarActivity getActivityObject();
	
}
