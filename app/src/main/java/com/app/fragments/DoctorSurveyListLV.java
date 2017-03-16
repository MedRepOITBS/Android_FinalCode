package com.app.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.interfaces.GetResponse;
import com.app.pojo.SignIn;
import com.app.pojo.Survery;
import com.app.pojo.SurveryList;
import com.app.task.NotificationGetTask;
import com.app.util.HttpUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import medrep.medrep.R;

/**
 * Created by masood on 9/11/15.
 */
public class DoctorSurveyListLV extends Fragment implements
        AdapterView.OnItemClickListener,
        View.OnClickListener, GetResponse{

    private ListView surveysLV;
    private ImageView back;

    private SurveryList surveyList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.doc_survey_list, container, false);
        surveysLV = (ListView) v.findViewById(R.id.survey_list);
        surveysLV.setOnItemClickListener(this);
        back = (ImageView) v.findViewById(R.id.back);
        back.setOnClickListener(this);

        v.findViewById(R.id.bell_image).setVisibility(View.GONE);
        ((TextView) v.findViewById(R.id.notification_company)).setText("Surveys");

        return v;
    }

    @Override
    public void onClick(View v) {
        goBack();
    }

    public void goBack(){
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setSurveys();
    }

    private void setSurveys() {
        /*int[] resultIDS = MedRepDatabaseHandler.getInstance(getActivity()).
                getNotificationIDs(notificationIDS, (int) spinner.getSelectedItemId());*/

       /* detailedNotifications =
                MedRepDatabaseHandler.getInstance(getActivity()).getDetailedNotification(resultIDS);*/
        ArrayList<Survery> surveysList = surveyList.getSurveryArrayList();
        System.out.println("surveysList " + surveysList.size());
        if(surveysList != null && surveysList.size() > 0){
            SurveysAdapter surveysAdapter  = new SurveysAdapter(surveysList);
            surveysLV.setAdapter(surveysAdapter);
        }else{
            Toast.makeText(getActivity(), "No surveys available.", Toast.LENGTH_SHORT).show();
            goBack();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Survery survery = (Survery) parent.getItemAtPosition(position);

        String url = survery.getSurveyUrl();

        //Open url in webview activity.
        DoctorSurveyFragment surveyFragment = new DoctorSurveyFragment();



        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, surveyFragment);
        fragmentTransaction.addToBackStack(null);
        surveyFragment.setUrl(url);
        fragmentTransaction.commit();

        /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);*/
    }

    public void setSurveyList(SurveryList surveyList) {
        this.surveyList = surveyList;
    }

    @Override
    public void response(String result) {
        System.out.println("result" + result);
        try {
            JSONObject object = new JSONObject(result);
            String status = object.getString("status");
            //if(status.equals("success")) {
                String resultString = object.getString("result");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Medrep")
                        .setMessage(resultString)
                        .setCancelable(false)
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
//            } else {
//
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private final class SurveysAdapter extends BaseAdapter{

        private ArrayList<Survery> surveysList;

        public SurveysAdapter(ArrayList<Survery> surveysList) {
            this.surveysList = surveysList;
        }

        @Override
        public int getCount() {
            return surveysList.size();
        }


        public Survery getItem(int position) {
            return surveysList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return surveysList.get(position).getSurveyId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                convertView = inflater.inflate(R.layout.row_notification, parent, false);
                holder = new ViewHolder();
                holder.titleTextView = (TextView)convertView.findViewById(R.id.notification_title);
                holder.descTextView = (TextView)convertView.findViewById(R.id.notification_desc);
                holder.ticketID_TextView = (TextView)convertView.findViewById(R.id.ticket_id);
                ImageView download = (ImageView)convertView.findViewById(R.id.download);
                download.setVisibility(View.VISIBLE);
                ImageView rightArrow = (ImageView)convertView.findViewById(R.id.rightArrow);
                rightArrow.setVisibility(View.VISIBLE);
                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        System.out.println("coming here$$$$$$$$$$");
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Medrep")
                        .setMessage("Do you want to request a Report for this survey")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        int surveyId = new Survery().getSurveyId();
                                        int doctorId = new Survery().getDoctorId();
                                        String url = HttpUrl.COMMONURL + "/survey/getReport?token="
                                                + SignIn.GET_ACCESS_TOKEN() + "&format=pdf&repId=0"
                                                + "&doctorId=" + doctorId + "&surveyId=" + surveyId;
                                        NotificationGetTask notificationGetTask = new NotificationGetTask();
                                        notificationGetTask.delegate = (GetResponse) DoctorSurveyListLV.this;
                                        notificationGetTask.execute(url);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                });
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            Survery survery = surveysList.get(position);

            String title = survery.getSurveyTitle();
            String description = survery.getSurveyDescription();

            title = (title != null && title.trim().length() !=0)?title.trim():"No Title";
            description = (description != null && description.trim().length() !=0)?description.trim():"No Description";

            holder.titleTextView.setText(title);
            holder.descTextView.setText(description);
            holder.ticketID_TextView.setText(title.charAt(0) + "");
            return convertView;
        }


        class ViewHolder{
            TextView titleTextView;
            TextView descTextView;
            TextView ticketID_TextView;
            ImageView rightArrowImageView;
        }
    }
}
