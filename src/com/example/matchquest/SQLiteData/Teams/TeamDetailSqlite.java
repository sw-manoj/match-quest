package com.example.matchquest.SQLiteData.Teams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.example.matchquest.SQLiteData.CommonSqlite;
import com.example.matchquest.SQLiteData.TeamQuestSqlite;
import com.example.matchquest.model.Comment;
import com.example.matchquest.model.Players;
import com.example.matchquest.model.Team;
import com.example.matchquest.model.TopicDetails;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TeamDetailSqlite {

	SQLiteDatabase database;
    public TeamDetailSqlite(SQLiteDatabase db) {
    	this.database = db;
    }
    
    public void deleteTopicDetail(String uniqueId){
    	database.delete(CommonSqlite.teamDetail_table, CommonSqlite.teamDetail_teamId+ " =?", new String[]{uniqueId});
    }
    
    public void insertTopicDetail(String uniqueId,List<TopicDetails> topicDetailList){
    	deleteTopicDetail(uniqueId);
    	
    	for(TopicDetails topicDetail : topicDetailList)
    	{
    		ContentValues contentValue = new ContentValues();
    		contentValue.put(CommonSqlite.teamDetail_category, topicDetail.getCategory());
    		contentValue.put(CommonSqlite.teamDetail_createdBy, topicDetail.getCreatedBy());
    		contentValue.put(CommonSqlite.teamDetail_option, topicDetail.getOption());
    		contentValue.put(CommonSqlite.teamDetail_optionIds, 
    				CommonSqlite.convertMapStringToString(topicDetail.getOptionIds()));
    		contentValue.put(CommonSqlite.teamDetail_options, 
    				CommonSqlite.convertMapToString(topicDetail.getOptionsAsString()));
    		contentValue.put(CommonSqlite.teamDetail_teamId, uniqueId);
    		contentValue.put(CommonSqlite.teamDetail_topic, topicDetail.getTopic());
    		contentValue.put(CommonSqlite.teamDetail_topicId, topicDetail.getTopicId());
    		
    		database.insert(CommonSqlite.teamDetail_table, null, contentValue);
    	}
    	
    }
    
    public void closeConnection()
    {
    	database.close();
    }
    
    public List<TopicDetails> getTopicDetails(String uniqueId,Team team)
    {
    	List<TopicDetails> topicDetailList = new ArrayList<TopicDetails>();
    	
    	 String selectQuery = "SELECT  * FROM " + CommonSqlite.teamDetail_table
        		 + " WHERE "+ CommonSqlite.teamDetail_teamId+" =? ";
 
        Cursor cursor = database.rawQuery(selectQuery, new String[]{uniqueId});
        if (cursor.moveToFirst()) {
            do {
            	TopicDetails topicDetail = new TopicDetails();
            	topicDetail.setTopicId(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.teamDetail_topicId)));
            	topicDetail.setUniqueId(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.teamDetail_teamId)));
            	topicDetail.setTopic(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.teamDetail_topic)));
            	topicDetail.setOption(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.teamDetail_option)));
            	topicDetail.setOptionIds(
            			CommonSqlite.convertStringToMapString(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.teamDetail_optionIds))));
            	
            	Map<String,List<Players>> options = new HashMap<String, List<Players>>();
            	Map<String,ArrayList<String>> optionsWithPlayers = CommonSqlite.convertStringToMap(cursor.getString(cursor.getColumnIndex(CommonSqlite.teamDetail_options)));
            	
            	PLayersSqlite playerDetail = new PLayersSqlite(database);
            	for(Entry<String, ArrayList<String>> entry : optionsWithPlayers.entrySet())
            	{
            		options.put(entry.getKey(),playerDetail.getPlayersInsertIfDontExist(entry.getValue(),team ));
            	}
            	
            	
            	topicDetail.setOptions(options);
            	topicDetail.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.teamDetail_category)));
            	topicDetail.setCreatedBy(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.teamDetail_createdBy)));

                topicDetailList.add(topicDetail);
                
            } while (cursor.moveToNext());
        }
    	return topicDetailList;
    }
    
    public void deleteTopicDetailComment(String teamId,String topicId){
    	database.delete(CommonSqlite.topicDetail_comments_table, CommonSqlite.topicDetail_comments_teamId+ " = ? and " + CommonSqlite.topicDetail_comments_topicId + " = ?", new String[]{teamId,topicId});
    	
    }
    
    public void insertTopicDetailComment(String teamId,String topicId,boolean toBeDeleted,List<Comment> commentList){
    	if(toBeDeleted)
    	{
    	deleteTopicDetailComment(teamId, topicId);
    	}
    	
    	for(Comment comment : commentList)
    	{
    		ContentValues contentValue = new ContentValues();
    		contentValue.put(CommonSqlite.topicDetail_comments_comment, comment.getComment());
    		contentValue.put(CommonSqlite.topicDetail_comments_playerId, comment.getPlayer().getPlayerId());
    		contentValue.put(CommonSqlite.topicDetail_comments_teamId, teamId);
    		contentValue.put(CommonSqlite.topicDetail_comments_topicId, topicId);
    		
    		database.insert(CommonSqlite.topicDetail_comments_table, null, contentValue);
    	}
    	
    }
    
    public List<Comment> getComments(String uniqueId,Team team,String topicId)
    {
    	List<Comment> commentList = new ArrayList<Comment>();
    	 String selectQuery = "SELECT  * FROM " + CommonSqlite.topicDetail_comments_table
        		 + " WHERE "+ CommonSqlite.topicDetail_comments_teamId+" = ? and " + CommonSqlite.topicDetail_comments_topicId + " = ? ";
 
        Cursor cursor = database.rawQuery(selectQuery, new String[]{uniqueId , topicId});
        if (cursor.moveToFirst()) {
            do {
            	Comment comment = new Comment();
            	comment.setUniqueId(uniqueId);
            	comment.setTopicId(topicId);
            	comment.setComment(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.topicDetail_comments_comment)));
            	PLayersSqlite playerDetail = new PLayersSqlite(database);
            	
            	List<Players> players = playerDetail.getPlayersInsertIfDontExist(new ArrayList<String>
            			(Arrays.asList(cursor.getString(cursor.getColumnIndexOrThrow(CommonSqlite.topicDetail_comments_playerId)))), team);
            	if(players != null && players.size() > 0)
            	{
            		comment.setPlayer(players.get(0));
            	}
            	commentList.add(comment);
            } while (cursor.moveToNext());
        }
        return commentList;
    }
}
