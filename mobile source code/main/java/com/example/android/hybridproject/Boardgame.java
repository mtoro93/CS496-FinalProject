package com.example.android.hybridproject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by eisat on 3/16/2018.
 */

public class Boardgame implements Parcelable {

    private String mTitle;
    private String mDescription;
    private int mPrice;
    private int mStock;
    private String mID;

    public Boardgame(String title, String description, int price, int stock, String id){
        mTitle = title;
        mDescription = description;
        mPrice = price;
        mStock = stock;
        mID = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getPrice() {
        return mPrice;
    }

    public int getStock() {
        return mStock;
    }

    public String getID() {
        return mID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mDescription);
        parcel.writeInt(mPrice);
        parcel.writeInt(mStock);
        parcel.writeString(mID);
    }


    public static final Parcelable.Creator<Boardgame> CREATOR
            = new Parcelable.Creator<Boardgame>() {
        public Boardgame createFromParcel(Parcel in) {
            return new Boardgame(in);
        }

        public Boardgame[] newArray(int size) {
            return new Boardgame[size];
        }
    };

    private Boardgame(Parcel in) {
        mTitle = in.readString();
        mDescription = in.readString();
        mPrice = in.readInt();
        mStock = in.readInt();
        mID = in.readString();
    }
}
