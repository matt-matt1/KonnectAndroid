package com.yumatechnical.konnectandroid.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yumatechnical.konnectandroid.Model.MyContact;
import com.yumatechnical.konnectandroid.R;

import java.util.ArrayList;

public class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.ContactsViewHolder> {

	private static final String TAG = ContactsRecyclerAdapter.class.getSimpleName();
	private ArrayList<MyContact> contactList;


	public interface TapListener {
		void OnClick(int index, int contactId);
	}
	private TapListener listener;

	public ContactsRecyclerAdapter(ArrayList<MyContact> contacts, TapListener listener) {
		contactList = contacts;
		this.listener = listener;
	}

	class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		private TextView tv_name;


		public ContactsViewHolder(View itemView) {
			super(itemView);
			tv_name = itemView.findViewById(R.id.contact_name);
			itemView.setOnClickListener(this);
		}

		void bind(String name) {
			tv_name.setText(name);
		}

		@Override
		public void onClick(View v) {
			Log.d(TAG, tv_name.getText().toString());
//			if (v != null) {
//				listener.OnClick(getAdapterPosition(), v.getId());
//			}
		}
	}

	@NonNull
	@Override
	public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		int layoutIdForListItem = R.layout.contact_item;
		LayoutInflater inflater = LayoutInflater.from(context);
		boolean shouldAttachToParentImmediately = false;
		View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
		return new ContactsViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
		MyContact contact = contactList.get(position);
		holder.bind(contact.getName());
//		Log.d(TAG, contact.getName() + ":" + contact.getHas_number());
		if (!contact.getHas_number()) {
			holder.tv_name.setTextColor(Color.LTGRAY);
		}
	}

	@Override
	public int getItemCount() {
		return contactList.size();
	}
}
