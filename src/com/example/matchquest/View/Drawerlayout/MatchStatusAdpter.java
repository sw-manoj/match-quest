package com.example.matchquest.View.Drawerlayout;

import java.util.ArrayList;
import java.util.List;

import com.example.matchquest.R;
import com.example.matchquest.View.teams.TeamAdapter;
import com.example.matchquest.model.MatchStatus;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu.OnDismissListener;


public class MatchStatusAdpter  extends RecyclerView.Adapter<MatchStatusAdpter.MyViewHolder>{
	Context mContext;
	int res;
	List<MatchStatus> matchStatusList;
	List<MatchStatus> originalList = new ArrayList<MatchStatus>();
	private LayoutInflater inflater;
	private String teamFilter = "team filter";
	private String dateFilter = "date filter";
	
	public MatchStatusAdpter(Context context, int resource, int textViewResourceId,
			List<MatchStatus> objects)
	{
		mContext = context;
		res = resource;
		inflater = LayoutInflater.from(context);
		matchStatusList = objects;	
		originalList.addAll(objects);
	}

	   class MyViewHolder extends RecyclerView.ViewHolder {
	        TextView matchStatusYourTeam;
	        TextView matchStatusOppoTeam;
	        TextView matchStatusLoc;
	        TextView matchStatusDate;
	        RelativeLayout matchDetailLayout;
	 
	        public MyViewHolder(View itemView) {
	            super(itemView);
	            
	            matchStatusYourTeam = (TextView) itemView.findViewById(R.id.matchStatusYourTeam);
	            matchStatusOppoTeam = (TextView) itemView.findViewById(R.id.matchStatusOppoTeam);
	            matchStatusLoc = (TextView) itemView.findViewById(R.id.matchStatusLoc);
	            matchStatusDate = (TextView) itemView.findViewById(R.id.matchStatusDate);
	            matchDetailLayout = (RelativeLayout) itemView.findViewById(R.id.drayerlayout);
	            matchDetailLayout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Toast.makeText(mContext, "loy",1000).show();			
					}
				});
	        }
	    }

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return matchStatusList.size();
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		MatchStatus status = matchStatusList.get(position);
		holder.matchStatusYourTeam.setText(status.getYourTeamName() +" (" + status.getYourTeamCode() + ")");
		holder.matchStatusOppoTeam.setText(status.getOpponentTeam() +" (" + status.getOpponentCode() + ")");
		holder.matchStatusLoc.setText(status.getLocation());
		holder.matchStatusDate.setText(status.getDateString() + "   " + status.getTime());
		holder.matchDetailLayout.setTag(status);
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
		View view = inflater.inflate(R.layout.drawerlayout_adpter, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
	}
	public void applyFilter(String teamName,String date)
	{
		teamFilter= teamName == null   ? teamFilter : teamName;
		dateFilter = (date == null) ? dateFilter : date;
		matchStatusList.clear();
		if((dateFilter.equals("All (date)") || dateFilter.equals("date filter")) && (teamFilter.equals("All (team)") || teamFilter.equals("team filter")))
		{
			matchStatusList.addAll(originalList);
		}else if(teamFilter.equals("All (team)") || teamFilter.equals("team filter")){
			for(MatchStatus status: originalList)
			{
				if(status.getDateString().equals(dateFilter))
				{
					matchStatusList.add(status);
				}
			}
		}else if(dateFilter.equals("All (date)") || dateFilter.equals("date filter")){
			teamFilter = teamFilter.substring(0, teamFilter.indexOf("(") != -1 ? teamFilter.indexOf("(")-1  : teamFilter.length());
			
			for(MatchStatus status: originalList)
			{

				if(status.getYourTeamName().equals(teamFilter) )
				{
					matchStatusList.add(status);
				}
			}
		}else{
			teamFilter = teamFilter.substring(0, teamFilter.indexOf("(") != -1 ? teamFilter.indexOf("(")-1  : teamFilter.length());
			for(MatchStatus status: originalList)
			{

				if(status.getYourTeamName().equals(teamFilter) && status.getDateString().equals(dateFilter) )
				{
					matchStatusList.add(status);
				}
			}
		}
		notifyDataSetChanged();
	}
	
	public void notifyDataSetChangedCustom(List<MatchStatus> list) {
//		matchStatusList.clear();
//		matchStatusList.addAll(list);
//		above two lines are not required because changes in fragment will reflect here so no need to add it again.
//		here in fragemetn it is changed which is reflceted here too so commented now
		originalList.clear();
		originalList.addAll(list);
		notifyDataSetChanged();
	}

}
