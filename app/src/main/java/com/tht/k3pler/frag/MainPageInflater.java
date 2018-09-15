package com.tht.k3pler.frag;


import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tht.k3pler.R;
import com.tht.k3pler.handler.RequestDialog;
import com.tht.k3pler.handler.SqliteDBHelper;
import com.tht.k3pler.sub.HTTPReq;
import com.tht.k3pler.sub.SQLiteBL;

import java.util.ArrayList;

public class MainPageInflater {
    private Context context;
    private ViewGroup viewGroup;
    private SqliteDBHelper sqliteDBHelper;
    private ArrayList httpReqs;
    public interface IRecylerView{
        void onInit(RecyclerView recyclerView);
    }
    // ** //
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swpMain;

    public MainPageInflater(Context context, ViewGroup viewGroup, ArrayList httpReqs){
        this.context = context;
        this.viewGroup = viewGroup;
        this.httpReqs = httpReqs;
    }
    public void init(IRecylerView iRecylerView){
        recyclerView = viewGroup.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        iRecylerView.onInit(recyclerView);
        swpMain = viewGroup.findViewById(R.id.swpMain);
        swpMain.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        httpReqs.clear();
                        recyclerView.setAdapter(null);
                        swpMain.setRefreshing(false);
                    }
                }, 800);
            }
        });
    }
    public void onDetailDialogItemClick(final HTTPReq item, final BlacklistPageInflater blacklistPageInflater,
                                        final ViewPager viewPager, final int pageID){
        new RequestDialog(context, item).show(new RequestDialog.IBtnBlackList() {
            @Override
            public void onInit(Button btnReqBlackList, final Dialog dialog, final String uri) {
                btnReqBlackList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sqliteDBHelper = new SqliteDBHelper(context,
                                new SQLiteBL(context).getWritableDatabase(),
                                SQLiteBL.BLACKLIST_DATA, SQLiteBL.TABLE_NAME);
                        if(!sqliteDBHelper.getAll().contains(uri)) {
                            sqliteDBHelper.insert(uri);
                        }
                        sqliteDBHelper.close();
                        dialog.cancel();
                        blacklistPageInflater.setBlacklistLstView();
                        /* viewPager.setCurrentItem(pageID); */
                    }
                });
            }
        });
    }
}
