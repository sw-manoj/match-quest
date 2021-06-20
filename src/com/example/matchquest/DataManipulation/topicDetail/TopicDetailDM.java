package com.example.matchquest.DataManipulation.topicDetail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.db.CommonDBConstants;
import com.example.matchquest.db.team.TeamDetailDB;
import com.example.matchquest.model.Players;
import com.example.matchquest.model.Team;
import com.example.matchquest.model.TopicDetails;

public class TopicDetailDM {

	DateFormat dateFormat = new SimpleDateFormat("E, MMM dd yyyy");
	
	public List<TopicDetails> getTopicDetailTeam1(String uniqueId )
	{
		List<TopicDetails> topicDetailList = new ArrayList<TopicDetails>();
		for(int i = 1 ;i < 5 ; i++)
		{
			TopicDetails topicDetail = new TopicDetails();
			topicDetail.setUniqueId(uniqueId);
			topicDetail.setTopicId("topic id"  + i);
			topicDetail.setTopic("topic " + i);
			topicDetail.setOption("option " + i);
			topicDetail.setCategory(i%2 ==0 ? TeamQuestConstants.general_key : TeamQuestConstants.nextgame_key);
			Map<String,List<Players>> optionDetail = new LinkedHashMap<String, List<Players>>();
			Map<String,String> optionIds = new LinkedHashMap<String, String>();
			if(i !=1)
			{
			for(int j = 0 ;j < i ; j++)
			{
				Players p1 = new Players();
				p1.setPlayerId("955159116" + j);
				Players p2 = new Players();
				p2.setPlayerId("955159116" + j+1);
				optionIds.put("option id" +j+""+i, "option " +j);
				optionDetail.put("option id" +j+""+i, new ArrayList<Players>(Arrays.asList(p1,p2)));
				
			}
			}
			topicDetail.setOptionIds(optionIds);
			topicDetail.setOptions(optionDetail);
			topicDetail.setCreatedBy("created by" + i);
			topicDetailList.add(topicDetail);
		}
		return topicDetailList;
	}
	
	public List<TopicDetails> getTopicDetail(List<Integer> topicIds , String uniqueId )
	{
		
		TeamDetailDB teamDetailDb = new TeamDetailDB();
		JSONArray teamDetArr = teamDetailDb.getTeamDetail(topicIds,uniqueId);
		
		List<TopicDetails> topicDetailList = formTopicDetails(teamDetArr);
			
		return topicDetailList;
	}
	
	public List<TopicDetails> getTopicDetailTeam(Team team )
	{
		
		TeamDetailDB teamDetailDb = new TeamDetailDB();
		JSONArray teamDetArr = teamDetailDb.getTeamDetail(team.getTopicIds(),team.getTeamId());
		
		List<TopicDetails> topicDetailList = formTopicDetails(teamDetArr);
			
		return topicDetailList;
	}
	
	public List<TopicDetails> getTopicDetailMatch(String uniqueId )
	{
		List<TopicDetails> topicDetailList = new ArrayList<TopicDetails>();
		for(int i = 1 ;i < 5 ; i++)
		{
			TopicDetails topicDetail = new TopicDetails();
			topicDetail.setUniqueId(uniqueId);
			topicDetail.setTopicId("match topic id"  + i);
			topicDetail.setTopic("match topic " + i);
			topicDetail.setOption("match option " + i);
			topicDetail.setCategory(i%2 ==0 ? TeamQuestConstants.general_key : TeamQuestConstants.matchdetail_key);
			Map<String,List<Players>> optionDetail = new LinkedHashMap<String, List<Players>>();
			Map<String,String> optionIds = new LinkedHashMap<String, String>();
			for(int j = 0 ;j < i ; j++)
			{
				Players p1 = new Players();
				p1.setPlayerId("955159116" + j);
				Players p2 = new Players();
				p2.setPlayerId("955159116" + j+1);
				optionIds.put("match option id" +j+""+i, "match option " +j);
				optionDetail.put("match option id" +j+""+i, new ArrayList<Players>(Arrays.asList(p1,p2)));
				
			}
			topicDetail.setOptionIds(optionIds);
			topicDetail.setOptions(optionDetail);
			topicDetail.setCreatedBy("created by" + i);
			topicDetailList.add(topicDetail);
		}
		return topicDetailList;
	}
	
	private List<TopicDetails> formTopicDetails(JSONArray teamDetArr)
	{
		List<TopicDetails> topicDetailList = null;
		String optionSelected = null;
		try{
			
			if(teamDetArr != null)
			{
				topicDetailList = new ArrayList<TopicDetails>();
			for(int i = 0 ; i < teamDetArr.length() ; i ++)
			{
				JSONObject topicDet = (JSONObject) teamDetArr.get(i);
				TopicDetails topicDetail = new TopicDetails();
				
				
				topicDetail.setTopicId(topicDet.getString(CommonDBConstants.topicId));
				topicDetail.setTopic(topicDet.getString(CommonDBConstants.topic));
				
				topicDetail.setCategory(topicDet.getString(CommonDBConstants.category));
				topicDetail.setCreatedBy(topicDet.getString(CommonDBConstants.createdBy));
				Map<String,List<Players>> optionDetail = new LinkedHashMap<String, List<Players>>();
				Map<String,String> optionIds = new LinkedHashMap<String, String>();
				
				JSONArray optionsArr = topicDet.getJSONArray(CommonDBConstants.options);
				if(optionsArr != null && optionsArr.length() == 1)
				{
					JSONObject optionDet = optionsArr.getJSONObject(0);
					topicDetail.setUniqueId(optionDet.getString(CommonDBConstants.uniqueId));
					optionSelected = String.valueOf(optionDet.getInt(CommonDBConstants.option));
					
					JSONArray optionDetArr = optionDet.getJSONArray(CommonDBConstants.options);
					for(int j = 0 ; j < optionDetArr.length() ; j++)
					{
						String option ;
						
						option = optionDetArr.getJSONObject(j).getString(CommonDBConstants.option);
						
						optionIds.put(optionDetArr.getJSONObject(j).getString(CommonDBConstants.optionId)
								, option);
						
						JSONArray playerIdArr = optionDetArr.getJSONObject(j).getJSONArray(CommonDBConstants.playerIds);
						List<Players> playersList = new ArrayList<Players>();
						for(int k = 0 ; k < playerIdArr.length() ; k ++)
						{
							Players player = new Players();
							player.setPlayerId(playerIdArr.getString(k));
							playersList.add(player);
						}
						
						optionDetail.put(optionDetArr.getJSONObject(j).getString(CommonDBConstants.optionId)
								, playersList);
					}
				}
				topicDetail.setOption(optionIds.get(optionSelected) == null ? "" : optionIds.get(optionSelected));
				topicDetail.setOptionIds(optionIds);
				topicDetail.setOptions(optionDetail);
				
				topicDetailList.add(topicDetail);
			}
			}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				topicDetailList = null;
			}
		
		return topicDetailList;
	}
	
	public TopicDetails saveDetail(TopicDetails topicDetail,TopicDetails modifiedTopicDetail,String playerId)
	{
		
		// update the "option" using the max player selected for a option and update it
		// after updating retrieve the the all data which is used below and send them back  
		
		
		//below code can be removed when DB is introduced 
		TopicDetails savedTopicDetail = null;
		try{
		
		
		JSONObject topicDet = new JSONObject();
		topicDet.put(CommonDBConstants.topicId, Integer.parseInt(topicDetail.getTopicId()));
		topicDet.put(CommonDBConstants.uniqueId, Integer.parseInt(topicDetail.getUniqueId()));
		
		JSONArray optionDetArr = new JSONArray();
		for(Entry<String, List<Players>> entry : topicDetail.getOptions().entrySet())
		{
			JSONObject optionDet = new JSONObject();
			optionDet.put(CommonDBConstants.optionId, Integer.parseInt(entry.getKey()));
			optionDet.put(CommonDBConstants.playerId, playerId);
			if(modifiedTopicDetail.getOptionModified().get(entry.getKey()).equals(Boolean.FALSE))
			{
				optionDet.put("toAdd", false);
			}else if(modifiedTopicDetail.getOptionModified().get(entry.getKey()).equals(Boolean.TRUE)){
				optionDet.put("toAdd", true);
				
			}
			optionDetArr.put(optionDet);
		}
		topicDet.put(CommonDBConstants.options, optionDetArr);
		
		TeamDetailDB teamDetailDb = new TeamDetailDB();
		JSONArray resultTopicDet = teamDetailDb.saveTopicDetail(topicDet);
		
		List<TopicDetails> savedTopicDetailList = formTopicDetails(resultTopicDet);
		
		if(savedTopicDetailList != null && savedTopicDetailList.size() == 1)
		{
			savedTopicDetail = savedTopicDetailList.get(0);
		}
		
		
		}
		catch(Exception e )
		{
			e.printStackTrace();
		}
		return savedTopicDetail;
	}
	
	public int addTopic(String uniqueId, String playerId, List<String> options,String topic,String screenName)
	{
//		unique id can be match status id or team id, topic id should be created newly
		
//		based on screen name we have update in team detail or match detail table
		JSONObject addTopicDoc = new JSONObject();
		JSONObject newTopicDet = new JSONObject();
		int result = -1;
		try {
			
			newTopicDet.put(CommonDBConstants.topic, topic);
			newTopicDet.put(CommonDBConstants.category, TeamQuestConstants.general_key);
			newTopicDet.put(CommonDBConstants.createdBy, playerId);
			
			JSONArray optionsDetArr = new JSONArray();
			
			JSONObject optionsDet = new JSONObject();
			optionsDet.put(CommonDBConstants.uniqueId, Integer.parseInt(uniqueId));
			
			
			JSONArray optionArr = new JSONArray();
			int i = 100;
			optionsDet.put(CommonDBConstants.option, i);
			for(String option : options)
			{				
				JSONObject optionDet = new JSONObject();
				optionDet.put(CommonDBConstants.playerIds, new JSONArray());
				optionDet.put(CommonDBConstants.optionId, i);
				optionDet.put(CommonDBConstants.option, option);
				i++;
				optionArr.put(optionDet);
			}
			
			optionsDet.put(CommonDBConstants.options, optionArr);
			
			optionsDetArr.put(optionsDet);
			
			newTopicDet.put(CommonDBConstants.options, optionsDetArr);
			
			addTopicDoc.put("TopicDetail", newTopicDet);
			addTopicDoc.put("ScreenName", screenName);
			
			TeamDetailDB teamDetailDb = new TeamDetailDB();
			result = teamDetailDb.addTopicDetail(addTopicDoc);
		} catch (JSONException e) {
			result = -1;
		}
		return result;
	}
	
	public TopicDetails updateTopic(TopicDetails updateTopicDetail)
	{
//		unique id from topicdetail can be match status id or team id, topic id can be used to update the new topic.
//		topic id , topic ,optionid's  can be taken from updatetopicdetail object
		
//		in optionsid map if id  already exist in DB then update the option value or else insert that option with new option id
		
//		based on screen name we have update in team detail or match detail table
		
		JSONObject topicDoc = new JSONObject();
		JSONObject modifyTopicDet = new JSONObject();
		TopicDetails result = null;
		try {
			
			modifyTopicDet.put(CommonDBConstants.topic, updateTopicDetail.getTopic());
			modifyTopicDet.put(CommonDBConstants.topicId, Integer.parseInt(updateTopicDetail.getTopicId()));
			
			JSONArray optionsDetArr = new JSONArray();
			
			JSONObject optionsDet = new JSONObject();
			optionsDet.put(CommonDBConstants.uniqueId, Integer.parseInt(updateTopicDetail.getUniqueId()));
			
			JSONArray optionArr = new JSONArray();
			int i = 100;
			for(Entry<String, String> optionEntry : updateTopicDetail.getOptionIds().entrySet())
			{				
				JSONObject optionDet = new JSONObject();
				try{
					int optionKey = Integer.parseInt(optionEntry.getKey());
					if(i < optionKey)
					{
						i = optionKey;
					}
					optionDet.put(CommonDBConstants.optionId, optionKey);
				}catch(Exception e)
				{
					i++;
					optionDet.put(CommonDBConstants.optionId, i);
				}
				
				optionDet.put(CommonDBConstants.option, optionEntry.getValue());
				optionDet.put(CommonDBConstants.playerIds, new JSONArray());
				optionArr.put(optionDet);
			}
			
			optionsDet.put(CommonDBConstants.options, optionArr);
			
			optionsDetArr.put(optionsDet);
			
			modifyTopicDet.put(CommonDBConstants.options, optionsDetArr);
			
			
			TeamDetailDB teamDetailDb = new TeamDetailDB();
			JSONArray resultTopicDet = teamDetailDb.updateTopicDetail(modifyTopicDet);
						
			List<TopicDetails> savedTopicDetailList = formTopicDetails(resultTopicDet);
			
			if(savedTopicDetailList != null && savedTopicDetailList.size() == 1)
			{
				result = savedTopicDetailList.get(0);
			}
			
		} catch (JSONException e) {
			result = null;
		}
		return result;
	}
	
}
