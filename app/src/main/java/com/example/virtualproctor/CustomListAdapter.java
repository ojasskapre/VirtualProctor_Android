package com.example.virtualproctor;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class CustomListAdapter extends ArrayAdapter<ChatList> {

    private static final String TAG = "CustomListAdapter";

    //the list values in the List of type hero
    List<ChatList> chatList;

    //activity context
    Context context;

    //the layout resource file for the list items
    int resource;

    String adapter_type = null;

    public CustomListAdapter(Context context, int resource, List<ChatList> chatList, String type) {
        super(context, resource, chatList);
        this.context = context;
        this.resource = resource;
        this.chatList = chatList;
        this.adapter_type = type;
    }



    //this will return the ListView Item as a View
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //we need to get the view of the xml for our list item
        //And for this we need a layoutInflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //getting the view
        View view = layoutInflater.inflate(resource, null, false);

        //getting the view elements of the list from the view
        ImageView imageView = view.findViewById(R.id.list_image);
        final TextView user_name = view.findViewById(R.id.name);

        //getting the hero of the specified position
        final ChatList chat_user = chatList.get(position);

        //adding values to the list item
        if(chat_user.img_url == null){
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.default_person));
        }
        user_name.setText(chat_user.getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(adapter_type.equals("teacher_group")){
                    Log.d(TAG, chat_user.getUsername());
                    Intent intent = new Intent(context, TeacherGroupMain.class);
                    intent.putExtra("to_group", chat_user.getUsername());
                    context.startActivity(intent);
                }else if(adapter_type.equals("personal_chat")){
                    Log.d(TAG, chat_user.getUsername());
                    Intent intent = new Intent(context, TeacherChatWindow.class);
                    intent.putExtra("to_user", chat_user.getUsername());
                    context.startActivity(intent);
                }
            }
        });

        //finally returning the view
        return view;
    }
}
