package medrep.medrep;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.app.AlertDialog;

import com.app.adapter.AutoCompleteTextViewCustomAdapter;
import com.app.adapter.CommonAdapter;
import com.app.interfaces.GetResponse;
import com.app.task.NotificationGetTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pharma.ValidateOtpActivity;

public class SearchForDrugsActivity extends AppCompatActivity implements View.OnClickListener, GetResponse{

    private ArrayList<String> names;
    private TextView drugName;
    private AutoCompleteTextView searchConnections;
    private TextView drugDescription;
    private TextView drugQuantity;
    private Button certified;
    private Boolean checkValue = true;
    private LinearLayout linearLayoutParent;
    ProgressDialog dialog;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_search_for_drugs);

        linearLayoutParent = (LinearLayout) findViewById(R.id.parent_container);
        HorizontalScrollView horizontalScroll = (HorizontalScrollView)findViewById(R.id.horizontalScroll);
        horizontalScroll.setVisibility(View.GONE);
        LinearLayout searchLayout = (LinearLayout)findViewById(R.id.searchLayout);
        searchLayout.setVisibility(View.GONE);
        TextView title = (TextView)findViewById(R.id.title);
        title.setText("Search for Drugs");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        Button downArrow = (Button)findViewById(R.id.downArrow);
//        downArrow.setOnClickListener(this);
        Button onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(this);

        drugName = (TextView)findViewById(R.id.drugName);
        drugDescription = (TextView)findViewById(R.id.drugDescription);
        drugQuantity = (TextView)findViewById(R.id.drugQuantity);
        certified = (Button)findViewById(R.id.certified);

        searchConnections = (AutoCompleteTextView)findViewById(R.id.searchDrug);
        searchConnections.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = searchConnections.getText().toString();
                if (alertDialog !=null && alertDialog.isShowing()){
                    alertDialog.dismiss();
                }
                if(checkValue && searchText.length() >= 4) {
                    String searchedText = searchConnections.getText().toString();
                    System.out.println(searchedText);
                    String path = searchedText.replaceAll(" ", "%20");
                    String url = "http://122.175.50.252:8080/medrep-web/drugSearch?callback=callback&id=" + path + "&limit=10";
                    NotificationGetTask task = new NotificationGetTask();
                    task.delegate = SearchForDrugsActivity.this;
                    task.execute(url);
                } else if(searchText.length() == 0) {
                    checkValue = true;
                }

            }
        });
    }

    //http://www.truemd.in/api/medicine_details/?id=crocin&key=77b06e379a6f984a1f1ec091ce70f4

    private void initializeListView(final JSONArray result) {
        ListView relatedListView = (ListView)findViewById(R.id.relatedListView);
        CommonAdapter adapter = new CommonAdapter(this, result);
        relatedListView.setAdapter(adapter);
        relatedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    hideSoftKeyboard(SearchForDrugsActivity.this);
                    JSONObject object = result.getJSONObject(position);
                    String brand = object.getString("brand");
                    String manufacturer = object.getString("manufacturer");
                    String unitType = object.getString("unit_type");
                    String packageQty = object.getString("package_qty");

                    drugName.setText(brand);
                    drugDescription.setText(manufacturer);
                    drugQuantity.setText(packageQty + "mg");
                    certified.setText(unitType);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int buttonObjectId = v.getId();
        switch (buttonObjectId) {
//            case R.id.downArrow:
//                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                names.clear();
//                String searchedText = searchDrug.getText().toString();
//                System.out.println(searchedText);
//                String url = " http://www.truemd.in/api/medicine_suggestions/?id=" + searchedText + "&key=77b06e379a6f984a1f1ec091ce70f4" + "&limit=10";
//                NotificationGetTask task = new NotificationGetTask();
//                task.delegate = SearchForDrugsActivity.this;
//                task.execute(url);
//                break;
            case R.id.onBackClick:
                finish();
                break;
        }
    }

    @Override
    public void response(String result) {
        if(result != null) {
            try {
                dialog = new ProgressDialog(SearchForDrugsActivity.this);
                dialog.setMessage("Loading...");
                dialog.setIndeterminate(true);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                if (result.startsWith("{")){

                JSONObject object = new JSONObject(result);
                JSONObject response = object.getJSONObject("response");
                if(response.has("medicine")) {
                    JSONArray getDetails = response.getJSONArray("constituents");
                    JSONObject getMedicine = response.getJSONObject("medicine");

                    if(getDetails.length() > 0) {
                        Log.w("GETDEATILS", getDetails+"");
                        LinearLayout detailsLayout = (LinearLayout)findViewById(R.id.detailsLayout);
                        detailsLayout.setVisibility(View.VISIBLE);
                        LinearLayout relatedLayout = (LinearLayout)findViewById(R.id.relatedLayout);
                        relatedLayout.setVisibility(View.VISIBLE);
                        JSONObject obj = getDetails.getJSONObject(0);
                        String name = getMedicine.getString("brand");
                        String strength = obj.getString("strength");
                        String manufacturer = getMedicine.getString("manufacturer");
                        String tablet = getMedicine.getString("category");
                        drugName.setText(name);
                        drugDescription.setText(manufacturer);
                        drugQuantity.setText(strength);
                        certified.setText(tablet);
                    }
                    String text = searchConnections.getText().toString();
                    String path = text.replaceAll(" ", "%20");
                    String url = "http://oaayush-aayush.rhcloud.com/api/medicine_alternatives/?" + "id=" + path + "&key=77b06e379a6f984a1f1ec091ce70f4" + "&limit=20";
                    NotificationGetTask task = new NotificationGetTask();
                    task.delegate = SearchForDrugsActivity.this;
                    task.execute(url);
                } else if(response.has("suggestions")){
                    JSONArray array = response.getJSONArray("suggestions");
                    //adapter.clear();
                    if(array.length() > 0) {

                        for(int i = 0; i < array.length(); i++) {
                            JSONObject particularMedicine = array.getJSONObject(i);
                            String suggestion = particularMedicine.getString("suggestion");
                            names.add(suggestion);
//                        adapter.add(suggestion);
//                        adapter.getFilter().filter(searchConnections.getText(), null);
                        }
                    }
                } else if(response.has("medicine_alternatives")) {
                    JSONArray array = response.getJSONArray("medicine_alternatives");
                    initializeListView(array);
                }


                }else {
                    JSONArray jsonArray = new JSONArray(result);
                    System.out.println(result);
                    names = new ArrayList<>();

                    if (jsonArray.length() > 0) {

                        for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject particularMedicine = jsonArray.getJSONObject(i);
//                        String suggestion = particularMedicine.get("");
                            names.add(jsonArray.get(i).toString());
//                        adapter.add(suggestion);
//                        adapter.getFilter().filter(searchConnections.getText(), null);
                        }
                        LinearLayout detailsLayout = (LinearLayout)findViewById(R.id.detailsLayout);
                        detailsLayout.setVisibility(View.GONE);
                        LinearLayout relatedLayout = (LinearLayout)findViewById(R.id.relatedLayout);
                        relatedLayout.setVisibility(View.GONE);
                        alertDialog = new AlertDialog.Builder(this).create();
                        LayoutInflater inflater = getLayoutInflater();
                        View convertView = (View) inflater.inflate(R.layout.custom_alert_dialog, null);
                        alertDialog.setView(convertView);
                        alertDialog.setTitle("Select Drug");
                        ListView lv = (ListView) convertView.findViewById(R.id.lv);
                        hideSoftKeyboard(SearchForDrugsActivity.this);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
                        lv.setAdapter(adapter);
                        alertDialog.show();
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                checkValue = false;
                                InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                searchConnections.setText(names.get(position));
                                alertDialog.dismiss();
                                String searchConnectionsText = searchConnections.getText().toString();
                                if (!searchConnectionsText.equals("")) {
                                    String text = searchConnections.getText().toString();
                                    String path = text.replaceAll(" ", "%20");
                                    String url = "http://122.175.50.252:8080/medrep-web/medicineDetails?id="+ path +"&limit=10";
                                    NotificationGetTask task = new NotificationGetTask();
                                    task.delegate = SearchForDrugsActivity.this;
                                    task.execute(url);
                                }
                            }
                        });
                    } else{
                        myDalog("No Drugs Found!!");
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            finally {
                if (dialog != null && dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        }
    }
    /**
     * Method hiding the android soft keypad
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    /**
     * Method creating a dialog-box, if there is no drugs found
     */
    public void myDalog(String msg) {
        final Dialog dialog = new Dialog(SearchForDrugsActivity.this);

        dialog.setContentView(R.layout.dialog);
        // Set dialog title
        dialog.setTitle("Medrep Message");

        Button next_btn;
        Button prev_btn;

        next_btn = (Button)dialog.findViewById(R.id.nextButton);
        prev_btn = (Button) dialog.findViewById(R.id.backButton);
        prev_btn.setVisibility(View.INVISIBLE);
        next_btn.setText("OK");

        // set values for custom dialog components - text, image and button
        final TextView text = (TextView) dialog.findViewById(R.id.textDialog);
        text.setText(msg);
        ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
        dialog.show();
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                searchConnections.setText("");

            }
        });
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();

            }
        });
    }
}
