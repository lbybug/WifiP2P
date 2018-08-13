package utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import listener.onReceiverProgress;

/**
 * Created by LLB on 2018/8/12.
 */

public class ReceiverUtils {

    private static final String TAG = "ReceiverUtils";

    public onReceiverProgress listener;

    private ThreadPoolUtils threadPoolUtils;

    private ServerSocket serverSocket;
    private Socket socket;

    public static final int PORT = 8088;

    public void setOnReceiverListener(onReceiverProgress listener){
        this.listener = listener;
    }

    public void startReceiver(){
        if (threadPoolUtils == null) {
            threadPoolUtils = ThreadPoolUtils.getInstance();
        }
        threadPoolUtils.addTask(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket();
                    serverSocket.setReuseAddress(true);
                    serverSocket.bind(new InetSocketAddress(PORT));
                    socket = serverSocket.accept();
                    Log.d(TAG, "run: 有客户端接入，IP地址为："+socket.getRemoteSocketAddress());
                    InputStream is = socket.getInputStream();
                    File file = new File(Environment.getExternalStorageDirectory()+File.separator+"WifiP2PFile"+File.separator+"");
                    FileOutputStream os = new FileOutputStream();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
