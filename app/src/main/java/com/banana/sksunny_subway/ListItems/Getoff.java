package com.banana.sksunny_subway.ListItems;

import android.os.Parcel;

import androidx.annotation.NonNull;

public class Getoff extends ListItem {

    private int carNo;
    private int doorNo;

    public Getoff(String abstractClassMemberString, int carNo, int doorNo) {
        super(abstractClassMemberString);
        this.carNo = carNo;
        this.doorNo = doorNo;
    }

    public Getoff() {
        this("4", 0, 0);
    }

    protected Getoff(Parcel resource) {
        super(resource);
        carNo = resource.readInt();
        doorNo = resource.readInt();
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
        dest.writeInt(CLASS_TYPE_FOUR);
        super.writeToParcel(dest, flags);
        dest.writeInt(carNo);
        dest.writeInt(doorNo);
    }
}
