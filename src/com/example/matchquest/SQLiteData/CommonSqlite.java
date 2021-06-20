package com.example.matchquest.SQLiteData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CommonSqlite {
	
	public static String strSeparator = "__,__";
	
	public static String keyValueSeparator = "__=__";
	
	public static String mapSeparator = "__&__";
	
	public static String database_name = "teamQuest.db";
	
	public static String teams_table = "teams";
	public static String teams_id = "teamId";
	public static String teams_name = "teamName";
	public static String teams_code = "teamCode";
	public static String teams_players = "players";
	public static String teams_nop = "noofplayers";
	public static String teams_captain = "captain";
	public static String teams_vicecaptain = "viceCaptain";
	
	public static String player_table = "players";
	public static String player_id = "playerId";
	public static String player_name = "player_name";
	public static String player_registered = "is_registered";
	public static String player_team = "teamId";
	
	public static String teamDetail_table = "teamDetail";
	public static String teamDetail_topicId = "topicId";
	public static String teamDetail_topic = "topic";
	public static String teamDetail_teamId = "teamId";
	public static String teamDetail_matchStatusId = "matchStatusId";
	public static String teamDetail_optionIds = "optionIds";
	public static String teamDetail_options = "options";
	public static String teamDetail_option = "option";
	public static String teamDetail_category = "category";
	public static String teamDetail_createdBy = "createdBy";
	
	public static String topicDetail_comments_table = "topicDetailComments";
	public static String topicDetail_comments_topicId = "topicDetailCommentsTopicId";
	public static String topicDetail_comments_teamId = "topicDetailCommentsTeamId";
	public static String topicDetail_comments_playerId = "topicDetailCommentsPlayerId";
	public static String topicDetail_comments_comment = "topicDetailCommentsComment";
	
	public static String matchSchedule_table = "matchSchedule";
	public static String matchSchedule_uniqueId = "matchscheduleUniqueId";
	public static String matchSchedule_teamId = "matchscheduleTeamId";
	public static String matchSchedule_teamName = "matchscheduleTeamName";
	public static String matchSchedule_teamCode = "matchscheduleTeamCode";
	public static String matchSchedule_oppTeamId = "matchscheduleOppTeamId";
	public static String matchSchedule_oppTeamName = "matchscheduleOppTeamName";
	public static String matchSchedule_oppTeamCode = "matchscheduleOppTeamCode";
	public static String matchSchedule_players = "matchschedulePlayers";
	public static String matchSchedule_loc = "matchscheduleLoc";
	public static String matchSchedule_date = "matchscheduleDate";
	public static String matchSchedule_time = "matchscheduleTime";
	public static String matchSchedule_nop = "matchscheduleNop";
	public static String matchSchedule_selected_players = "matchscheduleSelectedPlayer";
	
	public static String matchStatus_table = "matchStatus";
	public static String matchStatus_uniqueId = "matchStatusUniqueId";
	public static String matchStatus_teamId = "matchStatusTeamId";
	public static String matchStatus_teamName = "matchStatusTeamName";
	public static String matchStatus_teamCode = "matchStatusTeamCode";
	public static String matchStatus_oppTeamId = "matchStatusOppTeamId";
	public static String matchStatus_oppTeamName = "matchStatusOppTeamName";
	public static String matchStatus_oppTeamCode = "matchStatusOppTeamCode";
	public static String matchStatus_players = "matchStatusPlayers";
	public static String matchStatus_loc = "matchStatusLoc";
	public static String matchStatus_date = "matchStatusDate";
	public static String matchStatus_time = "matchStatusTime";
	
	public static String location_table = "locationTable";
	public static String location = "location";
	
	
	public static String convertListToString(List<String> array){
	    String str = "";
	    for (int i = 0;i<array.size(); i++) {
	        str = str+array.get(i);
	        // Do not append comma at the end of last element
	        if(i<array.size()-1){
	            str = str+strSeparator;
	        }
	    }
	    return str;
	}
	public static List<String> convertStringToList(String str){
	    String[] arr = str.split(strSeparator);
	    List<String> arrList = new ArrayList<String>();
	    for(int i = 0 ; i< arr.length ; i ++)
	    {
	    	arrList.add(arr[i]);
	    }
	    return arrList;
	}
	
	public static String convertMapToString(Map<String,List<String>> array){
	    String str = "";
	    int j = 0;
	    for(Entry<String, List<String>> entry : array.entrySet())
	    {
	    	String listStr = "";
	    	 for (int i = 0;i<entry.getValue().size(); i++) {
	    		 listStr = listStr+entry.getValue().get(i);
	    		 
	 	        if(i<entry.getValue().size()-1){
	 	        	listStr = listStr+strSeparator;
	 	        }
	 	    }
	    	 str = str + entry.getKey() + keyValueSeparator + listStr;
	    	 if(j<array.size()-1)
	    	 {
	    		 str = str + mapSeparator;
	    	 }
	    	 j++;
	    }
	    return str;
	}
	
	public static Map<String,ArrayList<String>> convertStringToMap(String str){
		Map<String,ArrayList<String>> strToMap = new HashMap<String, ArrayList<String>>();
		
		if(str != null && !str.trim().equals(""))
		{
		String[] mapArr = str.split(mapSeparator);
		for(int i = 0 ; i < mapArr.length ; i++)
		{
			String[] keyValue = mapArr[i].split(keyValueSeparator);
			
			ArrayList<String> list = new ArrayList<String>();
			if(keyValue.length == 2)
			{
				String[] keyValueArr = keyValue[1].split(strSeparator);
				for(int j = 0 ; j < keyValueArr.length ; j++)
				{
					list.add(keyValueArr[j]);
				}
			}
			strToMap.put(keyValue[0], list);
		}
		}
		return strToMap;
	}
	
	
	public static String convertMapStringToString(Map<String,String> array){
	    String str = "";
	    int j = 0;
	    for(Entry<String, String> entry : array.entrySet())
	    {
	    	 str = str + entry.getKey() + keyValueSeparator + entry.getValue();
	    	 if(j<array.size()-1)
	    	 {
	    		 str = str + mapSeparator;
	    	 }
	    	 j++;
	    }
	    return str;
	}
	
	public static Map<String,String> convertStringToMapString(String str){
		Map<String,String> strToMap = new HashMap<String, String>();
		if(str != null && !str.trim().equals(""))
		{
		String[] mapArr = str.split(mapSeparator);
		for(int i = 0 ; i < mapArr.length ; i++)
		{
			String[] keyValue = mapArr[i].split(keyValueSeparator);
			
			if(keyValue.length == 2)
			{
				strToMap.put(keyValue[0], keyValue[1]);
			}else{
				strToMap.put(keyValue[0], "");
			}
		}
		}
		return strToMap;
	}
	
	public static String[] convertStringToArray(String str){
	    String[] arr = str.split(strSeparator);
	    return arr;
	}
	
	public static String makePlaceholders(int len) {
	    if (len < 1) {
	        // It will lead to an invalid query anyway ..
	        throw new RuntimeException("No placeholders");
	    } else {
	        StringBuilder sb = new StringBuilder(len * 2 - 1);
	        sb.append("?");
	        for (int i = 1; i < len; i++) {
	            sb.append(",?");
	        }
	        return sb.toString();
	    }
	}
}
