package medrep.medrep;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SyncAdapterType;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.adapter.CommonAdapter;
import com.app.adapter.DoctorGroupListViewAdapter;
import com.app.interfaces.GetResponse;
import com.app.pojo.SignIn;
import com.app.task.NotificationGetTask;
import com.app.util.HttpUrl;
import com.app.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class TransformActivity extends AppCompatActivity implements View.OnClickListener, GetResponse{

//    private String[] news = {"Colon cancer varies by birth place", "Its not too late to get flu shot", "Best cancer screening methods",
//            "Best Heart Surgery Operations", "Best Hospitals", "Colon cancer varies by birth place", "Its not too late to get flu shot", "Best cancer screening methods"};
    private int[] buttonIds;
    private ListView newsListView;
    private JSONArray serverArray;
    private DoctorGroupListViewAdapter listAdapter;
    private LinearLayout dropdown;

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
        Button serve = (Button)findViewById(R.id.serve);
        serve.setOnClickListener(this);
        Button onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(this);
        LinearLayout addLayout = (LinearLayout)findViewById(R.id.addLayout);
        addLayout.setVisibility(View.GONE);
        Button transform = (Button)findViewById(R.id.transform);
        transform.setBackground(getResources().getDrawable(R.drawable.border_button_light));
        LinearLayout listLayout = (LinearLayout)findViewById(R.id.listLayout);
        listLayout.getBackground().setAlpha(120);

        newsListView = (ListView)findViewById(R.id.newsListView);
//        listAdapter = new DoctorGroupListViewAdapter(this, "transformActivity");
//        newsListView.setAdapter(listAdapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TransformActivity.this, TransformDetailActivity.class);
                try {
                    JSONObject object = serverArray.getJSONObject(position);
                    String title = object.getString("title");
                    String description = object.getString("newsDesc");
                    intent.putExtra("description", description);
                    intent.putExtra("newsTitle", title);
                    if(position == 1 || position == 3 || position == 7) {
                        intent.putExtra("video", "video");
                    } else if(position == 2) {
                        intent.putExtra("pdf", "pdf");
                    }
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

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
        CommonAdapter adapter = new CommonAdapter(this, array);
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
                tv.setBackgroundResource(R.color.home_highted);
            }
            tv.setText(names[i]);
            tv.setId(buttonIds[i]);
            tv.setTextColor(getResources().getColor(R.color.tect_color_white));
            tv.setTextSize(10f);
            tv.setPadding(10, 0, 10, 0);
            tv.setAllCaps(false);
            tv.setLayoutParams(lparams);
            horizontalLayout.addView(tv);
            final int j = i;
            tv.setOnClickListener(this);
        }
    }

    private void getDataFromServer(String category) {
        String ip = HttpUrl.COMMONURL;
        String accessToken = SignIn.GET_ACCESS_TOKEN();
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
        dropdown = (LinearLayout)findViewById(R.id.dropdown);
        dropdown.setVisibility(View.GONE);
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
                intent = new Intent(this, ShareActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
//            case R.id.slideButton:
//                slideSocialMenu();
//                break;
            case R.id.button1:
                clickButton(R.id.button1);
                //getDataFromServer("");
                //listAdapter = new DoctorGroupListViewAdapter(TransformActivity.this, "transform1");
                //newsListView.setAdapter(listAdapter);
                break;
            case R.id.button2:
                clickButton(R.id.button2);
                name = (Button)findViewById(R.id.button2);
                category = name.getText().toString();
                path = category.replaceAll(" ", "%20");
                getDataFromServer(path);
//                listAdapter = new DoctorGroupListViewAdapter(TransformActivity.this, "transform4");
//                newsListView.setAdapter(listAdapter);
                break;
            case R.id.button3:
                clickButton(R.id.button3);
                name = (Button)findViewById(R.id.button3);
                category = name.getText().toString();
                path = category.replaceAll(" ", "%20");
                getDataFromServer(path);
//                listAdapter = new DoctorGroupListViewAdapter(TransformActivity.this, "transform2");
//                newsListView.setAdapter(listAdapter);
                break;
            case R.id.button4:
                clickButton(R.id.button4);
                name = (Button)findViewById(R.id.button4);
                category = name.getText().toString();
                path = category.replaceAll(" ", "%20");
                getDataFromServer(path);
//                listAdapter = new DoctorGroupListViewAdapter(TransformActivity.this, "transformActivity");
//                newsListView.setAdapter(listAdapter);
                break;
            case R.id.button5:
                clickButton(R.id.button5);
                name = (Button)findViewById(R.id.button5);
                category = name.getText().toString();
                path = category.replaceAll(" ", "%20");
                getDataFromServer(path);
//                listAdapter = new DoctorGroupListViewAdapter(TransformActivity.this, "transform3");
//                newsListView.setAdapter(listAdapter);
                break;
            case R.id.button6:
                clickButton(R.id.button6);
                name = (Button)findViewById(R.id.button6);
                category = name.getText().toString();
                path = category.replaceAll(" ", "%20");
                getDataFromServer(path);
//                listAdapter = new DoctorGroupListViewAdapter(TransformActivity.this, "transform4");
//                newsListView.setAdapter(listAdapter);
                break;
            case R.id.button7:
                clickButton(R.id.button7);
                name = (Button)findViewById(R.id.button7);
                category = name.getText().toString();
                path = category.replaceAll(" ", "%20");
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
                findViewById(buttonIds[i]).setBackgroundResource(R.color.home_highted);
            }
        }
    }

    @Override
    public void response(String result) {
        //System.out.println("result from server &&&&: " + result);
        if(result != null) {
            try {
                JSONArray array = null;
                Object json = new JSONTokener(result).nextValue();
                if (json instanceof JSONObject) {
                    JSONObject object = new JSONObject(result);
                    JSONArray subCategories = object.getJSONArray("subCategories");
                    if(subCategories.length() > 0) {
                        dropdown.setVisibility(View.VISIBLE);
                        ArrayList<String> subCategoriesArray = new ArrayList<>();
                        for (int i = 0; i < subCategories.length(); i++) {
                            subCategoriesArray.add(subCategories.getString(i));
                        }
                        System.out.print(subCategoriesArray);
                        Spinner categorySpinner = (Spinner)findViewById(R.id.categorySpinner);
                        categorySpinner.setAdapter(new ArrayAdapter<String>(TransformActivity.this,
                                android.R.layout.simple_spinner_dropdown_item,  subCategoriesArray));
                    } else {
                        dropdown.setVisibility(View.GONE);
                    }

                    array = object.getJSONArray("transforms");
                } else if (json instanceof JSONArray) {
                    array = new JSONArray(result);
                    serverArray = array;
                }
                initializeListView(array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

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
