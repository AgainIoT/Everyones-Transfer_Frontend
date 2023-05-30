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

    protected Getoff(Parcel resource){
        carNo = resource.readInt();
        carNo = resource.readInt();
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
