package com.example.matchquest.View;

import com.example.matchquest.R;
import com.example.matchquest.common.TeamQuestConstants;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

public abstract class CommonActivityWithFragment  extends ActionBarActivity {
	
	public abstract CommonFragment getFrament();
	
	
	 private DrawerLayout mDrawerLayout;
	    private ActionBarDrawerToggle mDrawerToggle;
	    private String mActivityTitle;
	    int newWidth;
	    FragmentTransaction fragmentTransaction;
	    SlidingUpPanelLayout slidingLayout;
	     
	

	    protected void activitySetUp()
	    {
	    	DisplayMetrics displaymetrics = new DisplayMetrics();
	        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
	        
	        int height = displaymetrics.heightPixels;
	        int width = displaymetrics.widthPixels;
	        
	        slidingLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
	        setSlidingPanelListener();
	        newWidth=width/2;
	        mActivityTitle = getSupportActionBar().getTitle().toString();
	        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

	        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	        getSupportActionBar().setHomeButtonEnabled(true);
	        
	    }
	    
	    
	    protected void loadFragment(Fragment fragment,int resourceId)
	    {
	    	if( fragment != null)
	    	{
	    		
	    	fragmentTransaction = getFragmentManager().beginTransaction();
	    	
	        fragmentTransaction.replace(resourceId,  fragment );
	        fragmentTransaction.commit();
	    	}
	    }
	    
	    protected void drawerLayoutSetUp()
	    {

	        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
					
	            /** Called when a drawer has settled in a completely open state. */
	            public void onDrawerOpened(View drawerView) {
	                super.onDrawerOpened(drawerView);
	                
	                getSupportActionBar().setTitle("Match Schedule");
	               
	            }
	            
	            /** Called when a drawer has settled in a completely closed state. */
	            public void onDrawerClosed(View view) {
	                super.onDrawerClosed(view);
	               
	                getSupportActionBar().setTitle(mActivityTitle);
	               
	            }
	            
//	            public boolean onOptionsItemSelected(MenuItem item) {
//	                if (item != null && item.getItemId() == android.R.id.home) {
//	                    if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
//	                    	
//	                        mDrawerLayout.closeDrawer(Gravity.RIGHT);
//	                    } else {
//	                    	
//	                        mDrawerLayout.openDrawer(Gravity.RIGHT);
//	                    }
//	                }
//	                return false;
//	            }
	        };
	        
	        mDrawerToggle.setDrawerIndicatorEnabled(true);
	        mDrawerLayout.setDrawerListener(mDrawerToggle);
	        
	    
	    }
	    
	    @Override
	    protected void onPostCreate(Bundle savedInstanceState) {
	        super.onPostCreate(savedInstanceState);
	        // Sync the toggle state after onRestoreInstanceState has occurred.
	        mDrawerToggle.syncState();
	    }

	    @Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        mDrawerToggle.onConfigurationChanged(newConfig);
	    }
	    
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	
	    	 int id = item.getItemId();
	         if (id == R.id.updatesMenu) {
	         	if(!slidingLayout.getPanelState().equals(PanelState.EXPANDED))
	         	{
	 	             slidingLayout.setPanelState(PanelState.EXPANDED);
	         	}else{
	             	 slidingLayout.setPanelState(PanelState.COLLAPSED);
	         	}
	         	
	             return true;
	         }else
	        	 
	    	// Activate the navigation drawer toggle
	        if (mDrawerToggle.onOptionsItemSelected(item)) {
	            return true;
	        }
	    	return false;
	    }
	    
	    protected ActionBarDrawerToggle getDrawerToggle()
	    {
	    	return mDrawerToggle;
	    }
	    private void setSlidingPanelListener() 
	    {
	    	slidingLayout.setPanelSlideListener(new PanelSlideListener() {
				
				@Override
				public void onPanelSlide(View panel, float slideOffset) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onPanelHidden(View panel) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onPanelExpanded(View panel) {
					if(mDrawerLayout.isDrawerOpen(Gravity.RIGHT))
	 	        	{
	 	        		mDrawerLayout.closeDrawer(Gravity.RIGHT);
	 	        	}
	 	        	 getSupportActionBar().setDisplayHomeAsUpEnabled(false);
	 	             getSupportActionBar().setHomeButtonEnabled(false);
	 	             mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
	 	             getSupportActionBar().setTitle(mActivityTitle);
				}
				
				@Override
				public void onPanelCollapsed(View panel) {
					getSupportActionBar().setDisplayShowCustomEnabled(false);
	             	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	                getSupportActionBar().setHomeButtonEnabled(true);
	             	getSupportActionBar().setTitle(mActivityTitle);
	             	mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
					
				}
				
				@Override
				public void onPanelAnchored(View panel) {
					// TODO Auto-generated method stub
					
				}
			});
	    }
}
