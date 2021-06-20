package com.example.matchquest.DataManipulation.Teams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.matchquest.DataManipulation.FindOpponent.FindOpponentDM;
import com.example.matchquest.db.CommonDBConstants;
import com.example.matchquest.db.team.TeamDB;
import com.example.matchquest.model.Players;
import com.example.matchquest.model.RequestStatus;
import com.example.matchquest.model.Team;


public class TeamListDM {

	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
	
	public List<Team> getTeamData(String playerId) 
	{
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		List<Team> teamData = null;
		TeamDB teamDb = new TeamDB();
		JSONArray teamDetArr = teamDb.getTeamData(playerId);
		
		try{
		
		if(teamDetArr != null)
		{
			teamData = new ArrayList<Team>();
		for(int i = 0 ; i < teamDetArr.length() ; i ++)
		{
			JSONObject teamDet = (JSONObject) teamDetArr.get(i);
			Team team = new Team();
			
			team.setTeamId(teamDet.getString(CommonDBConstants.teamId));
			team.setTeamCode(teamDet.getString(CommonDBConstants.teamCode));
			team.setTeamName(teamDet.getString(CommonDBConstants.teamName));
			team.setCaptain(teamDet.getString(CommonDBConstants.captain));
			team.setViceCaptain(teamDet.getString(CommonDBConstants.viceCaptain));
			
			List<Integer> topicIds = new ArrayList<Integer>();
			if(teamDet.getJSONArray(CommonDBConstants.topicIds) != null && teamDet.getJSONArray(CommonDBConstants.topicIds).length() > 0)
			{
				JSONArray topicIdsArr = teamDet.getJSONArray(CommonDBConstants.topicIds);
				
				for(int j = 0 ; j < topicIdsArr.length() ;  j ++)
				{
					topicIds.add(topicIdsArr.getInt(j));
				}
			}
			
			team.setTopicIds(topicIds);
			
			List<Players> playerList = new ArrayList<Players>();
			
			if(teamDet.getJSONArray(CommonDBConstants.playerIds) != null && teamDet.getJSONArray(CommonDBConstants.playerIds).length() > 0)
			{
				JSONArray playerIdsArr = teamDet.getJSONArray(CommonDBConstants.playerIds);
				for(int j = 0 ; j < playerIdsArr.length() ; j++)
				{
					JSONObject playerDet = playerIdsArr.getJSONObject(j);
					Players player = new Players();
					
					player.setPlayerId(playerDet.getString(CommonDBConstants.playerId));
					player.setPlayerName(playerDet.getString(CommonDBConstants.playerName));
					player.setRegistered(playerDet.getBoolean(CommonDBConstants.isRegistered));
					
					playerList.add(player);
				}
			}
			
			team.setPlayersList(playerList);
			team.setNumPlayers(playerList.size());
			
//			JSONArray inviteDetArr = teamDet.getJSONArray(CommonDBConstants.invites);
//			team.setInvitesList(formRequestStatusList(inviteDetArr));
			
			teamData.add(team);
		}
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return teamData;
	}
	
	public List<RequestStatus> formRequestStatusList(JSONArray inviteDetArr)
	{
		List<RequestStatus> requestStatusList = new ArrayList<RequestStatus>();
		
		try{
			
		
		for(int i = 0 ; i < inviteDetArr.length() ; i ++)
		{
			RequestStatus requestStatus = new RequestStatus();
			
			JSONObject invite = inviteDetArr.getJSONObject(i);
			
			
			String location = "";
			List<String> locList = new ArrayList<String>();
			JSONArray locaArr = invite.getJSONArray(CommonDBConstants.location);
			
			for(int j = 0 ; j < locaArr.length() ; j ++)
			{
				locList.add(locaArr.getString(j));
				location += locaArr.getString(j) + ", ";
			}
			location = location.substring(0, location.length()-1);
			
			requestStatus.setLocation(location);
			requestStatus.setLocationList(locList);
			requestStatus.setTime(invite.getString(CommonDBConstants.time));
			requestStatus.setDate(dateFormat.parse(invite.getString(CommonDBConstants.date)));
			
			requestStatus.setNop(invite.getString(CommonDBConstants.nop));
			requestStatus.setUniqueId(invite.getString(CommonDBConstants.inviteId));
			
			JSONArray nonInterestedPLayerDetArr = invite.getJSONArray(CommonDBConstants.nonInterestedPlayerIds);
			formPlayerDet(requestStatus, nonInterestedPLayerDetArr);
			
			JSONArray requestDetArr = invite.getJSONArray(CommonDBConstants.request);
			if(requestDetArr != null)
			{
				List<RequestStatus> requestDetList = new ArrayList<RequestStatus>();
				for(int k = 0 ; k  < requestDetArr.length() ; k ++)
				{
					RequestStatus request = new RequestStatus();
					
					JSONObject requestDet = requestDetArr.getJSONObject(k);
					request.setUniqueId(requestDet.getString(CommonDBConstants.inviteId));
					request.setRequestSent(requestDet.getBoolean(CommonDBConstants.isRequestSent));
					
					JSONArray playerDetArr  = requestDet.getJSONArray(CommonDBConstants.playerIds);
					formPlayerDet(request, playerDetArr);
					
					requestDetList.add(request);
				}
				
				requestStatus.setOpponentInvites(requestDetList);
			}
			
			requestStatusList.add(requestStatus);
		}
		}catch(Exception e)
		{
			requestStatusList = null;
		}
		
		return requestStatusList;
	}
	
	public void formPlayerDet(RequestStatus request,JSONArray playerDetArr)
	{
		try{
			if(playerDetArr != null)
			{
				boolean isPlayerDetAvailable = false;
				List<Players> playersList = new ArrayList<Players>();
				List<String> playerStringList = new ArrayList<String>();
				for(int inc = 0 ; inc < playerDetArr.length() ; inc ++)
				{
					
					Object object = playerDetArr.get(inc);
					if(object instanceof JSONObject)
					{
						isPlayerDetAvailable = true;
						JSONObject playerDet = (JSONObject) object;
						Players player = new Players();
						player.setPlayerId(playerDet.getString(CommonDBConstants.playerId));
						player.setPlayerName(playerDet.getString(CommonDBConstants.playerName));
						player.setRegistered(playerDet.getBoolean(CommonDBConstants.isRegistered));
						playersList.add(player);
					}else if(object instanceof String)
					{
						playerStringList.add((String)object);
					}
				}
				if(isPlayerDetAvailable)
				{
					request.setPlayersList(playersList);
				}else{
					request.setPlayerStringList(playerStringList);
				}
			}
		}catch(Exception e)
		{
			
		}
	}
}
