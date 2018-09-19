package com.example.asohail4.bluetoothserver.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asohail4.bluetoothserver.Models.Message;
import com.example.asohail4.bluetoothserver.R;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter {

    private List<Message> mDataset;
    public static final int VIEWTYPE_BOT = 0;
    public static final int VIEWTYPE_USER = 1;


    public MessageListAdapter(List<Message> mDataset){
        this.mDataset = mDataset;
    }

    public void addItem(Message message){
        mDataset.add(message);
        notifyItemInserted(mDataset.size() - 1);
    }

    /*
        Gets the correct layout for the message (User/bot)
        Returns a new instance of the viewholder
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == MessageListAdapter.VIEWTYPE_BOT){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listitemrecieve, parent, false);
            return new BotViewHolder(v);

        }else if (viewType == MessageListAdapter.VIEWTYPE_USER){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listitemsent, parent, false);
            return new UserViewHolder(v);
        }

        return null;
    }

    /*
        Puts data in the layout, i.e. the text to be displayed.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserViewHolder){
            ((UserViewHolder) holder).mTextView.setText(mDataset.get(position).getMessage());

        }
        else if (holder instanceof BotViewHolder){
            ((BotViewHolder) holder).mTextView.setText(mDataset.get(position).getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataset.get(position).getViewType();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;

        public UserViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.txtChatBot);
        }
    }

    public class BotViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;

        public BotViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.txtChatUser);
        }
    }


}