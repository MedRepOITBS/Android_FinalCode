package com.app.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class RefreshToken implements Parcelable{

	private static String refreshToken;
	//private String refreshToken;
	private long expireToken;

	public RefreshToken(){}

	public RefreshToken(Parcel in){
		refreshToken = in.readString();
		expireToken = in.readLong();
	}

	public RefreshToken(String refreshToken, long expireToken) {
		this.refreshToken = refreshToken;
		this.expireToken = expireToken;
	}


	public static String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	public long getExpireToken() {
		return expireToken;
	}
	public void setExpireToken(long expireToken) {
		this.expireToken = expireToken;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(refreshToken);
		dest.writeLong(expireToken);
	}

	public static final Parcelable.Creator<RefreshToken> CREATOR = new Parcelable.Creator<RefreshToken>()
	{
		@Override
		public RefreshToken createFromParcel(Parcel in)
		{
			return new RefreshToken(in);
		}

		@Override
		public RefreshToken[] newArray(int size)
		{
			return new RefreshToken[size];
		}
	};
}
