package com.example.android.hybridproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by eisat on 3/17/2018.
 */

public class Customer implements Parcelable{
    private String mFirstName;
    private String mLastName;
    private int mMoney;
    private int mCapacity;
    private String mID;
    private ArrayList<Boardgame> mInventory;

    public Customer(String firstName, String lastName, int money, int capacity, String id, ArrayList<Boardgame> inventory){
        mFirstName = firstName;
        mLastName = lastName;
        mMoney = money;
        mCapacity = capacity;
        mID = id;
        mInventory = inventory;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public int getMoney() {
        return mMoney;
    }

    public int getCapacity() {
        return mCapacity;
    }

    public String getID() {
        return mID;
    }

    public ArrayList<Boardgame> getInventory(){
        return mInventory;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mFirstName);
        parcel.writeString(mLastName);
        parcel.writeInt(mMoney);
        parcel.writeInt(mCapacity);
        parcel.writeString(mID);
        parcel.writeList(mInventory);
    }


    public static final Parcelable.Creator<Customer> CREATOR
            = new Parcelable.Creator<Customer>() {
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    private Customer(Parcel in) {
        mFirstName = in.readString();
        mLastName = in.readString();
        mMoney = in.readInt();
        mCapacity = in.readInt();
        mID = in.readString();
        mInventory = in.readArrayList(String.class.getClassLoader());
    }
}
