package com.yumatechnical.konnectandroid.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yumatechnical.konnectandroid.Model.FileItem;
import com.yumatechnical.konnectandroid.R;

import java.util.ArrayList;


public class RightAdapter extends RecyclerView.Adapter<RightAdapter.RightAdapterViewHolder> {

    private ArrayList<FileItem> my_data;
//    private static final String TAG = RightAdapter.class.getSimpleName();
	private int selectedPos = RecyclerView.NO_POSITION;

    public interface ListItemClickListener {
    	void onListItemClick(String item);
	}
	final private ListItemClickListener listener;

    public RightAdapter(ArrayList<FileItem> items, ListItemClickListener listener) {
//    	Log.d(TAG, "constructor made");
    	my_data = items;
    	this.listener = listener;
    }

    class RightAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView labelView;
        final ImageView imageView;


        RightAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
	        labelView = itemView.findViewById(R.id.tv_right_label);
	        imageView = itemView.findViewById(R.id.iv_right_image);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
	        FileItem entry = my_data.get(position);
	        if (entry.getBitmap() != null) {
		        imageView.setImageBitmap(entry.getBitmap());
	        }
	        if (entry.getName() != null && !entry.getName().isEmpty()) {
		        if (!entry.getHasContents()) {
			        labelView.setTextColor(Color.LTGRAY);
		        }
		        labelView.setText(entry.getName());
	        }
        }

	    @Override
	    public void onClick(View v) {
		    if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
		    if (listener != null) {
			    listener.onListItemClick(String.valueOf(labelView.getText()));
		    }
		    notifyItemChanged(selectedPos);
		    selectedPos = getAdapterPosition();
		    notifyItemChanged(selectedPos);
	    }
    }

    @NonNull
    @Override
    public RightAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.inner_right, parent, false);
        return new RightAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RightAdapterViewHolder holder, int position) {
    	holder.bind(position);
	    holder.itemView.setBackgroundColor(selectedPos == position ? Color.LTGRAY : Color.TRANSPARENT);
//	    FileItem entry = my_data.get(position);
//	    if (entry.getBitmap() != null) {
//	    	holder.imageView.setImageBitmap(entry.getBitmap());
//	    }
//	    if (entry.getName() != null && !entry.getName().isEmpty()) {
//	    	if (!entry.getHasContents()) {
//	    		holder.labelView.setTextColor(Color.LTGRAY);
//		    }
//		    holder.labelView.setText(entry.getName());
//	    }
    }

    @Override
    public int getItemCount() {//done
        if (null == my_data) return 0;
        return my_data.size();
    }

    public void setData(ArrayList<FileItem> myData) {//done
        my_data = myData;
        notifyDataSetChanged();
    }

}
