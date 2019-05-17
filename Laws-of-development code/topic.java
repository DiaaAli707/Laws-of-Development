package com.example.dali.newsapp;


public class topic {
    String name,img;
    String topicID;


    public topic(String name,String img, String topicID) {
        this.img = img;
        this.name = name;
        this.topicID = topicID;

    }

    public String getImgg() {
        return img;
    }

    public void setImgg(String imgg) {
        this.img = imgg;
    }

    public String getTopicID() {
        return topicID;
    }

    public void setTopicID(String topicID) {
        this.topicID = topicID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
