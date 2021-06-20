package com.example.matchquest.DataManipulation.DrawerLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.matchquest.DataManipulation.RequestStatus.RequestStatusDM;
import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.db.CommonDBConstants;
import com.example.matchquest.db.MatchStatus.MatchStatusDB;
import com.example.matchquest.model.MatchStatus;
import com.example.matchquest.model.Players;
import com.example.matchquest.model.RequestStatus;


public class DrawerLayoutDM {

	Set<String> teamSet = new LinkedHashSet<String>();
	Set<String> dateStringSet = new LinkedHashSet<String>(); 
	public List<MatchStatus> getMatchStatus()
	{
		List<MatchStatus> matchStausData = new ArrayList<MatchStatus>();
		Calendar cal = Calendar.getInstance();
		teamSet.add("All (team)");
		dateStringSet.add("All (date)");
//		for(int i = 0 ; i < 4 ; i ++)
//		{
//			MatchStatus matchStatus = new MatchStatus();
//			matchStatus.setOpponentTeam("opponent " + i);
//			matchStatus.setOpponentTeamId("opponent id " + i);
//			matchStatus.setOpponentCode("4321 " + i);
//			matchStatus.setYourTeamName("teamName " + i);
//			matchStatus.setYourTeamId("teamName id " + i);
//			matchStatus.setYourTeamCode("1234");
//			matchStatus.setMatchStatusId("matchStatus id " + i);
//			matchStatus.setLocation("location " + i);
//			matchStatus.setDate(cal.getTime());
//			matchStatus.setTime("7:00 am");
//			matchStausData.add(matchStatus);
//			teamSet.add(matchStatus.getYourTeamName() + " (" + matchStatus.getYourTeamCode() + ")");
//			dateStringSet.add(matchStatus.getDateString());
//		}
//		cal.add(Calendar.DATE, 1);
//
//		for(int i = 0 ; i < 4 ; i ++)
//		{
//			MatchStatus matchStatus = new MatchStatus();
//			matchStatus.setOpponentTeam("opponent " + i);
//			matchStatus.setOpponentTeamId("opponent id " + i);
//			matchStatus.setYourTeamName("teamName " + i);
//			matchStatus.setYourTeamId("teamName id " + i);
//			matchStatus.setYourTeamCode("1234");
//			matchStatus.setOpponentCode("4321 " + i);
//			matchStatus.setMatchStatusId("matchStatus id " + i);
//			matchStatus.setLocation("location 22" + i);
//			matchStatus.setDate(cal.getTime());
//			matchStatus.setTime("7 :30 am");
//			matchStausData.add(matchStatus);
//			
//			teamSet.add(matchStatus.getYourTeamName() + " (" + matchStatus.getYourTeamCode() + ")");
//			dateStringSet.add(matchStatus.getDateString());
//		}
//		
//		cal.add(Calendar.DATE, 4);
//
//		for(int i = 0 ; i < 4 ; i ++)
//		{
//			MatchStatus matchStatus = new MatchStatus();
//			matchStatus.setOpponentTeam("opponent " + i);
//			matchStatus.setOpponentTeamId("opponent id " + i);
//			matchStatus.setYourTeamName("teamName " + i);
//			matchStatus.setYourTeamId("teamName id " + i);
//			matchStatus.setMatchStatusId("matchStatus id " + i);
//			matchStatus.setLocation("location 44" + i);
//			matchStatus.setYourTeamCode("1234");
//			matchStatus.setOpponentCode("4321 " + i);
//			matchStatus.setDate(cal.getTime());
//			matchStatus.setTime("9 :30 am");
//			matchStausData.add(matchStatus);
//			
//			teamSet.add(matchStatus.getYourTeamName() + " (" + matchStatus.getYourTeamCode() + ")");
//			dateStringSet.add(matchStatus.getDateString());
//		}
		return matchStausData;
		
	}
	
	public List<MatchStatus> getMatchStatusByPlayer(String playerId)
	{
	
		List<MatchStatus> matchScheduleList = new ArrayList<MatchStatus>();
		
		teamSet.add("All (team)");
		dateStringSet.add("All (date)");
		
		try{
			JSONObject matchStatusDet = new JSONObject();
			Map<String,Players> playerDetMap = new HashMap<String, Players>();
			
			MatchStatusDB matchStatusDb = new MatchStatusDB();
			matchStatusDet = matchStatusDb.getMatchStatusByPlayer(playerId);
			
			JSONArray matchStatusDetArr = matchStatusDet.getJSONArray("matchDet");
			JSONArray playerDetArr = matchStatusDet.getJSONArray("playerDet");
			
			
			if(matchStatusDetArr != null)
			{
				RequestStatusDM requestStatusDm = new RequestStatusDM();
				playerDetMap = requestStatusDm.formPlayerDet(playerDetArr);
				
				for(int i = 0 ; i < matchStatusDetArr.length() ; i ++)
				{
					JSONObject matchStatusJson = matchStatusDetArr.getJSONObject(i);
					MatchStatus matchStatus = new MatchStatus();
										
					JSONArray teamDetArr = matchStatusJson.getJSONArray(CommonDBConstants.teamDet);
					for(int teamInc = 0 ; teamInc < teamDetArr.length() ; teamInc ++)
					{
						JSONObject teamObj = teamDetArr.getJSONObject(teamInc);
						if(!teamObj.getBoolean("isHome"))
						{
//							opponent team info
							matchStatus.setOpponentTeamId(teamObj.getString(CommonDBConstants.teamId));
							matchStatus.setOpponentTeam(teamObj.getString(CommonDBConstants.teamName));
							matchStatus.setOpponentCode(teamObj.getString(CommonDBConstants.teamCode));
						}else{
							
//							home team player opinion
							
							matchStatus.setYourTeamId(teamObj.getString(CommonDBConstants.teamId));
							matchStatus.setYourTeamName(teamObj.getString(CommonDBConstants.teamName));
							matchStatus.setYourTeamCode(teamObj.getString(CommonDBConstants.teamCode));
							
							JSONArray selectedPlayerIds = teamObj.getJSONArray(CommonDBConstants.selectedPlayerIds);
							Map<String,Players> selectedPlayersMap = new HashMap<String, Players>();
							for(int playerInc = 0 ; playerInc < selectedPlayerIds.length() ; playerInc ++)
							{
								if(playerDetMap.get(selectedPlayerIds.getString(playerInc)) != null)
								{
									Players player = playerDetMap.get(selectedPlayerIds.getString(playerInc));
									selectedPlayersMap.put(player.getPlayerId(), player);
								}
							}
							matchStatus.setSelectedPlayersMap(selectedPlayersMap);
							
							List<Players> playersListChild = new ArrayList<Players>();
							JSONArray interestedPlayerIds = teamObj.getJSONArray(CommonDBConstants.interestedPlayerIds);
							for(int playerInc = 0 ; playerInc < interestedPlayerIds.length() ; playerInc ++)
							{
								if(playerDetMap.get(interestedPlayerIds.getString(playerInc)) != null)
								{
									Players player = playerDetMap.get(interestedPlayerIds.getString(playerInc));
									playersListChild.add(player);
								}
							}
							matchStatus.setPlayersList(playersListChild);
						}
					}
					matchStatus.setUniqueId(matchStatusJson.getString(CommonDBConstants.matchId));
					matchStatus.setMatchStatusId(matchStatusJson.getString(CommonDBConstants.matchId));
					
					matchStatus.setDate(TeamQuestConstants.getDateConvDbToJava().parse(matchStatusJson.getString(CommonDBConstants.date)));
					matchStatus.setTime(matchStatusJson.getString(CommonDBConstants.time));
					matchStatus.setNop(matchStatusJson.getString(CommonDBConstants.nop));
					
					String location = "";
					List<String> locList = new ArrayList<String>();
					JSONArray locaArr = matchStatusJson.getJSONArray(CommonDBConstants.location);
					
					for(int j = 0 ; j < locaArr.length() ; j ++)
					{
						locList.add(locaArr.getString(j));
						location += locaArr.getString(j) + ", ";
					}
					location = location.substring(0, location.length()-2);
					
					matchStatus.setLocation(location);
					matchStatus.setLocationList(locList);
					
					List<Integer> topicIds = new ArrayList<Integer>();
					if(matchStatusJson.getJSONArray(CommonDBConstants.topicIds) != null && matchStatusJson.getJSONArray(CommonDBConstants.topicIds).length() > 0)
					{
						JSONArray topicIdsArr = matchStatusJson.getJSONArray(CommonDBConstants.topicIds);
						
						for(int j = 0 ; j < topicIdsArr.length() ;  j ++)
						{
							topicIds.add(topicIdsArr.getInt(j));
						}
					}
					
					matchStatus.setTopicIds(topicIds);
					
					matchScheduleList.add(matchStatus);
					
					teamSet.add(matchStatus.getYourTeamName() + " (" + matchStatus.getYourTeamCode() + ")");
					dateStringSet.add(matchStatus.getDateString());
				}
				
			}
			
		}catch(Exception e)
		{
			matchScheduleList = null;
		}
		
		return matchScheduleList;
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
