package com.example.matchquest.View.Drawerlayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.example.matchquest.MainActivity;
import com.example.matchquest.R;
import com.example.matchquest.DataManipulation.DrawerLayout.DrawerLayoutDM;
import com.example.matchquest.SQLiteData.TeamQuestSqlite;
import com.example.matchquest.SQLiteData.matchSchedule.DrawerSqlite;
import com.example.matchquest.View.CommonFragment;
import com.example.matchquest.View.CommonViewClass;
import com.example.matchquest.common.Details;
import com.example.matchquest.model.MatchStatus;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MatchStatusFragment extends CommonFragment{
	MatchStatusAdpter matchStatusAdapter;
	RecyclerView drawerRecyclerView;
	Spinner teamFilter;
	Spinner dateFilter;
	TeamQuestSqlite drawerSqlite;
	TextView emptyTextView;
	
	List<MatchStatus> statusList = new ArrayList<MatchStatus>();
	
	List<String> teamFilterList = new ArrayList<String>();
	List<String> dateFilterList = new ArrayList<String>();
	ArrayAdapter<String> teamSpinnerAdapter; 
	ArrayAdapter<String> dateSpinnerAdapter;
	
	String playerId;
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.match_status_drawerlayout_view, container,false);
		drawerRecyclerView = (RecyclerView) view.findViewById(R.id.navList);
		drawerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
		
		drawerSqlite = new TeamQuestSqlite(getActivity());
		
		emptyTextView = (TextView) view.findViewById(R.id.empty_view);
		
		playerId = Details.getPlayer(getActivityObject()).getPlayerId();
		statusList.clear();
		statusList.addAll(drawerSqlite.getDrawerSqlite().getmatchStatus());
		
		if(statusList.size() == 0)
		{
			emptyTextView.setVisibility(View.VISIBLE);
			drawerRecyclerView.setVisibility(View.GONE);
		}
		
		matchStatusAdapter = new MatchStatusAdpter(getActivity(),0,0, statusList);
		drawerRecyclerView.setAdapter(matchStatusAdapter);
				
	    teamFilterList.addAll(drawerSqlite.getDrawerSqlite().getTeamSet());
	    teamFilterList.add("team filter");
	    dateFilterList.addAll(drawerSqlite.getDrawerSqlite().getDateStringSet());
	    dateFilterList.add("date filter");
	    
	        teamFilter  = (Spinner) view.findViewById(R.id.teamSpinner);
	         teamSpinnerAdapter = new ArrayAdapter<String>(getActivity(),
	        		android.R.layout.simple_spinner_item, teamFilterList){
	        	@Override
	        	public int getCount() {
	        		 int count = super.getCount();
	        		 return count > 0 ? count - 1 : count;
	        	}
	        };
	        teamSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        teamFilter.setAdapter(teamSpinnerAdapter);
//	        teamFilter.setSelection(teamSpinnerAdapter.getCount());
	        
	        dateFilter  = (Spinner) view.findViewById(R.id.dateSpinner);
	        dateSpinnerAdapter = new ArrayAdapter<String>(getActivity(),
	        		android.R.layout.simple_spinner_item, dateFilterList){
	        	@Override
	        	public int getCount() {
	        		 int count = super.getCount();
	        		 return count > 0 ? count - 1 : count;
	        	}
	        };
	        dateSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        dateFilter.setAdapter(dateSpinnerAdapter);
//	        dateFilter.setSelection(dateSpinnerAdapter.getCount());
	        
	        teamFilter.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					String teamFilter = teamFilterList.get(position);
					matchStatusAdapter.applyFilter(teamFilter,null);
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
			});
	        
	        dateFilter.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					matchStatusAdapter.applyFilter(null,dateFilterList.get(position));
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
			});
	        
	        if(CommonViewClass.isNetworkAvailable(getActivityObject()))
			  {
	        		reload();
			  }
		return view;
	}
	
	public MatchStatusAdpter getAdapter()
	{
		return matchStatusAdapter;
	}
	
	public Spinner getTeamSpinner()
	{
		return teamFilter;
	}
	
	public ArrayAdapter<String> getTeamSpinnerAdapter()
	{
		return teamSpinnerAdapter;
	}

	@Override
	protected int loadInBackGround() {
		DrawerLayoutDM matchStatusData = new DrawerLayoutDM();
		List<MatchStatus> list = matchStatusData.getMatchStatusByPlayer(playerId);
	    
		teamFilterList.clear();
	    dateFilterList.clear();
	    teamFilterList.addAll(matchStatusData.getTeamSet());
	    teamFilterList.add("team filter");
	    dateFilterList.addAll(matchStatusData.getDateStringSet());
	    dateFilterList.add("date filter");
	    
		if(list != null)
		{
		statusList.clear();
		statusList.addAll(list);
	   
	    	    
	    drawerSqlite.getDrawerSqlite().insertMatchStatus(list);
	    return 1;
		}
	    return -1;
	}

	@Override
	public void updateView() {
		 if(statusList.size() > 0)
		 {
			emptyTextView.setVisibility(View.GONE);
			drawerRecyclerView.setVisibility(View.VISIBLE);
			matchStatusAdapter.notifyDataSetChangedCustom(statusList);
		 }else{
			emptyTextView.setVisibility(View.VISIBLE);
			drawerRecyclerView.setVisibility(View.GONE);
		 }
		 teamSpinnerAdapter.notifyDataSetChanged();
		 dateSpinnerAdapter.notifyDataSetChanged();
		
	}
	
	
}
