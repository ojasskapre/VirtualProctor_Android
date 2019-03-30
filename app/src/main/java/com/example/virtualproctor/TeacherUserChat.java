package com.example.virtualproctor;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class TeacherUserChat extends AppCompatActivity {

    private static final String TAG = "TeacherUserChat";

    //a List of type hero for holding list items
    List<ChatList> chatList;

    //the listview
    ListView listView;

    // handle request
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    SharedPreferences prefs;

    CustomListAdapter adapter;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_user_chat);

        context = getApplicationContext();

        //shared preferences
        prefs = getSharedPreferences(getString(R.string.share_preference_filename), MODE_PRIVATE);

        //initializing objects
        chatList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.chat_list);

        //adding some values to our list
//        chatList.add(new ChatList("Spiderman", "10.25"));
//        chatList.add(new ChatList("Batman", "11.25"));
//        chatList.add(new ChatList("Superman", "10.23"));
//        chatList.add(new ChatList("Iron Man", "18.25"));

        //creating the adapter
        CustomListAdapter adapter = new CustomListAdapter(this, R.layout.list_row, chatList);

        //attaching adapter to the listview
        listView.setAdapter(adapter);
    }

    void request_server_for_list(){
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //setup the url
        String ssn = prefs.getString(getString(R.string.login_username), null);
        if(ssn == null){
            Log.e(TAG, "SSN NOT SET");
            return;
        }

        String url = Config.URL+"get_open_chats?username="+ssn;
        Log.d(TAG, url);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response from server "+response.toString());

                // TODO handle response


                if(adapter != null){
                    adapter.clear();
                }


                // todo fill chatlist

                adapter = new CustomListAdapter(context, R.layout.list_row, chatList);

                listView.setAdapter(adapter);




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
