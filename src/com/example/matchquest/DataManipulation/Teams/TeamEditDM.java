package com.example.matchquest.DataManipulation.Teams;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.matchquest.View.teams.TeamEditActivity;
import com.example.matchquest.db.CommonDBConstants;
import com.example.matchquest.db.team.TeamDB;
import com.example.matchquest.model.Contacts;
import com.example.matchquest.model.Players;
import com.example.matchquest.model.Team;



public class TeamEditDM {

	ProgressDialog mProgressDialog;
	
	TeamEditActivity teamEditActivity;
	
	boolean toModifyCaptain;
	
	public List<Players> addTeamMembers(Team team)
	{	
		List<Players> playerList = null;
		TeamDB teamDb = new TeamDB();
		JSONArray playerDetArr = teamDb.addTeamMember(team);
		try{
		if(playerDetArr != null)
		{
			playerList = new ArrayList<Players>();
			for(int i = 0 ; i < playerDetArr.length() ; i ++)
			{
				JSONObject playerDet = (JSONObject) playerDetArr.get(i);
				Players player = new Players();
				player.setPlayerId(playerDet.getString(CommonDBConstants.playerId));
				player.setPlayerName(playerDet.getString(CommonDBConstants.playerName));
				player.setRegistered(playerDet.getBoolean(CommonDBConstants.isRegistered));
				
				playerList.add(player);
			}
		}
		}catch(Exception e)
		{
			playerList = null;
		}
		return playerList;
	}
	
	public int updateTeamName(Team team)
	{
		TeamDB teamDb = new TeamDB();
		return teamDb.changeTeamName(team);
	}
	
	public Team removeFromTeam(Team team , String playerId)
	{
		TeamDB teamDb = new TeamDB();
		return teamDb.removeFromTeam(team, playerId);
	}
	public int makeViceCaptain(TeamEditActivity activity)
	{
		teamEditActivity = activity;
		toModifyCaptain = false;
		new changeLeaderTask().execute(teamEditActivity.getTeam());
		return 1;
	}
	public int makeCaptain(TeamEditActivity activity)
	{
		teamEditActivity = activity;
		toModifyCaptain = true;
		new changeLeaderTask().execute(teamEditActivity.getTeam());
		return 1;
	}
	
	public Team getTeamDetails(Team team )
	{
		return team;
	}
	
	private class changeLeaderTask extends AsyncTask<Team, Void, Integer>{

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			if(!isCancelled())
			{
		
			mProgressDialog = new ProgressDialog(teamEditActivity);
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
				TeamDB teamDb = new TeamDB();
				result = teamDb.changeLeader(params[0], teamEditActivity.getSelectedPlayerId(), toModifyCaptain);
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
					if(toModifyCaptain)
					{
						teamEditActivity.getTeamEditAdpter().getTeam().setCaptain(teamEditActivity.getSelectedPlayerId());	
					}else{
						teamEditActivity.getTeamEditAdpter().getTeam().setViceCaptain(teamEditActivity.getSelectedPlayerId());
					}
					teamEditActivity.getTeamEditAdpter().notifyDataSetChanged();
					
				
				}else{
					Toast.makeText(teamEditActivity.getApplicationContext(), "Something wrong happened please try again ", 1000).show();
				}
			}
		}
		
	}
}
