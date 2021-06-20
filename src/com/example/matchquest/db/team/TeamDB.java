package com.example.matchquest.db.team;

import java.net.HttpURLConnection;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.matchquest.db.CommonDBClass;
import com.example.matchquest.db.CommonDBConstants;
import com.example.matchquest.model.Players;
import com.example.matchquest.model.Team;

public class TeamDB {
	
	public int createTeam(Team team)
	{
		int status = -1;
		String urlPattern = "/team/createTeam";
		HttpURLConnection conn = null;
		try {
			JSONObject teamJson = new JSONObject();
			
			teamJson.put(CommonDBConstants.teamName, team.getTeamName());
			teamJson.put(CommonDBConstants.viceCaptain, team.getViceCaptain());
			teamJson.put(CommonDBConstants.captain, team.getCaptain());
			JSONArray playerArray = new JSONArray();
			for(Players player : team.getPlayersList())
			{
				JSONObject playerJson = new JSONObject();
				playerJson.put(CommonDBConstants.playerId, player.getPlayerId());
				playerJson.put(CommonDBConstants.playerName, player.getPlayerName());

				playerArray.put(playerJson);
			}
			teamJson.put(CommonDBConstants.playerIds, playerArray);
			
			conn = CommonDBClass.getDBConnection(urlPattern, teamJson, null, CommonDBConstants.postMethod_key);
			int responseCode = conn.getResponseCode();
			if(responseCode == HttpStatus.SC_OK){
				String responseString = CommonDBClass.readStream(conn.getInputStream());
				
				JSONObject responseJSON = new JSONObject(responseString);
				if(responseJSON.getString(CommonDBConstants.statusMsg_key) != null && responseJSON.getString(CommonDBConstants.statusMsg_key).equals(CommonDBConstants.success_key))
				{
					status = 1;
				}
			}
			
		}
		catch(Exception e)
		{
			status = -1;
		}finally{
			if(conn != null)
				conn.disconnect();
		}
		return status;
	}
	
	public JSONArray getTeamData(String playerId)
	{
		//if null at end then some error has happened, it will be empty if no data([])
		JSONArray teamDetArray = null ;
		String urlPattern = "/team/getTeamDetail/" + playerId;
		HttpURLConnection conn = null;
		
		try{
		conn = CommonDBClass.getDBConnection(urlPattern, null, null, CommonDBConstants.getMethod_key);
		int responseCode = conn.getResponseCode();
		if(responseCode == HttpStatus.SC_OK){
			String responseString = CommonDBClass.readStream(conn.getInputStream());
			
			JSONObject responseJSON = new JSONObject(responseString);
			if(responseJSON.getString(CommonDBConstants.statusMsg_key) != null && responseJSON.getString(CommonDBConstants.statusMsg_key).equals(CommonDBConstants.success_key))
			{
				teamDetArray = responseJSON.getJSONArray("teamDetail");
			}
		}
		}
		catch(Exception e)
		{
			return null;
			
		}finally{
			if(conn != null)
				conn.disconnect();
		}
		return teamDetArray;
	}
	
	public int changeTeamName(Team team)
	{
		
		int result = 0;
		JSONObject teamNewName = null ;
		String urlPattern = "/team/changeTeamName/";
		HttpURLConnection conn = null;
		
		try{
			
		if(team != null)
		{
			teamNewName = new JSONObject();
			teamNewName.put(CommonDBConstants.teamName, team.getTeamName());
			teamNewName.put(CommonDBConstants.teamId, Integer.parseInt(team.getTeamId()));
		}
			
		conn = CommonDBClass.getDBConnection(urlPattern, teamNewName, null, CommonDBConstants.postMethod_key);
		int responseCode = conn.getResponseCode();
		if(responseCode == HttpStatus.SC_OK){
			String responseString = CommonDBClass.readStream(conn.getInputStream());
			
			JSONObject responseJSON = new JSONObject(responseString);
			if(responseJSON.getString(CommonDBConstants.statusMsg_key) != null && responseJSON.getString(CommonDBConstants.statusMsg_key).equals(CommonDBConstants.success_key))
			{
				result = 1;
				
			}
		}
		}catch(Exception e)
		{
			result = -1;
		}finally{
			if(conn != null)
				conn.disconnect();
		}
		return result;
	}
	
	public JSONArray addTeamMember(Team team)
	{
		JSONArray playerDetArr = null;
		int result = 0;
		JSONObject teamNewMembers = null ;
		String urlPattern = "/team/addTeamMembers/";
		HttpURLConnection conn = null;
		try{
			
			if(team != null)
			{
				teamNewMembers = new JSONObject();
				teamNewMembers.put(CommonDBConstants.teamId, Integer.parseInt(team.getTeamId()));
				JSONArray playerArray = new JSONArray();
				for(Players player : team.getPlayersList())
				{
					JSONObject playerJson = new JSONObject();
					playerJson.put(CommonDBConstants.playerId, player.getPlayerId());
					playerJson.put(CommonDBConstants.playerName, player.getPlayerName());

					playerArray.put(playerJson);
				}
				teamNewMembers.put(CommonDBConstants.playerIds, playerArray);
			}
				
			conn = CommonDBClass.getDBConnection(urlPattern, teamNewMembers, null, CommonDBConstants.postMethod_key);
			int responseCode = conn.getResponseCode();
			if(responseCode == HttpStatus.SC_OK){
				String responseString = CommonDBClass.readStream(conn.getInputStream());
				
				JSONObject responseJSON = new JSONObject(responseString);
				if(responseJSON.getString(CommonDBConstants.statusMsg_key) != null && responseJSON.getString(CommonDBConstants.statusMsg_key).equals(CommonDBConstants.success_key))
				{
					playerDetArr = responseJSON.getJSONArray("playerDetail");
				}
			}
			}catch(Exception e)
			{
				playerDetArr = null;
			}finally{
				if(conn != null)
					conn.disconnect();
			}
		return playerDetArr;
	}
	
	public Team removeFromTeam(Team team, String playerId)
	{
		int result = 0;
		Team newTeam = null;
		JSONObject detail = null ;
		String urlPattern = "/team/removeFromTeam/";
		HttpURLConnection conn = null;
		
		try {
			detail = new JSONObject();
		
			detail.put(CommonDBConstants.teamId, Integer.parseInt(team.getTeamId()));
			detail.put(CommonDBConstants.playerId, playerId);
			if(team.getCaptain().equals(playerId))
			{
				detail.put("toUpdate", CommonDBConstants.captain);
				detail.put("newPlayerId", team.getViceCaptain());
			}else if(team.getViceCaptain().equals(playerId))
			{
				detail.put("toUpdate", CommonDBConstants.viceCaptain);
				detail.put("newPlayerId", team.getCaptain());
				
			}else{
				detail.put("toUpdate", null);
				detail.put("newPlayerId", null);
			}
			conn = CommonDBClass.getDBConnection(urlPattern, detail, null, CommonDBConstants.postMethod_key);
			
			int responseCode = conn.getResponseCode();
			if(responseCode == HttpStatus.SC_OK){
				String responseString = CommonDBClass.readStream(conn.getInputStream());
				
				JSONObject responseJSON = new JSONObject(responseString);
				if(responseJSON.getString(CommonDBConstants.statusMsg_key) != null && responseJSON.getString(CommonDBConstants.statusMsg_key).equals(CommonDBConstants.success_key))
				{
					newTeam = team;
					JSONObject teamDet = responseJSON.getJSONObject("teamDetail");
					
					String captain = teamDet.getString(CommonDBConstants.captain);
					String viceCaptain = teamDet.getString(CommonDBConstants.viceCaptain);
					if(captain != null)
					{
						newTeam.setCaptain(captain);
					}
					if(viceCaptain != null)
					{
						newTeam.setViceCaptain(viceCaptain);
					}
					
				}
			}
			
		}  catch (Exception e) {
			newTeam = null;
		}finally{
			if(conn != null)
				conn.disconnect();
		}
		
		return newTeam;
	}
	
	public int changeLeader(Team team,String playerId ,boolean toModifyCaptain )
	{
		int result = 0;
		JSONObject detail = null ;
		String urlPattern = null;
		
		if(toModifyCaptain)
		{
			urlPattern = "/team/changeCaptain";	
		}else{
			urlPattern = "/team/changeViceCaptain";
		}
		
		HttpURLConnection conn = null;
		
		try {
			detail = new JSONObject();
		
			detail.put(CommonDBConstants.teamId, Integer.parseInt(team.getTeamId()));
			detail.put("newPlayerId", playerId);
			if(toModifyCaptain)
			{
				if(team.getViceCaptain().equals(playerId))
				{
					detail.put("changeViceCaptain", true);
				}
			}
			
			conn = CommonDBClass.getDBConnection(urlPattern, detail, null, CommonDBConstants.postMethod_key);
			
			int responseCode = conn.getResponseCode();
			if(responseCode == HttpStatus.SC_OK){
				String responseString = CommonDBClass.readStream(conn.getInputStream());
				
				JSONObject responseJSON = new JSONObject(responseString);
				if(responseJSON.getString(CommonDBConstants.statusMsg_key) != null && responseJSON.getString(CommonDBConstants.statusMsg_key).equals(CommonDBConstants.success_key))
				{
					result = 1;
					if(responseJSON.has("viceCaptain") )
					{
						team.setViceCaptain(responseJSON.getString("viceCaptain"));
					}
				}
			}
			
		}  catch (Exception e) {
			result = -1;
		}finally{
			if(conn != null)
				conn.disconnect();
		}
		return result;
	}
}
