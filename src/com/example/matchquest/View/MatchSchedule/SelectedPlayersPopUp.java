package com.example.matchquest.View.MatchSchedule;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

import com.example.matchquest.R;
import com.example.matchquest.View.CommonActivityWithFragment;
import com.example.matchquest.View.CommonViewClass;
import com.example.matchquest.View.teams.PopUpAdpater;
import com.example.matchquest.View.teams.TeamEditActivity;
import com.example.matchquest.model.Players;
import com.example.matchquest.model.RequestStatus;

public class SelectedPlayersPopUp {

	PopupWindow selectedPlayersPopUp;
	
	
	RequestStatus requestStatus;
	ActionBarActivity context;
	TextView teamMemberCount;
	int totalCount;
	
	RecyclerView popUprecycleView;
	MatchDetailPopupAdapter popUpAdapter;
	
	public void showPopup(ActionBarActivity mcontext,RequestStatus status) 
	{
		this.context = mcontext;
		this.requestStatus = status;
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.selected_player_popup_layout,(ViewGroup) context.findViewById(R.id.popup_element));
		
		totalCount =requestStatus.getSelectedPlayersMap().size();
		teamMemberCount = (TextView) layout.findViewById(R.id.team_members_count_view);
		teamMemberCount.setText("("+ totalCount +"/" + totalCount + ")");
			Display display = context.getWindowManager().getDefaultDisplay();
			double width = display.getWidth();
			double height = display.getHeight();
			
			int heightSize = (int) ((height/4)*3);
			int widthSize = (int) ((width/5)*4);
		popUprecycleView = (RecyclerView) layout.findViewById(R.id.selectedplayer_popup_recycleview);
		popUprecycleView.setLayoutManager(new LinearLayoutManager(context));
		
		List<Players> playersList = new ArrayList<Players>();
		playersList.addAll(requestStatus.getSelectedPlayersMap().values());
		CommonViewClass.sortPlayersList(playersList);
		
		popUpAdapter = new MatchDetailPopupAdapter(context,playersList );
		popUprecycleView.setAdapter(popUpAdapter);
		
		selectedPlayersPopUp = new PopupWindow(context);
		selectedPlayersPopUp.setContentView(layout);
		selectedPlayersPopUp.setFocusable(true);
		selectedPlayersPopUp.setHeight(heightSize);
		selectedPlayersPopUp.setWidth(widthSize);

	       // Clear the default translucent background
//	       addMemberPopUp.setBackgroundDrawable(new BitmapDrawable());

		selectedPlayersPopUp.showAtLocation(layout, Gravity.CENTER, 0, 0);
		selectedPlayersPopUp.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				
				((CommonActivityWithFragment)context).getFrament().reGainLayout();				
			}
		});
	}
}
