package com.example.virtualproctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
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
    SharedPreferences prefs;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.parent_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        chatView = findViewById(R.id.chat_view);
        queue = Volley.newRequestQueue(getApplicationContext());
        prefs = getSharedPreferences(getString(R.string.share_preference_filename), MODE_PRIVATE);
        username = prefs.getString(getString(R.string.login_username), null);
        Log.e("RESPONSE", username);
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
                                    if (response.substring(0,7).equals("teacher")) {
                                        Intent intent = new Intent(getApplicationContext(), TeacherChatWindow.class);
                                        intent.putExtra("to_user", response.substring(7,14));
                                        intent.putExtra("title_name", response.substring(14,response.length()));
                                        startActivity(intent);
                                }else {
                                    chatView.addMessage(new ChatMessage(response, 12, ChatMessage.Type.RECEIVED));
                                }
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
                        params.put("username", username);
                        params.put("query", message);
                        Log.e("RESPONSE", params.toString() );
                        return params;
                    }};
                RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

                stringRequest.setRetryPolicy(mRetryPolicy);
                queue.add(stringRequest);

                // Add this line where you want when you receive the response from server
                return true;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.previous_chats:
                startActivity(new Intent(getApplicationContext(), TeacherUserChat.class));
                // do something
                return true;
            case R.id.notification_menu:
                // do something
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
