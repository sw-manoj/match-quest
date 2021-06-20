package com.example.matchquest.View.FindOpponent;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matchquest.R;
import com.example.matchquest.View.CommonActivityWithFragment;
import com.example.matchquest.View.CommonViewClass;
import com.example.matchquest.View.RequestStatus.RequestStatusActivity;
import com.example.matchquest.View.RequestStatus.RequestStatusDetailPopUp;
import com.example.matchquest.View.ReusableViews.PopUpMenuRecyclerView;
import com.example.matchquest.View.teams.TeamAdapter;
import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.model.RequestStatus;
import com.example.matchquest.model.Team;

public class FindOpponentInviteAdapter  extends RecyclerView.Adapter<FindOpponentInviteAdapter.MyViewHolder>  implements  android.widget.PopupMenu.OnMenuItemClickListener{

	Context mContext;
	List<RequestStatus> requestStatusList;
	Team team;
	LayoutInflater inflater;
	CommonActivityWithFragment activityObject;
	RequestStatus selectedStatus;
	RecyclerView recyclerView;
	FindOpponentFragment findOpponentFragment;
	
	public FindOpponentInviteAdapter(CommonActivityWithFragment activityObject,List<RequestStatus> requestStatusList,
			Team team,RecyclerView recyclerView,FindOpponentFragment fragment) {
		this.mContext = activityObject.getApplicationContext();
        this.activityObject = activityObject;
        inflater = LayoutInflater.from(mContext);
        this.recyclerView = recyclerView;
        this.requestStatusList = requestStatusList;
        this.findOpponentFragment = fragment;
        this.team = team;
	} 

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return requestStatusList.size();
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		RequestStatus status = requestStatusList.get(position);
		
		holder.requestStatusTeamName.setText(status.getTeamName());
		holder.requestStatusLoc.setText(" " + status.getLocation());
		holder.requestStatusDate.setText(" " + status.getDateString() + "   " + status.getTime());
		holder.requestStatusNop.setText(" " + status.getNop());
		holder.requestStatusLayout.setTag(status);
		holder.optionsIcon.setTag(status);
		
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
		View view = inflater.inflate(R.layout.findopponent_adapter_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
	}
	
	 private void setDetailListener(RelativeLayout requestStatusLayout)
	    {
	    	
	         requestStatusLayout.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					RequestStatus status = (RequestStatus) v.getTag();
	 				RequestStatusDetailPopUp popUp = new RequestStatusDetailPopUp();
	 				popUp.showPopup(activityObject, status,TeamQuestConstants.findOpponent_key);
	 				activityObject.getFrament().onPopUpOpen();
					return true;
				}
			});
	    }
	 
	 private void setOptionListener(RelativeLayout requestStatusLayout)
	    {
	    	
	         requestStatusLayout.setOnClickListener(new OnClickListener() {
	 			
	 			@Override
	 			public void onClick(View v) {
	 				selectedStatus = (RequestStatus) v.getTag();
					int selectedPos = requestStatusList.indexOf(selectedStatus);
					PopupMenu popUp = new PopUpMenuRecyclerView(mContext, v,
							R.menu.findopponent_invite_menu, selectedPos,recyclerView);
					
		           	popUp.setOnMenuItemClickListener(FindOpponentInviteAdapter.this);
		           	popUp.show();
	 			}
	 		});
	    }
	   
	class MyViewHolder extends RecyclerView.ViewHolder {
		TextView requestStatusTeamName;
        TextView requestStatusLoc;
        TextView requestStatusDate;
        TextView requestStatusNop;
        RelativeLayout requestStatusLayout;
        
        RelativeLayout optionsIcon;
 
        public MyViewHolder(View itemView) {
            super(itemView);
            
            requestStatusTeamName = (TextView) itemView.findViewById(R.id.find_opponent_adpter_team_text);
            requestStatusLoc = (TextView) itemView.findViewById(R.id.find_opponent_adapter_loc_text);
            requestStatusDate = (TextView) itemView.findViewById(R.id.find_opponent_adpter_date_text);
            requestStatusNop = (TextView) itemView.findViewById(R.id.find_opponent_adpter_nop_text);
            requestStatusLayout = (RelativeLayout) itemView.findViewById(R.id.find_opponent_adapter_detail_layout);
            optionsIcon = (RelativeLayout) itemView.findViewById(R.id.find_opponent_adapter_options);
            setDetailListener(requestStatusLayout);
            setOptionListener(optionsIcon);
        }
    }

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		
		if(!CommonViewClass.isNetworkAvailable(mContext))
		{
			CommonViewClass.showdialog(mContext, TeamQuestConstants.connectToInternet_key);
			return true;
		}
		if(item.getItemId() == R.id.findopponent_accept)
		{
			if(requestStatusList.remove(selectedStatus))
			{
				notifyDataSetChanged();
				findOpponentFragment.saveInvite(selectedStatus);
			
			}
		}else if(item.getItemId() == R.id.findopponent_remove)
		{
			if(requestStatusList.remove(selectedStatus))
			{
				notifyDataSetChanged();
				findOpponentFragment.checkAdapterSize();
				Toast.makeText(activityObject, "Remove the invite of team " + selectedStatus.getTeamName(), 1000).show();
			
			}
		}
		
		return false;
	}
}
