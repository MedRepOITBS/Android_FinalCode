package com.app.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by masood on 9/19/15.
 */
public class Therapeutic implements Parcelable {

    private int therapeuticId;

    public int getTherapeuticId() {
        return therapeuticId;
    }

    public void setTherapeuticId(int therapeuticId) {
        this.therapeuticId = therapeuticId;
    }

    public String getTherapeuticName() {
        return therapeuticName;
    }

    public void setTherapeuticName(String therapeuticName) {
        this.therapeuticName = therapeuticName;
    }

    public String getTherapeuticDesc() {
        return therapeuticDesc;
    }

    public void setTherapeuticDesc(String therapeuticDesc) {
        this.therapeuticDesc = therapeuticDesc;
    }

    public String getCompanies() {
        return companies;
    }

    public void setCompanies(String companies) {
        this.companies = companies;
    }

    private String therapeuticName;
    private String therapeuticDesc;
    private String companies;

    public Therapeutic(){

    }

    public Therapeutic(Parcel in){
        therapeuticId = in.readInt();
        therapeuticName = in.readString();
        therapeuticDesc = in.readString();
        companies = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(therapeuticId);
        dest.writeString(therapeuticName);
        dest.writeString(therapeuticDesc);
        dest.writeString(companies);
    }

    public static final Parcelable.Creator<Therapeutic> CREATOR = new Parcelable.Creator<Therapeutic>()
    {
        @Override
        public Therapeutic createFromParcel(Parcel in)
        {
            return new Therapeutic(in);
        }

        @Override
        public Therapeutic[] newArray(int size)
        {
            return new Therapeutic[size];
        }
    };
}
