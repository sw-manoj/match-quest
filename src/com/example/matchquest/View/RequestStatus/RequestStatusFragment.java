package com.example.matchquest.View.RequestStatus;

import java.util.Map;
import java.util.WeakHashMap;

import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matchquest.R;
import com.example.matchquest.DataManipulation.RequestStatus.RequestStatusDM;
import com.example.matchquest.View.CommonFragment;
import com.example.matchquest.common.TeamQuestConstants;
import com.example.matchquest.model.RequestStatus;
import com.example.matchquest.model.Team;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

public class RequestStatusFragment extends CommonFragment{
	
	LayoutInflater inflater;
	ViewGroup container;
	View rootView;
	
	RecyclerView requestStatusRecyclerView;
	
	RequestStatusAdapter requestStatusAdapter;
	
	Map<Long,RequestStatus> requestStatusMap;
	
	TextView emptyView;
	
	ExpandableStickyListHeadersListView expandableStickyList;
	
	 WeakHashMap<View,Integer> mOriginalViewHeightPool = new WeakHashMap<View, Integer>();
	
	Team team;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		this.container = container;
		team = (Team) getArguments().getSerializable(TeamQuestConstants.team_key);
		
		rootView = inflater.inflate(R.layout.sticky_header_layout, container, false);
		emptyView = (TextView) rootView.findViewById(R.id.empty_view);
		
		RequestStatusDM requestStatusDetails = new RequestStatusDM();
					
		expandableStickyList = (ExpandableStickyListHeadersListView) rootView.findViewById(R.id.list);
		
		if(requestStatusMap == null || requestStatusMap.size() == 0)
		{
			expandableStickyList.setVisibility(View.GONE);
			emptyView.setVisibility(View.VISIBLE);
			
		}
		
//		StickyListHeadersAdapter adapter = new MyAdapter(this);
		requestStatusAdapter = new RequestStatusAdapter((RequestStatusActivity)getActivity(), requestStatusDetails.getRequestStatusChildList(), requestStatusMap,team,expandableStickyList);
	
		expandableStickyList.setAnimExecutor(new AnimationExecutor());
		expandableStickyList.setAdapter(requestStatusAdapter);
		expandableStickyList.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
		            @Override
		            public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
		                if(expandableStickyList.isHeaderCollapsed(headerId)){
		                    expandableStickyList.expand(headerId);
		                }else {
		                    expandableStickyList.collapse(headerId);
		                }
		            }

					
		        });
		
		registernetworkListener(true);
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
	@Override
	protected int loadInBackGround() {
		RequestStatusDM requestStatusDetails = new RequestStatusDM();
		
		requestStatusMap = requestStatusDetails.getRequestStatusDetails(team);
		requestStatusAdapter.setRequestStatusMap(requestStatusMap);
		requestStatusAdapter.setRequestStatusChildList(requestStatusDetails.getRequestStatusChildList());
		
		return 1;
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
			requestStatusAdapter.filterRequset(item.getItemId());
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void updateView() {

		if(requestStatusMap == null || requestStatusMap.size() == 0)
		{
			expandableStickyList.setVisibility(View.GONE);
			emptyView.setVisibility(View.VISIBLE);
		}else{
			expandableStickyList.setVisibility(View.VISIBLE);
			emptyView.setVisibility(View.GONE);
			requestStatusAdapter.notifyDataSetChanged();
		}
		
	}
	
	class AnimationExecutor implements ExpandableStickyListHeadersListView.IAnimationExecutor {

        @Override
        public void executeAnim(final View target, final int animType) {
            if(ExpandableStickyListHeadersListView.ANIMATION_EXPAND==animType&&target.getVisibility()==View.VISIBLE){
                return;
            }
            if(ExpandableStickyListHeadersListView.ANIMATION_COLLAPSE==animType&&target.getVisibility()!=View.VISIBLE){
                return;
            }
            if(mOriginalViewHeightPool.get(target)==null){
                mOriginalViewHeightPool.put(target,target.getHeight());
            }
            final int viewHeight = mOriginalViewHeightPool.get(target);
            float animStartY = animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND ? 0f : viewHeight;
            float animEndY = animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND ? viewHeight : 0f;
            final ViewGroup.LayoutParams lp = target.getLayoutParams();
            ValueAnimator animator = ValueAnimator.ofFloat(animStartY, animEndY);
            animator.setDuration(200);
            target.setVisibility(View.VISIBLE);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND) {
                        target.setVisibility(View.VISIBLE);
                    } else {
                        target.setVisibility(View.GONE);
                    }
                    target.getLayoutParams().height = viewHeight;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    lp.height = ((Float) valueAnimator.getAnimatedValue()).intValue();
                    target.setLayoutParams(lp);
                    target.requestLayout();
                }
            });
            animator.start();

        }
    }
}
