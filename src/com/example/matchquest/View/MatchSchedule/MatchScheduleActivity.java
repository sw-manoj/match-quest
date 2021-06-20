package com.example.matchquest.View.MatchSchedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.matchquest.R;
import com.example.matchquest.View.CommonActivityWithFragment;
import com.example.matchquest.View.CommonFragment;
import com.example.matchquest.View.UpdateFragment;
import com.example.matchquest.View.Drawerlayout.MatchStatusFragment;
import com.example.matchquest.View.FindOpponent.FindOpponentFragment;
import com.example.matchquest.common.Details;
import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.model.Team;

public class MatchScheduleActivity  extends CommonActivityWithFragment{

	 MatchStatusFragment matchStatus;
	 Team team;
	 MatchScheduleFragment matchScheduleFragment;
	 UpdateFragment updateFragment;
	 boolean initialLoad = false;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.match_schdule_activity_layout);
		
		initialLoad = true;
		team = (Team) getIntent().getSerializableExtra(TeamQuestConstants.team_key);
		
		if(team != null)
	    {
	       	Details.setTeam(team);
	    }else{
	       	team = Details.getTeam();
	    }
//		getSupportActionBar().setTitle(team.getTeamName());
		getSupportActionBar().setTitle("");
		activitySetUp();
		
		LayoutInflater inflator = getLayoutInflater();
		 
		View view = inflator.inflate(R.layout.action_bar_layout, null);
		   
		getSupportActionBar().setCustomView(view);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		
		
		matchStatus = new MatchStatusFragment();
	    loadFragment(matchStatus, R.id.drawerlayout);
	       
	    updateFragment= new UpdateFragment();
	    loadFragment(updateFragment,R.id.update_container);
			
	    drawerLayoutSetUp();
	    
	    matchScheduleFragment =  new MatchScheduleFragment();
      	Bundle bundle = new Bundle();
		bundle.putSerializable(TeamQuestConstants.team_key, team);
		matchScheduleFragment.setArguments(bundle);
		loadFragment(matchScheduleFragment,R.id.match_schedule_container);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.findopponent_menu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 if (getDrawerToggle().onOptionsItemSelected(item)) {
			 if(initialLoad)
			 {
				 matchStatus.getTeamSpinner().setSelection(
				 matchStatus.getTeamSpinnerAdapter().getPosition(team.getTeamName()+" (" + team.getTeamCode() +")"));
				 initialLoad = false;
			 }
	   
		 	}
		 return super.onOptionsItemSelected(item);	
	}
	
	@Override
	public CommonFragment getFrament() {
		// TODO Auto-generated method stub
		return matchScheduleFragment;
	}

}
