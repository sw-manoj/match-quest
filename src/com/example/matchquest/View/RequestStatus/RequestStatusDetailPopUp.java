package com.example.matchquest.View.RequestStatus;

import com.example.matchquest.R;
import com.example.matchquest.View.CommonActivity;
import com.example.matchquest.View.CommonActivityWithFragment;
import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.model.RequestStatus;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class RequestStatusDetailPopUp {
	
	RequestStatus requestStatus;

	Context context;
	
	CommonActivityWithFragment activityObject;
	
	PopupWindow requestStatusPopUp;
	
	TextView teamName;
	
	TextView teamCode;
	
	TextView location;
	
	TextView date;
	
	TextView nop;
	
	TextView emptyView;
	
	TextView interestedPlayersText;
	
	RecyclerView playerRecyclerView;
	
	ImageView expandIcon;
	
	ImageView collapseIcon;
	
	RelativeLayout playerListLayout; 
	
	RelativeLayout playerViewLayout;
	
	public void showPopup(ActionBarActivity activityObject,RequestStatus rs,String screenName) 
	{
		this.activityObject = (CommonActivityWithFragment) activityObject;
		this.context = activityObject.getApplicationContext();
		this.requestStatus = rs;
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.request_status_detail_popup_layout,(ViewGroup) activityObject.findViewById(R.id.request_status_popup_element));
		
		Display display = activityObject.getWindowManager().getDefaultDisplay();
		double width = display.getWidth();
		double height = display.getHeight();
		
		int heightSize = (int) ((height/4)*3);
		int widthSize = (int) ((width/6)*5);
		
		playerViewLayout = (RelativeLayout) layout.findViewById(R.id.request_status_players_layout);
		if(screenName.equals(TeamQuestConstants.findOpponent_key))
		{
			heightSize = (int) ((width/3)*2);
			playerViewLayout.setVisibility(View.GONE);
		}
		teamName = (TextView) layout.findViewById(R.id.request_status_teamname);
		teamCode = (TextView) layout.findViewById(R.id.request_status_teamcode);
		location = (TextView) layout.findViewById(R.id.request_status_loc);
		date = (TextView) layout.findViewById(R.id.request_status_date);
		nop = (TextView) layout.findViewById(R.id.request_status_nop);
		
		teamName.setText(" " + requestStatus.getTeamName());
		teamCode.setText(" " + requestStatus.getTeamCode());
		location.setText(" " + requestStatus.getLocation());
		nop.setText(" " + requestStatus.getNop());
		date.setText(" " + requestStatus.getDateString() + "   " + requestStatus.getTime());
		
		playerRecyclerView = (RecyclerView) layout.findViewById(R.id.request_status_players_list);
		emptyView = (TextView) layout.findViewById(R.id.empty_view);
		
		interestedPlayersText = (TextView) layout.findViewById(R.id.request_status_players_text);
		interestedPlayersText.setText(interestedPlayersText.getText().toString() 
				+ " (" + (requestStatus.getPlayersList() == null ? "0" : requestStatus.getPlayersList().size()) + ")");
		
		playerRecyclerView.setLayoutManager(new LinearLayoutManager(context));
		RequestStatusPopUpAdapter requestStatusPopUpAdapter = new RequestStatusPopUpAdapter(context, requestStatus.getPlayersList());
		playerRecyclerView.setAdapter(requestStatusPopUpAdapter);
		if(requestStatus.getPlayersList() == null || requestStatus.getPlayersList().size() == 0)
		{
			playerRecyclerView.setVisibility(View.GONE);
			emptyView.setVisibility(View.VISIBLE);
		}
		if(screenName.equals(TeamQuestConstants.matchSchedule_key))
		{
			emptyView.setText(context.getResources().getString(R.string.no_players_match_schedule));
		}
		
		expandIcon = (ImageView) layout.findViewById(R.id.expand_icon_request_status);
		collapseIcon = (ImageView) layout.findViewById(R.id.collapse_icon_request_status);
		playerListLayout = (RelativeLayout) layout.findViewById(R.id.request_status_players_list_layout);
		
		collapseIcon.setVisibility(View.VISIBLE);
		expandIcon.setVisibility(View.GONE);
		collapseIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				collapseIcon.setVisibility(View.GONE);
				expandIcon.setVisibility(View.VISIBLE);
				playerListLayout.setVisibility(View.GONE);
			}
		});
		expandIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				expandIcon.setVisibility(View.GONE);
				collapseIcon.setVisibility(View.VISIBLE);
				playerListLayout.setVisibility(View.VISIBLE);
				
			}
		});
		requestStatusPopUp = new PopupWindow(activityObject);
		requestStatusPopUp.setContentView(layout);
	    requestStatusPopUp.setFocusable(true);
	    requestStatusPopUp.setHeight(heightSize);
	    requestStatusPopUp.setWidth(widthSize);

	    requestStatusPopUp.showAtLocation(layout, Gravity.CENTER, 0, 0);
	    
	    requestStatusPopUp.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				RequestStatusDetailPopUp.this.activityObject.getFrament().reGainLayout();
				
			}
		});
	}
}
