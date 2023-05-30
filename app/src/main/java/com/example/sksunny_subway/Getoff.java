package com.example.sksunny_subway;

import android.os.Parcel;

import androidx.annotation.NonNull;

public class Getoff extends ItemTest{

    private String chamber;

    public Getoff(String chamber) {
        this.chamber = chamber;
    }

    protected Getoff(Parcel resource){
        chamber = resource.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(chamber);
    }
}
