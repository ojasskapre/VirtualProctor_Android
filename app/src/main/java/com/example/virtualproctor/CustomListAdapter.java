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

    public CustomListAdapter(Context context, int resource, List<ChatList> chatList) {
        super(context, resource, chatList);
        this.context = context;
        this.resource = resource;
        this.chatList = chatList;
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
        TextView time = view.findViewById(R.id.time);

        //getting the hero of the specified position
        final ChatList chat_user = chatList.get(position);

        //adding values to the list item
        if(chat_user.img_url == null){
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.default_person));
        }
        user_name.setText(chat_user.getName());
        time.setText(chat_user.getTime());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, chat_user.getName());
//                Intent intent = new Intent(context, MainActivity.class);
//                intent.putExtra("TagetUser", chat_user.getName());
//                context.startActivity(intent);
            }
        });


//        //adding a click listener to the button to remove item from the list
//        buttonDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //we will call this method to remove the selected value from the list
//                //we are passing the position which is to be removed in the method
//                removeHero(position);
//            }
//        });

        //finally returning the view
        return view;
    }
}
