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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.yumatechnical.konnectandroid.Model.ListItem;
import com.yumatechnical.konnectandroid.R;

import java.util.ArrayList;
import java.util.Objects;

public class LeftArrayAdapter extends ArrayAdapter<ListItem> {

	private ArrayList<ListItem> my_data = new ArrayList<>();
	private int selectedPos = RecyclerView.NO_POSITION;
//	private static final String TAG = RightAdapter.class.getSimpleName();
	private View lastSelected = null;
	static class ViewHolder {
		TextView textView1;
		ImageView imgView1;
		TextView textView2;
		ImageView imgView2;
		LinearLayout line;
	}


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
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.inner_left, parent, false);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.textView1 = convertView.findViewById(R.id.tv_left_1);
			viewHolder.imgView1 = convertView.findViewById(R.id.iv_left_1);
			viewHolder.textView2 = convertView.findViewById(R.id.tv_left_2);
			viewHolder.imgView2 = convertView.findViewById(R.id.iv_left_2);
			viewHolder.line = convertView.findViewById(R.id.ll_left_entry);
			convertView.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
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
		convertView.setSelected(selectedPos == position);
		holder.line.setPadding(entry.getLeftPadding(), entry.getTopPadding(), 0, entry.getBotPadding());
		if (entry.getFaded()) {
			holder.textView1.setTextColor(Color.LTGRAY);
			holder.textView2.setTextColor(Color.LTGRAY);
			holder.imgView1.setColorFilter(Color.LTGRAY);
			holder.imgView2.setColorFilter(Color.LTGRAY);
		}
		convertView.setTag(entry);
		convertView.setOnClickListener(v -> {
			selectedPos = position;
			v.setSelected(true);
			if (lastSelected != null) {
				lastSelected.setSelected(false);
			}
			if (position > -1) {
				lastSelected = v;
			}
			ListItem clickedItem = (ListItem) v.getTag();
			if (!clickedItem.getFaded()) {
				listener.OnClickItem(clickedItem.getName());
			} else {
				new AlertDialog.Builder(getContext())
						.setMessage(R.string.item_disabled)
//							.setPositiveButton("OK", null)
						.setNegativeButton("Cancel", null)
						.create()
						.show()
				;
			}
		});
		return convertView;
	}


	public void setData(ArrayList<ListItem> myData) {
		my_data = myData;
		notifyDataSetChanged();
	}

}
