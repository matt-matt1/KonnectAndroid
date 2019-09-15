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


public class RightAdapter extends RecyclerView.Adapter<RightAdapter.RightAdapterViewHolder> {

    private List<String> my_data;
    private static final String TAG = LeftAdapter.class.getSimpleName();
	private int selectedPos = RecyclerView.NO_POSITION;

    public interface ListItemClickListener {
    	void onListItemClick(String item);
	}
	final private ListItemClickListener listener;

    public RightAdapter(List<String> items, ListItemClickListener listener) {
    	my_data = items;
    	this.listener = listener;
    }

    class RightAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView tv_right_data;

        RightAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
	        tv_right_data = itemView.findViewById(R.id.tv_left_data);
            itemView.setOnClickListener(this);
        }

	    @Override
	    public void onClick(View v) {
		    listener.onListItemClick(String.valueOf(tv_right_data.getText()));
	    }
    }

    @NonNull
    @Override
    public RightAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.inner_left, parent, false);
        return new RightAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RightAdapterViewHolder holder, int position) {
        String entry = my_data.get(position);
        holder.tv_right_data.setText(entry);
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

}
