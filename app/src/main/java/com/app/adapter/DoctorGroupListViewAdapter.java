package com.app.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.db.DoctorContacts;
import com.app.db.MedRepDatabaseHandler;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import medrep.medrep.AddProfileViewActivity;
import medrep.medrep.AllContactsActivity;
import medrep.medrep.DoctorMyGroupsActivty;
import medrep.medrep.DoctorsMyContactActivity;
import medrep.medrep.ProfileAwardActivity;
import medrep.medrep.ProfileEducationActivity;
import medrep.medrep.ProfileInterestAreaActivity;
import medrep.medrep.ProfilePublicationsActivity;
import medrep.medrep.ProfileViewActivity;
import medrep.medrep.R;
import medrep.medrep.ShareActivity;
import medrep.medrep.TransformActivity;

/**
 * Created by code_artist on 27/05/16.
 */
public class DoctorGroupListViewAdapter extends BaseAdapter {
    private Context context;
    private String activityName, titleListView;
    private ArrayList<DoctorContacts> doctorContacts;

    private String[] names = {"My Profile", "Dashboard", "Product Campaigns", "Doctor Activity Store", "Survey", "MedRep", "Other Marketing Campaigns", "Drug Sales",
            "Prescription Activities", "News & Update", "Settings & Preferences", "Logout"};
    private String[] news = {"Colon cancer varies by birth place", "Its not too late to get flu shot", "Best cancer screening methods",
    "Best Heart Surgery Operations", "Best Hospitals", "Colon cancer varies by birth place", "Its not too late to get flu shot", "Best cancer screening methods"};

    private int[] images = {R.drawable.news1, R.drawable.news2, R.drawable.news3, R.drawable.news4, R.drawable.news1, R.drawable.news2, R.drawable.news3, R.drawable.news4};

    public DoctorGroupListViewAdapter(Context context, String activityName) {
        this.context = context;
        this.activityName = activityName;
    }

    @Override
    public int getCount() {
        int count = 0;
        if(activityName == "doctorPostActivity" || activityName == "doctorMyGroupsActivty") {
            count = 3;
        } else if(activityName == "slideMenuActivity") {
            count = names.length;
        } else if(activityName == "commonGridView" || activityName == "commonGridViewSuggestions") {
            MedRepDatabaseHandler database = new MedRepDatabaseHandler(context);
            doctorContacts = database.getDoctorContactDetails();
            count = doctorContacts.size();
        } else if(context instanceof DoctorsMyContactActivity) {
            count = 1;
        } else if(activityName == "doctorGroupMembers") {
            count = 10;
        } else if(activityName == "notificationActivity") {
            count = 10;
        } else if(activityName == "addContact") {
            count = 1;
        } else if(activityName == "transformActivity") {
            count = news.length;
        } else if (activityName == "transform1") {
            count = 5;
        } else if(activityName == "transform2") {
            count = 3;
        } else if(activityName == "transform4") {
            count = 7;
        } else if(activityName == "transform5") {
            count = 6;
        } else if(activityName == "profileViewActivity") {
            count = 20;
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(activityName == "doctorPostActivity") {
            convertView = mInflater.inflate(R.layout.custom_doctor_contact_list_view, null);
            LinearLayout likeLayout = (LinearLayout)convertView.findViewById(R.id.likeLayout);
            final TextView numLikes = (TextView)convertView.findViewById(R.id.numLikes);
            likeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("TESTING", numLikes+"");
                    String likesCount = numLikes.getText().toString();
                    int likes = Integer.parseInt(likesCount);
                    likes += 1;
                    numLikes.setText(likes+"");
                }
            });

            LinearLayout messagelayout = (LinearLayout)convertView.findViewById(R.id.messagelayout);
            TextView commentCount = (TextView)convertView.findViewById(R.id.commentCount);
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
        } else if(activityName == "doctorMyGroupsActivty") {
            convertView = mInflater.inflate(R.layout.doctor_group_custom_list_view, null);
//            LinearLayout horizontalLayout = (LinearLayout)convertView.findViewById(R.id.horizontalLayout);
//            for(int i = 0; i < 7; i++) {
//                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                lparams.setMargins(24, 0, 0, 0);
//                Button doctorImageAndName = new Button(context);
//                doctorImageAndName.setSingleLine();
//                doctorImageAndName.setBackgroundResource(R.color.tect_color_white);
//                Drawable image = context.getResources().getDrawable(R.mipmap.launching_icon);
//                image.setBounds(0, 0, 120, 120);
//                doctorImageAndName.setCompoundDrawables(null, image, null, null);
//                doctorImageAndName.setText("John Deo");
//                doctorImageAndName.setTextColor(context.getResources().getColor(R.color.silver));
//                doctorImageAndName.setLayoutParams(lparams);
//                horizontalLayout.addView(doctorImageAndName);
//            }
        } else if(activityName == "addContact") {


        } else if(activityName == "transformActivity" || activityName == "transform1" || activityName == "transform1" ||
                activityName == "transform2" || activityName == "transform3" || activityName == "transform4") {
            convertView = mInflater.inflate(R.layout.doctor_group_custom_list_view, null);
            ImageView groupIcon = (ImageView)convertView.findViewById(R.id.groupIcon);
            groupIcon.getLayoutParams().width = 160;
            groupIcon.getLayoutParams().height = 140;
           // groupIcon.setBackground(context.getResources().getDrawable(R.drawable.white_background_rounded_corners));
            groupIcon.setImageResource(images[position]);

            if(position == 1 || position == 3 || position == 7) {
                ImageView videoThumbnail = (ImageView)convertView.findViewById(R.id.videoThumbnail);
                videoThumbnail.setVisibility(View.VISIBLE);
            } else if(position == 2) {
                groupIcon.setImageResource(R.drawable.pdf);
            }
            TextView name  = (TextView)convertView.findViewById(R.id.name);
            name.setText(news[position]);
            TextView description = (TextView)convertView.findViewById(R.id.groupDescription);
            description.setText("Supplementation appears safe but experts say it too soon for general medicine and it is not much effective");
        }
        else if(activityName == "slideMenuActivity") {
            convertView = mInflater.inflate(R.layout.slide_menu_custom_list_view, null);
            //ImageView sideMenuImage = (ImageView)convertView.findViewById(R.id.sideMenuImage);
            TextView sideMenuText = (TextView)convertView.findViewById(R.id.sideMenuText);
            sideMenuText.setText(names[position]);
        } else if(activityName == "commonGridView" || activityName == "commonGridViewSuggestions" || activityName == "John Deo") {
            convertView = mInflater.inflate(R.layout.contacts_custom_grid_view, null);

            //convertView.setLayoutParams(new GridView.LayoutParams(220, 220));
            TextView doctorName = (TextView)convertView.findViewById(R.id.doctorName);
            doctorName.setText(doctorContacts.get(position).getName());
            TextView specialistIn = (TextView)convertView.findViewById(R.id.specialistIn);
            specialistIn.setText(doctorContacts.get(position).getContactspecialist());

            if(position % 2 == 0) {
                ImageView doctorImage = (ImageView) convertView.findViewById(R.id.doctorImage);
                doctorImage.setImageDrawable(context.getResources().getDrawable(R.drawable.doctor_image1));

//                if(activityName != "John Deo"){
//                    doctorName.setText("Paul Waker");
//                }
            }
        } else if(activityName == "doctorGroupMembers") {
            convertView = mInflater.inflate(R.layout.doctor_group_members_custom_list_view, null);

        } else if(activityName == "notificationActivity") {
            convertView = mInflater.inflate(R.layout.notification_custom_list_view, null);
        } else if(activityName == "profileViewActivity") {
            convertView = mInflater.inflate(R.layout.custom_profile_list_view, null);
            ImageButton addData = (ImageButton)convertView.findViewById(R.id.addData);
            TextView fromTo = (TextView)convertView.findViewById(R.id.fromTo);
            TextView address = (TextView)convertView.findViewById(R.id.address);

            addData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent((ProfileViewActivity)context, AddProfileViewActivity.class);
                    ((ProfileViewActivity)context).startActivity(intent);
                }
            });
            TextView title = (TextView)convertView.findViewById(R.id.title);
            //String titleListView = "Work Experience";
            if(position == 0 || position == 5 || position == 10 || position == 15) {

//                ImageButton delete = (ImageButton)convertView.findViewById(R.id.delete);
//                delete.setVisibility(View.GONE);

                if(position == 5) {
                    title.setText("Educational Qualifications");
                    titleListView = "Educational Qualifications";
                } else if(position == 10) {
                    title.setText("Therapeutic Area");
                    titleListView = "Therapeutic Area";
                } else if(position == 15) {
                    title.setText("Publications");
                    titleListView = "Publications";
                } else if(position == 0) {
                    title.setText("Work Experience");
                    titleListView = "Work Experience";
                }
            } else {
                LinearLayout titleLayout = (LinearLayout)convertView.findViewById(R.id.titleLayout);
                titleLayout.setVisibility(View.GONE);
            }

            System.out.println(titleListView);

            if(titleListView.equals("Therapeutic Area") || titleListView.equals("Publications"))  {
                fromTo.setVisibility(View.GONE);
                address.setVisibility(View.GONE);
            }
            switch (position) {
                case 0:
//                    addData.setText("+ Add Qualification");
//                    title.setText("Educational Qualification");
                    addData.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent((ProfileViewActivity)context, AddProfileViewActivity.class);
                            ((ProfileViewActivity)context).startActivity(intent);
                        }
                    });
                    break;
                case 5:
//                    addData.setText("+ Add Publication");
//                    title.setText("Publications");
                    addData.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent((ProfileViewActivity)context, ProfileEducationActivity.class);
                            ((ProfileViewActivity)context).startActivity(intent);
                        }
                    });

                    break;
                case 10:
//                    addData.setText("+ Add Award");
//                    title.setText("Awards");
                    addData.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent((ProfileViewActivity)context, ProfileAwardActivity.class);
                            ((ProfileViewActivity)context).startActivity(intent);
                        }
                    });
                    break;
                case 15:
//                    addData.setText("+ Add Membership");
//                    title.setText("Memberships & Positions");
                    addData.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent((ProfileViewActivity)context, ProfilePublicationsActivity.class);
                            ((ProfileViewActivity)context).startActivity(intent);
                        }
                    });
                    break;
            }
        }

        return convertView;
    }
}
