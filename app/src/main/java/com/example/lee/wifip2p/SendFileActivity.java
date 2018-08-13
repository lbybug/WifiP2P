package com.example.lee.wifip2p;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import listener.onSendProgress;
import utils.SendUtils;
import utils.UriUtils;


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

    public static final int FILE_RESULT = 0x01;

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
                Intent chooseIntent = new Intent();
                chooseIntent.setType("*/*");
                chooseIntent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(chooseIntent, FILE_RESULT);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FILE_RESULT:
                    try {
                        Uri uri = data.getData();
                        if (uri != null) {
                            String path = UriUtils.getFileAbsolutePath(this,uri);
                            if (path != null) {
                                Log.d(TAG, "onActivityResult: 文件路径为："+path);
                                File file = new File(path);
                                if (!file.exists() || wifiP2pInfo ==null){
                                    Toast.makeText(context, "文件不存在", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                String address = wifiP2pInfo.groupOwnerAddress.getHostAddress();
                                sendFile(file,address);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private void sendFile(File file,String address) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("传输进度");
        dialog.setCancelable(false);
        final ProgressBar bar = new ProgressBar(context,null,android.R.attr.progressBarStyleHorizontal);
        bar.setMax(100);
        bar.setProgress(0);
        dialog.setView(bar);
        final AlertDialog dia = dialog.create();
        dia.show();
        SendUtils utils = new SendUtils(file,address);
        utils.startSend(new onSendProgress() {
            @Override
            public void onStart() {
                Log.d(TAG, "onStart: 传输通道准备完成，开始发送");
            }
            @Override
            public void onProgress(int progress) {
                Log.d(TAG, "onProgress: 传输进度："+progress);
                bar.setProgress(progress);
            }
            @Override
            public void onFailed() {
                Log.d(TAG, "onFailed: 传输失败");
                dia.dismiss();
            }
            @Override
            public void onFinish() {
                Log.d(TAG, "onFinish: 传输完成");
                dia.dismiss();
            }
        });
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
    public void onPeersInfo(Collection<WifiP2pDevice> wifiP2pDeviceList) {  //获取到设备信息，进行筛选，符合的展示
        super.onPeersInfo(wifiP2pDeviceList);
        for (WifiP2pDevice wifiP2pDevice : wifiP2pDeviceList) {
            if (!deviceName.contains(wifiP2pDevice.deviceName) && !deviceArray.contains(wifiP2pDevice)) {
                deviceArray.add(wifiP2pDevice);
                deviceName.add(wifiP2pDevice.deviceName + "-" + wifiP2pDevice.deviceAddress);
            }
        }
        showDeviceList();
    }

    private void showDeviceList() {  //将搜索到的设备展示出来
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, deviceName);
        deviceList.setAdapter(adapter);
        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                connectDevice(deviceArray.get(position));
            }
        });
    }

    private void connectDevice(final WifiP2pDevice wifiP2pDevice) { //连接设备
        WifiP2pConfig config = new WifiP2pConfig();
        if (wifiP2pDevice != null) {
            config.deviceAddress = wifiP2pDevice.deviceAddress;
            config.wps.setup = WpsInfo.PBC;
            manager.connect(channel, config, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "onSuccess: 与" + wifiP2pDevice.deviceName + "连接成功");
                    Toast.makeText(context, "与" + wifiP2pDevice.deviceName + "连接成功", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(int i) {
                    Log.d(TAG, "onFailure: 连接失败");
                    Toast.makeText(context, "连接失败", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
