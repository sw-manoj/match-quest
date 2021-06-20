package com.example.matchquest.SQLiteData.Teams;

import java.util.ArrayList;
import java.util.List;

import com.example.matchquest.SQLiteData.CommonSqlite;
import com.example.matchquest.model.Players;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LocationSqlite {

	SQLiteDatabase database;
	
	public LocationSqlite(SQLiteDatabase db)
	{
		this.database = db;
	}
	
	public List<String> getLocation()
	{
		 List<String> locationList = new ArrayList<String>();
	        // Select All Query
	        String selectQuery = "SELECT  * FROM " + CommonSqlite.location_table;
	 
	        Cursor cursor = database.rawQuery(selectQuery,null);
	 
	        if (cursor.moveToFirst()) {
	            do {
	            	locationList.add( cursor.getString(0));
	            }while (cursor.moveToNext());
	        }
		return locationList;
	}
	
	public void insertLocation(List<String> locationList)
	{
		database.delete(CommonSqlite.location_table,null,null);
		
		for(String location : locationList)
		{
			ContentValues values = new ContentValues();
	        values.put(CommonSqlite.location, location);
	        
	      database.insert(CommonSqlite.location_table, null, values);
		}
	}
}
