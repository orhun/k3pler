package com.tht.k3pler.sub;

import android.content.Context;
import android.os.AsyncTask;

import com.tht.k3pler.R;
import com.tht.k3pler.handler.NotificationHandler;

import org.littleshoot.proxy.HttpProxyServer;

public class ProxyNotifier extends AsyncTask<Void, String, String> {
    private Context context;
    private HttpProxyServer httpProxyServer;
    private NotificationHandler notificationHandler;

    public ProxyNotifier(Context context, HttpProxyServer httpProxyServer,
                          NotificationHandler notificationHandler){
        this.context = context;
        this.httpProxyServer = httpProxyServer;
        this.notificationHandler = notificationHandler;
    }

    @Override
    protected String doInBackground(Void... voids) {
        return httpProxyServer.getListenAddress().getHostName() + ":" +
                String.valueOf(httpProxyServer.getListenAddress().getPort());
    }
    @Override
    protected void onPostExecute(String str) {
        super.onPostExecute(str);
        notificationHandler.notify(context.getString(R.string.app_name),
                context.getString(R.string.proxy_running) + " [" + str + "]", true);
    }
}