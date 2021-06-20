package com.example.matchquest.View.teams;

import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.matchquest.R;
import com.example.matchquest.View.CommonViewClass;
import com.example.matchquest.View.FindOpponent.FindOpponentActivity;
import com.example.matchquest.View.MatchSchedule.MatchScheduleActivity;
import com.example.matchquest.View.RequestStatus.RequestStatusActivity;
import com.example.matchquest.View.ReusableViews.PopUpMenuRecyclerView;
import com.example.matchquest.View.teamDetail.TeamDetailActivity;
import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.model.Team;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.MyViewHolder>  implements  android.widget.PopupMenu.OnMenuItemClickListener{
	Context mContext;
	int res;
	List<Team> teamList;
	private LayoutInflater inflater;
	Team selectedTeam;
	Team longpressTeam;
	RecyclerView recyclerView;
	TeamListFragment fragment;
	
	public TeamAdapter(Context context, int resource, int textViewResourceId,
			List<Team> objects,RecyclerView r, TeamListFragment fragment) {
	this.mContext = context;
	this.res= resource;
	this.inflater = LayoutInflater.from(context);
	this.teamList = objects;
	this.recyclerView = r;
	this.fragment = fragment;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return teamList.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return teamList.get(position);
	}

	
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	public boolean onMenuItemClick(MenuItem item) {
//		Toast.makeText(mContext,selectedTeam.getTeamName(), 1000).show();
		if(item.getItemId() == R.id.match_schedule_menu)
		{
			Intent matchScheduleIntent = new Intent(mContext, MatchScheduleActivity.class); 
			matchScheduleIntent.putExtra(TeamQuestConstants.team_key, selectedTeam);  
			matchScheduleIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(matchScheduleIntent);
			return true;
		}
		
		if(!CommonViewClass.isNetworkAvailable(mContext))
		{
			CommonViewClass.showdialog(fragment.getActivityObject(), TeamQuestConstants.connectToInternet_key);
			return true;
		}
		
		if(item.getItemId() == R.id.request_status_menu)
		{
			
			Intent requestStatusIntent = new Intent(mContext, RequestStatusActivity.class); 
			requestStatusIntent.putExtra(TeamQuestConstants.team_key, selectedTeam);  
			requestStatusIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(requestStatusIntent);
		}else if(item.getItemId() == R.id.find_opponent_menu)
		{
			Intent findOpponentIntent = new Intent(mContext, FindOpponentActivity.class); 
			findOpponentIntent.putExtra(TeamQuestConstants.team_key, selectedTeam);  
			findOpponentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(findOpponentIntent);
		}
		return false;
	}
	
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView teamName;
        TextView teamCode;
        ImageView optionMenu;
        RelativeLayout optionMenuLayout;
        RelativeLayout teamDetailLayout;
 
        public MyViewHolder(View itemView) {
            super(itemView);
            optionMenuLayout = (RelativeLayout) itemView.findViewById(R.id.team_option_icon_layout);
            optionMenuLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(v.getId() == R.id.team_option_icon_layout)
					{
						selectedTeam = (Team) v.getTag();
						int selectedPos = teamList.indexOf(selectedTeam);
						PopupMenu popUp = new PopUpMenuRecyclerView(mContext, v,
								R.menu.team_options, selectedPos,recyclerView);
						
			           	popUp.setOnMenuItemClickListener(TeamAdapter.this);
			           	popUp.show();
					}
					
				}
			});
            teamName = (TextView) itemView.findViewById(R.id.teamName);
            teamCode = (TextView) itemView.findViewById(R.id.teamCode);
            teamDetailLayout = (RelativeLayout) itemView.findViewById(R.id.team_details_layout);
            teamDetailLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Team team = (Team) v.getTag();
					Intent teamDetailIntent = new Intent(mContext, TeamDetailActivity.class); 
					teamDetailIntent.putExtra("Team", team);  
					teamDetailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(teamDetailIntent);
										
				}
			});
            
            teamDetailLayout.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					 longpressTeam = (Team) v.getTag();
					return false;
				}
			});
        }
    }

    public Team getLongPressTeam()
    {
    	return longpressTeam;
    }
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return teamList.size();
	}
	

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		Team team = teamList.get(position);
		holder.teamName.setText(team.getTeamName());
		holder.teamCode.setText("Code : " + team.getTeamCode());
		holder.teamDetailLayout.setTag(team);
		holder.optionMenuLayout.setTag(team);
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
		View view = inflater.inflate(R.layout.team_adapter_view, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
	}

}
