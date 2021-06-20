package com.example.matchquest.SQLiteData.matchSchedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.matchquest.SQLiteData.CommonSqlite;
import com.example.matchquest.SQLiteData.Teams.PLayersSqlite;
import com.example.matchquest.model.Players;
import com.example.matchquest.model.RequestStatus;
import com.example.matchquest.model.Team;

public class MatchScheduleSqlite {
	
	SQLiteDatabase database;
	
	SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.getDefault());
	
    public MatchScheduleSqlite(SQLiteDatabase db) {
    	this.database = db;
    }
    
    public void deleteMatchSchedule(Team team)
    {
    	database.delete(CommonSqlite.matchSchedule_table,CommonSqlite.matchSchedule_teamId+ " =?", new String[]{team.getTeamId()});
    }

    
    public void insertMatchSchedule(Team team,List<RequestStatus> matchScheduleList)
    {
    	deleteMatchSchedule(team);
    	
    	for(RequestStatus status : matchScheduleList)
    	{
    		ContentValues contentValue = new ContentValues();
    		contentValue.put(CommonSqlite.matchSchedule_teamId, team.getTeamId());
    		contentValue.put(CommonSqlite.matchSchedule_uniqueId, status.getUniqueId());
    		contentValue.put(CommonSqlite.matchSchedule_oppTeamId, status.getTeamId());
//    		contentValue.put(CommonSqlite.matchSchedule_table, 
//    				CommonSqlite.convertMapStringToString(topicDetail.getOptionIds()));
    		List<String> playersList = new ArrayList<String>();
    		for(Players player : status.getPlayersList())
    		{
    			playersList.add(player.getPlayerId());
    		}
    				
    		contentValue.put(CommonSqlite.matchSchedule_players, 
    				CommonSqlite.convertListToString(playersList));
    		contentValue.put(CommonSqlite.matchSchedule_oppTeamName, status.getTeamName());
    		contentValue.put(CommonSqlite.matchSchedule_oppTeamCode, status.getTeamCode());
    		contentValue.put(CommonSqlite.matchSchedule_loc, status.getLocation());
    		contentValue.put(CommonSqlite.matchSchedule_date, getDateAsString(status.getDate()));
    		contentValue.put(CommonSqlite.matchSchedule_time, status.getTime());
    		contentValue.put(CommonSqlite.matchSchedule_nop, status.getNop());
    		
    		List<String> selectedPlayersList = new ArrayList<String>();
    		for(Players player : status.getSelectedPlayersMap().values())
    		{
    			selectedPlayersList.add(player.getPlayerId());
    		}
    		contentValue.put(CommonSqlite.matchSchedule_selected_players, 
    				CommonSqlite.convertListToString(playersList));
    		
    		database.insert(CommonSqlite.matchSchedule_table, null, contentValue);
    	}
    	
    	database.close();
    }
    
    public List<RequestStatus> getmatchSchedule(Team team)
    {
    	List<RequestStatus> matchScheduleList = new ArrayList<RequestStatus>();

   	 	String selectQuery = "SELECT  * FROM " + CommonSqlite.matchSchedule_table
       		 + " WHERE "+ CommonSqlite.matchSchedule_teamId+" =? ";

       Cursor cursor = database.rawQuery(selectQuery, new String[]{team.getTeamId()});
       if (cursor.moveToFirst()) {
           do {
        	   RequestStatus status = new RequestStatus();
        	   
        	   status.setUniqueId(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.matchSchedule_uniqueId)));
        	   status.setTeamId(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.matchSchedule_oppTeamId)));
        	   status.setTeamName(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.matchSchedule_oppTeamName)));
        	   status.setTeamCode(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.matchSchedule_oppTeamCode)));
        	   status.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.matchSchedule_loc)));
        	   status.setDate(getDateFromString(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.matchSchedule_date))));
        	   status.setTime(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.matchSchedule_time)));
        	   List<String> playersList = CommonSqlite.convertStringToList(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.matchSchedule_players)));
        	   
        	   PLayersSqlite playerSqlite = new PLayersSqlite(database);
        	   status.setPlayersList(playerSqlite.getPlayersInsertIfDontExist(playersList, team));
        	   status.setNop(status.getPlayersList() == null  ? "0" : status.getPlayersList().size() + "");
        	   
        	   playersList = CommonSqlite.convertStringToList(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.matchSchedule_selected_players)));
        	   
        	   List<Players> selectedPlayersList = playerSqlite.getPlayersInsertIfDontExist(playersList, team); 
        	   Map<String,Players> selectedPlayersMap = new HashMap<String, Players>();
        	   for(Players players : selectedPlayersList)
        	   {
        		   selectedPlayersMap.put(players.getPlayerId(), players);
        	   }
        	   
        	   status.setSelectedPlayersMap(selectedPlayersMap);
        	   
        	   matchScheduleList.add(status);
           } while (cursor.moveToNext());
       }
    	database.close();
    	return matchScheduleList;
    }
    
    
    private String getDateAsString(Date date)
    {
    	return dateFormat.format(date);
    }
    
    private Date getDateFromString(String date)
    {
    	try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return null;
		}
    }
}
