package com.example.matchquest.View.MatchSchedule;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matchquest.R;
import com.example.matchquest.DataManipulation.MatchSchedule.MatchScheduleDM;
import com.example.matchquest.SQLiteData.TeamQuestSqlite;
import com.example.matchquest.View.CommonActivityWithFragment;
import com.example.matchquest.View.CommonFragment;
import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.model.RequestStatus;
import com.example.matchquest.model.Team;

public class MatchScheduleFragment extends CommonFragment{
	
	LayoutInflater inflater;
	ViewGroup container;
	View rootView;
	
	RecyclerView matchScheduleRecyclerView;
	
	MatchScheduleAdapter matchScheduleAdapter;
	
	List<RequestStatus> matchScheduleList = new ArrayList<RequestStatus>();
	
	TeamQuestSqlite matchScheduleSqlite;
	
	TextView emptyView;
	Team team;
	boolean initialLoad = true;
	
	@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
		
		this.inflater = inflater;
		this.container = container;
		team = (Team) getArguments().getSerializable(TeamQuestConstants.team_key);
		
		rootView = inflater.inflate(R.layout.match_schedule_fragment_layout, container, false);
		
		matchScheduleRecyclerView = (RecyclerView) rootView.findViewById(R.id.match_schedule_recycler_view);
		
		emptyView = (TextView) rootView.findViewById(R.id.empty_view);
		
		matchScheduleAdapter = new MatchScheduleAdapter((CommonActivityWithFragment) getActivityObject(), matchScheduleList, team);
		
		matchScheduleSqlite = new  TeamQuestSqlite(getActivity());
		
		matchScheduleList.addAll(matchScheduleSqlite.getMatchScheduleSqlite().getmatchSchedule(team));
		
		if(matchScheduleList == null || matchScheduleList.size() == 0)
		{
			matchScheduleRecyclerView.setVisibility(View.GONE);
			emptyView.setVisibility(View.VISIBLE);
			
		}else{
			matchScheduleAdapter.notifyDataSetChanged();
		}
		
		matchScheduleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
		
		matchScheduleRecyclerView.setAdapter(matchScheduleAdapter);
		
		registernetworkListener(false);
		initialLoad = true;
		return rootView;
		}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(!initialLoad)
		{
			if(getNetworkChangeReceiver()!= null)
			{
			getNetworkChangeReceiver().updateData();
			}
			
		}
		initialLoad = false;
	}
	
	@Override
	public void reGainLayout() {
	
		rootView.setAlpha(1);
	}
	
	@Override
	public void onPopUpOpen() {
	
		rootView.setAlpha(0.3f);
	}
	@Override
	protected int loadInBackGround() {
		MatchScheduleDM matchScheduleDm = new MatchScheduleDM();
		List<RequestStatus> statusList = matchScheduleDm.getMatchSchedule(team);
		if(statusList != null)
		{
		matchScheduleList.clear();
		matchScheduleList.addAll(statusList);
		matchScheduleSqlite.getMatchScheduleSqlite().insertMatchSchedule(team, matchScheduleList);
		return 1;
		}
		return -1;
	}

	@Override
	public void updateView() {
		if(matchScheduleList == null || matchScheduleList.size() == 0)
		{
			matchScheduleRecyclerView.setVisibility(View.GONE);
			emptyView.setVisibility(View.VISIBLE);
		}else{
			matchScheduleRecyclerView.setVisibility(View.VISIBLE);
			emptyView.setVisibility(View.GONE);
			matchScheduleAdapter.notifyDataSetChanged();
		}
	}

}
