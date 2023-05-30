package com.example.sksunny_subway;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class ItemTest implements Parcelable {
    int resourceId;
    public static final Creator<ListItem> CREATOR = new Creator<ListItem>() {
        @Override
        public ListItem createFromParcel(Parcel source) {
            return new ListItem(source);
        }

        @Override
        public ListItem[] newArray(int size) {
            return new ListItem[size];
        }
    };

    public void setNumber(int resourceId) {
        this.resourceId = resourceId;
    }
    public int getResourceId() {
        return this.resourceId;
    }
    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}
