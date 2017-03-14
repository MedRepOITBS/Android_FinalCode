package medrep.medrep;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.interfaces.GetResponse;
import com.app.pojo.SignIn;
import com.app.task.HttpPost;
import com.app.task.NotificationGetTask;
import com.app.task.ProfilePostData;
import com.app.util.HttpUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class AddProfileViewActivity extends AppCompatActivity implements View.OnClickListener, GetResponse{

    private EditText designationText, organisationText, locationText, summaryText;
    private String mObjId = null;
    private Button month,toDate;
    private String monthText,toDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_add_profile_view);
        TextView title = (TextView)findViewById(R.id.title);
        TextView tvDone = (TextView)findViewById(R.id.tv_done);
        title.setText("Add Experience");
        designationText = (EditText)findViewById(R.id.designationText);
        organisationText = (EditText)findViewById(R.id.organisationText);
        locationText = (EditText)findViewById(R.id.locationText);
        summaryText = (EditText)findViewById(R.id.summaryText);
        month = (Button)findViewById(R.id.month);
        toDate = (Button)findViewById(R.id.toDate);

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                monthText = month.getText().toString();
                toDateText = toDate.getText().toString();

                if (!(monthText.equals("DD-MM-YYYY") || toDateText.equals("DD-MM-YYYY"))) {
                    int fromYearValue = Integer.parseInt(monthText.split("-")[2]);
                    int toYearValue = Integer.parseInt(toDateText.split("-")[2]);
                    if (fromYearValue > toYearValue) {
                        showErrorDialog();
                    } else if (fromYearValue == toYearValue) {
                        if (Integer.parseInt(monthText.split("-")[1]) > Integer.parseInt(toDateText.split("-")[1])) {
                            showAlertDialog("From Year Cannot be smaller than To Year. Please change the year accordingly.");
                        } else {
                            postData();
                        }
                    } else {
                        postData();
                    }
                }else {
                    showAlertDialog("Please Fill All the Details");
                }
            }
        });
        Button month = (Button)findViewById(R.id.month);
        month.setOnClickListener(this);
        Button onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(this);

        if (getIntent().getExtras() != null) {
            try {
                JSONObject jsonObject = new JSONObject(getIntent().getExtras().getString("jsonObj"));
                designationText.setText(jsonObject.getString("designation"));
                organisationText.setText(jsonObject.getString("hospital"));
                locationText.setText(jsonObject.getString("location"));
                summaryText.setText(jsonObject.getString("summary"));
      //          month.setText("01-"+);


                mObjId = jsonObject.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void showAlertDialog(String msg){
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

    @Override
    public void onClick(View v) {
        int value = v.getId();
        switch (value) {
            case R.id.month:
            case R.id.toDate:
                final Calendar c = Calendar.getInstance();

                int y = c.get(Calendar.YEAR);
                int m = c.get(Calendar.MONTH);
                int d = c.get(Calendar.DAY_OF_MONTH);
                //final String[] MONTH = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

                DatePickerDialog dp = new DatePickerDialog(AddProfileViewActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String erg = "";
                                erg = String.valueOf(dayOfMonth);
                                erg += "-" + String.valueOf(monthOfYear + 1);
                                erg += "-" + year;

                                ((Button)findViewById(R.id.month)).setText(erg);

                            }

                        }, y, m, d);
                dp.setTitle("Calender");
                dp.setMessage("Select Your Graduation date Please?");

                dp.show();
                break;

            case R.id.onBackClick:
                finish();
                break;
        }

    }

    private void postData() {
        String designationValue = designationText.getText().toString();
        String organisationValue = organisationText.getText().toString();
        String locationValue = locationText.getText().toString();
        String summaryValue = summaryText.getText().toString();



        if (designationValue != null && designationValue.length()>0 &&
                organisationValue != null && organisationValue.length()>0 &&
                locationValue != null && locationValue.length()>0) {
        String fromYear = monthText.split("-")[1] + " " + monthText.split("-")[2];
        String toYear = toDateText.split("-")[1] + " " + toDateText.split("-")[2];
        String ip = HttpUrl.COMMONURL;
        String accessToken = SignIn.GET_ACCESS_TOKEN();
        String url = null;
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();

        try {
            if (mObjId != null) {
                url = ip + "/doctorinfo/workexperience/update?token=" + accessToken;
                object.put("id", mObjId);
            } else {
                url = ip + "/doctorinfo/workexperience/add?token=" + accessToken;
            }
            object.put("hospital", organisationValue);
            object.put("fromDate", fromYear);
            object.put("toDate", toYear);
            object.put("location", locationValue);
            object.put("designation", designationValue);
            object.put("currentCompany", true);
            object.put("summary", summaryValue);
            array.put(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ProfilePostData post = new ProfilePostData(array);
        post.delegate = this;
        post.execute(url);
        }else {
            showAlertDialog("Please Fill All The Details");
        }
    }

    private void showErrorDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("MedRep");
        dialog.setMessage("From Year Cannot be smaller than To Year. Please change the year accordingly.");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void response(String result) {
        System.out.println(result);
        try {
            JSONObject object = new JSONObject(result);
            String status = object.getString("status");
            if(status.equals("success")) {
                Toast.makeText(this, "Successfully added Work experience details", Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
