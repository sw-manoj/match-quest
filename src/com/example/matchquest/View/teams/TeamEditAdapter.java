package com.example.matchquest.View.teams;

import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matchquest.R;
import com.example.matchquest.common.Details;
import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.model.Players;
import com.example.matchquest.model.Team;

public class TeamEditAdapter extends RecyclerView.Adapter<TeamEditAdapter.MyViewHolder> implements  android.widget.PopupMenu.OnMenuItemClickListener{
	
	List<Players> playerList ;
	
	TeamEditActivity mContext;
	Team team;
	LayoutInflater inflater;
	RecyclerView recyclerView;
	
	Players selectedPlayer;

	public TeamEditAdapter(TeamEditActivity context,  List<Players> objects,Team team,RecyclerView r) {
		this.playerList = objects;
		this.mContext = context;
		this.team = team;
		this.recyclerView = r;
		this.inflater = LayoutInflater.from(context);
	}
	public int getCount() {
		return playerList.size();
	}

	public Players getItem(int position) {
		return playerList.get(position);
	}

	
	public long getItemId(int position) {
		return position;
	}
	public List<Players> getList()
	{
		return playerList;
	}
	public Team getTeam()
	{
		return team;
	}
	
	public void setTeam(Team t)
	{
		this.team = t;
	}
	
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId())
		{
		case R.id.make_a_call:
			((TeamEditActivity)mContext).makeCall(selectedPlayer.getPlayerId());
			break;
		case R.id.make_vice_captain:
			((TeamEditActivity)mContext).makeViceCaptain(team,selectedPlayer.getPlayerId());
			break;
		case R.id.make_captain:
			((TeamEditActivity)mContext).makeCaptain(team,selectedPlayer.getPlayerId());
			break;
		case R.id.remove_from_team:
			((TeamEditActivity)mContext).deleteFromTeam(team,selectedPlayer.getPlayerId());
			 break;
		}
		return false;
	}
	
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView playerName;
        TextView playerStatus;
        ImageView optionMenu;
        RelativeLayout optionMenuLayout;
 
        public MyViewHolder(View itemView) {
            super(itemView);
            optionMenuLayout = (RelativeLayout) itemView.findViewById(R.id.team_edit_icon_layout);
            optionMenuLayout.setOnClickListener(new OnClickListener() {
				
				@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
				@Override
				public void onClick(View v) {
					final int startPos = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
					final int endPos = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
					if(v.getId() == R.id.team_edit_icon_layout)
					{
						
					PopupMenu popUp = new PopupMenu(mContext, v);
		           	 popUp.setOnMenuItemClickListener(TeamEditAdapter.this);
		           	 MenuInflater inflator = popUp.getMenuInflater();
		           	 inflator.inflate(R.menu.team_edit_options, popUp.getMenu());
		           	 selectedPlayer = (Players) v.getTag();
		           	 populatePopUp(popUp,selectedPlayer);
		           	 popUp.show();
		           	 
		           	
		           	
					final int selectedPos = playerList.indexOf(selectedPlayer);
					
					for(int k = 0 ; k <= (endPos-startPos) ; k ++)
					{
						if(k+startPos!= selectedPos)
						{
							final View child = ((LinearLayoutManager)recyclerView.getLayoutManager()).getChildAt(k);
							if (child != null) {
								child.clearAnimation();
								child.setAlpha(0.3f);
							}
						}
					}
					
					popUp.setOnDismissListener(new OnDismissListener() {
						
						@Override
						public void onDismiss(PopupMenu menu) {
							for(int k = 0 ; k <= (endPos-startPos) ; k ++)
							{
								if(k+startPos != selectedPos)
								{
									final View child = ((LinearLayoutManager)recyclerView.getLayoutManager()).getChildAt(k);
									if (child != null) {
										child.clearAnimation();
										child.setAlpha(1);
									}
								}
							}
							
						}
					});
					}
					
				}

				
			});
            playerName = (TextView) itemView.findViewById(R.id.playerName);
            playerStatus = (TextView) itemView.findViewById(R.id.playerStatus);
        
        }
        
        private void populatePopUp(PopupMenu popUp,
				Players selectedPlayer) {
        	
        	if(!selectedPlayer.isRegistered() )
        	{
        	popUp.getMenu().findItem(R.id.make_vice_captain).setVisible(false);
        	popUp.getMenu().findItem(R.id.make_captain).setVisible(false);
        	if(!team.getCaptain().equals(Details.getPlayer(((TeamEditActivity)mContext)).getPlayerId()) && !team.getViceCaptain().equals(Details.getPlayer(((TeamEditActivity)mContext)).getPlayerId()))
        	{
        	popUp.getMenu().findItem(R.id.remove_from_team).setVisible(false);
        	}
        	
        	}else{
        		
        	if(!team.getCaptain().equals(Details.getPlayer(((TeamEditActivity)mContext)).getPlayerId()) && !team.getViceCaptain().equals(Details.getPlayer(((TeamEditActivity)mContext)).getPlayerId()))
        	{
        		popUp.getMenu().findItem(R.id.make_vice_captain).setVisible(false);
            	popUp.getMenu().findItem(R.id.make_captain).setVisible(false);
            	popUp.getMenu().findItem(R.id.remove_from_team).setVisible(false);
            	return;
        	}else if(team.getCaptain().equals(Details.getPlayer(((TeamEditActivity)mContext)).getPlayerId()) && team.getViceCaptain().equals(selectedPlayer.getPlayerId()))
        	{
        		popUp.getMenu().findItem(R.id.make_vice_captain).setVisible(false);
        		
        	}else if(team.getViceCaptain().equals(Details.getPlayer(((TeamEditActivity)mContext)).getPlayerId()))
        	{
        		popUp.getMenu().findItem(R.id.make_captain).setVisible(false);
        		if( team.getCaptain().equals(selectedPlayer.getPlayerId()))
        		{
        			popUp.getMenu().findItem(R.id.make_vice_captain).setVisible(false);
                	popUp.getMenu().findItem(R.id.remove_from_team).setVisible(false);
        		}
        	}
        	}
		}
    }

  
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return playerList.size();
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		Players player = playerList.get(position);
		holder.playerName.setText(player.getPlayerName());
		if(player.isRegistered())
		{
			holder.playerName.setTextColor(Color.parseColor(TeamQuestConstants.registeredPlayerColor_key));
		}else{
			holder.playerName.setTextColor(Color.parseColor(TeamQuestConstants.nonregisteredPlayerColor_key));
		}		
			if(player.getPlayerId().equals(Details.getPlayer(((TeamEditActivity)mContext)).getPlayerId()))
			{
				holder.playerName.setText("You");
			}
			holder.playerStatus.setText("");
		if(player.getPlayerId().equals(team.getCaptain()))
		{
			holder.playerStatus.setText(" -Captain ");
		}else if(player.getPlayerId().equals(team.getViceCaptain()))
		{
			holder.playerStatus.setText(" -Vice Captain");
		}

		holder.optionMenuLayout.setTag(player);
		holder.optionMenuLayout.setVisibility(View.VISIBLE);
		if(player.getPlayerId().equals(Details.getPlayer(((TeamEditActivity)mContext)).getPlayerId()))
		{
			holder.optionMenuLayout.setVisibility(View.GONE);
		}
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
		View view = inflater.inflate(R.layout.team_edit_adapter, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
	}

}
