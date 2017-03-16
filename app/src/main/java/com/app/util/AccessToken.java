package com.app.util;

/**
 * Created by Kishore on 9/27/2015.
 */
public class AccessToken {

    private String value;
    private long expiration;
    private String tokenType;
    private RefreshToken refreshToken;
    private boolean expired;
    private long expiresIn;

    public AccessToken(String value, long expiration, String tokenType, RefreshToken refreshToken, boolean expired, long expiresIn) {
        this.value = value;
        this.expiration = expiration;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.expired = expired;
        this.expiresIn = expiresIn;
    }

    public String getValue() {
        return value;
    }

    public void setValues(String value) {
        this.value = value;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
