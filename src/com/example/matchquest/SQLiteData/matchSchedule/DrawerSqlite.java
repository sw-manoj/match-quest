package com.example.matchquest.SQLiteData.matchSchedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.matchquest.SQLiteData.CommonSqlite;
import com.example.matchquest.model.MatchStatus;

public class DrawerSqlite {

	SQLiteDatabase database;
	
	SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.getDefault());
	
	Set<String> teamSet = new LinkedHashSet<String>();
	Set<String> dateStringSet = new LinkedHashSet<String>(); 
	
    public DrawerSqlite(SQLiteDatabase db) {
    	this.database = db;
    }
    
    public void deleteMatchStatus()
    {
    	database.delete(CommonSqlite.matchStatus_table,null,null);
    }
    
    public void insertMatchStatus(List<MatchStatus> matchStatusList)
    {
    	deleteMatchStatus();
    	
    	for(MatchStatus status : matchStatusList)
    	{
    		ContentValues contentValue = new ContentValues();
    		contentValue.put(CommonSqlite.matchStatus_teamId, status.getYourTeamId());
    		contentValue.put(CommonSqlite.matchStatus_teamCode, status.getYourTeamCode());
    		contentValue.put(CommonSqlite.matchStatus_teamName, status.getYourTeamName());
    		contentValue.put(CommonSqlite.matchStatus_uniqueId, status.getMatchStatusId());
    		contentValue.put(CommonSqlite.matchStatus_oppTeamId, status.getOpponentTeamId());

    		contentValue.put(CommonSqlite.matchStatus_oppTeamName, status.getOpponentTeam());
    		contentValue.put(CommonSqlite.matchStatus_oppTeamCode, status.getOpponentCode());
    		contentValue.put(CommonSqlite.matchStatus_loc, status.getLocation());
    		contentValue.put(CommonSqlite.matchStatus_date, getDateAsString(status.getDate()));
    		contentValue.put(CommonSqlite.matchStatus_time, status.getTime());
    		
    		database.insert(CommonSqlite.matchStatus_table, null, contentValue);
    	}
    	
//    	database.close();
    }
    
    public List<MatchStatus> getmatchStatus()
    {
    	List<MatchStatus> matchStatusList = new ArrayList<MatchStatus>();
    	teamSet.add("All (team)");
		dateStringSet.add("All (date)");

   	 	String selectQuery = "SELECT  * FROM " + CommonSqlite.matchStatus_table;

       Cursor cursor = database.rawQuery(selectQuery,null);
       if (cursor.moveToFirst()) {
           do {
        	   MatchStatus status = new MatchStatus();
        	   
        	   status.setMatchStatusId(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.matchStatus_uniqueId)));
        	   status.setYourTeamId(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.matchStatus_teamId)));
        	   status.setYourTeamName(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.matchStatus_teamName)));
        	   status.setYourTeamCode(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.matchStatus_teamCode)));
        	   
        	   status.setOpponentTeamId(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.matchStatus_oppTeamId)));
        	   status.setOpponentTeam(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.matchStatus_oppTeamName)));
        	   status.setOpponentCode(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.matchStatus_oppTeamCode)));
        	   
        	   
        	   status.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.matchStatus_loc)));
        	   status.setDate(getDateFromString(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.matchStatus_date))));
        	   status.setTime(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.matchStatus_time)));
        	   
        	   	teamSet.add(status.getYourTeamName() + " (" + status.getYourTeamCode() + ")");
   				dateStringSet.add(status.getDateString());
        	   matchStatusList.add(status);
           } while (cursor.moveToNext());
       }
//    	database.close();
    	return matchStatusList;
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

	public Set<String> getTeamSet() {
		return teamSet;
	}

	public void setTeamSet(Set<String> teamSet) {
		this.teamSet = teamSet;
	}

	public Set<String> getDateStringSet() {
		return dateStringSet;
	}

	public void setDateStringSet(Set<String> dateStringSet) {
		this.dateStringSet = dateStringSet;
	}
}
