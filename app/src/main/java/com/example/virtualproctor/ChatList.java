package com.example.virtualproctor;

public class ChatList {
    String name;
    String img_url;
    String time;

    public ChatList(String name, String time) {
        this.name = name;
        this.img_url = null;
        this.time = time;
    }

    public ChatList(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getTime() {
        return time;
    }
}
