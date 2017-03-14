package com.app.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.app.json.SignInParser;
import com.app.util.GlobalVariables;
import com.app.util.HttpUrl;

import medrep.medrep.LoginActivity;

/**
 * Created by masood on 8/12/15.
 */
public class SignInTask extends AsyncTask<String,Void, Object> {

    private Context context;
    private ProgressDialog dialog;

    public SignInTask(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(context, "please wait","Loading");
        dialog.show();
    }

    @Override
    protected Object doInBackground(String... params) {
//        Log.d("TAG","my response------------------------------------ ");
        try {
//            String response = DoctorPostMethods.sendGet(HttpUrl.BASEURL+HttpUrl.SIGNIN+"&username=umar.ashraf@gmail.com&password=Test123");
            String response = DoctorPostMethods.sendGet(HttpUrl.BASEURL + HttpUrl.SIGNIN + "&username=" + params[0] + "&password=" + params[1]);
            Log.d("TAG","my response------------------------------------ "+response);
            return SignInParser.getSignInParser(response);
        }catch(Exception ex){
            ex.printStackTrace();
            if(!GlobalVariables.isConnected(context)){
                return "Check internet connection";
            }else{
                Log.d("TAG","authentication challenge " +ex.getMessage().contains("authentication challenge"));
                return null;
            }

        }

    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        dialog.dismiss();
        if(o!=null){
            if(o instanceof String){
                Toast.makeText(context, (String)o, Toast.LENGTH_SHORT).show();
            }else {
            //    Toast.makeText(context, "signIn sucessfull", Toast.LENGTH_SHORT).show();
                if(context instanceof LoginActivity){
                    ((LoginActivity)context).startSign(o);
                }
             /*   Toast.makeText(context, "signIn sucessfull", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, SignInDrawerActivity.class);
                intent.putExtra("signin",(SignIn)o);
                context.startActivity(intent);*/
            }
           /* Intent intent = new Intent(context, RegisterCategoryActivity.class);
            context.startActivity(intent);*/
        }else{
            Toast.makeText(context, "SignIn unsuccessful. PLease try again", Toast.LENGTH_SHORT).show();

        }
    }
}
