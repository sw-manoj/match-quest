package com.example.matchquest.View.MatchSchedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matchquest.R;
import com.example.matchquest.View.CommonActivityWithFragment;
import com.example.matchquest.View.CommonViewClass;
import com.example.matchquest.View.MatchSchedule.AsyncTask.SaveSelectedPlayers;
import com.example.matchquest.model.Players;
import com.example.matchquest.model.RequestStatus;
import com.example.matchquest.model.Team;

public class MatchPlayersPopUp {

	PopupWindow matchDetailPlayersPopUp;
	
	
	RequestStatus requestStatus;
	List<Players> interestedPlayerList = new ArrayList<Players>();
	List<Players> notInterestedPlayersList = new ArrayList<Players>();
	ActionBarActivity context;
	TextView teamMemberCount;
	int totalCount;
	int selectedCount = 0;
	Team team;
	TextView interestedEmptyText;
	LinearLayout interestedLinearLayout;
	MatchDetailPopupPopulate interestedPlayerDataPopulator;
	
	Button addTeamMembers;
	
	TextView notInterestedEmptyText;
	LinearLayout notInterestedLinearLayout;
	MatchDetailPopupPopulate notInterestedPlayerDataPopulator;
	
	List<String> playerIdsList;
	RequestStatus toSaveRequestStatus;
	
	
	public void showPopup(ActionBarActivity mcontext,Team t,RequestStatus status) 
	{
		this.context = mcontext;
		this.team = t;
		this.requestStatus = status;
	       // Inflate the popup_layout.xml
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.match_players_popup,(ViewGroup) context.findViewById(R.id.popup_element));
		
		if(requestStatus.getSelectedPlayersMap() != null && requestStatus.getSelectedPlayersMap().size() > 0)
		{
			selectedCount = requestStatus.getSelectedPlayersMap().size();
		}
		totalCount = Integer.parseInt(requestStatus.getNop());
		teamMemberCount = (TextView) layout.findViewById(R.id.team_members_count_view);
		teamMemberCount.setText("("+ selectedCount +"/" + totalCount + ")");
		
			Display display = context.getWindowManager().getDefaultDisplay();
			double width = display.getWidth();
			double height = display.getHeight();
			
			int heightSize = (int) ((height/4)*3);
			int widthSize = (int) ((width/6)*5);
			
			
			for(Players statusPlayer : requestStatus.getPlayersList())
			{
				Players interestedPlayer = new Players();
				interestedPlayer.setPlayerId(statusPlayer.getPlayerId());
				interestedPlayer.setPlayerName(statusPlayer.getPlayerName());
				interestedPlayer.setRegistered(statusPlayer.isRegistered());
				interestedPlayer.setSelected(statusPlayer.isSelected());
				interestedPlayerList.add(interestedPlayer);
							
			}
			
			CommonViewClass.sortPlayersList(interestedPlayerList);
			interestedLinearLayout = (LinearLayout) layout.findViewById(R.id.interested_players_layout);
			
			interestedPlayerDataPopulator = new MatchDetailPopupPopulate(mcontext, interestedPlayerList, 
					this, totalCount, interestedLinearLayout);
			interestedPlayerDataPopulator.populateData();
			
			interestedEmptyText = (TextView) layout.findViewById(R.id.interested_empty_view);
			if(interestedPlayerList == null || interestedPlayerList.size() == 0 )
			{
				interestedEmptyText.setVisibility(View.VISIBLE);
			}
			
			for(Players teamPlayer : team.getPlayersList())
			{
				boolean toAdd = true;
				for(Players statusPlayer : requestStatus.getPlayersList())
				{
					if(statusPlayer.getPlayerId().equals(teamPlayer.getPlayerId()))
					{
						toAdd = false;
						break;
					}
				}
				if(toAdd)
				{
					Players player = new Players();
					player.setPlayerId(teamPlayer.getPlayerId());
					player.setPlayerName(teamPlayer.getPlayerName());
					player.setRegistered(teamPlayer.isRegistered());
					player.setSelected(teamPlayer.isSelected());
					notInterestedPlayersList.add(player);
				}
			}
			
			CommonViewClass.sortPlayersList(notInterestedPlayersList);
			
			notInterestedLinearLayout = (LinearLayout) layout.findViewById(R.id.notinterested_players_layout);
			
			notInterestedPlayerDataPopulator = new MatchDetailPopupPopulate(mcontext, notInterestedPlayersList, 
					this, totalCount, notInterestedLinearLayout);
			notInterestedPlayerDataPopulator.populateData();
			
			notInterestedEmptyText = (TextView) layout.findViewById(R.id.notinterested_empty_view);
			if(notInterestedPlayersList == null || notInterestedPlayersList.size() == 0 )
			{
				notInterestedEmptyText.setVisibility(View.VISIBLE);
			}
	       // Creating the PopupWindow
			matchDetailPlayersPopUp = new PopupWindow(context);
			matchDetailPlayersPopUp.setContentView(layout);
			matchDetailPlayersPopUp.setFocusable(true);
			matchDetailPlayersPopUp.setHeight(heightSize);
			matchDetailPlayersPopUp.setWidth(widthSize);

	       // Clear the default translucent background
//	       addMemberPopUp.setBackgroundDrawable(new BitmapDrawable());

			matchDetailPlayersPopUp.showAtLocation(layout, Gravity.CENTER, 0, 0);
			matchDetailPlayersPopUp.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				
				((CommonActivityWithFragment)context).getFrament().reGainLayout();
				
			}
		});

	       // Getting a reference to Close button, and close the popup when clicked.
	       Button close = (Button) layout.findViewById(R.id.close_popup);
	       close.setOnClickListener(new OnClickListener() {

	         @Override
	         public void onClick(View v) {
	        	 matchDetailPlayersPopUp.dismiss();
	         }
	       });
	       
	       addTeamMembers = (Button) layout.findViewById(R.id.add_team_member_button);
	       if(selectedCount == totalCount)
	       {
	    	   addTeamMembers.setEnabled(true);
	       }else{
	    	   addTeamMembers.setEnabled(false);
	       }
	       addTeamMembers.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!CommonViewClass.isNetworkAvailable(context))
				{
					CommonViewClass.showdialog(context, "Please ,Connect to internet to add team members");
					return;
				}
					playerIdsList = new ArrayList<String>();
					
					 if(interestedPlayerDataPopulator.getSelectedPlayers().size()>0)
					 {
						 for(Players players : interestedPlayerDataPopulator.getSelectedPlayers())
						 {
							 playerIdsList.add(players.getPlayerId());
						 }
					
					 }
				 
					 if(notInterestedPlayerDataPopulator.getSelectedPlayers().size()>0)
					 {
						 for(Players players : notInterestedPlayerDataPopulator.getSelectedPlayers())
						 {
							 playerIdsList.add(players.getPlayerId());
						 }
					
					 }
					 
					 toSaveRequestStatus = new RequestStatus();
					 toSaveRequestStatus.setUniqueId(requestStatus.getUniqueId());
					 toSaveRequestStatus.setTeamId(team.getTeamId());
					 toSaveRequestStatus.setPlayerStringList(playerIdsList);
					 
					 new SaveSelectedPlayers(MatchPlayersPopUp.this).execute();
				
			}
		});

	}
	
	public void postAddMembers()
	{
		matchDetailPlayersPopUp.dismiss();
		if(requestStatus.getSelectedPlayersMap() != null)
		{
			 requestStatus.getSelectedPlayersMap().clear();
			 if(interestedPlayerDataPopulator.getSelectedPlayers().size()>0)
			 {
				 for(Players players : interestedPlayerDataPopulator.getSelectedPlayers())
				 {
					 requestStatus.getSelectedPlayersMap().put(players.getPlayerId(), players);
				 }
			
			 }
		 
			 if(notInterestedPlayerDataPopulator.getSelectedPlayers().size()>0)
			 {
				 for(Players players : notInterestedPlayerDataPopulator.getSelectedPlayers())
				 {
					 requestStatus.getSelectedPlayersMap().put(players.getPlayerId(), players);
				 }
			
			 }
		}
		((CommonActivityWithFragment)context).getFrament().updateView();
	}
	
	public void setTeamMemberCount(int size)
	{
		selectedCount += size;
		teamMemberCount.setText("(" + selectedCount + "/" + totalCount + ")");
		
			if(selectedCount == totalCount)
	       {
	    	   addTeamMembers.setEnabled(true);
	       }else{
	    	   addTeamMembers.setEnabled(false);
	       }
	}
	
	public Map<String,Players> getSelectedPlayers()
	{
		return requestStatus.getSelectedPlayersMap();
	}
	
	public int getSelectedCount()
	{
		return selectedCount;
	}

	public RequestStatus getToSaveRequestStatus() {
		return toSaveRequestStatus;
	}

	public void setToSaveRequestStatus(RequestStatus toSaveRequestStatus) {
		this.toSaveRequestStatus = toSaveRequestStatus;
	}

	public ActionBarActivity getContext() {
		return context;
	}
}
