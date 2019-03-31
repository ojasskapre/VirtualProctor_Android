package com.example.virtualproctor;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeacherGroupActivity extends AppCompatActivity {
    private final static String TAG = "TeacherGroupActivity";
    Button mSendNotificationBtn;

    //a List of type hero for holding list items
    List<ChatList> groupList;

    //the listview
    ListView listView;

    // handle request
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    Context context;
    SharedPreferences prefs;

    CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_group);

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        context = getApplicationContext();

        //shared preferences
        prefs = getSharedPreferences(getString(R.string.share_preference_filename), MODE_PRIVATE);

        //initializing objects
        groupList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.group_list);

        //creating the adapter
        CustomListAdapter adapter = new CustomListAdapter(this, R.layout.list_row, groupList, "teacher_group");

        //attaching adapter to the listview
        listView.setAdapter(adapter);

        request_server_for_list();


//        mSendNotificationBtn = findViewById(R.id.sendNotificationBtn);
//
//        mSendNotificationBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Code to broadcast notification to a group
//            }
//        });
    }

    void request_server_for_list(){
        //setup the url
        String ssn = prefs.getString(getString(R.string.login_username), null);
        if(ssn == null){
            Log.e(TAG, "SSN NOT SET");
            return;
        }

        String url = Config.URL+"get_groups";
        Log.d(TAG, url);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response from server "+response.toString());
                try {
                    JSONArray array = new JSONArray(response.toString());

                    for(int i=0; i < array.length(); i++){
                        JSONObject jsonObject = array.getJSONObject(i);
                        String name = jsonObject.optString("group_description").toString();
                        String username = jsonObject.optString("group_id").toString();
                        groupList.add(new ChatList(name, username));
                    }

                    if(adapter != null){
                        adapter.clear();
                    }

                    adapter = new CustomListAdapter(context, R.layout.list_row, groupList, "teacher_group");

                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    Log.e(TAG, "Error in parsing JSON");
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
