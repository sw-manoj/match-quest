package com.example.matchquest.View.teams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.example.matchquest.R;
import com.example.matchquest.DataManipulation.Teams.TeamEditDM;
import com.example.matchquest.SQLiteData.TeamQuestSqlite;
import com.example.matchquest.SQLiteData.Teams.TeamListSqlite;
import com.example.matchquest.View.CommonViewClass;
import com.example.matchquest.common.Details;
import com.example.matchquest.model.Contacts;
import com.example.matchquest.model.Players;
import com.example.matchquest.model.Team;

public class AddMemersPopUp {
	PopupWindow addMemberPopUp;
	RecyclerView popUprecycleView;
	PopUpAdpater popUpAdapter;
	List<Contacts> contactList;
	List<String> contactsNum;
	List<Players> playerList;
	ActionBarActivity context;
	TextView teamMemberCount;
	int totalCount;
	Team team;
	TextView emptyText;
	
	List<Players> playerToBeAdded ;
	ProgressDialog mProgressDialog;
	
	public void showPopup(ActionBarActivity mcontext,List<Players> players,Team t) 
	{
		this.context = mcontext;
		this.playerList = players;
		this.team = t;
	       // Inflate the popup_layout.xml
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.add_member_popup,(ViewGroup) context.findViewById(R.id.popup_element));
		totalCount =(Integer.parseInt(context.getString(R.string.team_member_count)) - playerList.size());
		teamMemberCount = (TextView) layout.findViewById(R.id.team_members_count_view);
		teamMemberCount.setText("(0/" + totalCount + ")");
			Display display = context.getWindowManager().getDefaultDisplay();
			double width = display.getWidth();
			double height = display.getHeight();
			
			int heightSize = (int) ((height/4)*3);
			int widthSize = (int) ((width/5)*4);
			popUprecycleView = (RecyclerView) layout.findViewById(R.id.team_edit_popup_recycleview);
			popUprecycleView.setLayoutManager(new LinearLayoutManager(context));
			contactList = getContacts();
			popUpAdapter = new PopUpAdpater(context, contactList, this,totalCount);
			popUprecycleView.setAdapter(popUpAdapter);
			emptyText = (TextView) layout.findViewById(R.id.empty_view);
			if(contactList == null || contactList.size() == 0 )
			{
				popUprecycleView.setVisibility(View.GONE);
				emptyText.setVisibility(View.VISIBLE);
			}
	       // Creating the PopupWindow
	       addMemberPopUp = new PopupWindow(context);
	       addMemberPopUp.setContentView(layout);
	       addMemberPopUp.setFocusable(true);
	       addMemberPopUp.setHeight(heightSize);
	       addMemberPopUp.setWidth(widthSize);

	       // Clear the default translucent background
//	       addMemberPopUp.setBackgroundDrawable(new BitmapDrawable());

	       addMemberPopUp.showAtLocation(layout, Gravity.CENTER, 0, 0);
	       addMemberPopUp.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				
				((TeamEditActivity)context).reGainLayout();
				
			}
		});

	       // Getting a reference to Close button, and close the popup when clicked.
	       Button close = (Button) layout.findViewById(R.id.close_popup);
	       close.setOnClickListener(new OnClickListener() {

	         @Override
	         public void onClick(View v) {
	        	 addMemberPopUp.dismiss();
	         }
	       });
	       
	       Button addTeamMembers = (Button) layout.findViewById(R.id.add_team_member_button);
	       addTeamMembers.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!CommonViewClass.isNetworkAvailable(context))
				{
					CommonViewClass.showdialog(context, "Please ,Connect to internet to add team members");
					return;
				}
					addMemberPopUp.dismiss();
				 if(popUpAdapter.getContacts().size()>0)
				 {
					
					 List<Players> playersList = convertContactsToPlayers(popUpAdapter.getContacts());
					 Team updateTeam = new Team();
					 updateTeam.setTeamId(team.getTeamId());
					 // players to be added
					 updateTeam.setPlayersList(playersList);
					 
					 new addTeamMembersTask().execute(updateTeam);
					 
				 }
				
			}
		});

	}
	
	private List<Players> convertContactsToPlayers(List<Contacts> contacts)
	{
		List<Players> playersList = new ArrayList<Players>();
		for (int i = 0 ; i < contacts.size(); i ++)
		{
			Players player = new Players();
			player.setPlayerId(contacts.get(i).getNumber());
			player.setPlayerName(contacts.get(i).getName());
			player.setRegistered((i>5)?false : true);
			playersList.add(player);
		}
		return playersList;
	}
	
	public List<Contacts> getContacts()
	{
		Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                null, null);
		contactsNum = new ArrayList<String>();
        List<Contacts> contacts = new ArrayList<Contacts>();
        
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
                    if(!contactsNum.contains(contact.getNumber()) && !contact.getNumber().equals(Details.getPlayer(context).getPlayerId()))
                    {
                    	contactsNum.add(contact.getNumber());
                    	boolean toBeAdded = true;
                    	for(Players player : playerList)
                    	{
                    		if(player.getPlayerId().equals(contact.getNumber()))
                    		{
                    			toBeAdded = false;
                    			break;
                    		}
                    	}
                    	if(toBeAdded)
                    	{
                    	contacts.add(contact);
                    	} 
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
        return contacts;
	}
	
	public void setTeamMemberCount(int size)
	{
		teamMemberCount.setText("(" + size + "/" + totalCount + ")");
	}
	
	private class addTeamMembersTask extends AsyncTask<Team, Void, Integer>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(!isCancelled())
			{
		
			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			
			}
		}
		@Override
		protected Integer doInBackground(Team... params) {
			if(!isCancelled())
			{
			TeamEditDM teamEdit = new TeamEditDM();
			playerToBeAdded = teamEdit.addTeamMembers(params[0]);
			if(playerToBeAdded != null)
			{
				TeamQuestSqlite teamlist = new TeamQuestSqlite(context);
				teamlist.getTeamListSqlite().insertTeamMembers(team, popUpAdapter.getContacts());
				
			}
			}
			return 0;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if(!isCancelled())
			{
				mProgressDialog.dismiss();
				if(playerToBeAdded != null)
				{
					addMemberPopUp.dismiss();
					((TeamEditActivity)context).reLoad(playerToBeAdded);
				
				}else{
					Toast.makeText(context, "Something wrong happened please try again ", 1000).show();
				}
			}
		}
		
	}
}
