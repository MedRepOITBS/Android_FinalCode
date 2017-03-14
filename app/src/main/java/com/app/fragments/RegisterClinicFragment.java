package com.app.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pojo.AddressProfile;
import com.app.pojo.MyProfile;
import com.app.util.GlobalVariables;

import java.util.ArrayList;
import java.util.List;

import medrep.medrep.GPSService;
import medrep.medrep.R;

/**
 * Created by masood on 9/2/15.
 */
public class RegisterClinicFragment extends Fragment implements View.OnClickListener{

    private NextPrivateClinicListener listener;
    private Button nextButton;
    private EditText addressEdit;
    private EditText doorNoEdit;
    private EditText stateEdit;
    private EditText cityEdit;
    private EditText zipEdit;
    public static LinearLayout addViewAddressLayout;
    private RelativeLayout addAddressLayout;
    private TextView addMoreAddress;
    private TextView addAddressTextView;
    private EditText zipCode;
//    private ImageView deleteImageView;
    private Typeface typeface;

    int addEditTextcount;
    private int childIndex = -1;
    private int removeAt = -1;
//    private ArrayList<AddressProfile> addressArrayList;
    private MyProfile myProfile;
    private boolean myflag;
    private Address address;

    public interface NextPrivateClinicListener{
        public void nextPrivateClinic();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (NextPrivateClinicListener) activity;
    }

    public static RegisterClinicFragment newInstance(){
        RegisterClinicFragment fragment = new RegisterClinicFragment();
        return fragment;
    }

    public static RegisterClinicFragment newInstance(Context context){
        RegisterClinicFragment privateFragment = new RegisterClinicFragment();
        return privateFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getActivity().getIntent().getExtras() != null){
            myflag = getActivity().getIntent().getExtras().getBoolean("myprofile");
        }
        View v = inflater.inflate(R.layout.doctor_reg_clinic, container, false);
        typeface = GlobalVariables.getTypeface(getActivity());
        addViewAddressLayout = (LinearLayout) v.findViewById(R.id.add_address_layout);
        addAddressTextView = (TextView) v.findViewById(R.id.addAddressTextView);
        addAddressTextView.setTypeface(typeface);
        addAddressTextView.setText(getString(R.string.add_address));
        addAddressTextView.setOnClickListener(this);
        nextButton = (Button)v.findViewById(R.id.nextButton);

        if(myflag){
            nextButton.setText("Update");
        }

        nextButton.setOnClickListener(this);

        Button backButton = (Button)v.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().finish();
            }
        });
//        addAdress();


        if(myflag){
            myProfile= MyProfile.getInstance();
            List<AddressProfile> addressProfileList = myProfile.getAddressArrayList();
            Log.d("TAG","MY ADDRESS PROFILE LIST Is "+addressProfileList.size());
            /*for (int count = 0; count< addressProfileList.size(); count++){
                addAdress(addressProfileList.get(count));
            }*/

            boolean addressAvailable = false;

            for(AddressProfile addressProfile: addressProfileList){

                if(addressProfile.getType() != AddressProfile.ADDRESS_TYPE.PRIVATE){
                    this.addAdress(addressProfile);
                    addressAvailable = true;
                }
            }

            if(!addressAvailable){
                addAdress();
            }

        }else{
            addAdress();
        }

        return v;
    }

    @Override
    public void onClick(View v) {
        if(v == nextButton){
            Log.d("TAG", "my child layout count  " + addAddressLayout.getChildCount());
            /*if(areAllFieldsEntered()){
                if(myflag){
                    if(getActivity() instanceof RegisterActivity){
                        ((RegisterActivity) getActivity()).submit();
                    }
                }else{
                    listener.nextPrivateClinic();
                }
            }*/

            RegisterAddressFragment.validateAndProceedNext(getActivity(), myflag);

        }else if(v == addAddressTextView){
            addAdress();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void addAdress(AddressProfile profile){
        addAddressLayout = (RelativeLayout)getActivity().getLayoutInflater().inflate(R.layout.doctor_registration_address, null);
        final ImageView deleteImageView = (ImageView) addAddressLayout.findViewById(R.id.removeImageView);

        TextView addressCountTextView = (TextView) addAddressLayout.findViewById(R.id.addressTextView);
        addressCountTextView.setTypeface(typeface);
        addressCountTextView.setText("ADDRESS " + " " + (addViewAddressLayout.getChildCount() + 1));
        addressEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_address_one);
        addressEdit.setText(profile.getAddress1());
        doorNoEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_address_two);
        doorNoEdit.setText(profile.getAddress2());
        stateEdit =(EditText) addAddressLayout.findViewById(R.id.edittext_state);
        stateEdit.setText(profile.getState());
        cityEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_city);
        cityEdit.setText(profile.getCity());
        zipEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_zipcode);

        String zipCode = profile.getZipCode();
        if(zipCode != null && !zipCode.trim().equals("") && !zipCode.trim().equalsIgnoreCase("null")){
            zipEdit.setText(zipCode);
        }


        childIndex = childIndex + 1;
        addAddressLayout.setTag(childIndex);
        addViewAddressLayout.addView(addAddressLayout);
//        deleteImageView.setTag(childIndex);

        deleteImageView.setTag(addViewAddressLayout.getChildCount() - 1);

        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeAt = (Integer) v.getTag();

                RelativeLayout view = (RelativeLayout) addViewAddressLayout.findViewWithTag(removeAt);
                ((LinearLayout) view.getParent()).removeView(view);
                Log.d("TAG", "my onclick childcount " + addViewAddressLayout.getChildCount());
                if (addViewAddressLayout.getChildCount() == 1) {
                    Log.d("TAG", "my onclick condition");
//                    deleteImageView.setBackgroundResource(0);
                    disableDelButton();
                }
            }
        });
    }

    public void addAdress(){
        addAddressLayout = (RelativeLayout)getActivity().getLayoutInflater().inflate(R.layout.doctor_registration_address, null);
        ImageView deleteImageView = (ImageView) addAddressLayout.findViewById(R.id.removeImageView);

        TextView addressCountTextView = (TextView) addAddressLayout.findViewById(R.id.addressTextView);
        addressCountTextView.setTypeface(typeface);
        addressCountTextView.setText("ADDRESS " + " " + (addViewAddressLayout.getChildCount() + 1));
        addressEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_address_one);
        doorNoEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_address_two);
        stateEdit =(EditText) addAddressLayout.findViewById(R.id.edittext_state);
        cityEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_city);
        zipEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_zipcode);


        childIndex = childIndex + 1;
        addAddressLayout.setTag(childIndex);
        addViewAddressLayout.addView(addAddressLayout);
//        deleteImageView.setTag(childIndex);

        deleteImageView.setTag(addViewAddressLayout.getChildCount() - 1);

        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeAt = (Integer) v.getTag();

                RelativeLayout view = (RelativeLayout) addViewAddressLayout.findViewWithTag(removeAt);
                ((LinearLayout) view.getParent()).removeView(view);
                Log.d("TAG", "my onclick childcount " + addViewAddressLayout.getChildCount());
                if (addViewAddressLayout.getChildCount() == 1) {
                    Log.d("TAG", "my onclick condition");
//                    deleteImageView.setBackgroundResource(0);
                    disableDelButton();
                }
            }
        });

        addAddressLayout.findViewById(R.id.auto_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddress(addAddressLayout);
            }
        });

        if(addViewAddressLayout.getChildCount() == 4){
            addAddressTextView.setVisibility(View.GONE);
        }else if (addViewAddressLayout.getChildCount() >= 2) {
            makeVisibleDelButtons();
        } else if (addViewAddressLayout.getChildCount() == 1){
            deleteImageView.setVisibility(View.GONE);
        }else if(addViewAddressLayout.getChildCount() <= 4){
            if(addAddressTextView.getVisibility()==View.GONE)
                addAddressTextView.setVisibility(View.VISIBLE);
        }
    }

    private void setAddress(RelativeLayout addAddressLayout) {

        if(address == null){
            GPSService mGPSService = new GPSService(getActivity());
            mGPSService.getLocation();

            if (mGPSService.isLocationAvailable == false) {
                // Here you can ask the user to try again, using return; for that
                Toast.makeText(getActivity(), "Your location is not available.", Toast.LENGTH_SHORT).show();
            } else {
                // Getting location co-ordinates
                address = mGPSService.getLocationAddress();
            }

            // make sure you close the gps after using it. Save user's battery power
            mGPSService.closeGPS();
        }


        if(address != null){
            EditText addressEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_address_one);
            EditText doorNoEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_address_two);
            EditText stateEdit =(EditText) addAddressLayout.findViewById(R.id.edittext_state);
            EditText cityEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_city);
            EditText zipEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_zipcode);

            String addressLine2 = "";

            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                if(i == 0){
                    addressEdit.setText(address.getAddressLine(i));
                }else{
                    addressLine2 = addressLine2 + address.getAddressLine(i);
                }
            }

            doorNoEdit.setText(addressLine2);
            stateEdit.setText(address.getAdminArea());
            cityEdit.setText(address.getLocality());
            zipEdit.setText(address.getPostalCode());
        }
    }

    private void disableDelButton()
    {
        RelativeLayout childView;
        ImageView imgView;
        for (int i = 0; i < addViewAddressLayout.getChildCount(); i++) {
            childView = (RelativeLayout) addViewAddressLayout.getChildAt(i);
            imgView = (ImageView) childView.findViewById(R.id.removeImageView);
            imgView.setVisibility(View.GONE);
        }
    }


    private void makeVisibleDelButtons()
    {
        RelativeLayout childView;
        ImageView imgView;
        Log.d("TAG", "my child count is make visible "+addViewAddressLayout.getChildCount());
        for (int i = 0; i < addViewAddressLayout.getChildCount(); i++) {
            childView = (RelativeLayout) addViewAddressLayout.getChildAt(i);
            imgView = (ImageView) addViewAddressLayout.findViewById(R.id.removeImageView);
            imgView.setVisibility(View.VISIBLE);
        }
    }
}
