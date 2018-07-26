package com.example.lee.wifip2p;

import android.os.Bundle;
import android.view.Window;

/**
 * Created by Lee on 2018/7/26.
 */

public class ReceiverFileActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_receiver_file);
    }
}
