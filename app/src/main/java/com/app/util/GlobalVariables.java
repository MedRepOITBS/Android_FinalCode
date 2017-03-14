package com.app.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by masood on 8/11/15.
 */
public class GlobalVariables {

    public static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";
    public static String ZIPCODE_PATTERN = "^[0-9]{6}(?:-[0-9]{4})?$";

    //matches 10-digit numbers only
    public static String MOBILE_NUMBER_PATTERN = "^[0-9]{10}$";

    public static Typeface getTypeface(Context context){
        return  Typeface.createFromAsset(context.getAssets(),"Roboto-Regular.ttf");
    }

    public static boolean isConnected(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    /**
     * Validate hex with regular expression
     *
     * @param hex
     *            hex for validation
     * @return true valid hex, false invalid hex
     */
    public static boolean validate(final String hex, String PATTERN) {
        Pattern  pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();

    }
}
