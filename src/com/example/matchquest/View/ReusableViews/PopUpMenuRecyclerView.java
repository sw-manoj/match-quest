package com.example.matchquest.View.ReusableViews;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.View;
import android.widget.PopupMenu;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class PopUpMenuRecyclerView extends PopupMenu{

	RecyclerView recyclerView;
	int menuId;
	int selectedPos;
	int startPos;
	int endPos;
	
	public PopUpMenuRecyclerView(Context context, View anchor,int menuId,int selectedPos,RecyclerView recyclerView) {
		
		super(context, anchor);
		this.menuId = menuId;
		this.selectedPos = selectedPos;
		this.recyclerView = recyclerView;
		MenuInflater inflator = getMenuInflater();
      	inflator.inflate(menuId, getMenu());
		
	}
	
	@Override
	public void show() {
		startPos = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
		endPos = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
		
      	
      	for(int k = 0 ; k <= (endPos-startPos) ; k ++)
		{
			if(k+startPos!= selectedPos)
			{
				final View child = ((LinearLayoutManager)recyclerView.getLayoutManager()).getChildAt(k);
				if (child != null) {
					child.clearAnimation();
					child.setAlpha(0.3f);
				}
			}
		}
      	setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(PopupMenu menu) {
				for(int k = 0 ; k <= (endPos-startPos) ; k ++)
				{
					if(k+startPos != selectedPos)
					{
						final View child = ((LinearLayoutManager)recyclerView.getLayoutManager()).getChildAt(k);
						if (child != null) {
							child.clearAnimation();
							child.setAlpha(1);
						}
					}
				}
				
			}
		});
		super.show();
	}
	
	
}
