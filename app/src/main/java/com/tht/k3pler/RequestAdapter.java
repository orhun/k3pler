package com.tht.k3pler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private Context context;
    public ArrayList<HTTPReq> requests;

    public interface OnItemClickListener {
        void onItemClick(HTTPReq item, int i);
    }
    private OnItemClickListener onItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txvRequest;
        public ViewHolder(View view) {
            super(view);
            txvRequest = view.findViewById(R.id.txvRequest);
        }
        public void bind(final HTTPReq item, final int i, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, i);
                }
            });
        }
    }
    public RequestAdapter(Context context, ArrayList<HTTPReq> requests, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.requests = requests;
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_requests, parent, false);
        return new ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String uri = requests.get(position).getUri();
        holder.txvRequest.setText(uri);
        holder.bind(requests.get(position), position, onItemClickListener);
    }
    @Override
    public int getItemCount() {
        return requests.size();
    }

}