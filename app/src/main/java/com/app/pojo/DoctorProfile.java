package com.app.pojo;

import java.util.ArrayList;

/**
 * Created by masood on 9/17/15.
 */
public class DoctorProfile {

    private static DoctorProfile INSTANCE;

    private DoctorProfile(){}

    public static DoctorProfile getInstance(){
        if(INSTANCE == null)
            INSTANCE = new DoctorProfile();
        return INSTANCE;
    }

    private String doctorId = "";
    private String firstName = "";
    private String lastName = "";
    private String mobileNumber = "";
    private String altMobileNumber = "";
    private String email = "";
    private String altEmail = "";
//    private String registrationNumber = "";
    private int therapeuticID;

    /*public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }*/

    public int getTherapeuticID() {
        return therapeuticID;
    }

    public void setTherapeuticID(int therapeuticID) {
        this.therapeuticID = therapeuticID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public static void clearProfile() {
        INSTANCE = null;
    }
}
