package com.example.lewis.list_window;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Lewis on 2017/9/11.
 * Description:
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context mContext;
    private List<String> data;
    private LayoutInflater mLayoutInflater;
    private OnImageViewClickListener mOnImageViewClickListener;

    public MyAdapter(Context context, List<String> data) {
        mContext = context;
        this.data = data;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mLayoutInflater.inflate(R.layout.adapter_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTitle.setText("Title");
        holder.mDescription.setText("Description");
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView mTitle, mDescription;
        private ImageView mImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.textView);
            mDescription = itemView.findViewById(R.id.textView2);
            mImageView = itemView.findViewById(R.id.imageView);
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnImageViewClickListener != null) {
                        mOnImageViewClickListener.onImageViewClick(view);
                    }
                }
            });
        }
    }

    interface OnImageViewClickListener {
        void onImageViewClick(View view);
    }

    public void setOnImageViewClickListener(OnImageViewClickListener onImageViewClickListener) {
        mOnImageViewClickListener = onImageViewClickListener;
    }
}
