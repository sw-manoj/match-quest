package com.example.matchquest.View.teams;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.matchquest.R;
import com.example.matchquest.model.Contacts;

public class PopUpAdpater  extends RecyclerView.Adapter<PopUpAdpater.MyViewHolder> implements  android.widget.PopupMenu.OnMenuItemClickListener{

	List<Contacts> contactList ;
	
	Context mContext;
	LayoutInflater inflater;
	List<Contacts> modifiedContacts;
	Contacts contactPlayer;
	AddMemersPopUp popUpView;
	int totalCount;
	public PopUpAdpater(Context context,  List<Contacts> objects,AddMemersPopUp popUp,int count)
	{
		this.contactList = objects;
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.popUpView = popUp;
		modifiedContacts = new ArrayList<Contacts>();
		this.totalCount = count;
	}
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return contactList.size();
	}
	class MyViewHolder extends RecyclerView.ViewHolder {
		 TextView name;
		 TextView num;
		 CheckBox selected;
		public MyViewHolder(View itemView) {
			super(itemView);
			 int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
			  name = (TextView) itemView.findViewById(R.id.contactName);
			  num = (TextView) itemView.findViewById(R.id.contactNum);
			  selected = (CheckBox) itemView.findViewById(R.id.contact_checkbox);
			  selected.setButtonDrawable(id);
			  
			  selected.setOnClickListener(new OnClickListener() {
				
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
					if(modifiedContacts.size() > totalCount)
					{
						modifiedContacts.remove(c);
						cb.setChecked(false);	
					}
					c.setSelected(cb.isChecked());
					popUpView.setTeamMemberCount(modifiedContacts.size());
				}
			});
		  }
		
		}
		
	
	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		Contacts contact = contactList.get(position);
		 
		 holder.name.setText(contact.getName());
		 holder.num.setText("-"+contact.getNumber());
		 holder.selected.setChecked(contact.isSelected());
		 holder.selected.setTag(contact);
		
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
		
		 View view = inflater.inflate(R.layout.contacts_view, parent, false);
	     MyViewHolder holder = new MyViewHolder(view);
		return holder;
	}

	public List<Contacts> getContacts()
	{
		return modifiedContacts;
	}
}
