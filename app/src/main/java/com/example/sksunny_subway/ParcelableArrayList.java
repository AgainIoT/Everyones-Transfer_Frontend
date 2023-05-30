package com.example.sksunny_subway;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ParcelableArrayList implements Parcelable {
    private ArrayList<ItemTest> list;

    public ParcelableArrayList(ArrayList<ItemTest> list) {
        this.list = list;
    }

    protected ParcelableArrayList(Parcel in) {
        list = new ArrayList<>();
        in.readTypedList(list, ItemTest.CREATOR);
    }

    public ArrayList<ItemTest> getList() {
        return list;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(list);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ParcelableArrayList> CREATOR = new Parcelable.Creator<ParcelableArrayList>() {
        @Override
        public ParcelableArrayList createFromParcel(Parcel in) {
            return new ParcelableArrayList(in);
        }

        @Override
        public ParcelableArrayList[] newArray(int size) {
            return new ParcelableArrayList[size];
        }
    };
}