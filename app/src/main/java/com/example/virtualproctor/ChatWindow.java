package com.example.virtualproctor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import co.intentservice.chatui.ChatView;

public class ChatWindow extends AppCompatActivity {
    ChatView chatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        chatView = findViewById(R.id.chat_view);
    }
}
