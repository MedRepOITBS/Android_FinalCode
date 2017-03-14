package com.app.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by masood on 8/13/15.
 */
public class Address implements Parcelable{

    private String addressLine;
    private String doornumber;
    private String city;
    private String state;
    private String zipcode;


    public String getDoornumber() {
        return doornumber;
    }

    public void setDoornumber(String doornumber) {
        this.doornumber = doornumber;
    }



    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }



    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }



    public Address(){

    }

    public Address(Parcel in){
        addressLine = in.readString();
        doornumber = in.readString();
        city = in.readString();
        state = in.readString();
        zipcode = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(addressLine);
        dest.writeString(doornumber);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(zipcode);
    }

    public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address>()
    {
        @Override
        public Address createFromParcel(Parcel in)
        {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size)
        {
            return new Address[size];
        }
    };
}
