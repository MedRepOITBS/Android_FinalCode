package medrep.medrep;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.app.db.TherapeuticCategory;
import com.app.interfaces.GetResponse;
import com.app.pojo.SignIn;
import com.app.task.ProfilePostData;
import com.app.util.HttpUrl;
import com.app.util.JSONParser;
import com.app.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guna on 21/07/16.
 */
public class ProfileAwardActivity extends AppCompatActivity implements GetResponse {

    private Spinner therapeuticSpinner;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_profile_award);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        TextView title = (TextView)findViewById(R.id.title);
        title.setText("Therapeutic Area");
        TextView tvDone = (TextView)findViewById(R.id.tv_done);
        therapeuticSpinner = (Spinner) findViewById(R.id.therapeutic_spinner);
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = therapeuticSpinner.getSelectedItem().toString();
                postDataToServer(area);


            }
        });

        Button onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(Utils.isNetworkAvailable(this)){
            getTherapeuticAsync.execute();
        }

    }
    private void postDataToServer(String area) {

        if (area != null && area.length()>0) {
            String ip = HttpUrl.COMMONURL;
            String accessToken = SignIn.GET_ACCESS_TOKEN();
            String url = ip + "/doctorinfo/therapeutic-area/update?token=" + accessToken;
            JSONObject object = new JSONObject();
            JSONArray array = new JSONArray();
            try {
                object.put("therapeuticName", area);
                object.put("therapeuticDesc", area);
                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ProfilePostData post = new ProfilePostData(array);
            post.delegate = this;
            post.execute(url);
        }
    }

    AsyncTask<Void, Void, ArrayList<TherapeuticCategory>> getTherapeuticAsync = new AsyncTask<Void, Void, ArrayList<TherapeuticCategory>>() {

        ProgressDialog pd;

        @Override
        protected ArrayList<TherapeuticCategory> doInBackground(Void... params) {
            return getTherapeuticAreaDetails();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ProfileAwardActivity.this);
            pd.setTitle("Getting Therapeutic Areas");
            pd.setMessage("Please wait, while we retrieve Therapeutic areas.");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(final ArrayList<TherapeuticCategory> therapeuticCategories) {
            super.onPostExecute(therapeuticCategories);
            System.out.println("therapeuticCategories: " + therapeuticCategories);

            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }


            //therapeuticSpinner.setVisibility(View.VISIBLE);
            therapeuticSpinner.setPrompt("Select Therapeutic Area");

            // Spinner Drop down elements

            List<String> categories = new ArrayList<String>();


            categories.add("Select Therapeutic Area");
            for (TherapeuticCategory therapeuticCategory : therapeuticCategories) {
                categories.add(therapeuticCategory.getTherapeuticName());
            }

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ProfileAwardActivity.this, R.layout.location_spinner_text, categories);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(R.layout.dropdown);

            // attaching data adapter to spinner
            therapeuticSpinner.setAdapter(dataAdapter);

            therapeuticSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

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

    @Override
    public void response(String result) {
        System.out.println(result);
        try {
            if (result != null) {
                JSONObject object = new JSONObject(result);
                String status = object.getString("status");
                if (status.equals("success")) {
                    Toast.makeText(this, "Successfully Added Therapeutic Details", Toast.LENGTH_LONG).show();
                    finish();
                }
            }else{
                Toast.makeText(this, "Internal Error.. Please Try Again", Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
