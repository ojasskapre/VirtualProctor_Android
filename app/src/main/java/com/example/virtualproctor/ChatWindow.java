package com.example.virtualproctor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

public class ChatWindow extends AppCompatActivity {
    ChatView chatView;
    RequestQueue queue;
    static String username;
    static String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        chatView = findViewById(R.id.chat_view);
        queue = Volley.newRequestQueue(getApplicationContext());
//        username = getIntent().getExtras().getString("username");
//        Log.e("RESPONSE", username);
        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener(){
            @Override
            public boolean sendMessage(final ChatMessage chatMessage){
                // perform actual message sending
                message = chatView.getTypedMessage();
                Log.e("RESPONSE", message );
                String url = Config.URL+"android_chat";
                Log.e("RESPONSE", url );
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("RESPONSE", response);
                                chatView.addMessage(new ChatMessage(response, 12, ChatMessage.Type.RECEIVED));
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<>();
                        params.put("query", message);
                        Log.e("RESPONSE", params.toString() );
                        return params;
                    }};
                queue.add(stringRequest);

                // Add this line where you want when you receive the response from server
                return true;
            }
        });
    }
}
