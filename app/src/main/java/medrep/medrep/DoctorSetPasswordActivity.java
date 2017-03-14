package medrep.medrep;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.db.TherapeuticCategory;
import com.app.pojo.DoctorProfile;
import com.app.task.DoctorRegisterTask;
import com.app.util.HttpUrl;
import com.app.util.JSONParser;
import com.app.util.Utils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by kishore on 8/10/15.
 */
public class DoctorSetPasswordActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.doctor_registration);

        final View view = View.inflate(DoctorSetPasswordActivity.this, R.layout.fragment_otp, null);

        ((FrameLayout)findViewById(R.id.category_layout)).addView(view);

        TextView whyThis = (TextView)findViewById(R.id.whyThis);
        String textValue = "<a href=''>Why This?</a>";
        whyThis.setTextColor(Color.WHITE);
        whyThis.setText(Html.fromHtml(textValue));
        whyThis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(DoctorSetPasswordActivity.this);
                dialog.setContentView(R.layout.why_this_layout);
                dialog.show();
            }
        });

        Spinner registrationYear = (Spinner)findViewById(R.id.registrationYear);
        Spinner stateMedicalCouncil = (Spinner)findViewById(R.id.stateMedicalCouncil);
        ArrayList<String> yearList = getPreviousYear();
        yearList.add(0, "Year of registration");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(DoctorSetPasswordActivity.this, R.layout.location_spinner_text, yearList);
        dataAdapter.setDropDownViewResource(R.layout.dropdown);
        registrationYear.setAdapter(dataAdapter);
        stateMedicalCouncil.setAdapter(dataAdapter);

        Button submitButton = (Button) view.findViewById(R.id.saveButton);

        //Register doctor here
        final DoctorProfile doctorProfile = DoctorProfile.getInstance();

        AsyncTask<Void, Void, ArrayList<TherapeuticCategory>> getTherapeuticAsync = new AsyncTask<Void, Void, ArrayList<TherapeuticCategory>>() {

            ProgressDialog pd;

            @Override
            protected ArrayList<TherapeuticCategory> doInBackground(Void... params) {
                return getTherapeuticAreaDetails();
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(DoctorSetPasswordActivity.this);
                pd.setTitle("Getting Therapeutic Areas");
                pd.setMessage("Please wait, while we retrieve Therapeutic areas.");
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            protected void onPostExecute(final ArrayList<TherapeuticCategory> therapeuticCategories) {
                super.onPostExecute(therapeuticCategories);

                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }

                Spinner therapeuticSpinner = (Spinner) findViewById(R.id.therapeutic_spinner);
                therapeuticSpinner.setPrompt("Select Therapatic Area");

                // Spinner Drop down elements

                List<String> categories = new ArrayList<String>();


                categories.add("Select Therapeutic Area");
                for (TherapeuticCategory therapeuticCategory : therapeuticCategories) {
                    categories.add(therapeuticCategory.getTherapeuticName());
                }

                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(DoctorSetPasswordActivity.this, R.layout.location_spinner_text, categories);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(R.layout.dropdown);

                // attaching data adapter to spinner
                therapeuticSpinner.setAdapter(dataAdapter);

                therapeuticSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(position == 0){
                            doctorProfile.setTherapeuticID(-1);
                        }else{

                           /* System.out.println(therapeuticCategories.get(position - 1).getTherapeuticId());
                            System.out.println(therapeuticCategories.get(position - 1).getTherapeuticName());

                            Toast.makeText(DoctorSetPasswordActivity.this,
                                    therapeuticCategories.get(position - 1).getTherapeuticId() +
                                            therapeuticCategories.get(position - 1).getTherapeuticName(),
                                    Toast.LENGTH_LONG).show();*/

                            doctorProfile.setTherapeuticID(therapeuticCategories.get(position - 1).getTherapeuticId());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        doctorProfile.setTherapeuticID(-1);
                    }
                });
            }

            private ArrayList<TherapeuticCategory> getTherapeuticAreaDetails() {
                String url = HttpUrl.THERAPEUTIC_AREA_DETAILS_URL;

                System.out.println("url: " + url);

                ArrayList<TherapeuticCategory> therapeuticCategories = new ArrayList<>();

                JSONParser parser = new JSONParser();
                JSONArray jsonArray = parser.getJSON_Response(url);
                parser.jsonParser(jsonArray, TherapeuticCategory.class, therapeuticCategories);
                return therapeuticCategories;
            }
        };

        if(Utils.isNetworkAvailable(this)){
            getTherapeuticAsync.execute();
        }

        final EditText desiredPwdET = (EditText) view.findViewById(R.id.email_edittext);
        final EditText confirmPwdET = (EditText) view.findViewById(R.id.pwd_edittext);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Utils.isNetworkAvailable(DoctorSetPasswordActivity.this)){
                    return;
                }

                if(doctorProfile.getTherapeuticID() == -1){
//                    Toast.makeText(DoctorSetPasswordActivity.this, "Please select Therapeutic area.", Toast.LENGTH_SHORT).show();
                    Utils.DISPLAY_GENERAL_DIALOG(DoctorSetPasswordActivity.this, "No Therapeutic Area", "Please select Therapeutic area.");
                    return;
                }

                String desiredPassword = desiredPwdET.getText().toString();
                String confirmPassword = confirmPwdET.getText().toString();


                if(desiredPassword.isEmpty() || desiredPassword.trim().length() ==0){
                  //  desiredPwdET.setError("Password cannot be empty.");

                    Toast.makeText(DoctorSetPasswordActivity.this, " DesiredPassword cannot be empty.", Toast.LENGTH_SHORT).show();
                }else if(confirmPassword.isEmpty() || confirmPassword.trim().length() ==0){
                    //confirmPwdET.setError("Please confirm password.");
                    Toast.makeText(DoctorSetPasswordActivity.this, "Please confirm password.", Toast.LENGTH_SHORT).show();
                }else if(desiredPassword.trim().length() > 0 && confirmPassword.trim().length() > 0){
                    if(!desiredPassword.equals(confirmPassword)){
                       // desiredPwdET.setError("Password do not match.");
                        Toast.makeText(DoctorSetPasswordActivity.this, "Password do not match.", Toast.LENGTH_SHORT).show();
                        //confirmPwdET.setError("Password do not match.");
                    }else{
                      //  desiredPwdET.setError(null);
                        //confirmPwdET.setError(null);
                    //    System.out.println("Passwords Valid");


//                        new DoctorRegisterTask(DoctorSetPasswordActivity.this, doctorProfile).
//                              execute("http://183.82.106.234:8080/MedRepApplication/preapi/registration/signupDoctor");
                        doctorProfile.setPassword(desiredPassword);

                        if(doctorProfile.getTherapeuticID() <= 0){

                            IntentFilter filter = new IntentFilter();
                            filter.addAction("android.provider.Telephony.SMS_RECEIVED");

                            DoctorSetPasswordActivity.this.registerReceiver(incomingSMS_Receiver, filter);

                            Utils.DISPLAY_GENERAL_DIALOG(DoctorSetPasswordActivity.this, "Specify Therapeutic Area", "Please select one therapeutic area.");

                        }else{
                            new DoctorRegisterTask(DoctorSetPasswordActivity.this, doctorProfile, false).
                                    execute(HttpUrl.BASEURL + HttpUrl.DOCTOR_SIGN_UP);
                        }


                        //On successful registration request for OTP
                        //On successful retrieval of OTP display OTP activity and clear activity stack


                        /*Intent intent = new Intent(DoctorSetPasswordActivity.this, DoctorOTP_Activity.class);
                        startActivity(intent);*/



//                        listener.submit(desiredPwd.getText().toString(), cnfirmPwd.getText().toString());
                    }
                }else{
                    desiredPwdET.setError("Invalid password.");
                    confirmPwdET.setError("Invalid password.");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try{
            unregisterReceiver(incomingSMS_Receiver);
        }catch (Exception e){

        }
    }

    private static ArrayList<String> getPreviousYear() {
        //Calendar prevYear = Calendar.getInstance();
//        prevYear.add(Calendar.YEAR, -1);
//        return prevYear.get(Calendar.YEAR);
        int currYear = Calendar.getInstance().get(Calendar.YEAR);
        ArrayList<String> yearList = new ArrayList();
        for(int i = 0; i < 75; i++) {
            yearList.add(currYear + "");
            currYear--;
        }
        return yearList;
    }


    public static BroadcastReceiver incomingSMS_Receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final Bundle bundle = intent.getExtras();
            try {
                if (bundle != null)
                {
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");
                    for (int i = 0; i < pdusObj .length; i++)
                    {
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[])                                                                                                    pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                        String senderNum = phoneNumber ;
                        String message = currentMessage .getDisplayMessageBody();
                        try
                        {
                            System.out.println("message: " + message);
                            if (senderNum .equals("MD-MedRep"))
                            {
                                String temp = "Password is ";

                                String otp = message.substring(message.indexOf(temp) + temp.length(), message.indexOf("."));
                                DoctorOTP_Activity.OTP = otp;
                                DoctorOTP_Activity.SET_OTP(otp);

                                /*Otp Sms = new Otp();
                                Sms.recivedSms(message );*/
                            }
                        }
                        catch(Exception e){}

                    }
                }

            } catch (Exception e)
            {

            }
        }
    };

    public void successResult(final String message) {
        if(message.equalsIgnoreCase("success")){
            //Registration is successful. Server will automatically sends the OTP
            AlertDialog.Builder builder = new AlertDialog.Builder(DoctorSetPasswordActivity.this)
                    .setTitle("Registration Successful")
                    .setMessage("Please confirm your mobile number through OTP.")
                    .setCancelable(false)
                    .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            DoctorProfile doctorProfile = DoctorProfile.getInstance();

                            DoctorOTP_Activity.ACTIVITY_STACK.add(DoctorSetPasswordActivity.this);

                            Intent intent = new Intent(DoctorSetPasswordActivity.this, DoctorOTP_Activity.class);
                            intent.putExtra(OTPActivity.MOBILE_NUMBER_KEY, doctorProfile.getMobileNumber());
                            intent.putExtra(WelcomeActivity.USERNAME_KEY, doctorProfile.getFirstName());
                            intent.putExtra(WelcomeActivity.EMAIL_KEY, doctorProfile.getEmail());
            /*intent.putExtra("username",firstname);*/
                            startActivity(intent);
                        }
                    });
            builder.show();

        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(DoctorSetPasswordActivity.this)
                    .setTitle("Error!")
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(message.contains("Email") || message.contains("Mobile")){
                                DoctorSetPasswordActivity.this.finish();
                                for(int i = 1; i <  DoctorOTP_Activity.ACTIVITY_STACK.size(); i++){
                                    DoctorOTP_Activity.ACTIVITY_STACK.get(i).finish();
                                }

                                Activity temp = DoctorOTP_Activity.ACTIVITY_STACK.get(0);

                                DoctorOTP_Activity.ACTIVITY_STACK.clear();

                                DoctorOTP_Activity.ACTIVITY_STACK.add(temp);
                            }
                        }
                    });
            builder.show();



        }
        /*DoctorProfile doctorProfile = DoctorProfile.getInstance();
        new OTPTask(DoctorSetPasswordActivity.this, doctorProfile).execute(HttpUrl.BASEURL + HttpUrl.OTP + doctorProfile.getMobileNumber());*/
    }

    private class TherapeuticSpinnerAdapter implements SpinnerAdapter {

        ArrayList<TherapeuticCategory> therapeuticCategories;

        public TherapeuticSpinnerAdapter(ArrayList<TherapeuticCategory> therapeuticCategories){
            this.therapeuticCategories = therapeuticCategories;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return null;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getCount() {
            return therapeuticCategories.size();
        }

        @Override
        public TherapeuticCategory getItem(int position) {
            return therapeuticCategories.get(position);
        }

        @Override
        public long getItemId(int position) {
            return therapeuticCategories.get(position).getTherapeuticId();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }
}