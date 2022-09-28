package com.java.sortsmsapp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.java.sortsmsapp.pojo.SMSData;

import java.util.List;

public class SMSListAdapter extends RecyclerView.Adapter<SMSListAdapter.ViewHolder>{
    private List<SMSData> listdata;

    // RecyclerView recyclerView;
    public SMSListAdapter(List<SMSData> listdata) {
        this.listdata = listdata;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SMSData smsdata = listdata.get(position);
        holder.phoneView.setText(listdata.get(position).getPhone());
        holder.msgView.setText(listdata.get(position).getMessage());
        holder.dateView.setText(listdata.get(position).getDate());
        holder.linearLayoutCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"From: "+smsdata.getPhone()+" Msg:"+smsdata.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView phoneView;
        public TextView msgView;
        public TextView dateView;
        public LinearLayoutCompat linearLayoutCompat;
        public ViewHolder(View itemView) {
            super(itemView);
            this.phoneView = (TextView) itemView.findViewById(R.id.phoneView);
            this.msgView = (TextView) itemView.findViewById(R.id.msgView);
            this.dateView = (TextView) itemView.findViewById(R.id.dateView);
            linearLayoutCompat = (LinearLayoutCompat) itemView.findViewById(R.id.linearLayout);
        }
    }
}
