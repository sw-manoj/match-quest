package com.example.matchquest.View.MatchSchedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.matchquest.R;
import com.example.matchquest.DataManipulation.topicDetail.TopicDetailDM;
import com.example.matchquest.SQLiteData.TeamQuestSqlite;
import com.example.matchquest.View.CommonFragment;
import com.example.matchquest.View.CommonViewClass;
import com.example.matchquest.View.NetworkChangeReceiver;
import com.example.matchquest.View.MatchSchedule.AsyncTask.SavePlayerOpinion;
import com.example.matchquest.View.ReusableViews.SavePlayerOpinionInterface;
import com.example.matchquest.View.ReusableViews.SaveTopicDetailInterface;
import com.example.matchquest.View.teamDetail.AddTopicActivity;
import com.example.matchquest.View.teamDetail.TopicDetailActivity;
import com.example.matchquest.View.teamDetail.AsyncTask.SaveTopicDetailTask;
import com.example.matchquest.common.Details;
import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.model.Players;
import com.example.matchquest.model.RequestStatus;
import com.example.matchquest.model.Team;
import com.example.matchquest.model.TopicDetails;

public class MatchDetailFragment extends CommonFragment implements SaveTopicDetailInterface ,SavePlayerOpinionInterface{

	RequestStatus requestStatus;
	Team team;
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
	
	Button interestedButton;
	Button notInterestedButton;
	Button choosePlayersForMatchButton;
	Button selectedPlayersForMatchButton;
	
	NetworkChangeReceiver networkChangeReceiver ;
	List<TopicDetails> topicDetailList ;
	Map<String,TopicDetails> topicDetailSaveMap ;
	TeamQuestSqlite teamDetailSqlite;
	
	private TopicDetails selectedTopicDetail;
	private LinearLayout selectedoptionDetailLayout;
	private TopicDetails selectedSaveTopicDetail;
	private TextView selectedTextView;
	private String selectedPlayerId;
	
	private RequestStatus selectedRequestStatus;
	private RequestStatus toSaveRequestStatus;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, android.os.Bundle savedInstanceState) {
		this.inflater = inflater;
		this.container = container;
		requestStatus = (RequestStatus) getArguments().getSerializable(TeamQuestConstants.requestStatus_key);
		team = (Team) getArguments().getSerializable(TeamQuestConstants.team_key);
		
		toUpdate = getArguments().getBoolean(TeamQuestConstants.toupdate_key,false);
		rootView = inflater.inflate(R.layout.match_detail_fragment_layout, container, false);
		
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
				Intent topicEditIntent = new Intent(getActivity(), AddTopicActivity.class);
				topicEditIntent.putExtra(TeamQuestConstants.requestStatus_key, requestStatus);  
				topicEditIntent.putExtra(TeamQuestConstants.toupdate_key, false);
				topicEditIntent.putExtra(TeamQuestConstants.screenName_key, TeamQuestConstants.matchDetailScreen_key);
				startActivity(topicEditIntent);
			}
		});
		
		choosePlayersForMatchButton = (Button) rootView.findViewById(R.id.players_for_match_button_match_detail);
		selectedPlayersForMatchButton = (Button) rootView.findViewById(R.id.selectedplayers_for_match_button_match_detail);
		
		interestedButton = (Button) rootView.findViewById(R.id.interested_button_match_detail);
		notInterestedButton = (Button) rootView.findViewById(R.id.noninterested_button_match_detail);
		
		nextGameIconListeners();
		generalIconListeners();
		
		topicDetailList = new ArrayList<TopicDetails>();
		topicDetailSaveMap = new HashMap<String, TopicDetails>();
		
		teamDetailSqlite = new TeamQuestSqlite(getActivity());
		topicDetailList.clear();
		topicDetailList.addAll(teamDetailSqlite.getTeamDetailSqlite().getTopicDetails(requestStatus.getUniqueId(),team));
		topicDetailSaveMap.clear();
		updateView();
		
		registernetworkListener(false);
		initalLoad = true;

		
		return rootView;
	};
	
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
	
	@Override
	public void reGainLayout() {
		
		rootView.setAlpha(1);
	
	}
	private void selectMatchPlayersButtonSetup()
	{
		
			if(team.getCaptain().equals(Details.getPlayer(getActivityObject()).getPlayerId()) || team.getViceCaptain().equals(Details.getPlayer(getActivityObject()).getPlayerId()))
			{
				choosePlayersForMatchButton.setVisibility(View.VISIBLE);
			}
			
			if(requestStatus.getSelectedPlayersMap() != null && requestStatus.getSelectedPlayersMap().size() > 0)
			{
				selectedPlayersForMatchButton.setVisibility(View.VISIBLE);
			}
						
			choosePlayersForMatchButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MatchPlayersPopUp matchDetailPopUp = new MatchPlayersPopUp();
					matchDetailPopUp.showPopup(getActivityObject(), team, requestStatus);
					rootView.setAlpha(0.3f);
				}
			});
			
			selectedPlayersForMatchButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					SelectedPlayersPopUp selectedPlayerPopUp = new SelectedPlayersPopUp();
					selectedPlayerPopUp.showPopup(getActivityObject(),  requestStatus);
					rootView.setAlpha(0.3f);
				}
			});
		
	}
	private void interestButtonSetUp()
	{
		
		boolean isLiked = false;
		for(Players player :requestStatus.getPlayersList())
		{
			if(player.getPlayerId().equals(Details.getPlayer(getActivityObject()).getPlayerId()))
			{
			isLiked = true;
			break;
			}
		}
		if(isLiked)
		{
			interestedButton.setVisibility(View.GONE);
			notInterestedButton.setVisibility(View.VISIBLE);
		}else{
			notInterestedButton.setVisibility(View.GONE);
			interestedButton.setVisibility(View.VISIBLE);
		}
		
		interestedButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(CommonViewClass.isNetworkAvailable(getActivity()))
				{
					
					savePlayerOpinion(requestStatus, false);
				}else{
					CommonViewClass.showdialog(getActivity(), TeamQuestConstants.connectToInternet_key);
				}
				
				
			}
		});
		
		notInterestedButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(CommonViewClass.isNetworkAvailable(getActivity()))
				{
					savePlayerOpinion(requestStatus, true);
				}else{
					CommonViewClass.showdialog(getActivity(), TeamQuestConstants.connectToInternet_key);
				}
				
				
			}
		});
	}
	
	 private void savePlayerOpinion(RequestStatus status,boolean toRemove)
	    {	    	
	    	selectedRequestStatus = status;
	   				
			toSaveRequestStatus = new RequestStatus();
			toSaveRequestStatus.setUniqueId(status.getUniqueId());
			toSaveRequestStatus.setToRemove(toRemove);
			toSaveRequestStatus.setTeamId(team.getTeamId());
			toSaveRequestStatus.setPlayerId(Details.getPlayer(getActivityObject()).getPlayerId());
				
			new SavePlayerOpinion(this).execute();
	    }
	 
	 public void postSavePlayerOpinion(boolean toRemove)
	 {
		 	notInterestedButton.setVisibility(toRemove ? View.GONE : View.VISIBLE);
			interestedButton.setVisibility(toRemove ? View.VISIBLE : View.GONE);
			if(toRemove)
			{
				for(Iterator<Players> it = selectedRequestStatus.getPlayersList().iterator();it.hasNext(); )
				{
					Players player = it.next();
					if(player.getPlayerId().equals(Details.getPlayer(getActivityObject()).getPlayerId()))
					{
						it.remove();
						break;
					}
				}
			}else{
				selectedRequestStatus.getPlayersList().add(Details.getPlayer(getActivityObject()));
			}
	 }
	 
	//next game in this class represents match detail section
	private void addNextGameDetails(List<TopicDetails> topicDetailList)
	{
		nextGameLayout.removeAllViews();
		int i = 0;
		for( final TopicDetails topicDetail : topicDetailList)
		{
			if(topicDetail.getCategory().equals(TeamQuestConstants.matchdetail_key))
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
					teamDetailIntent.putExtra(TeamQuestConstants.screenName_key, TeamQuestConstants.matchDetailScreen_key);
					teamDetailIntent.putExtra(TeamQuestConstants.requestStatus_key, requestStatus);  
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
					teamDetailIntent.putExtra(TeamQuestConstants.screenName_key, TeamQuestConstants.matchDetailScreen_key);
					teamDetailIntent.putExtra(TeamQuestConstants.requestStatus_key, requestStatus);  
					teamDetailIntent.putExtra(TeamQuestConstants.topicDetail_key, topicDetail);
					startActivity(teamDetailIntent);
					
				}
			});
			
			final LinearLayout generalOptionDetailLayout = (LinearLayout) 
					generalView.findViewById(R.id.topic_option_layout);
			
			final Button generalSaveOption = (Button) generalView.findViewById(R.id.topic_options_save);
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
	
	@Override
	protected int loadInBackGround() {
		TopicDetailDM topicDetails = new TopicDetailDM();
		List<TopicDetails> topicDetail = topicDetails.getTopicDetail(requestStatus.getTopicIds(),requestStatus.getUniqueId());
		if(topicDetail != null)
		{
		topicDetailList.clear();
		topicDetailList.addAll(topicDetail);
		topicDetailSaveMap.clear();
		teamDetailSqlite.getTeamDetailSqlite().insertTopicDetail(requestStatus.getUniqueId(), topicDetailList);
		return 1;
		}
		return -1;
	}

	@Override
	public void updateView() {
		addNextGameDetails(topicDetailList);
		addGeneralDetails(topicDetailList);
		
		interestButtonSetUp();
		selectMatchPlayersButtonSetup();  
		
	}

	public Button getSelectedPlayersForMatchButton() {
		return selectedPlayersForMatchButton;
	}

	public void setSelectedPlayersForMatchButton(
			Button selectedPlayersForMatchButton) {
		this.selectedPlayersForMatchButton = selectedPlayersForMatchButton;
	}

	public TopicDetails getSelectedTopicDetail() {
		return selectedTopicDetail;
	}

	public void setSelectedTopicDetail(TopicDetails selectedTopicDetail) {
		this.selectedTopicDetail = selectedTopicDetail;
	}

	public LinearLayout getSelectedoptionDetailLayout() {
		return selectedoptionDetailLayout;
	}

	public void setSelectedoptionDetailLayout(
			LinearLayout selectedoptionDetailLayout) {
		this.selectedoptionDetailLayout = selectedoptionDetailLayout;
	}

	public TopicDetails getSelectedSaveTopicDetail() {
		return selectedSaveTopicDetail;
	}

	public void setSelectedSaveTopicDetail(TopicDetails selectedSaveTopicDetail) {
		this.selectedSaveTopicDetail = selectedSaveTopicDetail;
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

	public RequestStatus getSelectedRequestStatus() {
		return selectedRequestStatus;
	}

	public void setSelectedRequestStatus(RequestStatus selectedRequestStatus) {
		this.selectedRequestStatus = selectedRequestStatus;
	}

	public RequestStatus getToSaveRequestStatus() {
		return toSaveRequestStatus;
	}

	public void setToSaveRequestStatus(RequestStatus toSaveRequestStatus) {
		this.toSaveRequestStatus = toSaveRequestStatus;
	}


}
