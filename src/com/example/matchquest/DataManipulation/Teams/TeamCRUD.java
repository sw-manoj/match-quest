package com.example.matchquest.DataManipulation.Teams;

import com.example.matchquest.db.team.TeamDB;
import com.example.matchquest.model.Team;


public class TeamCRUD {

	public int createTeam(Team team)
	{
		TeamDB teamDb = new TeamDB();
		return teamDb.createTeam(team);
	}
}
