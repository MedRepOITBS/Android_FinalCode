package com.app.util;

/**
 * Created by Kishore on 9/26/2015.
 */
public class RefreshToken {

    private String value;
    private long expiration;

    public RefreshToken(String value, long expiration) {
        this.value = value;
        this.expiration = expiration;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }
}
