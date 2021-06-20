package com.example.matchquest.model;

import java.io.Serializable;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Team implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String teamId;
	public String teamName;
	public int numPlayers;
	private List<Players> playersList;
	public String area;
	private String captain;
	private String viceCaptain;
	private String teamCode;
	
	private List<Integer> topicIds;
	private List<RequestStatus> invitesList;
	
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public int getNumPlayers() {
		return numPlayers;
	}
	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}

	public String getCaptain() {
		return captain;
	}
	public void setCaptain(String captain) {
		this.captain = captain;
	}
	public String getViceCaptain() {
		return viceCaptain;
	}
	public void setViceCaptain(String viceCaptain) {
		this.viceCaptain = viceCaptain;
	}
	public String getTeamCode() {
		return teamCode;
	}
	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}
	public List<Players> getPlayersList() {
		return playersList;
	}
	public void setPlayersList(List<Players> playersList) {
		this.playersList = playersList;
	}

	public List<RequestStatus> getInvitesList() {
		return invitesList;
	}
	public void setInvitesList(List<RequestStatus> invitesList) {
		this.invitesList = invitesList;
	}
	public List<Integer> getTopicIds() {
		return topicIds;
	}
	public void setTopicIds(List<Integer> topicIds) {
		this.topicIds = topicIds;
	}
	
	
	
}
