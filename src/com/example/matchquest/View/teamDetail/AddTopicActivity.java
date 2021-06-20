package com.example.matchquest.View.teamDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.matchquest.R;
import com.example.matchquest.DataManipulation.topicDetail.TopicDetailDM;
import com.example.matchquest.View.CommonFragment;
import com.example.matchquest.View.CommonViewClass;
import com.example.matchquest.View.teamDetail.AsyncTask.AddTopicTask;
import com.example.matchquest.View.teamDetail.AsyncTask.UpdateTopicTask;
import com.example.matchquest.common.Details;
import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.model.Players;
import com.example.matchquest.model.RequestStatus;
import com.example.matchquest.model.Team;
import com.example.matchquest.model.TopicDetails;

public class AddTopicActivity extends ActionBarActivity{
	
	Players player;
	Team team;
	RequestStatus requestStatus;
	TopicDetails topicDetail;
	TextView addTopicTopicCount;
	EditText addTopicTopicText;
	String totalCount;
	
	TextView addTopicOptionACount;
	EditText addTopicOptionAText;
	
	TextView addTopicOptionBCount;
	EditText addTopicOptionBText;
	
	TextView addTopicOptionCCount;
	EditText addTopicOptionCText;
	
	TextView addTopicOptionDCount;
	EditText addTopicOptionDText;
	
	Button addTopicButton;
	
	boolean toUpdate;
	
	String screen;
	
	String selectedPlayerId;
	String selectedUniqueId;
	String selectedTopic;
	String selectedScreen;
	List<String> selectedOptions;
	TopicDetails selectedTopicDetail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_topic_layout);
		player = Details.getPlayer(this);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
        toUpdate = getIntent().getBooleanExtra(TeamQuestConstants.toupdate_key, false);
        screen = getIntent().getStringExtra(TeamQuestConstants.screenName_key);
        
        if(screen.equals(TeamQuestConstants.teamDetailScreen_key))
        {
        	team =  (Team) getIntent().getSerializableExtra("Team");
        	
        	if(team != null)
            {
            	Details.setTeam(team);
            }else{
            	team = Details.getTeam();
            }
        	
        	
        }else if(screen.equals(TeamQuestConstants.matchDetailScreen_key))
        {
        	requestStatus = (RequestStatus) getIntent().getSerializableExtra(TeamQuestConstants.requestStatus_key);
        }
        	
        if(toUpdate)
        {
        	topicDetail = (TopicDetails) getIntent().getSerializableExtra(TeamQuestConstants.topicDetail_key);
        }
        
        if(screen.equals(TeamQuestConstants.teamDetailScreen_key))
        {
        	getSupportActionBar().setTitle(team.getTeamName());
        }else if(screen.equals(TeamQuestConstants.matchDetailScreen_key))
        {
        	getSupportActionBar().setTitle(requestStatus.getTeamName());
        }
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		addTopicButton = (Button) findViewById(R.id.create_add_topic);
		addTopicButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				
				createTopic();
				
			}
		});
		
		totalCount = getApplicationContext().getString(R.string.add_topic_count);
		
		addTopicTopicCount = (TextView) findViewById(R.id.add_topic_topic_count);
		addTopicTopicCount.setText("(0/" + totalCount +")" );
		
		addTopicTopicText = (EditText) findViewById(R.id.add_topic_edittext);
		
		setTextChangeListener(addTopicTopicText,addTopicTopicCount);
		
		addTopicOptionACount = (TextView) findViewById(R.id.add_topic_optiona_count);
		addTopicOptionACount.setText("(0/" + totalCount +")" );
		
		addTopicOptionAText = (EditText) findViewById(R.id.add_topic_optiona_edittext);
		setTextChangeListener(addTopicOptionAText,addTopicOptionACount);
		
		addTopicOptionBCount = (TextView) findViewById(R.id.add_topic_optionb_count);
		addTopicOptionBCount.setText("(0/" + totalCount +")" );
		
		addTopicOptionBText = (EditText) findViewById(R.id.add_topic_optionb_edittext);
		setTextChangeListener(addTopicOptionBText,addTopicOptionBCount);
		
		addTopicOptionCCount = (TextView) findViewById(R.id.add_topic_optionc_count);
		addTopicOptionCCount.setText("(0/" + totalCount +")" );
		
		addTopicOptionCText = (EditText) findViewById(R.id.add_topic_optionc_edittext);
		setTextChangeListener(addTopicOptionCText,addTopicOptionCCount);
		
		addTopicOptionDCount = (TextView) findViewById(R.id.add_topic_optiond_count);
		addTopicOptionDCount.setText("(0/" + totalCount +")" );
		
		addTopicOptionDText = (EditText) findViewById(R.id.add_topic_optiond_edittext);
		setTextChangeListener(addTopicOptionDText,addTopicOptionDCount);
		
		if(toUpdate)
		{
			loadFields();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case android.R.id.home:
	    	
	        finish();
	        return true;
	}
	    return false;
	}
	private void loadFields()
	{
		addTopicOptionAText.setTag("optionA");
		addTopicOptionBText.setTag("optionB");
		addTopicOptionCText.setTag("optionC");
		addTopicOptionDText.setTag("optionD");
		
		addTopicButton.setText("Update");
		addTopicTopicText.setText(topicDetail.getTopic());
		addTopicTopicCount.setText("("+topicDetail.getTopic().length()+"/" + totalCount +")" );
		addTopicTopicText.setEnabled(false);
		
		int i = 1;
		for(Entry<String, String> entry : topicDetail.getOptionIds().entrySet())
		{
			if(i == 1)
			{
				addTopicOptionAText.setText(entry.getValue());
				addTopicOptionAText.setTag(entry.getKey());
				addTopicOptionACount.setText("("+entry.getValue().length()+"/" + totalCount +")" );
				
			}else if(i ==2)
			{
				addTopicOptionBText.setText(entry.getValue());
				addTopicOptionBText.setTag(entry.getKey());
				addTopicOptionBCount.setText("("+entry.getValue().length()+"/" + totalCount +")" );
				
			}else if (i ==3)
			{
				addTopicOptionCText.setText(entry.getValue());
				addTopicOptionCText.setTag(entry.getKey());
				addTopicOptionCCount.setText("("+entry.getValue().length()+"/" + totalCount +")" );
				
			}else if (i ==4)
			{
				addTopicOptionDText.setText(entry.getValue());
				addTopicOptionDText.setTag(entry.getKey());
				addTopicOptionDCount.setText("("+entry.getValue().length()+"/" + totalCount +")" );
				
			}
			i++;
		}
		
		if((topicDetail.getCategory().equals(TeamQuestConstants.general_key)) && (team.getCaptain().equals(player.getPlayerId()) || team.getViceCaptain().equals(player.getPlayerId())  || topicDetail.getCreatedBy().equals(player.getPlayerId())))
		{
			addTopicTopicText.setEnabled(true);
		}
		
	}

	private void setTextChangeListener(final EditText editText , final TextView textCount)
	{
		editText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				textCount.setText("(" + editText.getText().toString().length() + "/" + totalCount + ")");					
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				
			}
			
		});
	}
	
	private boolean createTopic()
	{
		if(toUpdate)
		{
			return updateTopic();
		}
		if(!CommonViewClass.isNetworkAvailable(getApplicationContext()))
		{
			CommonViewClass.showdialog(AddTopicActivity.this, "Please, Connect to internet !!!");
			return false;
		}
		List<String> optionsList = new ArrayList<String>();
		List<String> options = new ArrayList<String>();
		String topic = addTopicTopicText.getText().toString().trim();
		if(topic == null || topic.equals(""))
		{
			CommonViewClass.showdialog(AddTopicActivity.this, "topic can't be empty");
			return false;
		}
		optionsList.add(addTopicOptionAText.getText().toString().trim());
		optionsList.add(addTopicOptionBText.getText().toString().trim());
		optionsList.add(addTopicOptionCText.getText().toString().trim());
		optionsList.add(addTopicOptionDText.getText().toString().trim());
		
		for(String option : optionsList)
		{
			if(option != null && !option.equals(""))
			{
				options.add(option);
			}
		}
		if(options.size() < 2)
		{
			CommonViewClass.showdialog(AddTopicActivity.this, "Atleast two options must be filled");
			return false;
		}
		String uniqueId = null;
		if(screen.equals(TeamQuestConstants.teamDetailScreen_key))
        {
			uniqueId = team.getTeamId();
        }else if(screen.equals(TeamQuestConstants.matchDetailScreen_key))
        {
        	uniqueId = requestStatus.getUniqueId();
        }
				selectedUniqueId = uniqueId;
				selectedPlayerId = Details.getPlayer(AddTopicActivity.this).getPlayerId();
				selectedOptions = options;
				selectedTopic = topic;
				selectedScreen = screen;
				new AddTopicTask(this).execute();
		return true;
	}
	
	private boolean updateTopic()
	{
		if(!CommonViewClass.isNetworkAvailable(getApplicationContext()))
		{
			CommonViewClass.showdialog(AddTopicActivity.this, "Please, Connect to internet !!!");
			return false;
		}
		Map<String,String> optionsList = new HashMap<String,String>();
		Map<String,String> options = new HashMap<String,String>();
		String topic = addTopicTopicText.getText().toString().trim();
		if(topic == null || topic.equals(""))
		{
			CommonViewClass.showdialog(AddTopicActivity.this, "topic can't be empty");
			return false;
		}
		optionsList.put(addTopicOptionAText.getTag().toString(),addTopicOptionAText.getText().toString().trim());
		optionsList.put(addTopicOptionBText.getTag().toString(),addTopicOptionBText.getText().toString().trim());
		optionsList.put(addTopicOptionCText.getTag().toString(),addTopicOptionCText.getText().toString().trim());
		optionsList.put(addTopicOptionDText.getTag().toString(),addTopicOptionDText.getText().toString().trim());
		
		for(Entry<String, String> option : optionsList.entrySet())
		{
			if(option.getValue() != null && !option.getValue().equals(""))
			{
				options.put(option.getKey(), option.getValue());
			}
		}
		if(options.size() < 2)
		{
			CommonViewClass.showdialog(AddTopicActivity.this, "Atleast two options must be filled");
			return false;
		}
		
		TopicDetails updateTopicDetail = new TopicDetails(); 
		if(screen.equals(TeamQuestConstants.teamDetailScreen_key))
        {
        	updateTopicDetail.setUniqueId(team.getTeamId());
        }else if(screen.equals(TeamQuestConstants.matchDetailScreen_key))
        {
        	updateTopicDetail.setUniqueId(requestStatus.getUniqueId());
        }
		updateTopicDetail.setTopic(topic);
		updateTopicDetail.setTopicId(topicDetail.getTopicId());
		updateTopicDetail.setOptionIds(options);
		
		selectedTopicDetail = updateTopicDetail;
		new UpdateTopicTask(this).execute();
		return true;
	}

	public String getSelectedPlayerId() {
		return selectedPlayerId;
	}

	public void setSelectedPlayerId(String selectedPlayerId) {
		this.selectedPlayerId = selectedPlayerId;
	}

	public String getSelectedUniqueId() {
		return selectedUniqueId;
	}

	public void setSelectedUniqueId(String selectedUniqueId) {
		this.selectedUniqueId = selectedUniqueId;
	}

	public String getSelectedTopic() {
		return selectedTopic;
	}

	public void setSelectedTopic(String selectedTopic) {
		this.selectedTopic = selectedTopic;
	}

	public String getSelectedScreen() {
		return selectedScreen;
	}

	public void setSelectedScreen(String selectedScreen) {
		this.selectedScreen = selectedScreen;
	}

	public List<String> getSelectedOptions() {
		return selectedOptions;
	}

	public void setSelectedOptions(List<String> selectedOptions) {
		this.selectedOptions = selectedOptions;
	}

	public TopicDetails getSelectedTopicDetail() {
		return selectedTopicDetail;
	}

	public void setSelectedTopicDetail(TopicDetails selectedTopicDetail) {
		this.selectedTopicDetail = selectedTopicDetail;
	}
}
