package com.example.matchquest.View.FindOpponent;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

public class AutoCompleteAdapter extends ArrayAdapter<String>{

	Filter filter;
	
	MultiAutoCompleteTextView autoCompleteView;
	
	List<String> orginalList = new ArrayList<String>();
	
	private List<String> adapterList = new ArrayList<String>();
	
	List<String> locationList = new ArrayList<String>();
	
	Context context;
	int res;
	
	public AutoCompleteAdapter(Context context, int resource,MultiAutoCompleteTextView autoCompleteView,List<String> orgList) {
		super(context, resource);
		this.autoCompleteView = autoCompleteView;
		this.orginalList = orgList;
		this.res = resource;
		this.context = context;
		adapterList.addAll(orginalList);
		formFilter();
		
	}
	
	@Override
	public View getView (int position, View convertView, ViewGroup parent)
	{
	View view;
    TextView text ;

    if (convertView == null) {
        view = LayoutInflater.from(context).inflate(res, parent, false);
    } else {
        view = convertView;
    }
    text = (TextView) view;
	
	String item = getItem(position);
    if (item instanceof CharSequence) {
        text.setText((CharSequence)item);
    } else {
        text.setText(item.toString());
    }

    return view;
	}

	@Override
	public int getCount ()
	{
	return adapterList.size();
	}

	@Override
	public String getItem (int position)
	{
	return adapterList.get(position);
	}
	
	private void formFilter()
	{
		filter = new Filter() {
			
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				
				if (results != null && results.count > 0)
				{
					adapterList.clear();
					adapterList.addAll((List<String>) results.values);
					notifyDataSetChanged();
				} else{
					notifyDataSetInvalidated();
				}
				
		
				
			}
			
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults fr = new FilterResults();
				populateAutoCompleteAdapter();
				if(constraint != null && !constraint.toString().equals(""))
				{
					int count = locationList.size();
					final ArrayList<String> newValues = new ArrayList<String>();
					for(int i = 0 ; i < count ; i++)
					{
						if(locationList.get(i).toLowerCase().startsWith(constraint.toString().toLowerCase()))
						{
							newValues.add(locationList.get(i));
						}
					}
					locationList.clear();
					locationList.addAll(newValues);
				}
				fr.count = locationList.size();
				fr.values = locationList;
				return fr;
			}
		};
	}
	
	private void populateAutoCompleteAdapter()
	{

		String lastAutoCompleteText = autoCompleteView.getText().toString();
		if(lastAutoCompleteText != null && !lastAutoCompleteText.trim().equals(""))
		{
			locationList.clear();
			locationList.addAll(orginalList);
		String[] arr = lastAutoCompleteText.split(",");
		for(int i=0;i< arr.length;i++){
			if(locationList.remove(arr[i].trim())){
				
			}else{
				for(int j = 0 ; j < locationList.size() ; j ++ )
				{
					if (arr[i].trim().contains(locationList.get(j))) {
						locationList.remove(j);
					}
				}
			}
		}
		}
		
	
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return filter;
	}
}
