package com.example.matchquest.View.FindOpponent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matchquest.R;
import com.example.matchquest.DataManipulation.FindOpponent.FindOpponentDM;
import com.example.matchquest.SQLiteData.TeamQuestSqlite;
import com.example.matchquest.View.CommonActivityWithFragment;
import com.example.matchquest.View.CommonFragment;
import com.example.matchquest.View.CommonViewClass;
import com.example.matchquest.View.FindOpponent.AsyncTask.PostInviteTask;
import com.example.matchquest.View.FindOpponent.AsyncTask.SaveInviteTask;
import com.example.matchquest.common.Details;
import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.model.RequestStatus;
import com.example.matchquest.model.Team;

public class FindOpponentFragment extends CommonFragment{

	LayoutInflater inflater;
	ViewGroup container;
	View rootView;
	
	Team team;
	
	MultiAutoCompleteTextView locAutoCompleteView;
	
	AutoCompleteAdapter locationAdapter;
	
	RelativeLayout dateRelativeLayout;
	
	TextView dateTextView;
	
	Spinner nopSpinner;
	
	Spinner timeSpinner;
	
	ImageView autoCompleteExpandableIcon;
	
	ImageView autoCompleteCollapsebleIcon;
	
	ImageView fullScreenIcon;
	
	ImageView normalScreenIcon;
		
	RecyclerView inviteRecyclerView;
	
	TextView emptyView;
	
	RelativeLayout searchViewlayout;
	
	Button inviteAllButton;
	
	Button closeButton;
	
	ProgressBar searchOpponentProgressBar;
	
	List<RequestStatus> requestStatusList;
	
	List<RequestStatus> list;
	
	List<RequestStatus> saveList;
	
	List<String> nopList = new ArrayList<String>();
	List<String> timeList = new ArrayList<String>();
	
//	String[] words=new String[] {
//		     "menu1", "word2", "word3", "word4", "word5"
//		 };
	
	List<String> locationList;
	List<String> searchByLoc;
	Date searchByDate;
	String searchByNop;
	String searchByTime;
	
    Calendar calendar = Calendar.getInstance();
    
    
    DateFormat dateFormat = new SimpleDateFormat("E, MMM dd yyyy");
    
    DateFormat dateFormatDisplay = new SimpleDateFormat("E, MMM dd yyyy");
   
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		this.inflater = inflater;
		this.container = container;

		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		 
		team = (Team) getArguments().getSerializable(TeamQuestConstants.team_key);
		
		rootView = inflater.inflate(R.layout.find_opponent_fragment_layout, container, false);
		initializeViews();
		loadAutoCompleteView();
		loadSpinners();
		dateDialogListener();
		viewListener();
			
		return rootView;
	}
	
	@Override
	public void onPopUpOpen() {
	
		rootView.setAlpha(0.3f);
	}
	
	@Override
	public void reGainLayout() {
	
		rootView.setAlpha(1);
	}
	
	private void initializeViews()
	{
		saveList = new ArrayList<RequestStatus>();
		
		searchViewlayout = (RelativeLayout) rootView.findViewById(R.id.search_views_layout_findopponent);
		
		locAutoCompleteView = (MultiAutoCompleteTextView) rootView.findViewById(R.id.auto_complete_view_findopponent);
		
		autoCompleteExpandableIcon = (ImageView) rootView.findViewById(R.id.auto_complete_expand_icon);
		
		autoCompleteCollapsebleIcon = (ImageView) rootView.findViewById(R.id.auto_complete_collapse_icon);
		
		dateRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.date_relativelayout_findopponent);
		
		dateTextView = (TextView) rootView.findViewById(R.id.date_findopponent);
		
		timeSpinner = (Spinner) rootView.findViewById(R.id.time_spinner_findopponent);
		
		nopSpinner = (Spinner) rootView.findViewById(R.id.nop_spinner_findopponent);
		
		fullScreenIcon = (ImageView) rootView.findViewById(R.id.full_screen_icon);
		normalScreenIcon = (ImageView) rootView.findViewById(R.id.normal_screen_icon);
		
		searchOpponentProgressBar = (ProgressBar) rootView.findViewById(R.id.serachopponent_progressbar);
		searchOpponentProgressBar.setVisibility(View.GONE);
		
		inviteRecyclerView = (RecyclerView) rootView.findViewById(R.id.navList);
		inviteRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
		
		requestStatusList = new ArrayList<RequestStatus>();
		FindOpponentInviteAdapter findOpponentAdapter = new FindOpponentInviteAdapter((CommonActivityWithFragment)getActivity(), requestStatusList, 
				team,inviteRecyclerView,this);
		
		inviteRecyclerView.setAdapter(findOpponentAdapter);
		
		emptyView = (TextView) rootView.findViewById(R.id.empty_view);
		
		inviteAllButton = (Button) rootView.findViewById(R.id.post_invite);
		
		closeButton = (Button) rootView.findViewById(R.id.close_invite);
	}
	
	
	private void loadAutoCompleteView()
	{
		TeamQuestSqlite locationSqlite = new TeamQuestSqlite(getActivity());
		
		locationList = locationSqlite.getLocationSqlite().getLocation();
		
		locationAdapter = new AutoCompleteAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line,
				locAutoCompleteView,locationList);
				
		locAutoCompleteView.setAdapter(locationAdapter);
		
		locAutoCompleteView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer() );
		
		
		locAutoCompleteView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				locationAdapter.getFilter().filter("");
				searchInvites();
			}
		});
		
				
		autoCompleteExpandableIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				locAutoCompleteView.showDropDown();
				
			}
		});
		
		
	}
	
	private void dateDialogListener()
	{
		Date date = calendar.getTime();
    	String dateStr = dateFormatDisplay.format(date);
    	
        dateTextView.setText(dateStr);
		final DialogFragment newFragment = new SelectDateFragment();
		dateRelativeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
	            newFragment.show(getFragmentManager(), "DatePicker");
				
			}
		});
		
	}
	
	private void loadSpinners()
	{
		for(int i = 2 ; i <= team.getNumPlayers() ;i++)
		{
			nopList.add(i+"");
		}
		
		ArrayAdapter<String> nopSpinnerAdapter = new ArrayAdapter<String>(getActivity(),
        		android.R.layout.simple_spinner_item, nopList);
//        		{
//        	@Override
//        	public int getCount() {
//        		 int count = super.getCount();
//        		 return count > 0 ? count - 1 : count;
//        	}
//        };
        nopSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		nopSpinner.setAdapter(nopSpinnerAdapter);
		nopSpinner.setSelection(nopSpinnerAdapter.getCount()-1);
		nopSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				searchInvites();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		timeList.addAll(Arrays.asList(getResources().getStringArray(R.array.timeArray)));
		ArrayAdapter<String> timeSpinnerAdapter = new ArrayAdapter<String>(getActivity(),
        		android.R.layout.simple_spinner_item, timeList);
//        		{
//        	@Override
//        	public int getCount() {
//        		 int count = super.getCount();
//        		 return count > 0 ? count - 1 : count;
//        	}
//        };
        timeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		timeSpinner.setAdapter(timeSpinnerAdapter);
		timeSpinner.setSelection(4);
		timeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				searchInvites();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
       
	}
	
	private void viewListener()
	{
		
		fullScreenIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				fullScreenIcon.setVisibility(View.GONE);
				normalScreenIcon.setVisibility(View.VISIBLE);
				searchViewlayout.setVisibility(View.GONE);
			}
		});
		normalScreenIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				normalScreenIcon.setVisibility(View.GONE);
				fullScreenIcon.setVisibility(View.VISIBLE);
				searchViewlayout.setVisibility(View.VISIBLE);
			}
		});
		
		inviteAllButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveAndCloseActivity(v);
				
			}
		});
		
		closeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveAndCloseActivity(v);
				
			}
		});
	}
	
	private void searchInvites()	 
	{
		try{
		List<String> locationPreffered = new ArrayList<String>();
		searchByLoc =  new ArrayList<String>();
		
			String[] locationsPreffered = null ;
		if(!locAutoCompleteView.getText().toString().trim().equals(""))
		{
		 locationsPreffered = locAutoCompleteView.getText().toString().split(",");
		}
		
		if(locationsPreffered != null && locationsPreffered.length > 0)
		{
			for(int i= 0 ; i < locationsPreffered.length ; i++)
			{
				if(!locationsPreffered[i].trim().equals("") && locationList.contains(locationsPreffered[i].trim()))
				{
				locationPreffered.add(locationsPreffered[i].trim());
				}
			}
		}
		
		if(locationPreffered == null || locationPreffered.size() == 0)
		{
			Toast.makeText(getActivityObject(), "choose a location", 1000).show();
			return;
		}
		
		
		searchByLoc = locationPreffered;
		searchByDate = dateFormat.parse(dateTextView.getText().toString());
		searchByNop = nopSpinner.getSelectedItem().toString();
		searchByTime = timeSpinner.getSelectedItem().toString();
		
		reload();
		}
		catch(Exception e)
		{
			Toast.makeText(getActivityObject(), "some error before searching", 1000).show();
		}
		
		
	}
	
	public void checkAdapterSize()
	{
		if(inviteRecyclerView.getAdapter().getItemCount()==0)
    	{
    		inviteRecyclerView.setVisibility(View.GONE);
    		emptyView.setVisibility(View.VISIBLE);
    	}
	}
	public void saveInvite(RequestStatus opponentStatus) 
	{
		
    	checkAdapterSize();
    	new SaveInviteTask(saveList.get(saveList.size()-1), opponentStatus, this).execute();
	}
	
	public void postSaveInviteTask(RequestStatus opponentStatus)
	{
		saveList.get(saveList.size()-1).setInviteSaved(true);
    	saveList.get(saveList.size()-1).setTobeSaved(true);
    	
    	Toast.makeText(getActivityObject(), "invite has been sent to " + opponentStatus.getTeamName(), 1000).show();
    	
	}
	public void saveAndCloseActivity(View v)
    {
		if(!CommonViewClass.isNetworkAvailable(getActivity()))
		{
			CommonViewClass.showdialog(getActivity(), TeamQuestConstants.connectToInternet_key);
			return ;
		}
		
    	try{
    		FindOpponentDM inviteAll = new FindOpponentDM();
    			switch (v.getId()) {
				case R.id.post_invite:
					if(searchByLoc != null && searchByLoc.size() == 0)
					{
						Toast.makeText(getActivityObject(), "choose a location", 1000).show();
						return;
					}
					new PostInviteTask(this).execute();
					
					break;

				case R.id.close_invite:

					inviteAll.updateStatus(saveList);
  
					break;
				
    		}
    	getActivity().finish();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
	
	public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

		 	 String dateStr;
		 	 Date date;
	        @Override
	        public Dialog onCreateDialog(Bundle savedInstanceState) {
	        	
	        int yy = calendar.get(Calendar.YEAR);
	        int mm = calendar.get(Calendar.MONTH);
	        int dd = calendar.get(Calendar.DAY_OF_MONTH);
	        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
	        }

	        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
	            populateSetDate(yy, mm, dd);
	        }
	        public void populateSetDate(int year, int month, int day) {
	        	calendar.set(year, month, day);
	        	date = calendar.getTime();
	        	dateStr = dateFormatDisplay.format(date);
	        	
	            dateTextView.setText(dateStr);
	            
	            searchInvites();
	            }

	    }
	 
	@Override
	protected void preExecute() {
		inviteRecyclerView.setVisibility(View.GONE);
		emptyView.setVisibility(View.GONE);
		searchOpponentProgressBar.setVisibility(View.VISIBLE);
	
	}
	@Override
	protected int loadInBackGround() {
		try{
			
		FindOpponentDM searchInvites = new FindOpponentDM();
		list = searchInvites.searchOpponent(searchByLoc, searchByDate, searchByNop, team);
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 1;
	}

	@Override
	public void updateView() {
		
		searchOpponentProgressBar.setVisibility(View.GONE);
		if(list != null && list.size() > 0)
		{
			requestStatusList.clear();
			requestStatusList.addAll(list);
			inviteRecyclerView.setVisibility(View.VISIBLE);
			emptyView.setVisibility(View.GONE);
			
			inviteRecyclerView.getAdapter().notifyDataSetChanged();
		}else{
			inviteRecyclerView.setVisibility(View.GONE);
			emptyView.setVisibility(View.VISIBLE);
		}
		
		RequestStatus searchStatus = new RequestStatus();
		searchStatus.setTeamCode(team.getTeamCode());
		searchStatus.setTeamName(team.getTeamName());
		searchStatus.setTeamId(team.getTeamId());
		searchStatus.setPostedBy(Details.getPlayer(getActivityObject()).getPlayerId());
		
		searchStatus.setUniqueId(searchStatus.getTeamId() + "-" + searchStatus.getPostedBy() + "-" + System.currentTimeMillis());
		searchStatus.setDate(searchByDate); 
		searchStatus.setTime(searchByTime);
		searchStatus.setLocationList(searchByLoc);
		searchStatus.setNop(searchByNop);
		
		searchStatus.setInviteSaved(false);
		searchStatus.setTobeSaved(false);
		
		saveList.add(searchStatus);
		
	}

	public List<RequestStatus> getSaveList() {
		return saveList;
	}

	public void setSaveList(List<RequestStatus> saveList) {
		this.saveList = saveList;
	}



}
