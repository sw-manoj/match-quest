package com.example.matchquest.SQLiteData.Teams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.matchquest.SQLiteData.CommonSqlite;
import com.example.matchquest.model.Contacts;
import com.example.matchquest.model.Players;
import com.example.matchquest.model.Team;


public class TeamListSqlite   {
	 
	 SQLiteDatabase database;
    public TeamListSqlite(SQLiteDatabase db) {
    	this.database = db;
    }

    /**
     * Inserts User into SQLite DB
     * @param queryValues
     */
    public void insertTeams(List<Team> teamData) {

    	database.delete(CommonSqlite.teams_table, null, null);
        for(Team t : teamData)
        {
            	ContentValues values = new ContentValues();
  		        values.put("teamId", t.getTeamId());
  		        values.put("teamName", t.getTeamName());
  		        values.put("teamCode", t.getTeamCode());
  		        List<String> playersList = new ArrayList<String>();
  		        for(Players player : t.getPlayersList())
  		        {
  		        	playersList.add(player.getPlayerId());
  		        }
  		        PLayersSqlite playerSqlite = new PLayersSqlite(database);
  		        playerSqlite.insertPlayers(t.getPlayersList(), t, true);
  		        
  		        values.put("players", CommonSqlite.convertListToString(playersList));
  		      	values.put("noofplayers", t.getPlayersList().size());
  		    	values.put("captain", t.getCaptain());
  		    	values.put("viceCaptain", t.getViceCaptain());
  		      database.insert("teams", null, values);
  		  
        }
        database.close();
    }
 
    public void insertTeam(Team teamData) {
        
       
            	ContentValues values = new ContentValues();
  		        values.put("teamId", teamData.getTeamId());
  		        values.put("teamName", teamData.getTeamName());
  		        values.put("teamCode", teamData.getTeamCode());
  		        List<String> playersList = new ArrayList<String>();
		        for(Players player : teamData.getPlayersList())
		        {
		        	playersList.add(player.getPlayerId());
		        }
  		        values.put("players", CommonSqlite.convertListToString(playersList));
		      	values.put("noofplayers", teamData.getPlayersList().size());
		    	values.put("captain", teamData.getCaptain());
		    	values.put("viceCaptain", teamData.getViceCaptain());
  		        database.insert("teams", null, values);
        
        database.close();
    }
    /**
     * Get list of Users from SQLite DB as Array List
     * @return
     */
    public ArrayList<Team> getAllUsers() {
        ArrayList<Team> usersList;
        usersList = new ArrayList<Team>();
        String selectQuery = "SELECT  teamId,teamName,teamCode ,"+
        						CommonSqlite.teams_captain +"," +
        						CommonSqlite.teams_vicecaptain +"," +
        						CommonSqlite.teams_nop +"," +
        						CommonSqlite.teams_players  +" FROM teams";
        
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                Team t = new Team();
                t.setTeamId(cursor.getString(0));
                t.setTeamName(cursor.getString(1));
                t.setTeamCode(cursor.getString(2));
                t.setCaptain(cursor.getString(3));
                t.setViceCaptain(cursor.getString(4));
                t.setNumPlayers(Integer.parseInt(cursor.getString(5)));
                
                List<String> playersList = CommonSqlite.convertStringToList(cursor.getString(6));
                PLayersSqlite playerSqlite = new PLayersSqlite(database);
                t.setPlayersList(playerSqlite.getPlayers(playersList, t));
                usersList.add(t);
            } while (cursor.moveToNext());
        }
        database.close();
        return usersList;
    }
    
    public void insertTeamMembers(Team team,List<Contacts> playerList )
    {
    	Cursor cursor = database.query(CommonSqlite.teams_table, new String[] { CommonSqlite.teams_players}, CommonSqlite.teams_id + "=?",
                new String[] { String.valueOf(team.getTeamId()) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        List<String> players = new ArrayList<String>();
        players.addAll(Arrays.asList(CommonSqlite.convertStringToArray(cursor.getString(0))));
        for(Contacts pla : playerList)
        {
        	players.add(pla.getNumber());
        }
        ContentValues values = new ContentValues();
        values.put("players", CommonSqlite.convertListToString(players));
        values.put("noofplayers", players.size());
 
        // updating row
        database.update("teams", values, "teamId" + " = ?",
                new String[] { String.valueOf(team.getTeamId()) });
    }
    
    public void updateTeamName(Team team, String name)
    {
    	ContentValues values = new ContentValues();
        values.put(CommonSqlite.teams_name, name);
 
        // updating row
         database.update(CommonSqlite.teams_table, values, CommonSqlite.teams_id + " = ?",
                new String[] { team.getTeamId() });
    }
    
    public void removeFromTeam(Team team, String playerId)
    {
    	Cursor cursor = database.query(CommonSqlite.teams_table, new String[] { CommonSqlite.teams_players}, CommonSqlite.teams_id + "=?",
                new String[] { String.valueOf(team.getTeamId()) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        List<String> players = new ArrayList<String>();
        players.addAll(CommonSqlite.convertStringToList(cursor.getString(0)));
        if(players.remove(playerId))
        {
        	
        	ContentValues values = new ContentValues();
        	values.put("players", CommonSqlite.convertListToString(players));
        	values.put("noofplayers", players.size());
        	
        	database.update(CommonSqlite.teams_table, values, CommonSqlite.teams_id + " = ?",
        			new String[] { team.getTeamId() });
        }

    }
 
}
