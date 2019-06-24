package com.k3.k3pler.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.k3.k3pler.R;

import java.util.ArrayList;

/** Adapter for recyclerView list [blacklist page] **/
public class BlacklistAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> blackList;
    private LayoutInflater inflater;

    private TextView txvReqBlacklist;

    private void init(View view) {
        txvReqBlacklist = view.findViewById(R.id.txvReqBlacklist);
    }

    public BlacklistAdapter(Context context, ArrayList<String> blackList){
        this.context = context;
        this.blackList = blackList;
    }
    @Override
    public int getCount(){
        return blackList.size();
    }

    @Override
    public Object getItem(int position){
        return blackList.get(position);
    }

    @Override
    public long getItemId(int position){
        return 0;
    }
    @Override
    public View getView(final int position, View conView, ViewGroup viewGroup){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conView = inflater.inflate(R.layout.list_blacklist, viewGroup, false);
        init(conView);
        txvReqBlacklist.setText(blackList.get(position));
        return conView;
    }
}
