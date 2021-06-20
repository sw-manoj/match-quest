package com.example.matchquest.DataManipulation.FindOpponent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.matchquest.db.CommonDBConstants;
import com.example.matchquest.db.FIndOpponent.FindOpponentDB;
import com.example.matchquest.model.Players;
import com.example.matchquest.model.RequestStatus;
import com.example.matchquest.model.Team;

public class FindOpponentDM {
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
	

	public List<RequestStatus> searchOpponent(List<String> locationlist,Date date, String nop,Team team)
	{
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		List<RequestStatus> requestStatusList = new ArrayList<RequestStatus>();
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		if(Integer.parseInt(nop) == 10)
//		{
//			return requestStatusList;
//		}
//		for(int i = 0 ; i < Integer.parseInt(nop) ; i++)
//		{
//			cal.add(Calendar.DAY_OF_MONTH, i);
//			RequestStatus requestStatus = new RequestStatus();
//			requestStatus.setTeamId("opp id " + i);
//			requestStatus.setTeamName("opp name " + i);
//			requestStatus.setTeamCode("opp code " + i);
//			requestStatus.setDate(cal.getTime());
//			requestStatus.setLocation("loc opp "+i);
//			requestStatus.setTime("10 :00 AM");
//			requestStatus.setNop(i %2 == 0 ? nop : Integer.parseInt(nop)-1 + "");
//			requestStatus.setUniqueId("inviteId " + i);
//			
//			requestStatusList.add(requestStatus);
//		}
		try{
			
			JSONObject searchQuery = new JSONObject();
			searchQuery.put(CommonDBConstants.teamId, Integer.parseInt(team.getTeamId()));
			searchQuery.put(CommonDBConstants.date, date);
			searchQuery.put(CommonDBConstants.nop, nop);
			JSONArray locationArr = new JSONArray();
			for(String location : locationlist)
			{
				locationArr.put(location);
			}
			
			searchQuery.put(CommonDBConstants.location, locationArr);
			
			FindOpponentDB findOpponentDb = new FindOpponentDB();
			JSONArray inviteDetArr = findOpponentDb.searchInvites(searchQuery);
			
			requestStatusList = formRequestStatusList(inviteDetArr);
			
		}catch(Exception e)
		{
			requestStatusList = null;
		}
		return requestStatusList;
	}
	
	public List<RequestStatus> formRequestStatusList(JSONArray inviteDetArr)
	{
		List<RequestStatus> requestStatusList = new ArrayList<RequestStatus>();
		
		try{
			
		
		for(int i = 0 ; i < inviteDetArr.length() ; i ++)
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
			requestStatus.setDate(dateFormat.parse(inviteDet.getString(CommonDBConstants.date)));
			
			requestStatus.setNop(inviteDet.getString(CommonDBConstants.nop));
			requestStatus.setUniqueId(inviteDet.getString(CommonDBConstants.inviteId));
			
			JSONArray nonInterestedPLayerDetArr = inviteDet.getJSONArray(CommonDBConstants.nonInterestedPlayerIds);
			formPlayerDet(requestStatus, nonInterestedPLayerDetArr);
			
			JSONArray requestDetArr = inviteDet.getJSONArray(CommonDBConstants.request);
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
	public int saveInvite(RequestStatus requestStatus,RequestStatus opponentStatus)
	{
//		if(!requestStatus.isInviteSaved())
//		{
////			insert this searchStatus only inside condition 
//		}
////		save opponent status and update each other invite id some attribute
		
		int result = -1;
		try{
			JSONObject requestInviteDet = new JSONObject();
			
			if(requestStatus.isInviteSaved())
			{
				requestInviteDet.put(CommonDBConstants.inviteId, requestStatus.getUniqueId());
				requestInviteDet.put(CommonDBConstants.isInviteSaved, requestStatus.isInviteSaved());
				requestInviteDet.put(CommonDBConstants.canGetRequest, false);
				
				JSONObject requestDet = new JSONObject();
				requestDet.put(CommonDBConstants.inviteId, opponentStatus.getUniqueId());
				requestDet.put(CommonDBConstants.isRequestSent, true);
				requestDet.put(CommonDBConstants.playerIds, new JSONArray());
				
				requestInviteDet.put(CommonDBConstants.request, requestDet);
			}else{
				
				requestInviteDet.put(CommonDBConstants.inviteId, requestStatus.getUniqueId());
				requestInviteDet.put(CommonDBConstants.isInviteSaved, requestStatus.isInviteSaved());
				requestInviteDet.put(CommonDBConstants.canGetRequest, false);
				requestInviteDet.put(CommonDBConstants.postedBy, requestStatus.getPostedBy());
					
					JSONArray requestArr = new JSONArray();
					JSONObject requestDet = new JSONObject();
					requestDet.put(CommonDBConstants.inviteId, opponentStatus.getUniqueId());
					requestDet.put(CommonDBConstants.isRequestSent, true);
					requestDet.put(CommonDBConstants.playerIds, new JSONArray());
					
					requestArr.put(requestDet);
					
				requestInviteDet.put(CommonDBConstants.request, requestArr);
				JSONArray locationArr = new JSONArray();
				for(String location : requestStatus.getLocationList())
				{
					locationArr.put(location);
				}
					requestInviteDet.put(CommonDBConstants.location, locationArr);
					requestInviteDet.put(CommonDBConstants.nop, Integer.parseInt(requestStatus.getNop()));
					requestInviteDet.put(CommonDBConstants.date, requestStatus.getDate());
					requestInviteDet.put(CommonDBConstants.time, requestStatus.getTime());
					requestInviteDet.put(CommonDBConstants.nonInterestedPlayerIds, new JSONArray());
					requestInviteDet.put(CommonDBConstants.teamName, requestStatus.getTeamName());
					requestInviteDet.put(CommonDBConstants.teamCode, Integer.parseInt(requestStatus.getTeamCode()));
			}
			
			requestInviteDet.put(CommonDBConstants.teamId, Integer.parseInt(requestStatus.getTeamId()));
			
			JSONObject opponentInviteDet = new JSONObject();
			
			opponentInviteDet.put(CommonDBConstants.teamId, Integer.parseInt(opponentStatus.getTeamId()));
			opponentInviteDet.put(CommonDBConstants.inviteId, opponentStatus.getUniqueId());
			
			JSONObject opponentRequestDet = new JSONObject();
			opponentRequestDet.put(CommonDBConstants.inviteId, requestStatus.getUniqueId());
			opponentRequestDet.put(CommonDBConstants.isRequestSent, false);
			opponentRequestDet.put(CommonDBConstants.playerIds, new JSONArray());
			
			opponentInviteDet.put(CommonDBConstants.request, opponentRequestDet);
			
			JSONObject saveInviteDoc = new JSONObject();
			saveInviteDoc.put("opponentDet", opponentInviteDet);
			saveInviteDoc.put("requestDet", requestInviteDet);
			
			FindOpponentDB findOpponentDb = new FindOpponentDB();
			result = findOpponentDb.saveInvite(saveInviteDoc);
		}catch(Exception e)
		{
			result = -1;
		}
		return result;
	}
	
	public int postInvite(RequestStatus requestStatus)
	{
//		if(statusList.get(statusList.size()-1).isInviteSaved())
//		{
////			save in DB 
////			which means no invite has been made for this search so post invite so that other can invite 
//		}
//		
//	
//		
//	for(int i = 0 ; i < statusList.size()-1 ;i ++)
//	{
//		if(statusList.get(i).isInviteSaved())
//		{
////			just update a attribute which denotes that this invite should not get any more request from other teams
//		}
//	
//	}
	int result = 1; 
	try{
	
		JSONObject inviteDet = new JSONObject();
		
		if(requestStatus.isInviteSaved())
		{
			inviteDet.put(CommonDBConstants.inviteId, requestStatus.getUniqueId());
			inviteDet.put(CommonDBConstants.isInviteSaved, requestStatus.isInviteSaved());
			inviteDet.put(CommonDBConstants.canGetRequest, true);
		}else{
			
				inviteDet.put(CommonDBConstants.inviteId, requestStatus.getUniqueId());
				inviteDet.put(CommonDBConstants.isInviteSaved, requestStatus.isInviteSaved());
				inviteDet.put(CommonDBConstants.canGetRequest, true);
				inviteDet.put(CommonDBConstants.postedBy, requestStatus.getPostedBy());
				
				inviteDet.put(CommonDBConstants.request, new JSONArray());
				JSONArray locationArr = new JSONArray();
				for(String location : requestStatus.getLocationList())
				{
					locationArr.put(location);
				}
				inviteDet.put(CommonDBConstants.location, locationArr);
				inviteDet.put(CommonDBConstants.nop, Integer.parseInt(requestStatus.getNop()));
				inviteDet.put(CommonDBConstants.date, requestStatus.getDate());
				inviteDet.put(CommonDBConstants.time, requestStatus.getTime());
				inviteDet.put(CommonDBConstants.nonInterestedPlayerIds, new JSONArray());
			
				inviteDet.put(CommonDBConstants.teamName, requestStatus.getTeamName());
				inviteDet.put(CommonDBConstants.teamCode, Integer.parseInt(requestStatus.getTeamCode()));
		}
		
		inviteDet.put(CommonDBConstants.teamId, Integer.parseInt(requestStatus.getTeamId()));
	
	
		FindOpponentDB findOpponentDB = new FindOpponentDB();
		result = findOpponentDB.postInvite(inviteDet);
	
	}
	catch(Exception e)
	{
		return -1;
	}
		return result;
	}
	
	public int updateStatus(List<RequestStatus> statusList)
	{
		for(int i = 0 ; i < statusList.size()-1 ;i ++)
		{
			if(statusList.get(i).isInviteSaved())
			{
//				just update a attribute which denotes that this invite should not get any more request from other teams
			}
		
		}
		return 1;
	}
}
