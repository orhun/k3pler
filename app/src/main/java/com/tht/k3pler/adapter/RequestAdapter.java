package com.tht.k3pler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tht.k3pler.R;
import com.tht.k3pler.sub.HTTPReq;

import java.util.ArrayList;


public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private Context context;
    public ArrayList<HTTPReq> requests;
    private int requestColor;

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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_requests, parent, false);
        return new ViewHolder(itemView);
    }
    @Override
    @SuppressWarnings("deprecation")
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (requests.get(position).getBlocked())
            requestColor = ContextCompat.getColor(context, android.R.color.holo_red_dark);
        else
            requestColor = ContextCompat.getColor(context, android.R.color.white);
        String htmlEntry = "<b>"+
                "<font color=\"" +
                requestColor + "\">"
                + requests.get(position).getUri() + "</font>"+"<font color=\""+
                ContextCompat.getColor(context, R.color.color3lighter) +  "\">"
                +" ~ </font>"+"<font color=\""+
                ContextCompat.getColor(context, R.color.color2) + "\">"
                + requests.get(position).getMethod() +"</font>"+"<font color=\""+
                ContextCompat.getColor(context, R.color.color1) +  "\">"
                + " [" + requests.get(position).getProtocol() + "] " +"</font>"+"<font color=\""+
                ContextCompat.getColor(context, R.color.orange) +  "\">"
                + "_" + requests.get(position).getResult() + "_ " +"</font>"+"<font color=\""+
                ContextCompat.getColor(context, R.color.lightYellow) +  "\">"
                + " " + requests.get(position).getTime()  +"</font>"
                + "</b>";
        holder.txvRequest.setText(Html.fromHtml(htmlEntry));
        holder.bind(requests.get(position), position, onItemClickListener);
    }
    @Override
    public int getItemCount() {
        return requests.size();
    }

}