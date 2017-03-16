package pharma;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.util.HttpUrl;
import com.app.util.PharmaRegBean;
import com.app.util.ServiceHandler;
import com.app.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import medrep.medrep.R;
import pharma.PharmaDashBoard;

/**
 * Created by admin on 9/26/2015.
 */
public class GenerateOTPActivity extends AppCompatActivity {
    Button next_btn;
    Button prev_btn;
    private ProgressDialog progress = null;
    private Thread thread;
//    private Handler handler = new Handler();
    Button nextButton;
    EditText mpassword,mconfirmPassword;
    GenerateOTPActivity _activity;
    String otp;
    String status;
    PharmaRegBean pharmaRegBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        _activity = this;

        pharmaRegBean=PharmaRegBean.getInstance();
      nextButton=(Button)findViewById(R.id.saveButton);
        mpassword=(EditText)findViewById(R.id.email_edittext);
        mconfirmPassword=(EditText)findViewById(R.id.pwd_edittext);

//        next_btn=(Button)findViewById(R.id.nextButton);
//        prev_btn=(Button)findViewById(R.id.backButton);

//        if(Utils.isPharmaUpdate){
//            next_btn.setText("Update");
//        }else{
//            next_btn.setText("Next");
//        }

         pharmaRegBean=PharmaRegBean.getInstance();
        System.out.println("result" + pharmaRegBean.getMdRmName());

//        next_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                System.out.println("Data...clicked");
//
//                if(mpassword.getText().toString().equalsIgnoreCase(mconfirmPassword.getText().toString())) {
//
//                    report_Data();
//
//
//                }else{
//                    mconfirmPassword.setError("Password mismatched");
//                }
//
//            }
//        });



//        prev_btn.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                onBackPressed();
//
//            }
//
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    public void report_Data() {
        progress = new ProgressDialog(GenerateOTPActivity.this);
        progress.setCancelable(false);
        progress.setMessage("Please Wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setProgress(0);
        progress.setMax(100);
        progress.show();

        thread = new Thread() {
            public void run() {
                try {
                    boolean boolValue=true;
                    int phharmaRollId=3;
                    ServiceHandler sh=new ServiceHandler();

                    JSONObject obj = new JSONObject();
                    obj.put("username",pharmaRegBean.getMdEmailId());
                    obj.put("password",mconfirmPassword.getText().toString());
                    obj.put("loginTime","null");

                    obj.put("userSecurityId","null");

                    obj.put("companyName",pharmaRegBean.getMdCompanyName());
                    obj.put("accountNonExpired",boolValue);
                    obj.put("accountNonLocked",boolValue);
                    obj.put("credentialsNonExpired",boolValue);
                    obj.put("enabled",boolValue);
                    obj.put("roleId",phharmaRollId);
                    obj.put("roleName",pharmaRegBean.getStrPassString());
                    obj.put("firstName",pharmaRegBean.getMdFirstName());
                    obj.put("middleName","");
                    obj.put("lastName",pharmaRegBean.getMdLastName());
                    obj.put("alias","null");
                    obj.put("title","Mr.");
                    obj.put("phoneNo",pharmaRegBean.getRmMobile());
                    obj.put("mobileNo",pharmaRegBean.getMdMobileNumber());
                    obj.put("emailId",pharmaRegBean.getMdEmailId());
                    obj.put("alternateEmailId","");

                    obj.put("repId","2015");
                    obj.put("companyId","123456789");
                    obj.put("coveredArea",pharmaRegBean.getMdAreaCovered());
                    obj.put("managerId","1");
                    obj.put("managerEmail",pharmaRegBean.getRmMail());
                    obj.put("coveredZone","Zone1");

                    JSONObject joj=new JSONObject();
                    joj.put("locationId", "522522");
                    joj.put("address1", pharmaRegBean.getMdaddress1().toString());
                    joj.put("address2", pharmaRegBean.getMmdAddress2().toString());
                    joj.put("city", pharmaRegBean.getMdCity());
                    joj.put("state",pharmaRegBean.getMdState());
                    joj.put("country", "India");
                    joj.put("zipcode",pharmaRegBean.getMdZipcode());
                    joj.put("type", "Pharmacy");

                    JSONArray ja=new JSONArray();
                    ja.put(joj);
                    obj.put("locations", ja);
                    System.out.println("DATA SET:" + obj.toString());

                    if(Utils.isPharmaUpdate) {
                        String url = HttpUrl.UPDATEPHARMA+ Utils.GET_ACCESS_TOKEN(GenerateOTPActivity.this);
                        status = sh.makeServiceCall(url, 2, obj.toString());
                    }else {

                        String url = HttpUrl.url_pharmaReg;
                        status = sh.makeServiceCall(url, 2, obj.toString());


                    }

                    }catch (Exception e){

                    System.out.print("Exception:"+e.getMessage());


                }

                GenerateOTPActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        System.out.println("Data...clicked"+status);

                        try{

                            JSONObject jobj=new JSONObject(status);

                            if(Utils.isPharmaUpdate){
                                if (status!=null) {

                                    myDalog("Pharma Updated successfull");


                                } else {

                                    myDalog("Registration Failed! " + jobj.getString("message"));
                                    //   Toast.makeText(getApplicationContext(),"Registration Failed!",Toast.LENGTH_LONG).show();
                                }
                            }else {

                                if (status.contains("Success")) {

                                    myDalog("Registration successfull, Please verify your Mobile Number");


                                } else {

                                    myDalog("Registration Failed! " + jobj.getString("message"));
                                    //   Toast.makeText(getApplicationContext(),"Registration Failed!",Toast.LENGTH_LONG).show();
                                }
                            }



                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                });

//                handler.post(createUI);
            }
        };

        thread.start();
    }

    final Runnable createUI = new Runnable() {
        public void run() {
            progress.dismiss();
            System.out.println("Data...clicked"+status);

            try{

                JSONObject jobj=new JSONObject(status);

                if(Utils.isPharmaUpdate){
                    if (status!=null) {

                        myDalog("Pharma Updated successfull");


                    } else {

                        myDalog("Registration Failed! " + jobj.getString("message"));
                        //   Toast.makeText(getApplicationContext(),"Registration Failed!",Toast.LENGTH_LONG).show();
                    }
                }else {

                    if (status.contains("Success")) {

                        myDalog("Registration successfull, Please verify your Mobile Number");


                    } else {

                        myDalog("Registration Failed! " + jobj.getString("message"));
                        //   Toast.makeText(getApplicationContext(),"Registration Failed!",Toast.LENGTH_LONG).show();
                    }
                }



            }catch(JSONException e){

            }
        }
    };


    public void myDalog(String msg){
        final Dialog dialog = new Dialog(GenerateOTPActivity.this);

        final String message=msg;

        dialog.setContentView(R.layout.dialog);
        // Set dialog title
        dialog.setTitle("Medrep Message");
        Button next_btn;
        Button prev_btn;

        next_btn=(Button)dialog.findViewById(R.id.nextButton);
        prev_btn=(Button)dialog.findViewById(R.id.backButton);
        next_btn.setText("Yes");
        prev_btn.setVisibility(View.GONE);
        // set values for custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.textDialog);
        text.setText(msg);
        ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
        //image.setImageResource(R.drawable.image0);

        dialog.show();




        // if decline button is clicked, close the custom dialog
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog

                if(Utils.isPharmaUpdate){
                    Intent i = new Intent(getApplicationContext(), PharmaDashBoard.class);


                    startActivity(i);
                    dialog.dismiss();
                }else if(message.contains("Registration Failed!")) {
                    dialog.dismiss();

                }else{
                        Intent i = new Intent(getApplicationContext(), ValidateOtpActivity.class);

                        startActivity(i);
                        dialog.dismiss();
                    }
                }

        });
    }
}

