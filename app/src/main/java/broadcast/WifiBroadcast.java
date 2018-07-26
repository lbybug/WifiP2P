package broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;

import listener.onWifiP2pListener;

/**
 * Created by Lee on 2018/7/26.
 */

public class WifiBroadcast extends BroadcastReceiver{

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private onWifiP2pListener listener;

    public WifiBroadcast(WifiP2pManager manager,WifiP2pManager.Channel channel,onWifiP2pListener listener){
        this.manager = manager;
        this.channel = channel;
        this.listener = listener;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        switch (action){
            case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:
                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                    listener.wifiP2pEnabled(true);
                }else {
                    listener.wifiP2pEnabled(false);
                }
                break;
            case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                NetworkInfo info = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_STATE);
                if(info.isConnected()){
                    manager.requestConnectionInfo(channel, new WifiP2pManager.ConnectionInfoListener() {
                        @Override
                        public void onConnectionInfoAvailable(WifiP2pInfo info) {
                            listener.onConnection(info);
                        }
                    });
                }else {
                    listener.onDisconnection();
                }
                break;
            case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                manager.requestPeers(channel, new WifiP2pManager.PeerListListener() {
                    @Override
                    public void onPeersAvailable(WifiP2pDeviceList peers) {
                        listener.onPeersInfo(peers.getDeviceList());
                    }
                });
                break;
            case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:
                WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
                listener.onDeviceInfo(device);
                break;

        }


    }
}
