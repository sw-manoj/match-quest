package com.example.matchquest.db.FIndOpponent;

import java.net.HttpURLConnection;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.matchquest.db.CommonDBClass;
import com.example.matchquest.db.CommonDBConstants;

public class FindOpponentDB {
	
	String baseUrl = "/findOpponent";
	
	public int postInvite(JSONObject inviteDetArr)
	{
		int result = -1;
		
		String urlPattern = baseUrl + "/postInvite";
		
		HttpURLConnection conn = null;
		
		try{
		conn = CommonDBClass.getDBConnection(urlPattern, inviteDetArr, null, CommonDBConstants.postMethod_key);
		int responseCode = conn.getResponseCode();
		if(responseCode == HttpStatus.SC_OK){
			String responseString = CommonDBClass.readStream(conn.getInputStream());
			
			JSONObject responseJSON = new JSONObject(responseString);
			if(responseJSON.getString(CommonDBConstants.statusMsg_key) != null && responseJSON.getString(CommonDBConstants.statusMsg_key).equals(CommonDBConstants.success_key))
			{
				result = 1;
			}
		}
		}
		catch(Exception e)
		{
			return -1;
			
		}finally{
			if(conn != null)
				conn.disconnect();
		}
		return result;
		
	}
	
	public int saveInvite(JSONObject saveInviteDoc)
	{
		int result = -1;
		
		String urlPattern = baseUrl + "/saveInvite";
		
		HttpURLConnection conn = null;
		
		try{
		conn = CommonDBClass.getDBConnection(urlPattern, saveInviteDoc, null, CommonDBConstants.postMethod_key);
		int responseCode = conn.getResponseCode();
		if(responseCode == HttpStatus.SC_OK){
			String responseString = CommonDBClass.readStream(conn.getInputStream());
			
			JSONObject responseJSON = new JSONObject(responseString);
			if(responseJSON.getString(CommonDBConstants.statusMsg_key) != null && responseJSON.getString(CommonDBConstants.statusMsg_key).equals(CommonDBConstants.success_key))
			{
				result = 1;
			}
		}
		}
		catch(Exception e)
		{
			return -1;
			
		}finally{
			if(conn != null)
				conn.disconnect();
		}
		return result;
		
	}
	
	public JSONArray searchInvites(JSONObject searchQuery)
	{
		JSONArray inviteDetArr = null;
		
		String urlPattern = baseUrl + "/searchInvite";
		
		HttpURLConnection conn = null;
		
		try{
		conn = CommonDBClass.getDBConnection(urlPattern, searchQuery, null, CommonDBConstants.postMethod_key);
		int responseCode = conn.getResponseCode();
		if(responseCode == HttpStatus.SC_OK){
			String responseString = CommonDBClass.readStream(conn.getInputStream());
			
			JSONObject responseJSON = new JSONObject(responseString);
			if(responseJSON.getString(CommonDBConstants.statusMsg_key) != null && responseJSON.getString(CommonDBConstants.statusMsg_key).equals(CommonDBConstants.success_key))
			{
				inviteDetArr = responseJSON.getJSONArray("inviteDetArr");
			}
		}
		}catch(Exception e)
		{
			inviteDetArr = null;
		}
		return inviteDetArr;
	}

}
