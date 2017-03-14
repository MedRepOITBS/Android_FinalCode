package medrep.medrep;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.interfaces.GetResponse;
import com.app.pojo.SignIn;
import com.app.task.HttpPost;
import com.app.task.ProfilePostData;
import com.app.util.HttpUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProfileEducationActivity extends AppCompatActivity implements GetResponse {

    private EditText aggregateText, degreeText, specialtyText, instituteText;
    private String mObjId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_education);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        aggregateText = (EditText) findViewById(R.id.aggregateText);
        degreeText = (EditText) findViewById(R.id.degreeText);
        specialtyText = (EditText) findViewById(R.id.specialtyText);
        instituteText = (EditText) findViewById(R.id.instituteText);

        TextView title = (TextView) findViewById(R.id.title);
        TextView tvDone = (TextView) findViewById(R.id.tv_done);
        final Spinner fromText = (Spinner) findViewById(R.id.fromText);
        final Spinner toText = (Spinner) findViewById(R.id.toText);
        title.setText("Education");
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int from = Integer.parseInt(fromText.getSelectedItem().toString());
                int to = Integer.parseInt(toText.getSelectedItem().toString());
                System.out.println(from + " " + to);
                if (from >= to) {
                    showAlertDialog("From Year Cannot be smaller than To Year. Please change the year accordingly.");

                } else {
                    postData(from, to);
                }
            }
        });

        Button onBackClick = (Button) findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Spinner element

        final ArrayList<String> item = new ArrayList<>();

        // Spinner click listener
        fromText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item.add(parent.getItemAtPosition(position).toString());
                //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        toText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item.add(parent.getItemAtPosition(position).toString());
                //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner Drop down elements
        List<String> categories = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(cal.YEAR);
        for (int i = 0; i < 25; i++) {
            categories.add(year + "");
            year--;
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        fromText.setAdapter(dataAdapter);
        toText.setAdapter(dataAdapter);


        if (getIntent().getExtras() != null) {
            try {
                JSONObject jsonObject = new JSONObject(getIntent().getExtras().getString("jsonObj"));
                aggregateText.setText(jsonObject.getString("aggregate"));
                degreeText.setText(jsonObject.getString("degree"));
                specialtyText.setText(jsonObject.getString("specialization"));
                instituteText.setText(jsonObject.getString("collegeName"));

                String yearOfPassout = jsonObject.getString("yearOfPassout");
                if (yearOfPassout != null) {
                    String[] splitted = yearOfPassout.split("-");
                    fromText.setSelection(dataAdapter.getPosition(splitted[0].trim()));
                    toText.setSelection(dataAdapter.getPosition(splitted[1].trim()));
                }
                mObjId = jsonObject.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void showAlertDialog(String msg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("MedRep");
        dialog.setMessage(msg);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void postData(int fromDate, int toDate) {
        String aggregateTextValue = aggregateText.getText().toString();
        String degreeTextValue = degreeText.getText().toString();
        String specialtyTextValue = specialtyText.getText().toString();
        String instituteTextValue = instituteText.getText().toString();

        if (aggregateTextValue != null && aggregateTextValue.length() > 0 &&
                degreeTextValue != null && degreeTextValue.length() > 0 &&
                specialtyTextValue != null && specialtyTextValue.length() > 0 &&
                instituteTextValue != null && instituteTextValue.length() > 0) {
            String url = null;
            JSONArray array = null;
            try {
                String ip = HttpUrl.COMMONURL;
                String accessToken = SignIn.GET_ACCESS_TOKEN();
                JSONObject object = new JSONObject();
                array = new JSONArray();
                if (mObjId != null) {
                    url = ip + "/doctorinfo/education/update?token=" + accessToken;
                    object.put("id", mObjId);
                } else {
                    url = ip + "/doctorinfo/education/add?token=" + accessToken;
                }
                object.put("collegeName", instituteTextValue);
                object.put("course", degreeTextValue);
                object.put("aggregate", aggregateTextValue);
                object.put("specialization", specialtyTextValue);
                object.put("yearOfPassout", fromDate + "-" + toDate);
                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ProfilePostData post = new ProfilePostData(array);
            post.delegate = this;
            post.execute(url);
        } else {
            showAlertDialog("Please Fill All The Details");
        }
    }

    @Override
    public void response(String result) {
        System.out.println(result);
        try {
            JSONObject object = new JSONObject(result);
            String status = object.getString("status");
            if (status.equals("success")) {
                Toast.makeText(this, "Successfully added education details", Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
