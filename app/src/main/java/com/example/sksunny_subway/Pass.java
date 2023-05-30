package com.example.sksunny_subway;

import android.os.Parcel;

import androidx.annotation.NonNull;

public class Pass extends ItemTest{

    public Pass(){};

    protected Pass(Parcel resource){};

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }
}
