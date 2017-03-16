package pharma;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.db.DoctorProfile;
import com.app.util.HttpUrl;
import com.app.util.JSONParser;
import com.app.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import medrep.medrep.R;
import pharma.model.Appointment;

/**
 * Created by kishore on 1/11/15.
 */
public class AppointmentsAdapter extends BaseAdapter {

    Activity activity;

    /**
     * Appointments
     */
    ArrayList<Appointment> appointments;

    public AppointmentsAdapter(Activity activity, ArrayList<Appointment> appointments) {
        this.activity = activity;
        this.appointments = appointments;
    }

    @Override
    public int getCount() {
        return appointments.size();
    }

    @Override
    public Appointment getItem(int position) {
        return appointments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return appointments.get(position).getAppointmentId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            convertView = inflater.inflate(R.layout.pharma_converted_appointment_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.titleTV = (TextView) convertView.findViewById(R.id.title_tv);
            viewHolder.timeTV = (TextView) convertView.findViewById(R.id.tv_timing);
            viewHolder.locationTV = (TextView) convertView.findViewById(R.id.location_tv);
            viewHolder.pharmaProfilePic = (ImageView) convertView.findViewById(R.id.pharma_profilepic);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        try {
            Appointment appointment = appointments.get(position);

            String datetime = appointment.getStartDate();

            if(datetime != null && datetime.trim().length() > 0){
                int hour = Integer.parseInt(datetime.substring(8, 10));
                int minute = Integer.parseInt(datetime.substring(10, 12));

                int year = Integer.parseInt(datetime.substring(0, 4));
                int month = Integer.parseInt(datetime.substring(4, 6));
                int date = Integer.parseInt(datetime.substring(6, 8));

                String time =hour + ":" + minute + (hour < 12?"am":"pm");

                String mon = Utils.formatMonth(month).substring(0,3).toUpperCase().toString();
                viewHolder.timeTV.setText(mon+" "+date+" "+time);
            }else{
                viewHolder.timeTV.setVisibility(View.INVISIBLE);
            }

            String title = appointment.getDoctorName();

            if(title != null && title.trim().length() > 0){
                viewHolder.titleTV.setText(title.trim());
            }else{
                viewHolder.titleTV.setText("No Title");
            }

            final ViewHolder finalViewHolder = viewHolder;

            AsyncTask<Integer, Void, Bitmap> task = new AsyncTask<Integer, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Integer... params) {
                    String url = HttpUrl.PHARMA_GET_DOCTOR_PROFILE + params[0] +
                            "?access_token=" +
                            Utils.GET_ACCESS_TOKEN(activity);
                    JSONParser parser = new JSONParser();
                    JSONObject jsonObject = parser.getJSON_Response(url, true);

                    DoctorProfile doctorProfile = null;
                    try {
                        doctorProfile = (DoctorProfile) parser.jsonParser(jsonObject, DoctorProfile.class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    DoctorProfile.ProfilePicture profilePicture = doctorProfile.getProfilePicture();

                    String pictureData = (profilePicture == null)?null:profilePicture.getData();

                    Bitmap bmp = null;

                    if(pictureData != null && pictureData.trim().length() > 0){
//                        String bitmapData = doctorProfile.getProfilePicture().getData();

                        if (pictureData != null && pictureData.trim().length() > 0) {
                            bmp = Utils.decodeBase64(pictureData);
                        }
                    }

                    return bmp;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);

                    if (bitmap != null) {
                        finalViewHolder.pharmaProfilePic.setImageBitmap(bitmap);
                    }
                }
            };

            if(Utils.isNetworkAvailableWithOutDialog(activity) && appointment.getDoctorId() > 0){
                task.execute(appointment.getDoctorId());
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    class ViewHolder {
        TextView titleTV, timeTV, locationTV;
        ImageView pharmaProfilePic;
    }

}
