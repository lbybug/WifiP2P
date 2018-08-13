package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import listener.onSendProgress;
import model.FileBean;

/**
 * Created by LLB on 2018/8/12.
 */

public class SendUtils {

    public ThreadPoolUtils threadPoolUtils;

    public static final int PORT = 8088;

    public File file;
    public String address;


    public SendUtils(File file, String address){
        this.file = file;
        this.address = address;
    }

    public void startSend(final onSendProgress listener){
        if (threadPoolUtils == null) {
            threadPoolUtils = ThreadPoolUtils.getInstance();
        }
        threadPoolUtils.addTask(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket();
                    InetSocketAddress inetSocketAddress = new InetSocketAddress(address, PORT);
                    socket.connect(inetSocketAddress);
                    listener.onStart();
                    OutputStream os = socket.getOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
                    FileBean bean = new FileBean.Builder(file.getName(),file.length()).build();
                    objectOutputStream.writeObject(bean);
                    FileInputStream is = new FileInputStream(file);
                    long total = 0;
                    int len;
                    byte bytes[] = new byte[1024];
                    while((len = is.read(bytes)) != -1){
                        os.write(bytes,0,len);
                        total += len;
                        int progress = (int)((total*100)/file.length());
                        listener.onProgress(progress);
                    }
                    os.flush();
                    os.close();
                    is.close();
                    socket.close();
                    objectOutputStream.flush();
                    objectOutputStream.close();
                    listener.onFinish();
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onFailed();
                }
            }
        });

    }
}
