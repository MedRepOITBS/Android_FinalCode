package com.app.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.location.Address;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.db.Company;
import com.app.db.DoctorProfile;
import com.app.pojo.SignIn;
import com.app.task.DoctorCreateAppointmentTask;
import com.app.util.HttpUrl;
import com.app.util.JSONParser;
import com.app.util.Utils;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import medrep.medrep.GPSService;
import medrep.medrep.R;

/**
 * Created by masood on 9/12/15.
 */
public class CallMedRepFragment extends Fragment implements View.OnClickListener {

    private TextView back;
    private SignIn signIn;
    private Button createAppointment;
    //    private Button timePicker;
    private static Button datePicker;
    private static Button timePicker;
    //    private static TextView dateTextSet;
    private String dateText;
    private String timeText;
    EditText anyother_loc_edittext;
    TextView anyother_location;

    private Spinner locationSpinner;
    private ProgressBar locationProgress;

//    private static final String APPOINTMENT_DESCRIPTION = "No Description";
private static final String APPOINTMENT_DESCRIPTION = "";
//    private InputMethodManager mInputMethodManager;
    private boolean useAnyOtherLocation = false;

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = appointmentTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTherapeuticName() {
        return therapeuticName;
    }

    public void setTherapeuticName(String therapeuticName) {
        this.therapeuticName = therapeuticName;
    }

    private int notificationID;
    private String appointmentTitle;
    private String companyName;
    private String therapeuticName;
    RelativeLayout lin_back;
//    EditText durationET, descriptionET;
    private Address address;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getActivity().getIntent().getExtras() != null)
            signIn = getActivity().getIntent().getExtras().getParcelable("signin");

        View v = inflater.inflate(R.layout.doctor_callmedrep, container, false);

        lin_back=(RelativeLayout)v.findViewById(R.id.lin_back);
        lin_back.setBackgroundColor(getResources().getColor(R.color.notification_header_color));

        locationSpinner = (Spinner) v.findViewById(R.id.select_address_spinner);
        locationProgress = (ProgressBar) v.findViewById(R.id.select_address_progress);

        back = (TextView) v.findViewById(R.id.back_tv);
        back.setOnClickListener(this);
        createAppointment = (Button) v.findViewById(R.id.Schedule_button);
        anyother_loc_edittext = (EditText) v.findViewById(R.id.anyother_loc_edittext);
        anyother_location= (TextView) v.findViewById(R.id.anyother_location);
        anyother_location.setVisibility(View.GONE);
        anyother_location.setOnClickListener(this);
        /*descriptionET = (EditText) v.findViewById(R.id.description);
        durationET = (EditText) v.findViewById(R.id.duration);

        descriptionET.requestFocus();*/

        createAppointment.setOnClickListener(this);

        datePicker = (Button) v.findViewById(R.id.Date_picker_button);
        datePicker.setOnClickListener(this);
        timePicker = (Button) v.findViewById(R.id.time_picker_button);
        timePicker.setOnClickListener(this);
        //  dateTextSet = (TextView)v.findViewById(R.id.calendartext);

        if(Utils.isNetworkAvailableWithOutDialog(getActivity())){
            //Get Locations and set these locations to spinner.
            task.execute();
        }else{
            Toast.makeText(getActivity(), "Not connected to internet. Cannot get locations.", Toast.LENGTH_SHORT).show();
        }


        ((TextView) v.findViewById(R.id.drug_name)).setText(getAppointmentTitle());
        ((TextView) v.findViewById(R.id.therapitic_name)).setText(getTherapeuticName());
        ((TextView) v.findViewById(R.id.company_name)).setText(getCompanyName());

        /*v.findViewById(R.id.temp_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Hello", Toast.LENGTH_SHORT).show();
            }
        });*/

        return v;
    }

    AsyncTask<Void, Void, DoctorProfile> task = new AsyncTask<Void, Void, DoctorProfile>() {

        @Override
        protected DoctorProfile doInBackground(Void... params) {
            String url = HttpUrl.BASEURL + HttpUrl.DOCTOR_GET_MY_DETAILS + Utils.GET_ACCESS_TOKEN(getActivity());

            System.out.println("url: " + url);

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = parser.getJSON_Response(url, true);

            DoctorProfile doctorProfile = null;
            try {
                doctorProfile = (DoctorProfile) parser.jsonParser(jsonObject, DoctorProfile.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return doctorProfile;
        }

        @Override
        protected void onPostExecute(DoctorProfile doctorProfile) {
            super.onPostExecute(doctorProfile);


            locationProgress.setVisibility(View.GONE);
            locationSpinner.setAdapter(new SpinnerAdapter(doctorProfile));

            locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    useAnyOtherLocation = false;

                    if(position == parent.getCount() - 1){

                        anyother_loc_edittext.setText("");

                        GPSService mGPSService = new GPSService(getActivity());
                        mGPSService.getLocation();

                        if (mGPSService.isLocationAvailable == false) {
                            Toast.makeText(getActivity(), "Your location is not available.", Toast.LENGTH_SHORT).show();
                        } else {
                            address = mGPSService.getLocationAddress();
                        }


                        mGPSService.closeGPS();
                        String addressLine2 = "";

//                        if (anyother_loc_edittext.getVisibility() == View.GONE) {
//                            anyother_loc_edittext.setVisibility(View.VISIBLE);

                            if(address != null){
                                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                                    if(i == 0){
                                        //  anyother_loc_edittext.setText(address.getAddressLine(i));
                                        anyother_loc_edittext.setText(address.getAddressLine(i)+"\n"+address.getLocality()+", "+ address.getAdminArea()+", "+address.getPostalCode());
                                    }else{
                                        addressLine2 = addressLine2 + address.getAddressLine(i);
                                    }
                                }
                                anyother_loc_edittext.append(", " + addressLine2);
                            }
                            useAnyOtherLocation = true;
                        /*}else if (anyother_loc_edittext.getVisibility() == View.VISIBLE){
                            anyother_loc_edittext.setVisibility(View.GONE);
                            anyother_loc_edittext.setText("");
                        }*/
                    }else if(anyother_loc_edittext != null && anyother_loc_edittext.getVisibility() == View.VISIBLE){
//                        anyother_loc_edittext.setVisibility(View.GONE);
                        Company.Location location = (Company.Location) parent.getItemAtPosition(position);

                        anyother_loc_edittext.setText(location.getAddress1() + "," +
                                location.getAddress2() + "," +
                                location.getCity() + "," +
                                location.getState() + "," +
                                location.getCountry() + "," +
                                location.getZipcode());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NotificationDetails.UPDATE_NOTIFICATION = false;
    }

    public void success(String sucess) {
//        Toast.makeText(getActivity(), "Sucess", Toast.LENGTH_SHORT).show();
//        getActivity().getSupportFragmentManager().popBackStack();
//        Utils.DISPLAY_GENERAL_DIALOG(getActivity(), "Success", "Appointment created successfully");
        goBack();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final ContentResolver resolver = getActivity().getContentResolver();
        builder.setTitle("Success")
                .setMessage("Appointment created successfully. Do you want sync this to your calendar?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createCalendarEvent(resolver);
                    }
                })
                .setNegativeButton("No", null);
        builder.create().show();
    }

    public void failure() {
//        Toast.makeText(getActivity(), "Appointment could not be created", Toast.LENGTH_SHORT).show();
        Utils.DISPLAY_GENERAL_DIALOG(getActivity(), "Failed", "Appointment could not be created, Please try again later.");
    }

    @Override
    public void onClick(View v) {
        if (v == back)
            goBack();
        else if (v == createAppointment) {

            System.out.println("dateText: " + dateText);
            System.out.println("timeText: " + timeText);
            System.out.println("locationSpinner.getSelectedItemPosition(): " + locationSpinner.getSelectedItemPosition());


            if (dateText == null || timeText == null) {
//                Toast.makeText(getActivity(), "Please set date and time", Toast.LENGTH_SHORT).show();
                Utils.DISPLAY_GENERAL_DIALOG(getActivity(), "No Date & Time", "Please set date and time");
                return;
            }else if(locationSpinner.getSelectedItemPosition() < 0){
                Utils.DISPLAY_GENERAL_DIALOG(getActivity(), "No Location Found", "Please select location or wait till it gets loaded.");
                return;
            }

            Map<String, String> comment = new HashMap<String, String>();
            comment.put("title", getAppointmentTitle());
            comment.put("appointmentDesc", APPOINTMENT_DESCRIPTION);
            comment.put("notificationId", getNotificationID() + "");
            comment.put("startDate", dateText + timeText);

//            String duration = durationET.getText().toString();

//            comment.put("duration", (duration != null && duration.trim().length() > 0) ? duration.trim() : "30");
//            comment.put("description", descriptionET.getText().toString());
            comment.put("status", "Pending");
            comment.put("doctorId", com.app.pojo.DoctorProfile.getInstance().getDoctorId() + "");
            comment.put("feedback", "Feedback");

            if(useAnyOtherLocation && anyother_loc_edittext != null && anyother_loc_edittext.getVisibility() == View.VISIBLE && anyother_loc_edittext.getText() != null){
                String location = anyother_loc_edittext.getText() + "";

                if(location != null && location.trim().length() > 0){
                    comment.put("location", location.trim());
                }else{

                    Company.Location tempLocation = (Company.Location) locationSpinner.getSelectedItem();
                    String locStr = tempLocation.getAddress1() + "\n" +
                            tempLocation.getAddress2() + ", " +
                            tempLocation.getCity() + ", " +
                            tempLocation.getState() + ", " +
                            tempLocation.getCountry() + ", " +
                            tempLocation.getZipcode();

                    comment.put("location", locStr);
                }
            }else{
                Company.Location tempLocation = (Company.Location) locationSpinner.getSelectedItem();
                String locStr = tempLocation.getAddress1() + "\n" +
                        tempLocation.getAddress2() + ", " +
                        tempLocation.getCity() + ", " +
                        tempLocation.getState() + ", " +
                        tempLocation.getCountry() + ", " +
                        tempLocation.getZipcode();

                comment.put("location", locStr);
            }


            String json = new GsonBuilder().create().toJson(comment, Map.class);

            System.out.println("json: " + json);

//            createCalendarEvent();


//            makeRequest("http://192.168.0.1:3000/post/77/comments", json);


           /* Appointment appointment = new Appointment();
            appointment.setDuration(30);
            appointment.setAppointmentDesc(APPOINTMENT_DESCRIPTION);
            appointment.setDoctorId(14);
            appointment.setStatus("New");
            appointment.setAppointmentId(null);
            appointment.setCreatedOn(formatter.format(new Date()));
            appointment.setLocation(null);
            appointment.setNotificationId(1);
            appointment.setFeedback("feedback");
            appointment.setStartDate(datetext);*/
            new DoctorCreateAppointmentTask(getActivity(), this, json).execute(HttpUrl.BASEURL + HttpUrl.CREATE_APPOINTMENT + Utils.GET_ACCESS_TOKEN(getActivity()));

        } else if (v == datePicker) {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
        } else if (v == timePicker) {
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
        } else if (v == anyother_location) {

        }
    }

    private void goBack() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void createCalendarEvent(ContentResolver resolver) {
        Calendar beginTime = Calendar.getInstance(TimeZone.getDefault());
        beginTime.set(Integer.parseInt(dateText.substring(0, 4)),
                Integer.parseInt(dateText.substring(4, 6)) - 1,
                Integer.parseInt(dateText.substring(6, dateText.length())),
                Integer.parseInt(timeText.substring(0,2)),
                Integer.parseInt(timeText.substring(2, 4)),
                00);

        //Time format HHMMSS

        /*String[] date = dateText.split("-");

        String time[] = timeText.split(":");*/
//        beginTime.set();

       /*Calendar beginTime = Calendar.getInstance();
        beginTime.set(yearInt, monthInt - 1, dayInt, 7, 30);*/

        String title = getAppointmentTitle();

        if(title == null || title.trim().length() == 0 || title.equalsIgnoreCase("no title")){
            title = "MedRep Event";
        }


        ContentValues eventValues = new ContentValues();
        eventValues.put("calendar_id", 1);
        eventValues.put("title", title);
        eventValues.put("description", APPOINTMENT_DESCRIPTION);


        String location;

        if(useAnyOtherLocation && anyother_loc_edittext != null && anyother_loc_edittext.getVisibility() == View.VISIBLE && anyother_loc_edittext.getText() != null){
            location = anyother_loc_edittext.getText() + "";

            if(location != null && location.trim().length() > 0){
//                comment.put("location", location.trim());
            }else{

                Company.Location tempLocation = (Company.Location) locationSpinner.getSelectedItem();
                location = tempLocation.getAddress1() + "\n" +
                        tempLocation.getAddress2() + ", " +
                        tempLocation.getCity() + ", " +
                        tempLocation.getState() + ", " +
                        tempLocation.getCountry() + ", " +
                        tempLocation.getZipcode();
            }
        }else{
            Company.Location tempLocation = (Company.Location) locationSpinner.getSelectedItem();
            location = tempLocation.getAddress1() + "\n" +
                    tempLocation.getAddress2() + ", " +
                    tempLocation.getCity() + ", " +
                    tempLocation.getState() + ", " +
                    tempLocation.getCountry() + ", " +
                    tempLocation.getZipcode();
        }


        eventValues.put("eventLocation", location);
        eventValues.put("dtstart", beginTime.getTimeInMillis());

//        String durationStr = durationET.getText().toString();

//        int duration = Integer.parseInt((durationStr != null && durationStr.trim().length() > 0)?durationStr.trim():"30");

        eventValues.put("dtend", beginTime.getTimeInMillis() + (30 * 60 * 1000));
        eventValues.put("allDay", 0);
        eventValues.put("eventTimezone", TimeZone.getDefault().getID());
//        eventValues.put("rrule", "FREQ=YEARLY");
        // status: 0~ tentative; 1~ confirmed; 2~ canceled
        // eventValues.put("eventStatus", 1);

//        eventValues.put("eventTimezone", "India");
        Uri l_eventUri;
        if (Build.VERSION.SDK_INT >= 8) {
            l_eventUri = Uri.parse("content://com.android.calendar/events");
        } else {
            l_eventUri = Uri.parse("content://calendar/events");
        }
        Uri l_uri = resolver.insert(l_eventUri, eventValues);


        /*if (Build.VERSION.SDK_INT >= 14) {
            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis())
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.getTimeInMillis() + (30 * 60 * 1000))
                    .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
                    .putExtra(CalendarContract.Events.TITLE, getAppointmentTitle())
                    .putExtra(CalendarContract.Events.DESCRIPTION, APPOINTMENT_DESCRIPTION)
                    .putExtra(CalendarContract.Events.EVENT_LOCATION, locationSpinner.getSelectedItem().toString())
                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
            System.out.println("intent == null: " + intent == null);

            activity.startActivity(intent);
        } else {
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setType("vnd.android.cursor.item/eventValues");
            intent.putExtra("beginTime", cal.getTimeInMillis());
            intent.putExtra("allDay", true);
            intent.putExtra("title", getAppointmentTitle());
            intent.putExtra("description", APPOINTMENT_DESCRIPTION);
            intent.putExtra("eventLocation", locationSpinner.getSelectedItem().toString());
            intent.putExtra("endTime", cal.getTimeInMillis() + 30 * 60 * 1000);

            activity.startActivity(intent);
        }*/
    }


    public class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it

            TimePickerDialog timePic = new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));

            return timePic;
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


            final Calendar c = Calendar.getInstance();

            Format formatter = new SimpleDateFormat("yyyyMMdd");

            String temp = formatter.format(c.getTime());

            System.out.println("temp: " + temp);
            System.out.println("dateText: " + dateText);


            if(temp.trim().equals(dateText)){
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minutes = c.get(Calendar.MINUTE);

                if(hourOfDay <= hour && minute <= minutes){
                    Utils.DISPLAY_GENERAL_DIALOG(getActivity(), "Invalid Entry", "Time can not be less than current Time");
                    return;
                }
            }
            String time = hourOfDay + ":" + minute;

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                Date dateObj = sdf.parse(time);
                System.out.println(dateObj);
                System.out.println();
                new SimpleDateFormat("H:mm").format(dateObj);
                timePicker.setText(new SimpleDateFormat("H:mm").format(dateObj));
            } catch (final ParseException e) {
                e.printStackTrace();
            }

            //Time format HHMMSS

            timeText = ((hourOfDay < 10)?("0" + hourOfDay):hourOfDay) + "" + ((minute < 10)?("0" + minute):minute) + "00";

//            System.out.println("Kishore: " + dateText + timeText);

//            Toast.makeText(getActivity(), "Kishore: " + dateText + timeText, Toast.LENGTH_SHORT).show();

        }
    }

    public class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog datePic = new DatePickerDialog(getActivity(), this, year, month, day);

            datePic.getDatePicker().setMinDate(c.getTimeInMillis());

            return datePic;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            datePicker.setText(day + "-" + (month + 1) + "-" + year);
            //Date format YYYYMMDD
            dateText = year + "" + month + "" + day;

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);

            Format formatter = new SimpleDateFormat("yyyyMMdd");

            dateText = formatter.format(calendar.getTime());

            //System.out.println("datetext" + dateText);
        }
    }

    /**
     * Adapter to set location list
     */
    private class SpinnerAdapter extends BaseAdapter {
        private final ArrayList<Company.Location> locations;
//        private String[] location = {"Location1", "Location2", "Location3"};

        public SpinnerAdapter(DoctorProfile doctorProfile) {
            locations = doctorProfile.getLocations();
        }

        @Override
        public int getCount() {
            return locations.size() + 1;
        }


        public Company.Location getItem(int position) {
            if(position < locations.size()){
                return locations.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                convertView = inflater.inflate(R.layout.location_spinner_text, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if(position < locations.size()){
                Company.Location location = locations.get(position);

                viewHolder.textView.setText(location.getAddress1() + "\n" +
                        location.getAddress2() + "\n" +
                        location.getCity() + "\n" +
                        location.getState() + "\n" +
                        location.getCountry() + "\n" +
                        location.getZipcode());
            }else{
                viewHolder.textView.setText("Current Location");
            }


            return convertView;
        }

        private class ViewHolder {
            TextView textView;
        }
    }

}
