package com.example.lewis.list_window;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Lewis on 2017/9/11.
 * Description:
 */

public class WindowAdapter extends RecyclerView.Adapter<WindowAdapter.WindowVh>{

    private LayoutInflater mInflater;
    private List<String> data;
    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

    public WindowAdapter(Context context, List<String> data) {
        this.data = data;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public WindowVh onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WindowVh(mInflater.inflate(R.layout.adapter_window, parent, false));
    }

    @Override
    public void onBindViewHolder(WindowVh holder, int position) {
        holder.mTextView.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data == null ?  0 : data.size();
    }

    class WindowVh extends RecyclerView.ViewHolder{

        private TextView mTextView;

        public WindowVh(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if (mOnItemClickListener != null)
                       mOnItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public String getData(int position) {
        if (position < 0 || position >= data.size()) return "";
        return data.get(position);
    }
}
