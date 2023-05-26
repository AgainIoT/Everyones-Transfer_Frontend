package com.example.sksunny_subway;

public class ListItem {
    String name;
    int resourceId;

    public ListItem(String name, int resourceId){
        this.name = name;
        this.resourceId = resourceId;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getResourceId(){
        return this.resourceId;
    }
    public void setResourceId(int resourceId){
        this.resourceId = resourceId;
    }


}
