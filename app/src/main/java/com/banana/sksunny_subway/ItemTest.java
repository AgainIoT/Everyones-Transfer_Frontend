package com.banana.sksunny_subway;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public abstract class ItemTest implements Parcelable {

    public static final int CLASS_TYPE_ONE = 1;
    public static final int CLASS_TYPE_TWO = 2;
    public static final int CLASS_TYPE_THREE = 3;
    public static final int CLASS_TYPE_FOUR = 4;

    int resourceId;

    protected String mabstractClassMemberString;

    public ItemTest(String abstractClassMemberString){
        mabstractClassMemberString = abstractClassMemberString;
    };

    protected ItemTest(Parcel resource) {
        mabstractClassMemberString = resource.readString();
        this.resourceId = resource.readInt();
    }

    public ItemTest(int resourceId) {
        this.resourceId = resourceId;
    }

    public static final Creator<ItemTest> CREATOR = new Creator<ItemTest>() {
        @Override
        public ItemTest createFromParcel(Parcel source) {
            return ItemTest.getConcreteClass(source);
        }

        @Override
        public ItemTest[] newArray(int size) {
            return new ItemTest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(mabstractClassMemberString);
        dest.writeInt(resourceId);
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

    public static ItemTest getConcreteClass(Parcel source){
        switch (source.readInt()){
            case CLASS_TYPE_ONE:
                return new Elevator(source);
            case CLASS_TYPE_TWO:
                return new Walk(source);
            case CLASS_TYPE_THREE:
                return new Pass(source);
            case CLASS_TYPE_FOUR:
                return new Getoff(source);
            default:
                return null;
        }
    }
}