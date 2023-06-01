package com.banana.sksunny_subway;

import android.os.Parcel;

import androidx.annotation.NonNull;

public class Pass extends ItemTest {

    public Pass() {
        this("3");
    }

    public Pass(String abstractClassMemberString) {
        super(abstractClassMemberString);
    }

    ;

    protected Pass(Parcel resource) {
        super(resource);
    }

    ;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(CLASS_TYPE_THREE);
        super.writeToParcel(dest, flags);
    }
}
