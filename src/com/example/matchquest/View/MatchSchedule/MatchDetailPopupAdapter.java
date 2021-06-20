package com.example.matchquest.View.MatchSchedule;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.matchquest.R;
import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.model.Players;

public class MatchDetailPopupAdapter   extends RecyclerView.Adapter<MatchDetailPopupAdapter.MyViewHolder> {

	
	List<Players> playerList ;
	
	Context mContext;
	LayoutInflater inflater;
	Players Player;

	public MatchDetailPopupAdapter(Context context,  List<Players> objects)
	{
		this.playerList = objects;
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		
	}
	
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return playerList.size();
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		Players player = playerList.get(position);
		 
		 holder.name.setText(player.getPlayerName());
		 holder.num.setText("-"+player.getPlayerId());
		  if(player.isRegistered())
		  {
			  holder.name.setTextColor(Color.parseColor(TeamQuestConstants.registeredPlayerColor_key));
		  }else{
			  holder.name.setTextColor(Color.parseColor(TeamQuestConstants.nonregisteredPlayerColor_key));
		  }	
		
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
		 View view = inflater.inflate(R.layout.contacts_view, parent, false);
	     MyViewHolder holder = new MyViewHolder(view);
		return holder;
	}
	
	class MyViewHolder extends RecyclerView.ViewHolder {
		 TextView name;
		 TextView num;
		 CheckBox selected;
		public MyViewHolder(View itemView) {
			super(itemView);
			  name = (TextView) itemView.findViewById(R.id.contactName);
			  num = (TextView) itemView.findViewById(R.id.contactNum);
			  selected = (CheckBox) itemView.findViewById(R.id.contact_checkbox);
			  selected.setVisibility(View.GONE);
			  
			   }
		
		}

}
