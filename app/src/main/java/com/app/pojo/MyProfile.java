package com.app.pojo;

import com.app.db.*;
import com.app.db.DoctorProfile;

import java.util.ArrayList;

/**
 * Created by masood on 9/16/15.
 */
public class MyProfile {

    private String doctorId;
    private String firstname;
    private String lastname;
    private String mobileNumber;
    private String altMobileNumber;
    private String email;
    private String altEmail;
    private String registrationNumber;
    private int therapeuticID;

    public int getTherapeuticID() {
        return therapeuticID;
    }

    public void setTherapeuticID(int therapeuticID) {
        this.therapeuticID = therapeuticID;
    }

    private DoctorProfile.ProfilePicture profilePicture;

    private MyProfile(){}

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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

    public String getSelectedCat() {
        return selectedCat;
    }

    public void setSelectedCat(String selectedCat) {
        this.selectedCat = selectedCat;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<AddressProfile> getAddressArrayList() {
        return addressArrayList;
    }

    public void setAddressArrayList(ArrayList<AddressProfile> addressArrayList) {
        this.addressArrayList = addressArrayList;
    }

    private String selectedCat;
    private String password;
    private ArrayList<AddressProfile> addressArrayList;
    private static MyProfile myProfile;

    public static MyProfile getInstance(){
        if(myProfile == null)
            myProfile = new MyProfile();
        return myProfile;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }


    public void setProfilePicture(DoctorProfile.ProfilePicture profilePicture) {
        this.profilePicture = profilePicture;
    }

    public DoctorProfile.ProfilePicture getProfilePicture() {
        return profilePicture;
    }
}
