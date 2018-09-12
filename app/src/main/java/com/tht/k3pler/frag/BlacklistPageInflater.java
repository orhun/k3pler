package com.tht.k3pler.frag;


import android.content.Context;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tht.k3pler.R;

public class BlacklistPageInflater {
    private Context context;
    private ViewGroup viewGroup;
    public interface IListView{
        void onInit(ListView listView);
    }
    // * //
    private ListView lstBlacklist;

    public BlacklistPageInflater(Context context, ViewGroup viewGroup){
        this.context = context;
        this.viewGroup = viewGroup;
    }
    public void init(IListView iListView){
        lstBlacklist = viewGroup.findViewById(R.id.lstBlacklist);
        iListView.onInit(lstBlacklist);
    }
}
