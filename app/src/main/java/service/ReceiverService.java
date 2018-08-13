package service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import listener.onReceiverProgress;
import utils.ReceiverUtils;

public class ReceiverService extends IntentService {

    private static final String TAG = "ReceiverService";

    private ReceiverUtils receiverUtils;

    public ReceiverService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        receiverUtils = new ReceiverUtils();
        receiverUtils.startReceiver();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder{
        public MyBinder(){super();}
        public void registerListener(onReceiverProgress listener){
            receiverUtils.setOnReceiverListener(listener);
        }
    }
}
