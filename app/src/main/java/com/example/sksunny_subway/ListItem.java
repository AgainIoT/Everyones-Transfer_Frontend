package com.example.sksunny_subway;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ListItem implements Parcelable {
    String name;
    int resourceId;

    public ListItem(String name, int resourceId) {
        this.name = name;
        this.resourceId = resourceId;
    }

    protected ListItem(Parcel resource) {
        name = resource.readString();
        resourceId = resource.readInt();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getResourceId() {
        return this.resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(resourceId);
    }
}
