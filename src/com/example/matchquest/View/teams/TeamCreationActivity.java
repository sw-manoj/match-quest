package com.example.matchquest.View.teams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matchquest.MainActivity;
import com.example.matchquest.R;
import com.example.matchquest.Register;
import com.example.matchquest.DataManipulation.Player.PlayerDM;
import com.example.matchquest.DataManipulation.Teams.TeamCRUD;
import com.example.matchquest.SQLiteData.TeamQuestSqlite;
import com.example.matchquest.SQLiteData.Teams.TeamListSqlite;
import com.example.matchquest.View.CommonViewClass;
import com.example.matchquest.common.Details;
import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.model.Contacts;
import com.example.matchquest.model.Players;
import com.example.matchquest.model.Team;


public class TeamCreationActivity extends ActionBarActivity{
	
	EditText teamName;
	ListView mListView;
	ContactsAdapter mAdapter;
	Cursor cursor;
	List<Contacts> contacts;
	String playerId;
	TextView teamNameCount;
	TextView teamNameCountLimit;
	boolean isSearching = false;;
	String title;
	List<String> contactsNum;
	TextView teamMemberView ;
	String teamMemberText;
	
	ProgressDialog mProgressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        setContentView(R.layout.team_creation);
        playerId = getIntent().getStringExtra("playerNo");
        teamName = (EditText) findViewById(R.id.teamName);
		mListView = (ListView) findViewById(R.id.task_list);
		teamMemberView = (TextView) findViewById(R.id.team_members_view);
		teamMemberText = teamMemberView.getText().toString();
		teamMemberView.setText(teamMemberText + "(0/" + getString(R.string.team_member_count) + ")");
		
		teamNameCount = (TextView) findViewById(R.id.team_name_count);
		teamNameCount.setText("0");
		teamNameCountLimit = (TextView) findViewById(R.id.team_name_count_limit);
		teamNameCountLimit.setText("/" + teamNameCountLimit.getText());
		 getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		 getSupportActionBar().setHomeButtonEnabled(true);
		 title = getSupportActionBar().getTitle().toString();
		Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                null, null);
		contactsNum = new ArrayList<String>();
        contacts = new ArrayList<Contacts>();
        
        if (cursor.getCount() > 0) {

            if (cursor.moveToFirst()) {
                do {
                	Contacts contact = new Contacts();
                	if(cursor.getInt(cursor.getColumnIndex(Phone.TYPE)) == Phone.TYPE_MOBILE)
                	{
                    contact.setNumber(cursor
                            .getString(cursor
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[^\\d]", ""));
                    
                    contact.setName(cursor
                            .getString(cursor
                                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                    if(!contactsNum.contains(contact.getNumber()) && !contact.getNumber().equals(playerId))
                    {
                    	contactsNum.add(contact.getNumber());
                    	contacts.add(contact);
                    }
                	}
                
                } while (cursor.moveToNext());
            }

        }
		
        Collections.sort(contacts,new Comparator<Contacts>() {

			@Override
			public int compare(Contacts lhs, Contacts rhs) {
				// TODO Auto-generated method stub
				return lhs.getName().compareToIgnoreCase(rhs.getName());
			}
		});
        
		
		mAdapter = new ContactsAdapter(getApplicationContext(), contacts,this);
		mListView.setAdapter(mAdapter);
//		mListView.setOnItemClickListener(this);
		
		teamName.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				teamNameCount.setText(String.valueOf(s.length()));
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	public void setTeamMemberLimit(String size)
	{
		teamMemberView.setText(teamMemberText + "(" + size + "/" + getString(R.string.team_member_count) + ")");
	}
	

	
	//without non registered class. change in xml button click method
	public void saveTeam(View v)
	{		
		if(!CommonViewClass.isNetworkAvailable(getApplicationContext()))
		{
			CommonViewClass.showdialog(TeamCreationActivity.this, "Please ,Connect to internet to create new team");
			return;
		}
        //we nee dto generate a team id which will be unique to all teams can used to serach a single team alone too 
        //by sharing that code to others
		final List<Players> toBeSaved = new ArrayList<Players>();
		final List<String> registeredPlayers = new ArrayList<String>();
		final List<String> nonRegisteredPlayers = new ArrayList<String>();
        for(Contacts contact :mAdapter.getModifiedContacts())
        {
        	Players player = new Players();
        	player.setPlayerId(contact.getNumber().replaceAll("\\s+",""));
        	player.setPlayerName(contact.getName().replaceAll("\\s+",""));
        	toBeSaved.add(player);
        }
		
		if(teamName.getText() == null || teamName.getText().toString().trim().equals(""))
		{
//			mProgressDialog.dismiss();
			Toast.makeText(getApplicationContext(), "Name Your Team", 2000).show();

			
			return ;
			
		}
		
		if(toBeSaved == null || toBeSaved.size() <= 2)
		{
			Toast.makeText(getApplicationContext(), "team must have atleast 3 members", 2000).show();
			return ;
			
		}
		Team team = new Team();
		toBeSaved.add(0,Details.getPlayer(this));
		team.setTeamId(teamName.getText().toString()+"-" + playerId);
		team.setTeamName(teamName.getText().toString());
		team.setPlayersList(toBeSaved);
		team.setCaptain(toBeSaved.get(0).getPlayerId());
		team.setViceCaptain(toBeSaved.get(1).getPlayerId());
		team.setTeamCode("");
		
		new CreateTeamTask().execute(team);
//		in server side code we must insert a this team into team collection with plaers as a list
//		and in players collections each player must be available if not availale create that player with isregistered as false
		
		
		
		return ;
		
	}
	
	private class CreateTeamTask extends AsyncTask<Team, Void, Integer>{

		@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				if(!isCancelled())
				{
			
				mProgressDialog = new ProgressDialog(TeamCreationActivity.this);
				mProgressDialog.setMessage("Loading...");
				mProgressDialog.setIndeterminate(true);
				mProgressDialog.setCancelable(false);
				mProgressDialog.show();
				
				}
			}
		
		@Override
		protected Integer doInBackground(Team... team) {
			
			int result = 0;
			if(!isCancelled())
			{
			TeamCRUD teamCreation = new TeamCRUD();
			result = teamCreation.createTeam(team[0]);
			}
//			if(result == 1){
//				TeamQuestSqlite teamCreatesqlite = new TeamQuestSqlite(getApplicationContext());
//				teamCreatesqlite.getTeamListSqlite().insertTeam(team[0]);
//			}
			return result;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			if(!isCancelled())
			{
				
				if(result == 1)
				{
					Toast.makeText(getApplicationContext(), "Team Created", 1000).show();
				
					
					Intent teamListIntent = new Intent(getApplicationContext(),MainActivity.class);
					startActivity(teamListIntent);
					
				}else{
					Toast.makeText(getApplicationContext(), TeamQuestConstants.updationError_key, 1000).show();
				}
			}
		}

	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(!isSearching)
		{
		MenuInflater inflate = getMenuInflater();
		inflate.inflate(R.menu.team_creation_menu, menu);
		}
				return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.searchContact){
			
//            startActionMode(new ActionMode.Callback() {
//				
//				@Override
//				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//					return false;
//				}
//				
//				@Override
//				public void onDestroyActionMode(ActionMode mode) {
//					// TODO Auto-generated method stub
//					
//				}
//				
//				@Override
//				public boolean onCreateActionMode(final ActionMode mode, Menu menu) {
//					
//					LayoutInflater inflator = getLayoutInflater();
//					View view = inflator.inflate(R.layout.contact_search_action_bar, null);
//				final EditText text = (EditText) view.findViewById(R.id.toBeSearched);
//				text.setHint("search by name");
//				Button button = (Button) view.findViewById(R.id.done);
//				button.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						mListView.setSelection(mAdapter.searchContact(text.getText().toString()));
//						
//					}
//				});
//					mode.setCustomView(view);
////					mode.getCustomView().setBackgroundColor(color.colorPrimary);
//					
//					return true;
//				}
//			
//				@Override
//				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//					return true;
//				}
//			});
			getSupportActionBar().setTitle("");
			LayoutInflater inflator = getLayoutInflater();
			 getSupportActionBar().setDisplayHomeAsUpEnabled(false);
			 getSupportActionBar().setHomeButtonEnabled(false);
			View view = inflator.inflate(R.layout.contact_search_action_bar, null);
			final EditText searchText = (EditText) view.findViewById(R.id.toBeSearched);
			searchText.getBackground().setColorFilter(getResources().getColor(R.color.blackColor), PorterDuff.Mode.SRC_ATOP);
			searchText.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					mListView.setSelection(mAdapter.searchContact(s.toString()));
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					
				}
			});
			
			ImageView backNavIcon = (ImageView) view.findViewById(R.id.navigation_icon);
			backNavIcon.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					getSupportActionBar().setDisplayShowCustomEnabled(false);
					 getSupportActionBar().setDisplayHomeAsUpEnabled(true);
					 getSupportActionBar().setHomeButtonEnabled(true);
					 getSupportActionBar().setTitle(title);
					 isSearching = false;
					 invalidateOptionsMenu();
				}
			});
			getSupportActionBar().setCustomView(view);
			getSupportActionBar().setDisplayShowCustomEnabled(true);
			isSearching = true;
			invalidateOptionsMenu();
            return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
