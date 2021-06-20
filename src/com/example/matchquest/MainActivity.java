package com.example.matchquest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.matchquest.View.CommonActivityWithFragment;
import com.example.matchquest.View.CommonFragment;
import com.example.matchquest.View.CommonViewClass;
import com.example.matchquest.View.UpdateFragment;
import com.example.matchquest.View.Drawerlayout.MatchStatusFragment;
import com.example.matchquest.View.teams.TeamCreationActivity;
import com.example.matchquest.View.teams.TeamListFragment;
import com.example.matchquest.common.Details;
import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.model.Players;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


public class MainActivity extends CommonActivityWithFragment  {

	String playerId;
   
    TeamListFragment teamsDisplayFragment;
    MatchStatusFragment matchStatus;
    SharedPreferences prefs;
    UpdateFragment updateFragment;
     
    SlidingUpPanelLayout slidingLayout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		prefs = getSharedPreferences(TeamQuestConstants.teamQuest_key, MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        activitySetUp();
        CommonViewClass.setContext(getApplicationContext());
		matchStatus= new MatchStatusFragment();
        loadFragment(matchStatus, R.id.drawerlayout);
        
		updateFragment= new UpdateFragment();
		loadFragment(updateFragment,R.id.update_container);

        drawerLayoutSetUp();

        playerId =  prefs.getString("PlayerNo", null);
        	
	   		Bundle bundle = new Bundle();
	   		bundle.putString("playerNo", playerId);
	   		teamsDisplayFragment = new TeamListFragment();
	   		teamsDisplayFragment.setArguments(bundle);
	   		loadFragment(teamsDisplayFragment, R.id.container);

     
    }
    
    @Override
    protected void onStart() {
    	
         if(prefs.getString("PlayerNo", null) == null)
         {
        	 Intent registerScreen = new Intent(getApplicationContext(),Register.class);
        	 startActivity(registerScreen);
         }
         else{
        	 Players player = new Players();
        	player.setPlayerId(prefs.getString(TeamQuestConstants.playerId_key, null));
        	player.setPlayerName(prefs.getString(TeamQuestConstants.playerName_key, null));
        	player.setRegistered(true);
        	Details.setPlayer(player);
         }
         super.onStart();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
        getMenuInflater().inflate(R.menu.main, menu);
//        menu.findItem(R.id.addTeaamMenu).setIcon(resizeImage(R.drawable.addmem,600,300));
    	
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       
        int id = item.getItemId();
         if (id == R.id.addTeaamMenu)
        {
        	if(!CommonViewClass.isNetworkAvailable(getApplicationContext()))
			{
        		CommonViewClass.showdialog(MainActivity.this, "Please ,Connect to internet to create new team");
			}else{
        	Intent teamCreation = new Intent(this, TeamCreationActivity.class);
        	teamCreation.putExtra("playerNo", playerId);
        	startActivity(teamCreation);
			}
        }
     
        return super.onOptionsItemSelected(item);
    }
    
    private Drawable resizeImage(int resId, int w, int h)
    {
          // load the origial Bitmap
          Bitmap BitmapOrg = BitmapFactory.decodeResource(getResources(), resId);
          int width = BitmapOrg.getWidth();
          int height = BitmapOrg.getHeight();
          int newWidth = w;
          int newHeight = h;
          // calculate the scale
          float scaleWidth = ((float) newWidth) / width;
          float scaleHeight = ((float) newHeight) / height;
          // create a matrix for the manipulation
          Matrix matrix = new Matrix();
          matrix.postScale(scaleWidth, scaleHeight);
          Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0,width, height, matrix, true);
          return new BitmapDrawable(resizedBitmap);
    }

	@Override
	public CommonFragment getFrament() {
		// TODO Auto-generated method stub
		return teamsDisplayFragment;
	}
  
    
}
