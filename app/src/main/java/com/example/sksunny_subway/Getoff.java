package com.example.sksunny_subway;

import android.os.Parcel;

import androidx.annotation.NonNull;

public class Getoff extends ItemTest{

    private int carNo;
    private int doorNo;

    public Getoff(int carNo, int doorNo) {
        this.carNo = carNo;
        this.doorNo = doorNo;
    }
    public Getoff(){
        this(0,0);
    }

    protected Getoff(Parcel resource){
        carNo = resource.readInt();
        carNo = resource.readInt();
    }

    public int getCarNo() {
        return carNo;
    }

    public int getDoorNo() {
        return doorNo;
    }

    public void setCarNo(int carNo) {
        this.carNo = carNo;
    }

    public void setDoorNo(int doorNo) {
        this.doorNo = doorNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(carNo);
        dest.writeInt(doorNo);
    }
}
