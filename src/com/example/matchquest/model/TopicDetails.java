package com.example.matchquest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TopicDetails implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//this can be team id match Status id
	String uniqueId;
		
	String topicId;
	
	String topic;
	
	Map<String, String> optionIds = new LinkedHashMap<String, String>();
	
	Map<String, List<Players>> options = new LinkedHashMap<String, List<Players>>();
	
	String createdBy;
	
	String category;
	
	String option;
	
	//used only for save purpose
	Map<String, Boolean> optionModified = new LinkedHashMap<String, Boolean>();
	
	Map<String, Boolean> optionOriginal = new LinkedHashMap<String, Boolean>();


	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Map<String, List<Players>> getOptions() {
		return options;
	}
	
	public Map<String, List<String>> getOptionsAsString() {
		Map<String, List<String>> optionsAsString = new HashMap<String, List<String>>();
		for(Entry<String, List<Players>> entry : options.entrySet())
		{
			List<String> playersIdList = new ArrayList<String>();
			for(Players player : entry.getValue())
			{
				playersIdList.add(player.getPlayerId());
			}
			optionsAsString.put(entry.getKey(), playersIdList);
		}
		return optionsAsString;
	}

	public void setOptions(Map<String, List<Players>> options) {
		this.options = options;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public Map<String, String> getOptionIds() {
		return optionIds;
	}

	public void setOptionIds(Map<String, String> optionIds) {
		this.optionIds = optionIds;
	}

	public Map<String, Boolean> getOptionModified() {
		return optionModified;
	}

	public void setOptionModified(Map<String, Boolean> optionModified) {
		this.optionModified = optionModified;
	}

	public Map<String, Boolean> getOptionOriginal() {
		return optionOriginal;
	}

	public void setOptionOriginal(Map<String, Boolean> optionOriginal) {
		this.optionOriginal = optionOriginal;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	
}
