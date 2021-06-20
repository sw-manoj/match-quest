package com.example.matchquest.View.teams;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matchquest.R;
import com.example.matchquest.DataManipulation.Teams.TeamEditDM;
import com.example.matchquest.DataManipulation.Teams.TeamListDM;
import com.example.matchquest.SQLiteData.TeamQuestSqlite;
import com.example.matchquest.View.CommonFragment;
import com.example.matchquest.View.CommonViewClass;
import com.example.matchquest.View.NetworkChangeReceiver;
import com.example.matchquest.common.Details;
import com.example.matchquest.model.Players;
import com.example.matchquest.model.Team;





public class TeamListFragment extends CommonFragment implements View.OnClickListener {

	
	// Declare Variables
	View rootView;
	RecyclerView recyclerView;
	TextView emptyText;
	ProgressDialog mProgressDialog;
	TeamAdapter adapter;
	List<String> teamId;
	String playerId;
	List<Team> teamData;
	TeamQuestSqlite teamListSqlite ;
	NetworkChangeReceiver networkChangeReceiver;
	
	String selectedPlayerId;
	boolean initialLoad = false;
	
	public TeamListFragment() {
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		playerId = getArguments().getString("playerNo");
		rootView = inflater.inflate(R.layout.fragment_team_list, container, false);
		emptyText = (TextView) rootView.findViewById(R.id.empty_view);
		recyclerView = (RecyclerView) rootView.findViewById(R.id.drawerList);
			registerForContextMenu(recyclerView);

		 ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(false);
		teamData = new ArrayList<Team>();
		teamListSqlite = new  TeamQuestSqlite(getActivity());
		teamData.addAll(teamListSqlite.getTeamListSqlite().getAllUsers());
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
			// Pass the results into an ArrayAdapter
			adapter = new TeamAdapter(getActivity().getApplicationContext(), 0, 0, teamData,recyclerView,this);
			recyclerView.setAdapter(adapter);

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
	public void onClick(View v)
	{

		switch(v.getId())
		{
		
		}

	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		 super.onCreateOptionsMenu(menu, inflater);
		
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
	  	 MenuInflater inflater = getActivity().getMenuInflater();
         inflater.inflate(R.menu.longpress_menu, menu);
       
	  	 

	}
	 
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id){
			
			case R.id.team_info:
//				deRegisterNetworkReceiver();
				Intent teamEditIntent = new Intent(getActivity(), TeamEditActivity.class);
				teamEditIntent.putExtra("Team", adapter.getLongPressTeam());  
				startActivity(teamEditIntent);
				break;
			case R.id.delete_team : 
				deleteFromTeam(adapter.getLongPressTeam(), Details.getPlayer(getActivityObject()).getPlayerId());
				getNetworkChangeReceiver().updateData();
				break;	
		}
		return true;
	}
	

	public void deleteFromTeam(Team team, String playerId)
	{
		if(!CommonViewClass.isNetworkAvailable(getActivity()))
		{
			CommonViewClass.showdialog(getActivity(), "Please ,Connect to internet !!");
			return;
		}
		
		selectedPlayerId = playerId;
		new RemoveTeamMemberTask().execute(team);
		
	}
	
		@Override
		protected int loadInBackGround() {
			TeamListDM teamListDm = new TeamListDM();
			List<Team> teamList = teamListDm.getTeamData(Details.getPlayer(getActivityObject()).getPlayerId());
			//if teamList is null then some error has happened so we dont do anything with current data
			if(teamList != null)
			{
			 teamData.clear();
			
			 teamData.addAll(teamList);
			 teamListSqlite.getTeamListSqlite().insertTeams(teamData);
			 
			 return 1;
			 
			}else{
				return -1;
			}
			
		}

		@Override
		public void updateView() {
			if(teamData != null && teamData.size() >0 )
			{
			teamId = new ArrayList<String>();
			
			adapter.notifyDataSetChanged();


			}else{
				recyclerView.setVisibility(View.GONE);
				emptyText.setVisibility(View.VISIBLE);
			}
			
		}
	
		private class RemoveTeamMemberTask extends AsyncTask<Team, Void, Integer>{

			@Override
			protected void onPreExecute() {
				
				super.onPreExecute();
				if(!isCancelled())
				{
			
				mProgressDialog = new ProgressDialog(getActivityObject());
				mProgressDialog.setMessage("Loading...");
				mProgressDialog.setIndeterminate(true);
				mProgressDialog.setCancelable(false);
				mProgressDialog.show();
				
				}
			}
			@Override
			protected Integer doInBackground(Team... params) {
				int result = 0;
				if(!isCancelled())
				{
				TeamEditDM teamEdit = new TeamEditDM();
				
				Team newTeam = teamEdit.removeFromTeam(params[0], selectedPlayerId);
				if(newTeam != null)
				{
					result = 1;
				}
				}
				return result;
			}
			
			@Override
			protected void onPostExecute(Integer result) {
				
				super.onPostExecute(result);
				mProgressDialog.dismiss();
				if(!isCancelled())
				{
					if(result == 1)
					{
						if(networkChangeReceiver != null)
						{
							networkChangeReceiver.updateData();
						}
					}else{
						Toast.makeText(getActivityObject().getApplicationContext(), "Something wrong happened please try again ", 1000).show();
					}
				}
			}
			
		}
}