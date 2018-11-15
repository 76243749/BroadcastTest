package com.myandroid.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/*
    接收、发送广播
 */

public class MainActivity extends AppCompatActivity {
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    private Button button;
    private LocalReceiver localReceiver;
    private LocalBroadcastManager localBroadcastManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  本地广播
        localBroadcastManager = LocalBroadcastManager.getInstance(this);


        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"click button",Toast.LENGTH_SHORT).show();
                /*
                Intent intent = new Intent("com.myandroid.broadcasttest.MY_BROADCAST");
                //  发送标准广播
                //sendBroadcast(intent);
                //  发送有序广播
                sendOrderedBroadcast(intent,null);
                */

                //  发送本地广播
                Intent intent = new Intent("com.myandroid.broadcasttest.LOCAL_BROADCAST");
                localBroadcastManager.sendBroadcast(intent);


            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver,intentFilter);

        //  发送本地广播
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction("com.myandroid.broadcasttest.LOCAL_BROADCAST");
        //  注册本地广播监听器
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver,intentFilter1);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
        //  删除本地广播监听
        localBroadcastManager.unregisterReceiver(localReceiver);
    }

    class NetworkChangeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()){
                Toast.makeText(context,"打开网络状态",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context,"关闭网络状态",Toast.LENGTH_LONG).show();
            }
            //Toast.makeText(context,"network change",Toast.LENGTH_LONG).show();
        }
    }

    class LocalReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"传送本地广播，只能在本程序里接收",Toast.LENGTH_SHORT).show();
        }
    }
}
