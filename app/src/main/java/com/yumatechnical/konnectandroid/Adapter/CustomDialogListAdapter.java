package com.yumatechnical.konnectandroid.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.yumatechnical.konnectandroid.Model.ListItem;
import com.yumatechnical.konnectandroid.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CustomDialogListAdapter extends ArrayAdapter<ListItem> implements Filterable {

	private List<ListItem> my_data/*, my_data_filtered*/;
	private int selectedPos = RecyclerView.NO_POSITION;
	private View lastSelected = null;
	static class ViewHolder {
		TextView textView1;
		ImageView imgView1;
		TextView textView2;
		ImageView imgView2;
		LinearLayout line;
	}
	private static final String TAG = CustomDialogListAdapter.class.getSimpleName();
	private LayoutInflater layoutInflater;

	public interface OnClickListener {
//		void OnSelectItem(int connectionID);
//		void OnSelectItem(String name);
		void OnSelectItem(ListItem item);
	}
	private final OnClickListener listener;


	public CustomDialogListAdapter(@NonNull Context context, int resource, @NonNull List<ListItem> objects,
	                               OnClickListener listener) {
		super(context, resource, objects);
		this.my_data = objects;
		this.listener = listener;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.inner_left, parent, false);
			holder.textView1 = convertView.findViewById(R.id.tv_left_1);
			holder.imgView1 = convertView.findViewById(R.id.iv_left_1);
			holder.textView2 = convertView.findViewById(R.id.tv_left_2);
			holder.imgView2 = convertView.findViewById(R.id.iv_left_2);
			holder.line = convertView.findViewById(R.id.ll_left_entry);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		ListItem entry = getItem(position);
		if (entry != null) {
			if (entry.getIconBeforeText() != null) {
				if (entry.getIconAsString() != null && !entry.getIconAsString().isEmpty()) {
					holder.textView1.setText(entry.getIconAsString());
				}
				if (entry.getDrawable() != null) {
					holder.imgView1.setImageDrawable(entry.getDrawable());
				}
				if (entry.getName() != null) {
					holder.textView2.setText(entry.getName());
					holder.textView2.setPadding(entry.getIconTextPadding(), 0, 0, 0);
				}
			} else {
				if (entry.getName() != null) {
					holder.textView1.setText(entry.getName());
					holder.textView1.setPadding(0, 0, entry.getIconTextPadding(), 0);
				}
				if (entry.getIconAsString() != null && !entry.getIconAsString().isEmpty()) {
					holder.textView2.setText(entry.getIconAsString());
				}
				if (entry.getDrawable() != null) {
					holder.imgView2.setImageDrawable(entry.getDrawable());
				}
			}
			holder.line.setPadding(entry.getLeftPadding(), entry.getTopPadding(), 0, entry.getBotPadding());
			if (entry.getFaded()) {
				holder.textView1.setTextColor(Color.LTGRAY);
				holder.textView2.setTextColor(Color.LTGRAY);
				holder.imgView1.setColorFilter(Color.LTGRAY);
				holder.imgView2.setColorFilter(Color.LTGRAY);
			}
		}
		convertView.setOnClickListener(v -> {
			selectedPos = position;
			ListItem clickedItem = my_data.get(position);
//			Log.d(TAG, "selected listitem="+ clickedItem.toString());
			v.setSelected(true);
			if (lastSelected != null) {
				lastSelected.setSelected(false);
			}
			if (position > -1) {
				lastSelected = v;
			}
			listener.OnSelectItem(clickedItem);
		});
		return convertView;
	}
/*
	@NonNull
//	@Override
	public View WASgetView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.inner_left,
					parent, false);
			final ViewHolder holder = new ViewHolder();
			holder.textView1 = view.findViewById(R.id.tv_left_1);
			holder.imgView1 = view.findViewById(R.id.iv_left_1);
			holder.textView2 = view.findViewById(R.id.tv_left_2);
			holder.imgView2 = view.findViewById(R.id.iv_left_2);
			holder.line = view.findViewById(R.id.ll_left_entry);
			view.setTag(holder);
		} else {
			view = convertView;
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		ListItem entry = getItem(position);
		if ((entry != null ? entry.getIconBeforeText() : false)) {
			if (entry.getIconAsString() != null && !entry.getIconAsString().isEmpty()) {
				holder.textView1.setText(entry.getIconAsString());
			}
			if (entry.getDrawable() != null) {
				holder.imgView1.setImageDrawable(entry.getDrawable());
			}
			if (entry.getName() != null) {
				holder.textView2.setText(entry.getName());
				holder.textView2.setPadding(entry.getIconTextPadding(), 0, 0, 0);
			}
		} else {
			if ((entry != null ? entry.getName() : null) != null) {
				holder.textView1.setText(entry.getName());
				holder.textView1.setPadding(0, 0, entry.getIconTextPadding(), 0);
			}
			if ((entry != null ? entry.getIconAsString() : null) != null && !entry.getIconAsString().isEmpty()) {
				holder.textView2.setText(entry.getIconAsString());
			}
			if (Objects.requireNonNull(entry).getDrawable() != null) {
				holder.imgView2.setImageDrawable(entry.getDrawable());
			}
		}
		view.setSelected(selectedPos == position);
		holder.line.setPadding(entry.getLeftPadding(), entry.getTopPadding(), 0, entry.getBotPadding());
		if (entry.getFaded()) {
			holder.textView1.setTextColor(Color.LTGRAY);
			holder.textView2.setTextColor(Color.LTGRAY);
			holder.imgView1.setColorFilter(Color.LTGRAY);
			holder.imgView2.setColorFilter(Color.LTGRAY);
		}
		view.setOnClickListener(v -> {
			selectedPos = position;
			ListItem clickedItem = my_data.get(position);
//			Log.d(TAG, "selected listitem="+ clickedItem.toString());
			v.setSelected(true);
			if (lastSelected != null) {
				lastSelected.setSelected(false);
			}
			if (position > -1) {
				lastSelected = v;
			}
			listener.OnSelectItem(clickedItem);
		});
		return view;
	}
*/
	@Override
	public long getItemId(int position) {
		if (position > 0 && my_data.size() > position)
			return position;
		return 0;
	}

	@Nullable
	@Override
	public ListItem getItem(int position) {
		if (position > -1 && my_data.size() > position)
			return my_data.get(position);
		return null;
	}

	@Override
	public int getPosition(@Nullable ListItem item) {
		int position = 0;
		for (ListItem listItem : my_data) {
			if (listItem.equals(item)) {
				return position;
			}
			++position;
		}
		return -1;
	}

	@Override
	public int getCount() {
		return my_data.size();
	}

	public void setData(ArrayList<ListItem> data) {
		my_data = data;
	}
/*
	@NonNull
	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				if (results != null && results.values != null) {
					my_data_filtered = (ArrayList<ListItem>) results.values;
				}
				else {
					my_data_filtered = my_data;//ApplicationGlobal._dataManager.get_ArraylistFull();
				}
				notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				ArrayList<ListItem> filteredList = new ArrayList<>();

				//Do your filtering operation with the constraint String
				//Return result for publishResults to use it
				results.count = filteredList.size();
				results.values = filteredList;

				return results;
			}
		};
		return filter;
	}
*/
}
