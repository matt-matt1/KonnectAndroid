package com.yumatechnical.konnectandroid.Fragment;
/**
TO BE DELETED! KEPT ONLY FOR AN EXAMPLE

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yumatechnical.konnectandroid.Fragment.LeftItemFragment.OnLeftListFragmentInteractionListener;
import com.yumatechnical.konnectandroid.Model.ListItem;
import com.yumatechnical.konnectandroid.R;

import java.util.List;

 * {@link RecyclerView.Adapter} that can display a {@link ListItem} and makes a call to the
 * specified {@link OnLeftListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.

public class LeftItemRecyclerViewAdapter extends RecyclerView.Adapter<LeftItemRecyclerViewAdapter.ViewHolder> {

	private final List<ListItem> mValues;
	private final LeftItemFragment.OnLeftListFragmentInteractionListener mListener;


	public LeftItemRecyclerViewAdapter(List<ListItem> items, OnLeftListFragmentInteractionListener listener) {
		mValues = items;
		mListener = listener;
	}


	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.inner_left, parent, false);
//				.inflate(R.layout.fragment_left_item, parent, false);
		return new ViewHolder(view);
	}


	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		holder.mItem = mValues.get(position);
//		holder.mIdView.setText(mValues.get(position).id);
//		holder.mContentView.setText(mValues.get(position).content);

		holder.mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mListener) {
					// Notify the active callbacks interface (the activity, if the
					// fragment is attached to one) that an item has been selected.
					mListener.onListFragmentInteraction(holder.mItem);
				}
			}
		});
	}


	@Override
	public int getItemCount() {
		return mValues.size();
	}


	public class ViewHolder extends RecyclerView.ViewHolder {
		public final View mView;
		public final TextView mIdView;
		public final TextView mContentView;
		public ListItem mItem;

		public ViewHolder(View view) {
			super(view);
			mView = view;
			mIdView = (TextView) view.findViewById(R.id.item_number);
			mContentView = (TextView) view.findViewById(R.id.content);
		}

		@Override
		public String toString() {
			return super.toString() + " '" + mContentView.getText() + "'";
		}
	}

}*/
