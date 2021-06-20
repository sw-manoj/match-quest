package com.example.matchquest.View.RequestStatus;

import java.util.List;

import com.example.matchquest.R;
import com.example.matchquest.model.Players;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RequestStatusPopUpAdapter  extends RecyclerView.Adapter<RequestStatusPopUpAdapter.MyViewHolder>{
	
	List<Players> playersList;
	
	Context mContext;
	
	LayoutInflater inflater;
	
	public RequestStatusPopUpAdapter(Context context,List<Players> players)
	{
		this.mContext = context;
		this.inflater = LayoutInflater.from(mContext);
		playersList = players;
	}

	class MyViewHolder extends RecyclerView.ViewHolder {
		
		TextView playerName;
		 
		 public MyViewHolder(View itemView) {
				super(itemView);
				playerName = (TextView) itemView.findViewById(R.id.playerName);
		}
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return playersList.size();
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		Players player = playersList.get(position);
		
		holder.playerName.setText(player.getPlayerName());
		if(player.isRegistered())
		{
			holder.playerName.setTextColor(Color.parseColor("#0277BD"));
		}else{
			holder.playerName.setTextColor(Color.parseColor("#FF0000"));
		}	
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
		 View view = inflater.inflate(R.layout.request_status_popup_adapter, parent, false);
	     MyViewHolder holder = new MyViewHolder(view);
		return holder;
	}
}
