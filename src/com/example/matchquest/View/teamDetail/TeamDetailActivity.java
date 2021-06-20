package com.example.matchquest.View.teamDetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.matchquest.R;
import com.example.matchquest.View.CommonActivityWithFragment;
import com.example.matchquest.View.CommonFragment;
import com.example.matchquest.View.UpdateFragment;
import com.example.matchquest.View.Drawerlayout.MatchStatusFragment;
import com.example.matchquest.View.teams.TeamEditActivity;
import com.example.matchquest.common.Details;
import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.model.Team;

public class TeamDetailActivity extends CommonActivityWithFragment{
	
	 MatchStatusFragment matchStatus;
	 boolean isUpdateVisible;
	 Team team;
	 boolean initialLoad = false;
	 TeamDetailFragment teamDetailFragment;
	 UpdateFragment updateFragment;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team_detail_layout);
		
	   initialLoad = true;
	   team =  (Team) getIntent().getSerializableExtra("Team");
	   
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
       
       teamDetailFragment =  new TeamDetailFragment();
       	Bundle bundle = new Bundle();
 		bundle.putSerializable(TeamQuestConstants.team_key, team);
 		bundle.putBoolean(TeamQuestConstants.toupdate_key, true);
 		teamDetailFragment.setArguments(bundle);
 		loadFragment(teamDetailFragment,R.id.team_detail_container);
 		

       
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.team_detail_menu, menu);
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
	        }else if(item.getItemId() == R.id.team_info)
	        {
				Intent teamEditIntent = new Intent(getApplicationContext(), TeamEditActivity.class);
				teamEditIntent.putExtra("Team", team);  
				startActivity(teamEditIntent);
	        	return true;
	        }
		return super.onOptionsItemSelected(item);
	}

	@Override
	public CommonFragment getFrament() {
		// TODO Auto-generated method stub
		return teamDetailFragment;
	}
		
}
