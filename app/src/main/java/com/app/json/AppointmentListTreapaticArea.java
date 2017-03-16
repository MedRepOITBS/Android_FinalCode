package com.app.json;

import com.app.pojo.AppointmentList;
import com.app.pojo.Therapeutic;
import com.app.pojo.TherapeuticList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by masood on 9/6/15.
 */
public class AppointmentListTreapaticArea {

    public static Object parseAppointment(String response){
        JSONArray jsonArray = null;
        try{
            jsonArray = new JSONArray(response);
            AppointmentList appointmentList = new AppointmentList();
            ArrayList<com.app.pojo.Appointment> appointmentArrayList = new ArrayList<>();
            for (int count = 0; count < jsonArray.length() ; count++){
                JSONObject jsonObject = jsonArray.getJSONObject(count);
                com.app.pojo.Appointment appointment = new com.app.pojo.Appointment();
                if(jsonObject.has(JSONTag.CREATED_ON)){
                    appointment.setCreatedOn(jsonObject.getString(JSONTag.CREATED_ON));
                }
                if(jsonObject.has(JSONTag.FEEDBACK)){
                    appointment.setFeedback(jsonObject.getString(JSONTag.FEEDBACK));
                }
                if(jsonObject.has(JSONTag.STARTDATE)){
                    appointment.setStartDate(jsonObject.getString(JSONTag.STARTDATE));
                }
                if(jsonObject.has(JSONTag.NOT_ID)){
                    appointment.setNotificationId(jsonObject.getInt(JSONTag.NOT_ID));

                    System.out.println("appointment.getNotificationId(): " + appointment.getNotificationId());
                }
                if(jsonObject.has(JSONTag.DURATION)){

                    String temp = jsonObject.getString(JSONTag.DURATION);

                    if(temp != null && !temp.trim().equals("null") && !temp.trim().equals("")){
                        appointment.setDuration(jsonObject.getInt(JSONTag.DURATION));
                    }else{
                        appointment.setDuration(0);
                    }


                }
                if(jsonObject.has(JSONTag.TITLE)){
                    appointment.setTitle(jsonObject.getString(JSONTag.TITLE));
                }
                if(jsonObject.has(JSONTag.LOCATION)){
                    appointment.setLocation(jsonObject.getString(JSONTag.LOCATION));
                }
                if(jsonObject.has(JSONTag.STATUS)){
                    appointment.setStatus(jsonObject.getString(JSONTag.STATUS));
                }
                if(jsonObject.has(JSONTag.DOCTOR_ID)){
                    appointment.setDoctorId(jsonObject.getInt(JSONTag.DOCTOR_ID));
                }
                if(jsonObject.has(JSONTag.APP_ID)){
                    appointment.setAppointmentId(jsonObject.getString(JSONTag.APP_ID));
                }
                if(jsonObject.has(JSONTag.APP_DESC)){
                    appointment.setAppointmentDesc(jsonObject.getString(JSONTag.APP_DESC));
                }
                if(jsonObject.has(JSONTag.COMPANY_ID)){
                    appointment.setCompanyId(jsonObject.getString(JSONTag.COMPANY_ID));
                }
                if(jsonObject.has(JSONTag.COMPANY_NAME)){
                    appointment.setCompanyName(jsonObject.getString(JSONTag.COMPANY_NAME));
                }
                appointmentArrayList.add(appointment);
            }
            appointmentList.setAppointmentArrayList(appointmentArrayList);
            return appointmentList;
        }catch (JSONException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static Object getTherapeuticArea(String response){
        try {
            JSONArray jsonArray = new JSONArray(response);
            TherapeuticList therapeuticList = new TherapeuticList();
            ArrayList<Therapeutic> therapeuticArrayList = new ArrayList<>();
            for (int i=0 ; i<jsonArray.length(); i++){
                Therapeutic therapeutic = new Therapeutic();
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if(jsonObject.has("companies")){
                    therapeutic.setCompanies(jsonObject.getString("companies"));
                }

                if(jsonObject.has("therapeuticDesc")){
                    therapeutic.setTherapeuticDesc(jsonObject.getString("therapeuticDesc"));
                }

                if(jsonObject.has("therapeuticName")){
                    therapeutic.setTherapeuticName(jsonObject.getString("therapeuticName"));
                }

                if(jsonObject.has("therapeuticId")){
                    therapeutic.setTherapeuticId(jsonObject.getInt("therapeuticId"));
                }
                therapeuticArrayList.add(therapeutic);
            }
            therapeuticList.setAppointmentArrayList(therapeuticArrayList);
            return therapeuticList;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
