package com.tht.k3pler;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


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
    @SuppressWarnings("deprecation")
    public void onBindViewHolder(ViewHolder holder, int position) {
        String uri = requests.get(position).getUri();
        String method = requests.get(position).getMethod();
        String htmlEntry = "<b>"+
                "<font color=\"" +
                ContextCompat.getColor(context, android.R.color.white) + "\">"
                + uri + "</font>"+"<font color=\""+
                ContextCompat.getColor(context, R.color.color3lighter) +  "\">"
                +" ~ </font>"+"<font color=\""+
                ContextCompat.getColor(context, R.color.color2) + "\">"
                + method +"</font>"+"<font color=\""+
                ContextCompat.getColor(context, R.color.color1) +  "\">"
                + " " + getTime() +"</font>"
                + "</b>";
        holder.txvRequest.setText(Html.fromHtml(htmlEntry));
        holder.bind(requests.get(position), position, onItemClickListener);
    }
    @Override
    public int getItemCount() {
        return requests.size();
    }

    private String getTime(){
        DateFormat df = new SimpleDateFormat("[HH:mm:ss]", Locale.getDefault());
        return df.format(Calendar.getInstance().getTime());
    }

}