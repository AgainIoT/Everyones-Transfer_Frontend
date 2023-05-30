package com.example.sksunny_subway;

import android.os.Parcel;

import androidx.annotation.NonNull;

public class Walk extends ItemTest{
    private String direction;
    private int distance;

    public Walk(String direction, int distance){
        this.direction = direction;
        this.distance = distance;
    }
    public Walk(){
        this("방향", 0);
    }

    protected Walk(Parcel resouce){
        direction = resouce.readString();
        distance = resouce.readInt();
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }
}
