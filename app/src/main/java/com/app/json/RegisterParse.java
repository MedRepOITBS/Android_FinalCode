package com.app.json;

import android.util.Log;

import com.app.db.DoctorProfile;
import com.app.pojo.Address;
import com.app.pojo.AddressProfile;
import com.app.pojo.MyProfile;
import com.app.pojo.Register;
import com.app.util.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 * GET my profle
 * Created by masood on 9/12/15.
 */
public class RegisterParse {

    public static Register parseJsonData(String response){

        JSONObject jsonObject;
        Register register = new Register();
        try{
            jsonObject = new JSONObject(response);
            if(jsonObject.has(JSONTag.FIRSTNAME)) {
                Log.d("TAG","My firstname is "+jsonObject.getString(JSONTag.FIRSTNAME));
                register.setFirstname(jsonObject.getString(JSONTag.FIRSTNAME));
            }
            if(jsonObject.has(JSONTag.LASTNAME))
                register.setLastname(jsonObject.getString(JSONTag.LASTNAME));
            if(jsonObject.has(JSONTag.EMAILID))
                register.setEmail(jsonObject.getString(JSONTag.EMAILID));
            if(jsonObject.has(JSONTag.MOBILENO))
                register.setMobileNumber(jsonObject.getString(JSONTag.MOBILENO));
            if(jsonObject.has(JSONTag.QUALIFICATION))
                register.setSelectedCat(jsonObject.getString(JSONTag.QUALIFICATION));
            if(jsonObject.has(JSONTag.LOCATIONS)){
                JSONArray jsonArray = jsonObject.getJSONArray(JSONTag.LOCATIONS);
                ArrayList<Address> addressArrayList = new ArrayList<Address>();
                for (int count = 0; count< jsonArray.length(); count++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(count);
                    Address address = new Address();
                    if(jsonObject1.has(JSONTag.ADDRESS1)) {
                        Log.d("TAG","My Address is "+jsonObject1.getString(JSONTag.ADDRESS1));
                        address.setAddressLine(jsonObject1.getString(JSONTag.ADDRESS1));
                    }
                    if(jsonObject1.has(JSONTag.ADDRESS2))
                        address.setDoornumber(jsonObject1.getString(JSONTag.ADDRESS2));
                    if(jsonObject1.has(JSONTag.CITY))
                        address.setCity(jsonObject1.getString(JSONTag.CITY));
                    if(jsonObject1.has(JSONTag.ZIPCODE))
                        address.setZipcode(jsonObject1.getString(JSONTag.ZIPCODE));
                    addressArrayList.add(address);
                }
                register.setAddressArrayList(addressArrayList);
            }
        }catch (JSONException ex){
            ex.printStackTrace();
        }



        return  register;
    }

    public MyProfile getMyProfile(String response){
        JSONObject jsonObject;

        MyProfile myProfile = MyProfile.getInstance();
        try{
            jsonObject = new JSONObject(response);

            if(jsonObject.has(JSONTag.FIRSTNAME)) {
                Log.d("TAG","My firstname is "+jsonObject.getString(JSONTag.FIRSTNAME));
                myProfile.setFirstname(jsonObject.getString(JSONTag.FIRSTNAME));
            }
            if(jsonObject.has(JSONTag.LASTNAME))
                myProfile.setLastname(jsonObject.getString(JSONTag.LASTNAME));
            if(jsonObject.has(JSONTag.REGISTRATIONNUMBER))
                myProfile.setRegistrationNumber(jsonObject.getString(JSONTag.REGISTRATIONNUMBER));
            if(jsonObject.has(JSONTag.EMAILID))
                myProfile.setEmail(jsonObject.getString(JSONTag.EMAILID));
            if(jsonObject.has(JSONTag.MOBILENO))
                myProfile.setMobileNumber(jsonObject.getString(JSONTag.MOBILENO));
            if(jsonObject.has(JSONTag.QUALIFICATION))
                myProfile.setSelectedCat(jsonObject.getString(JSONTag.QUALIFICATION));
            if(jsonObject.has(JSONTag.THERAPEUTICID))
                myProfile.setTherapeuticID(jsonObject.getInt(JSONTag.THERAPEUTICID));
            if(jsonObject.has(JSONTag.PROFILE_PICTURE) && !jsonObject.isNull(JSONTag.PROFILE_PICTURE)){
                try{
                    JSONObject jsonObject1 = jsonObject.getJSONObject(JSONTag.PROFILE_PICTURE);

                    JSONParser jsonParser = new JSONParser();
                    DoctorProfile.ProfilePicture profilePicture =
                            (DoctorProfile.ProfilePicture) jsonParser.jsonParser(
                                    jsonObject1,
                                    DoctorProfile.ProfilePicture.class);


                    myProfile.setProfilePicture(profilePicture);
                }catch(JSONException e){
                    e.printStackTrace();
                }

            }
            if(jsonObject.has(JSONTag.LOCATIONS)){
                JSONArray jsonArray = jsonObject.getJSONArray(JSONTag.LOCATIONS);
                ArrayList<AddressProfile> addressArrayList = new ArrayList<AddressProfile>();
                for (int count = 0; count< jsonArray.length(); count++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(count);
                    AddressProfile address = new AddressProfile();
                    if(jsonObject1.has(JSONTag.ADDRESS1)) {
                        Log.d("TAG","My Address is "+jsonObject1.getString(JSONTag.ADDRESS1));
                        address.setAddress1(jsonObject1.getString(JSONTag.ADDRESS1));
                    }
                    if(jsonObject1.has(JSONTag.ADDRESS2))
                        address.setAddress2(jsonObject1.getString(JSONTag.ADDRESS2));
                    if(jsonObject1.has(JSONTag.CITY))
                        address.setCity(jsonObject1.getString(JSONTag.CITY));
                    if(jsonObject1.has(JSONTag.STATE))
                        address.setState(jsonObject1.getString(JSONTag.STATE));
                    if(jsonObject1.has(JSONTag.COUNTRY))
                        address.setCountry(jsonObject1.getString(JSONTag.COUNTRY));
                    if(jsonObject1.has(JSONTag.ZIPCODE))
                        address.setZipCode(jsonObject1.getString(JSONTag.ZIPCODE));

                    if(jsonObject1.has(JSONTag.TYPE) && !jsonObject1.isNull(JSONTag.TYPE)) {
                        address.setType(jsonObject1.getInt(JSONTag.TYPE));
                    }
                    else{
                        address.setType(AddressProfile.ADDRESS_TYPE.HOSPITAL);
                    }




                            addressArrayList.add(address);
                }
                myProfile.setAddressArrayList(addressArrayList);
            }
    }catch (JSONException ex){
        ex.printStackTrace();
    }
        return  myProfile;
    }

}
