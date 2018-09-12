package com.tht.k3pler.frag;


import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tht.k3pler.R;

public class MainPageInflater {
    private Context context;
    private ViewGroup viewGroup;
    public interface IRecylerView{
        void onInit(RecyclerView recyclerView);
    }
    // ** //
    private RecyclerView recyclerView;

    public MainPageInflater(Context context, ViewGroup viewGroup){
        this.context = context;
        this.viewGroup = viewGroup;
    }
    public void init(IRecylerView iRecylerView){
        recyclerView = viewGroup.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        iRecylerView.onInit(recyclerView);
    }

}
