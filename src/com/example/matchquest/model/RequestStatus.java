package com.example.matchquest.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.example.matchquest.common.TeamQuestConstants;

public class RequestStatus implements Serializable,Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String teamName;
	
	private String teamId;
	
	private String teamCode;
	//iniviteId or match Id
	private String uniqueId;
	
	private long uniqueIdLong;
	
	private String location;
	
	private List<String> locationList;
	
	private Date date;
	
	private String dateString;
	
	private String time;
	
	private String nop;
	
	private boolean isParent;
	
	private String parentInviteId;
	
	private boolean isRequestSent;

	//used only in find opponent screen
	
	private boolean isInviteSaved;
	
	private boolean tobeSaved;
	
	private String postedBy;
	
	private List<Players> playersList;
	
	private List<Integer> topicIds;
		
	//used only in match detail
	private Map<String,Players> selectedPlayersMap;
	
	//used in team display,request status
	
	private List<RequestStatus> opponentInvites;
	
	private List<String> playerStringList;
	
	//savePlayerOpinion
	
	private String playerId;
	
	private boolean toRemove;
		
	public RequestStatus()
	{
		
	}
	
	public RequestStatus clone()throws CloneNotSupportedException{  
		return (RequestStatus) super.clone();  
	}  

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getNop() {
		return nop;
	}

	public void setNop(String nop) {
		this.nop = nop;
	}

	public boolean isParent() {
		return isParent;
	}

	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}


	public String getParentInviteId() {
		return parentInviteId;
	}

	public void setParentInviteId(String parentInviteId) {
		this.parentInviteId = parentInviteId;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public List<Players> getPlayersList() {
		return playersList;
	}

	public void setPlayersList(List<Players> playersList) {
		this.playersList = playersList;
	}

	public long getUniqueIdLong() {
		return uniqueIdLong;
	}

	public void setUniqueIdLong(long uniqueIdLong) {
		this.uniqueIdLong = uniqueIdLong;
	}

	public boolean isRequestSent() {
		return isRequestSent;
	}

	public void setRequestSent(boolean isRequestSent) {
		this.isRequestSent = isRequestSent;
	}

	public String getTeamCode() {
		return teamCode;
	}

	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}
	

	public boolean isTobeSaved() {
		return tobeSaved;
	}

	public void setTobeSaved(boolean tobeSaved) {
		this.tobeSaved = tobeSaved;
	}

	public boolean isInviteSaved() {
		return isInviteSaved;
	}

	public void setInviteSaved(boolean isInviteSaved) {
		this.isInviteSaved = isInviteSaved;
	}

	public Map<String, Players> getSelectedPlayersMap() {
		return selectedPlayersMap;
	}

	public void setSelectedPlayersMap(Map<String, Players> selectedPlayersMap) {
		this.selectedPlayersMap = selectedPlayersMap;
	}

	public String getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(String postedBy) {
		this.postedBy = postedBy;
	}

	public List<String> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<String> locationList) {
		this.locationList = locationList;
	}

	public List<RequestStatus> getOpponentInvites() {
		return opponentInvites;
	}

	public void setOpponentInvites(List<RequestStatus> opponentInvites) {
		this.opponentInvites = opponentInvites;
	}

	public List<String> getPlayerStringList() {
		return playerStringList;
	}

	public void setPlayerStringList(List<String> playerStringList) {
		this.playerStringList = playerStringList;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public boolean isToRemove() {
		return toRemove;
	}

	public void setToRemove(boolean toRemove) {
		this.toRemove = toRemove;
	}

	public List<Integer> getTopicIds() {
		return topicIds;
	}

	public void setTopicIds(List<Integer> topicIds) {
		this.topicIds = topicIds;
	}

	
}
