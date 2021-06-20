package com.example.matchquest.DataManipulation.Player;

import com.example.matchquest.db.Player.PlayerDB;
import com.example.matchquest.model.Players;

public class PlayerDM {

	public int registerPlayer(Players player)
	{
		PlayerDB playerDB = new PlayerDB();
		return playerDB.registerPlayer(player);
	}
}
