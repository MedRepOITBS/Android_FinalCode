package com.app.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by masood on 9/19/15.
 */
public class TherapeuticList  implements Parcelable {
    private ArrayList<Therapeutic> appointmentArrayList;

    public TherapeuticList(){

    }

    public void setAppointmentArrayList(ArrayList<Therapeutic> appointmentArrayList) {
        this.appointmentArrayList = appointmentArrayList;
    }

    public ArrayList<Therapeutic> getAppointmentArrayList() {
        return appointmentArrayList;
    }

    public TherapeuticList(Parcel in){
        appointmentArrayList = new ArrayList<>();
        in.readList(appointmentArrayList, Therapeutic.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(appointmentArrayList);
    }

    public static final Parcelable.Creator<TherapeuticList> CREATOR = new Parcelable.Creator<TherapeuticList>()
    {
        @Override
        public TherapeuticList createFromParcel(Parcel in)
        {
            return new TherapeuticList(in);
        }

        @Override
        public TherapeuticList[] newArray(int size)
        {
            return new TherapeuticList[size];
        }
    };
}
