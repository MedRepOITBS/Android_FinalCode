package com.app.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.app.fragments.NotificationInsideBrand;
import com.app.json.AppointmentListTreapaticArea;
import com.app.pojo.TherapeuticList;
import com.app.util.HttpUrl;

/**
 * Created by masood on 9/19/15.
 */
public class TherapeuticTask extends AsyncTask<String,Void, Object>{

    private Context context;
    private ProgressDialog dialog;
    private Fragment fragment;

    public TherapeuticTask(Context context, Fragment fragment){
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog = ProgressDialog.show(context, "please Wait", "Loading");
    }

    @Override
    protected Object doInBackground(String... params) {
        try {
            String response = DoctorPostMethods.sendGet(HttpUrl.BASEURL + "/preapi/masterdata/getTherapeuticAreaDetails");
            return AppointmentListTreapaticArea.getTherapeuticArea(response);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        dialog.dismiss();
        if(o instanceof TherapeuticList){
            TherapeuticList therapeuticList= (TherapeuticList)o;
//            ((NotificationInsideBrand) fragment).showList(therapeuticList);
        }
    }
}
