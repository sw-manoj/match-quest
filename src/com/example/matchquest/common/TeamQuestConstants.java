package com.example.matchquest.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.example.matchquest.db.CommonDBConstants;

public class TeamQuestConstants {

	public static final String teamQuest_key = "TeamQuest";
	
	public static final String team_key = "Team";
	
	public static final String toupdate_key = "toUpdate";
	
	public static final String screenName_key = "screenName";
	
	public static final String matchDetailScreen_key = "matchStatus";
	
	public static final String teamDetailScreen_key = "teamDetail";
	
	public static final String nextgame_key = "NextGame";
	
	public static final String matchdetail_key = "MatchDetail";
	
	public static final String general_key = "General";
	
	public static final String matchstatus_key = "MatchStatus";
		
	public static final String topicDetail_key = "TopicDetail";
	
	public static final String connectToInternet_key = "Please ,Connect to internet !!";
	
	public static final String playerId_key = "PlayerNo";
	
	public static final String playerName_key = "PlayerName";
	
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat( "MMM dd" ); 
	
	public static final int success_key = 1;
	
	public static final int failure_key = -1;
	
	public static final String emptyUniqueId_key = "emptyUniqueId";
	
	public static final String saveRequestStatusFailure_key = "Something went wrong please try again";
	
	public static final String date_key = "Date";
	
	public static final String nop_key = "No of players";
	
	public static final String time_key = "Time"; 
	
	public static final String findOpponent_key = "FindOpponent"; 
	
	public static final String matchSchedule_key = "MatchStatus"; 
	
	public static final String requestStatus_key = "RequestStatus";
	
	public static final String registeredPlayerColor_key = "#0277BD";
	
	public static final String nonregisteredPlayerColor_key = "#FF0000";
	
	public static final String updationError_key = "Something wrong happened please try again ";
	
	public static final String JsonDateToJava(String dateString)
	{
		SimpleDateFormat fmt;
		if (dateString.endsWith("Z")) {
		    fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
		} else {
		    fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sssZ");
		}
		
		try {
			return dateFormat.format(fmt.parse(dateString));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public static final SimpleDateFormat getDateConvDbToJava()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat;
	}
}
