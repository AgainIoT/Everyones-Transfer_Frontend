package com.example.sksunny_subway;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Pass extends ItemTest{

    public Pass(){
        this("3");
    }

    public Pass(String abstractClassMemberString){
        super(abstractClassMemberString);
    };

    protected Pass(Parcel resource){
        super(resource);
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(CLASS_TYPE_THREE);
        super.writeToParcel(dest, flags);
    }
//    public static final Parcelable.Creator<Pass> CREATOR = new Parcelable.Creator<Pass>() {
//        @Override
//        public Pass createFromParcel(Parcel in) {
//            return new Pass(in);
//        }
//
//        @Override
//        public Pass[] newArray(int size) {
//            return new Pass[size];
//        }
//    };
}
