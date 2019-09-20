package com.yumatechnical.konnectandroid.Adapter;
/*
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yumatechnical.konnectandroid.Model.ListItem;
import com.yumatechnical.konnectandroid.R;

import java.util.List;


public class LeftAdapter extends RecyclerView.Adapter<LeftAdapter.LeftAdapterViewHolder> {

    private List<ListItem> my_data;
    private static final String TAG = LeftAdapter.class.getSimpleName();
	private int selectedPos = RecyclerView.NO_POSITION;

	public interface OnItemClickListener {
		void onItemClick(String item);
	}
	private final List<ListItem> items;
	private final OnItemClickListener listener;
    public LeftAdapter(List<ListItem> items, OnItemClickListener listener) {
	    this.items = items;
	    this.listener = listener;
    }

	class LeftAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    	final LinearLayout linout, line;
        final TextView textView1, textView2;
        final ImageView imgView1, imgView2;
        final View underline;


        LeftAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
	        linout = itemView.findViewById(R.id.ll_left_root);
	        line = itemView.findViewById(R.id.ll_left_entry);
            textView1 = itemView.findViewById(R.id.tv_left_1);
            textView2 = itemView.findViewById(R.id.tv_left_2);
            imgView1 = itemView.findViewById(R.id.iv_left_1);
            imgView2 = itemView.findViewById(R.id.iv_left_2);
	        underline = itemView.findViewById(R.id.underline);
            itemView.setOnClickListener(this);
        }

		@Override
		public void onClick(View v) {
			notifyItemChanged(selectedPos);
			selectedPos = getLayoutPosition();
			if (!my_data.get(selectedPos).getFaded()) {
				listener.onItemClick(my_data.get(selectedPos).getName());
			}
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
        ListItem entry = my_data.get(position);
        if (entry.getIconBeforeText()) {
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
	    holder.itemView.setSelected(selectedPos == position);
        holder.line.setPadding(0, entry.getTopPadding(), 0, entry.getBotPadding());
        holder.linout.setPadding(entry.getLeftPadding(), 0, 0, 0);
        if (entry.getFaded()) {
        	holder.textView1.setTextColor(Color.LTGRAY);
        	holder.textView2.setTextColor(Color.LTGRAY);
        	holder.imgView1.setColorFilter(Color.LTGRAY);
        	holder.imgView2.setColorFilter(Color.LTGRAY);
        }
    }

    @Override
    public int getItemCount() {
        if (null == my_data) return 0;
        return my_data.size();
    }

    public void setData(List<ListItem> myData) {
        my_data = myData;
        notifyDataSetChanged();
    }

	@Override
	public long getItemId(int position) {
		return super.getItemId(position);
	}

}
*/