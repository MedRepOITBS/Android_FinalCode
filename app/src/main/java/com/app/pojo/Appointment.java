package com.app.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by masood on 9/6/15.
 */
public class Appointment implements Parcelable {
    private String createdOn;
    private String feedback;
    private String startDate;
    private int notificationId;
    private int duration;
    private String title;
    private String location;
    private String status;
    private int doctorId;
    private String appointmentId;
    private String appointmentDesc;
    private String companyName;

    public String getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    private String companyId;

    public Appointment(){

    }

    public Appointment(Parcel in){
        createdOn = in.readString();
        feedback = in.readString();
        startDate = in.readString();
        notificationId = in.readInt();
        duration = in.readInt();
        title = in.readString();
        location = in.readString();
        status = in.readString();
        doctorId = in.readInt();
        appointmentId = in.readString();
        appointmentDesc = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(createdOn);
        dest.writeString(feedback);
        dest.writeString(startDate);
        dest.writeInt(notificationId);
        dest.writeInt(duration);
        dest.writeString(title);
        dest.writeString(location);
        dest.writeString(status);
        dest.writeInt(doctorId);
        dest.writeString(appointmentId);
        dest.writeString(appointmentDesc);
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getAppointmentDesc() {
        return appointmentDesc;
    }

    public void setAppointmentDesc(String appointmentDesc) {
        this.appointmentDesc = appointmentDesc;
    }

    @Override
    public int describeContents() {
        return 0;
    }



    public static final Parcelable.Creator<Appointment> CREATOR = new Parcelable.Creator<Appointment>()
    {
        @Override
        public Appointment createFromParcel(Parcel in)
        {
            return new Appointment(in);
        }

        @Override
        public Appointment[] newArray(int size)
        {
            return new Appointment[size];
        }
    };

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
