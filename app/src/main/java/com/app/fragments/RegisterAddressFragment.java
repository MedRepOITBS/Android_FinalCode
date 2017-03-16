package com.app.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adapter.ViewPagerAdapter;
import com.app.pojo.AddressProfile;
import com.app.pojo.DoctorProfile;
import com.app.util.GlobalVariables;
import com.app.util.Utils;

import java.util.ArrayList;

import medrep.medrep.DoctorOTP_Activity;
import medrep.medrep.DoctorSetPasswordActivity;
import medrep.medrep.R;
import medrep.medrep.RegisterActivity;

/**
 * Created by masood on 9/1/15.
 */
public class RegisterAddressFragment extends Fragment implements View.OnClickListener{

    private TextView privateClinlic;
    private TextView govClinic;
    private ClinicListener listener;
    private ViewPager _mViewPager;
    private ViewPagerAdapter _adapter;
    private LinearLayout firstLayout;
    private LinearLayout secondLayout;

    public interface ClinicListener{
        public void privateClinic(int layoutId);
        public void publicClinic(int layoutId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ClinicListener) activity;
    }

    private void setUpView(View v){
        _mViewPager = (ViewPager) v.findViewById(R.id.viewPager);
        _adapter = new ViewPagerAdapter(getActivity(),getFragmentManager());
        _mViewPager.setAdapter(_adapter);
        _mViewPager.setCurrentItem(0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doctor_registration_tabs, container, false);
        firstLayout = (LinearLayout)view.findViewById(R.id.first_text);
        firstLayout.setOnClickListener(this);
        secondLayout = (LinearLayout)view.findViewById(R.id.second_text);
        secondLayout.setOnClickListener(this);
        setUpView(view);
        setTab(view);



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        listener.privateClinic(R.id.clinic);
    }

    @Override
    public void onClick(View v) {

        if(v == firstLayout){
            Log.d("TAG","first layout");
            _mViewPager.setCurrentItem(0);

            getActivity().findViewById(R.id.first_tab).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.second_tab).setVisibility(View.INVISIBLE);
        }else if(v == secondLayout){

            Log.d("TAG","second layout");
            _mViewPager.setCurrentItem(1);
            getActivity().findViewById(R.id.first_tab).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.second_tab).setVisibility(View.VISIBLE);
        }
    }

    private void setTab(final View v){
        _mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrollStateChanged(int position) {}
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}
            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                switch(position){
                    case 0:
                        v.findViewById(R.id.first_tab).setVisibility(View.VISIBLE);
                        v.findViewById(R.id.second_tab).setVisibility(View.INVISIBLE);
                        break;

                    case 1:
                        v.findViewById(R.id.first_tab).setVisibility(View.INVISIBLE);
                        v.findViewById(R.id.second_tab).setVisibility(View.VISIBLE);
                        break;
                }
            }

        });

    }

    private static boolean isAddressAvailable(final LinearLayout addressLayout){

        if(addressLayout == null){
            return false;
        }

        EditText addressEdit;
        EditText doorNoEdit;
        EditText stateEdit;
        EditText cityEdit;
        EditText zipEdit;

        for (int i = 0; i < addressLayout.getChildCount(); i++)
        {
            RelativeLayout childView = (RelativeLayout) addressLayout.getChildAt(i);
            addressEdit = (EditText) childView.findViewById(R.id.edittext_address_one);
            doorNoEdit = (EditText) childView.findViewById(R.id.edittext_address_two);
            stateEdit =(EditText) childView.findViewById(R.id.edittext_state);
            cityEdit = (EditText) childView.findViewById(R.id.edittext_city);
            zipEdit = (EditText) childView.findViewById(R.id.edittext_zipcode);

            if(addressEdit.getText().toString().trim().length() > 0){
                return true;
            }

            if(stateEdit.getText().toString().trim().length() > 0){
                return true;
            }

            if(cityEdit.getText().toString().length() > 0){
                return true;
            }

            if(zipEdit.getText().toString().length() > 0){
                return true;
            }
        }

        return false;
    }

    public static void validateAndProceedNext(Activity activity, boolean myFlag) {

        if(!(isAddressAvailable(RegisterHospitalFragment.addViewAddressLayout) || isAddressAvailable(RegisterClinicFragment.addViewAddressLayout))){
            Utils.DISPLAY_GENERAL_DIALOG(activity, "Invalid Address", "Please check entered address and try again.");
            return;
        }

        ArrayList<AddressProfile> addressArrayList = new ArrayList<>();

        if(isAddressAvailable(RegisterHospitalFragment.addViewAddressLayout)){
            if(!areAllFieldsEntered(activity, RegisterHospitalFragment.addViewAddressLayout, AddressProfile.ADDRESS_TYPE.PRIVATE, addressArrayList))
                return;
        }

        if(isAddressAvailable(RegisterClinicFragment.addViewAddressLayout)){
            if(!areAllFieldsEntered(activity, RegisterClinicFragment.addViewAddressLayout, AddressProfile.ADDRESS_TYPE.HOSPITAL, addressArrayList))
                return;
        }

        ArrayList<AddressProfile> temp = DoctorProfile.getInstance().getAddressArrayList();

        /*for (AddressProfile address: temp){
            System.out.println("address.getType(): " + address.getType());
            Toast.makeText(activity, address.getType() + address.getZipCode() + "", Toast.LENGTH_SHORT).show();
        }*/

        if(myFlag){
            if(activity instanceof RegisterActivity){
                ((RegisterActivity) activity).submit();
            }
        }else{
//            listener.nextPrivateClinic();
        }

       /* Intent intent = new Intent(activity, DoctorSetPasswordActivity.class);
        startActivity(intent);

        DoctorOTP_Activity.ACTIVITY_STACK.add(activity);*/

    }

    private static boolean areAllFieldsEntered(Activity activity, LinearLayout addressLayout, int addressType, ArrayList<AddressProfile> addressArrayList){
        RelativeLayout childView;
        EditText addressEdit;
        EditText doorNoEdit;
        EditText stateEdit;
        EditText cityEdit;
        EditText zipEdit;
        DoctorProfile doctorProfile = DoctorProfile.getInstance();

        for (int i = 0; i < addressLayout.getChildCount(); i++)
        {
            AddressProfile address = new AddressProfile();
//            Address address = new Address();
            childView = (RelativeLayout) addressLayout.getChildAt(i);
            addressEdit = (EditText) childView.findViewById(R.id.edittext_address_one);
            Log.d("TAG",addressEdit.getText().toString());
            doorNoEdit = (EditText) childView.findViewById(R.id.edittext_address_two);
            Log.d("TAG","doorNoEdit "+doorNoEdit.getText().toString());
            stateEdit =(EditText) childView.findViewById(R.id.edittext_state);
            Log.d("TAG","state edit "+stateEdit.getText().toString());
            cityEdit = (EditText) childView.findViewById(R.id.edittext_city);
            Log.d("TAG","city edit "+cityEdit.getText().toString());
            zipEdit = (EditText) childView.findViewById(R.id.edittext_zipcode);

            if(addressEdit.getText().toString().trim().length() == 0 || addressEdit.getText().toString().isEmpty()){
                Toast.makeText(activity, "please enter the text in address line 1", Toast.LENGTH_SHORT).show();
                return false;
            }else if(addressEdit.getText().toString().length()>0){
                address.setAddress1(addressEdit.getText().toString());
            }

            if(doorNoEdit.getText().toString().trim().length() ==0 || doorNoEdit.getText().toString().isEmpty()){
                Toast.makeText(activity, "please enter the text in address line 2", Toast.LENGTH_SHORT).show();
                return false;
            }else if(doorNoEdit.getText().length()>0){
                address.setAddress2(doorNoEdit.getText().toString());
            }

            if(stateEdit.getText().toString().trim().length() ==0 || stateEdit.getText().toString().isEmpty()){
                Toast.makeText(activity, "please enter the state", Toast.LENGTH_SHORT).show();
                return false;
            }else if(stateEdit.getText().length() > 0){
                address.setState(stateEdit.getText().toString());
            }

            if(cityEdit.getText().toString().length() == 0 || cityEdit.getText().toString().isEmpty()){
                Toast.makeText(activity, "please enter the city", Toast.LENGTH_SHORT).show();
                return false;
            }else if(cityEdit.getText().toString().length() > 0){
                address.setCity(cityEdit.getText().toString());
            }

            if(zipEdit.getText().toString().length() == 0 || zipEdit.getText().toString().isEmpty()){
                Toast.makeText(activity, "please enter the valid zipcode", Toast.LENGTH_SHORT).show();
                return false;
            }else if(!GlobalVariables.validate(zipEdit.getText().toString(), GlobalVariables.ZIPCODE_PATTERN)){
                Toast.makeText(activity, "please enter the valid zipcode", Toast.LENGTH_SHORT).show();
                return false;
            }else if(zipEdit.getText().length() > 0){
                System.out.println("Kishore: zipEdit.getText(): " + zipEdit.getText());
                address.setZipCode(zipEdit.getText() + "");
            }


            address.setType(addressType);
            addressArrayList.add(address);

            /*Toast.makeText(getActivity(), ""+zipEdit.getText(), Toast.LENGTH_LONG).show();
            Toast.makeText(getActivity(), "asd "+address.getZipCode(), Toast.LENGTH_LONG).show();*/
        }
        doctorProfile.setAddressArrayList(addressArrayList);

        /*Toast.makeText(getActivity(), "addressArrayList.get(0).getZipCode() "+addressArrayList.get(0).getZipCode(), Toast.LENGTH_LONG).show();
        Toast.makeText(getActivity(), "addressArrayList.get(1).getZipCode() "+addressArrayList.get(1).getZipCode(), Toast.LENGTH_LONG).show();*/


        return true;
    }
}
