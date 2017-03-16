package medrep.medrep;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.util.GlobalVariables;
import com.app.util.Utils;

import pharma.PhramaRegistrationTwo;


public class RegisterCategoryActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener{


    private FrameLayout registerFrame;

    private TextView registerCategoryTextView;

    private Button nextButton;
    private Typeface typeface;
    Button next_btn;
    Button prev_btn;

    private RadioButton doctorRadioButton;
    private RadioButton companyRadioButton;
    private RadioButton pharmacyRadioButton;
    private RadioButton patientRadioButton;
    private RadioGroup categoryGroup;
    private String selectedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_registration_categery);

        setView();
    }

    private  void setView(){
        typeface = GlobalVariables.getTypeface(RegisterCategoryActivity.this);

        next_btn=(Button)findViewById(R.id.nextButton);
        prev_btn=(Button)findViewById(R.id.backButton);

        registerCategoryTextView = (TextView) this.findViewById(R.id.register_textView);
        registerCategoryTextView.setTypeface(typeface);
        registerCategoryTextView.setText(getString(R.string.register_category));

        categoryGroup = (RadioGroup) this.findViewById(R.id.category_group);
        categoryGroup.setOnCheckedChangeListener(this);

        doctorRadioButton = (RadioButton) this.findViewById(R.id.doctorRadioButton);
        doctorRadioButton.setTypeface(typeface);
        doctorRadioButton.setText(getString(R.string.doctor));
     //   doctorRadioButton.setChecked(true);
        //selectedText = "Doctor";
        selectedText = "";

        companyRadioButton = (RadioButton) this.findViewById(R.id.companyRadioButton);
        companyRadioButton.setTypeface(typeface);
        companyRadioButton.setText(getString(R.string.company));
//        companyRadioButton.setEnabled(false);


        pharmacyRadioButton = (RadioButton) this.findViewById(R.id.pharmacyRadioButton);
        pharmacyRadioButton.setTypeface(typeface);
        pharmacyRadioButton.setText("Pharmacy");
//        pharmacyRadioButton.setEnabled(false);

        patientRadioButton = (RadioButton) this.findViewById(R.id.patientRadioButton);
        patientRadioButton.setTypeface(typeface);
        patientRadioButton.setText(getString(R.string.patient));
//        patientRadioButton.setEnabled(false);

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedText.equalsIgnoreCase("Pharmacy")){
                    Intent intent = new Intent(getApplicationContext(), PhramaRegistrationTwo.class);
                    intent.putExtra("selectedCategory", selectedText);
                    startActivity(intent);
                }/*else if(selectedText.equalsIgnoreCase("Company")){

                    Utils.DISPLAY_GENERAL_DIALOG(RegisterCategoryActivity.this, "Message", "Please send a Contact request to info@erfolglifesciences.com (or) info@medrep.in to gain access to the MedRep App");


                }*/else if(selectedText.equalsIgnoreCase(getString(R.string.doctor))){

                    finish();

                    Intent intent = new Intent(getApplicationContext(), RegisterDoctorActivity.class);
                    intent.putExtra("selectedCategory", selectedText);
                    startActivity(intent);
                }else{
                   // Toast.makeText(getApplicationContext(),"Please select your Role",Toast.LENGTH_LONG).show();

                    Utils.DISPLAY_GENERAL_DIALOG(RegisterCategoryActivity.this, "Message", "Please select your Role");
                }
            }
        });

        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getApplicationContext(),HomeScreenActivity.class);
                startActivity(i);
            }
        });


    }


    @Override
    public void onClick(View v) {


    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton rb=(RadioButton)findViewById(checkedId);

        switch (checkedId){
            case R.id.doctorRadioButton:
                selectedText = getString(R.string.doctor);
                break;
            case R.id.pharmacyRadioButton:
//                selectedText = "Pharmacy";
                selectedText = "Pharmacy";
                //pharmacyRadioButton.setChecked(false);
                //Utils.DISPLAY_GENERAL_DIALOG(RegisterCategoryActivity.this, "Coming Soon", "Functionality is under development.");
                break;
            case R.id.companyRadioButton:
              selectedText = getString(R.string.company);
                selectedText = "";
                companyRadioButton.setChecked(false);
                Utils.DISPLAY_GENERAL_DIALOG(RegisterCategoryActivity.this, "Message", "Please send a Contact request to info@erfolglifesciences.com (or) info@medrep.in to gain access to the MedRep App");

                break;
            case R.id.patientRadioButton:
//                selectedText = getString(R.string.patient);
                selectedText = "";
                patientRadioButton.setChecked(false);
                Utils.DISPLAY_GENERAL_DIALOG(RegisterCategoryActivity.this, "Coming Soon", "Functionality is under development.");
                break;
        }
        Log.d("TAG", "selected text"+selectedText);
    }
}
