package com.yumatechnical.konnectandroid.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import com.mikepenz.iconics.IconicsColor;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.IconicsSize;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;
import com.yumatechnical.konnectandroid.Model.FileItem;
import com.yumatechnical.konnectandroid.R;

import java.util.ArrayList;

import static com.mikepenz.iconics.Iconics.getApplicationContext;


public class RightAdapter extends RecyclerView.Adapter<RightAdapter.RightAdapterViewHolder> {

    private ArrayList<FileItem> my_data;
//    private static final String TAG = RightAdapter.class.getSimpleName();
	private int selectedPos = RecyclerView.NO_POSITION;
	private Drawable defaultImage;

    public interface ListItemClickListener {
    	void onListItemClick(String item);
    	void onListItemClick(FileItem item);
	}
	final private ListItemClickListener listener;

    public RightAdapter(ArrayList<FileItem> items, Drawable defaultImage, ListItemClickListener listener) {
//    	Log.d(TAG, "constructor made");
    	my_data = items;
    	this.listener = listener;
    	this.defaultImage = defaultImage;
    }

    class RightAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView labelView, labelText;
        final ImageView /*imageView,*/ labelImg;
        final FrameLayout labelFrame;
        final PhotoView imageView;


        RightAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
	        labelView = itemView.findViewById(R.id.tv_right_label);
	        imageView = itemView.findViewById(R.id.iv_right_image);
	        labelFrame = itemView.findViewById(R.id.fl_right_item_label);
	        labelText = itemView.findViewById(R.id.tv_right_item_label);
	        labelImg = itemView.findViewById(R.id.iv_right_item_label);
            itemView.setOnClickListener(this);
//            itemView.setTag(getPosition());
        }

        void bind(int position) {
	        FileItem entry = my_data.get(position);
	        if (entry.getBitmap() != null) {
		        imageView.setImageBitmap(entry.getBitmap());
	        } else {
	        	imageView.setImageDrawable(defaultImage);
	        	imageView.setColorFilter(Color.GRAY);
	        }
	        if (entry.getName() != null && !entry.getName().isEmpty() && !entry.isHideName()) {
		        if (!entry.getHasContents()) {
			        labelView.setTextColor(Color.LTGRAY);
		        }
		        labelView.setText(entry.getName());
		        labelView.setGravity(Gravity.CENTER_HORIZONTAL);
	        }
	        if (entry.getLabel() != null) {
	        	labelText.setText(entry.getLabel());
	        	labelImg.setImageDrawable(new IconicsDrawable(getApplicationContext())
				        .icon(FontAwesome.Icon.faw_circle).color(IconicsColor.colorRes(R.color.Gray))
				        .size(IconicsSize.TOOLBAR_ICON_SIZE));
		        labelFrame.setVisibility(View.VISIBLE);
	        }
        }

	    @Override
	    public void onClick(View v) {
		    if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
		    FileItem clickedItem = my_data.get(getAdapterPosition());
		    if (listener != null) {
			    listener.onListItemClick(String.valueOf(labelView.getText()));
			    listener.onListItemClick(clickedItem);
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

    public void setDefaultImage(Drawable defaultImage) {
    	this.defaultImage = defaultImage;
	    notifyDataSetChanged();
    }

	public void clear() {
		my_data.clear();
		notifyDataSetChanged();
	}

	public void addAll(ArrayList<FileItem> list) {
		my_data.addAll(list);
		notifyDataSetChanged();
	}

}
