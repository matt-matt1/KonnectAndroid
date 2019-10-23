package com.yumatechnical.konnectandroid.Adapter;
//inuse
import android.content.Context;
import android.graphics.Color;
//import android.view.ActionMode;
import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.yumatechnical.konnectandroid.Model.ListItem;
import com.yumatechnical.konnectandroid.R;

import java.util.ArrayList;
import java.util.Objects;


public class LeftArrayAdapter extends ArrayAdapter<ListItem> /*implements ActionMode.Callback*/ {

	private ArrayList<ListItem> my_data;
	private int selectedPos = RecyclerView.NO_POSITION;
	private View lastSelected = null;
//	private static final String TAG = RightAdapter.class.getSimpleName();
	private Context context;
//	private Object mActionMode;

	static class ViewHolder {
		TextView textView1;
		ImageView imgView1;
		TextView textView2;
		ImageView imgView2;
		LinearLayout line;
	}


	public interface OnListener {
		void SelectedLeftItemId(int id);
		void SelectedLeftItemIndex(int index);
		void LongPressedLeftItemId(int id);
		void LongPressedLeftItemIndex(int index);
	}
	private final OnListener listener;
	public LeftArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ListItem> objects,
	                        OnListener listener) {
		super(context, resource, objects);
		this.context = context;
		this.listener = listener;
		this.my_data = objects;
	}


	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		View view;
		context = getContext();
		if (convertView == null) {
			view = LayoutInflater.from(context).inflate(R.layout.inner_left, parent, false);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.textView1 = view.findViewById(R.id.tv_left_1);
			viewHolder.imgView1 = view.findViewById(R.id.iv_left_1);
			viewHolder.textView2 = view.findViewById(R.id.tv_left_2);
			viewHolder.imgView2 = view.findViewById(R.id.iv_left_2);
			viewHolder.line = view.findViewById(R.id.ll_left_entry);
			view.setTag(viewHolder);
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
		view.setOnLongClickListener(v -> {
//			if (mActionMode != null) {
//				return false;
//			}
			selectedPos = position;
			ListItem clickedItem = my_data.get(position);
			v.setSelected(true);
			if (lastSelected != null) {
				lastSelected.setSelected(false);
			}
			lastSelected = v;
//			mActionMode = view.startActionMode(LeftArrayAdapter.this);
			listener.LongPressedLeftItemId(clickedItem.getID());
			listener.LongPressedLeftItemIndex(position);
			return true;
		});
/*		view.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				event.getX();
				event.getY();
				int action = MotionEventCompat.getActionMasked(event);
				switch (action) {
					case (MotionEvent.ACTION_CANCEL) :
//						Log.d(DEBUG_TAG,"Action was CANCEL");
						return true;
					case (MotionEvent.ACTION_OUTSIDE) :
//						Log.d(DEBUG_TAG,"Movement occurred outside bounds");
						return true;
					case (MotionEvent.ACTION_MOVE) :
//						Log.d(DEBUG_TAG,"Action was MOVE");
						return true;
					case (MotionEvent.ACTION_DOWN):
						break;
					case (MotionEvent.ACTION_UP):
						break;
				}
				return false;
			}
		});*/
/*		view.setOnTouchListener(new OnSwipeTouchListener(context) {
			@Override
			public void onSwipeDown() {
//				Toast.makeText(this, "Down", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSwipeLeft() {
//				Toast.makeText(MainActivity.this, "Left", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSwipeUp() {
//				Toast.makeText(MainActivity.this, "Up", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSwipeRight() {
//				Toast.makeText(MainActivity.this, "Right", Toast.LENGTH_SHORT).show();
			}
		});*/
		view.setOnClickListener(v -> {
			selectedPos = position;
			ListItem clickedItem = my_data.get(position);
			v.setSelected(true);
			if (lastSelected != null) {
				lastSelected.setSelected(false);
			}
			lastSelected = v;
			listener.SelectedLeftItemId(clickedItem.getID());
			listener.SelectedLeftItemIndex(position);
		});
		return view;
	}

/*
	private void show() {
	}
	//ActionMode.Callback
	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		MenuInflater inflater = mode.getMenuInflater();
		inflater.inflate(R.menu.left_context, menu);
		return true;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return false;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		switch (item.getItemId()) {
			case R.id.context_left_remove:
				show();
				mode.finish();
				return true;
			case R.id.context_left_move:
				return true;
			default:
				return false;
		}
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		mActionMode = null;
		selectedPos = RecyclerView.NO_POSITION;
	}
*/
}
