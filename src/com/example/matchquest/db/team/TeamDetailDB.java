package com.example.matchquest.db.team;

import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.matchquest.db.CommonDBClass;
import com.example.matchquest.db.CommonDBConstants;
import com.example.matchquest.model.Team;

public class TeamDetailDB {

	public JSONArray getTeamDetail(List<Integer> topicIds,String uniqueId)
	{
		
		JSONArray teamDetArray = null ;
		String urlPattern = "/topicDetail/getTopicDetail";
		HttpURLConnection conn = null;
		try{
			
			JSONObject teamJson = new JSONObject();
			
			teamJson.put(CommonDBConstants.uniqueId, Integer.parseInt(uniqueId));
			
			JSONArray topicIdsArr = new JSONArray();
			
			for(int i = 0 ; i  < topicIds.size() ; i ++)
			{
				topicIdsArr.put(topicIds.get(i));
			}
			teamJson.put(CommonDBConstants.topicIds, topicIdsArr);
			
			conn = CommonDBClass.getDBConnection(urlPattern, teamJson, null, CommonDBConstants.postMethod_key);
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
	
	public JSONArray saveTopicDetail(JSONObject topicDet)
	{
		JSONArray topicDetArray = null ;
		String urlPattern = "/topicDetail/saveTopicDetail";
		HttpURLConnection conn = null;
		try{
			conn = CommonDBClass.getDBConnection(urlPattern, topicDet, null, CommonDBConstants.postMethod_key);
			int responseCode = conn.getResponseCode();
			if(responseCode == HttpStatus.SC_OK){
				String responseString = CommonDBClass.readStream(conn.getInputStream());
				
				JSONObject responseJSON = new JSONObject(responseString);
				if(responseJSON.getString(CommonDBConstants.statusMsg_key) != null && responseJSON.getString(CommonDBConstants.statusMsg_key).equals(CommonDBConstants.success_key))
				{
					topicDetArray = responseJSON.getJSONArray("topicDetail");
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
		
		return topicDetArray;
	
	}
	
	public int addTopicDetail(JSONObject addTopicDetDocs)
	{
		int result = -1;
		
		String urlPattern = "/topicDetail/addTopicDetail";
		HttpURLConnection conn = null;
		try{
			conn = CommonDBClass.getDBConnection(urlPattern, addTopicDetDocs, null, CommonDBConstants.postMethod_key);
			int responseCode = conn.getResponseCode();
			if(responseCode == HttpStatus.SC_OK){
				String responseString = CommonDBClass.readStream(conn.getInputStream());
				
				JSONObject responseJSON = new JSONObject(responseString);
				if(responseJSON.getString(CommonDBConstants.statusMsg_key) != null && responseJSON.getString(CommonDBConstants.statusMsg_key).equals(CommonDBConstants.success_key))
				{
					result = responseJSON.getInt(CommonDBConstants.topicId);
				}
			}
		}catch(Exception e )
		{
			result = -1;
		}finally{
			if(conn != null)
			conn.disconnect();
		}
		return result;
	}
	
	public JSONArray updateTopicDetail(JSONObject updatedTopicDetDocs)
	{
		JSONArray topicDetArray = null ;
		
		String urlPattern = "/topicDetail/updateTopicDetail";
		HttpURLConnection conn = null;
		try{
			conn = CommonDBClass.getDBConnection(urlPattern, updatedTopicDetDocs, null, CommonDBConstants.postMethod_key);
			int responseCode = conn.getResponseCode();
			if(responseCode == HttpStatus.SC_OK){
				String responseString = CommonDBClass.readStream(conn.getInputStream());
				
				JSONObject responseJSON = new JSONObject(responseString);
				if(responseJSON.getString(CommonDBConstants.statusMsg_key) != null && responseJSON.getString(CommonDBConstants.statusMsg_key).equals(CommonDBConstants.success_key))
				{
					topicDetArray = responseJSON.getJSONArray("topicDetail");
				}
			}
		}catch(Exception e )
		{
			topicDetArray = null;
		}finally{
			if(conn != null)
			conn.disconnect();
		}
		return topicDetArray;
	}
	
	public int addTopicComments(JSONObject addTopicCommentDocs)
	{
		int result = -1;
		
		String urlPattern = "/topicDetail/addTopicComment";
		HttpURLConnection conn = null;
		try{
			conn = CommonDBClass.getDBConnection(urlPattern, addTopicCommentDocs, null, CommonDBConstants.postMethod_key);
			int responseCode = conn.getResponseCode();
			if(responseCode == HttpStatus.SC_OK){
				String responseString = CommonDBClass.readStream(conn.getInputStream());
				
				JSONObject responseJSON = new JSONObject(responseString);
				if(responseJSON.getString(CommonDBConstants.statusMsg_key) != null && responseJSON.getString(CommonDBConstants.statusMsg_key).equals(CommonDBConstants.success_key))
				{
					result = 1;
				}
			}
		}catch(Exception e )
		{
			result = -1;
		}finally{
			if(conn != null)
			conn.disconnect();
		}
		return result;
	}
	
	public JSONArray getTopicComments(JSONObject commentQuery)
	{
		//if null at end then some error has happened, it will be empty if no data([])
		JSONArray topicCommentArray = null ;
		String urlPattern = "/topicDetail/getTopicComments";
		HttpURLConnection conn = null;
		
		try{
			conn = CommonDBClass.getDBConnection(urlPattern, commentQuery, null, CommonDBConstants.postMethod_key);
		int responseCode = conn.getResponseCode();
		if(responseCode == HttpStatus.SC_OK){
			String responseString = CommonDBClass.readStream(conn.getInputStream());
			
			JSONObject responseJSON = new JSONObject(responseString);
			if(responseJSON.getString(CommonDBConstants.statusMsg_key) != null && responseJSON.getString(CommonDBConstants.statusMsg_key).equals(CommonDBConstants.success_key))
			{
				topicCommentArray = responseJSON.getJSONArray("topicComment");
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
		return topicCommentArray;
	}
}
