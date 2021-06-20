package com.example.matchquest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.matchquest.DataManipulation.Player.PlayerDM;
import com.example.matchquest.View.CommonViewClass;
import com.example.matchquest.model.Players;

public class Register extends Activity{
	
	Button create;
	EditText nameText; 
	EditText noText;
	String name;
	String no;
	boolean save = true;
	ProgressDialog mProgressDialog;
	Players player;
	SharedPreferences prefs = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.register);
		prefs = getSharedPreferences("TeamQuest", MODE_PRIVATE);
		nameText = (EditText) findViewById(R.id.name_register);
		noText = (EditText) findViewById(R.id.no_register);
		create = (Button) findViewById(R.id.create_register);
		
		create.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!CommonViewClass.isNetworkAvailable(getApplicationContext()))
				{
					Toast.makeText(getApplicationContext(), "Connnect to Internet to reister", 1000).show();
					return;
				}
				if(nameText.getText().toString() == null && nameText.getText().toString().trim().equals(""))
				{
					Toast.makeText(getApplicationContext(), "Please enter a name", 2000).show();
					save = false;
				}
				else{
					name = nameText.getText().toString().trim();
				}
				if(noText.getText().toString() == null && noText.getText().toString().trim().equals(""))
				{
					Toast.makeText(getApplicationContext(), "Please enter a no", 2000).show();
					save = false;
				}
				else{
					
					no = noText.getText().toString().trim();
					if(no.length() != 10)
					{
						Toast.makeText(getApplicationContext(), "Please enter a proper no", 2000).show();
						save = false;
					}
					
				}
				
				if(save)
				{
				
				
				player = new Players();
				player.setPlayerId(no);
				player.setPlayerName(name);
				player.setRegistered(true);
				
                 new RegisterPlayer().execute();
                 
				}
				save = true;
				
			}
		});
		
		
	}
	
	private class RegisterPlayer extends AsyncTask<Void, Void, Integer>{

		@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				if(!isCancelled())
				{
			
				mProgressDialog = new ProgressDialog(Register.this);
				mProgressDialog.setMessage("Loading...");
				mProgressDialog.setIndeterminate(true);
				mProgressDialog.setCancelable(false);
				mProgressDialog.show();
				
				}
			}
		
		@Override
		protected Integer doInBackground(Void... params) {
		
			PlayerDM playerDm = new PlayerDM();
			int result = playerDm.registerPlayer(player);
			return result;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(!isCancelled())
			{
				mProgressDialog.dismiss();
				if(result == 1)
				{
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString("PlayerNo", no);
					editor.putString("PlayerName", name);
					editor.commit();
					
				Intent data = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(data);
				}else{
					Toast.makeText(getApplicationContext(), "Something wrong happened please try again ", 1000).show();
				}
			}
		}
	}
}
