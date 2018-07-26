package com.example.lee.wifip2p;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lee on 2018/7/26.
 */

public class SendFileActivity extends BaseActivity {

    @BindView(R.id.discoveryDevice)
    Button discoveryDevice;
    @BindView(R.id.chooseFile)
    Button chooseFile;
    @BindView(R.id.deviceList)
    ListView deviceList;

    private Context context = SendFileActivity.this;

    private static final String TAG = "SendFileActivity";

    private ArrayList<String> deviceName = new ArrayList<>();
    private ArrayList<WifiP2pDevice> deviceArray = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_send_file);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.discoveryDevice, R.id.chooseFile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.discoveryDevice:
                discoveryDevice();
                break;
            case R.id.chooseFile:
                break;
        }
    }

    private void discoveryDevice() {
        deviceArray.clear();
        deviceName.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: 搜索设备成功");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "onFailure: 搜索设备失败");
            }
        });
    }

    @Override
    public void onPeersInfo(Collection<WifiP2pDevice> wifiP2pDeviceList) {
        super.onPeersInfo(wifiP2pDeviceList);
        for (WifiP2pDevice wifiP2pDevice : wifiP2pDeviceList) {
                if(!deviceName.contains(wifiP2pDevice.deviceName) && !deviceArray.contains(wifiP2pDevice)){
                    deviceArray.add(wifiP2pDevice);
                    deviceName.add(wifiP2pDevice.deviceName+"-"+wifiP2pDevice.deviceAddress);
                }
        }
        showDeviceList();
    }

    private void showDeviceList() {
        adapter= new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,deviceName);
        deviceList.setAdapter(adapter);
        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
