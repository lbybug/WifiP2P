package com.example.lee.wifip2p;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;

import java.util.Collection;

import broadcast.WifiBroadcast;
import listener.onWifiP2pListener;
import utils.ThreadPoolUtils;

public class BaseActivity extends Activity implements onWifiP2pListener {
    
    private Context context = BaseActivity.this;

    private static final String TAG = "BaseActivity";

    private ThreadPoolUtils threadPoolUtils;

    private WifiBroadcast broadcast;

    public WifiP2pManager manager;
    public WifiP2pManager.Channel channel;
    public WifiP2pInfo wifiP2pInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcast != null) {
            unregisterReceiver(broadcast);
        }
        if (threadPoolUtils != null) {
            threadPoolUtils.shutDown();
        }
    }

    private void init() {
        manager = (WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(context,getMainLooper(),this);
        broadcast = new WifiBroadcast(manager,channel,this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        registerReceiver(broadcast,filter);
    }

    @Override
    public void wifiP2pEnabled(boolean enabled) {
        Log.d(TAG, "wifiP2pEnabled: 传输通道是否可用："+enabled);
    }

    @Override
    public void onConnection(WifiP2pInfo wifiP2pInfo) {
        if (wifiP2pInfo != null) {
            this.wifiP2pInfo = wifiP2pInfo;
        }
    }

    @Override
    public void onDisconnection() {

    }

    @Override
    public void onDeviceInfo(WifiP2pDevice wifiP2pDevice) {

    }

    @Override
    public void onPeersInfo(Collection<WifiP2pDevice> wifiP2pDeviceList) {

    }

    @Override
    public void onChannelDisconnected() {

    }
}
