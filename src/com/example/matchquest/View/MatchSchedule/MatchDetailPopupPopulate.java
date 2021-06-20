package com.example.matchquest.View.MatchSchedule;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.matchquest.R;
import com.example.matchquest.common.Details;
import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.model.Players;

public class MatchDetailPopupPopulate {
	
	List<Players> playerList ;
	
	Context mContext;
	LayoutInflater inflater;
	List<Players> modifiedplayers;
	Players Player;
	MatchPlayersPopUp popUpView;
	LinearLayout linearLayout;
	int totalCount;
	
	 TextView name;
	 TextView num;
	 CheckBox selected;
	
	public MatchDetailPopupPopulate(Context context,  List<Players> objects,MatchPlayersPopUp popUp,int count,LinearLayout layout)
	{
		this.playerList = objects;
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.popUpView = popUp;
		this.linearLayout = layout;
		modifiedplayers = new ArrayList<Players>();
		
		this.totalCount = count;
	}
	
	public void populateData()
	{
		for(Players player : playerList)
		{
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.contacts_view,null);
			
			int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
			  name = (TextView) layout.findViewById(R.id.contactName);
			  num = (TextView) layout.findViewById(R.id.contactNum);
			  selected = (CheckBox) layout.findViewById(R.id.contact_checkbox);
			  selected.setButtonDrawable(id);
			  if(player.getPlayerId().equals(Details.getPlayer((ActionBarActivity)mContext).getPlayerId()))
			  {
				  name.setText("You");
			  }else{
				  name.setText(player.getPlayerName());
			  }
			  
			  if(player.isRegistered())
			  {
				  name.setTextColor(Color.parseColor(TeamQuestConstants.registeredPlayerColor_key));
			  }else{
				  name.setTextColor(Color.parseColor(TeamQuestConstants.nonregisteredPlayerColor_key));
			  }	
			  
			  num.setText("-"+player.getPlayerId());
			  if(popUpView.getSelectedPlayers()!= null && popUpView.getSelectedPlayers().get(player.getPlayerId()) != null)
			  {  
				  selected.setChecked(true);
				  player.setSelected(true);
				  modifiedplayers.add(player);
			  }
			  selected.setTag(player);
			  
			  
			  linearLayout.addView(layout);
			  
			  selected.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						Players c = (Players) cb.getTag();
						if(cb.isChecked())
						{
							modifiedplayers.add(c);								
							popUpView.setTeamMemberCount(1);
						}
						else{
							modifiedplayers.remove(c);
							popUpView.setTeamMemberCount(-1);
						}
						if(popUpView.getSelectedCount() > totalCount)
						{
							modifiedplayers.remove(c);
							popUpView.setTeamMemberCount(-1);
							cb.setChecked(false);	
						}
						c.setSelected(cb.isChecked());
						
					}
				});
			
		}
	}
	
	public List<Players> getSelectedPlayers()
	{
		return modifiedplayers;
	}


}
