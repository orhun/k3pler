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

public class AboutPageInflater {
    private Context context;
    private ViewGroup viewGroup;
    private Dialog guiDialog;
    private static String version = "1.0",
            github_page = "https://github.com/KeyLo99",
            bitbucket_page = "https://bitbucket.org/KeyLo99/",
            email = "keylo99official@gmail.com",
            k3pwn = "http://www.k3pwn.me",
            turkhackteam_profile = "https://www.turkhackteam.org/members/763537.html",
            recyclerviewLicence = "https://developer.android.com/topic/libraries/support-library/packages",
            littleproxyProject = "https://github.com/adamfisk/LittleProxy",
            turkhackteam_link = "https://www.turkhackteam.org";
    // ** //
    private TextView txvK3, txvTHT, txvContactGh,
            txvContactBb, txvContactTw, txvContactYt,
            txvContactMail, txvK3pwn, txvProjectInfo,
            txvOpenSourceLicences, txvLicenceRecycler,
            txvLicenceLittleProxy;
    public AboutPageInflater(Context context, ViewGroup viewGroup, Dialog guiDialog){
        this.context = context;
        this.viewGroup = viewGroup;
        this.guiDialog = guiDialog;
    }
    public void init(){
        txvK3 = viewGroup.findViewById(R.id.txvK3);
        txvTHT = viewGroup.findViewById(R.id.txvTHT);
        txvContactGh = viewGroup.findViewById(R.id.txvContactGh);
        txvContactBb = viewGroup.findViewById(R.id.txvContactBb);
        txvContactTw = viewGroup.findViewById(R.id.txvContactTw);
        txvContactYt = viewGroup.findViewById(R.id.txvContactYt);
        txvContactMail = viewGroup.findViewById(R.id.txvContactMail);
        txvK3pwn = viewGroup.findViewById(R.id.txvK3pwn);
        txvProjectInfo = viewGroup.findViewById(R.id.txvProjectInfo);
        txvOpenSourceLicences = viewGroup.findViewById(R.id.txvOpenSourceLicences);
        txvLicenceRecycler = viewGroup.findViewById(R.id.txvLicenceRecycler);
        txvLicenceLittleProxy = viewGroup.findViewById(R.id.txvLicenceLittleProxy);
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
        }catch (Exception e){
            e.printStackTrace();
        }
        txvProjectInfo.setText(context.getString(R.string.main_page) + " v" + version);
        txvContactGh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPage(github_page);
            }
        });
        txvContactBb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPage(bitbucket_page);
            }
        });
        txvContactYt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openYoutubeIntent(context);
            }
        });
        txvContactTw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTwitterIntent(context);
            }
        });
        txvK3pwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPage(k3pwn);
            }
        });
        txvK3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPage(turkhackteam_profile);
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
        txvTHT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPage(turkhackteam_link);
            }
        });
        txvContactMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyToClipBoard(email);
            }
        });
        setTextWithUnderline(txvTHT, txvTHT.getText().toString());
        setTextWithUnderline(txvK3pwn, txvK3pwn.getText().toString());
    }
    private void setTextWithUnderline(TextView textView, String text){
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);
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
