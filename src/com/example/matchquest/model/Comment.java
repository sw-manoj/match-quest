package com.example.matchquest.model;

import java.io.Serializable;

public class Comment implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String comment;
	
	Players player;
	
	//both teamid and topicid are primary key
	String uniqueId;
	String topicId;
	
	public Comment()
	{
		
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}


	public Players getPlayer() {
		return player;
	}

	public void setPlayer(Players player) {
		this.player = player;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}	
	
}
