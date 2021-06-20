package com.example.matchquest.common;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;

import com.example.matchquest.model.Players;
import com.example.matchquest.model.Team;


public class Details {
	
	
	static Players player;
	
	static Team team;
	
	static SharedPreferences prefs;
	
	public static Players getPlayer(ActionBarActivity activityObject) {
		
		if(player == null)
		{
			if(prefs == null)
			{
				prefs = activityObject.getSharedPreferences(TeamQuestConstants.teamQuest_key, activityObject.MODE_PRIVATE);
			}
			 Players player = new Players();
	            player.setPlayerId(prefs.getString(TeamQuestConstants.playerId_key, null));
	        	player.setPlayerName(prefs.getString(TeamQuestConstants.playerName_key, null));
	        	player.setRegistered(true);
	        	setPlayer(player);
		}
		return player;
	}
	public static void setPlayer(Players player) {
		Details.player = player;
	}
	public static Team getTeam() {
		return team;
	}
	public static void setTeam(Team team) {
		Details.team = team;
	}
	
	
	

}
