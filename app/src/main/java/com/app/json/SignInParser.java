package com.app.json;

import com.app.pojo.RefreshToken;
import com.app.pojo.SignIn;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by masood on 8/12/15.
 */
public class SignInParser {

    public static Object getSignInParser(String response){
        JSONObject jObject= null;
        JSONObject jrefreshObject = null;
        try{
            jObject= new JSONObject(response);

            SignIn signIn = new SignIn();
            if(jObject.has(JSONTag.VALUE)){
                signIn.SET_ACCESS_TOKEN(jObject.getString(JSONTag.VALUE));
                System.out.println("JSONTag.VALUE: " + jObject.getString(JSONTag.VALUE));
            }

            if(jObject.has(JSONTag.EXPIRATION)){
                signIn.SET_TOKEN_EXPIRY_IN_MILLIS(jObject.getLong(JSONTag.EXPIRATION));
                System.out.println("JSONTag.EXPIRATION: " + jObject.getLong(JSONTag.EXPIRATION));
            }

            if(jObject.has(JSONTag.REFRESHTOKEN)){
                System.out.println("about to get " + JSONTag.REFRESHTOKEN);
                jrefreshObject = jObject.getJSONObject(JSONTag.REFRESHTOKEN);

//                ArrayList<RefreshToken> refreshTokenList = new ArrayList<>();
				/*for (int i = 0; i < jArray.length(); i++) {
					jObject = jArray.getJSONObject(i);*/
                RefreshToken refreshToken = new RefreshToken();

                if(jObject.has(JSONTag.VALUE)){
                    refreshToken.setRefreshToken(jrefreshObject.getString(JSONTag.VALUE));
                    System.out.println("jrefreshObject.getString(JSONTag.VALUE): " + jrefreshObject.getString(JSONTag.VALUE));
                }

                if(jObject.has(JSONTag.EXPIRATION)){
                    refreshToken.setExpireToken(jrefreshObject.getLong(JSONTag.EXPIRATION));
                    System.out.println(jrefreshObject.getLong(JSONTag.EXPIRATION));
                }

                SignIn.setRefreshToken(refreshToken);

//                refreshTokenList.add(refreshToken);

//				}
            }


            if(jObject.has(JSONTag.EXPIRESIN)){
                SignIn.SET_EXPIRES_IN(jObject.getLong(JSONTag.EXPIRESIN));
                System.out.println("JSONTag.EXPIRESIN: " + jObject.getLong(JSONTag.EXPIRESIN));
            }

            if(jObject.has(JSONTag.EXPIRED)){
                signIn.setExpries(jObject.getBoolean(JSONTag.EXPIRED));
                System.out.println("JSONTag.EXPIRED: " + jObject.getBoolean(JSONTag.EXPIRED));
            }


            if(jObject.has(JSONTag.TOKENTYPE)){
                signIn.setTokenType(jObject.getString(JSONTag.TOKENTYPE));
                System.out.println(jObject.getString(JSONTag.TOKENTYPE));
            }
            return signIn;
        }catch(JSONException ex){
            ex.printStackTrace();
        }catch(Exception ex){

        }
        return null;
    }
}
