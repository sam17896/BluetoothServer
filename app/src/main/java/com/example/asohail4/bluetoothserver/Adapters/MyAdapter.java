package com.example.asohail4.bluetoothserver.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asohail4.bluetoothserver.Models.MyDevice;
import com.example.asohail4.bluetoothserver.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<MyDevice> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name, addrees;
        public MyViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.name);
            addrees = view.findViewById(R.id.address);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<MyDevice> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem, parent, false);
        MyViewHolder vh = new MyViewHolder(itemView);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.setText(mDataset.get(position).getName());
        holder.addrees.setText(mDataset.get(position).getAddress());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
