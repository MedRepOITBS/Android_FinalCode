package medrep.medrep;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.app.task.ProfilePostData;
import com.app.util.HttpUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfilePublicationsActivity extends AppCompatActivity implements GetResponse {

    private EditText articleTitleText, publicationText, urlPublcText;
    private String mObjId = null;
    private Button mGoToDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_profile_publications);
        TextView title = (TextView) findViewById(R.id.title);
        TextView tvDone = (TextView) findViewById(R.id.tv_done);
        title.setText("Add Publications");
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postDataToServer();

            }
        });

        Button onBackClick = (Button) findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        articleTitleText = (EditText) findViewById(R.id.articleTitleText);
        publicationText = (EditText) findViewById(R.id.publicationText);
        urlPublcText = (EditText) findViewById(R.id.et_ref_url);


        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.yearText);

        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("YYYY");
        categories.add("2001");
        categories.add("2002");
        categories.add("2003");
        categories.add("2004");
        categories.add("2005");
        categories.add("2006");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        mGoToDetails = (Button) findViewById(R.id.goto_details);
        if (getIntent().getExtras() != null) {
            try {
                JSONObject jsonObject = new JSONObject(getIntent().getExtras().getString("jsonObj"));
                articleTitleText.setText(jsonObject.getString("articleName"));
                publicationText.setText(jsonObject.getString("publication"));
                urlPublcText.setText(jsonObject.getString("url"));
                if (urlPublcText.getText().toString().equals("")){
                    mGoToDetails.setVisibility(View.GONE);
                }

                String yearOfPub = jsonObject.getString("year");
                if (yearOfPub != null) {
                    //              String[] splitted = yearOfPub.split("-");
                    spinner.setSelection(dataAdapter.getPosition(yearOfPub));
                }
                mObjId = jsonObject.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            mGoToDetails.setVisibility(View.GONE);
        }
        mGoToDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (urlPublcText.getText().toString() != null) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ProfilePublicationsActivity.this);

                    WebView wv = new WebView(ProfilePublicationsActivity.this);
                    wv.loadUrl(urlPublcText.getText().toString());
                    wv.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);

                            return true;
                        }
                    });
                    alert.setView(wv);
                    alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();


                }
            }
        });

    }

    private void postDataToServer() {
        String articleTitleTextValue = articleTitleText.getText().toString();
        String publicationTextValue = publicationText.getText().toString();
        String urlPublicationTextValue = urlPublcText.getText().toString();
        Spinner yearText = (Spinner) findViewById(R.id.yearText);
        String yearTextValue = yearText.getSelectedItem().toString();
        if (!urlPublicationTextValue.contains("http")){
            showAlertDialog("Please Enter the URL in a proper format \n Eg: http://www.medrep.com");
        }

        if (articleTitleTextValue != null && articleTitleTextValue.length() > 0 &&
                publicationTextValue != null && publicationTextValue.length() > 0 &&
                yearTextValue != null && yearTextValue.length() > 0) {
            String ip = HttpUrl.COMMONURL;
            String accessToken = SignIn.GET_ACCESS_TOKEN();
            String url = null;
            JSONObject object = new JSONObject();
            JSONArray array = new JSONArray();
            try {
                if (mObjId != null) {
                    url = ip + "/doctorinfo/publications/update?token=" + accessToken;
                    object.put("id", mObjId);
                } else {
                    url = ip + "/doctorinfo/publications/add?token=" + accessToken;
                }
                object.put("articleName", articleTitleTextValue);
                object.put("publication", publicationTextValue);
                object.put("url", urlPublicationTextValue);
                object.put("year", yearTextValue);
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
    public void response(String result) {
        Log.i("RESULT", result);
        try {
            JSONObject object = new JSONObject(result);
            String status = object.getString("status");
            if (status.equals("success")) {
                Toast.makeText(this, "Successfully added publications for the user", Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
