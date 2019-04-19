package com.example.randy.fragmentdemo;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.randy.observerdemo.CalculateInterface;

/**
 * @author randy 客户端
 */
public class CalculateClient extends AppCompatActivity {
    private static final String TAG = "CalculateClient";

    private Button btnCalculate, btnCalculate2;

    private EditText etNum1;

    private EditText etNum2;

    private TextView tvResult;

    private CalculateInterface mService;

    private int type = 0;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }

        //因为有可能有多个应用同时进行RPC操作，所以同步该方法
        @Override
        public synchronized void onServiceConnected(ComponentName arg0, IBinder binder) {
            //获得IPerson接口
            mService = CalculateInterface.Stub.asInterface(binder);
            if (mService != null) {
                //RPC方法调用
                Toast.makeText(CalculateClient.this, "远程进程调用成功！值为 ： " + "1", Toast.LENGTH_LONG).show();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNum1 = (EditText) findViewById(R.id.editText);
        etNum2 = (EditText) findViewById(R.id.editText2);

        tvResult = (TextView) findViewById(R.id.textView3);

        btnCalculate = (Button) findViewById(R.id.button);
        btnCalculate2 = (Button) findViewById(R.id.button2);

        btnCalculate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (type == 0) {
                    return;
                }
                logE("开始远程运算");
                try {
                    double num1 = Double.parseDouble(etNum1.getText().toString());
                    double num2 = Double.parseDouble(etNum2.getText().toString());
                    String answer = "计算结果：" + mService.doCalculate(num1, num2);
                    tvResult.setTextColor(Color.BLUE);
                    tvResult.setText("计算结果：" + answer);

                } catch (RemoteException e) {
                }
            }
        });
        btnCalculate2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                logE("开始绑定");
//                Bundle args = new Bundle();
//                Intent intent = new Intent();
//                intent.setAction("MyService");
//                intent.setPackage("com.example.randy.observerdemo");
//                intent.putExtras(args);
//                bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
                Intent intent = new Intent();
                ComponentName componentName = new ComponentName("com.example.randy.observerdemo", "com.example.randy.observerdemo.MyService");
                intent.setComponent(componentName);
                intent.setAction("MyService");
                bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
                type = 1;
            }
        });
    }

    private void logE(String str) {
        Log.e(TAG, "--------" + str + "--------");
    }
}