package com.yumatechnical.konnectandroid.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yumatechnical.konnectandroid.R;

import java.util.List;


public class LeftAdapter extends RecyclerView.Adapter<LeftAdapter.LeftAdapterViewHolder> {

    private List<String> my_data;
    private static final String TAG = LeftAdapter.class.getSimpleName();
	private int selectedPos = RecyclerView.NO_POSITION;

	public interface OnItemClickListener {
		void onItemClick(String item);
	}
	private final List<String> items;
	private final OnItemClickListener listener;
    public LeftAdapter(List<String> items, OnItemClickListener listener) {
	    this.items = items;
	    this.listener = listener;
    }

	class LeftAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView tv_left_data;

        LeftAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_left_data = itemView.findViewById(R.id.tv_left_data);
            itemView.setOnClickListener(this);
        }

		@Override
		public void onClick(View v) {
			notifyItemChanged(selectedPos);
			selectedPos = getLayoutPosition();
			listener.onItemClick(String.valueOf(tv_left_data.getText()));
		}
	}

    @NonNull
    @Override
    public LeftAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.inner_left, parent, false);
        return new LeftAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeftAdapterViewHolder holder, int position) {
        String entry = my_data.get(position);
        holder.tv_left_data.setText(entry);
	    holder.itemView.setSelected(selectedPos == position);
    }

    @Override
    public int getItemCount() {
        if (null == my_data) return 0;
        return my_data.size();
    }

    public void setData(List<String> myData) {
        my_data = myData;
        notifyDataSetChanged();
    }

	@Override
	public long getItemId(int position) {
		return super.getItemId(position);
	}

}
