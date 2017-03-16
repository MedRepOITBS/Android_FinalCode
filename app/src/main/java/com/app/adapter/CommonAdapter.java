package com.app.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.db.MedRepDatabaseHandler;
import com.app.pojo.SignIn;
import com.app.task.AddressDeleteData;
import com.app.task.HttpPost;
import com.app.task.NotificationGetTask;
import com.app.task.PostData;
import com.app.task.ProfilePostData;
import com.app.util.HttpUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.LogRecord;

import medrep.medrep.AddContactActivity;
import medrep.medrep.AddGroupMembersActivity;
import medrep.medrep.AddProfileViewActivity;
import medrep.medrep.AllContactsActivity;
import medrep.medrep.ContactsMoreOptionsActivity;
import medrep.medrep.DoctorMyGroupsActivty;
import medrep.medrep.DoctorParticularGroupActivity;
import medrep.medrep.DoctorPostsActivity;
import medrep.medrep.DoctorsMyContactActivity;
import medrep.medrep.GroupMoreActivity;
import medrep.medrep.LoginActivity;
import medrep.medrep.MobileContactsActivity;
import medrep.medrep.ProfileAddressInfoActivity;
import medrep.medrep.ProfileAwardActivity;
import medrep.medrep.ProfileContactInfoActivity;
import medrep.medrep.ProfileEducationActivity;
import medrep.medrep.ProfilePublicationsActivity;
import medrep.medrep.ProfileViewActivity;
import medrep.medrep.R;
import medrep.medrep.SearchForDrugsActivity;
import medrep.medrep.ShareActivity;
import medrep.medrep.SuggestedContactsActivity;
import medrep.medrep.TransformActivity;

/**
 * Created by GunaSekhar on 25-06-2016.
 */
public class CommonAdapter extends BaseAdapter {

    private JSONArray result;
    private Context context;
    private JSONObject resultObject;
    private String titleListView;
    private int currentPosition = -1;
    //private Button image;
//    private int groupLength;
//    private int doctorLength;

    public CommonAdapter(Context context, JSONArray result) {
        this.result = result;
        this.context = context;
    }

    public CommonAdapter(Context context, JSONObject resultObject) {
        //for profile screen
        this.context = context;
        this.resultObject = resultObject;
    }

    @Override
    public int getCount() {
        if(result instanceof JSONArray) {
            return result.length();
        } else{
            JSONArray workExperiencesArray = null;
            try {
                workExperiencesArray = resultObject.getJSONArray("workexperiences");
                JSONArray publicationsArray = resultObject.getJSONArray("publications");
                JSONObject contactInfoObject = resultObject.getJSONObject("contactInfo");
                JSONArray addressArray = resultObject.getJSONArray("address");
//                JSONObject therapeuticareaObject = resultObject.getJSONObject("therapeuticarea");
                JSONArray interestArray = resultObject.getJSONArray("interests");
                JSONArray educationdetailsArray = resultObject.getJSONArray("educationdetails");
                JSONObject aboutObject = resultObject.getJSONObject("about");
                int workExperiencesLength = workExperiencesArray.length();
                int publicationsLenght = publicationsArray.length();
 //               int contactInfoLength = contactInfoObject.length();
                int addressLength = addressArray.length();
                int interestsLength = interestArray.length();
               // int therapeuticareaLength = therapeuticareaObject.length();
                int educationdetailsLength = educationdetailsArray.length();
                System.out.println("Length: " + workExperiencesLength + publicationsLenght + educationdetailsLength);
                return educationdetailsLength + workExperiencesLength + publicationsLenght + 1 +
                        addressLength + interestsLength;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(context instanceof DoctorMyGroupsActivty) {
            convertView = mInflater.inflate(R.layout.doctor_group_custom_list_view, null);
            try {
                final JSONObject object = result.getJSONObject(position);
                String imageUrl = object.getString("imageUrl");
                System.out.println(imageUrl);
                Button image = (Button) convertView.findViewById(R.id.groupIcon);
                String groupName = object.getString("group_name");
                if(!imageUrl.equals(null)) {
                    LoadImage imageLoadFromServer = new LoadImage(image);
                    imageLoadFromServer.execute(imageUrl);
                } else {
                    image.setText(groupName.charAt(0));
                }
                if (groupName != null || !(groupName.equals(""))) {
                    TextView name = (TextView)convertView.findViewById(R.id.name);
                    name.setText(groupName);
                    String shortDesc = object.getString("group_short_desc");
                    TextView groupDescription = (TextView)convertView.findViewById(R.id.groupDescription);
                    groupDescription.setText(shortDesc);
                    Button addButton = (Button)convertView.findViewById(R.id.addButton);
                    //addButton.setBackground(context.getResources().getDrawable(R.drawable.delete));
                    //addButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.delete, 0, 0);
                    addButton.setBackgroundColor(context.getResources().getColor(R.color.pharma_actscor_blue03));
                    addButton.setText("Delete");
                    addButton.setVisibility(View.VISIBLE);
                    addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Are you sure you want to delete group")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            String ip = HttpUrl.COMMONURL;
                                            String accessToken = SignIn.GET_ACCESS_TOKEN();
                                            if(accessToken != null) {
                                                String url = ip + "/deleteGroup?token=" + accessToken;
                                                JSONObject jsonobject = new JSONObject();
                                                try {
                                                    jsonobject.put("group_id", object.getString("group_id"));
                                                    HttpPost post = new HttpPost(jsonobject);
                                                    post.delegate = (DoctorMyGroupsActivty)context;
                                                    post.execute(url);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                Intent intent = new Intent((DoctorMyGroupsActivty)context, LoginActivity.class);
                                                ((DoctorMyGroupsActivty)context).startActivity(intent);
                                            }
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // User cancelled the dialog
                                        }
                                    });
                            builder.show();
                        }

                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if(context instanceof AddContactActivity) {
            convertView = mInflater.inflate(R.layout.doctor_group_custom_list_view, null);
            try {
                JSONObject object = result.getJSONObject(position);
                String doctorDetails = object.getString("profilePicture");
                JSONObject doctorDetalsObject = new JSONObject(doctorDetails);
                String doctorImage = doctorDetalsObject.getString("data");
                String doctorName = object.getString("displayName");
                String doctotSpecialist = object.getString("therapeuticName");

                TextView description = (TextView)convertView.findViewById(R.id.groupDescription);
                description.setText(doctotSpecialist);
                TextView name  = (TextView)convertView.findViewById(R.id.name);
                name.setText("Dr. " + doctorName);
                Button groupIcon = (Button) convertView.findViewById(R.id.groupIcon);
                if(!doctorImage.equals(null)) {
                    byte[] decodedString = Base64.decode(doctorImage, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    BitmapDrawable bdrawable = new BitmapDrawable(context.getResources(),decodedByte);

                    groupIcon.setBackground(bdrawable);
                } else {
                    groupIcon.setText(doctorName.charAt(0));
                }

                Button addButton = (Button)convertView.findViewById(R.id.addButton);
                addButton.setVisibility(View.VISIBLE);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        MedRepDatabaseHandler database = new MedRepDatabaseHandler(context);
//                        database.addDoctorContact("John Deo", "Ortho");
//                        Intent intent = new Intent(context, DoctorsMyContactActivity.class);
//                        context.startActivity(intent);
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if(context instanceof DoctorsMyContactActivity || context instanceof SuggestedContactsActivity) {
            //System.out.println(selected);
            convertView = mInflater.inflate(R.layout.contacts_custom_grid_view, null);
            System.out.println(result);
            try {
                JSONObject object = result.getJSONObject(position);
                String firstName = object.getString("firstName");
                String lastName = object.getString("lastName");
                String fullName = firstName +" "+ lastName;
                TextView doctorName = (TextView)convertView.findViewById(R.id.doctorName);
                doctorName.setText("Dr. "+ fullName);
                String doctotSpecialist = object.getString("therapeuticArea");
                TextView specialistIn = (TextView)convertView.findViewById(R.id.specialistIn);
                specialistIn.setText(doctotSpecialist);
                String doctorImage = object.getString("dPicture");
                Log.w("DOCTOR IMAGE URL", doctorImage);
                if(doctorImage != null) {
                   // final URL url = new URL(doctorImage);
                    Button image = (Button) convertView.findViewById(R.id.doctorImage);
                    LoadImage imageLoadFromServer = new LoadImage(image);
                    imageLoadFromServer.execute(doctorImage);
//                    Drawable imageDrawable = LoadImageFromWebOperations(doctorImage);
//                    image.setBackground(imageDrawable);
                    //Bitmap imageBitMap = getBitmapFromURL(doctorImage);
                    //image.setImageBitmap(imageBitMap);
//                    GetImage bitImage = new GetImage(image);
//                    bitImage.execute(doctorImage);
//                    byte[] decodedString = Base64.decode(doctorImage, Base64.DEFAULT);
//                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    //image.setImageBitmap();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if(context instanceof DoctorParticularGroupActivity) {
            convertView = mInflater.inflate(R.layout.contact_custom_more_list_view, null);
            try {
                final JSONObject object = result.getJSONObject(position);
                String firstName = object.getString("firstName");
                String lastName = object.getString("lastName");
                String fullName = firstName + " " + lastName;
                TextView doctorName = (TextView)convertView.findViewById(R.id.doctorName);
                doctorName.setText("Dr. " + fullName);
                String specalist = object.getString("therapeuticDesc");
                TextView doctorSpecialist = (TextView)convertView.findViewById(R.id.doctorSpecialist);
                doctorSpecialist.setText(specalist);
                String dPicture = object.getString("data");
                Button image = (Button) convertView.findViewById(R.id.doctorImage);
                if(dPicture != null) {
                    LoadImage loadImage = new LoadImage(image);
                    loadImage.execute(dPicture);
//                    byte[] decodedString = Base64.decode(dPicture, Base64.DEFAULT);
//                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                    image.setImageBitmap(decodedByte);
                } else {
                    image.setText(firstName.charAt(0) + lastName.charAt(0));
                }

                //checkbox
                CheckBox checkbox = (CheckBox)convertView.findViewById(R.id.checkbox);
                convertView.setTag(checkbox);
                convertView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        System.out.print("coming here");
                        CheckBox cb = (CheckBox) v.getTag();
                        if(cb.isChecked()) {
                            cb.setChecked(false);
                        } else {
                            cb.setChecked(true);
                            try {
                                ((DoctorParticularGroupActivity)context).selectedId.add(object.getInt("member_id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if(context instanceof ContactsMoreOptionsActivity || context instanceof GroupMoreActivity) {
            convertView = mInflater.inflate(R.layout.contact_custom_more_list_view, null);
            try {
                final JSONObject object = result.getJSONObject(position);
                String firstName = object.getString("firstName");
                String lastName = object.getString("lastName");
                String fullName = firstName + " " + lastName;
                TextView doctorName = (TextView)convertView.findViewById(R.id.doctorName);
                doctorName.setText("Dr. " + fullName);
                String specalist = object.getString("therapeuticArea");
                TextView doctorSpecialist = (TextView)convertView.findViewById(R.id.doctorSpecialist);
                doctorSpecialist.setText(specalist);
                String dPicture = object.getString("dPicture");
                ImageView doctorImage = (ImageView)convertView.findViewById(R.id.doctorImage);
                if(dPicture != null) {
                    byte[] decodedString = Base64.decode(dPicture, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    doctorImage.setImageBitmap(decodedByte);
                }

                //checkbox
                CheckBox checkbox = (CheckBox)convertView.findViewById(R.id.checkbox);
                convertView.setTag(checkbox);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v.getTag();
                        if(cb.isChecked()) {
                            cb.setChecked(false);
                        } else {
                            cb.setChecked(true);
                            try {
                                ((ContactsMoreOptionsActivity) context).selectedId.add(object.getInt("userId"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if(context instanceof AddGroupMembersActivity) {
            convertView = mInflater.inflate(R.layout.contact_custom_more_list_view, null);
            try {
                final JSONObject object = result.getJSONObject(position);
                String firstName = object.getString("firstName");
                String lastName = object.getString("lastName");
                String fullName = firstName + " " + lastName;
                TextView doctorName = (TextView)convertView.findViewById(R.id.doctorName);
                doctorName.setText("Dr. " + fullName);
                String specalist = object.getString("therapeuticArea");
                TextView doctorSpecialist = (TextView)convertView.findViewById(R.id.doctorSpecialist);
                doctorSpecialist.setText(specalist);
                String dPicture = object.getString("dPicture");
                ImageView doctorImage = (ImageView)convertView.findViewById(R.id.doctorImage);
                if(dPicture != null) {
                    byte[] decodedString = Base64.decode(dPicture, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    doctorImage.setImageBitmap(decodedByte);
                }

                //checkbox
                CheckBox checkbox = (CheckBox)convertView.findViewById(R.id.checkbox);
                convertView.setTag(checkbox);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v.getTag();
                        if(cb.isChecked()) {
                            cb.setChecked(false);
                        } else {
                            cb.setChecked(true);
                            try {
                                ((AddGroupMembersActivity) context).selectedId.add(object.getInt("userId"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if(context instanceof MobileContactsActivity) {
            convertView = mInflater.inflate(R.layout.contact_custom_more_list_view, null);
            try {
                final JSONObject object = result.getJSONObject(position);
                String name = object.getString("name");
                TextView doctorName = (TextView)convertView.findViewById(R.id.doctorName);
                doctorName.setText(name);
                String number = object.getString("number");
                TextView doctorSpecialist = (TextView)convertView.findViewById(R.id.doctorSpecialist);
                doctorSpecialist.setText(number);
                ImageView doctorImage = (ImageView)convertView.findViewById(R.id.doctorImage);
                doctorImage.setVisibility(View.GONE);
                CheckBox checkbox = (CheckBox)convertView.findViewById(R.id.checkbox);
                convertView.setTag(checkbox);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v.getTag();
                        if(cb.isChecked()) {
                            cb.setChecked(false);
                        } else {
                            cb.setChecked(true);
                            try {
                                ((MobileContactsActivity) context).selectedContacts.add(object.getString("number"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if(context instanceof SearchForDrugsActivity) {
            convertView = mInflater.inflate(R.layout.custom_related_list_view, null);
            TextView title = (TextView)convertView.findViewById(R.id.title);
            TextView desc = (TextView)convertView.findViewById(R.id.desc);
            TextView tablet = (TextView)convertView.findViewById(R.id.tablet);
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String = "http://www.truemd.in/api/medicine_details/?" + "id=" + path + "&key=77b06e379a6f984a1f1ec091ce70f4";
//                }
//            });
            final JSONObject object;
            try {
                object = result.getJSONObject(position);
                String name = object.getString("brand");
                title.setText(name);
                String descp = object.getString("manufacturer");
                desc.setText(descp);
                String category = object.getString("category");
                tablet.setText(category);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if(context instanceof AddContactActivity) {
            convertView = mInflater.inflate(R.layout.doctor_group_custom_list_view, null);
            try {
                JSONObject object = result.getJSONObject(position);
                String firstName = object.getString("firstName");
                String lastName = object.getString("middleName");
                String fullName = firstName + " " + lastName;
                String title = object.getString("title");
                String roleName = object.getString("roleName");
                JSONObject profilePicture = object.getJSONObject("profilePicture");
                String icon = profilePicture.getString("data");
                TextView description = (TextView)convertView.findViewById(R.id.groupDescription);
                description.setText(roleName);
                Button addButton = (Button)convertView.findViewById(R.id.addButton);
                addButton.setVisibility(View.VISIBLE);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        MedRepDatabaseHandler database = new MedRepDatabaseHandler(context);
//                        database.addDoctorContact("John Deo", "Ortho");
//                        Intent intent = new Intent(context, DoctorsMyContactActivity.class);
//                        context.startActivity(intent);
                    }
                });
                TextView name  = (TextView)convertView.findViewById(R.id.name);
                name.setText(title + ". " + fullName);
                byte[] decodedString = Base64.decode(icon, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                BitmapDrawable bdrawable = new BitmapDrawable(context.getResources(),decodedByte);
                Button groupIcon = (Button) convertView.findViewById(R.id.groupIcon);
                groupIcon.setBackground(bdrawable);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if(context instanceof AllContactsActivity){
            String name = null, shortDesc = null, imageUrl = null;
            try {
                convertView = mInflater.inflate(R.layout.contact_custom_more_list_view, null);
                //JSONObject mainObject = result.getJSONObject(0);
                //if(position < groupLength) {
                    //JSONArray groupArray = mainObject.getJSONArray("groups");
                    JSONObject groupObject = result.getJSONObject(position);
                    name = groupObject.getString("group_name");
                    shortDesc = groupObject.getString("group_short_desc");
                    imageUrl = groupObject.getString("imageUrl");
                    CheckBox checkbox = (CheckBox)convertView.findViewById(R.id.checkbox);
                    convertView.setTag(checkbox);
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CheckBox cb = (CheckBox) v.getTag();
                            if(cb.isChecked()) {
                                cb.setChecked(false);
                            } else {
                                cb.setChecked(true);
                                try {
                                    JSONObject groupObject = result.getJSONObject(position);
                                    System.out.print(groupObject.getInt("group_id"));
                                    //((AllContactsActivity) context).selectedId.add(groupObject.getInt("group_id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
               // }
//                else if(position < doctorLength){
//                    position =- groupLength;
//                    JSONArray array = mainObject.getJSONArray("doctor");
//                    final JSONObject doctorObject = array.getJSONObject(position);
//                    String firstName = doctorObject.getString("firstName");
//                    String lastName = doctorObject.getString("lastName");
//                    if (!firstName.equals("null") && !lastName.equals("null")) {
//                        name = firstName + " " + lastName;
//                    } else if(!firstName.equals("null")) {
//                        name = firstName;
//                    } else if(!lastName.equals("null")) {
//                        name = lastName;
//                    }
//                    shortDesc = doctorObject.getString("therapeuticName");
//                    JSONObject profilePicture = doctorObject.getJSONObject("profilePicture");
//                    if(profilePicture.has("imageUrl")) {
//                        imageUrl = profilePicture.getString("imageUrl");
//                    } else {
//                        imageUrl = null;
//                    }
//
//                    CheckBox checkbox = (CheckBox)convertView.findViewById(R.id.checkbox);
//                    convertView.setTag(checkbox);
//                    convertView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            CheckBox cb = (CheckBox) v.getTag();
//                            if(cb.isChecked()) {
//                                cb.setChecked(false);
//                            } else {
//                                cb.setChecked(true);
//                                try {
//                                    ((AllContactsActivity) context).selectedId.add(doctorObject.getInt("doctorId"));
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    });
//                }

                //if(name != null) {

                    TextView doctorName = (TextView)convertView.findViewById(R.id.doctorName);
                    doctorName.setText(name);

                    TextView doctorSpecialist = (TextView)convertView.findViewById(R.id.doctorSpecialist);
                    doctorSpecialist.setText(shortDesc);

                    Button doctorImage = (Button)convertView.findViewById(R.id.doctorImage);
                    if(imageUrl != null) {
                        LoadImage imageLoadFromServer = new LoadImage(doctorImage);
                        imageLoadFromServer.execute(imageUrl);
//                        byte[] decodedString = Base64.decode(dPicture, Base64.DEFAULT);
//                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                        doctorImage.setImageBitmap(decodedByte);
                    } else {
                        //doctorImage.setText(name.charAt(0));
                    }

                    //checkbox

//                } else {
//                    //convertView = null;
//                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if(context instanceof TransformActivity) {
            int[] images = {R.drawable.news1, R.drawable.news2, R.drawable.news3, R.drawable.news4, R.drawable.news1, R.drawable.news2, R.drawable.news3, R.drawable.news4};
            convertView = mInflater.inflate(R.layout.doctor_group_custom_list_view, null);
            Button groupIcon = (Button) convertView.findViewById(R.id.groupIcon);
            groupIcon.getLayoutParams().width = 160;
            groupIcon.getLayoutParams().height = 140;
            // groupIcon.setBackground(context.getResources().getDrawable(R.drawable.white_background_rounded_corners));
            groupIcon.setBackgroundResource(images[0]);

            if(position == 1 || position == 3 || position == 7) {
                ImageView videoThumbnail = (ImageView)convertView.findViewById(R.id.videoThumbnail);
                videoThumbnail.setVisibility(View.VISIBLE);
            } else if(position == 2) {
                groupIcon.setBackgroundResource(R.drawable.pdf);
            }
            try {
                JSONObject object = result.getJSONObject(position);
                String title = object.getString("title");
                TextView name  = (TextView)convertView.findViewById(R.id.name);
                name.setText(title);
                String desc = object.getString("tagDesc");
                TextView description = (TextView)convertView.findViewById(R.id.groupDescription);
                description.setText(Html.fromHtml(desc));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if(context instanceof ShareActivity) {

            try {
                JSONObject object = result.getJSONObject(position);
                String doctorTitle = object.getString("doctor_Name");
                //if(!doctorTitle.equals("null")) {
                convertView = mInflater.inflate(R.layout.custom_doctor_contact_list_view, null);
                TextView doctorName = (TextView)convertView.findViewById(R.id.doctorName);
                doctorName.setText(doctorTitle);
                Button doctorImage = (Button)convertView.findViewById(R.id.doctorImage);
                String doctorPic = object.getString("displayPicture");
                if(doctorPic == null) {
                    doctorImage.setText(doctorTitle.charAt(0));
                } else {
                    LoadImage loadImage = new LoadImage(doctorImage);
                    loadImage.execute(doctorPic);
                }
                TextView doctorStatus = (TextView)convertView.findViewById(R.id.doctorStatus);
                String message = object.getString("short_desc");
                if(!message.equals("null")) {
                    doctorStatus.setText(message);
                } else {
                    doctorStatus.setText("");
                }

                ImageView doctorImageShare = (ImageView)convertView.findViewById(R.id.doctorImageShare);
                doctorImageShare.setVisibility(View.GONE);

                LinearLayout likeLayout = (LinearLayout)convertView.findViewById(R.id.likeLayout);
                final TextView numLikes = (TextView)convertView.findViewById(R.id.numLikes);
                String likes = object.getString("likes_count");
                numLikes.setText(likes);
                TextView commentCount = (TextView)convertView.findViewById(R.id.commentCount);
                String comment = object.getString("comment_count");
                commentCount.setText(comment);
                TextView shareCount = (TextView)convertView.findViewById(R.id.shareCount);
                String share = object.getString("share_count");
                shareCount.setText(share);
                long postedOn = object.getLong("posted_on");
                String dateTime = getDate(postedOn);
                TextView sharedMessageDate = (TextView)convertView.findViewById(R.id.shared_message_date);
                sharedMessageDate.setText(dateTime);
                final int finalPosition = position;
                likeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ip = HttpUrl.COMMONURL;
                        String accessToken = SignIn.GET_ACCESS_TOKEN();
                        String url = ip + "/postLike?token=" + accessToken;
                        JSONObject obj = new JSONObject();
                        try {
                            JSONObject resultObj = result.getJSONObject(finalPosition);
                            obj.put("like_status", "true");
                            obj.put("message_id", resultObj.getString("id"));
                            Log.i("TESTING", numLikes+"");
                            String likesCount = numLikes.getText().toString();
                            int likes = Integer.parseInt(likesCount);
                            likes += 1;
                            numLikes.setText(likes+"");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        HttpPost post = new HttpPost(obj);
                        post.delegate = (ShareActivity)context;
                        post.execute(url);
                    }
                });
                LinearLayout messagelayout = (LinearLayout)convertView.findViewById(R.id.messagelayout);
                //TextView commentCount = (TextView)convertView.findViewById(R.id.commentCount);
                messagelayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder((ShareActivity)context);
                        LayoutInflater inflater = ((ShareActivity) context).getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.custom_share_layout, null);
                        dialog.setView(dialogView);
                        final Button uploadImage = (Button)dialogView.findViewById(R.id.uploadImage);
                        ImageView previewImage = (ImageView)dialogView.findViewById(R.id.previewImage);
                        ((ShareActivity)context).previewImage = previewImage;
                        uploadImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Add Photo!");
                                builder.setItems(items, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                        if (items[item].equals("Take Photo")) {
                                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            ((ShareActivity)context).startActivityForResult(takePicture, 0);
                                        } else if (items[item].equals("Choose from Library")) {
                                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                            ((ShareActivity)context).startActivityForResult(pickPhoto , 1);
                                        } else if (items[item].equals("Cancel")) {
                                            dialog.dismiss();
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                        final AlertDialog dialogShow = dialog.create();
                        dialogShow.show();
                        Button okButton = (Button)dialogView.findViewById(R.id.okButton);
                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogShow.dismiss();
                            }
                        });
                    }
                });

                LinearLayout shareLayout = (LinearLayout)convertView.findViewById(R.id.shareLayout);
                shareLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent((ShareActivity)context, AllContactsActivity.class);
                        ((ShareActivity)context).startActivity(intent);
                    }
                });
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if(context instanceof DoctorPostsActivity) {

            try {
                JSONObject object = result.getJSONObject(position);
                String doctorTitle = object.getString("doctor_Name");
                //if(!doctorTitle.equals("null")) {
                convertView = mInflater.inflate(R.layout.custom_doctor_contact_list_view, null);
                TextView doctorName = (TextView)convertView.findViewById(R.id.doctorName);
                doctorName.setText(doctorTitle);
                Button doctorImage = (Button)convertView.findViewById(R.id.doctorImage);
                String doctorPic = object.getString("displayPicture");
                if(doctorPic == null) {
                    doctorImage.setText(doctorTitle.charAt(0));
                } else {
                    LoadImage loadImage = new LoadImage(doctorImage);
                    loadImage.execute(doctorPic);
                }
                TextView doctorStatus = (TextView)convertView.findViewById(R.id.doctorStatus);
                String message = object.getString("short_desc");
                if(!message.equals("null")) {
                    doctorStatus.setText(message);
                } else {
                    doctorStatus.setText("");
                }

                ImageView doctorImageShare = (ImageView) convertView.findViewById(R.id.doctorImageShare);
                //doctorImageShare.setVisibility(View.GONE);
                String url = object.getString("url");
//                System.out.println("Iamge url" + url.getClass().getName());
                if(url.length() > 10) {
                    doctorImageShare.setVisibility(View.VISIBLE);
                    LoadImage loadImage = new LoadImage(doctorImageShare);
                    loadImage.execute(url);
                } else {
                    doctorImageShare.setVisibility(View.GONE);
                }

                LinearLayout statistics = (LinearLayout)convertView.findViewById(R.id.statistics);
                statistics.setVisibility(View.GONE);
                long postedOn = object.getLong("posted_on");
                String dateTime = getDate(postedOn);
                TextView sharedMessageDate = (TextView)convertView.findViewById(R.id.shared_message_date);
                sharedMessageDate.setVisibility(View.VISIBLE);
                sharedMessageDate.setText(dateTime);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else if( context instanceof ProfileViewActivity) {
            convertView = mInflater.inflate(R.layout.custom_profile_list_view, null);
            TextView specialist = (TextView)convertView.findViewById(R.id.specialist);
            ImageView addData = (ImageView)convertView.findViewById(R.id.addData);
            TextView fromTo = (TextView)convertView.findViewById(R.id.fromTo);
            TextView address = (TextView)convertView.findViewById(R.id.address);
            ImageButton delete = (ImageButton)convertView.findViewById(R.id.delete);
            int workExperiencesLength = 0;
            int publicationsLength = 0;
            int interestsLength = 0;
            int educationdetailsLength = 0;
            int contactInfoLength = 0;
            int addressLength = 0;
            //int currentPosition = -1;

            addData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent((ProfileViewActivity)context, AddProfileViewActivity.class);
                    ((ProfileViewActivity)context).startActivity(intent);
                }
            });
            TextView title = (TextView)convertView.findViewById(R.id.title);
            //String titleListView = "Work Experience";
            JSONArray workExperiencesArray = null;
            JSONArray publicationsArray = null;
            JSONObject contactInfoObject = null;
            JSONArray addressArray = null;
     //       JSONObject therapeuticareaObject = null;
            JSONArray interestArray = null;
            JSONArray educationdetailsArray = null;
            JSONObject aboutObject;
            try {
                workExperiencesArray = resultObject.getJSONArray("workexperiences");
                publicationsArray = resultObject.getJSONArray("publications");
                contactInfoObject = resultObject.getJSONObject("contactInfo");
                addressArray = resultObject.getJSONArray("address");
                //therapeuticareaObject = resultObject.getJSONObject("therapeuticarea");
                interestArray = resultObject.getJSONArray("interests");
                educationdetailsArray = resultObject.getJSONArray("educationdetails");
                aboutObject = resultObject.getJSONObject("about");

                workExperiencesLength = workExperiencesArray.length();
                publicationsLength = publicationsArray.length();
                //therapeuticareaLength = therapeuticareaObject.length();
                educationdetailsLength = educationdetailsArray.length();
                interestsLength = interestArray.length();
                contactInfoLength = contactInfoObject.length();
                addressLength = addressArray.length();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //if(position == 0 || position == workExperiencesLength+educationdetailsLength || position == workExperiencesLength+educationdetailsLength+therapeuticareaLength || position == workExperiencesLength+educationdetailsLength+therapeuticareaLength+publicationsLength) {
                if(position >= 0 && position < educationdetailsLength) {
                    if(position == 0) {
                        currentPosition = 0;
                        title.setText("Educational Qualifications");
                        titleListView = "Educational Qualifications";
                        addData.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent((ProfileViewActivity)context, ProfileEducationActivity.class);
                                ((ProfileViewActivity)context).startActivity(intent);
                            }
                        });
                    } else {
                        currentPosition += 1;
                        RelativeLayout titleLayout = (RelativeLayout)convertView.findViewById(R.id.titleLayout);
                        titleLayout.setVisibility(View.GONE);
                    }
                    try {

                        final JSONObject object = educationdetailsArray.getJSONObject(currentPosition);
                        String college = object.getString("collegeName");
                        Log.i("College Name", college);
                        String[] collegeAddress = college.split(",");

                        String collegeName = collegeAddress[0];
                        if(collegeAddress.length == 2) {
                            String addressOfCollege = collegeAddress[1];
                            address.setText(addressOfCollege);
                        }

                        String yearOfPassout = object.getString("yearOfPassout");

                        fromTo.setText(yearOfPassout);
                        specialist.setText(collegeName);
                        final int id = object.getInt("id");
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String ip = HttpUrl.COMMONURL;
                                String accessToken = SignIn.GET_ACCESS_TOKEN();
                                String url = ip + "/doctorinfo/education/delete/"+ id +"?token=" + accessToken;
                                NotificationGetTask getTask = new NotificationGetTask();
                                getTask.delegate = (ProfileViewActivity)context;
                                getTask.execute(url);
                            }
                        });

                        LinearLayout dataLayout = (LinearLayout) convertView.findViewById(R.id.data_layout);
                        dataLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent((ProfileViewActivity)context, ProfileEducationActivity.class);
                                intent.putExtra("jsonObj",object.toString());
                                ((ProfileViewActivity)context).startActivity(intent);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if(position >= educationdetailsLength && position < workExperiencesLength+educationdetailsLength) {
                    //currentPosition = currentPosition + 1;
                    if(position == educationdetailsLength) {
                        //System.out.println("First time coming here&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                        currentPosition = 0;
                        title.setText("Work Experience");
                        titleListView = "Work Experience";
                        addData.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent((ProfileViewActivity)context, AddProfileViewActivity.class);
                                ((ProfileViewActivity)context).startActivity(intent);
                            }
                        });
                    } else {
                        //System.out.println("except First time coming here&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                        RelativeLayout titleLayout = (RelativeLayout) convertView.findViewById(R.id.titleLayout);
                        titleLayout.setVisibility(View.GONE);
                        currentPosition = currentPosition + 1;
                    }

                    try {
                        final JSONObject workExperienceObject = workExperiencesArray.getJSONObject(currentPosition);
                        Log.i("Current Position:", currentPosition + "");
                        String designation = workExperienceObject.getString("designation");
                        String fromDate = workExperienceObject.getString("fromDate");
                        String toDate = workExperienceObject.getString("toDate");
                        fromTo.setText(fromDate+"-"+toDate);
                        String hospital = workExperienceObject.getString("hospital");
                        String location = workExperienceObject.getString("location");
                        address.setText(hospital + ", " + location);
                        specialist.setText(designation);
                        titleListView = "Work Experience";
                        final int id = workExperienceObject.getInt("id");
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String ip = HttpUrl.COMMONURL;
                                String accessToken = SignIn.GET_ACCESS_TOKEN();
                                String url = ip + "/doctorinfo/workexperience/delete/"+ id +"?token=" + accessToken;
                                NotificationGetTask getTask = new NotificationGetTask();
                                getTask.delegate = (ProfileViewActivity)context;
                                getTask.execute(url);

                            }
                        });
                        LinearLayout dataLayout = (LinearLayout) convertView.findViewById(R.id.data_layout);
                        dataLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent((ProfileViewActivity)context, AddProfileViewActivity.class);
                                intent.putExtra("jsonObj",workExperienceObject.toString());
                                ((ProfileViewActivity)context).startActivity(intent);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if(position >= workExperiencesLength+educationdetailsLength &&
                        position < workExperiencesLength+educationdetailsLength+publicationsLength) {
                    if(position == workExperiencesLength+educationdetailsLength) {
                        currentPosition = 0;
                        title.setText("Publications");
                        titleListView = "Publications";
                        addData.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent((ProfileViewActivity)context, ProfilePublicationsActivity.class);
                                ((ProfileViewActivity)context).startActivity(intent);
                            }
                        });
                    } else {
                        currentPosition += 1;
                        RelativeLayout titleLayout = (RelativeLayout)convertView.findViewById(R.id.titleLayout);
                        titleLayout.setVisibility(View.GONE);
                    }

                    try {
                        final JSONObject object = publicationsArray.getJSONObject(currentPosition);
                        String articleName = object.getString("articleName");
                        specialist.setText(articleName);
                        final int id = object.getInt("id");
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String ip = HttpUrl.COMMONURL;
                                String accessToken = SignIn.GET_ACCESS_TOKEN();
                                String url = ip + "/doctorinfo/publications/delete/" + id + "?token=" + accessToken;
                                NotificationGetTask getTask = new NotificationGetTask();
                                getTask.delegate = (ProfileViewActivity) context;
                                getTask.execute(url);
                            }
                        });
                        LinearLayout dataLayout = (LinearLayout) convertView.findViewById(R.id.data_layout);
                        dataLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent((ProfileViewActivity)context, ProfilePublicationsActivity.class);
                                intent.putExtra("jsonObj",object.toString());
                                ((ProfileViewActivity)context).startActivity(intent);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if(position >= workExperiencesLength+educationdetailsLength+publicationsLength
                        && position < workExperiencesLength+educationdetailsLength+publicationsLength + 1) {
                    title.setText("Contact Details");
                    titleListView = "Contact Details";
                    addData.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, ProfileContactInfoActivity.class);
                                context.startActivity(intent);
                            }
                        });
  //                  delete.setVisibility(View.GONE);
                    try {
                        String phoneNumber = contactInfoObject.getString("phoneNo");
                        String mobileNumber = contactInfoObject.getString("mobileNo");
                        String emailId = contactInfoObject.getString("email");
                        specialist.setText(mobileNumber);
                        fromTo.setText(phoneNumber);
                        address.setText(emailId);
                        LinearLayout dataLayout = (LinearLayout) convertView.findViewById(R.id.data_layout);
                        final JSONObject finalContactInfoObject = contactInfoObject;
                        dataLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent((ProfileViewActivity)context, ProfileContactInfoActivity.class);
                                intent.putExtra("jsonObj", finalContactInfoObject.toString());
                                ((ProfileViewActivity)context).startActivity(intent);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(position >= workExperiencesLength+educationdetailsLength+publicationsLength + 1 &&
                        position < workExperiencesLength+educationdetailsLength+publicationsLength + 1+interestsLength) {
                    if(position == workExperiencesLength+educationdetailsLength+publicationsLength + 1) {
                        currentPosition = 0;
                        title.setText("Therapeutic Area");
                        titleListView = "Therapeutic Area";
                        addData.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent((ProfileViewActivity) context, ProfileAwardActivity.class);
                                ((ProfileViewActivity) context).startActivity(intent);
                            }
                        });
                    } else {
                        currentPosition += 1;
                        RelativeLayout titleLayout = (RelativeLayout)convertView.findViewById(R.id.titleLayout);
                        titleLayout.setVisibility(View.GONE);
                    }

                    try {
                        final JSONObject interestObject = interestArray.getJSONObject(currentPosition);
                        String therapeuticName = interestObject.getString("name");
                        specialist.setText(therapeuticName);
                        final int id = interestObject.getInt("id");
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String ip = HttpUrl.COMMONURL;
                                String accessToken = SignIn.GET_ACCESS_TOKEN();
                                String url = ip + "/doctorinfo/interests/delete/"+ id +"?token=" + accessToken;
                                NotificationGetTask getTask = new NotificationGetTask();
                                getTask.delegate = (ProfileViewActivity)context;
                                getTask.execute(url);
                            }
                        });
                        LinearLayout dataLayout = (LinearLayout) convertView.findViewById(R.id.data_layout);
                        dataLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent((ProfileViewActivity)context, ProfileAwardActivity.class);
                                intent.putExtra("jsonObj", interestObject.toString());
                                ((ProfileViewActivity)context).startActivity(intent);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(position >= workExperiencesLength+educationdetailsLength+publicationsLength + 1+interestsLength ) {
                    if(position == workExperiencesLength+educationdetailsLength+publicationsLength + 1+interestsLength) {
                        currentPosition = 0;
                        title.setText("Address");
                        titleListView = "Address";
                        addData.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent((ProfileViewActivity) context, ProfileAddressInfoActivity.class);
                                ((ProfileViewActivity) context).startActivity(intent);
                            }
                        });
                    } else {
                        currentPosition += 1;
                        RelativeLayout titleLayout = (RelativeLayout)convertView.findViewById(R.id.titleLayout);
                        titleLayout.setVisibility(View.GONE);
                    }

                    try {
                        final JSONObject addressObject = addressArray.getJSONObject(currentPosition);
                        String address1 = addressObject.getString("address1");
                        String address2 = addressObject.getString("address2");
                        String city = addressObject.getString("city");
                        String state = addressObject.getString("state");
                        String zip = addressObject.getString("zipcode");
                        specialist.setText(address1+","+address2);
                        fromTo.setText(city);
                        address.setText(state+"-"+zip);

                        final int locId = addressObject.getInt("locationId");
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String ip = HttpUrl.COMMONURL;
                                String accessToken = SignIn.GET_ACCESS_TOKEN();
                                String url = ip + "/doctorinfo/address/delete?token=" + accessToken;

                                JSONObject jObject = new JSONObject();
                             //   JSONArray jArray = new JSONArray();
                                try {
                                    jObject.put("locationIds", locId);
                            //        jArray.put(jObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                AddressDeleteData post = new AddressDeleteData(jObject);
                                post.delegate = (ProfileViewActivity)context;
                                post.execute(url);

                               /* NotificationGetTask getTask = new NotificationGetTask();
                                getTask.delegate = (ProfileViewActivity)context;
                                getTask.execute(url);*/
                            }
                        });
                        LinearLayout dataLayout = (LinearLayout) convertView.findViewById(R.id.data_layout);
                        dataLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent((ProfileViewActivity)context, ProfileAddressInfoActivity.class);
                                intent.putExtra("jsonObj", addressObject.toString());
                                ((ProfileViewActivity)context).startActivity(intent);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

//            } else {
//
//            }

            //System.out.println(titleListView);

//            if(titleListView.equals("Therapeutic Area") || titleListView.equals("Publications"))  {
//                fromTo.setVisibility(View.GONE);
//                address.setVisibility(View.GONE);
//            }
//            switch (position) {
//                case 0:
////                    addData.setText("+ Add Qualification");
////                    title.setText("Educational Qualification");
//
//                case 5:
////                    addData.setText("+ Add Publication");
////                    title.setText("Publications");
//
//
//                    break;
//                case 10:
////                    addData.setText("+ Add Award");
////                    title.setText("Awards");
//
//                    break;
//                case 15:
////                    addData.setText("+ Add Membership");
////                    title.setText("Memberships & Positions");
//
//                    break;
//            }
        }
        return convertView;
    }

//    private static Drawable LoadImageFromWebOperations(String url) {
//        try {
//            InputStream is = (InputStream) new URL(url).getContent();
//            Drawable d = Drawable.createFromStream(is, "urlImage");
//            Log.e("Img", String.valueOf(d));
//            return d;
//        } catch (Exception e) {
//            return null;
//        }
//    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        private View buttonImage;

        private LoadImage(View buttonImage) {
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
                    if(!args[0].equals("null")) {
                        InputStream in = new URL(args[0]).openStream();
                        bitmap = BitmapFactory.decodeStream(in);
                    }

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

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy HH:mm:ss", cal).toString();
        return date;
    }

//    private void setImage(ImageView view, Bitmap image) {
//        view.setImageBitmap(image);
//    }
//
//    private class GetImage extends AsyncTask<String, Void, Bitmap> {
//        private ImageView view;
//        private GetImage(ImageView view) {
//            this.view = view;
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... params) {
//            Bitmap bmp = null;
//            try {
//                URL url = new URL(params[0]);
//
//                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return bmp;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap map) {
//            Log.d("UI thread", String.valueOf(map));
//            view.setImageBitmap(map);
//        }
//    }
}