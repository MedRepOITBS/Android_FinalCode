package com.app.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.pojo.DoctorProfile;
import com.app.pojo.MyProfile;
import com.app.pojo.Register;
import com.app.util.GlobalVariables;

import medrep.medrep.R;
import medrep.medrep.RegisterActivity;

/**
 * Created by masood on 9/1/15.
 */
public class RegisterOneFragment extends Fragment implements View.OnClickListener{

    private NextScreenListener listener;
    private EditText doctorIdEditText;
    private EditText firstNameEdit;
    private EditText lastNameEdit;
    private EditText mobileEdit;
    private EditText emailEdit;
    private EditText passwordEdit;
    private LinearLayout addSecMobileLayout;
    private LinearLayout addAltEmailLayout;
    private TextView addMobileTextView;
    private TextView addEmailTextView;
    private Button nextButton;
    private Button backButton;
    private EditText alternateMobileEdit;
    private EditText alternativeEmailEdit;
    Register register;
    private MyProfile profile;
    private boolean myflag;
    private DoctorProfile doctorProfile;

    public interface NextScreenListener {
        public void nextScreen();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (NextScreenListener)activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(getActivity().getIntent().getExtras() != null){
            myflag = getActivity().getIntent().getExtras().getBoolean("myprofile");
        }


        View view = inflater.inflate(R.layout.doctor_registration_one, container, false);
        doctorIdEditText = (EditText) view.findViewById(R.id.edittext_doctorid);
        firstNameEdit = (EditText) view.findViewById(R.id.edittext_firstname);
        lastNameEdit = (EditText) view.findViewById(R.id.edittext_lastname);
        mobileEdit = (EditText) view.findViewById(R.id.edittext_mobile);
        emailEdit = (EditText)view.findViewById(R.id.edittext_email);


        addSecMobileLayout = (LinearLayout) view.findViewById(R.id.add_mobile_layout);
        addSecMobileLayout.setOnClickListener(this);

        addAltEmailLayout = (LinearLayout) view.findViewById(R.id.add_email_layout);
        addAltEmailLayout.setOnClickListener(this);

        addMobileTextView = (TextView) view.findViewById(R.id.addMobiletextView);
//        addMobileTextView.setTypeface(typeface);
        addMobileTextView.setText(getString(R.string.add_mobile));

        addEmailTextView = (TextView) view.findViewById(R.id.altEmailTextView);
//        addEmailTextView.setTypeface(typeface);
        addEmailTextView.setText(getString(R.string.alt_email));

        nextButton = (Button) view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);

        if(myflag){
            nextButton.setText("Update");
            mobileEdit.setEnabled(false);
        }

        backButton = (Button) view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myflag){
                    getActivity().finish();
                }else {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });



       /* if(getActivity().getIntent().getExtras() != null) {

            Bundle b1 = getActivity().getIntent().getBundleExtra("bundle");
            register = b1.getParcelable("register");
            Log.d("TAG", "MY FIRSTNAME IS "+register);
//            firstNameEdit.setText(register.getFirstname());
        }*/
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(myflag){
            setText();
        }
    }

    private void setText(){

        profile = MyProfile.getInstance();
     //   doctorIdEditText.setText(profile.getDoctorId());

        firstNameEdit.setText(profile.getFirstname());
        lastNameEdit.setText(profile.getLastname());
        mobileEdit.setText(profile.getMobileNumber());
        emailEdit.setText(profile.getEmail());
        doctorIdEditText.setText(profile.getRegistrationNumber());

        Log.d("AltEmail", profile.getAltEmail() + "");

        if(profile.getAltEmail() != null && !profile.getAltEmail().trim().equals("")){
            addAlternativeEmailEditText(addAltEmailLayout);
            alternativeEmailEdit.setText(profile.getAltEmail());
        }

        Log.d("AltMobile", profile.getAltMobileNumber() + "");

        if(profile.getAltMobileNumber() != null && !profile.getAltMobileNumber().trim().equals("")){
            addSecondaryMobileEditText(addSecMobileLayout);
            alternateMobileEdit.setText(profile.getAltMobileNumber());
        }

        doctorIdEditText.setEnabled(false);

       /* emailEdit.setEnabled(false);
        mobileEdit.setEnabled(false);
        doctorIdEditText.setEnabled(false);*/

    }

    @Override
    public void onClick(View v) {
        if(v == addSecMobileLayout){
            addSecondaryMobileEditText(addSecMobileLayout);
        }else if(v == addAltEmailLayout){
            addAlternativeEmailEditText(addAltEmailLayout);
        }else if(v == nextButton){

            if(setValidation()) {
                doctorProfile = DoctorProfile.getInstance();
                doctorProfile.setFirstName(firstNameEdit.getText().toString());
                doctorProfile.setLastName(lastNameEdit.getText().toString());
                doctorProfile.setEmail(emailEdit.getText().toString());
                doctorProfile.setMobileNumber(mobileEdit.getText().toString());

                String doctorID = doctorIdEditText.getText() + "";

                if(doctorID != null && doctorID.trim().equals("")){
                    doctorIdEditText.setError("Registration id cannot be empty.");
                    return;
                }else{
                    doctorProfile.setDoctorId(doctorID);
                }

                if(alternateMobileEdit !=null){
                    if(alternateMobileEdit.getVisibility() == View.VISIBLE){
                        doctorProfile.setAltMobileNumber(alternateMobileEdit.getText().toString());
                    }
                }

                if(alternativeEmailEdit != null){
                    if(alternativeEmailEdit.getVisibility() == View.VISIBLE){
                        doctorProfile.setAltEmail(alternativeEmailEdit.getText().toString());
                    }
                }
                if(myflag){
                    if(getActivity() instanceof RegisterActivity){
                        ((RegisterActivity) getActivity()).submit();
                    }
                }else{
                    listener.nextScreen();
                }

               /* RegisterAddressFragment addressFragment = new RegisterAddressFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
//                fragmentTransaction.add(addressFragment, "AddressFragment");
                fragmentTransaction.replace(R.id.category_layout, addressFragment, "address");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/
            }
        }else if(v == backButton){
            System.out.println("Back Button");
            getActivity().getSupportFragmentManager().popBackStack();
        }

    }

    private boolean setValidation(){
        /*if(doctorIdEditText.getText().toString().trim().length() == 0 || doctorIdEditText.getText().toString().isEmpty()){
            doctorIdEditText.setError("Doctor registration id cannot be empty.");
            return false;
        }else */if(firstNameEdit.getText().toString().trim().length() == 0 || firstNameEdit.getText().toString().isEmpty()){
            firstNameEdit.setError("First name cannot be empty.");
//            Toast.makeText(getActivity(), "please enter the valid first name", Toast.LENGTH_SHORT).show();
            return false;
        }/*else if(!GlobalVariables.validate(firstNameEdit.getText().toString(), GlobalVariables.USERNAME_PATTERN)){
            Toast.makeText(getActivity(), "please enter the valid username", Toast.LENGTH_SHORT).show();
            return false;
        }*/else if(lastNameEdit.getText().toString().length() ==0 || lastNameEdit.getText().toString().isEmpty()){
            lastNameEdit.setError("Last name cannot be empty.");
//            Toast.makeText(getActivity(), "please enter the valid last name", Toast.LENGTH_SHORT).show();
            return false;
        }/*else if(!GlobalVariables.validate(lastNameEdit.getText().toString(), GlobalVariables.USERNAME_PATTERN)){
            Toast.makeText(getActivity(), "please enter the valid lastname", Toast.LENGTH_SHORT).show();
            return false;
        }*/else if(mobileEdit.getText().toString().length() == 0 || mobileEdit.getText().toString().isEmpty()){
            mobileEdit.setError("Primary mobile number cannot be empty.");
//            Toast.makeText(getActivity(), "please enter the valid email", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!GlobalVariables.validate(mobileEdit.getText().toString(), GlobalVariables.MOBILE_NUMBER_PATTERN)){
            mobileEdit.setError("Please enter 10 digits mobile number.");
//            Toast.makeText(getActivity(), "please enter the valid email", Toast.LENGTH_SHORT).show();
            return false;
        }else if(emailEdit.getText().toString().length() == 0 || emailEdit.getText().toString().isEmpty()){
            emailEdit.setError("Email address cannot be empty.");
//            Toast.makeText(getActivity(), "please enter the valid email", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!GlobalVariables.validate(emailEdit.getText().toString(), GlobalVariables.EMAIL_PATTERN)){
            emailEdit.setError("Please enter valid email address.");
//            Toast.makeText(getActivity(), "please enter the valid email", Toast.LENGTH_SHORT).show();
            return false;
        }else if(addSecMobileLayout != null &&
                addSecMobileLayout.getChildCount() > 0 &&
                alternateMobileEdit != null &&
                (alternateMobileEdit.getText() + "").trim().length() > 0 &&
                !GlobalVariables.validate(alternateMobileEdit.getText() + "", GlobalVariables.MOBILE_NUMBER_PATTERN)){
                alternateMobileEdit.setError("Please enter the valid alternative mobile.");
                return false;
        }else if(addAltEmailLayout != null && addAltEmailLayout.getChildCount() > 0 && alternativeEmailEdit != null &&
                (alternativeEmailEdit.getText() + "").trim().length() > 0 &&
                !GlobalVariables.validate(alternativeEmailEdit.getText().toString(), GlobalVariables.EMAIL_PATTERN)){
                alternativeEmailEdit.setError("Please enter the valid alternative email address.");
                return false;
        }

        return  true;
    }

    private void addSecondaryMobileEditText(LinearLayout addMobileLayout){
        /*alternateMobileEdit = addEditText();
      //  alternateMobileEdit = addEditText(altMobileNumber);
//            altMobileNumber.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        alternateMobileEdit.setHint("SECONDARY MOBILE NUMBER");
        alternateMobileEdit.setInputType(InputType.TYPE_CLASS_PHONE);
        alternateMobileEdit.setMaxEms(10);
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(10);
        alternateMobileEdit.setFilters(FilterArray);*/

       /* alternateMobileEdit.setHint("SECONDARY MOBILE NUMBER");
        addMobileLayout.addView(alternateMobileEdit);
        addMobileTextView.setVisibility(View.GONE);

        alternateMobileEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
*/

        if(alternateMobileEdit == null){
            alternateMobileEdit = addEditText();
//            altMobileNumber.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            alternateMobileEdit.setHint("SECONDARY MOBILE NUMBER");
            alternateMobileEdit.setInputType(InputType.TYPE_CLASS_PHONE);
            alternateMobileEdit.setMaxEms(10);
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(10);
            alternateMobileEdit.setFilters(FilterArray);

            addMobileLayout.addView(alternateMobileEdit);
            addMobileTextView.setText("Remove");
            addMobileTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.minus, 0, 0, 0);
            addMobileTextView.setCompoundDrawablePadding(5);

            //Change image here
        }else{
            //Change image here
            addMobileLayout.removeView(alternateMobileEdit);
            addMobileTextView.setText("ADD MOBILE NUMBER");
            addMobileTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.add_icon, 0, 0, 0);
            addMobileTextView.setCompoundDrawablePadding(5);
            alternateMobileEdit = null;
        }
    }


    private void addAlternativeEmailEditText(LinearLayout addEmailLayout){
        /*alternativeEmailEdit = addEditText();
        alternativeEmailEdit.setHint("Alternative Email Address");
        addEmailLayout.addView(alternativeEmailEdit);
        addEmailTextView.setVisibility(View.GONE);
        alternativeEmailEdit.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);*/

        if(alternativeEmailEdit == null){

            //      final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,48); // Width , height



            alternativeEmailEdit = addEditText();
            //   altEmailID_Edit.setLayoutParams(lparams);

            //   altEmailID_Edit.set
            alternativeEmailEdit.setHint("ALTERNATE EMAIL ADDRESS");
            alternativeEmailEdit.setTextSize(15);
            alternativeEmailEdit.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            addEmailLayout.addView(alternativeEmailEdit);
            addEmailTextView.setText("Remove");
            addEmailTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.minus, 0, 0, 0);
            addEmailTextView.setCompoundDrawablePadding(5);

            //Change image here
        }else{
            //Change image here



            addEmailLayout.removeView(alternativeEmailEdit);
            addEmailTextView.setText("ALTERNATE EMAIL ADDRESS");
            addEmailTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.add_icon, 0, 0, 0);
            addEmailTextView.setCompoundDrawablePadding(5);
            alternativeEmailEdit = null;
        }
    }

    private EditText addEditText(){
        EditText addEditText = new EditText(getActivity());
        addEditText.setLayoutParams(getLayoutParms());
        addEditText.setPadding(15, 15, 15, 15);
        addEditText.setTextSize(15);
        addEditText.setBackgroundColor(getResources().getColor(R.color.home_button_text));
        return addEditText;
    }

    private LinearLayout.LayoutParams getLayoutParms(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 0);
        return  params;
    }
}
