package com.example.matchquest.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.example.matchquest.common.TeamQuestConstants;

public class MatchStatus implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String uniqueId;

	private String opponentTeam;
	
	private String opponentCode;
	
	private String opponentTeamId;
	
	private String yourTeamName;
	
	private String yourTeamCode;
	
	private String yourTeamId;
	
	private String matchStatusId;
	
	private String location;
	
	private List<String> locationList;
	
	private Date date;
	
	private String time; 
	
	private String nop;
	
	private String DateString;
	
	private List<Players> playersList;
	
	private List<Integer> topicIds;
	
	private Map<String,Players> selectedPlayersMap;
	  

	public MatchStatus()
	{
		
	}

	public String getOpponentTeam() {
		return opponentTeam;
	}

	public void setOpponentTeam(String opponentTeam) {
		this.opponentTeam = opponentTeam;
	}

	public String getOpponentTeamId() {
		return opponentTeamId;
	}

	public void setOpponentTeamId(String opponentTeamId) {
		this.opponentTeamId = opponentTeamId;
	}

	public String getYourTeamName() {
		return yourTeamName;
	}

	public void setYourTeamName(String yourTeamName) {
		this.yourTeamName = yourTeamName;
	}

	public String getYourTeamId() {
		return yourTeamId;
	}

	public void setYourTeamId(String yourTeamId) {
		this.yourTeamId = yourTeamId;
	}

	public String getMatchStatusId() {
		return matchStatusId;
	}

	public void setMatchStatusId(String matchStatusId) {
		this.matchStatusId = matchStatusId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
		setDateString(TeamQuestConstants.dateFormat.format(date));
	}

	public String getOpponentCode() {
		return opponentCode;
	}

	public void setOpponentCode(String opponentCode) {
		this.opponentCode = opponentCode;
	}

	public String getYourTeamCode() {
		return yourTeamCode;
	}

	public void setYourTeamCode(String yourTeamCode) {
		this.yourTeamCode = yourTeamCode;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDateString() {
		return DateString;
	}

	public void setDateString(String dateString) {
		DateString = dateString;
	}

	public List<Players> getPlayersList() {
		return playersList;
	}

	public void setPlayersList(List<Players> playersList) {
		this.playersList = playersList;
	}

	public List<Integer> getTopicIds() {
		return topicIds;
	}

	public void setTopicIds(List<Integer> topicIds) {
		this.topicIds = topicIds;
	}

	public Map<String, Players> getSelectedPlayersMap() {
		return selectedPlayersMap;
	}

	public void setSelectedPlayersMap(Map<String, Players> selectedPlayersMap) {
		this.selectedPlayersMap = selectedPlayersMap;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getNop() {
		return nop;
	}

	public void setNop(String nop) {
		this.nop = nop;
	}

	public List<String> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<String> locationList) {
		this.locationList = locationList;
	}
	
}
