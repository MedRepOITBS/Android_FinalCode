package medrep.medrep;

import android.app.Activity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.attr.data;

public class LatestSurveyReportsActivity extends Activity implements GetResponse{
    private ListView surveyReportsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activivty_latest_survey_reports);
        HorizontalScrollView horizontalScroll = (HorizontalScrollView) findViewById(R.id.horizontalScroll);
        horizontalScroll.setVisibility(View.GONE);
        LinearLayout searchLayout = (LinearLayout) findViewById(R.id.searchLayout);
        searchLayout.setVisibility(View.GONE);
        Button onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = (TextView)findViewById(R.id.title);
        title.setText("Surveys");
        title.setTextSize(20f);
        surveyReportsList = (ListView) findViewById(R.id.surveyReportsList);
        SurveyReportsGetAsyncTask surveyReportsGetAsyncTask = new SurveyReportsGetAsyncTask(this);
        String baseUrl = HttpUrl.COMMONURL;
        String url = baseUrl + "/survey/getCompanySurveys";
        surveyReportsGetAsyncTask.execute(url);

    }

    @Override
    public void response(String result) {
        System.out.println(result);
        if(!result.isEmpty()) {
            try {
                Object json = new JSONTokener(result).nextValue();
                if(json instanceof JSONArray) {
                    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
                    final JSONArray array = new JSONArray(result);
                    for(int i = 0; i < array.length(); i++) {
                        HashMap<String, String> list = new HashMap<>();
                        JSONObject object = (JSONObject) array.get(i);
                        String surveyTitle = (String) object.get("surveyTitle");
                        list.put("surveyTitle", surveyTitle);
                        String surveyDescription = (String) object.getString("surveyDescription");
                        list.put("surveyDescription", surveyDescription);
                        Boolean reportsAvailable = object.getBoolean("reportsAvailable");
                        list.put("reportsAvailable", reportsAvailable.toString());
                        arrayList.add(list);
                    }
                    SurveyListAdapter adapter = new SurveyListAdapter(this, arrayList);
                    surveyReportsList.setAdapter(adapter);
                    surveyReportsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                JSONObject object = (JSONObject) array.get(position);
                                int surveyId = object.getInt("surveyId");
                                String surveyName = object.getString("surveyTitle");
                                Intent intent = new Intent(LatestSurveyReportsActivity.this, SurveyDetailsActivity.class);
                                intent.putExtra("surveyId", surveyId);
                                intent.putExtra("surveyName", surveyName);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
