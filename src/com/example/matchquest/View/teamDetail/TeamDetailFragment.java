package com.example.matchquest.View.teamDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matchquest.R;
import com.example.matchquest.DataManipulation.Teams.TeamEditDM;
import com.example.matchquest.DataManipulation.topicDetail.TopicDetailDM;
import com.example.matchquest.SQLiteData.TeamQuestSqlite;
import com.example.matchquest.View.CommonFragment;
import com.example.matchquest.View.CommonViewClass;
import com.example.matchquest.View.NetworkChangeReceiver;
import com.example.matchquest.View.FindOpponent.FindOpponentActivity;
import com.example.matchquest.View.MatchSchedule.MatchScheduleActivity;
import com.example.matchquest.View.RequestStatus.RequestStatusActivity;
import com.example.matchquest.View.ReusableViews.SaveTopicDetailInterface;
import com.example.matchquest.View.teamDetail.AsyncTask.SaveTopicDetailTask;
import com.example.matchquest.View.teams.TeamEditActivity;
import com.example.matchquest.common.Details;
import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.model.Players;
import com.example.matchquest.model.Team;
import com.example.matchquest.model.TopicDetails;

public class TeamDetailFragment extends CommonFragment implements SaveTopicDetailInterface {

	static Team team;
	View rootView;
	ProgressDialog mProgressDialog;
	boolean toUpdate;
	ImageView expandIcon;
	ImageView collapseIcon;
	View seperator;
	LinearLayout nextGameLayout;
	
	ImageView generalExpandIcon;
	ImageView generalCollapseIcon;
	View generalSeperator;
	LinearLayout generalLayout;
	Button addTopic;
	
	View nextGameView;
	LinearLayout nextgameTopicLayout;
	TextView topicQues;
	ImageView topicIcon;
	
	View nextGameOptionsView;
	TextView topicOption;
	TextView topicOptionDetail;
	CheckBox optionCheckBox;
	
	View generalView;
	LinearLayout generalTopicLayout;
	TextView generalTopicQues;
	ImageView generalTopicIcon;
	
	View generalOptionsView;
	TextView generalTopicOption;
	CheckBox generalOptionCheckBox;
	
	LayoutInflater inflater;
	ViewGroup container;
	
	boolean initalLoad = false;
	
	NetworkChangeReceiver networkChangeReceiver ;
	List<TopicDetails> topicDetailList ;
	Map<String,TopicDetails> topicDetailSaveMap ;
	TeamQuestSqlite teamDetailSqlite;
	
	Button findOppButton;
	Button matchScheduleButton;
	Button requestStatusButton;
	
	TopicDetails selectedTopicDetail;
	TopicDetails selectedSaveTopicDetail;
	LinearLayout selectedoptionDetailLayout;
	TextView selectedTextView;
	String selectedPlayerId;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		this.inflater = inflater;
		this.container = container;
		team = (Team) getArguments().getSerializable(TeamQuestConstants.team_key);
		toUpdate = getArguments().getBoolean(TeamQuestConstants.toupdate_key,false);
		rootView = inflater.inflate(R.layout.team_detail_fragment_layout, container, false);
		
		expandIcon = (ImageView) rootView.findViewById(R.id.expand_icon_team_detail);
		collapseIcon = (ImageView) rootView.findViewById(R.id.collapse_icon_team_detail);
		seperator = rootView.findViewById(R.id.seperatorView_nextGame);
		nextGameLayout = (LinearLayout) rootView.findViewById(R.id.nextgame_view_team_detail);
		
		generalExpandIcon = (ImageView) rootView.findViewById(R.id.expand_icon_general_team_detail);
		generalCollapseIcon = (ImageView) rootView.findViewById(R.id.collapse_icon_general_team_detail);
		generalSeperator = rootView.findViewById(R.id.seperatorView_general);
		generalLayout = (LinearLayout) rootView.findViewById(R.id.general_view_team_detail);
		addTopic = (Button) rootView.findViewById(R.id.addtopic_team_detail);
		addTopic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent teamEditIntent = new Intent(getActivity(), AddTopicActivity.class);
				teamEditIntent.putExtra("Team", team);  
				teamEditIntent.putExtra(TeamQuestConstants.toupdate_key, false);
				teamEditIntent.putExtra(TeamQuestConstants.screenName_key, TeamQuestConstants.teamDetailScreen_key);
				startActivity(teamEditIntent);
			}
		});
		
		  
		nextGameIconListeners();
		generalIconListeners();
		
		teamOptionListeners();
		
		topicDetailList = new ArrayList<TopicDetails>();
		topicDetailSaveMap = new HashMap<String, TopicDetails>();
		
		teamDetailSqlite = new TeamQuestSqlite(getActivity());
		topicDetailList.clear();
		topicDetailList.addAll(teamDetailSqlite.getTeamDetailSqlite().getTopicDetails(team.getTeamId(),team));
		topicDetailSaveMap.clear();
		updateView();
		
		registernetworkListener(false);
		initalLoad = true;
		
		return rootView;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(!initalLoad)
		{
			if(getNetworkChangeReceiver()!= null)
			{
				getNetworkChangeReceiver().updateData();
			}
			
		}
		initalLoad = false;
		
	}

	private void teamOptionListeners()
	{
		findOppButton = (Button) rootView.findViewById(R.id.findoppo_button_team_detail);
		findOppButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent findOpponentIntent = new Intent(getActivityObject(), FindOpponentActivity.class); 
				findOpponentIntent.putExtra(TeamQuestConstants.team_key, team);  
				findOpponentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(findOpponentIntent);
			}
		});
		
		matchScheduleButton = (Button) rootView.findViewById(R.id.matchStatus_button_team_detail);
		matchScheduleButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent matchScheduleIntent = new Intent(getActivityObject(), MatchScheduleActivity.class); 
				matchScheduleIntent.putExtra(TeamQuestConstants.team_key, team);  
				matchScheduleIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(matchScheduleIntent);
			}
		});
		
		requestStatusButton = (Button) rootView.findViewById(R.id.requestStatus_button_team_detail);
		requestStatusButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent requestStatusIntent = new Intent(getActivityObject(), RequestStatusActivity.class); 
				requestStatusIntent.putExtra(TeamQuestConstants.team_key, team);  
				requestStatusIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(requestStatusIntent);
			}
		});
	}
	private void nextGameIconListeners()
	{
		expandIcon.setVisibility(View.GONE);
		nextGameLayout.setVisibility(View.VISIBLE);
		collapseIcon.setVisibility(View.VISIBLE);
		seperator.setVisibility(View.VISIBLE);
		expandIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				expandIcon.setVisibility(View.GONE);
				collapseIcon.setVisibility(View.VISIBLE);
				seperator.setVisibility(View.VISIBLE);
				nextGameLayout.setVisibility(View.VISIBLE);
			}
		});
		collapseIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				expandIcon.setVisibility(View.VISIBLE);
				collapseIcon.setVisibility(View.GONE);
				seperator.setVisibility(View.GONE);
				nextGameLayout.setVisibility(View.GONE);
			}
		});
	}

	private void generalIconListeners()
	{
		generalExpandIcon.setVisibility(View.GONE);
		generalLayout.setVisibility(View.VISIBLE);
		generalCollapseIcon.setVisibility(View.VISIBLE);
		generalSeperator.setVisibility(View.VISIBLE);
		generalExpandIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				generalExpandIcon.setVisibility(View.GONE);
				generalCollapseIcon.setVisibility(View.VISIBLE);
				generalSeperator.setVisibility(View.VISIBLE);
				generalLayout.setVisibility(View.VISIBLE);
			}
		});
		generalCollapseIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				generalExpandIcon.setVisibility(View.VISIBLE);
				generalCollapseIcon.setVisibility(View.GONE);
				generalSeperator.setVisibility(View.GONE);
				generalLayout.setVisibility(View.GONE);
			}
		});
	}
	private void addNextGameDetails(List<TopicDetails> topicDetailList)
	{
		nextGameLayout.removeAllViews();
		int i = 0;
		for( final TopicDetails topicDetail : topicDetailList)
		{
			if(topicDetail.getCategory().equals(TeamQuestConstants.nextgame_key))
			{
				final TopicDetails saveTopicDetail = new TopicDetails();
				saveTopicDetail.setUniqueId(topicDetail.getUniqueId());
				saveTopicDetail.setCategory(topicDetail.getCategory());
				saveTopicDetail.setTopicId(topicDetail.getTopicId());
			nextGameView = inflater.inflate(R.layout.topic_detail_layout, null);
			nextgameTopicLayout = (LinearLayout) nextGameView.findViewById(R.id.topic_detail_layout_id);
			
			ImageView nextGameTopicIcon = (ImageView) nextGameView.findViewById(R.id.topic_detail_icon);
			nextGameTopicIcon.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent teamDetailIntent = new Intent(getActivity(), TopicDetailActivity.class);
					teamDetailIntent.putExtra(TeamQuestConstants.team_key, team); 
					teamDetailIntent.putExtra(TeamQuestConstants.screenName_key, TeamQuestConstants.teamDetailScreen_key);
					teamDetailIntent.putExtra(TeamQuestConstants.topicDetail_key, topicDetail);
					startActivity(teamDetailIntent);
					
				}
			});
			
			final TextView topicAns;

			topicQues = (TextView) nextGameView.findViewById(R.id.topic_ques);
			topicAns = (TextView) nextGameView.findViewById(R.id.topic_ans);
			topicIcon = (ImageView) nextGameView.findViewById(R.id.topic_detail_icon);
			
			final LinearLayout nextgameOptionDetailLayout = (LinearLayout) 
					nextGameView.findViewById(R.id.topic_option_layout);
			
			final Button nextGameSaveOption = (Button) nextGameView.findViewById(R.id.topic_options_save);
			nextGameSaveOption.setEnabled(CommonViewClass.isNetworkAvailable(getActivity()));
			nextGameSaveOption.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					TopicDetailDM saveOption = new TopicDetailDM();
//					TopicDetails savedTopicDetail = saveOption.saveDetail(topicDetail, saveTopicDetail, Details.getPlayer(getActivityObject()).getPlayerId());
//					topicAns.setText(savedTopicDetail.getOption());
//					saveTopicDetail.getOptionOriginal().clear();
//					saveTopicDetail.getOptionOriginal().putAll(saveTopicDetail.getOptionModified());
//					topicDetail.setOptions(savedTopicDetail.getOptions());
//					topicDetail.setOptionIds(savedTopicDetail.getOptionIds());
//					nextgameOptionDetailLayout.setVisibility(View.GONE);
//					Toast.makeText(getActivity(), "saved", 1000).show();
					saveTopicDetailDb(topicDetail, saveTopicDetail, Details.getPlayer(getActivityObject()).getPlayerId(), topicAns, nextgameOptionDetailLayout);
				}
			});
			
			nextgameTopicLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(nextgameOptionDetailLayout.getVisibility() == View.GONE)
					{
						if(topicDetail.getOptions()!= null && topicDetail.getOptions().size() > 0)
						{
						nextgameOptionDetailLayout.removeViews(0, nextgameOptionDetailLayout.getChildCount()-1);
						loadOptionView(topicDetail, saveTopicDetail, nextGameSaveOption, nextgameOptionDetailLayout);
						nextgameOptionDetailLayout.setVisibility(View.VISIBLE);
						}
					}else{
						if(nextGameSaveOption.isEnabled())
						{
							AlertDialog alert = CommonViewClass.showdialogTwoButton(getActivity(), "Changes are not saved,click ok to continue without saving");
							alert.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
								    new DialogInterface.OnClickListener() {
								        public void onClick(DialogInterface dialog, int which) {
								            dialog.dismiss();
								            nextgameOptionDetailLayout.setVisibility(View.GONE);
								        }
								    });
							alert.show();
						}else{
							nextgameOptionDetailLayout.setVisibility(View.GONE);
						}
					}
				}
			});
			topicQues.setText(topicDetail.getTopic());
			topicAns.setText(topicDetail.getOption());
			nextGameLayout.addView(nextGameView);
			
			nextgameOptionDetailLayout.setVisibility(View.GONE);
			
			topicDetailSaveMap.put(topicDetail.getTopicId(), saveTopicDetail);

			i++;
			}
		}
		
	}
	
	private void saveTopicDetailDb( TopicDetails topicDetail,  TopicDetails saveTopicDetail,
			 String playerId, TextView topicAns, LinearLayout optionDetailLayout)
	{
		if(!CommonViewClass.isNetworkAvailable(getActivityObject()))
		{
			CommonViewClass.showdialog(getActivity(), TeamQuestConstants.connectToInternet_key);
			return;
		}
		selectedTopicDetail = topicDetail;
		selectedoptionDetailLayout = optionDetailLayout;
		selectedSaveTopicDetail = saveTopicDetail;
		selectedTextView = topicAns;
		selectedPlayerId = playerId;
		
		
		
		new SaveTopicDetailTask(this).execute();
	}
	
	
	private void addGeneralDetails(List<TopicDetails> topicDetailList) {
		
		generalLayout.removeViews(0, generalLayout.getChildCount() -1);
		int i = 0;
		for(final TopicDetails topicDetail : topicDetailList)
		{
			if(topicDetail.getCategory().equals(TeamQuestConstants.general_key))
			{
				final TopicDetails saveTopicDetail = new TopicDetails();
				saveTopicDetail.setUniqueId(topicDetail.getUniqueId());
				saveTopicDetail.setCategory(topicDetail.getCategory());
				saveTopicDetail.setTopicId(topicDetail.getTopicId());
				
				final TextView generalTopicAns;

			generalView = inflater.inflate(R.layout.topic_detail_layout, null);
			generalTopicLayout = (LinearLayout) generalView.findViewById(R.id.topic_detail_layout_id);
			generalTopicQues = (TextView) generalView.findViewById(R.id.topic_ques);
			generalTopicAns = (TextView) generalView.findViewById(R.id.topic_ans);
			generalTopicIcon = (ImageView) generalView.findViewById(R.id.topic_detail_icon);
			generalTopicIcon.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent teamDetailIntent = new Intent(getActivity(), TopicDetailActivity.class);
					teamDetailIntent.putExtra(TeamQuestConstants.team_key, team);  
					teamDetailIntent.putExtra(TeamQuestConstants.screenName_key, TeamQuestConstants.teamDetailScreen_key);  
					teamDetailIntent.putExtra(TeamQuestConstants.topicDetail_key, topicDetail);
					startActivity(teamDetailIntent);
					
				}
			});
			
			final LinearLayout generalOptionDetailLayout = (LinearLayout) 
					generalView.findViewById(R.id.topic_option_layout);
			
			final Button generalSaveOption = (Button) generalView.findViewById(R.id.topic_options_save);
			generalSaveOption.setEnabled(CommonViewClass.isNetworkAvailable(getActivity()));
			generalSaveOption.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					TopicDetailDM saveOption = new TopicDetailDM();
//					TopicDetails savedTopicDetail = saveOption.saveDetail(topicDetail, saveTopicDetail, Details.getPlayer(getActivityObject()).getPlayerId());
//					generalTopicAns.setText(savedTopicDetail.getOption());
//					saveTopicDetail.getOptionOriginal().clear();
//					saveTopicDetail.getOptionOriginal().putAll(saveTopicDetail.getOptionModified());
//					topicDetail.setOptions(savedTopicDetail.getOptions());
//					topicDetail.setOptionIds(savedTopicDetail.getOptionIds());
//					generalOptionDetailLayout.setVisibility(View.GONE);
//					Toast.makeText(getActivity(), "saved", 1000).show();
					saveTopicDetailDb(topicDetail, saveTopicDetail, Details.getPlayer(getActivityObject()).getPlayerId(), generalTopicAns, generalOptionDetailLayout);
					
				}
			});
			
			generalTopicLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(generalOptionDetailLayout.getVisibility() == View.GONE)
					{
						if(topicDetail.getOptions() != null && topicDetail.getOptions().size() > 0)
						{
						generalOptionDetailLayout.removeViews(0, generalOptionDetailLayout.getChildCount() -1);
						loadOptionView(topicDetail, saveTopicDetail, generalSaveOption, generalOptionDetailLayout);
						generalOptionDetailLayout.setVisibility(View.VISIBLE);
						}
					}else{

						if(generalSaveOption.isEnabled())
						{
							AlertDialog alert = CommonViewClass.showdialogTwoButton(getActivity(), "Changes are not saved,click ok to continue without saving");
							alert.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
								    new DialogInterface.OnClickListener() {
								        public void onClick(DialogInterface dialog, int which) {
								            dialog.dismiss();
								            generalOptionDetailLayout.setVisibility(View.GONE);
								        }
								    });
							alert.show();
						}else{
							generalOptionDetailLayout.setVisibility(View.GONE);
						}
					
					}
				}
			});
			generalTopicQues.setText(topicDetail.getTopic());
			generalTopicAns.setText(topicDetail.getOption());
			generalLayout.addView(generalView,i);
			
			generalOptionDetailLayout.setVisibility(View.GONE);
			
			topicDetailSaveMap.put(topicDetail.getTopicId(), saveTopicDetail);
			i++;
			}
		}
		
	
	}
	
	public void loadOptionView(final TopicDetails topicDetail, TopicDetails saveTopicDetail
			,final Button generalSaveOption,LinearLayout generalOptionDetailLayout)
	{
		generalSaveOption.setEnabled(false);

		int j = 0;
		for(final Entry<String, String> entry : topicDetail.getOptionIds().entrySet())
		{
		generalOptionsView =inflater.inflate(R.layout.topic_option_layout, null);
		generalTopicOption = (TextView) generalOptionsView.findViewById(R.id.topic_options);
		final TextView generalTopicOptionDetail = (TextView) generalOptionsView.findViewById(R.id.topic_options_detail);
		
		generalOptionCheckBox = (CheckBox) generalOptionsView.findViewById(R.id.topic_options_checkbox);
		generalOptionCheckBox.setEnabled(CommonViewClass.isNetworkAvailable(getActivity()));
		generalOptionCheckBox.setTag(entry.getKey());
		generalOptionCheckBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				boolean flag = false;
				String optionId = (String) cb.getTag();
				if(cb.isChecked())
				{
					if(topicDetailSaveMap.get(topicDetail.getTopicId()).getOptionOriginal().get(optionId).equals(Boolean.TRUE))
					{
						generalTopicOptionDetail.setText((topicDetail.getOptions().get(entry.getKey()).size() )+ " players choosed this");						
					}else{
						generalTopicOptionDetail.setText((topicDetail.getOptions().get(entry.getKey()).size()+1 )+ " players choosed this");
					}
				}else{
					if(topicDetailSaveMap.get(topicDetail.getTopicId()).getOptionOriginal().get(optionId).equals(Boolean.TRUE))
					{
						generalTopicOptionDetail.setText((topicDetail.getOptions().get(entry.getKey()).size() -1)+ " players choosed this");						
					}else{
						generalTopicOptionDetail.setText((topicDetail.getOptions().get(entry.getKey()).size())+ " players choosed this");
					}				
				}
				topicDetailSaveMap.get(topicDetail.getTopicId()).getOptionModified().put( optionId, cb.isChecked());
				for(Entry<String, Boolean> optionEntry :topicDetailSaveMap.get(topicDetail.getTopicId()).getOptionModified().entrySet())
				{
					if(!optionEntry.getValue().equals(topicDetailSaveMap.get(topicDetail.getTopicId()).getOptionOriginal().get(optionEntry.getKey())))
					{
						flag = true;
						break;
					}
				}
				
				generalSaveOption.setEnabled(flag);
				
				
			}
		});
		saveTopicDetail.getOptionModified().put(entry.getKey(), false);
		saveTopicDetail.getOptionOriginal().put(entry.getKey(), false);
		saveTopicDetail.getOptions().put(entry.getKey(), topicDetail.getOptions().get(entry.getKey()));
		for(Players player : topicDetail.getOptions().get(entry.getKey()))
		{
			if(player.getPlayerId().equals(Details.getPlayer(getActivityObject()).getPlayerId()))
			{
				generalOptionCheckBox.setChecked(true);

				saveTopicDetail.getOptionModified().put(entry.getKey(), true);
				saveTopicDetail.getOptionOriginal().put(entry.getKey(), true);
				break;
			}
		}
		
		
		generalTopicOption.setText(entry.getValue());
		generalTopicOptionDetail.setText(topicDetail.getOptions().get(entry.getKey()).size() + " players choosed this");
		generalOptionDetailLayout.addView(generalOptionsView,j);
		j++;
		}
	}
	@Override
	protected int loadInBackGround() {
		TopicDetailDM topicDetails = new TopicDetailDM();
		
		List<TopicDetails> topicDetail = topicDetails.getTopicDetail(team.getTopicIds(),team.getTeamId());
		if(topicDetail !=  null)
		{
			topicDetailList.clear();
			topicDetailList.addAll(topicDetail);
			topicDetailSaveMap.clear();
			teamDetailSqlite.getTeamDetailSqlite().insertTopicDetail(team.getTeamId(), topicDetailList);
			
			return 1;
		}else{
			return -1;
		}
		
		
	}

	@Override
	public void updateView() {
		addNextGameDetails(topicDetailList);
		addGeneralDetails(topicDetailList);

		
	}

	public TopicDetails getSelectedTopicDetail() {
		return selectedTopicDetail;
	}

	public void setSelectedTopicDetail(TopicDetails selectedTopicDetail) {
		this.selectedTopicDetail = selectedTopicDetail;
	}

	public TopicDetails getSelectedSaveTopicDetail() {
		return selectedSaveTopicDetail;
	}

	public void setSelectedSaveTopicDetail(TopicDetails selectedSaveTopicDetail) {
		this.selectedSaveTopicDetail = selectedSaveTopicDetail;
	}

	public LinearLayout getSelectedoptionDetailLayout() {
		return selectedoptionDetailLayout;
	}

	public void setSelectedoptionDetailLayout(
			LinearLayout selectedoptionDetailLayout) {
		this.selectedoptionDetailLayout = selectedoptionDetailLayout;
	}

	public TextView getSelectedTextView() {
		return selectedTextView;
	}

	public void setSelectedTextView(TextView selectedTextView) {
		this.selectedTextView = selectedTextView;
	}

	public String getSelectedPlayerId() {
		return selectedPlayerId;
	}

	public void setSelectedPlayerId(String selectedPlayerId) {
		this.selectedPlayerId = selectedPlayerId;
	}

	public static Team getTeam() {
		return team;
	}

	public static void setTeam(Team team) {
		TeamDetailFragment.team = team;
	}
	
}
