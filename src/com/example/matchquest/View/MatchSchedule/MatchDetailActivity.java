package com.example.matchquest.View.MatchSchedule;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.matchquest.R;
import com.example.matchquest.View.CommonActivityWithFragment;
import com.example.matchquest.View.CommonFragment;
import com.example.matchquest.View.UpdateFragment;
import com.example.matchquest.View.Drawerlayout.MatchStatusFragment;
import com.example.matchquest.View.teamDetail.TeamDetailFragment;
import com.example.matchquest.common.Details;
import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.model.RequestStatus;
import com.example.matchquest.model.Team;

public class MatchDetailActivity extends CommonActivityWithFragment{

	 MatchStatusFragment matchStatus;
	 Team team;
	 boolean isUpdateVisible;
	 RequestStatus requestStatus;
	 boolean initialLoad = false;
	 MatchDetailFragment matchDetailFragment;
	 UpdateFragment updateFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.team_detail_layout);
		
		   initialLoad = true;
		   requestStatus =  (RequestStatus) getIntent().getSerializableExtra(TeamQuestConstants.requestStatus_key);
		   team = (Team) getIntent().getSerializableExtra(TeamQuestConstants.team_key);
		   		   
		   getSupportActionBar().setTitle(requestStatus.getTeamName());
		   activitySetUp();
	              

	       matchStatus = new MatchStatusFragment();
	       loadFragment(matchStatus, R.id.drawerlayout);
	       
	       updateFragment= new UpdateFragment();
			loadFragment(updateFragment,R.id.update_container);
			
	       drawerLayoutSetUp();
	       
	       matchDetailFragment =  new MatchDetailFragment();
	       Bundle bundle = new Bundle();
	 	   bundle.putSerializable(TeamQuestConstants.requestStatus_key, requestStatus);
	 	   bundle.putSerializable(TeamQuestConstants.team_key, team);
	 	   bundle.putBoolean(TeamQuestConstants.toupdate_key, true);
	 	   matchDetailFragment.setArguments(bundle);
	 	   loadFragment(matchDetailFragment,R.id.team_detail_container);
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
		return matchDetailFragment;
	}
	
	

}
