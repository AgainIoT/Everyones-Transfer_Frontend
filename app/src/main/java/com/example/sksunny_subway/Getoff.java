package com.example.sksunny_subway;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Getoff extends ItemTest {

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

//    public static final Parcelable.Creator<Getoff> CREATOR = new Parcelable.Creator<Getoff>() {
//        @Override
//        public Getoff createFromParcel(Parcel in) {
//            return new Getoff(in);
//        }
//
//        @Override
//        public Getoff[] newArray(int size) {
//            return new Getoff[size];
//        }
//    };
//}
