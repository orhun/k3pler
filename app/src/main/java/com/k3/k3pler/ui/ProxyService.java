package com.k3.k3pler.ui;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.k3.k3pler.frag.AboutPageInflater;
import com.k3.k3pler.frag.BlacklistPageInflater;
import com.k3.k3pler.frag.SettingsPageInflater;
import com.k3.k3pler.handler.LProxy;
import com.k3.k3pler.handler.RequestDialog;
import com.k3.k3pler.handler.SqliteDBHelper;
import com.k3.k3pler.R;
import com.k3.k3pler.sub.TextViewEFX;
import com.k3.k3pler.adapter.LayoutPagerAdapter;
import com.k3.k3pler.adapter.RequestAdapter;
import com.k3.k3pler.frag.MainPageInflater;
import com.k3.k3pler.handler.NotificationHandler;
import com.k3.k3pler.sub.FilteredResponse;
import com.k3.k3pler.sub.HTTPReq;

import org.littleshoot.proxy.HttpProxyServer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import io.netty.handler.codec.http.HttpRequest;

/** Main service **/
public class ProxyService extends Service {

    public HttpProxyServer httpProxyServer;
    private NotificationHandler notificationHandler;
    private final IBinder mBinder = new Binder();
    private Intent currentIntent;
    private static ArrayList<HTTPReq> httpReqs = new ArrayList<>();
    private ArrayList<String> settings = new ArrayList<>();
    private Dialog guiDialog, splashDialog;
    private String decoderResult = "", arrowChar = " > ";
    private Handler mainHandler;
    private LayoutPagerAdapter layoutPagerAdapter;
    private Boolean pageBackwards = false;
    private enum pageIDs {
        Main(0),
        BlackList(1),
        Settings(2),
        About(3);
        private int ID;
        private pageIDs(int ID){
            this.ID = ID;
        }
        private int getID(){
            return ID;
        }
    }
    // ** //
    private TextView txvPage, txvNum, txvMainPageMsg;
    private RecyclerView mRecyclerView;
    private ViewPager viewPager;
    private RelativeLayout rlMain;
    private MainPageInflater mainPageInflater;
    private BlacklistPageInflater blacklistPageInflater;
    private SettingsPageInflater settingsPageInflater;
    private AboutPageInflater aboutPageInflater;

    public ProxyService() {}

    @Override
    public IBinder onBind(Intent intent) { return mBinder; }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent currentIntent, int flags, int startId) {
        this.currentIntent = currentIntent;
        this.checkExtras();
        return START_NOT_STICKY;
    }
    private void checkExtras() {
        if (currentIntent != null) {
            try {
                if (currentIntent.getBooleanExtra(getString(R.string.show_gui), false)) {
                    showGuiDialog();
                } else if (currentIntent.getBooleanExtra(getString(R.string.proxy_stop), false)) {
                    stopSelf();
                } else{
                    showGUI();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            showGUI();
        }
    }
    @SuppressWarnings("deprecation")
    private void initGUI(Dialog dialog){
        txvPage = dialog.findViewById(R.id.txvPage);
        txvNum = dialog.findViewById(R.id.txvNum);
        viewPager = dialog.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(5);
        rlMain = dialog.findViewById(R.id.rlMain);
        layoutPagerAdapter = new LayoutPagerAdapter(getApplicationContext(), new LayoutPagerAdapter.IViewPager() {
            @Override
            public void onViewsAdded(ArrayList<ViewGroup> layouts) {
                try {
                    mainPageInflater = new MainPageInflater(getApplicationContext(), layouts.get(pageIDs.Main.getID()), httpReqs);
                    mainPageInflater.init(new MainPageInflater.IMainPage() {
                        @Override
                        public void onRecyclerViewInit(RecyclerView recyclerView) {
                            mRecyclerView = recyclerView;
                        }
                        @Override
                        public void onTextViewInit(TextView textView) {
                            txvMainPageMsg = textView;
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    blacklistPageInflater = new BlacklistPageInflater(getApplicationContext(), layouts.get(pageIDs.BlackList.getID()));
                    blacklistPageInflater.init();
                }catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    settingsPageInflater = new SettingsPageInflater(getApplicationContext(), layouts.get(pageIDs.Settings.getID()));
                    settingsPageInflater.init();
                }catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    aboutPageInflater = new AboutPageInflater(getApplicationContext(), layouts.get(pageIDs.About.getID()), guiDialog);
                    aboutPageInflater.init();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        viewPager.setAdapter(layoutPagerAdapter);
        new TextViewEFX().useFX(txvPage, arrowChar + getString(LayoutPagerAdapter.PagerEnum.MainPage.getTitleResId()));
        onViewPager_select(pageIDs.Main.getID());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageScrollStateChanged(int state) {}
            @Override
            public void onPageSelected(int position) {
                new TextViewEFX().useFX(txvPage, arrowChar + getString(LayoutPagerAdapter.PagerEnum.values()[position].getTitleResId()));
                onViewPager_select(position);
            }
        });
        txvNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(viewPager.getCurrentItem() + 1 != layoutPagerAdapter.getCount()
                            && !pageBackwards || viewPager.getCurrentItem() - 1 == -1) {
                        pageBackwards = false;
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                    }else{
                        pageBackwards = true;
                        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
                    }
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }
        });
    }
    private interface ISplash{
        void onShow();
    }
    @SuppressWarnings("deprecation")
    private void showSplash(final ISplash iSplash){
        try {
            settings = new SettingsPageInflater(getApplicationContext(), null).getSettings();
            if (Integer.valueOf(settings.get(4)) == 1) {
                iSplash.onShow();
            } else {
                try {
                    LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    splashDialog = new Dialog(this, android.R.style.Theme_Black);
                    splashDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    splashDialog.getWindow().setType(new RequestDialog().getWindowType());
                    splashDialog.setContentView(inflater.inflate(R.layout.layout_splash, null));
                    /* splashDialog.setCancelable(false); */
                    final TextView txvK3Load = splashDialog.findViewById(R.id.txvK3Load);
                    final String loadDots = "...";
                    final int delay1 = 800, delay2 = 1200;
                    for (int s = 0; s < loadDots.toCharArray().length; s++) {
                        final int i = s;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                txvK3Load.setText("[" + txvK3Load.getText().toString().replace("[", "").replace("]", "") +
                                        String.valueOf(loadDots.toCharArray()[i]) + "]");
                                if (i == loadDots.toCharArray().length - 1) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            iSplash.onShow();
                                        }
                                    }, delay2);
                                }
                            }
                        }, s * delay1);
                    }
                    splashDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    iSplash.onShow();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            iSplash.onShow();
        }
    }
    @SuppressWarnings("deprecation")
    private void showGUI(){
        try {
            showSplash(new ISplash() {
                @Override
                public void onShow() {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    guiDialog = new Dialog(getApplicationContext(), android.R.style.Theme_Black);
                    guiDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    guiDialog.getWindow().setType(new RequestDialog().getWindowType());
                    guiDialog.setContentView(inflater.inflate(R.layout.layout_main, null));
                    initGUI(guiDialog);
                    mainHandler = new Handler(getApplicationContext().getMainLooper());
                    if(splashDialog != null && splashDialog.isShowing()) {
                        splashDialog.cancel();
                    }
                    guiDialog.show();
                    settings = new SettingsPageInflater(getApplicationContext(), null).getSettings();
                    new LProxy(getApplicationContext(), Integer.parseInt(settings.get(0)),
                            Integer.parseInt(settings.get(1)), Integer.parseInt(settings.get(2)),
                            Integer.parseInt(settings.get(3)))
                            .start(new LProxy.IProxyStatus() {
                                @Override
                                public void onReceive(HttpRequest httpRequest, String blacklist) {
                                    httpReqs.add(new HTTPReq(httpRequest.getUri(),
                                            httpRequest.getMethod().name(),
                                            httpRequest.getProtocolVersion().text(),
                                            httpRequest.getDecoderResult(),
                                            getTime(), new FilteredResponse(Integer.parseInt(settings.get(2))).isBlacklisted(httpRequest.getUri(),
                                            blacklist.split("[" + SqliteDBHelper.SPLIT_CHAR + "]"))));
                                    final ArrayList<HTTPReq> tmpHttpReqs = new ArrayList<>(httpReqs);
                                    Collections.reverse(tmpHttpReqs);
                                    Runnable setAdapterRunnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                mRecyclerView.setAdapter(new RequestAdapter(getApplicationContext(), tmpHttpReqs, new RequestAdapter.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(HTTPReq item, int i) {
                                                        mainPageInflater.onDetailDialogItemClick(item, blacklistPageInflater, viewPager, pageIDs.BlackList.getID());
                                                    }
                                                }));
                                                if(txvMainPageMsg != null && txvMainPageMsg.getVisibility() == View.VISIBLE)
                                                    txvMainPageMsg.setVisibility(View.GONE);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    mainHandler.post(setAdapterRunnable);
                                }

                                @Override
                                public void onNotify(NotificationHandler notificationHandler1, HttpProxyServer httpProxyServer1) {
                                    notificationHandler = notificationHandler1;
                                    httpProxyServer = httpProxyServer1;
                                }

                                @Override
                                public void onError(Exception e) {
                                    Log.d(getString(R.string.app_name), e.toString());
                                }

                                private String getTime() {
                                    DateFormat df = new SimpleDateFormat("{HH:mm:ss}", Locale.getDefault());
                                    return df.format(Calendar.getInstance().getTime());
                                }
                            });

                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.d(getString(R.string.app_name), "GUI start error.");
            stopSelf();
        }
    }
    @SuppressWarnings("deprecation")
    private void onViewPager_select(int position){
        String pageNumHTML = "";
        int color;
        for(int i = 0; i < LayoutPagerAdapter.PagerEnum.values().length; i++){
            if(i == position)
                color = ContextCompat.getColor(getApplicationContext(), android.R.color.white);
            else
                color = ContextCompat.getColor(getApplicationContext(), R.color.color2dark);
            pageNumHTML += "<font color=\"" + color + "\">" + String.valueOf(i+1) + " " + "</font>";
        }
        txvNum.setText(Html.fromHtml(pageNumHTML));
    }
    private void showGuiDialog(){
        try{
            if(guiDialog != null && !guiDialog.isShowing()){
                guiDialog.show();
            }else if (guiDialog == null){
                cancelNotifications();
                showGUI();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void cancelNotifications(){
        try {
            notificationHandler.getNotificationManager().cancelAll();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void stopProxy(){
        if(guiDialog != null && guiDialog.isShowing()) {
            guiDialog.cancel();
            guiDialog = null;
        }
        try{
            httpProxyServer.stop();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            httpProxyServer.abort();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelNotifications();
        stopProxy();
    }
}
