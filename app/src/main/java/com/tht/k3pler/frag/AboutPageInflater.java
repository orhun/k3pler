package com.tht.k3pler.frag;


import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tht.k3pler.R;

/** About page **/
public class AboutPageInflater {
    private Context context;
    private ViewGroup viewGroup;
    private Dialog guiDialog;
    private static String version = "1.0",
            k3pwn = "http://www.k3pwn.me",
            recyclerviewLicence = "https://developer.android.com/topic/libraries/support-library/packages",
            littleproxyProject = "https://github.com/adamfisk/LittleProxy";
    // ** //
    private TextView txvK3pwn, txvProjectInfo, txvLicenceRecycler,
            txvLicenceLittleProxy, txvJKepler;
    public AboutPageInflater(Context context, ViewGroup viewGroup, Dialog guiDialog){
        this.context = context;
        this.viewGroup = viewGroup;
        this.guiDialog = guiDialog;
    }
    public void init(){
        txvK3pwn = viewGroup.findViewById(R.id.txvK3pwn);
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
        txvK3pwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPage(k3pwn);
            }
        });
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
    private void openTwitterIntent(Context context) {
        try {
            String id = "4745956696";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name="+id));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            dismissGuiDialog();
        } catch (Exception e) {
            openPage("https://twitter.com/KeyLo_99");
        }
    }
    private void openYoutubeIntent(Context context) {
        try {
            String id = "/channel/UC1Y6oS0iyZkjbHxidx5Oirg";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + id));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            dismissGuiDialog();
        } catch (Exception e) {
            openPage("https://www.youtube.com/channel/UC1Y6oS0iyZkjbHxidx5Oirg");
        }
    }
    private void copyToClipBoard(String data){
        try{
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(context.getString(R.string.app_name), data);
            clipboard.setPrimaryClip(clip);
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
