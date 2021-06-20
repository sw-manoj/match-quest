package com.example.matchquest.DataManipulation.RequestStatus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import android.R.integer;
import android.support.v7.internal.widget.ActivityChooserView.InnerLayout;

import com.example.matchquest.R;
import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.db.CommonDBConstants;
import com.example.matchquest.db.RequestStatus.RequestStatusDB;
import com.example.matchquest.model.Players;
import com.example.matchquest.model.RequestStatus;
import com.example.matchquest.model.Team;

public class RequestStatusDM {
	
	List<RequestStatus> requestStatusChildList = new ArrayList<RequestStatus>();
	
	public Map<Long,RequestStatus> getRequestStatusDetails1(Team team)
	{
		
		Calendar cal = Calendar.getInstance();
		Map<Long,RequestStatus> requestStatusMap = new LinkedHashMap<Long, RequestStatus>();
		
		for(int i = 0 ; i < 4 ; i ++)
		{
			cal.add(Calendar.DATE, 2);
				long uniqueIdLong = 111L +i;
				RequestStatus requestStatusParent = new RequestStatus();
			
				requestStatusParent.setTeamId(team.getTeamId());
				requestStatusParent.setTeamName(team.getTeamName());
				requestStatusParent.setTeamCode(team.getTeamCode());
				requestStatusParent.setUniqueId("home inviteId" + i);
				requestStatusParent.setUniqueIdLong(uniqueIdLong);
				requestStatusParent.setParent(true);
				requestStatusParent.setDate(cal.getTime());
				requestStatusParent.setTime("7 : 00 am");
				
				requestStatusParent.setLocation("santhome, adyar");
				requestStatusParent.setParentInviteId(requestStatusParent.getUniqueId());
				
				List<Players> playersList = new ArrayList<Players>();
				for(int k = i ; k < 4 ; k++)
				{
					Players player = new Players();
					player.setPlayerId("955159116" + k);
					player.setPlayerName("955159116" + k);
					player.setRegistered(k%2 == 0 ? true : false);
					playersList.add(player);
				}
				requestStatusParent.setPlayersList(playersList);
				requestStatusParent.setNop(requestStatusParent.getPlayersList().size() + "");
				
				requestStatusMap.put(requestStatusParent.getUniqueIdLong(), requestStatusParent);
				
				if(i == 2)
				{
					RequestStatus requestStatusChild = new RequestStatus();
					requestStatusChild.setTeamId(null);
					requestStatusChild.setUniqueId(TeamQuestConstants.emptyUniqueId_key+uniqueIdLong);
					requestStatusChild.setUniqueIdLong(uniqueIdLong);
					requestStatusChild.setParent(false);
					requestStatusChild.setParentInviteId(requestStatusParent.getUniqueId());	
					List<Players> playersListChild = new ArrayList<Players>();
							
					requestStatusChild.setPlayersList(playersListChild);
					
					requestStatusChildList.add(requestStatusChild);
				}else{
				for(int j = 0 ; j < 4; j ++)
				{
					RequestStatus requestStatusChild = new RequestStatus();
					requestStatusChild.setTeamId("opp id" + i + " " + j);
					requestStatusChild.setTeamName("opp team Name" + i + " " + j);
					requestStatusChild.setTeamCode("opp team code" + i + " " + j);
					requestStatusChild.setUniqueId("opp inviteId" + i + " " + j);
					requestStatusChild.setUniqueIdLong(uniqueIdLong);
					requestStatusChild.setParent(false);
					requestStatusChild.setRequestSent(i>2 ? true : false);
					requestStatusChild.setDate(cal.getTime());
					requestStatusChild.setTime("7 : 00 am");
					
					requestStatusChild.setLocation("santhome, adyar" + i + " " + j);
					requestStatusChild.setParentInviteId(requestStatusParent.getUniqueId());	
					List<Players> playersListChild = new ArrayList<Players>();
							for(int k = j ; k < 8 ; k++)
							{
								Players player = new Players();
								player.setPlayerId("955159116" + k);
								player.setPlayerName("955159116" + k);
								player.setRegistered(k%2 == 0 ? true : false);
								playersListChild.add(player);
							}
					requestStatusChild.setPlayersList(playersListChild);
					requestStatusChild.setNop(requestStatusChild.getPlayersList().size()+"");
					
					requestStatusChildList.add(requestStatusChild);
				}
				}
			
				
		}
		
		return requestStatusMap;
	}

	public List<RequestStatus> getRequestStatusChildList()
	{
		return requestStatusChildList;
	}
	
	public Map<String,Players> formPlayerDet(JSONArray playerDetArr)
	{
		Map<String,Players> playerMap = new HashMap<String, Players>();
		try{
			
			if(playerDetArr != null){
				for(int i = 0 ; i < playerDetArr.length() ; i ++)
				{
					JSONObject playerDet = playerDetArr.getJSONObject(i);
					Players player = new Players();
					player.setPlayerId(playerDet.getString(CommonDBConstants.playerId));
					player.setPlayerName(playerDet.getString(CommonDBConstants.playerName));
					player.setRegistered(playerDet.getBoolean(CommonDBConstants.isRegistered));
					
					playerMap.put(player.getPlayerId(), player);
				}
			}
		}catch(Exception e)
		{
			
		}
		return playerMap;
		
	}
	
	private Map<String,RequestStatus> formInviteDetail(JSONArray inviteDetArr){
		Map<String,RequestStatus> inviteDetMap = new HashMap<String, RequestStatus>();
		try{
			if(inviteDetArr != null)
			{
				for(int i = 0 ; i < inviteDetArr.length() ; i++)
				{
					JSONObject inviteDet = inviteDetArr.getJSONObject(i);
					RequestStatus requestStatus = new RequestStatus();
					requestStatus.setTeamId(inviteDet.getString(CommonDBConstants.teamId));
					requestStatus.setTeamName(inviteDet.getString(CommonDBConstants.teamName));
					requestStatus.setTeamCode(inviteDet.getString(CommonDBConstants.teamCode));					
					
					String location = "";
					List<String> locList = new ArrayList<String>();
					JSONArray locaArr = inviteDet.getJSONArray(CommonDBConstants.location);
					
					for(int j = 0 ; j < locaArr.length() ; j ++)
					{
						locList.add(locaArr.getString(j));
						location += locaArr.getString(j) + ", ";
					}
					location = location.substring(0, location.length()-1);
					
					requestStatus.setLocation(location);
					requestStatus.setLocationList(locList);
					requestStatus.setTime(inviteDet.getString(CommonDBConstants.time));
					requestStatus.setDate(TeamQuestConstants.getDateConvDbToJava().parse(inviteDet.getString(CommonDBConstants.date)));
					
					requestStatus.setNop(inviteDet.getString(CommonDBConstants.nop));
					requestStatus.setUniqueId(inviteDet.getString(CommonDBConstants.inviteId));
					
					inviteDetMap.put(requestStatus.getUniqueId(), requestStatus);
					
				}
				
			}
			
		}catch(Exception e)
		{
			
		}
		return inviteDetMap;
	}
	
	public List<RequestStatus> formRequestStatusList(JSONArray inviteDetArr)
	{
		List<RequestStatus> requestStatusList = new ArrayList<RequestStatus>();
		
		try{
			
		
		for(int i = 0 ; i < inviteDetArr.length() ; i ++)
		{
			RequestStatus requestStatus = new RequestStatus();
			
			JSONObject invite = inviteDetArr.getJSONObject(i);
			
			requestStatus.setTeamId(invite.getString(CommonDBConstants.teamId));
			requestStatus.setTeamCode(invite.getString(CommonDBConstants.teamCode));
			requestStatus.setTeamName(invite.getString(CommonDBConstants.teamName));
			
			String location = "";
			List<String> locList = new ArrayList<String>();
			JSONArray locaArr = invite.getJSONArray(CommonDBConstants.location);
			
			for(int j = 0 ; j < locaArr.length() ; j ++)
			{
				locList.add(locaArr.getString(j));
				location += locaArr.getString(j) + ", ";
			}
			location = location.substring(0, location.length()-2);
			
			requestStatus.setLocation(location);
			requestStatus.setLocationList(locList);
			requestStatus.setTime(invite.getString(CommonDBConstants.time));
			requestStatus.setDate(TeamQuestConstants.getDateConvDbToJava().parse(invite.getString(CommonDBConstants.date)));
			
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
	
	public Map<Long,RequestStatus> getRequestStatusDetails(Team team)
	{
		Map<Long,RequestStatus> requestStatusMap = new LinkedHashMap<Long, RequestStatus>();
		JSONObject inviteDetDoc = getInviteDetail(team);
		
		Map<String,Players> playerDetMap = new HashMap<String, Players>();
		Map<String,RequestStatus> inviteDetMap = new HashMap<String, RequestStatus>();
		List<RequestStatus> inviteList;
		try{
		if(inviteDetDoc != null)
		{
			JSONArray inviteDetArr = inviteDetDoc.getJSONArray("inviteDet");
			JSONArray requestInviteDetArr = inviteDetDoc.getJSONArray("requestInviteDet");
			JSONArray playerDetArr = inviteDetDoc.getJSONArray("playerDet");
			
			inviteList = formRequestStatusList(inviteDetArr);
			playerDetMap = formPlayerDet(playerDetArr);
			inviteDetMap = formInviteDetail(requestInviteDetArr);
		
				
		if(inviteList != null && inviteList.size() > 0)
		{
			int i = 0 ;
			for(RequestStatus parentRequestStatus : inviteList)
			{
				long uniqueIdLong = 111L +i;
				
				parentRequestStatus.setUniqueIdLong(uniqueIdLong);
				parentRequestStatus.setParent(true);
				parentRequestStatus.setParentInviteId(parentRequestStatus.getUniqueId());
				
				List<Players> nonInterestedPlayersList = new ArrayList<Players>();
				for(String playerId : parentRequestStatus.getPlayerStringList())
				{
					if(playerDetMap.get(playerId) != null)
					{
						nonInterestedPlayersList.add(playerDetMap.get(playerId));
					}
				}
				parentRequestStatus.setPlayersList(nonInterestedPlayersList);
				
				requestStatusMap.put(parentRequestStatus.getUniqueIdLong(), parentRequestStatus);
				
				int childCount = 0;
				if(parentRequestStatus.getOpponentInvites() != null && parentRequestStatus.getOpponentInvites().size() > 0)
				{
					for(RequestStatus opponentInvite : parentRequestStatus.getOpponentInvites())
					{
						if(inviteDetMap.get(opponentInvite.getUniqueId()) != null)
						{
							childCount ++;
							RequestStatus childRequestStatus = inviteDetMap.get(opponentInvite.getUniqueId()).clone();
							
							childRequestStatus.setUniqueIdLong(uniqueIdLong);
							childRequestStatus.setParent(false);
							childRequestStatus.setRequestSent(opponentInvite.isRequestSent());
							childRequestStatus.setParentInviteId(parentRequestStatus.getUniqueId());
							
							List<Players> interestedPlayersList = new ArrayList<Players>();
							for(String playerId : opponentInvite.getPlayerStringList())
							{
								if(playerDetMap.get(playerId) != null)
								{
									interestedPlayersList.add(playerDetMap.get(playerId));
								}
							}
							childRequestStatus.setPlayersList(interestedPlayersList);
							//other details already set at beginning of this screen data loading process
							
							requestStatusChildList.add(childRequestStatus);
						}
					}
				}
				
				if(childCount ==0){
					RequestStatus requestStatusChild = new RequestStatus();
					requestStatusChild.setTeamId(null);
					requestStatusChild.setUniqueId(TeamQuestConstants.emptyUniqueId_key+uniqueIdLong);
					requestStatusChild.setUniqueIdLong(uniqueIdLong);
					requestStatusChild.setParent(false);
					requestStatusChild.setParentInviteId(parentRequestStatus.getUniqueId());	
					List<Players> playersListChild = new ArrayList<Players>();
							
					requestStatusChild.setPlayersList(playersListChild);
					
					requestStatusChildList.add(requestStatusChild);
				}
				
				i++;
			}
		}
		}else{
			requestStatusMap = null;
		}
	}catch(Exception e)
	{
		requestStatusMap = null;
	}
		return requestStatusMap;
	}
	
	public JSONObject getInviteDetail(Team team)
	{
		JSONObject inviteDetDoc;
		try{
			
		RequestStatusDB requestStatusDb = new RequestStatusDB();
		inviteDetDoc = requestStatusDb.getRequestStatus(team.getTeamId());
		
		}
		catch(Exception e)
		{
			inviteDetDoc = null;
		}
		return inviteDetDoc;
	}
	
	
	public int savePlayerOpinion(RequestStatus status)
	{
		//ifchild inviteId is empty then change have been made on parent invite , so upadte for parent invite id
		
		//id child inviteid is not null then change made on child sp update on subdocument
		
		//if toremove is false then add the player id in list
		
		int result = -1;
		try{
			
		JSONObject playerOpinionDet = new JSONObject();
		
		playerOpinionDet.put(CommonDBConstants.inviteId, status.getUniqueId());
		playerOpinionDet.put(CommonDBConstants.parentInviteId, status.getParentInviteId());
		playerOpinionDet.put(CommonDBConstants.isRequestSent,status.isRequestSent());
		playerOpinionDet.put(CommonDBConstants.isParent, status.isParent());
		playerOpinionDet.put(CommonDBConstants.teamId, Integer.parseInt(status.getTeamId()));
		playerOpinionDet.put(CommonDBConstants.playerId, status.getPlayerId());
		playerOpinionDet.put(CommonDBConstants.toRemove, status.isToRemove());
		
		RequestStatusDB requestStatusDb = new RequestStatusDB();
		result = requestStatusDb.savePlayerOpinion(playerOpinionDet);
		
		}catch(Exception e)
		{
			result = -1;
		}
		
		return result;
		
	}

	
	public int acceptInvite(RequestStatus childStatus,RequestStatus parentStatus)
	{
//		use parent invite id , unique id ,isrequestsent from status 
//		to get their data from collection and use then to insert into matchdetail collection 
//		delete their invites 
		
//		by the time we update if someone else updates that invite then we give status as failue (-1)
//		so display messageing saying that invites no longer existes and reload the screen
		int result = -1;
		try{
		JSONObject inviteDet = new JSONObject();
		
		inviteDet.put(CommonDBConstants.inviteId, childStatus.getUniqueId());
		inviteDet.put(CommonDBConstants.parentInviteId, parentStatus.getUniqueId());

		JSONArray teamDetArr = new JSONArray();
		
		JSONObject teamDet = new JSONObject();
		teamDet.put(CommonDBConstants.teamCode, Integer.parseInt(childStatus.getTeamCode()));
		teamDet.put(CommonDBConstants.teamName, childStatus.getTeamName());
		teamDet.put(CommonDBConstants.teamId, Integer.parseInt(childStatus.getTeamId()));
		teamDet.put(CommonDBConstants.interestedPlayerIds, new JSONArray());
		teamDet.put(CommonDBConstants.selectedPlayerIds, new JSONArray());
		
		teamDetArr.put(teamDet);
		
		teamDet = new JSONObject();
		teamDet.put(CommonDBConstants.teamCode, Integer.parseInt(parentStatus.getTeamCode()));
		teamDet.put(CommonDBConstants.teamName, parentStatus.getTeamName());
		teamDet.put(CommonDBConstants.teamId, Integer.parseInt(parentStatus.getTeamId()));
		teamDet.put(CommonDBConstants.interestedPlayerIds, new JSONArray());
		teamDet.put(CommonDBConstants.selectedPlayerIds, new JSONArray());
		
		teamDetArr.put(teamDet);
		
		inviteDet.put(CommonDBConstants.teamDet, teamDetArr);
		inviteDet.put(CommonDBConstants.date, childStatus.getDate());
		
		Set<String> locationSet = new HashSet<String>();
		locationSet.addAll(parentStatus.getLocationList());
		locationSet.addAll(childStatus.getLocationList());
		
		JSONArray locationArr = new JSONArray();
		for(String location : locationSet)
		{
			locationArr.put(location);
		}
		
		inviteDet.put(CommonDBConstants.location, locationArr);
		inviteDet.put(CommonDBConstants.nop, childStatus.getNop());
		inviteDet.put(CommonDBConstants.time, childStatus.getTime());
		
		RequestStatusDB requestStatusDb = new RequestStatusDB();
		
		result = requestStatusDb.acceptInvite(inviteDet);
		
		}catch(Exception e)
		{
			result = -1;
		}
		return result;
	}
	
	public int rejectInvite(RequestStatus status)
	{
//		use the unique id, isrequestsent to remove this uniqueid from list 
//		if it is already removed then no problem anyway
//		so mostly we wont get any failure in this step
		
		return TeamQuestConstants.success_key;
	}
	
	public int closeInvite(RequestStatus status) 
	{
//		use the uniqueid to remove the invite itself and remove this invite id in other invite too
		
//		if this invite id is not present that means someone else has already accpeted the invite 
//		so dispaly message saying a match has already been setup for this match
		int result = -1;
		try{
			JSONObject inviteDet = new JSONObject();
			inviteDet.put(CommonDBConstants.inviteId, status.getUniqueId());
			
			RequestStatusDB requestStatusDb = new RequestStatusDB();
			result = requestStatusDb.closeInvite(inviteDet);
			
		}catch(Exception e)
		{
			result = -1;
		}
		return result ;
	}

	public int cancelRequest(RequestStatus status,Team team)
	{
//		use uniqueid, parentuniqueid and isrequestsent for removing the request
//		remove parent uniqueid from list of invite with uniqueid
		int result = -1;
		try{
			
			JSONObject inviteDet = new JSONObject();
			inviteDet.put(CommonDBConstants.inviteId, status.getUniqueId());
			inviteDet.put(CommonDBConstants.teamId, Integer.parseInt(status.getTeamId()));
			inviteDet.put(CommonDBConstants.parentInviteId, status.getParentInviteId());
			inviteDet.put(CommonDBConstants.parentTeamId, Integer.parseInt(team.getTeamId()));
			
			RequestStatusDB requestStatusDb = new RequestStatusDB();
			result = requestStatusDb.cancelInvite(inviteDet);
		}catch(Exception e)
		{
			result = -1;
		}
		return result;
	}
}
