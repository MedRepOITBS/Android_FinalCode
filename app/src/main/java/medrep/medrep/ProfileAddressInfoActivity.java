package medrep.medrep;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.interfaces.GetResponse;

import android.location.Address;

import com.app.pojo.SignIn;
import com.app.task.ProfilePostData;
import com.app.util.HttpUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by RKatakam on 9/18/2016.
 */
public class ProfileAddressInfoActivity extends AppCompatActivity implements GetResponse {

    private Address address;
    private EditText etAdrs1, etAdrs2, etCity, etState, etZip;
    private ImageView pickLocation,mHospital,mClinic;
    private String mObjId = null;
    private boolean isHospital = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_address_info);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        TextView title = (TextView) findViewById(R.id.title);
        TextView tvDone = (TextView) findViewById(R.id.tv_done);
        mHospital= (ImageView) findViewById(R.id.hospital);
        mClinic= (ImageView) findViewById(R.id.clinic);
        mHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHospital = true;
                findViewById(R.id.first_tab).setVisibility(View.VISIBLE);
                findViewById(R.id.second_tab).setVisibility(View.INVISIBLE);
            }
        });
        mClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHospital = false;
                findViewById(R.id.first_tab).setVisibility(View.VISIBLE);
                findViewById(R.id.second_tab).setVisibility(View.INVISIBLE);
            }
        });
        title.setText("Edit Location");
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDataToServer();
            }
        });

        pickLocation = (ImageView) findViewById(R.id.pick_location);
        pickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddressFromGps();
            }
        });

        Button onBackClick = (Button) findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etAdrs1 = (EditText) findViewById(R.id.et_adrs_1);
        etAdrs2 = (EditText) findViewById(R.id.et_adrs_2);
        etCity = (EditText) findViewById(R.id.et_city);
        etState = (EditText) findViewById(R.id.et_state);
        etZip = (EditText) findViewById(R.id.et_zipcode);

        if (getIntent().getExtras() != null) {
            try {
                JSONObject jsonObject = new JSONObject(getIntent().getExtras().getString("jsonObj"));
                etAdrs1.setText(jsonObject.getString("address1"));
                etAdrs2.setText(jsonObject.getString("address2"));
                etCity.setText(jsonObject.getString("city"));
                etState.setText(jsonObject.getString("state"));
                etZip.setText(jsonObject.getString("zipcode"));

                mObjId = jsonObject.getString("locationId");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private void postDataToServer() {
        String adrsValue1 = etAdrs1.getText().toString();
        String adrsValue2 = etAdrs2.getText().toString();
        String cityValue = etCity.getText().toString();
        String stateValue = etState.getText().toString();
        String zipValue = etZip.getText().toString();

        if (adrsValue1 != null && adrsValue1.length() > 0 ||
                adrsValue2 != null && adrsValue2.length() > 0 ||
                cityValue != null && cityValue.length() > 0 ||
                zipValue != null && zipValue.length() > 0 ||
                stateValue != null && stateValue.length() > 0) {
            String ip = HttpUrl.COMMONURL;
            String accessToken = SignIn.GET_ACCESS_TOKEN();
            String url = ip + "/doctorinfo/address/update?token=" + accessToken;
            JSONObject object = new JSONObject();
            JSONArray array = new JSONArray();
            try {

                if (mObjId != null) {
                    url = ip + "/doctorinfo/address/update?token=" + accessToken;
                    object.put("locationId", mObjId);
                } else {
                    url = ip + "/doctorinfo/address/add?token=" + accessToken;
                }
                object.put("address1", adrsValue1);
                object.put("address2", adrsValue2);
                object.put("city", cityValue);
                object.put("state", stateValue);
                object.put("zipcode", zipValue);
                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ProfilePostData post = new ProfilePostData(array);
            post.delegate = this;
            post.execute(url);
        } else {
            finish();
        }
    }

    private void setAddressFromGps() {
        if (address == null) {
            GPSService mGPSService = new GPSService(this);
            mGPSService.getLocation();

            if (mGPSService.isLocationAvailable == false) {
                // Here you can ask the user to try again, using return; for that

            } else {
                // Getting location co-ordinates
                address = mGPSService.getLocationAddress();
            }
            // make sure you close the gps after using it. Save user's battery power
            mGPSService.closeGPS();
        }
        if (address != null) {
            String addressLine2 = "";

            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                if (i == 0) {
                    etAdrs1.setText(address.getAddressLine(i));
                } else {
                    addressLine2 = addressLine2 + address.getAddressLine(i);
                }
            }

            etAdrs2.setText(addressLine2);
            etState.setText(address.getAdminArea());
            etCity.setText(address.getLocality());
            etZip.setText(address.getPostalCode());
        }

    }


    @Override
    public void response(String result) {
        System.out.println(result);
        try {
            if (result != null) {
                JSONObject object = new JSONObject(result);
                String status = object.getString("status");
                if (status.equals("success")) {
                    Toast.makeText(this, "Successfully Added Location Details", Toast.LENGTH_LONG).show();
                    finish();
                }
            } else {
                Toast.makeText(this, "Internal Error.. Please Try Again", Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
