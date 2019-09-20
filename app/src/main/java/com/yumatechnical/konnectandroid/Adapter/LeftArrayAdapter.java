package com.yumatechnical.konnectandroid.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yumatechnical.konnectandroid.Model.ListItem;
import com.yumatechnical.konnectandroid.R;

import java.util.ArrayList;
import java.util.Objects;

public class LeftArrayAdapter extends ArrayAdapter<ListItem> {

	ArrayList<ListItem> my_data = new ArrayList<>();


	public interface OnClickListener {
		void OnClickItem(String name);
	}
	private final OnClickListener listener;
	public LeftArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ListItem> objects, OnClickListener listener) {
		super(context, resource, objects);
		this.listener = listener;
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		ListItem entry = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.inner_left, parent, false);
		}
		TextView textView1 = convertView.findViewById(R.id.tv_left_1);
		ImageView imgView1 = convertView.findViewById(R.id.iv_left_1);
		TextView textView2 = convertView.findViewById(R.id.tv_left_2);
		ImageView imgView2 = convertView.findViewById(R.id.iv_left_2);
		LinearLayout linout = convertView.findViewById(R.id.ll_left_root);
		LinearLayout line = convertView.findViewById(R.id.ll_left_entry);
		if ((entry != null ? entry.getIconBeforeText() : false)) {
			if (entry.getIconAsString() != null && !entry.getIconAsString().isEmpty()) {
				textView1.setText(entry.getIconAsString());
			}
			if (entry.getDrawable() != null) {
				imgView1.setImageDrawable(entry.getDrawable());
			}
			if (entry.getName() != null) {
				textView2.setText(entry.getName());
				textView2.setPadding(entry.getIconTextPadding(), 0, 0, 0);
			}
		} else {
			if ((entry != null ? entry.getName() : null) != null) {
				textView1.setText(entry.getName());
				textView1.setPadding(0, 0, entry.getIconTextPadding(), 0);
			}
			if ((entry != null ? entry.getIconAsString() : null) != null && !entry.getIconAsString().isEmpty()) {
				textView2.setText(entry.getIconAsString());
			}
			if (Objects.requireNonNull(entry).getDrawable() != null) {
				imgView2.setImageDrawable(entry.getDrawable());
			}
		}
//		itemView.setSelected(selectedPos == position);
		line.setPadding(0, entry.getTopPadding(), 0, entry.getBotPadding());
		linout.setPadding(entry.getLeftPadding(), 0, 0, 0);
		if (entry.getFaded()) {
			textView1.setTextColor(Color.LTGRAY);
			textView2.setTextColor(Color.LTGRAY);
			imgView1.setColorFilter(Color.LTGRAY);
			imgView2.setColorFilter(Color.LTGRAY);
		}
		convertView.setTag(entry);
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ListItem clickedItem = (ListItem) v.getTag();
				if (!clickedItem.getFaded()) {
					listener.OnClickItem(clickedItem.getName());
				} else {
					;
				}
			}
		});
		return convertView;
	}

	public void setData(ArrayList<ListItem> myData) {
		my_data = myData;
		notifyDataSetChanged();
	}

}
