package com.luobotie.kingshun.mychat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luobotie.kingshun.mychat.R;

import java.util.List;

/**
 * Created by Administrator on 2017/10/23.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private static final String TAG = "MyAdapter";
    private Context mContext;
    private List<String> mDatas;
    private onItemClickListener listener;


    public MyAdapter(Context mContext, List<String> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_chat_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);
        if (position % 2 == 1) {
            //左边消息
            holder.mLeftContainer.setVisibility(View.VISIBLE);
            holder.mRightContainer.setVisibility(View.GONE);
            holder.mLeftMsg.setText(mDatas.get(position));
        } else {
            holder.mRightContainer.setVisibility(View.VISIBLE);
            holder.mLeftContainer.setVisibility(View.GONE);
            holder.mRightMsg.setText(mDatas.get(position));
        }
    }

    public void swapData(List<String> datas) {
        mDatas = datas;
        this.notifyItemInserted(mDatas.size());
    }

    @Override
    public int getItemCount() {
        if (mDatas == null) {
            return 0;
        } else {
            return mDatas.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView mLeftMsg;
        private TextView mRightMsg;
        private LinearLayout mLeftContainer;
        private LinearLayout mRightContainer;

        public MyViewHolder(View view) {
            super(view);
            mLeftMsg = (TextView) view.findViewById(R.id.msg_left);
            mRightMsg = (TextView) view.findViewById(R.id.msg_right);
            mRightContainer = (LinearLayout) view.findViewById(R.id.ll_right_container);
            mLeftContainer = (LinearLayout) view.findViewById(R.id.ll_left_container);
            //点击监听器注册
            mRightMsg.setOnClickListener(this);
            mLeftMsg.setOnClickListener(this);

            mRightMsg.setOnLongClickListener(this);
            mLeftMsg.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            listener.onLongClick(getAdapterPosition());
            return true;
        }
    }

    public interface onItemClickListener {
        void onClick(int position);
        void onLongClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }
}
