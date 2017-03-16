package com.app.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.app.pojo.Appointment;
import com.app.pojo.AppointmentList;
import com.app.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import medrep.medrep.DoctorGetAppointmentDetails;
import medrep.medrep.R;

/**
 * Created by masood on 9/12/15.
 */
public class DoctorGetAppointmentListv extends Fragment implements AdapterView.OnItemClickListener {

    AppointmentList appointmentList;
    ListView listView;
    AppointmentListAdapter adapter;
    String datetime;
    SimpleDateFormat monthParse;
    int year;
    int month;
    int date;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            appointmentList = getArguments().getParcelable("appointment");
        }
        View v = inflater.inflate(R.layout.doc_getappoinment_listv, container, false);
        listView = (ListView) v.findViewById(R.id.listview);
        listView.setOnItemClickListener(this);

        v.findViewById(R.id.lin_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        return v;
    }

    public void goBack(){
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<Appointment> list = appointmentList.getAppointmentArrayList();
        adapter = new AppointmentListAdapter(list);
       /* for (int count= 0; count < list.size(); count++){
            Log.d("TAG", "my parcelable data is "+list.get(count).getCreatedOn());
        }*/
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Appointment appointment = adapter.getItem(position);
        Log.d("TAG", "Checking my appointment  are " + appointment);

        //change to Appointment detail  class
        DoctorGetAppointmentDetails fragment = new DoctorGetAppointmentDetails();
        fragment.setAppointment(appointment, view);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


    private class AppointmentListAdapter extends BaseAdapter {

        ArrayList<Appointment> list;

        AppointmentListAdapter(ArrayList<Appointment> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Appointment getItem(int position) {
            return list.get(position);
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
                convertView = inflater.inflate(R.layout.getappointment_listitem, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                viewHolder.month = (TextView) convertView.findViewById(R.id.month_tv);
                viewHolder.date = (TextView) convertView.findViewById(R.id.date_tv);
                viewHolder.nameDesignation = (TextView) convertView.findViewById(R.id.name_designation);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            datetime = list.get(position).getStartDate();

            year = Integer.parseInt(datetime.substring(0, 4));
            month = Integer.parseInt(datetime.substring(4, 6));
            date = Integer.parseInt(datetime.substring(6, 8));

            try {

                Appointment appointment = list.get(position);


//                viewHolder.name.setText(appointment.getTitle());
                viewHolder.name.setText(appointment.getCompanyName());
                viewHolder.nameDesignation.setText(appointment.getAppointmentDesc());
                viewHolder.month.setText(Utils.formatMonth(month).substring(0,3).toUpperCase());
                viewHolder.date.setText(date + "");

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }

    class ViewHolder {
        TextView name, month, date,nameDesignation;
    }
}
