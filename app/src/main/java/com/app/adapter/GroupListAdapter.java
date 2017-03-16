package com.app.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import medrep.medrep.AllContactsActivity;
import medrep.medrep.R;

/**
 * Created by guna on 15/08/16.
 */
public class GroupListAdapter extends BaseAdapter {
    private Context context;
    private JSONArray result;
    public ArrayList<String> doctorId;

    public GroupListAdapter(Context context, JSONArray result) {
        this.result = result;
        this.context = context;

    }

    @Override
    public int getCount() {
        return result.length();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.contact_custom_more_list_view, null);
//        JSONObject doctorObject = null;
        String shortDesc = null, imageUrl = null;
        try {
            final JSONObject doctorObject = result.getJSONObject(position);
            String firstName = doctorObject.getString("firstName");
            String lastName = doctorObject.getString("lastName");
            //final int doctorUniqId = doctorObject.getInt("doctorId");
            String name = firstName + lastName;
            if (!firstName.equals("null") && !lastName.equals("null")) {
                name = firstName + " " + lastName;
            } else if(!firstName.equals("null")) {
                name = firstName;
            } else if(!lastName.equals("null")) {
                name = lastName;
            }
            Button doctorImage = (Button)convertView.findViewById(R.id.doctorImage);

            shortDesc = doctorObject.getString("therapeuticName");
            JSONObject profilePicture = doctorObject.getJSONObject("profilePicture");
            if(profilePicture.has("imageUrl")) {
                imageUrl = profilePicture.getString("imageUrl");
            } else {
                imageUrl = null;
            }
            if(imageUrl != null) {
                LoadImage imageLoadFromServer = new LoadImage(doctorImage);
                imageLoadFromServer.execute(imageUrl);
            } else {
                //doctorImage.setText(name.charAt(0));
            }
            CheckBox checkbox = (CheckBox)convertView.findViewById(R.id.checkbox);
            convertView.setTag(checkbox);
            //final JSONObject finalDoctorObject = doctorObject;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v.getTag();
                    if(cb.isChecked()) {
                        cb.setChecked(false);
                    } else {
                        try {
                            JSONObject object = result.getJSONObject(position);
                            //int id = object.getInt("doctorId");
                            System.out.print(object.getInt("doctorId"));
                            //doctorId.add(id+"");
                            cb.setChecked(true);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            TextView doctorName = (TextView)convertView.findViewById(R.id.doctorName);
            doctorName.setText(name);

            TextView doctorSpecialist = (TextView)convertView.findViewById(R.id.doctorSpecialist);
            doctorSpecialist.setText(shortDesc);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        private Button buttonImage;

        private LoadImage(Button buttonImage) {
            this.buttonImage = buttonImage;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected Bitmap doInBackground(String... args) {
            Bitmap bitmap = null;
            System.out.println("image url: " + args[0]);
            if(args[0] != null) {
                try {
                    //bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());"http://theopentutorials.com/totwp331/wp-content/uploads/totlogo.png"
                    InputStream in = new URL(args[0]).openStream();
                    bitmap = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("bitmap image*****: " + bitmap);
                return bitmap;
            } else {
                return null;
            }

        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        protected void onPostExecute(Bitmap bitmapImage) {
            if(bitmapImage != null){
                BitmapDrawable bdrawable = new BitmapDrawable(context.getResources(),bitmapImage);
                buttonImage.setBackground(bdrawable);
            }
        }
    }
}
