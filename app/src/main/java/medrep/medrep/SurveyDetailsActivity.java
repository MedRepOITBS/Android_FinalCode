package medrep.medrep;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.adapter.SurveyListAdapter;
import com.app.interfaces.GetResponse;
import com.app.task.SurveyReportsGetAsyncTask;
import com.app.util.HttpUrl;
import com.app.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;

import pharma.PharmaDashBoard;

public class SurveyDetailsActivity extends AppCompatActivity implements GetResponse{

    private ListView doctorsList;
    private int surveyId;
    private TextView sentValue;
    private TextView sentNumber;
    private TextView recivedNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_details);
        doctorsList = (ListView)findViewById(R.id.doctorsList);
        surveyId = getIntent().getExtras().getInt("surveyId");
        String surveyName = getIntent().getExtras().getString("surveyName");
        HorizontalScrollView horizontalScroll = (HorizontalScrollView) findViewById(R.id.horizontalScroll);
        horizontalScroll.setVisibility(View.GONE);
        LinearLayout searchLayout = (LinearLayout) findViewById(R.id.searchLayout);
        searchLayout.setVisibility(View.GONE);
        sentValue = (TextView)findViewById(R.id.sent_value);
        sentNumber = (TextView)findViewById(R.id.p_sentnumber);
        recivedNumber = (TextView)findViewById(R.id.tv_recivednumer);
        Button onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = (TextView)findViewById(R.id.title);
        title.setText(surveyName);
        title.setTextSize(20f);
        getData();
    }

    private void getData() {
        //get data from server
        SurveyReportsGetAsyncTask surveyReportsGetAsyncTask = new SurveyReportsGetAsyncTask(this);
        String baseUrl = HttpUrl.COMMONURL;
        String url = baseUrl + "/survey/getSurveyStatistics/" + surveyId;
        surveyReportsGetAsyncTask.execute(url);
    }

    @Override
    public void response(String result) {
        System.out.println(result);
        try {
            Object json = new JSONTokener(result).nextValue();
            if(json instanceof JSONObject) {
                JSONObject surveyResultObject = new JSONObject(result);
                if(surveyResultObject.has("status")) {
                    String message  = surveyResultObject.getString("message");
                    String status = surveyResultObject.getString("status");
                    Utils.DISPLAY_GENERAL_DIALOG(this, "MedRep", message);
                    if(status.equals("success")) {
                        getData();
                    }
                } else {
                    String surveyId = surveyResultObject.getString("surveyId");
                    String totalPending = surveyResultObject.getString("totalPending");
                    String totalCompleted = surveyResultObject.getString("totalCompleted");
                    String totalSent = surveyResultObject.getString("totalSent");
                    sentValue.setText(totalSent);
                    sentNumber.setText(totalPending);
                    recivedNumber.setText(totalCompleted);
                    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
                    JSONArray array = ((JSONObject) json).getJSONArray("pending");
                    for (int i = 0; i < array.length(); i++) {
                        HashMap<String, String> hash = new HashMap<>();
                        JSONObject object = (JSONObject)array.get(i);
                        String displayName = object.getString("displayName");
                        String remindNotification = object.getString("remindNotification");
                        String doctorId = object.getString("doctorId");
                        hash.put("displayName", displayName);
                        hash.put("remindNotification", remindNotification);
                        hash.put("surveyId", surveyId);
                        hash.put("doctorId", doctorId);
                        arrayList.add(hash);
                    }
                    SurveyListAdapter adapter = new SurveyListAdapter(this, arrayList);
                    doctorsList.setAdapter(adapter);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
