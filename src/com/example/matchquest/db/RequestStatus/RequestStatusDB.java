package com.example.matchquest.db.RequestStatus;

import java.net.HttpURLConnection;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

import com.example.matchquest.db.CommonDBClass;
import com.example.matchquest.db.CommonDBConstants;

public class RequestStatusDB {

	String baseUrl = "/requestStatus";
	
	public JSONObject getRequestStatus(String teamId)
	{
		JSONObject inviteDetDoc = null;
		
		String urlPattern = baseUrl + "/getRequestStatus/" + Integer.parseInt(teamId);
		
		HttpURLConnection conn = null;
		
		try{
		conn = CommonDBClass.getDBConnection(urlPattern, null, null, CommonDBConstants.getMethod_key);
		int responseCode = conn.getResponseCode();
		if(responseCode == HttpStatus.SC_OK){
			String responseString = CommonDBClass.readStream(conn.getInputStream());
			
			JSONObject responseJSON = new JSONObject(responseString);
			if(responseJSON.getString(CommonDBConstants.statusMsg_key) != null && responseJSON.getString(CommonDBConstants.statusMsg_key).equals(CommonDBConstants.success_key))
			{
				inviteDetDoc = responseJSON.getJSONObject("inviteDetDoc");
			}
		}
		}catch(Exception e)
		{
			inviteDetDoc = null;
		}
		return inviteDetDoc;
	}
	
	public int savePlayerOpinion(JSONObject playerOpinionDet)
	{
		int result = -1;
		try{
			String urlPattern = baseUrl + "/savePlayerOpinion";
			HttpURLConnection conn = null;
			
			conn = CommonDBClass.getDBConnection(urlPattern, playerOpinionDet, null, CommonDBConstants.postMethod_key);
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
	
	public int cancelInvite(JSONObject inviteDet)
	{
		int result = -1;
		try{
			String urlPattern = baseUrl + "/cancelInvite";
			HttpURLConnection conn = null;
			
			conn = CommonDBClass.getDBConnection(urlPattern, inviteDet, null, CommonDBConstants.postMethod_key);
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
	
	
	public int closeInvite(JSONObject inviteDet)
	{
		int result = -1;
		try{
			String urlPattern = baseUrl + "/closeInvite";
			HttpURLConnection conn = null;
			
			conn = CommonDBClass.getDBConnection(urlPattern, inviteDet, null, CommonDBConstants.postMethod_key);
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
	
	public int acceptInvite(JSONObject inviteDet)
	{
		int result = -1;
		try{
			String urlPattern = baseUrl + "/acceptInvite";
			HttpURLConnection conn = null;
			
			conn = CommonDBClass.getDBConnection(urlPattern, inviteDet, null, CommonDBConstants.postMethod_key);
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
}
