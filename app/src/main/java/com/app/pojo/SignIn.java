package com.app.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;

public class SignIn implements Parcelable{

	private static String ACCESS_TOKEN;
	private String tokenType;
	//Values will be stored in seconds
	private static long EXPIRES_IN;
	private static long TOKEN_EXPIRY_IN_MILLIS;
	private static boolean expries = false;
	
	private static ArrayList<RefreshToken> REFRESH_TOKEN_LIST;

	private static RefreshToken REFRESH_TOKEN;

	public SignIn(){}
	public SignIn(Parcel in){
		ACCESS_TOKEN = in.readString();
		tokenType = in.readString();
		EXPIRES_IN = in.readLong();
		TOKEN_EXPIRY_IN_MILLIS = in.readLong();
		expries = in.readByte() == 1;
		REFRESH_TOKEN_LIST = new ArrayList<>();
		in.readList(REFRESH_TOKEN_LIST, RefreshToken.class.getClassLoader());
	}
	public static void SET_ACCESS_TOKEN(String accessToken){
		ACCESS_TOKEN = accessToken;
	}
	public static String GET_ACCESS_TOKEN(){
		return ACCESS_TOKEN;
	}
	
	public static void SET_TOKEN_EXPIRY_IN_MILLIS(long tokenExpiryInMillis){
		TOKEN_EXPIRY_IN_MILLIS = tokenExpiryInMillis;
	}

	public static long GET_TOKEN_EXPIRY_IN_MILLIS(){
		return TOKEN_EXPIRY_IN_MILLIS;
	}
	
	public void setTokenType(String tokenType){
		this.tokenType = tokenType;
	}
	public String getTokenType(){
		return tokenType;
	}
	
	public void setExpries(boolean expries){
		this.expries = expries;
	}
	public boolean getExpries(){
		return expries;
	}
	
	public static void SET_EXPIRES_IN(long expiresIn){
		EXPIRES_IN = Calendar.getInstance().getTimeInMillis() + (expiresIn * 1000);
	}
	public static long GET_EXPIRES_IN(){
		System.out.println("EXPIRES_IN: " + EXPIRES_IN);
		return EXPIRES_IN;
	}
	
	/*public static void setRefreshTokenList(*//*ArrayList<RefreshToken> refreshTokenList*//*RefreshToken refreshToken){
//		this.REFRESH_TOKEN_LIST = refreshTokenList;
		REFRESH_TOKEN = refreshToken;
	}
	public static RefreshToken getRefreshTokenList(){
//		return REFRESH_TOKEN_LIST;
		return REFRESH_TOKEN;
	}*/

	public static void setRefreshToken(RefreshToken refreshToken){
//		System.out.println("Refresh token has been set to " + refreshToken.getRefreshToken());
		REFRESH_TOKEN = refreshToken;
	}

	public static RefreshToken getRefreshToken(){
		return REFRESH_TOKEN;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(ACCESS_TOKEN);
		dest.writeString(tokenType);
		dest.writeLong(EXPIRES_IN);
		dest.writeLong(TOKEN_EXPIRY_IN_MILLIS);
		dest.writeByte((byte) (expries ? 1 : 0));
		dest.writeList(REFRESH_TOKEN_LIST);
	}


	public static final Parcelable.Creator<SignIn> CREATOR = new Parcelable.Creator<SignIn>()
	{
		@Override
		public SignIn createFromParcel(Parcel in)
		{
			return new SignIn(in);
		}

		@Override
		public SignIn[] newArray(int size)
		{
			return new SignIn[size];
		}
	};
}
