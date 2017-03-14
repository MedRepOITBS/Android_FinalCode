package com.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.interfaces.GetResponse;
import com.app.task.SurveyReportsGetAsyncTask;
import com.app.util.HttpUrl;
import com.app.util.Utils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import medrep.medrep.LatestSurveyReportsActivity;
import medrep.medrep.R;
import medrep.medrep.SurveyDetailsActivity;
import pharma.PharmaDashBoard;

/**
 * Created by Gunasekhar on 01/03/17.
 */

public class SurveyListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<HashMap<String, String>> values;

    public SurveyListAdapter(Context context, ArrayList<HashMap<String, String>> values) {
        this.context = context;
        this.values = values;
    }
    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(context instanceof LatestSurveyReportsActivity) {
            convertView = layoutInflater.inflate(R.layout.latest_survey_custom_view, null);
            TextView titleFirstLetter = (TextView)convertView.findViewById(R.id.titleFirstLetter);
            TextView surveyTitle = (TextView)convertView.findViewById(R.id.survey_title);
            TextView surveyDescription = (TextView)convertView.findViewById(R.id.survey_description);
            String surveyTitleValue = values.get(position).get("surveyTitle");
            String surveyDescValue = values.get(position).get("surveyDescription");
            titleFirstLetter.setText(surveyTitleValue.charAt(0) + "");
            GradientDrawable bgShape = (GradientDrawable)titleFirstLetter.getBackground();
            int[] colors = {R.color.yellow, R.color.red, R.color.pink, R.color.home_highted, R.color.notification_header_color, R.color.l_blue, R.color.dialog_button, R.color.pharma_actscor_blue03};
            Random random = new Random();
            int index = random.nextInt(colors.length);
            System.out.println(index);
            bgShape.setColor(context.getResources().getColor(colors[index]));
            surveyTitle.setText(surveyTitleValue);
            surveyDescription.setText(surveyDescValue);
            ImageView coludImage = (ImageView)convertView.findViewById(R.id.coludImage);
            String reportsAvailable = values.get(position).get("reportsAvailable");
            if(reportsAvailable.equals("true")) {
                coludImage.setImageResource(R.mipmap.download_from_cloud);
                coludImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("on click it is firing ************");
                    }
                });
            }
        } else if (context instanceof SurveyDetailsActivity) {
            convertView = layoutInflater.inflate(R.layout.doctor_pending_list, null);
            TextView doctorName = (TextView)convertView.findViewById(R.id.doctor_name);
            Button remindStatus = (Button)convertView.findViewById(R.id.remind_status);
            String doctorNameValue = values.get(position).get("displayName");
            String remindStatusValue = values.get(position).get("remindNotification");
            doctorName.setText(doctorNameValue);
            if(remindStatusValue.equals("false")) {
                remindStatus.setText("Reminder Sent");
                remindStatus.setBackgroundColor(context.getResources().getColor(R.color.pharma_actscor_blue03));
                remindStatus.setClickable(false);
            } else {
                remindStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String surveyId = values.get(position).get("surveyId");
                        String doctorId = values.get(position).get("doctorId");
                        //get data from server
                        SurveyReportsGetAsyncTask surveyReportsGetAsyncTask = new SurveyReportsGetAsyncTask((GetResponse) context);
                        String baseUrl = HttpUrl.COMMONURL;
                        String url = baseUrl + "/pharmarep/sendReminder/" + surveyId + "/" + doctorId;
                        surveyReportsGetAsyncTask.execute(url);
                    }
                });
            }
        }

        return convertView;
    }
}
