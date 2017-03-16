package medrep.medrep;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.print.PrintAttributes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.adapter.CommonAdapter;
import com.app.adapter.CustomSpinnerAdapter;
import com.app.adapter.DoctorGroupListViewAdapter;
import com.app.interfaces.GetResponse;
import com.app.pojo.SignIn;
import com.app.task.NotificationGetTask;
import com.app.util.HttpUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.StreamHandler;

public class TransformActivity extends AppCompatActivity implements View.OnClickListener, GetResponse{

//    private String[] news = {"Colon cancer varies by birth place", "Its not too late to get flu shot", "Best cancer screening methods",
//            "Best Heart Surgery Operations", "Best Hospitals", "Colon cancer varies by birth place", "Its not too late to get flu shot", "Best cancer screening methods"};
    private int[] buttonIds;
    private ListView newsListView;
    private JSONArray serverArray;
    private JSONArray tempResArray;
    private DoctorGroupListViewAdapter listAdapter;
    private LinearLayout dropdown;
    private TextView searchConnections;
    private JSONArray resArray;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transform);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        TextView title = (TextView)findViewById(R.id.title);
        title.setText("Transform");
        Button connect = (Button)findViewById(R.id.connect);
        connect.setOnClickListener(this);
        Button share = (Button)findViewById(R.id.share);
        share.setOnClickListener(this);
        final Button serve = (Button)findViewById(R.id.serve);
        serve.setOnClickListener(this);
        Button onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(this);
        LinearLayout addLayout = (LinearLayout)findViewById(R.id.addLayout);
        addLayout.setVisibility(View.GONE);
        Button transform = (Button)findViewById(R.id.transform);
        transform.setBackground(getResources().getDrawable(R.drawable.border_button_light));
        LinearLayout listLayout = (LinearLayout)findViewById(R.id.listLayout);
        searchConnections = (TextView)findViewById(R.id.searchConnections);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN == 0){
            Log.i("DDDDDDDDDDDDDDD", "true");
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        searchConnections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchConnections.setCursorVisible(true);
            }
        });

//        searchConnections.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                searchConnections.setCursorVisible(false);
//                if (event == null ) {
//                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
////                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
////                    in.hideSoftInputFromWindow(searchConnections.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
//                }
//                return false;
//            }
//        });

        searchConnections.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                searchConnections.setCursorVisible(true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchConnections.setCursorVisible(true);
//                if(count > 0){
                    resArray = new JSONArray();

                    try{
                        JSONObject jsonObjectTemp = new JSONObject();
                        jsonObjectTemp.put("Empty", "Empty");
                        if(serverArray != null) {
                            for(int i=1; i< serverArray.length(); i++){
                                JSONObject jsonObject = serverArray.getJSONObject(i);
                                if(jsonObject.getString("title").toLowerCase().contains(s.toString().toLowerCase())){
                                    resArray.put(jsonObject);
                                }
//                            else {
//                                resArray.put(jsonObjectTemp);
//                            }
                            }
                        }
//                    }
                        Log.i("ressssssssssss", resArray.length()+":"+serverArray+"");
                        initializeListView(resArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                }
//                if(count == 0){
//                    serverArray = tempResArray;
//                    initializeListView(serverArray);
//                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("SSSSSSSSSSSSSSS", "dsfsdfsd");
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//                searchConnections.setCursorVisible(true);
            }
        });

//        listLayout.getBackground().setAlpha(120);
        dropdown = (LinearLayout)findViewById(R.id.dropdown);
        dropdown.setVisibility(View.GONE);

        newsListView = (ListView)findViewById(R.id.newsListView);
//        listAdapter = new DoctorGroupListViewAdapter(this, "transformActivity");
//        newsListView.setAdapter(listAdapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchConnections.setCursorVisible(false);
                JSONArray listViewArray = new JSONArray();
                if(searchConnections.getText().toString().isEmpty()){
                    listViewArray = serverArray;
                    initializeListView(serverArray);
                }else {
                    listViewArray = resArray;
                }
                Intent intent = new Intent(TransformActivity.this, TransformDetailActivity.class);
                try {
                    JSONObject object = reverse(listViewArray).getJSONObject(position);
                    String title = object.getString("title");
                    String description = object.getString("tagDesc");
                    String innerImgUrl;
                    String videoUrl;
                    if(object.has("innerImgUrl")){
                        if(!object.getString("innerImgUrl").equals("null")){
                            //                        videoUrl = object.getString("videoUrl");
                            intent.putExtra("innerImgUrl", object.getString("innerImgUrl"));
                        }
                    }
                    if(object.has("videoUrl")){
                        if(!object.getString("videoUrl").equals("null")){
                            //                        videoUrl = object.getString("videoUrl");
                            intent.putExtra("videoUrl", object.getString("videoUrl"));
                        }
                    }

                    intent.putExtra("description", description);
                    intent.putExtra("newsTitle", title);


//                    if(position == 1 || position == 3 || position == 7) {
//                        intent.putExtra("video", "video");
//                    } else if(position == 2) {
//                        intent.putExtra("pdf", "pdf");
//                    }
                    startActivity(intent);
                    searchConnections.setText("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

//        if(getIntent().hasExtra("Marketing")){
//            Intent intent = new Intent(this, ServeActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
//        }


        initialieHorizontalView();
        getDataFromServer("");
    }

//    protected void getNews() {
//        super.onStart();
//        String ip = HttpUrl.COMMONURL;
//        String accessToken = SignIn.GET_ACCESS_TOKEN();
//        String url = ip + "/getNewsAndTransform?token="+ accessToken;
//        NotificationGetTask task = new NotificationGetTask();
//        task.delegate = TransformActivity.this;
//        task.execute(url);
//    }

    private void initializeListView(JSONArray array) {
        Log.i("InitializeArrayyyyyyy", array+"");
        CommonAdapter adapter = new CommonAdapter(this, reverse(array));
        newsListView.setAdapter(adapter);

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void initialieHorizontalView() {
        //final ListView listViewObject = listView;
        final String names[] = {"News & Updates", "Therapeutic Area", "Regulatory", "Education", "Journals", "Medical Innovation", "Podcasts / Webcasts", "Best Practices", "Case Studies", "Whitepapers", "Videos", "Clinical Trials"};
        buttonIds = new int[]{R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9, R.id.button10, R.id.button11, R.id.button12};
        LinearLayout horizontalLayout = (LinearLayout)findViewById(R.id.horizontalLayout);
        for(int i = 0; i < names.length; i++) {
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            Button tv=new Button(this);
            //tv.setCompoundDrawablePadding(15);
            tv.setBackgroundResource(R.color.home_button);
            if(i == 0) {
                tv.setBackgroundResource(R.drawable.background_tab_button);
            }
            tv.setGravity(Gravity.CENTER);
            tv.setText(names[i]);
            tv.setId(buttonIds[i]);
            tv.setTextColor(getResources().getColor(R.color.tect_color_white));
            tv.setTextSize(10f);
            tv.setPadding(0, 0, 0, 0);
            tv.setAllCaps(false);
            tv.setLayoutParams(lparams);
            horizontalLayout.addView(tv);
            final int j = i;
            tv.setOnClickListener(this);
        }
    }

    private void getDataFromServer(String category) {
        Log.i("urlllllllllllll", category);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        String ip = HttpUrl.COMMONURL;
        String accessToken = SignIn.GET_ACCESS_TOKEN();
        Log.i("accessssssssssss", accessToken);
        String url;
        if(category.equals("")) {
            url = ip + "/getNewsAndUpdates?token="+ accessToken;
        } else {
            url = ip + "/getNewsAndTransform/?token="+ accessToken +"&category="+category;
        }
        NotificationGetTask task = new NotificationGetTask();
        task.delegate = TransformActivity.this;
        task.execute(url);
    }

    @Override
    public void onClick(View v) {
        int objectId = v.getId();
        Intent intent;
        Button name;
        String category;
        String path;
        searchConnections.setText("");
        searchConnections.setCursorVisible(false);
//        dropdown = (LinearLayout)findViewById(R.id.dropdown);
//        dropdown.setVisibility(View.GONE);
        switch (objectId) {
            case R.id.connect:
                intent = new Intent(this, DoctorsMyContactActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
            case R.id.onBackClick:
                finish();
                break;
            case R.id.serve:
                intent = new Intent(this, ServeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
            case R.id.share:
                //Utils.DISPLAY_GENERAL_DIALOG(this, "Coming Soon...", "This Feature is presently under development.");
                intent = new Intent(this, MyShareActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
//            case R.id.slideButton:
//                slideSocialMenu();
//                break;
            case R.id.button1:
                clickButton(R.id.button1);
                getDataFromServer("");

                //listAdapter = new DoctorGroupListViewAdapter(TransformActivity.this, "transform1");
                //newsListView.setAdapter(listAdapter);
                break;
            case R.id.button2:
                clickButton(R.id.button2);
                name = (Button)findViewById(R.id.button2);
                category = name.getText().toString();
                path = category.replaceAll(" ", "");
                getDataFromServer(path);
//                listAdapter = new DoctorGroupListViewAdapter(TransformActivity.this, "transform4");
//                newsListView.setAdapter(listAdapter);
                break;
            case R.id.button3:
                clickButton(R.id.button3);
                name = (Button)findViewById(R.id.button3);
                category = name.getText().toString();
                path = category.replaceAll(" ", "");
                getDataFromServer(path);
//                listAdapter = new DoctorGroupListViewAdapter(TransformActivity.this, "transform2");
//                newsListView.setAdapter(listAdapter);
                break;
            case R.id.button4:
                clickButton(R.id.button4);
                name = (Button)findViewById(R.id.button4);
                category = name.getText().toString();
                path = category.replaceAll(" ", "");
                getDataFromServer(path);
//                listAdapter = new DoctorGroupListViewAdapter(TransformActivity.this, "transformActivity");
//                newsListView.setAdapter(listAdapter);
                break;
            case R.id.button5:
                clickButton(R.id.button5);
                name = (Button)findViewById(R.id.button5);
                category = name.getText().toString();
                path = category.replaceAll(" ", "");
                getDataFromServer(path);
//                listAdapter = new DoctorGroupListViewAdapter(TransformActivity.this, "transform3");
//                newsListView.setAdapter(listAdapter);
                break;
            case R.id.button6:
                clickButton(R.id.button6);
                name = (Button)findViewById(R.id.button6);
                category = name.getText().toString();
                path = category.replaceAll(" ", "");
                getDataFromServer(path);
//                listAdapter = new DoctorGroupListViewAdapter(TransformActivity.this, "transform4");
//                newsListView.setAdapter(listAdapter);
                break;
            case R.id.button7:
                clickButton(R.id.button7);
                name = (Button)findViewById(R.id.button7);
                category = name.getText().toString();
                path = category.replaceAll(" ", "");
                getDataFromServer(path);
//                listAdapter = new DoctorGroupListViewAdapter(TransformActivity.this, "transform5");
//                newsListView.setAdapter(listAdapter);
                break;
            case R.id.button8:
                clickButton(R.id.button8);
                name = (Button)findViewById(R.id.button8);
                category = name.getText().toString();
                path = category.replaceAll(" ", "");
                getDataFromServer(path);
//                listAdapter = new DoctorGroupListViewAdapter(TransformActivity.this, "transform5");
//                newsListView.setAdapter(listAdapter);
                break;
            case R.id.button9:
                clickButton(R.id.button9);
                name = (Button)findViewById(R.id.button9);
                category = name.getText().toString();
                path = category.replaceAll(" ", "");
                getDataFromServer(path);
//                listAdapter = new DoctorGroupListViewAdapter(TransformActivity.this, "transform5");
//                newsListView.setAdapter(listAdapter);
                break;
            case R.id.button10:
                clickButton(R.id.button10);
                name = (Button)findViewById(R.id.button10);
                category = name.getText().toString();
                path = category.replaceAll(" ", "");
                getDataFromServer(path);
//                listAdapter = new DoctorGroupListViewAdapter(TransformActivity.this, "transform5");
//                newsListView.setAdapter(listAdapter);
                break;
            case R.id.button11:
                clickButton(R.id.button11);
                name = (Button)findViewById(R.id.button11);
                category = name.getText().toString();
                path = category.replaceAll(" ", "");
                getDataFromServer(path);
//                listAdapter = new DoctorGroupListViewAdapter(TransformActivity.this, "transform5");
//                newsListView.setAdapter(listAdapter);
                break;
            case R.id.button12:
                clickButton(R.id.button12);
                name = (Button)findViewById(R.id.button12);
                category = name.getText().toString();
                path = category.replaceAll(" ", "");
                getDataFromServer(path);
//                listAdapter = new DoctorGroupListViewAdapter(TransformActivity.this, "transform5");
//                newsListView.setAdapter(listAdapter);
                break;
        }
    }

    private void clickButton(int id) {
        for(int i = 0; i < buttonIds.length; i++) {
            if(buttonIds[i] != id) {
                findViewById(buttonIds[i]).setBackgroundResource(R.color.home_button);
            } else {
                findViewById(buttonIds[i]).setBackgroundResource(R.drawable.background_tab_button);
            }
        }
    }

    @Override
    public void response(String result) {
        ArrayList<String> test = new ArrayList<>();
        test.add("one");
        test.add("two");
        test.add("three");
        String[] tests = {"one", "two", "Three"};
        System.out.println("result from server &&&&&&&&&&&&&&&: " + result);
        dropdown.setVisibility(View.GONE);
        if(result != null) {
            try {
                JSONArray array = null;
                Object json = new JSONTokener(result).nextValue();
                if (json instanceof JSONObject) {
                    final JSONObject object = new JSONObject(result);
                    JSONArray subCategories = object.getJSONArray("subCategories");
                    if(subCategories.length() > 1) {
                        dropdown.setVisibility(View.VISIBLE);
                        final ArrayList<String> subCategoriesArray = new ArrayList<>();
                        for (int i = 0; i < subCategories.length(); i++) {
                            subCategoriesArray.add(subCategories.getString(i));
                        }
                        subCategoriesArray.add(0, "All");
                        System.out.print(subCategoriesArray);
                        final Button categorySpinner = (Button) findViewById(R.id.categorySpinner);
                        categorySpinner.setText("All");

                        final Dialog dialog1 = new Dialog(TransformActivity.this,0);
                        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog1.setContentView(R.layout.custom_spinner);
                        LinearLayout dialogWindow = (LinearLayout)dialog1.findViewById(R.id.dialogWindow);
                        ListView listView = (ListView)dialog1.findViewById(R.id.customSpinnerList);
                        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(TransformActivity.this, subCategoriesArray);
                        listView.setAdapter(adapter);
                        dialog1.setCanceledOnTouchOutside(true);
                        dialog1.setCancelable(true);
                        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        dialogWindow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.cancel();
                            }
                        });

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                try{
                                    categorySpinner.setText(subCategoriesArray.get(position));
                                    if(position == 0){
                                        JSONArray all = object.getJSONArray("transforms");
                                        initializeListView(all);
                                    }else {
//                                        JSONObject jsonObjectTemp = new JSONObject();
//                                        jsonObjectTemp.put("Empty", "Empty");
                                        JSONArray jsonArray = new JSONArray();
                                        JSONArray subArray = object.getJSONArray("transforms");
                                        for(int i=0; i<subArray.length(); i++){
                                            JSONObject jsonObject = subArray.getJSONObject(i);
                                            if(jsonObject.getString("subCategory").contains(subCategoriesArray.get(position))){
                                                jsonArray.put(jsonObject);
                                            }
//                                            else {
//                                                resArray.put(jsonObjectTemp);
//                                            }
                                        }


                                        resArray = jsonArray;
                                        initializeListView(resArray);
                                        serverArray = resArray;

                                    }
                                    dialog1.cancel();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        categorySpinner.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.show();
                            }
                        });
//                        Window window = dialog1.getWindow();
//                        WindowManager.LayoutParams wlp = window.getAttributes();
//
//                        wlp.gravity = Gravity.TOP;
//                        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//                        window.setAttributes(wlp);

//                        categorySpinner.setAdapter(new ArrayAdapter<String>(TransformActivity.this,
//                                android.R.layout.simple_spinner_dropdown_item,  test));
                    } else {
                        dropdown.setVisibility(View.GONE);
                    }

                    array = object.getJSONArray("transforms");
                    serverArray = array;
                } else if (json instanceof JSONArray) {

                    array = new JSONArray(result);
                    serverArray = array;
                    Log.i("ressssssssssssssssssss", "outside"+array);
                }


                tempResArray = array;


                Log.i("ressssssssssssssssssss", serverArray.length()+"final"+serverArray);
                initializeListView(array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private JSONArray reverse(JSONArray array){
        JSONArray jsonArray = new JSONArray();
        try{
            for(int i=10; i>0; i--){

            }
            for(int i=array.length()-1; i>=0; i--){
                jsonArray.put(array.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("lllllllllllllllores", array+"");
        Log.i("lllllllllllllllochange", jsonArray+"");
        return jsonArray;
    }

//    private void slideSocialMenu() {
//        System.out.println("coming here&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
//        RelativeLayout parentLayout = (RelativeLayout)findViewById(R.id.parentLayout);
//        final View child = getLayoutInflater().inflate(R.layout.slide_social_menu, null);
//        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in);
//        child.setAnimation(fadeInAnimation);
//        child.findViewById(R.id.transparentArea).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                child.setVisibility(View.GONE);
//            }
//        });
//        parentLayout.addView(child);
//    }
}
