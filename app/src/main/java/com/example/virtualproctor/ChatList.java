package com.example.virtualproctor;

public class ChatList {
    String username;
    String name;
    String img_url;

    public ChatList(String name, String username) {
        this.name = name;
        this.username = username;
        this.img_url = null;
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

    public String getUsername() {
        return username;
    }
}
