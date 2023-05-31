package com.example.sksunny_subway;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Elevator extends ItemTest{
    private String startFloor;
    private String endFloor;

    public Elevator(String abstractClassMemberString, String startFloor, String endFloor){
        super(abstractClassMemberString);
        this.startFloor = startFloor;
        this.endFloor = endFloor;
    }
    public Elevator(){
        this("1","출발층", "도착층");
    }


    protected Elevator(Parcel resource) {
        super(resource);
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
        dest.writeInt(CLASS_TYPE_ONE);
        super.writeToParcel(dest, flags);
        dest.writeString(startFloor);
        dest.writeString(endFloor);
    }

//    public static final Parcelable.Creator<Elevator> CREATOR = new Parcelable.Creator<Elevator>() {
//        @Override
//        public Elevator createFromParcel(Parcel in) {
//            return new Elevator(in);
//        }
//
//        @Override
//        public Elevator[] newArray(int size) {
//            return new Elevator[size];
//        }
//    };
}
