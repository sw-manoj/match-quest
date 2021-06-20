package com.example.matchquest.SQLiteData.Teams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.matchquest.SQLiteData.CommonSqlite;
import com.example.matchquest.model.Players;
import com.example.matchquest.model.Team;


public class PLayersSqlite {

	SQLiteDatabase database;
	
	
	public PLayersSqlite(SQLiteDatabase db) {
		this.database = db;
	}
	
//setting All Contacts
    public List<Players> getAllPlayers(Team team) {
        List<Players> playerList = new ArrayList<Players>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + CommonSqlite.player_table
        		 + " WHERE "+ CommonSqlite.player_team+" =? ";
 
        Cursor cursor = database.rawQuery(selectQuery, new String[]{team.getTeamId()});
 
        if (cursor.moveToFirst()) {
            do {
                Players player = new Players();
                player.setPlayerId(cursor.getString(cursor.getColumnIndex(CommonSqlite.player_id)));
                player.setPlayerName(cursor.getString(cursor.getColumnIndex(CommonSqlite.player_name)));
                player.setRegistered(cursor.getString(cursor.getColumnIndex(CommonSqlite.player_registered)).equals("1") ? true : false);

                playerList.add(player);
            } while (cursor.moveToNext());
        }
 
        return playerList;
    }
    
    public void deletePlayers(List<Players> playerData,Team team )
    {
        database.delete(CommonSqlite.player_table, CommonSqlite.player_team + " =?", new String[]{team.getTeamId()});
    }
    
    public void insertPlayers(List<Players> playerData,Team team ,boolean toBeDeleted) {
    	if(toBeDeleted)
    	{
        deletePlayers(playerData, team);
    	}
        for(Players player : playerData)
        {
            	ContentValues values = new ContentValues();
  		        values.put(CommonSqlite.player_id, player.getPlayerId());
  		        values.put(CommonSqlite.player_name, player.getPlayerName());
  		        values.put(CommonSqlite.player_registered, player.isRegistered() ? "1" : "0" );
  		        values.put(CommonSqlite.player_team, team.getTeamId());
  		      database.insert(CommonSqlite.player_table, null, values);
  		  
        }
    }
    
    public void removePlayer(Team team,String playerId)
    {
    	database.delete(CommonSqlite.player_table, CommonSqlite.player_id + "= ? and " + 
    					CommonSqlite.player_team + " = ? ", new String[]{playerId,team.getTeamId()});
    }
    
    public List<Players> getPlayers(List<String> playersIdList ,Team team) {
        List<Players> playerList = new ArrayList<Players>();
        // Select All Query
        String param = "";
        for(String playerId : playersIdList)
        {
        	param = param + "'" + playerId + "',";
        }
         param = param.substring(0,param.length() - 1);
         
        String selectQuery = "SELECT  * FROM " + CommonSqlite.player_table
        		 + " WHERE "+ CommonSqlite.player_id+" in ( "+ param +") and "  +CommonSqlite.player_team + " = ? ";
 
        
        Cursor cursor = database.rawQuery(selectQuery, new String[]{team.getTeamId()});
        
        if (cursor.moveToFirst()) {
            do {
                Players player = new Players();
                player.setPlayerId(cursor.getString(cursor.getColumnIndex(CommonSqlite.player_id)));
                player.setPlayerName(cursor.getString(cursor.getColumnIndex(CommonSqlite.player_name)));
                player.setRegistered(cursor.getString(cursor.getColumnIndex(CommonSqlite.player_registered)).equals("1") ? true : false);

                playerList.add(player);
            } while (cursor.moveToNext());
        }
        
        return playerList;
    }
    
    public List<Players> getPlayersInsertIfDontExist(List<String> playersIdList ,Team team) {
        List<Players> playerList = new ArrayList<Players>();
        // Select All Query
        for(String playerId : playersIdList)
        {
        
        String selectQuery = "SELECT  * FROM " + CommonSqlite.player_table
        		 + " WHERE "+ CommonSqlite.player_id+" = ? and "  +CommonSqlite.player_team + " = ? ";
 
        
        Cursor cursor = database.rawQuery(selectQuery, new String[]{playerId,team.getTeamId()});
        
        if (cursor.moveToFirst()) {
            do {
                Players player = new Players();
                player.setPlayerId(cursor.getString(cursor.getColumnIndex(CommonSqlite.player_id)));
                player.setPlayerName(cursor.getString(cursor.getColumnIndex(CommonSqlite.player_name)));
                player.setRegistered(cursor.getString(cursor.getColumnIndex(CommonSqlite.player_registered)).equals("1") ? true : false);

                playerList.add(player);
            } while (cursor.moveToNext());
        }else{
        	Players player = new Players();
            player.setPlayerId(playerId);
            player.setPlayerName(playerId);
            player.setRegistered(false);

            playerList.add(player);
        	insertPlayers(new ArrayList<Players>(Arrays.asList(player)), team, false);
        }
        }
        
        return playerList;
    }
    
    public void clsoeConnection()
    {
    	database.close();
    }
}
