package com.example.matchquest.DataManipulation.topicDetail;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.R.integer;

import com.example.matchquest.db.CommonDBConstants;
import com.example.matchquest.db.team.TeamDetailDB;
import com.example.matchquest.model.Comment;
import com.example.matchquest.model.Players;

public class TopicDetailCommentsDM {
	
	public List<Comment> getComments1(String topicId,String teamId)
	{
		List<Comment> commentList = new ArrayList<Comment>();
		for(int i = 0 ; i < 5 ; i ++)
		{
			Comment comment = new Comment();
			comment.setComment("comment comm " + i);
				Players player = new Players();
				player.setPlayerId("955159116" + i);
				player.setPlayerName("playername" + i);
				player.setRegistered((i%2 == 0) ? true : false);
			
			comment.setPlayer(player);
			comment.setTopicId(topicId);
			comment.setUniqueId(teamId);
			commentList.add(comment);
			
		}
		
		return commentList;
	}
	
	public List<Comment> getComments(String topicId,String uniqueId)
	{
		List<Comment> commentList = null;
		try{
			JSONObject commentQuery = new JSONObject();
			commentQuery.put(CommonDBConstants.topicId, Integer.parseInt(topicId));
			commentQuery.put(CommonDBConstants.uniqueId, Integer.parseInt(uniqueId));
			
			TeamDetailDB getTopicCommentsDb = new TeamDetailDB();
			JSONArray topicCommentArr = getTopicCommentsDb.getTopicComments(commentQuery);
			if(topicCommentArr != null)
			{
				commentList = new ArrayList<Comment>();
				
				for(int i = 0 ; i < topicCommentArr.length() ; i ++ )
				{
				Comment comment = new Comment();
				comment.setComment(topicCommentArr.getJSONObject(i).getString(CommonDBConstants.comment));
				
				JSONArray playerDetArr = topicCommentArr.getJSONObject(i).getJSONArray(CommonDBConstants.playerId);
				if(playerDetArr != null && playerDetArr.length() ==1)
				{
					Players player = new Players();
					player.setPlayerId(playerDetArr.getJSONObject(0).getString(CommonDBConstants.playerId));
					player.setPlayerName(playerDetArr.getJSONObject(0).getString(CommonDBConstants.playerName));
					player.setRegistered(playerDetArr.getJSONObject(0).getBoolean(CommonDBConstants.isRegistered));
					
					comment.setPlayer(player);
				}
				
				commentList.add(comment);
				}
			}
		}
		catch(Exception e)
		{
			commentList = null;
		}
		return commentList;
	}
	
	public int saveComment(Comment comment){
		int result = -1;
		try{
			
			JSONObject topicCommentDocs = new JSONObject();
			if(comment != null)
			{
				topicCommentDocs.put(CommonDBConstants.topicId, Integer.parseInt(comment.getTopicId()));
				topicCommentDocs.put(CommonDBConstants.uniqueId, Integer.parseInt(comment.getUniqueId()));
				topicCommentDocs.put(CommonDBConstants.playerId, comment.getPlayer().getPlayerId());
				topicCommentDocs.put(CommonDBConstants.comment, comment.getComment());
				
				TeamDetailDB addTopicCommentDb = new TeamDetailDB();
				result = addTopicCommentDb.addTopicComments(topicCommentDocs);
				
			}
		}catch(Exception e)
		{
			result = -1;
		}
		return result;
	}

}
