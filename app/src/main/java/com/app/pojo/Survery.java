package com.app.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by masood on 9/6/15.
 */
public class Survery implements Parcelable {

    private int surveyId;
    private int doctorId;
    private int doctorSurveryId;
    private String surveyTitle;
    private String surveyUrl;
    private String createdOn;
    private String status;
    private String scheduledStart;
    private String scheduledFinish;
    private int  companyId;
    private int therapeuticId;
    private String companyName;
    private String therapeuticName;
    private String surveyDescription;

    public Survery() {
    }

    public Survery(Parcel in) {
        surveyId = in.readInt();
        doctorId = in.readInt();
        doctorSurveryId = in.readInt();
        surveyTitle = in.readString();
        surveyUrl = in.readString();
        createdOn = in.readString();
        status = in.readString();
        scheduledStart = in.readString();
        scheduledFinish = in.readString();
        companyId = in.readInt();
        therapeuticId = in.readInt();
        companyName = in.readString();
        therapeuticName = in.readString();
        surveyDescription = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(surveyId);
        dest.writeInt(doctorId);
        dest.writeInt(doctorSurveryId);
        dest.writeString(surveyTitle);
        dest.writeString(surveyUrl);
        dest.writeString(createdOn);
        dest.writeString(status);
        dest.writeString(scheduledStart);
        dest.writeString(scheduledFinish);
        dest.writeInt(companyId);
        dest.writeInt(therapeuticId);
        dest.writeString(companyName);
        dest.writeString(therapeuticName);
        dest.writeString(surveyDescription);
    }

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getDoctorSurveryId() {
        return doctorSurveryId;
    }

    public void setDoctorSurveryId(int doctorSurveryId) {
        this.doctorSurveryId = doctorSurveryId;
    }

    public String getSurveyTitle() {
        return surveyTitle;
    }

    public void setSurveyTitle(String surveyTitle) {
        this.surveyTitle = surveyTitle;
    }

    public String getSurveyUrl() {
        return surveyUrl;
    }

    public void setSurveyUrl(String surveyUrl) {
        this.surveyUrl = surveyUrl;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getScheduledStart() {
        return scheduledStart;
    }

    public void setScheduledStart(String scheduledStart) {
        this.scheduledStart = scheduledStart;
    }

    public String getScheduledFinish() {
        return scheduledFinish;
    }

    public void setScheduledFinish(String scheduledFinish) {
        this.scheduledFinish = scheduledFinish;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getTherapeuticId() {
        return therapeuticId;
    }

    public void setTherapeuticId(int therapeuticId) {
        this.therapeuticId = therapeuticId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTherapeuticName() {
        return therapeuticName;
    }

    public void setTherapeuticName(String therapeuticName) {
        this.therapeuticName = therapeuticName;
    }

    public String getSurveyDescription() {
        return surveyDescription;
    }

    public void setSurveyDescription(String surveyDescription) {
        this.surveyDescription = surveyDescription;
    }



    public static final Parcelable.Creator<Survery> CREATOR = new Parcelable.Creator<Survery>()
    {
        @Override
        public Survery createFromParcel(Parcel in)
        {
            return new Survery(in);
        }

        @Override
        public Survery[] newArray(int size)
        {
            return new Survery[size];
        }
    };
}
