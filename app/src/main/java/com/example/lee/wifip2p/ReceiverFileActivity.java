package com.example.lee.wifip2p;

import android.app.AlertDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import listener.onReceiverProgress;
import service.ReceiverService;

/**
 * Created by Lee on 2018/7/26.
 */

public class ReceiverFileActivity extends BaseActivity {

    private static final String TAG = "ReceiverFileActivity";
    
    private Context context = ReceiverFileActivity.this;

    private ReceiverService.MyBinder mBinder;
    
    private AlertDialog.Builder dialog;
    private AlertDialog dia;
    private ProgressBar bar;

    @BindView(R.id.createGroup)
    Button createGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_receiver_file);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(context,ReceiverService.class);
        bindService(intent,conn, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: 移除群组成功");
            }

            @Override
            public void onFailure(int i) {
                Log.d(TAG, "onFailure: 移出群组失败");
            }
        });
    }

    @OnClick(R.id.createGroup)
    public void onViewClicked() {
        manager.createGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: 创建群组成功");
            }

            @Override
            public void onFailure(int i) {
                Log.d(TAG, "onFailure: 创建群组失败");
            }
        });
    }

    public ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBinder = (ReceiverService.MyBinder)iBinder;
            mBinder.registerListener(listener);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };
    public onReceiverProgress listener = new onReceiverProgress() {
        @Override
        public void onBegin() {
            Log.d(TAG, "onBegin: 开始接收文件");
            dialog = new AlertDialog.Builder(context);
            bar = new ProgressBar(context,null,android.R.attr.progressBarStyleHorizontal);
            bar.setMax(100);
            bar.setProgress(0);
            dialog.setView(bar);
            dia = dialog.create();
            dia.show();
        }

        @Override
        public void onProgress(int progress) {
            Log.d(TAG, "onProgress: 接收进度："+progress);
            bar.setProgress(progress);
        }

        @Override
        public void onFailed() {
            Log.d(TAG, "onFailed: 接收失败");
        }

        @Override
        public void onFinish() {
            Log.d(TAG, "onFinish: 接收成功");
        }
    };
}
