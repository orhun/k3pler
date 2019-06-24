package com.k3.k3pler.sub;

import android.content.Context;
import android.os.AsyncTask;

import com.k3.k3pler.R;
import com.k3.k3pler.handler.NotificationHandler;

import org.littleshoot.proxy.HttpProxyServer;

/** Get proxy details and show with notificationHandler **/
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