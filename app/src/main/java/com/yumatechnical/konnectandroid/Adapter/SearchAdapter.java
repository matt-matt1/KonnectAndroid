package com.yumatechnical.konnectandroid.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yumatechnical.konnectandroid.Model.ItemObject;
import com.yumatechnical.konnectandroid.R;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private ArrayList<String> listItemStorage;
//	private List<ItemObject> listItemStorage;


	public SearchAdapter(Context context, ArrayList<String> items) {
		layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		listItemStorage = items;
	}
//	public SearchAdapter(Context context, List<ItemObject> customizedListView) {
//		layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		listItemStorage = customizedListView;
//	}

	@Override
	public int getCount() {
		return listItemStorage.size();
	}

	@Override
	public Object getItem(int position) {
		return listItemStorage.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

	/*
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder listViewHolder;
			if (convertView == null) {
				listViewHolder = new ViewHolder();
				convertView = layoutInflater.inflate(R.layout.list_item, parent, false);
				listViewHolder.dictionaryWord = convertView.findViewById(R.id.list_item_search);
				convertView.setTag(listViewHolder);
			} else {
				listViewHolder = (ViewHolder)convertView.getTag();
			}
	//		listViewHolder.dictionaryWord.setText(listItemStorage.get(position).getWord());
			return convertView;
		}
	*/
	static class ViewHolder{
		TextView dictionaryWord;
	}
}
