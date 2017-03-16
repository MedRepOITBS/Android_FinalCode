package medrep.medrep;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pojo.AddressProfile;
import com.app.pojo.DoctorProfile;
import com.app.util.GlobalVariables;
import com.app.util.Utils;

import java.util.ArrayList;

import static com.app.util.GlobalVariables.getTypeface;

/**
 * Created by kishore on 7/10/15.
 */
public class RegisterDoctorAddressAct extends Activity {

    private int childIndex = -1;
    private TextView addAddressTextView;
    private Address address;

    private boolean autoAddress = false;
//    private LinearLayout addViewAddressLayout;
//    private ArrayList<AddressProfile> addressArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.doctor_registration);

        final View view = View.inflate(RegisterDoctorAddressAct.this, R.layout.doctor_registration_tabs, null);

        ((FrameLayout)findViewById(R.id.category_layout)).addView(view);

        final ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPager);

        TextView whyThis = (TextView)findViewById(R.id.whyThis);
        String textValue = "<a>Why This?</a>";
        whyThis.setText(Html.fromHtml(textValue));
        whyThis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(RegisterDoctorAddressAct.this);
                LayoutInflater inflater = RegisterDoctorAddressAct.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.why_this_layout, null);
                TextView titlealert = (TextView) dialogView.findViewById(R.id.title_alert);
                titlealert.setText("Address Capture");
                TextView description = (TextView) dialogView.findViewById(R.id.description);
                description.setText("MedRep has an easy address capture process. You can use the \'Pick a Location\' button to let GPS automatically capture the address of your \'Private Clinic\' and/or \'Hospital\'." );
                dialog.setContentView(dialogView);
                dialog.show();
            }
        });


        DoctorProfile doctorProfile = DoctorProfile.getInstance();
        ArrayList<AddressProfile> addressList = doctorProfile.getAddressArrayList();

        if(addressList != null && addressList.size() > 0){

            MyPagerAdapter adapter = new MyPagerAdapter(addressList);
            mViewPager.setAdapter(adapter);
            mViewPager.setCurrentItem(0);

            for(AddressProfile addressProfile: addressList){
                int type = addressProfile.getType();

                if(type != -1){
                    if(type == AddressProfile.ADDRESS_TYPE.HOSPITAL){
                        mViewPager.setCurrentItem(0);

                        view.findViewById(R.id.first_tab).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.second_tab).setVisibility(View.INVISIBLE);

                    }else if(type == AddressProfile.ADDRESS_TYPE.PRIVATE){
                        mViewPager.setCurrentItem(1);

                        view.findViewById(R.id.first_tab).setVisibility(View.INVISIBLE);
                        view.findViewById(R.id.second_tab).setVisibility(View.VISIBLE);

                    }
                }
            }
        }else{
            MyPagerAdapter adapter = new MyPagerAdapter();
            mViewPager.setAdapter(adapter);
            mViewPager.setCurrentItem(0);
        }

        LinearLayout firstLayout = (LinearLayout) view.findViewById(R.id.first_text);
        firstLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);

                view.findViewById(R.id.first_tab).setVisibility(View.VISIBLE);
                view.findViewById(R.id.second_tab).setVisibility(View.INVISIBLE);
            }
        });

        LinearLayout secondLayout = (LinearLayout) view.findViewById(R.id.second_text);
        secondLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
                view.findViewById(R.id.first_tab).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.second_tab).setVisibility(View.VISIBLE);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                Toast.makeText(RegisterDoctorAddressAct.this, "Position: " + position, Toast.LENGTH_SHORT).show();
                switch (position){
                    case 0:
                        view.findViewById(R.id.first_tab).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.second_tab).setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        view.findViewById(R.id.first_tab).setVisibility(View.INVISIBLE);
                        view.findViewById(R.id.second_tab).setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        backPressed();
    }

    private class MyPagerAdapter extends android.support.v4.view.PagerAdapter{

        ArrayList<AddressProfile> addressList;
        private View.OnClickListener backListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPressed();
            }
        };
        private LinearLayout privateClinicAddressLayout;
        private LinearLayout hospitalAddressLayout;

        public MyPagerAdapter(ArrayList<AddressProfile> addressList) {
            this.addressList = addressList;
        }

        public MyPagerAdapter() {
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            System.out.println("Position: " + position);

            /*if(addressList != null && addressList.size() > 0){
                for(AddressProfile addressProfile: addressList){
                    switch (addressProfile.getType()){
                        case AddressProfile.ADDRESS_TYPE.HOSPITAL:
                            getHospitalAddressView(container);
                            break;
                        case AddressProfile.ADDRESS_TYPE.PRIVATE:
                            getPrivateClinicAddress(container);
                            break;
                    }
                }
            }else{*/
            switch(position){
                case 0:
                    // 1st tab
                    return getHospitalAddressView(container, addressList);
                case 1:
                    //2nd tab
                    return getPrivateClinicAddress(container, addressList);
            }
//            }


            return null;
        }

        private View getPrivateClinicAddress(ViewGroup container, ArrayList<AddressProfile> addressList) {
            final View view2 = View.inflate(RegisterDoctorAddressAct.this, R.layout.doctor_reg_hospital, null);
            container.addView(view2);

            Typeface typeface = getTypeface(RegisterDoctorAddressAct.this);

            privateClinicAddressLayout = (LinearLayout) view2.findViewById(R.id.add_address_layout);
            addAddressTextView = (TextView) view2.findViewById(R.id.addAddressTextView);
            addAddressTextView.setTypeface(typeface);
            addAddressTextView.setText(getString(R.string.add_address));
            addAddressTextView.setTextSize(13);
            addAddressTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.add_icon, 0, 0, 0);
            addAddressTextView.setCompoundDrawablePadding(5);
            addAddressTextView.setPadding(20, 0, 0, 0);

            addAddressTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    autoAddress = false;
                    addAddress(privateClinicAddressLayout, null);
                }
            });

            Button nextButton = (Button) view2.findViewById(R.id.nextButton);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if (areAllFieldsEntered(privateClinicAddressLayout, AddressProfile.ADDRESS_TYPE.PRIVATE)) {
                        System.out.println("Success");

                        Intent intent = new Intent(RegisterDoctorAddressAct.this, DoctorSetPasswordActivity.class);
                        startActivity(intent);

                        DoctorOTP_Activity.ACTIVITY_STACK.add(RegisterDoctorAddressAct.this);

//                        finish();

                        *//*if(fragmentManager.findFragmentByTag("otp") == null){
                            OtpFragment fragment = new OtpFragment();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
                            fragmentTransaction.replace(R.id.category_layout, fragment, "otp");
                            fragmentTransaction.commit();
                        }*//*
                    }*/

                    validateAndProceedNext();
                }
            });

            Button backButton = (Button) view2.findViewById(R.id.backButton);
            backButton.setOnClickListener(backListener);

            if(addressList != null && addressList.size() > 0){
                for(AddressProfile addressProfile: addressList){
                    if(addressProfile != null && addressProfile.getType() == AddressProfile.ADDRESS_TYPE.PRIVATE){
                        addAddress(privateClinicAddressLayout, addressProfile);
                    }
                }
            }else{
//                autoAddress = true;
                addAddress(privateClinicAddressLayout, null);
            }

            /*if(address != null){
                setAddress(addViewAddressLayout2);
            }*/

            return view2;
        }

        private void setAddress(final RelativeLayout addAddressLayout) {


            if(autoAddress && address == null) {
                GPSService mGPSService = new GPSService(RegisterDoctorAddressAct.this);
                mGPSService.getLocation();

                if (mGPSService.isLocationAvailable == false) {
                    // Here you can ask the user to try again, using return; for that


                } else {
                    // Getting location co-ordinates
                    address = mGPSService.getLocationAddress();
                }

                boolean result = mGPSService.isLocationAvailable;

                // make sure you close the gps after using it. Save user's battery power
                mGPSService.closeGPS();
            }


            if (address != null && autoAddress) {
                EditText addressEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_address_one);
                EditText doorNoEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_address_two);
                EditText stateEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_state);
                EditText cityEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_city);
                EditText zipEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_zipcode);

                String addressLine2 = "";

                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    if (i == 0) {
                        addressEdit.setText(address.getAddressLine(i));
                    } else {
                        addressLine2 = addressLine2 + address.getAddressLine(i);
                    }
                }

                doorNoEdit.setText(addressLine2);
                stateEdit.setText(address.getAdminArea());
                cityEdit.setText(address.getLocality());
                zipEdit.setText(address.getPostalCode());
            }

        }


        public void addAddress(final LinearLayout addViewAddressLayout, AddressProfile addressProfile){
            final RelativeLayout addAddressLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.doctor_registration_address, null);
            ImageView deleteImageView = (ImageView) addAddressLayout.findViewById(R.id.removeImageView);
            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int removeAt = (Integer) v.getTag();

                    RelativeLayout view = (RelativeLayout) addViewAddressLayout.findViewWithTag(removeAt);
                    ((LinearLayout) view.getParent()).removeView(view);
                    Log.d("TAG", "my onclick child count " + addViewAddressLayout.getChildCount());
                    if (addViewAddressLayout.getChildCount() == 1) {
                        Log.d("TAG", "my onclick condition");
//                    deleteImageView.setBackgroundResource(0);
                        disableDelButton(addViewAddressLayout);
                    }
                }
            });

            Typeface typeface = getTypeface(RegisterDoctorAddressAct.this);
            TextView addressCountTextView = (TextView) addAddressLayout.findViewById(R.id.addressTextView);
            addressCountTextView.setTypeface(typeface);
            addressCountTextView.setText("ADDRESS " + " " + (addViewAddressLayout.getChildCount() + 1));

            addAddressLayout.findViewById(R.id.auto_address).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterDoctorAddressAct.this)
                            .setTitle("Please Confirm!")
                            .setMessage("Do you want to Pick Current GPS Location?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    autoAddress = true;
                                    setAddress(addAddressLayout);
                                }
                            })
                            .setNegativeButton("No", null);
                    builder.show();

                }
            });


            if(addressProfile != null){
                EditText addressEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_address_one);
                EditText doorNoEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_address_two);
                EditText stateEdit =(EditText) addAddressLayout.findViewById(R.id.edittext_state);
                EditText cityEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_city);
                EditText zipEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_zipcode);

                addressEdit.setText(addressProfile.getAddress1());
                doorNoEdit.setText(addressProfile.getAddress2());
                stateEdit.setText(addressProfile.getState());
                cityEdit.setText(addressProfile.getCity());
                zipEdit.setText(addressProfile.getZipCode());
            }else {
                setAddress(addAddressLayout);
            }



            childIndex = childIndex + 1;
            addAddressLayout.setTag(childIndex);
            addViewAddressLayout.addView(addAddressLayout);
            deleteImageView.setTag(childIndex);
            if(addViewAddressLayout.getChildCount() == 4){
                addAddressTextView.setVisibility(View.GONE);
            }else if (addViewAddressLayout.getChildCount() >= 2) {
                makeVisibleDelButtons(addViewAddressLayout);
            } else if (addViewAddressLayout.getChildCount() == 1){
                deleteImageView.setVisibility(View.GONE);
            }else if(addViewAddressLayout.getChildCount() <= 4){
                if(addAddressTextView.getVisibility()==View.GONE)
                    addAddressTextView.setVisibility(View.VISIBLE);
            }
        }


        public void addHospitalAddress(final LinearLayout addViewAddressLayout, AddressProfile addressProfile){
            final RelativeLayout addAddressLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.doctor_registration_address, null);
            ImageView deleteImageView = (ImageView) addAddressLayout.findViewById(R.id.removeImageView);

//            addViewAddressLayout = (LinearLayout)view.findViewById(R.id.add_address_layout);

            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int removeAt = (Integer) v.getTag();

                    RelativeLayout view = (RelativeLayout) addViewAddressLayout.findViewWithTag(removeAt);
                    ((LinearLayout) view.getParent()).removeView(view);
                    Log.d("TAG", "my onclick childcount " + addViewAddressLayout.getChildCount());
                    if (addViewAddressLayout.getChildCount() == 1) {
                        Log.d("TAG", "my onclick condition");
//                    deleteImageView.setBackgroundResource(0);
                        disableDelButton(addViewAddressLayout);
                    }
                }
            });

            Typeface typeface = getTypeface(RegisterDoctorAddressAct.this);

            TextView addressCountTextView = (TextView) addAddressLayout.findViewById(R.id.addressTextView);
            addressCountTextView.setTypeface(typeface);
            addressCountTextView.setText("ADDRESS " + " " + (addViewAddressLayout.getChildCount() + 1));

            addAddressLayout.findViewById(R.id.auto_address).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterDoctorAddressAct.this)
                            .setTitle("Please Confirm!")
                            .setMessage("Do you want to Pick Current GPS Location?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    autoAddress = true;
                                    setAddress(addAddressLayout);
                                }
                            })
                            .setNegativeButton("No", null);
                    builder.show();




                }
            });

            if(addressProfile != null){
                EditText addressEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_address_one);
                EditText doorNoEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_address_two);
                EditText stateEdit =(EditText) addAddressLayout.findViewById(R.id.edittext_state);
                EditText cityEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_city);
                EditText zipEdit = (EditText) addAddressLayout.findViewById(R.id.edittext_zipcode);

                addressEdit.setText(addressProfile.getAddress1());
                doorNoEdit.setText(addressProfile.getAddress2());
                stateEdit.setText(addressProfile.getState());
                cityEdit.setText(addressProfile.getCity());
                zipEdit.setText(addressProfile.getZipCode());
            }else{
                setAddress(addAddressLayout);
            }


            childIndex = childIndex + 1;
            addAddressLayout.setTag(childIndex);
            addViewAddressLayout.addView(addAddressLayout);
            deleteImageView.setTag(childIndex);
            if(addViewAddressLayout.getChildCount() == 4){
                addAddressTextView.setVisibility(View.GONE);
            }else if (addViewAddressLayout.getChildCount() >= 2) {
                makeVisibleDelButtons(addViewAddressLayout);
            } else if (addViewAddressLayout.getChildCount() == 1){
                deleteImageView.setVisibility(View.GONE);
            }else if(addViewAddressLayout.getChildCount() <= 4){
                if(addAddressTextView.getVisibility()==View.GONE)
                    addAddressTextView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }


        private boolean areAllFieldsEntered(final LinearLayout addViewAddressLayout, int addressType){
//            RelativeLayout childView;
            EditText addressEdit;
            EditText doorNoEdit;
            EditText stateEdit;
            EditText cityEdit;
            EditText zipEdit;

            DoctorProfile doctorProfile = DoctorProfile.getInstance();

            ArrayList<AddressProfile> addressArrayList = doctorProfile.getAddressArrayList();

            if(addressArrayList == null){
                addressArrayList = new ArrayList<>();
            }

            for (int i = 0; i < addViewAddressLayout.getChildCount(); i++)
            {
                AddressProfile address = new AddressProfile();
//            Address address = new Address();
                RelativeLayout childView = (RelativeLayout) addViewAddressLayout.getChildAt(i);
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
                   // addressEdit.setError("Address Line1 cannot be empty");
                    Toast.makeText(RegisterDoctorAddressAct.this, "Address Line1 cannot be empty", Toast.LENGTH_SHORT).show();
                    return false;
                }else if(addressEdit.getText().toString().length()>0){
                    address.setAddress1(addressEdit.getText().toString());
                }/*else{

                    addressEdit.setError(null);
                }*/

                if(doorNoEdit.getText().toString().trim().length() ==0 || doorNoEdit.getText().toString().isEmpty()){
//                    doorNoEdit.setError("Door no cannot be empty");
                    Toast.makeText(RegisterDoctorAddressAct.this, "Door no cannot be empty", Toast.LENGTH_SHORT).show();
                    return false;
                }else if(doorNoEdit.getText().length()>0){
                    address.setAddress2(doorNoEdit.getText().toString());
                }

                if(stateEdit.getText().toString().trim().length() ==0 || stateEdit.getText().toString().isEmpty()){
               //     stateEdit.setError("State cannot be empty");
                    Toast.makeText(RegisterDoctorAddressAct.this, "State cannot be empty", Toast.LENGTH_SHORT).show();

                    return false;
                }else if(stateEdit.getText().length() > 0){
                    address.setState(stateEdit.getText().toString());
                }/*else{

                    stateEdit.setError(null);
                }
*/
                if(cityEdit.getText().toString().length() == 0 || cityEdit.getText().toString().isEmpty()){
                  //  cityEdit.setError("City cannot be empty");
                    Toast.makeText(RegisterDoctorAddressAct.this, "City cannot be empty", Toast.LENGTH_SHORT).show();

                    return false;
                }else if(cityEdit.getText().toString().length() > 0){
                    address.setCity(cityEdit.getText().toString());
                }/*else{

                    cityEdit.setError(null);
                }*/


                if(zipEdit.getText().toString().length() == 0 || zipEdit.getText().toString().isEmpty()){
                   // zipEdit.setError("Zip code cannot be empty.");
                    Toast.makeText(RegisterDoctorAddressAct.this, "Zip code cannot be empty.", Toast.LENGTH_SHORT).show();

                    return false;
                }else if(zipEdit.getText().toString().trim().length() > 0 && !GlobalVariables.validate(zipEdit.getText().toString(), GlobalVariables.ZIPCODE_PATTERN)){
              //  zipEdit.setError("Please enter valid zip code");
                Toast.makeText(RegisterDoctorAddressAct.this, "Please enter valid zip code", Toast.LENGTH_SHORT).show();

                return false;
            }else if(zipEdit.getText().length() > 0){
                address.setZipCode(zipEdit.getText().toString());
            } /*else{

                zipEdit.setError(null);
            }*/
                address.setType(addressType);
                addressArrayList.add(address);
            }

            doctorProfile.setAddressArrayList(addressArrayList);

            return true;
        }


        private boolean isAddressAvailable(final LinearLayout addViewAddressLayout){

            if(addViewAddressLayout == null){
                return false;
            }

            EditText addressEdit;
            EditText doorNoEdit;
            EditText stateEdit;
            EditText cityEdit;
            EditText zipEdit;

            for (int i = 0; i < addViewAddressLayout.getChildCount(); i++)
            {
                RelativeLayout childView = (RelativeLayout) addViewAddressLayout.getChildAt(i);
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



        private void disableDelButton(LinearLayout addViewAddressLayout){
            RelativeLayout childView;
            ImageView imgView;
            for (int i = 0; i < addViewAddressLayout.getChildCount(); i++) {
                childView = (RelativeLayout) addViewAddressLayout.getChildAt(i);
                imgView = (ImageView) childView.findViewById(R.id.removeImageView);
                imgView.setVisibility(View.GONE);
            }
        }

        private void makeVisibleDelButtons(LinearLayout addViewAddressLayout)
        {
            RelativeLayout childView;
            ImageView imgView;
            Log.d("TAG", "my child count is make visible " + addViewAddressLayout.getChildCount());
            for (int i = 0; i < addViewAddressLayout.getChildCount(); i++) {
                childView = (RelativeLayout) addViewAddressLayout.getChildAt(i);
                imgView = (ImageView) addViewAddressLayout.findViewById(R.id.removeImageView);
                imgView.setVisibility(View.VISIBLE);
            }
        }

        public View getHospitalAddressView(ViewGroup container, ArrayList<AddressProfile> addressList) {
            final View view = View.inflate(RegisterDoctorAddressAct.this, R.layout.doctor_reg_clinic, null);
            container.addView(view);

            Typeface typeface = getTypeface(RegisterDoctorAddressAct.this);

            hospitalAddressLayout = (LinearLayout) view.findViewById(R.id.add_address_layout);
            addAddressTextView = (TextView) view.findViewById(R.id.addAddressTextView);
            addAddressTextView.setTypeface(typeface);
            addAddressTextView.setText(getString(R.string.add_address));
            addAddressTextView.setTextSize(13);
            addAddressTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.add_icon,0,0,0);
            addAddressTextView.setCompoundDrawablePadding(5);
            addAddressTextView.setPadding(20, 0, 0, 0);

            addAddressTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    autoAddress = false;
                    addHospitalAddress(hospitalAddressLayout, null);
                }
            });

            Button nextButton = (Button) view.findViewById(R.id.nextButton);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validateAndProceedNext();

                    /*if (areAllFieldsEntered(hospitalAddressLayout, AddressProfile.ADDRESS_TYPE.HOSPITAL)) {
                        System.out.println("Success");

                        Intent intent = new Intent(RegisterDoctorAddressAct.this, DoctorSetPasswordActivity.class);
                        startActivity(intent);

                        DoctorOTP_Activity.ACTIVITY_STACK.add(RegisterDoctorAddressAct.this);

//                        finish();
                            *//*doctorProfile.getAddressArrayList();

                            OtpFragment fragment = new OtpFragment();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
                            fragmentTransaction.replace(R.id.category_layout, fragment, "private");

                            fragmentTransaction.commit();*//*
                    }*/
                }
            });

            Button backButton = (Button) view.findViewById(R.id.backButton);
            backButton.setOnClickListener(backListener);

            if(addressList != null && addressList.size() > 0){
                for (AddressProfile addressProfile: addressList){
                    if(addressProfile.getType() == AddressProfile.ADDRESS_TYPE.HOSPITAL){
                        addHospitalAddress(hospitalAddressLayout, addressProfile);
                    }
                }
            }else{
//                autoAddress = true;
                addHospitalAddress(hospitalAddressLayout, null);
            }

            return view;
        }

        private void validateAndProceedNext() {

            if(!(isAddressAvailable(hospitalAddressLayout) || isAddressAvailable(privateClinicAddressLayout))){
                Utils.DISPLAY_GENERAL_DIALOG(RegisterDoctorAddressAct.this, "Invalid Address", "Please check entered address and try again.");
                return;
            }

            DoctorProfile doctorProfile = DoctorProfile.getInstance();

            doctorProfile.setAddressArrayList(null);

            if(isAddressAvailable(hospitalAddressLayout)){
                if(!areAllFieldsEntered(hospitalAddressLayout, AddressProfile.ADDRESS_TYPE.HOSPITAL))
                    return;
            }

            if(isAddressAvailable(privateClinicAddressLayout)){
                if(!areAllFieldsEntered(privateClinicAddressLayout, AddressProfile.ADDRESS_TYPE.PRIVATE))
                    return;
            }

            ArrayList<AddressProfile> temp = DoctorProfile.getInstance().getAddressArrayList();

            /*for (AddressProfile address: temp){
                System.out.println("address.getType(): " + address.getType());
                Toast.makeText(RegisterDoctorAddressAct.this, address.getType() + address.getZipCode() + "", Toast.LENGTH_SHORT).show();
            }*/


            Intent intent = new Intent(RegisterDoctorAddressAct.this, DoctorSetPasswordActivity.class);
            startActivity(intent);

            DoctorOTP_Activity.ACTIVITY_STACK.add(RegisterDoctorAddressAct.this);

        }
    }

    private void backPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterDoctorAddressAct.this)
                .setTitle("Are You Sure?")
                .setTitle("Details entered by you may be lost.")
                .setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RegisterDoctorAddressAct.this.finish();
                    }
                })
                .setNegativeButton("Cancel", null);
        builder.show();
    }
}
