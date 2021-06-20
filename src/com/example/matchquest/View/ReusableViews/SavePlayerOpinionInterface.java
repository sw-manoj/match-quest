package com.example.matchquest.View.ReusableViews;

import android.support.v7.app.ActionBarActivity;

import com.example.matchquest.model.RequestStatus;

public interface SavePlayerOpinionInterface {

	public RequestStatus getSelectedRequestStatus() ;

	public void setSelectedRequestStatus(RequestStatus selectedRequestStatus) ;

	public RequestStatus getToSaveRequestStatus() ;

	public void setToSaveRequestStatus(RequestStatus toSaveRequestStatus) ;
	
	 public void postSavePlayerOpinion(boolean toRemove);
	 
	 public ActionBarActivity getActivityObject();

}
