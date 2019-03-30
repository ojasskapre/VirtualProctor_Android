package com.example.virtualproctor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

public class TeacherChatWindow extends AppCompatActivity {
    ChatView chatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_chat_window);

        chatView = findViewById(R.id.chat_view);

        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener(){
            @Override
            public boolean sendMessage(ChatMessage chatMessage){
                // perform actual message sending

                // Add this line where you want when you receive the response from server
                chatView.addMessage(new ChatMessage("sdfsd", 12, ChatMessage.Type.RECEIVED));
                return true;
            }
        });
    }
}
