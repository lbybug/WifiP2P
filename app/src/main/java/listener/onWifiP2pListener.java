package listener;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;

import java.util.Collection;

/**
 * Created by Lee on 2018/7/26.
 */

public interface onWifiP2pListener extends WifiP2pManager.ChannelListener{

    void wifiP2pEnabled(boolean enabled);

    void onConnection(WifiP2pInfo wifiP2pInfo);

    void onDisconnection();

    void onDeviceInfo(WifiP2pDevice wifiP2pDevice);

    void onPeersInfo(Collection<WifiP2pDevice> wifiP2pDeviceList);
}
