package com.app.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by masood on 8/26/15.
 */
public class ForgotPassword implements Parcelable {

    private String username;
    private String password;
    private String newPassword;
    private String otp;
    private String verificationType;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerificationType() {
        return verificationType;
    }

    public void setVerificationType(String verificationType) {
        this.verificationType = verificationType;
    }

    public ForgotPassword(){

    }
    public ForgotPassword(Parcel in){
        username = in.readString();
        password = in.readString();
        newPassword = in.readString();
        otp= in.readString();
        verificationType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(newPassword);
        dest.writeString(otp);
        dest.writeString(verificationType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ForgotPassword> CREATOR = new Parcelable.Creator<ForgotPassword>()
    {
        @Override
        public ForgotPassword createFromParcel(Parcel in)
        {
            return new ForgotPassword(in);
        }

        @Override
        public ForgotPassword[] newArray(int size)
        {
            return new ForgotPassword[size];
        }
    };
}
