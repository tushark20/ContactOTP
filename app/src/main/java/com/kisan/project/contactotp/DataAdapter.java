package com.kisan.project.contactotp;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private ArrayList<String> mList;
    private int section;

    public DataAdapter(ArrayList<String> List, int section) {
        this.mList = List;
        this.section = section;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        if(section==1) //list item of 'contact list'
        {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
            return new ViewHolder(view);
        }
        else // list item of 'sent message list'
        {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.msgcard_row, viewGroup, false);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {
        if(section==1)
        {viewHolder.account.setText(mList.get(i));} // insert contact into list item
        else
        {
            //insert sent message details
            String[] values = mList.get(i).split("#");

            viewHolder._name.setText(viewHolder._name.getText().toString()+" "+values[0]);
            viewHolder._time.setText(values[1]);
            viewHolder._otp.setText(viewHolder._otp.getText().toString()+" "+values[2]);

        }
    }

    @Override
    public int getItemCount() {

            return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView account;
        private TextView _name;
        private TextView _time;
        private TextView _otp;
        public ViewHolder(View view)
        {
            super(view);
            account = (TextView)view.findViewById(R.id.account);
            _name = (TextView)view.findViewById(R.id.mName);
            _time = (TextView)view.findViewById(R.id.mTime);
            _otp = (TextView)view.findViewById(R.id.mOTP);
        }
    }

}

