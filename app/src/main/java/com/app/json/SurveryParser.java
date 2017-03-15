package com.app.json;

import android.util.Log;

import com.app.pojo.Survery;
import com.app.pojo.SurveryList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by masood on 9/6/15.
 */
public class SurveryParser {

    public static Object surveryParser(String response){
        JSONArray jsonArray = null;
        SurveryList surveryList = null;
        try{
            surveryList = new SurveryList();
            jsonArray = new JSONArray(response);
            ArrayList<Survery> surveryArrayList = new ArrayList<>();

            for (int count = 0; count < jsonArray.length(); count++){
                JSONObject jsonObject = jsonArray.getJSONObject(count);
                Survery survery = new Survery();
                if(jsonObject.has(JSONTag.SURVERYID)){
                    survery.setSurveyId(jsonObject.getInt(JSONTag.SURVERYID));
                }
                if(jsonObject.has(JSONTag.DOCTOR_ID)){
                    survery.setDoctorId(jsonObject.getInt(JSONTag.DOCTOR_ID));
                }

                if(jsonObject.has(JSONTag.DOC_SURVERY_ID)){
                    survery.setDoctorSurveryId(jsonObject.getInt(JSONTag.DOC_SURVERY_ID));
                }
                if(jsonObject.has(JSONTag.SURVERY_TITLE)){
                    survery.setSurveyTitle(jsonObject.getString(JSONTag.SURVERY_TITLE));
                }
                if(jsonObject.has(JSONTag.SURVERY_URL)){
                    survery.setSurveyUrl(jsonObject.getString(JSONTag.SURVERY_URL));
                }
                if(jsonObject.has(JSONTag.CREATED_ON)){
                    survery.setCreatedOn(jsonObject.getString(JSONTag.CREATED_ON));
                }
                if (jsonObject.has(JSONTag.STATUS)){
                    survery.setStatus(jsonObject.getString(JSONTag.STATUS));
                }
                if(jsonObject.has(JSONTag.SCH_START)){
                    survery.setScheduledStart(jsonObject.getString(JSONTag.SCH_START));
                }
                if(jsonObject.has(JSONTag.SCH_FINISH)){
                    survery.setScheduledFinish(jsonObject.getString(JSONTag.SCH_FINISH));
                }
                if (jsonObject.has(JSONTag.COMPANY_ID)){
                    survery.setCompanyId(jsonObject.getInt(JSONTag.COMPANY_ID));
                }
                if(jsonObject.has(JSONTag.THERAPEUTIC_ID)){
                    //if(jsonObject.getInt(JSONTag.THERAPEUTIC_ID) == ) {
                        //survery.setTherapeuticId(jsonObject.getInt(JSONTag.THERAPEUTIC_ID));
                    //}
                }
                if(jsonObject.has(JSONTag.COMP_NAME)){
                    survery.setCompanyName(jsonObject.getString(JSONTag.COMP_NAME));
                }
                if(jsonObject.has(JSONTag.THEPTIC_NAME)){
                    survery.setTherapeuticName(jsonObject.getString(JSONTag.THEPTIC_NAME));
                }
                if(jsonObject.has(JSONTag.SURVERY_DESC)){
                    survery.setSurveyDescription(jsonObject.getString(JSONTag.SURVERY_DESC));
                }
                    surveryArrayList.add(survery);

            }
            surveryList.setSurveryArrayList(surveryArrayList);
            Log.d("TAG", "MY SURVERY LIST >>>>>>>>>>>>>>>>>>>>> ");
            return surveryList;
        }catch (JSONException ex){
            ex.printStackTrace();;
        }
        return surveryList;
    }
}
