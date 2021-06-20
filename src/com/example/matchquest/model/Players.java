package com.example.matchquest.model;

import java.io.Serializable;

public class Players  implements Serializable{

	String playerName;
	
	String playerId;
	
	boolean isRegistered;
	
	boolean isSelected;
	
	public Players() {
		// TODO Auto-generated constructor stub
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public String getPlayerId() {
		return playerId;
	}
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	public boolean isRegistered() {
		return isRegistered;
	}
	public void setRegistered(boolean isRegistered) {
		this.isRegistered = isRegistered;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
}
