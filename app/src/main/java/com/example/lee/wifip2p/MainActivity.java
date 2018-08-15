package com.example.lee.wifip2p;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private Context context = MainActivity.this;

    @BindView(R.id.sendButton)
    Button sendButton;
    @BindView(R.id.receiverButton)
    Button receiverButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.sendButton, R.id.receiverButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sendButton:
                startActivity(new Intent(context,SendFileActivity.class));
                break;
            case R.id.receiverButton:
                startActivity(new Intent(context,ReceiverFileActivity.class));
                break;
        }
    }
}
