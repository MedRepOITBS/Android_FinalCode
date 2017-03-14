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
import com.app.pojo.DoctorProfile;
import com.app.pojo.MyProfile;
import com.app.util.GlobalVariables;

import java.util.ArrayList;
import java.util.List;

import medrep.medrep.GPSService;
import medrep.medrep.R;


public class RegisterHospitalFragment extends Fragment implements View.OnClickListener{

    private NextPublicClinicListener listener;
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
    private boolean myflag;
    private Typeface typeface;

    int addEditTextcount;
    private int childIndex = -1;
//    private int removeAt = -1;
    /*private ArrayList<AddressProfile> addressArrayList;*/
    private Address address;

    public interface NextPublicClinicListener{
        public void nextPublicClinic();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (NextPublicClinicListener) activity;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void addAddress(AddressProfile profile){
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

                int removeAt = (Integer) v.getTag();

                /*RelativeLayout view = (RelativeLayout) */addViewAddressLayout.removeViewAt(removeAt);
//                ((LinearLayout) view.getParent()).removeView(view);
                Log.d("TAG", "my onclick childcount " + addViewAddressLayout.getChildCount());
                if (addViewAddressLayout.getChildCount() == 1) {
                    Log.d("TAG", "my onclick condition");
//                    deleteImageView.setBackgroundResource(0);
                    disableDelButton();
                }
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(getActivity().getIntent().getExtras() != null){
            myflag = getActivity().getIntent().getExtras().getBoolean("myprofile");
        }

        View v = inflater.inflate(R.layout.doctor_reg_hospital, container, false);
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

//        addAddress();


        if(myflag){
            MyProfile myProfile = MyProfile.getInstance();
            List<AddressProfile> addressProfileList = myProfile.getAddressArrayList();
            Log.d("TAG","MY ADDRESS PROFILE LIST Is "+addressProfileList.size());
            /*for (int count = 0; count< addressProfileList.size(); count++){
                addAdress(addressProfileList.get(count));
            }*/

            boolean addressAvailable = false;

            for(AddressProfile addressProfile: addressProfileList){

                if(addressProfile.getType() == AddressProfile.ADDRESS_TYPE.PRIVATE){
                    this.addAddress(addressProfile);
                    addressAvailable = true;
                }

            }

            if(!addressAvailable){
                addAddress();
            }


        }else{
            addAddress();
        }

        return v;
    }

    public static RegisterHospitalFragment newInstance(Context context){
        RegisterHospitalFragment publicFragment = new RegisterHospitalFragment();
        return publicFragment;
    }

    @Override
    public void onClick(View v) {
        if(v == nextButton){
            Log.d("TAG", "my child layout count  "+addAddressLayout.getChildCount());
            /*if(areAllFieldsEntered()){
                if(myflag){
                    if(getActivity() instanceof RegisterActivity){
                        ((RegisterActivity) getActivity()).submit();
                    }
                }else{
                    listener.nextPublicClinic();
                }
            }*/
            RegisterAddressFragment.validateAndProceedNext(getActivity(), myflag);

        }else if(v == addAddressTextView){
            addAddress();
        }
    }



    public void addAddress(){
        addAddressLayout = (RelativeLayout)getActivity().getLayoutInflater().inflate(R.layout.doctor_registration_address, null);
        final ImageView deleteImageView = (ImageView) addAddressLayout.findViewById(R.id.removeImageView);

        TextView addressCountTextView = (TextView) addAddressLayout.findViewById(R.id.addressTextView);
        addressCountTextView.setTypeface(typeface);
        addressCountTextView.setText("ADDRESS " + " " + (addViewAddressLayout.getChildCount()+1));
        addressEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_address_one);
        doorNoEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_address_two);
        stateEdit =(EditText) addAddressLayout.findViewById(R.id.edittext_state);
        cityEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_city);
        zipEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_zipcode);


//        childIndex = childIndex + 1;
        addAddressLayout.setTag(childIndex);
//        addViewAddressLayout.addView(addAddressLayout, addViewAddressLayout.getChildCount());
        addViewAddressLayout.addView(addAddressLayout);
//        deleteImageView.setTag(childIndex);
        deleteImageView.setTag(addViewAddressLayout.getChildCount() - 1);

        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int removeAt = (Integer) deleteImageView.getTag();

//                RelativeLayout view = (RelativeLayout) addViewAddressLayout.findViewWithTag(removeAt);
//                ((LinearLayout) view.getParent()).removeView(view);
                ((LinearLayout) deleteImageView.getParent().getParent()).removeViewAt(removeAt);
                Log.d("TAG", "my onclick childcount " + addViewAddressLayout.getChildCount());
                if (addViewAddressLayout.getChildCount() == 1) {
                    Log.d("TAG", "my onclick condition");
//                deleteImageView.setBackgroundResource(0);
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

    private boolean areAllFieldsEntered()
    {
        RelativeLayout childView;
        EditText addressEdit;
        EditText doorNoEdit;
        EditText stateEdit;
        EditText cityEdit;
        EditText zipEdit;
        DoctorProfile doctorProfile = DoctorProfile.getInstance();

        ArrayList<AddressProfile> addressArrayList = new ArrayList<>();

//        Toast.makeText(getActivity(), ""+addViewAddressLayout.getChildCount(), Toast.LENGTH_LONG).show();

        for (int i = 0; i < addViewAddressLayout.getChildCount(); i++)
        {
            AddressProfile address = new AddressProfile();
//            Address address = new Address();
            childView = (RelativeLayout) addViewAddressLayout.getChildAt(i);
            addressEdit = (EditText) childView.findViewById(R.id.edittext_address_one);
            Log.d("TAG",addressEdit.getText().toString());
            doorNoEdit = (EditText) childView.findViewById(R.id.edittext_address_two);
            Log.d("TAG","doorNoEdit "+doorNoEdit.getText().toString());
            stateEdit =(EditText) childView.findViewById(R.id.edittext_state);
            Log.d("TAG","state edit "+stateEdit.getText().toString());
            cityEdit = (EditText) childView.findViewById(R.id.edittext_city);
            Log.d("TAG","city edit "+cityEdit.getText().toString());
            zipEdit = (EditText) childView.findViewById(R.id.edittext_zipcode);

            System.out.println("zipEdit.getText(): " + zipEdit.getText());

            if(addressEdit.getText().toString().trim().length() == 0 || addressEdit.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "please enter the text in address line 1", Toast.LENGTH_SHORT).show();
                return false;
            }else if(addressEdit.getText().toString().length()>0){
                address.setAddress1(addressEdit.getText().toString());
            }

            if(doorNoEdit.getText().toString().trim().length() ==0 || doorNoEdit.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "please enter the text in address line 2", Toast.LENGTH_SHORT).show();
                return false;
            }else if(doorNoEdit.getText().length()>0){
                address.setAddress2(doorNoEdit.getText().toString());
            }

            if(stateEdit.getText().toString().trim().length() ==0 || stateEdit.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "please enter the state", Toast.LENGTH_SHORT).show();
                return false;
            }else if(stateEdit.getText().length() > 0){
                address.setState(stateEdit.getText().toString());
            }

            if(cityEdit.getText().toString().length() == 0 || cityEdit.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "please enter the city", Toast.LENGTH_SHORT).show();
                return false;
            }else if(cityEdit.getText().toString().length() > 0){
                address.setCity(cityEdit.getText().toString());
            }

            if(zipEdit.getText().toString().length() == 0 || zipEdit.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "please enter the valid zipcode", Toast.LENGTH_SHORT).show();
                return false;
            }/*else if(!GlobalVariables.validate(zipEdit.getText().toString(), GlobalVariables.ZIPCODE_PATTERN)){
                Toast.makeText(getActivity(), "please enter the valid zipcode", Toast.LENGTH_SHORT).show();
                return false;
            }*/else if(zipEdit.getText().length() > 0){
                System.out.println("Kishore: zipEdit.getText()2: " + zipEdit.getText());
                address.setZipCode(zipEdit.getText().toString());
            }

            address.setType(AddressProfile.ADDRESS_TYPE.HOSPITAL);
            addressArrayList.add(address);
        }
        doctorProfile.setAddressArrayList(addressArrayList);

        return true;
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
