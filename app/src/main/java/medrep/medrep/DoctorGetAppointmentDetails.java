package medrep.medrep;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.db.DoctorProfile;
import com.app.db.MedRepDatabaseHandler;
import com.app.pojo.Appointment;
import com.app.task.DoctorCreateAppointmentTask;
import com.app.util.HttpUrl;
import com.app.util.Utils;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


public class DoctorGetAppointmentDetails extends Fragment{

    private Appointment appointment;
//    private TextView textView,back;
    private TextView companyName;
    private TextView drugName;
    private TextView therapiticName;

    private Button datePickerButton;
    private Button timePickerButton;

    private Button button;
    private String dateText;
    private String timeText;
    private View view;


    public DoctorGetAppointmentDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public void setAppointment(Appointment appointment, View view){
        this.appointment = appointment;
        this.view = view;
    }

    private Appointment getAppointment(){
        return appointment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        MedRepDatabaseHandler dbHandler = MedRepDatabaseHandler.getInstance(getActivity());

        View v = inflater.inflate(R.layout.doctor_callmedrep, container, false);
        drugName = (TextView)v.findViewById(R.id.drug_name);
        drugName.setText(appointment.getTitle());

        ((TextView)v.findViewById(R.id.back_tv)).setText("Appointment Details");

        v.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        therapiticName = (TextView)v.findViewById(R.id.therapitic_name);
        therapiticName.setText(dbHandler.getTherapeuticName(appointment.getNotificationId()));

        companyName = (TextView)v.findViewById(R.id.company_name);
        companyName.setText(dbHandler.getCompanyName(appointment.getNotificationId()));
     //   textView = (TextView)v.findViewById(R.id.locaton_name);

        String dateTime = appointment.getStartDate();

        dateText = dateTime.substring(0, 8);
        timeText = dateTime.substring(8);

        String date = dateTime.substring(0, 4) + "-" + dateTime.substring(4, 6) + "-" + dateTime.substring(6, 8);
        String time = dateTime.substring(8, 10) + ":" + dateTime.substring(10, 12);

        datePickerButton = (Button) v.findViewById(R.id.Date_picker_button);
        datePickerButton.setClickable(false);
        datePickerButton.setText(date);
        /*datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });*/

        timePickerButton = (Button) v.findViewById(R.id.time_picker_button);
        timePickerButton.setClickable(false);
        timePickerButton.setText(time);
        /*timePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
            }
        });*/


        button = (Button)v.findViewById(R.id.Schedule_button);
        /*
        Change by Kishore
        Date: Nov 5, 2015.
        'Update Schedule' has been changed to 'Back' as per requested changes mentioned in mail(From samara at Wed, Nov 4, 2015 at 9:49 PM)
         */
//        button.setText("Update Schedule");
        button.setText("Back");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                updateAppointment();
                goBack();
            }
        });

        v.findViewById(R.id.select_address_spinner).setEnabled(false);
        v.findViewById(R.id.select_address_progress).setVisibility(View.GONE);

        EditText addressET = (EditText) v.findViewById(R.id.anyother_loc_edittext);
//        addressET.setVisibility(View.VISIBLE);
        addressET.setText(appointment.getLocation());
        addressET.setEnabled(false);

        return v;
    }

    private void updateAppointment() {
        if (dateText == null || timeText == null) {
            Toast.makeText(getActivity(), "Please set date and time", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> comment = new HashMap<String, String>();
        comment.put("appointmentId", appointment.getAppointmentId() + "");
        comment.put("title", appointment.getTitle());
        comment.put("appointmentDesc", appointment.getAppointmentDesc());
        comment.put("notificationId", appointment.getNotificationId() + "");
        comment.put("startDate", dateText + timeText);
        comment.put("duration", "30");
        comment.put("status", "Update");
        comment.put("doctorId", com.app.pojo.DoctorProfile.getInstance().getDoctorId() + "");
        comment.put("feedback", "Feedback");
        comment.put("location", appointment.getLocation());
        String json = new GsonBuilder().create().toJson(comment, Map.class);

        appointment.setStartDate(dateText + timeText);
        appointment.setStatus("Update");
        appointment.setFeedback("Feedback");

        view.invalidate();

        new DoctorCreateAppointmentTask(getActivity(), DoctorGetAppointmentDetails.this, json).execute(HttpUrl.BASEURL + HttpUrl.UPDATE_APPOINTMENT + Utils.GET_ACCESS_TOKEN(getActivity()));
    }

    public void success(String sucess) {
//        Toast.makeText(getActivity(), "Sucess", Toast.LENGTH_SHORT).show();
//        getActivity().getSupportFragmentManager().popBackStack();
//        Utils.DISPLAY_GENERAL_DIALOG(getActivity(), "Success", "Appointment created successfully");
        goBack();
        ((DoctorDashboard)getActivity()).getAppointmentsList();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final ContentResolver resolver = getActivity().getContentResolver();
        builder.setTitle("Success")
                .setMessage("Appointment updated successfully. Do you want sync this to your calendar?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        view.invalidate();
                        createCalendarEvent(resolver);
                    }
                })
                .setNegativeButton("No", null);
        builder.create().show();
    }

    private void createCalendarEvent(ContentResolver resolver) {
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(Integer.parseInt(dateText.substring(0, 4)),
                Integer.parseInt(dateText.substring(4, 6)) - 1,
                Integer.parseInt(dateText.substring(6, dateText.length())));

        String[] date = dateText.split("-");

        String time[] = timeText.split(":");
//        beginTime.set();

       /*Calendar beginTime = Calendar.getInstance();
        beginTime.set(yearInt, monthInt - 1, dayInt, 7, 30);*/

        String title = appointment.getTitle();

        if(title == null || title.trim().length() == 0 || title.equalsIgnoreCase("no title")){
            title = "MedRep Event";
        }


        ContentValues eventValues = new ContentValues();
        eventValues.put("calendar_id", 1);
        eventValues.put("title", title);
        eventValues.put("description", appointment.getAppointmentDesc());
        eventValues.put("eventLocation", appointment.getLocation());
        eventValues.put("dtstart", beginTime.getTimeInMillis());
        eventValues.put("dtend", beginTime.getTimeInMillis() + (30 * 60 * 1000));
        eventValues.put("allDay", 1);
        eventValues.put("eventTimezone", TimeZone.getDefault().getID());
//        eventValues.put("rrule", "FREQ=YEARLY");
        // status: 0~ tentative; 1~ confirmed; 2~ canceled
        // eventValues.put("eventStatus", 1);

        eventValues.put("eventTimezone", "India");
        Uri l_eventUri;
        if (Build.VERSION.SDK_INT >= 8) {
            l_eventUri = Uri.parse("content://com.android.calendar/events");
        } else {
            l_eventUri = Uri.parse("content://calendar/events");
        }
        Uri l_uri = resolver.insert(l_eventUri, eventValues);
    }

    private void goBack() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    public void failure() {
//        Toast.makeText(getActivity(), "Appointment could not be created", Toast.LENGTH_SHORT).show();
        Utils.DISPLAY_GENERAL_DIALOG(getActivity(), "Failed", "Appointment could not be updated, Please try again later.");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Log.d("TAG", "My appointments are " + appointment.getLocation() + textView);
    //    textView.setText(appointment.getLocation());
    }

    @Override
    public void onDetach() {
        super.onDetach();

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
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


            String time = hourOfDay + ":" + minute;

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                Date dateObj = sdf.parse(time);
                System.out.println(dateObj);
                System.out.println(new SimpleDateFormat("H:mm").format(dateObj));
                timePickerButton.setText(new SimpleDateFormat("H:mm").format(dateObj));
            } catch (final ParseException e) {
                e.printStackTrace();
            }



            //Time format HHMMSS
            timeText = hourOfDay + "" + minute + "00";
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
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            datePickerButton.setText(day + "-" + (month + 1) + "-" + year);
            //Date format YYYYMMDD
            dateText = year + "" + month + "" + day;

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);

            Format formatter = new SimpleDateFormat("yyyyMMdd");

            dateText = formatter.format(calendar.getTime());

            System.out.println("datetext" + dateText);
        }
    }
}
