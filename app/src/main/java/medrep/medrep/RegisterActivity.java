package medrep.medrep;


import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.app.fragments.OtpFragment;
import com.app.fragments.RegisterClinicFragment;
import com.app.fragments.RegisterAddressFragment;
import com.app.fragments.RegisterOneFragment;
import com.app.fragments.RegisterHospitalFragment;
import com.app.pojo.Address;
import com.app.pojo.DoctorProfile;
import com.app.pojo.MyProfile;
import com.app.pojo.Register;
import com.app.task.DoctorRegisterTask;
import com.app.task.OTPTask;
import com.app.util.HttpUrl;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity implements RegisterOneFragment.NextScreenListener,
        RegisterAddressFragment.ClinicListener, RegisterHospitalFragment.NextPublicClinicListener, RegisterClinicFragment.NextPrivateClinicListener,
        OtpFragment.SubmitListener{


    private String firstname;
    private String lastname;
    private String email;
    private String mobile;
    private String addressOne;
    private String addressTwo;
    private String zipCode;
    private String state;
    private String city;
    private String desiredPwd;
    private String cnfirmPwd;
    private String selectedCategory;
    private boolean myprofileFlag = false;
    private boolean addressFlag = false;
    private ArrayList<Address> addressArrayList;
    private DoctorProfile doctorProfile;
    private String accesstoken;

    FragmentManager fragmentManager;
    //    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_registration_main);

        doctorProfile = DoctorProfile.getInstance();

        getBundle();

        if(!addressFlag){
            Fragment fragment = new RegisterOneFragment();
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.category_layout, fragment, "address");
            fragmentTransaction.commit();
        }else{
            doctorProfile.setSelectedCat(selectedCategory);

            Fragment fragment = new RegisterAddressFragment();
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
            fragmentTransaction.replace(R.id.category_layout, fragment, "address");

            fragmentTransaction.commit();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DoctorProfile.clearProfile();
    }

    private void getBundle(){
        Bundle bundle= getIntent().getExtras();

        if(bundle != null) {
            selectedCategory = bundle.getString("selectedCategory");
            myprofileFlag = bundle.getBoolean("myprofile");
            addressFlag = bundle.getBoolean("address");
            accesstoken = bundle.getString("accesstoken");
        }
    }

    @Override
    public void nextScreen(/*String firstname, String lastname, String email, String mobile*/) {


        doctorProfile.setSelectedCat(selectedCategory);
        Log.d("TAG", "My REGISTER ACTIVITY IS " + doctorProfile.getFirstName());
        Log.d("TAG", "My REGISTER ACTIVITY IS " + doctorProfile.getLastName());
        Log.d("TAG", "My REGISTER ACTIVITY IS " + doctorProfile.getMobileNumber());
        Log.d("TAG", "My REGISTER ACTIVITY IS " + doctorProfile.getEmail());

        Fragment fragment = new RegisterAddressFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
        fragmentTransaction.replace(R.id.category_layout, fragment, "address");

        fragmentTransaction.commit();
    }

    @Override
    public void privateClinic(int layoutId) {
        Log.d("TAG", "MY PRIVATE CLINIC METHOD ");
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(fragmentManager.findFragmentByTag("private") == null) {
            Log.d("TAG","MY PRIVATE CLINIC IF METHOD ");
            RegisterClinicFragment fragment = RegisterClinicFragment.newInstance();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(layoutId, fragment, "private");
            fragmentTransaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
            fragmentTransaction.commit();

        }else{
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Log.d("TAG", "MY PRIVATE CLINIC ELSE METHOD " + ((RegisterClinicFragment) fragmentManager.findFragmentByTag("private")).isVisible());
            fragmentTransaction.show((RegisterClinicFragment) fragmentManager.findFragmentByTag("private"));
            fragmentTransaction.commit();
        }

    }

    @Override
    public void publicClinic(int layoutId) {
        Log.d("TAG","MY PUBLIC CLINIC METHOD ");
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(fragmentManager.findFragmentByTag("public") == null){
            Log.d("TAG", "MY PUBLIC CLINIC METHOD IF");
            RegisterHospitalFragment fragment = new RegisterHospitalFragment();
            fragmentTransaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
            fragmentTransaction.replace(layoutId, fragment, "public");
        }else{
            Log.d("TAG", "MY PUBLIC CLINIC METHOD ELSE");
            fragmentTransaction.attach((RegisterHospitalFragment) fragmentManager.findFragmentByTag("public"));
        }
        fragmentTransaction.commit();

    }

    @Override
    public void nextPublicClinic() {
        if(fragmentManager.findFragmentByTag("otp") == null){
            OtpFragment fragment = new OtpFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
            fragmentTransaction.replace(R.id.category_layout, fragment, "otp");
            fragmentTransaction.commit();
        }
    }


    // Otp screen
    @Override
    public void nextPrivateClinic() {
        doctorProfile.getAddressArrayList();

        OtpFragment fragment = new OtpFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right);
        fragmentTransaction.replace(R.id.category_layout, fragment, "private");

        fragmentTransaction.commit();
    }

    Register register;

    public void submit() {
        if (myprofileFlag) {
            Log.d("TAG", "my access token is " + accesstoken);
            //update doctor profile

            doctorProfile = DoctorProfile.getInstance();

            MyProfile myProfile = MyProfile.getInstance();

            myProfile.setFirstname(doctorProfile.getFirstName());
            myProfile.setLastname(doctorProfile.getLastName());
            myProfile.setMobileNumber(doctorProfile.getMobileNumber());
            myProfile.setAltMobileNumber(doctorProfile.getAltMobileNumber());
            myProfile.setEmail(doctorProfile.getEmail());
            myProfile.setAltEmail(doctorProfile.getAltEmail());
            myProfile.setAddressArrayList(doctorProfile.getAddressArrayList());
            myProfile.setSelectedCat(doctorProfile.getSelectedCat());
            myProfile.setDoctorId(doctorProfile.getDoctorId());
            myProfile.setPassword(doctorProfile.getPassword());


            String firstName = (myProfile.getFirstname() == null ? "" : myProfile.getFirstname());
            String lastName = (myProfile.getLastname() == null ? "" : myProfile.getLastname());

            String profileName = firstName + " " + lastName;

            System.out.println("profileName: " + profileName);

            if(profileName.trim().equals("")){
                profileName = "User";
            }

            SharedPreferences signPref = getSharedPreferences(SplashScreen.SIGNIN_PREF, MODE_PRIVATE);
            SharedPreferences.Editor editor = signPref.edit();
            editor.putString(SplashScreen.PROFILE_NAME_KEY, profileName);
            editor.commit();

            new DoctorRegisterTask(RegisterActivity.this, doctorProfile, true).execute(HttpUrl.BASEURL + HttpUrl.UPDATE_DOCTOR_PROFILE);
//            finish();
        }
    }



    @Override
    public void submit(String desiredPwd, String cnfirmPwd) {
        this.desiredPwd = desiredPwd;
        this.cnfirmPwd = cnfirmPwd;

        doctorProfile.setPassword(desiredPwd);
       /* register = new Register();
        register.setDoctorId("");
        register.setFirstname(firstname);
        register.setLastname(lastname);
        register.setMobileNumber(mobile);
        register.setEmail(email);
        register.setSelectedCat(selectedCategory);
        register.setPassword(desiredPwd);
        register.setAddressArrayList(addressArrayList);*/
//        register.set



        if(myprofileFlag){
            Log.d("TAG", "my access token is " + accesstoken);
            //update doctor profile
            new DoctorRegisterTask(RegisterActivity.this, doctorProfile, true).execute(HttpUrl.BASEURL + HttpUrl.UPDATE_DOCTOR_PROFILE);

        }else
            //
            new DoctorRegisterTask(RegisterActivity.this, doctorProfile, true).execute(HttpUrl.BASEURL + HttpUrl.DOCTOR_SIGN_UP);
    }

    public void successResult(){
        if(!myprofileFlag) {
            new OTPTask(RegisterActivity.this, doctorProfile).execute(HttpUrl.BASEURL + HttpUrl.OTP + doctorProfile.getMobileNumber());
        }else{
            Toast.makeText(RegisterActivity.this, "Success. Your profile has been updated." ,Toast.LENGTH_SHORT).show();

            finish();
        }
    }

    public void failure(){
        Toast.makeText(RegisterActivity.this, "failure" ,Toast.LENGTH_SHORT).show();
    }

    public void duplicateData(String message){
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();;
    }
    //    http://www.mysamplecode.com/2011/10/android-programmatically-generate.html
}
