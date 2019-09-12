package com.k3.k3pler.frag;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.k3.k3pler.R;

/** About page **/
public class AboutPageInflater {
    private Context context;
    private ViewGroup viewGroup;
    private Dialog guiDialog;
    private static String version = "1.0",
            recyclerviewLicence = "https://developer.android.com/topic/libraries/support-library/packages",
            littleproxyProject = "https://github.com/adamfisk/LittleProxy";
    // ** //
    private TextView txvProjectInfo, txvLicenceRecycler,
            txvLicenceLittleProxy, txvJKepler;
    public AboutPageInflater(Context context, ViewGroup viewGroup, Dialog guiDialog){
        this.context = context;
        this.viewGroup = viewGroup;
        this.guiDialog = guiDialog;
    }
    public void init(){
        txvProjectInfo = viewGroup.findViewById(R.id.txvProjectInfo);
        txvLicenceRecycler = viewGroup.findViewById(R.id.txvLicenceRecycler);
        txvLicenceLittleProxy = viewGroup.findViewById(R.id.txvLicenceLittleProxy);
        txvJKepler = viewGroup.findViewById(R.id.txvJKepler);
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
        }catch (Exception e){
            e.printStackTrace();
        }
        txvProjectInfo.setText(context.getString(R.string.main_page) + " v" + version);
        txvLicenceRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPage(recyclerviewLicence);
            }
        });
        txvLicenceLittleProxy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPage(littleproxyProject);
            }
        });
        txvJKepler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPage(context.getString(R.string.jkepler_link));
            }
        });
    }
    private void openPage(String url){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            dismissGuiDialog();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void dismissGuiDialog(){
        try {
            if(guiDialog != null && guiDialog.isShowing()) {
                guiDialog.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
