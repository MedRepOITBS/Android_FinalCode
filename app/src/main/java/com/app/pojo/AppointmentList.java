package com.app.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by masood on 9/6/15.
 */
public class AppointmentList implements Parcelable {

    private ArrayList<Appointment> appointmentArrayList;

    public AppointmentList(){

    }



    public void setAppointmentArrayList(ArrayList<Appointment> appointmentArrayList) {
        this.appointmentArrayList = appointmentArrayList;
    }

    public ArrayList<Appointment> getAppointmentArrayList() {
        return appointmentArrayList;
    }

    public AppointmentList(Parcel in){
        appointmentArrayList = new ArrayList<>();
        in.readList(appointmentArrayList, Appointment.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(appointmentArrayList);
    }

    public static final Parcelable.Creator<AppointmentList> CREATOR = new Parcelable.Creator<AppointmentList>()
    {
        @Override
        public AppointmentList createFromParcel(Parcel in)
        {
            return new AppointmentList(in);
        }

        @Override
        public AppointmentList[] newArray(int size)
        {
            return new AppointmentList[size];
        }
    };
}
