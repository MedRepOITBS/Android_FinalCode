package com.app.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by masood on 8/13/15.
 */
public class Register implements Parcelable{

    private String doctorId;
    private String firstname;
    private String lastname;
    private String mobileNumber;
    private String altMobileNumber;
    private String email;
    private String altEmail;
    private String selectedCat;
    private String password;
    private ArrayList<Address> addressArrayList;

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getSelectedCat(){
        return selectedCat;
    }

    public void setSelectedCat(String selectedCat){
        this.selectedCat = selectedCat;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAltMobileNumber() {
        return altMobileNumber;
    }

    public void setAltMobileNumber(String altMobileNumber) {
        this.altMobileNumber = altMobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAltEmail() {
        return altEmail;
    }

    public void setAltEmail(String altEmail) {
        this.altEmail = altEmail;
    }

    public ArrayList<Address> getAddressArrayList() {
        return addressArrayList;
    }

    public void setAddressArrayList(ArrayList<Address> addressArrayList) {
        this.addressArrayList = addressArrayList;
    }


    public Register(){

    }
    public Register(Parcel in){
        doctorId = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        mobileNumber = in.readString();
        altMobileNumber = in.readString();
        email = in.readString();
        altEmail = in.readString();
        selectedCat = in.readString();
        password = in.readString();
        addressArrayList = new ArrayList<Address>();
        in.readList(addressArrayList, ArrayList.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(doctorId);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(mobileNumber);
        dest.writeString(altMobileNumber);
        dest.writeString(email);
        dest.writeString(altEmail);
        dest.writeString(selectedCat);
        dest.writeString(password);
        dest.writeList(addressArrayList);
    }

    public static final Parcelable.Creator<Register> CREATOR = new Parcelable.Creator<Register>()
    {
        @Override
        public Register createFromParcel(Parcel in)
        {
            return new Register(in);
        }

        @Override
        public Register[] newArray(int size)
        {
            return new Register[size];
        }
    };
}
