package utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import listener.onReceiverProgress;
import model.FileBean;

/**
 * Created by LLB on 2018/8/12.
 */

public class ReceiverUtils {

    private static final String TAG = "ReceiverUtils";

    public onReceiverProgress listener;

    private ThreadPoolUtils threadPoolUtils;

    private ServerSocket serverSocket;
    private Socket socket;

    private InputStream is;
    private ObjectInputStream objectInputStream;
    private OutputStream os;

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
                    is = socket.getInputStream();
                    objectInputStream = new ObjectInputStream(is);
                    FileBean bean = (FileBean) objectInputStream.readObject();
                    String fileName = bean.getFileName();
                    Log.d(TAG, "run: 准备接收"+fileName);
                    File file = new File(Environment.getExternalStorageDirectory()+File.separator+"Wifi2PFile"+File.separator+fileName);
                    if (!file.exists()){
                        file.createNewFile();
                    }
                    os = new FileOutputStream(file);
                    listener.onBegin();
                    byte bytes[] = new byte[1024];
                    int len;
                    long total = 0;
                    while ((len = is.read(bytes)) != -1){
                        os.write(bytes,0,len);
                        total += len;
                        int progress = (int)((total*100)/bean.getFileSize());
                        listener.onProgress(progress);
                    }
                    if (file.length() == bean.getFileSize()){
                        listener.onFinish();
                    }else {
                        listener.onFailed();
                    }
                    is.close();
                    serverSocket.close();
                    os.close();
                    objectInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailed();
                }
            }
        });
    }

    public void clean(){
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            if (socket != null) {
                socket.close();
            }
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
            if (objectInputStream != null) {
                objectInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
