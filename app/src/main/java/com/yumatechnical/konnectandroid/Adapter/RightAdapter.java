package com.yumatechnical.konnectandroid.Adapter;

import android.content.Context;
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
    private static final String TAG = RightAdapter.class.getSimpleName();
	private int selectedPos = RecyclerView.NO_POSITION;

    public interface ListItemClickListener {
    	void onListItemClick(String item);
	}
	final private ListItemClickListener listener;

    public RightAdapter(ArrayList<FileItem> items, ListItemClickListener listener) {
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

	    @Override
	    public void onClick(View v) {
		    listener.onListItemClick(String.valueOf(labelView.getText()));
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
	    FileItem entry = my_data.get(position);
//	    if (entry.getDrawable() != null) {
//	    	holder.imageView.setImageDrawable(entry.getDrawable());
//	    } else if (entry.getBitmap() != null) {
//	    	holder.imageView.setImageBitmap(entry.getBitmap());
//	    }
//	    if (entry.getName() != null && !entry.getName().isEmpty()) {
//		    holder.labelView.setText(entry.getName());
//	    }
    }

    @Override
    public int getItemCount() {
        if (null == my_data) return 0;
        return my_data.size();
    }

    public void setData(ArrayList<FileItem> myData) {
        my_data = myData;
        notifyDataSetChanged();
    }

}
