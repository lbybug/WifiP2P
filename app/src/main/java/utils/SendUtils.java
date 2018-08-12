package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import listener.onSendProgress;

/**
 * Created by LLB on 2018/8/12.
 */

public class SendUtils {

    public ThreadPoolUtils threadPoolUtils;

    public static final int PORT = 10000;

    public InputStream is;
    public String address;


    public SendUtils(InputStream is,String address){
        this.is = is;
        this.address = address;
    }

    public void startSend(onSendProgress listener){
        try {
            if (threadPoolUtils == null) {
                threadPoolUtils = ThreadPoolUtils.getInstance();
            }
            listener.onStart();
            Socket socket = new Socket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(address, PORT);
            socket.connect(inetSocketAddress);
            OutputStream os = socket.getOutputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
