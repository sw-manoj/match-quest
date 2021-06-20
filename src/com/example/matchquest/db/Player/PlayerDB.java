package com.example.matchquest.db.Player;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.matchquest.SQLiteData.TeamQuestSqlite;
import com.example.matchquest.View.CommonViewClass;
import com.example.matchquest.db.CommonDBClass;
import com.example.matchquest.db.CommonDBConstants;
import com.example.matchquest.model.Players;

public class PlayerDB {

	public int registerPlayer(Players player)
	{
		int status = -1;
		
		String urlPattern = "/player/addPlayer";
		try {
		JSONObject playerJson = new JSONObject();
		
			playerJson.put(CommonDBConstants.playerId, player.getPlayerId());
			playerJson.put(CommonDBConstants.playerName, player.getPlayerName());
			playerJson.put(CommonDBConstants.isRegistered, player.isRegistered());
			
			HttpURLConnection conn = CommonDBClass.getDBConnection(urlPattern, playerJson, null, CommonDBConstants.postMethod_key);
			int responseCode = conn.getResponseCode();
			if(responseCode == HttpStatus.SC_OK){
					String responseString = CommonDBClass.readStream(conn.getInputStream());
					
					JSONObject responseJSON = new JSONObject(responseString);
					if(responseJSON.getString(CommonDBConstants.statusMsg_key) != null && responseJSON.getString(CommonDBConstants.statusMsg_key).equals(CommonDBConstants.success_key))
					{
						status = 1;
						JSONArray locationArray = responseJSON.getJSONArray("location");
						List<String> locationList = new ArrayList<String>();
						
						for(int i = 0 ; i < locationArray.length() ; i++)
						{
							locationList.add(locationArray.getJSONObject(i).getString("location"));
						}
						TeamQuestSqlite locationSqlite = new TeamQuestSqlite(CommonViewClass.getContext());
						locationSqlite.getLocationSqlite().insertLocation(locationList);
					}
			}
			if(conn != null)
				conn.disconnect();
			
		} catch (Exception e) {
			status = -1;
			e.printStackTrace();
		}
		
		return status;
	}
}
