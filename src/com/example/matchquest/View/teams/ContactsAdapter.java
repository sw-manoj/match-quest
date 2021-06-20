package com.example.matchquest.View.teams;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matchquest.R;
import com.example.matchquest.model.Contacts;

public class ContactsAdapter extends ArrayAdapter<Contacts>{
	
	List<Contacts> contacts ;
	
	Context mContext;
	
	TextView contactName;
	
	TextView contactNum;
	
	CheckBox contactIsSelected;
	
	List<Contacts> modifiedContacts;
	TeamCreationActivity teamCreationActivity;

	public ContactsAdapter(Context context,  List<Contacts> objects,TeamCreationActivity teamCreation) {
		super(context, R.layout.contacts_view, objects);
		this.contacts = objects;
		this.mContext = context;
		this.teamCreationActivity = teamCreation;
		modifiedContacts = new ArrayList<Contacts>();
	}
	 private class ViewHolder {
		   TextView name;
		   TextView num;
		   CheckBox selected;
		  }

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		 if(convertView ==null)
		  {
			  LayoutInflater inflater = LayoutInflater.from(mContext);
			  convertView = inflater.inflate(R.layout.contacts_view, null);
			  
			  holder = new ViewHolder();
			  int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
			  holder.name = (TextView) convertView.findViewById(R.id.contactName);
			  holder.num = (TextView) convertView.findViewById(R.id.contactNum);
			  holder.selected = (CheckBox) convertView.findViewById(R.id.contact_checkbox);
			  holder.selected.setButtonDrawable(id);
			  
			  holder.selected.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					Contacts c = (Contacts) cb.getTag();
					if(cb.isChecked())
					{
						modifiedContacts.add(c);
					}
					else{
						modifiedContacts.remove(c);
					}
					if(modifiedContacts.size() > Integer.parseInt(mContext.getString(R.string.team_member_count)))
					{
						modifiedContacts.remove(c);
						cb.setChecked(false);	
					}
					c.setSelected(cb.isChecked());
					teamCreationActivity.setTeamMemberLimit(modifiedContacts.size() + "");
				}
			});
			  convertView.setTag(holder);
		  }
		 else{
			 holder = (ViewHolder) convertView.getTag();
		 }
		 
		 Contacts contact = contacts.get(position);
		 
		 holder.name.setText(contact.getName());
		 holder.num.setText("-"+contact.getNumber());
		 holder.selected.setChecked(contact.isSelected());
		 holder.selected.setTag(contact);
		 
		 
		
		return convertView; 
				
	}
	
	public List<Contacts> getModifiedContacts()
	{
		return modifiedContacts;
	}
	
	public int searchContact(String text)
	{
		int position = 0;
		for(Contacts c: contacts)
		{
			if(c.getName().toUpperCase().contains(text.toUpperCase()))
			{
				return getPosition(c);
			}
		}
		
		return position;
	}

}

