package com.example.p_messageapp.message;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.p_messageapp.R;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    private RecyclerView circleRecycle;
    private EditText inputEdit;
    private Button btn_send;
    private List<Msg> msgList = new ArrayList<>();
    private MsgAdapter msgAdapter;
    private LocalReceiver localReceiver;
    private LocalBroadcastManager localBroadcastManager;

    BroadcastMain receiver;

    //内部类，实现BroadcastReceiver
    public class BroadcastMain extends BroadcastReceiver {
        //必须要重载的方法，用来监听是否有广播发送
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("brocast", "------> 接收到消息 ");
//            Toast.makeText(Create9FileActivity.this, intent.getStringExtra("msg"), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Intent intent = new Intent();
        intent.setClass(this, MessageActivity.class);
        startService(intent);

        circleRecycle = (RecyclerView) findViewById(R.id.circle_edit);
        inputEdit = (EditText) findViewById(R.id.et_content);
        btn_send = (Button) findViewById(R.id.btn_send);

        // 初始化消息数据
        initMsgs();

        receiver = new BroadcastMain();
        //新添代码，在代码中注册广播接收程序
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.goods.updata");
        registerReceiver(receiver, filter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        circleRecycle.setLayoutManager(layoutManager);
        msgAdapter = new MsgAdapter(msgList);
        circleRecycle.setAdapter(msgAdapter);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = inputEdit.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SEND);
                    msgList.add(msg);
                    msgAdapter.notifyItemInserted(msgList.size() - 1);// 当有新消息时，刷新适配器
                    circleRecycle.scrollToPosition(msgList.size() - 1);// 将circleRecycle 定位到最后一行
                    inputEdit.setText("");// 清空输入框
                }
            }
        });
    }

    private void initMsgs() {
        Msg msg1 = new Msg("Hello guy.", Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("Hello . Who is that?", Msg.TYPE_SEND);
        msgList.add(msg2);
        Msg msg3 = new Msg("This is Tom. Nice talking to you.", Msg.TYPE_RECEIVED);
        msgList.add(msg3);
    }

    private class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("brocast", "-----> 接收");
            Toast.makeText(MessageActivity.this, "收到本地广播", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Intent intent = new Intent();
//        intent.setClass(this, PassengerService.class);class
        stopService(intent);

    }
}