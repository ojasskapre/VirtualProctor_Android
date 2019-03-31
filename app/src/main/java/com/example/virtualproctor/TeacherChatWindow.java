package com.example.virtualproctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

import static com.example.virtualproctor.ChatWindow.message;

public class TeacherChatWindow extends AppCompatActivity {
    private static final String TAG = "TeacherChatWindow";
    ChatView chatView;

    static String to_user;
    static String from_user;
    SharedPreferences prefs;

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_chat_window);

        // get the targer_username
        Bundle extras = getIntent().getExtras();
        prefs = getSharedPreferences(getString(R.string.share_preference_filename), MODE_PRIVATE);


        from_user = prefs.getString(getString(R.string.login_username), null);
        to_user = extras.getString("to_user");
        String title = extras.getString("title_name");
        setTitle(title);

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        chatView = findViewById(R.id.chat_view);


        // endpoint : get_all_chats?user1= &user2=

        if(from_user!=null && to_user!=null){
            load_previous_chats();
        }


        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener(){
            @Override
            public boolean sendMessage(ChatMessage chatMessage){
                // perform actual message sending
                message = chatView.getTypedMessage();
                Log.e("RESPONSE", message );

                sendMessageToServer(message);

//                // Add this line where you want when you receive the response from server
//                chatView.addMessage(new ChatMessage("sdfsd", 12, ChatMessage.Type.RECEIVED));
                return true;
            }
        });
    }

    void sendMessageToServer(String message){
        String password = prefs.getString(getString(R.string.login_password), null);

        if(password == null){
            Log.e(TAG, "password not set");
            return;
        }

        String url = Config.URL+"insert_chat";
        Log.d(TAG, url);

        try{
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("from_user", from_user);
            jsonBody.put("to_user", to_user);
            jsonBody.put("msg_body", message);
            jsonBody.put("password", password);
            final String requestBody = jsonBody.toString();
            Log.e(TAG, requestBody);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }){
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

            stringRequest.setRetryPolicy(mRetryPolicy);

            mRequestQueue.add(stringRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void load_previous_chats(){
        Log.d(TAG, "load text called");

        //setup the url
        String ssn = prefs.getString(getString(R.string.login_username), null);
        if(ssn == null){
            Log.e(TAG, "SSN NOT SET");
            return;
        }

        String url = Config.URL+"get_all_chats?user1="+from_user+"&user2="+to_user;
        Log.d(TAG, url);

        //String Request initialized
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response from server "+response.toString());

                try {
                    JSONArray array = new JSONArray(response.toString());
                    for(int i=0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);

                        String msg_from_user = jsonObject.optString("from_user").toString();
                        String msg_body = jsonObject.optString("msg_body").toString();
                        String msg_time = jsonObject.optString("msg_time").toString();
                        String msg_to_user = jsonObject.optString("to_user").toString();

                        if(from_user.equals(msg_from_user)){
                            chatView.addMessage(new ChatMessage(msg_body, 12, ChatMessage.Type.SENT));
                        }else{
                            chatView.addMessage(new ChatMessage(msg_body, 12, ChatMessage.Type.RECEIVED));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
    }
}
