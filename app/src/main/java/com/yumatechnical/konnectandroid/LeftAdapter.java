package com.yumatechnical.konnectandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;


public class LeftAdapter extends RecyclerView.Adapter<LeftAdapter.LeftAdapterViewHolder> {

    private String[] my_data;
    private static final String TAG = LeftAdapter.class.getSimpleName();


    LeftAdapter() {
    }

    class LeftAdapterViewHolder extends RecyclerView.ViewHolder {

        final TextView tv_left_data;

        LeftAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_left_data = itemView.findViewById(R.id.tv_left_data);
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
        String entry = my_data[position];
        holder.tv_left_data.setText(entry);
    }

    @Override
    public int getItemCount() {
        if (null == my_data) return 0;
        return my_data.length;
    }

    void setData(String[] myData) {
        my_data = myData;
        notifyDataSetChanged();
    }

}
