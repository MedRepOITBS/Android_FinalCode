package medrep.medrep;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pojo.DoctorProfile;
import com.app.util.GlobalVariables;

import org.w3c.dom.Text;

/**
 * Created by kishore on 7/10/15.
 */
public class RegisterDoctorActivity extends Activity{

    private EditText altMobileEdit;
    private TextView addMobileTextView;
    private TextView addEmailTextView;
    private EditText altEmailID_Edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.doctor_registration);

        View view = View.inflate(RegisterDoctorActivity.this, R.layout.doctor_registration_one, null);

        ((FrameLayout)findViewById(R.id.category_layout)).addView(view);

        final EditText doctorIdEditText = (EditText) view.findViewById(R.id.edittext_doctorid);
        final EditText firstNameEdit = (EditText) view.findViewById(R.id.edittext_firstname);
        final EditText lastNameEdit = (EditText) view.findViewById(R.id.edittext_lastname);
        final EditText mobileEdit = (EditText) view.findViewById(R.id.edittext_mobile);
        final EditText emailEdit = (EditText) view.findViewById(R.id.edittext_email);
        TextView whythis = (TextView)view.findViewById(R.id.why_this);
        whythis.setPaintFlags(whythis.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        whythis.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterDoctorActivity.this);
                LayoutInflater inflater = RegisterDoctorActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_alert_view, null);
                AlertDialog.Builder customLayout = builder.setView(dialogView);
                TextView dialogCotent = (TextView)dialogView.findViewById(R.id.dialog_cotent);
                dialogCotent.setText("We request all users to mention accurate information within this field as our Background Verification team throughly check the valididty of this data. Once verified, the user can receive information on this application.");
                builder.show();
            }
        });

        DoctorProfile doctorProfile = DoctorProfile.getInstance();

        doctorIdEditText.setText(doctorProfile.getDoctorId());
        firstNameEdit.setText(doctorProfile.getFirstName());
        lastNameEdit.setText(doctorProfile.getLastName());
        mobileEdit.setText(doctorProfile.getMobileNumber());
        emailEdit.setText(doctorProfile.getEmail());


        final LinearLayout addSecMobileLayout = (LinearLayout) view.findViewById(R.id.add_mobile_layout);

        addMobileTextView = (TextView) view.findViewById(R.id.addMobiletextView);
//        addMobileTextView.setTypeface(typeface);
        addMobileTextView.setText(getString(R.string.add_mobile));

        addEmailTextView = (TextView) view.findViewById(R.id.altEmailTextView);
//        addEmailTextView.setTypeface(typeface);
        addEmailTextView.setText(getString(R.string.alt_email));

        String altMobileNumber = doctorProfile.getAltMobileNumber();
        if(altMobileNumber != null && altMobileNumber.trim().length() != 0){
            addSecondaryMobileEditText(addSecMobileLayout, altMobileNumber);
        }

        addSecMobileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSecondaryMobileEditText(addSecMobileLayout, "");
            }
        });

        final LinearLayout addAltEmailLayout = (LinearLayout) view.findViewById(R.id.add_email_layout);

        final String altEmail = doctorProfile.getAltEmail();
        if(altEmail != null && altEmail.trim().length() != 0){
            addAlternativeEmailEditText(addAltEmailLayout, altEmail);
        }
        addAltEmailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlternativeEmailEditText(addAltEmailLayout, "");
            }
        });

        Button nextButton = (Button) view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setValidation(doctorIdEditText, firstNameEdit, lastNameEdit, mobileEdit, emailEdit)){
                    DoctorProfile doctorProfile = DoctorProfile.getInstance();
                    doctorProfile.setDoctorId(doctorIdEditText.getText().toString());
                    doctorProfile.setFirstName(firstNameEdit.getText().toString());
                    doctorProfile.setLastName(lastNameEdit.getText().toString());
                    doctorProfile.setMobileNumber(mobileEdit.getText().toString());
                    doctorProfile.setEmail(emailEdit.getText().toString());

                    if(altMobileEdit != null){
                        String altMobile = (altMobileEdit.getText() + "").trim();

                        if(altMobile.length() == 0 || altMobile.isEmpty()){
                           // altMobileEdit.setError("Alternate mobile number cannot be empty.");
                            Toast.makeText(RegisterDoctorActivity.this, "Alternate mobile number cannot be empty.", Toast.LENGTH_SHORT).show();

                            return;
                      //  }else if(!GlobalVariables.validate(altMobile, GlobalVariables.MOBILE_NUMBER_PATTERN)){
                       //     altMobileEdit.setError("Please enter 10 digits mobile number.");

                        }else  if(!GlobalVariables.validate(altMobileEdit.getText().toString(), GlobalVariables.MOBILE_NUMBER_PATTERN)){
                                //   mobileEdit.setError("Please enter 10 digits mobile number.");
                                Toast.makeText(RegisterDoctorActivity.this, "Please enter 10 digits Alternate Mobile number.", Toast.LENGTH_SHORT).show();

                            return;
                        }
                        doctorProfile.setAltMobileNumber(altMobile);

                        /*if(altMobile != null && altMobile.length() > 0 && GlobalVariables.validate(altMobile, GlobalVariables.MOBILE_NUMBER_PATTERN)){
                            doctorProfile.setAltMobileNumber(altMobile);
                        }*/
                    }

                    if(altEmailID_Edit != null){
                        String altEmailID = (altEmailID_Edit.getText() + "").trim();

                        if(altEmailID.length() == 0 || altEmailID.isEmpty()){
                           // altEmailID_Edit.setError("Alternate email address cannot be empty.");
                            Toast.makeText(RegisterDoctorActivity.this, "Alternate email address cannot be empty.", Toast.LENGTH_SHORT).show();


                            return;
                        }else if(!GlobalVariables.validate(altEmailID, GlobalVariables.EMAIL_PATTERN)){
                        //    altEmailID_Edit.setError("Please enter valid email address.");
                            Toast.makeText(RegisterDoctorActivity.this, "Please enter valid  Alternate email address.", Toast.LENGTH_SHORT).show();

                            return;
                        }

                        doctorProfile.setAltMobileNumber(altEmailID);

                        /*if(altEmailID != null && altEmailID.length() > 0 && GlobalVariables.validate(altEmailID, GlobalVariables.EMAIL_PATTERN)){
                            doctorProfile.setAltMobileNumber(altEmailID);
                        }*/
                    }


                    Intent intent = new Intent(RegisterDoctorActivity.this, RegisterDoctorAddressAct.class);
                    startActivity(intent);

                    DoctorOTP_Activity.ACTIVITY_STACK.clear();
                    DoctorOTP_Activity.ACTIVITY_STACK.add(RegisterDoctorActivity.this);

//                    finish();
                }
            }
        });


        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPressed();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DoctorProfile.clearProfile();
    }

    @Override
    public void onBackPressed() {
        backPressed();
    }

    private void backPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterDoctorActivity.this)
                .setTitle("Are You Sure?")
                .setTitle("Details entered by you may be lost.")
                .setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RegisterDoctorActivity.this.finish();
                        DoctorProfile.clearProfile();
                    }
                })
                .setNegativeButton("Cancel", null);
        builder.show();
    }

    private void addSecondaryMobileEditText(LinearLayout addMobileLayout, String altMobileNumber){

        if(altMobileEdit == null){
            altMobileEdit = addEditText(altMobileNumber);
//            altMobileNumber.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            altMobileEdit.setHint("SECONDARY MOBILE NUMBER");
            altMobileEdit.setInputType(InputType.TYPE_CLASS_PHONE);
            altMobileEdit.setMaxEms(10);
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(10);
            altMobileEdit.setFilters(FilterArray);

            addMobileLayout.addView(altMobileEdit);
            addMobileTextView.setText("Remove");
            addMobileTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.minus, 0, 0, 0);
            addMobileTextView.setCompoundDrawablePadding(5);

            //Change image here
        }else{
            //Change image here
            addMobileLayout.removeView(altMobileEdit);
            addMobileTextView.setText("ADD MOBILE NUMBER");
            addMobileTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.add_icon, 0, 0, 0);
            addMobileTextView.setCompoundDrawablePadding(5);
            altMobileEdit = null;
        }

//        addMobileTextView.setVisibility(View.GONE);
    }

    private EditText addEditText(String text){
        EditText addEditText = new EditText(RegisterDoctorActivity.this);
        addEditText.setLayoutParams(getLayoutParams());
        addEditText.setPadding(15, 0, 15, 0);
        addEditText.setBackgroundColor(getResources().getColor(R.color.home_button_text));
        addEditText.setText(text);
        addEditText.setTextSize(15);
        return addEditText;
    }

    private LinearLayout.LayoutParams getLayoutParams(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 95);
        params.setMargins(0, 10, 0, 0);
        return  params;
    }

    private void addAlternativeEmailEditText(LinearLayout addEmailLayout, String altEmail){
        if(altEmailID_Edit == null){

      //      final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,48); // Width , height



            altEmailID_Edit = addEditText(altEmail);
         //   altEmailID_Edit.setLayoutParams(lparams);

         //   altEmailID_Edit.set
            altEmailID_Edit.setHint("ALTERNATE EMAIL ADDRESS");
            altEmailID_Edit.setTextSize(15);
            addEmailLayout.addView(altEmailID_Edit);
            addEmailTextView.setText("Remove");
            addEmailTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.minus, 0, 0, 0);
            addEmailTextView.setCompoundDrawablePadding(5);

            //Change image here
        }else{
            //Change image here



            addEmailLayout.removeView(altEmailID_Edit);
            addEmailTextView.setText("ALTERNATE EMAIL ADDRESS");
            addEmailTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.add_icon, 0, 0, 0);
            addEmailTextView.setCompoundDrawablePadding(5);
            altEmailID_Edit = null;
        }

//        addEmailTextView.setVisibility(View.GONE);
    }

    private boolean setValidation(EditText doctorIdEditText, EditText firstNameEdit, EditText lastNameEdit, EditText mobileEdit, EditText emailEdit){
        if(doctorIdEditText.getText().toString().trim().length() == 0 || doctorIdEditText.getText().toString().isEmpty()){
          //  doctorIdEditText.setError("Doctor registration id cannot be empty.");
            Toast.makeText(RegisterDoctorActivity.this, "Doctor registration id cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(firstNameEdit.getText().toString().trim().length() == 0 || firstNameEdit.getText().toString().isEmpty()){
            //firstNameEdit.setError("First name cannot be empty.");
            Toast.makeText(RegisterDoctorActivity.this, "First name cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }/*else if(!GlobalVariables.validate(firstNameEdit.getText().toString(), GlobalVariables.USERNAME_PATTERN)){
            Toast.makeText(getActivity(), "please enter the valid username", Toast.LENGTH_SHORT).show();
            return false;
        }*/else if(lastNameEdit.getText().toString().length() ==0 || lastNameEdit.getText().toString().isEmpty()){
            //lastNameEdit.setError("Last name cannot be empty.");
            Toast.makeText(RegisterDoctorActivity.this, "Last name cannot be empty.", Toast.LENGTH_SHORT).show();
//            Toast.makeText(getActivity(), "please enter the valid last name", Toast.LENGTH_SHORT).show();
            return false;
        }/*else if(!GlobalVariables.validate(lastNameEdit.getText().toString(), GlobalVariables.USERNAME_PATTERN)){
            Toast.makeText(getActivity(), "please enter the valid lastname", Toast.LENGTH_SHORT).show();
            return false;
        }*/else if(mobileEdit.getText().toString().length() == 0 || mobileEdit.getText().toString().isEmpty()){
            //mobileEdit.setError("Primary mobile number cannot be empty.");
            Toast.makeText(RegisterDoctorActivity.this, "Primary mobile number cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!GlobalVariables.validate(mobileEdit.getText().toString(), GlobalVariables.MOBILE_NUMBER_PATTERN)){
         //   mobileEdit.setError("Please enter 10 digits mobile number.");
            Toast.makeText(RegisterDoctorActivity.this, "Please enter 10 digits mobile number.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(emailEdit.getText().toString().length() == 0 || emailEdit.getText().toString().isEmpty()){
        //    emailEdit.setError("Email address cannot be empty.");
            Toast.makeText(RegisterDoctorActivity.this, "Email address cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!GlobalVariables.validate(emailEdit.getText().toString(), GlobalVariables.EMAIL_PATTERN)){
          //  emailEdit.setError("Please enter valid email address.");
            Toast.makeText(RegisterDoctorActivity.this, "Please enter valid email address.", Toast.LENGTH_SHORT).show();
            return false;
        }/*else{

            lastNameEdit.setError(null);
            firstNameEdit.setError(null);
            mobileEdit.setError(null);
            doctorIdEditText.setError(null);
            emailEdit.setError(null);
        }
*/
        /*else if(alternativeMobileEdit != null){
            if(!GlobalVariables.validate(alternativeMobileEdit.getText().toString(), GlobalVariables.EMAIL_PATTERN)){
                Toast.makeText(getActivity(), "please enter the valid alternative email", Toast.LENGTH_SHORT).show();
                return false;
            }
        }*/

        return  true;
    }
}
