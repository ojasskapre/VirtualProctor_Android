package com.example.virtualproctor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class TeacherUserChat extends AppCompatActivity {

    //a List of type hero for holding list items
    List<ChatList> chatList;

    //the listview
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_user_chat);

        //initializing objects
        chatList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.chat_list);

        //adding some values to our list
        chatList.add(new ChatList("Spiderman", "10.25"));
        chatList.add(new ChatList("Batman", "11.25"));
        chatList.add(new ChatList("Superman", "10.23"));
        chatList.add(new ChatList("Iron Man", "18.25"));

        //creating the adapter
        CustomListAdapter adapter = new CustomListAdapter(this, R.layout.list_row, chatList);

        //attaching adapter to the listview
        listView.setAdapter(adapter);


    }
}
