package com.example.matchquest.View.RequestStatus;

import java.util.ArrayList;
import java.util.List;

import com.example.matchquest.R;
import com.example.matchquest.model.Team;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class MyAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private List<Team> teams = new ArrayList<Team>();
    private LayoutInflater inflater;

    public MyAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        
        for(int i = 0 ;i < 30 ; i ++)
        {
        	Team t = new Team();
        	if(i/10 < 1)
            	{
            	t.setTeamId("11111");
            	t.setTeamName("teamName" + i);
            	t.setTeamCode("code66" + i);
            	}else if(i/10 < 2)
            	{
            		t.setTeamId("22222");
                	t.setTeamName("teamName" + i);
                	t.setTeamCode("code55" + i);
            	}else if(i/10 < 3)
            	{
            		t.setTeamId("33333");
                	t.setTeamName("teamName" + i);
                	t.setTeamCode("code44" + i);
            	}
        	teams.add(t);
        }
    }

    @Override
    public int getCount() {
        return teams.size();
    }

    @Override
    public Object getItem(int position) {
        return teams.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.sticky_list_item_layout, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(teams.get(position).getTeamName());

        return convertView;
    }

    @Override 
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.sticky_header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set header text as first char in name
        String headerText = teams.get(position).getTeamCode();
        holder.text.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {    	
        return Long.parseLong(teams.get(position).getTeamId());
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView text;
    }

}