package com.example.matchquest.db.MatchStatus;

import java.net.HttpURLConnection;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

import com.example.matchquest.db.CommonDBClass;
import com.example.matchquest.db.CommonDBConstants;
import com.example.matchquest.model.RequestStatus;

public class MatchStatusDB {

	String baseUrl = "/matchStatus";
	
	public JSONObject getMatchSchedule(String teamId)
	{

		JSONObject matchStatusDet = null;
		
		String urlPattern = baseUrl + "/getMatchStatus/" + Integer.parseInt(teamId);
		
		HttpURLConnection conn = null;
		
		try{
		conn = CommonDBClass.getDBConnection(urlPattern, null, null, CommonDBConstants.getMethod_key);
		int responseCode = conn.getResponseCode();
		if(responseCode == HttpStatus.SC_OK){
			String responseString = CommonDBClass.readStream(conn.getInputStream());
			
			JSONObject responseJSON = new JSONObject(responseString);
			if(responseJSON.getString(CommonDBConstants.statusMsg_key) != null && responseJSON.getString(CommonDBConstants.statusMsg_key).equals(CommonDBConstants.success_key))
			{
				matchStatusDet = responseJSON.getJSONObject("matchStatusDet");
			}
		}
		}catch(Exception e)
		{
			matchStatusDet = null;
		}
		return matchStatusDet;
	
	}
	
	public int savePlayerOpinion(JSONObject playerOptionDet)
	{
		int result = -1;
		try{
			String urlPattern = baseUrl + "/savePlayerOpinion";
			HttpURLConnection conn = null;
			
			conn = CommonDBClass.getDBConnection(urlPattern, playerOptionDet, null, CommonDBConstants.postMethod_key);
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
		}
		
		
		return result;
	}
	
	public int saveSelectedPlayers(JSONObject saveSelectedPlayerDet)
	{

		int result = -1;
		try{
			String urlPattern = baseUrl + "/saveSelectedPlayers";
			HttpURLConnection conn = null;
			
			conn = CommonDBClass.getDBConnection(urlPattern, saveSelectedPlayerDet, null, CommonDBConstants.postMethod_key);
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
		}
		
		
		return result;
	
	}
	
	public JSONObject getMatchStatusByPlayer(String playerId)
	{

		JSONObject matchStatusDet = null;
		
		String urlPattern = baseUrl + "/getMatchStatusByPlayer/" + playerId;
		
		HttpURLConnection conn = null;
		
		try{
		conn = CommonDBClass.getDBConnection(urlPattern, null, null, CommonDBConstants.getMethod_key);
		int responseCode = conn.getResponseCode();
		if(responseCode == HttpStatus.SC_OK){
			String responseString = CommonDBClass.readStream(conn.getInputStream());
			
			JSONObject responseJSON = new JSONObject(responseString);
			if(responseJSON.getString(CommonDBConstants.statusMsg_key) != null && responseJSON.getString(CommonDBConstants.statusMsg_key).equals(CommonDBConstants.success_key))
			{
				matchStatusDet = responseJSON.getJSONObject("matchStatusDet");
			}
		}
		}catch(Exception e)
		{
			matchStatusDet = null;
		}
		return matchStatusDet;
	
	}
}
