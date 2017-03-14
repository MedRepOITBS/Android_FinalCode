package com.app.adapter;

/**
 * Created by amit kumar on 2/10/2016.
 */
public class AppointmentModel {


    String appointmentName;
    String appointmentDate;
    String appoointmentDesc;
    String appointmentLocation;
    String drugName;

    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    public void setAppointmentLocation(String appointmentLocation) {
        this.appointmentLocation = appointmentLocation;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentName() {
        return appointmentName;
    }

    public void setAppointmentName(String appointmentName) {
        this.appointmentName = appointmentName;
    }

    public String getAppoointmentDesc() {
        return appoointmentDesc;
    }

    public void setAppoointmentDesc(String appoointmentDesc) {
        this.appoointmentDesc = appoointmentDesc;
    }
}
