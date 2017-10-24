package com.xiaoyuan.longer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.xiaoyuan.Class.Chat;
import com.xiaoyuan.adapter.ChatAdapter;
import com.xiaoyuan.adapter.MsgAdapter;
import com.xiaoyuan.adapter.NewsAdapter;
import com.xiaoyuan.tools.Toast;

import java.util.ArrayList;
import java.util.List;

public class Chat_Activityh extends Activity {
    private EditText inputText;
    public Button send;
    public static ChatAdapter chatAdapter;
    public static RecyclerView recyclerView_chat;
    public static LinearLayoutManager linearLayoutManager_chat;
    public  List<Chat>chatList=new ArrayList<Chat>();
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initMsgs();
        inputText=(EditText)findViewById(R.id.edtTxt_chat_input);
        send=(Button)findViewById(R.id.library_select_bu);
        send.setOnClickListener(listener);
        context=Chat_Activityh.this;
        chatAdapter=new ChatAdapter(chatList,context);
        linearLayoutManager_chat = new LinearLayoutManager(Chat_Activityh.this);
        recyclerView_chat= (RecyclerView) findViewById(R.id.rv_main);
        recyclerView_chat.setItemAnimator(new DefaultItemAnimator());
        linearLayoutManager_chat.setStackFromEnd(true);
        recyclerView_chat.setLayoutManager(linearLayoutManager_chat);

        recyclerView_chat.setAdapter(chatAdapter);

    }

    Button.OnClickListener listener = new Button.OnClickListener() {
        public void onClick(View v) {
            String content=inputText.getText().toString();
            if (!"".equals(content)) {
                Chat chat = new Chat(content, Chat.TYPE_SEND);
                chatList.add(chat);
                chatAdapter.notifyDataSetChanged();
                inputText.setText("");
            }
        }
    };
    private void initMsgs(){
        Chat chat1=new Chat("你好！",Chat.TYPE_RECEVED);
        chatList.add(chat1);
        Chat chat2=new Chat("好",Chat.TYPE_SEND);
        chatList.add(chat2);
        Chat chat3=new Chat("今天天气真的好！",Chat.TYPE_RECEVED);
        chatList.add(chat3);
        Chat chat4=new Chat("是啊！一起出去去玩吧！",Chat.TYPE_SEND);
        chatList.add(chat4);

    }
}
