package medrep.medrep;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.interfaces.GetResponse;
import com.app.pojo.SignIn;
import com.app.task.ProfilePostData;
import com.app.util.HttpUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by RKatakam on 9/18/2016.
 */
public class ProfileContactInfoActivity extends AppCompatActivity implements GetResponse {

    private EditText et_mobile,et_phone,et_email,et_alt_email;
    private Button onBackClick;
    private String mObjId = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_contact_info);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        TextView title = (TextView)findViewById(R.id.title);
        TextView tvDone = (TextView)findViewById(R.id.tv_done);
        title.setText("Edit Contact Info");
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    postDataToServer();

            }
        });

        et_mobile = (EditText)findViewById(R.id.et_mobile);
        et_phone = (EditText)findViewById(R.id.et_phone);
        et_email = (EditText)findViewById(R.id.et_email);
        et_alt_email = (EditText)findViewById(R.id.et_alt_email);
        onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (getIntent().getExtras() != null) {
            try {
                JSONObject jsonObject = new JSONObject(getIntent().getExtras().getString("jsonObj"));
                et_mobile.setText(jsonObject.getString("mobileNo"));
                et_phone.setText(jsonObject.getString("phoneNo"));
                et_email.setText(jsonObject.getString("email"));
                et_alt_email.setText(jsonObject.getString("alternateEmail"));

                mObjId = jsonObject.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void postDataToServer() {

        String mobileValue = et_mobile.getText().toString();
        String phoneValue = et_phone.getText().toString();
        String emailValue = et_email.getText().toString();
        String altEmailValue = et_alt_email.getText().toString();

        if (mobileValue != null && mobileValue.length()>0 ||
                phoneValue != null && phoneValue.length()>0 ||
                emailValue != null && emailValue.length()>0 ||
                altEmailValue != null && altEmailValue.length()>0) {
            String ip = HttpUrl.COMMONURL;
            String accessToken = SignIn.GET_ACCESS_TOKEN();
            String url = ip + "/doctorinfo/contactInfo/update?token=" + accessToken;
            JSONObject object = new JSONObject();
            JSONArray array = new JSONArray();
            try {
                if (mObjId != null) {
                    url = ip + "/doctorinfo/contactInfo/update?token=" + accessToken;
                    object.put("id", mObjId);
                } else {
                    url = ip + "/doctorinfo/contactInfo/add?token=" + accessToken;
                }
                object.put("mobileNo", mobileValue);
                object.put("phoneNo", phoneValue);
                object.put("emailId", emailValue);
                object.put("alternateEmailId", altEmailValue);
                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ProfilePostData post = new ProfilePostData(array);
            post.delegate = this;
            post.execute(url);
        }else {
            finish();
        }
    }

    @Override
    public void response(String result) {
        System.out.println(result);
        try {
            JSONObject object = new JSONObject(result);
            String status = object.getString("status");
            if(status.equals("success")) {
                Toast.makeText(this, "Successfully Added Contact Details", Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
