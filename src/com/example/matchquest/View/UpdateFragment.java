package com.example.matchquest.View;

import com.example.matchquest.MainActivity;
import com.example.matchquest.R;

import android.app.Fragment;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.app.ActionBarActivity;

public class UpdateFragment  extends Fragment {
	
	private ListView updateList;
    private ArrayAdapter<String> updateAdapter;
    private ImageView navigationIcon;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		updateList = new ListView(getActivity());
//		LayoutInflater mInflater = LayoutInflater.from(getActivity());
//
//		View cutomActionBar = mInflater.inflate(R.layout.update_actionbar, null);
//		navigationIcon = (ImageView) cutomActionBar.findViewById(R.id.navigation_icon);
//		
//		((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("");
//		((ActionBarActivity)getActivity()).getSupportActionBar().setCustomView(cutomActionBar);
//		((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(true);
		addUpdatedItems();

		return updateList;
	}
	 private void addUpdatedItems() {
	        String[] osArray = { "Android", "iOS", "Windows", "OS X", "Linux" };
	        updateAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, osArray);
	        updateList.setAdapter(updateAdapter);

	        updateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                Toast.makeText(getActivity(), "Time for an upgrade!", Toast.LENGTH_SHORT).show();
	            }
	        });
	    }
	 
	 @Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
//		super.onCreateOptionsMenu(menu, inflater);
	}

}
