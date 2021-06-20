package com.example.matchquest.View.RequestStatus;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.matchquest.R;
import com.example.matchquest.View.CommonActivityWithFragment;
import com.example.matchquest.View.CommonFragment;
import com.example.matchquest.View.UpdateFragment;
import com.example.matchquest.View.Drawerlayout.MatchStatusFragment;
import com.example.matchquest.common.Details;
import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.model.Team;

public class RequestStatusActivity extends CommonActivityWithFragment	{
	
	MatchStatusFragment matchStatus;
	 Team team;
	 RequestStatusFragment requestStatusFragment;
	 UpdateFragment updateFragment;
	 boolean initialLoad = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.request_status_detail_activity);
		
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
	   
	   	requestStatusFragment =  new RequestStatusFragment();
      	Bundle bundle = new Bundle();
		bundle.putSerializable(TeamQuestConstants.team_key, team);
		requestStatusFragment.setArguments(bundle);
		loadFragment(requestStatusFragment,R.id.request_status_container);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.request_status_menu, menu);
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

	public RequestStatusFragment getRequestStatusFragment() {
		return requestStatusFragment;
	}

	public void setRequestStatusFragment(RequestStatusFragment requestStatusFragment) {
		this.requestStatusFragment = requestStatusFragment;
	}

	@Override
	public CommonFragment getFrament() {
		// TODO Auto-generated method stub
		return requestStatusFragment;
	}

}
