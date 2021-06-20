package com.example.matchquest.DataManipulation.MatchSchedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.R.integer;

import com.example.matchquest.DataManipulation.RequestStatus.RequestStatusDM;
import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.db.CommonDBConstants;
import com.example.matchquest.db.MatchStatus.MatchStatusDB;
import com.example.matchquest.model.Players;
import com.example.matchquest.model.RequestStatus;
import com.example.matchquest.model.Team;

public class MatchScheduleDM {

	public List<RequestStatus> getMatchSchedule(Team team)
	{
	
		List<RequestStatus> matchScheduleList = new ArrayList<RequestStatus>();
		
		try{
			JSONObject matchStatusDet = new JSONObject();
			Map<String,Players> playerDetMap = new HashMap<String, Players>();
			
			MatchStatusDB matchStatusDb = new MatchStatusDB();
			matchStatusDet = matchStatusDb.getMatchSchedule(team.getTeamId());
			
			JSONArray matchStatusDetArr = matchStatusDet.getJSONArray("matchDet");
			JSONArray playerDetArr = matchStatusDet.getJSONArray("playerDet");
			
			
			if(matchStatusDetArr != null)
			{
				RequestStatusDM requestStatusDm = new RequestStatusDM();
				playerDetMap = requestStatusDm.formPlayerDet(playerDetArr);
				
				for(int i = 0 ; i < matchStatusDetArr.length() ; i ++)
				{
					JSONObject matchStatusJson = matchStatusDetArr.getJSONObject(i);
					RequestStatus matchStatus = new RequestStatus();
										
					JSONArray teamDetArr = matchStatusJson.getJSONArray(CommonDBConstants.teamDet);
					for(int teamInc = 0 ; teamInc < teamDetArr.length() ; teamInc ++)
					{
						JSONObject teamObj = teamDetArr.getJSONObject(teamInc);
						if(!teamObj.getString(CommonDBConstants.teamId).equals(team.getTeamId()))
						{
//							opponent team info
							matchStatus.setTeamId(teamObj.getString(CommonDBConstants.teamId));
							matchStatus.setTeamName(teamObj.getString(CommonDBConstants.teamName));
							matchStatus.setTeamCode(teamObj.getString(CommonDBConstants.teamCode));
						}else{
							
//							home team player opinion
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
				}
				
			}
			
		}catch(Exception e)
		{
			matchScheduleList = null;
		}
		
		return matchScheduleList;
	}
	
	public int savePlayerOpinion(RequestStatus status)
	{
		int result = -1;
		try{
			
		JSONObject playerOptionDet = new JSONObject();
		playerOptionDet.put(CommonDBConstants.uniqueId, Integer.parseInt(status.getUniqueId()));
		playerOptionDet.put(CommonDBConstants.teamId, Integer.parseInt(status.getTeamId()));
		playerOptionDet.put(CommonDBConstants.toRemove, status.isToRemove());
		playerOptionDet.put(CommonDBConstants.playerId, status.getPlayerId());
		
		MatchStatusDB matchStatusDb = new MatchStatusDB();
		result = matchStatusDb.savePlayerOpinion(playerOptionDet);
		}catch(Exception e)
		{
			result = -1;
		}
		return result;
	}
	
	public int saveSelectedPlayers(RequestStatus requestStatus)
	{
		int result = -1;
		
		try{
			JSONObject saveSelectedPlayerDet = new JSONObject();
			saveSelectedPlayerDet.put(CommonDBConstants.uniqueId, Integer.parseInt(requestStatus.getUniqueId()));
			saveSelectedPlayerDet.put(CommonDBConstants.teamId, Integer.parseInt(requestStatus.getTeamId()));
			JSONArray playerIds = new JSONArray();
			for(String playerId : requestStatus.getPlayerStringList())
			{
				playerIds.put(playerId);
			}
			saveSelectedPlayerDet.put(CommonDBConstants.selectedPlayerIds, playerIds);
			
			MatchStatusDB matchStatusDb = new MatchStatusDB();
			result = matchStatusDb.saveSelectedPlayers(saveSelectedPlayerDet);
			
		}catch(Exception e)
		{
			result = -1;
		}
		return result;
	}
	
	
}
