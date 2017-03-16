package com.app.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import medrep.medrep.R;

/**
 * Created by masood on 9/2/15.
 */
public class OtpFragment extends Fragment implements View.OnClickListener{

    private Button submitButton;
    public SubmitListener listener;
    private EditText desiredPwd;
    private EditText cnfirmPwd;

    public interface SubmitListener{
        void submit(String desiredPwd, String cnfirmPwd);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (SubmitListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_otp, container, false);
        desiredPwd = (EditText) v.findViewById(R.id.email_edittext);
        cnfirmPwd = (EditText) v.findViewById(R.id.pwd_edittext);
        submitButton = (Button) v.findViewById(R.id.saveButton);
        submitButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        if( v == submitButton){
//            listener.submit();
            if(desiredPwd.getText().toString().isEmpty() || desiredPwd.getText().toString().trim().length() ==0){
                Toast.makeText(getActivity(), "Please enter the valid text in the text boxes", Toast.LENGTH_SHORT).show();

            }else if(cnfirmPwd.getText().toString().isEmpty() || cnfirmPwd.getText().toString().trim().length() ==0){
                Toast.makeText(getActivity(), "Please enter the valid confirm text in the text boxes", Toast.LENGTH_SHORT).show();
            }else if(desiredPwd.getText().toString().length() > 0 && cnfirmPwd.getText().toString().length() > 0){
                if(!desiredPwd.getText().toString().equals(cnfirmPwd.getText().toString())){
                    Toast.makeText(getActivity(), "desired password is not equal to confirm password", Toast.LENGTH_SHORT).show();
                }else{
                    listener.submit(desiredPwd.getText().toString(), cnfirmPwd.getText().toString());
                }
            }
        }
    }
}
