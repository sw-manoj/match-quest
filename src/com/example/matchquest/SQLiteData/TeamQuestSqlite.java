package com.example.matchquest.SQLiteData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.matchquest.SQLiteData.CommonSqlite;
import com.example.matchquest.SQLiteData.Teams.LocationSqlite;
import com.example.matchquest.SQLiteData.Teams.PLayersSqlite;
import com.example.matchquest.SQLiteData.Teams.TeamDetailSqlite;
import com.example.matchquest.SQLiteData.Teams.TeamListSqlite;
import com.example.matchquest.SQLiteData.matchSchedule.DrawerSqlite;
import com.example.matchquest.SQLiteData.matchSchedule.MatchScheduleSqlite;

public class TeamQuestSqlite   extends SQLiteOpenHelper {

	PLayersSqlite playerSqlite;
	TeamListSqlite teamSqlite;
	TeamDetailSqlite teamDetailSqlite;
	MatchScheduleSqlite matchScheduleSqlite;
	LocationSqlite locationSqlite;
	

	DrawerSqlite drawerSqlite;
	
	public TeamQuestSqlite(Context context) {
		super(context, CommonSqlite.database_name, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String query;
        query = "CREATE TABLE teams ( teamId TEXT PRIMARY KEY,teamName TEXT, teamCode TEXT,"
        		+ "players TEXT , noofplayers INTEGER ,captain TEXT , viceCaptain TEXT)";
        db.execSQL(query);
        
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + CommonSqlite.player_table + "("
                + CommonSqlite.player_id + " TEXT ," + CommonSqlite.player_name + " TEXT," + CommonSqlite.player_team + " TEXT,"
                + CommonSqlite.player_registered + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        
        String createTeamDetailTable = "CREATE TABLE " + CommonSqlite.teamDetail_table + "("
                + CommonSqlite.teamDetail_topicId + " TEXT ," + CommonSqlite.teamDetail_topic + " TEXT," + CommonSqlite.teamDetail_teamId + " TEXT,"
                + CommonSqlite.teamDetail_matchStatusId + " TEXT ," + CommonSqlite.teamDetail_option + " TEXT," + CommonSqlite.teamDetail_optionIds + " TEXT,"
                + CommonSqlite.teamDetail_options + " TEXT ," + CommonSqlite.teamDetail_createdBy + " TEXT," 
                + CommonSqlite.teamDetail_category + " TEXT" + ")";
        db.execSQL(createTeamDetailTable);
        
        String CREATE_COMMENT_TABLE = "CREATE TABLE " + CommonSqlite.topicDetail_comments_table + "("
                + CommonSqlite.topicDetail_comments_playerId + " TEXT ," + CommonSqlite.topicDetail_comments_teamId + " TEXT," + CommonSqlite.topicDetail_comments_topicId + " TEXT,"
                + CommonSqlite.topicDetail_comments_comment + " TEXT" + ")";
        db.execSQL(CREATE_COMMENT_TABLE);
        
        String createMatchScheduleTable = "CREATE TABLE " + CommonSqlite.matchSchedule_table + "("
                + CommonSqlite.matchSchedule_teamId + " TEXT ," 
        		+ CommonSqlite.matchSchedule_uniqueId + " TEXT," 
                + CommonSqlite.matchSchedule_oppTeamId + " TEXT,"
                + CommonSqlite.matchSchedule_oppTeamCode + " TEXT,"
                + CommonSqlite.matchSchedule_oppTeamName + " TEXT,"
                + CommonSqlite.matchSchedule_date + " DATETIME,"
                + CommonSqlite.matchSchedule_time + " TEXT,"
                + CommonSqlite.matchSchedule_players + " TEXT,"
                + CommonSqlite.matchSchedule_selected_players + " TEXT,"
                + CommonSqlite.matchSchedule_nop + " TEXT,"
                + CommonSqlite.matchSchedule_loc + " TEXT" + ")";
        db.execSQL(createMatchScheduleTable);
        
        String createMatchStatusTable = "CREATE TABLE " + CommonSqlite.matchStatus_table + "("
                + CommonSqlite.matchStatus_teamId + " TEXT ," 
                + CommonSqlite.matchStatus_teamName + " TEXT ," 
                + CommonSqlite.matchStatus_teamCode + " TEXT ," 
        		+ CommonSqlite.matchStatus_uniqueId + " TEXT," 
                + CommonSqlite.matchStatus_oppTeamId + " TEXT,"
                + CommonSqlite.matchStatus_oppTeamCode + " TEXT,"
                + CommonSqlite.matchStatus_oppTeamName + " TEXT,"
                + CommonSqlite.matchStatus_date + " DATETIME,"
                + CommonSqlite.matchStatus_time + " TEXT,"
                + CommonSqlite.matchStatus_loc + " TEXT" + ")";
        db.execSQL(createMatchStatusTable);
        
        String createLocationTable = "CREATE TABLE " + CommonSqlite.location_table + "("
                + CommonSqlite.location + " TEXT" + ")";
        db.execSQL(createLocationTable);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	public PLayersSqlite getPlayerSqlite()
	{
		playerSqlite = new PLayersSqlite(this.getWritableDatabase());
		return playerSqlite;
	}
	
	public TeamListSqlite getTeamListSqlite()
	{
		teamSqlite = new TeamListSqlite(this.getWritableDatabase());
		return teamSqlite;
	}

	public TeamDetailSqlite getTeamDetailSqlite() {
		teamDetailSqlite = new TeamDetailSqlite(this.getWritableDatabase());
		return teamDetailSqlite;
	}

	public MatchScheduleSqlite getMatchScheduleSqlite() {
		matchScheduleSqlite = new MatchScheduleSqlite(this.getWritableDatabase());
		return matchScheduleSqlite;
	}

	public LocationSqlite getLocationSqlite() {
		locationSqlite = new LocationSqlite(this.getWritableDatabase());
		return locationSqlite;
	}
	public DrawerSqlite getDrawerSqlite() {
		if(drawerSqlite == null)
		{
		drawerSqlite = new DrawerSqlite(this.getWritableDatabase());
		}
		return drawerSqlite;
	}
	
}
