package com.example.matchquest.View.FindOpponent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.matchquest.R;
import com.example.matchquest.View.CommonActivityWithFragment;
import com.example.matchquest.View.CommonFragment;
import com.example.matchquest.View.UpdateFragment;
import com.example.matchquest.View.Drawerlayout.MatchStatusFragment;
import com.example.matchquest.View.RequestStatus.RequestStatusFragment;
import com.example.matchquest.common.Details;
import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.model.Team;

public class FindOpponentActivity extends CommonActivityWithFragment{

	 MatchStatusFragment matchStatus;
	 Team team;
	 FindOpponentFragment findOpponentFragment;
	 UpdateFragment updateFragment;
	 boolean initialLoad = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.find_opponent_activity_layout);
		
		initialLoad = true;
		team = (Team) getIntent().getSerializableExtra(TeamQuestConstants.team_key);
		
		if(team != null)
	    {
	       	Details.setTeam(team);
	    }else{
	       	team = Details.getTeam();
	    }
		getSupportActionBar().setTitle(team.getTeamName());
		activitySetUp();
		   
		matchStatus = new MatchStatusFragment();
	    loadFragment(matchStatus, R.id.drawerlayout);
	       
	    updateFragment= new UpdateFragment();
	    loadFragment(updateFragment,R.id.update_container);
			
	    drawerLayoutSetUp();
	    
	    findOpponentFragment =  new FindOpponentFragment();
      	Bundle bundle = new Bundle();
		bundle.putSerializable(TeamQuestConstants.team_key, team);
		findOpponentFragment.setArguments(bundle);
		loadFragment(findOpponentFragment,R.id.find_oppoent_container);
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
		return findOpponentFragment;
	}

}
