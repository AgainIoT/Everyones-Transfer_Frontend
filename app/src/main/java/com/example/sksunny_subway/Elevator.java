package com.example.sksunny_subway;

import android.os.Parcel;

import androidx.annotation.NonNull;

public class Elevator extends ItemTest{

    private String startFloor;
    private String endFloor;

    Elevator(String startFloor, String endFloor){
        this.startFloor = startFloor;
        this.endFloor = endFloor;
    }
    Elevator(){
        this("출발층", "도착층");
    }


    protected Elevator(Parcel resource) {
        startFloor = resource.readString();
        endFloor = resource.readString();
    }

    public String getStartFloor() {
        return startFloor;
    }

    public void setStartFloor(String startFloor) {
        this.startFloor = startFloor;
    }

    public String getEndFloor() {
        return endFloor;
    }

    public void setEndFloor(String endFloor) {
        this.endFloor = endFloor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(startFloor);
        dest.writeString(endFloor);
    }
}
