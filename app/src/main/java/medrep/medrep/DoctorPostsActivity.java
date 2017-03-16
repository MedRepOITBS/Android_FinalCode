package medrep.medrep;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adapter.CommonAdapter;
import com.app.adapter.DoctorGroupListViewAdapter;
import com.app.interfaces.GetResponse;
import com.app.pojo.SignIn;
import com.app.task.HttpPost;
import com.app.task.NotificationGetTask;
import com.app.util.HttpUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;

public class DoctorPostsActivity extends AppCompatActivity implements View.OnClickListener, GetResponse{

    private int contactId;
    private int doctorId;
    private ListView postList;
    private TextView postAvailable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_contact);
        ImageButton add = (ImageButton)findViewById(R.id.add);
        //add.setVisibility(View.GONE);
        ImageButton message = (ImageButton)findViewById(R.id.message);
        // message.setVisibility(View.GONE);
//        ListView doctorPostListView = (ListView)findViewById(R.id.doctorPostListView);
//        DoctorGroupListViewAdapter adapter = new DoctorGroupListViewAdapter(this, "doctorPostActivity");
//        doctorPostListView.setAdapter(adapter);
        String fullName = getIntent().getExtras().getString("fullName");
        //int userId = getIntent().getExtras().getInt("userId");
        String therapeuticArea = getIntent().getExtras().getString("therapeuticArea");
        String imageData = getIntent().getExtras().getString("dPicture");
        doctorId = getIntent().getExtras().getInt("doctorId");
        contactId = getIntent().getExtras().getInt("contactId");
//        byte[] imageAsBytes = Base64.decode(imageData.getBytes(), Base64.DEFAULT);
//        Bitmap image = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        LinearLayout searchLayout = (LinearLayout)findViewById(R.id.searchLayout);
        searchLayout.setVisibility(View.GONE);
        HorizontalScrollView horizontalScroll = (HorizontalScrollView)findViewById(R.id.horizontalScroll);
        horizontalScroll.setVisibility(View.GONE);
//        LinearLayout postLayout = (LinearLayout)findViewById(R.id.postLayout);
//        postLayout.setVisibility(View.GONE);
//        specialistaLayout.setVisibility(View.VISIBLE);
        Button onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(this);
        TextView title = (TextView)findViewById(R.id.title);
        title.setText("Contact Details");
        TextView doctorName = (TextView)findViewById(R.id.doctorName);
        doctorName.setText(fullName);
        TextView specialist = (TextView)findViewById(R.id.specialist);
        specialist.setText("TherapeuticArea: " + therapeuticArea);
        ImageView doctorImage = (ImageView)findViewById(R.id.doctorImage);
        LoadImage loadImage = new LoadImage(doctorImage);
        loadImage.execute(imageData);
        //doctorImage.setImageBitmap(image);

        Button addContct = (Button)findViewById(R.id.addContct);
        addContct.setVisibility(View.GONE);
        addContct.setOnClickListener(this);

//        Button addConnection = (Button)findViewById(R.id.add_connection);
//        addConnection.setVisibility(View.VISIBLE);
//        addConnection.setOnClickListener(this);
//
//        Button comment = (Button)findViewById(R.id.comment);
//        comment.setVisibility(View.VISIBLE);
//        comment.setOnClickListener(this);

        postList = (ListView)findViewById(R.id.postList);
        postList.setVisibility(View.VISIBLE);

        postAvailable = (TextView)findViewById(R.id.postAvailable);

        Button deleteButton = (Button)findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);
//        ImageButton message = (ImageButton)findViewById(R.id.message);
//        message.setOnClickListener(this);
//        addIcon = (ImageButton)findViewById(R.id.addIcon);
//        addIcon.setOnClickListener(this);

        NotificationGetTask notificationGetTask = new NotificationGetTask();
        notificationGetTask.delegate = this;
        String accessToken = SignIn.GET_ACCESS_TOKEN();
        String url = HttpUrl.COMMONURL + "/getMessageById?token=" + accessToken + "&memberId=" + doctorId + "&groupId=0";
        notificationGetTask.execute(url);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.onBackClick:
                finish();
                break;
            case R.id.deleteButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to delete contact.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    String ip = HttpUrl.BASEURL;
                                    String accessToken = SignIn.GET_ACCESS_TOKEN();
                                    String url = ip + "/api/doctorPlus/deleteContact?access_token=" + accessToken;
                                    JSONObject obj = new JSONObject();
                                    obj.put("connID", contactId);
                                    obj.put("docID", doctorId);
                                    obj.put("status", "EXIT");
                                    HttpPost post = new HttpPost(obj);
                                    post.delegate = DoctorPostsActivity.this;
                                    post.execute(url);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                break;
            case R.id.message:
//                System.out.print("coming here to open dialog");
//                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//                LayoutInflater inflater = getLayoutInflater();
//                View dialogView = inflater.inflate(R.layout.custom_share_layout, null);
//                dialog.setView(dialogView);
//                RelativeLayout dialogLayout = (RelativeLayout) dialogView.findViewById(R.id.dialogLayout);
//                //dialogLayout.setBackgroundColor(R.color.home_highted);
//                TextView title = (TextView)dialogView.findViewById(R.id.title);
//                title.setText("Group | Contact Name");
//                Button uploadImage = (Button)dialogView.findViewById(R.id.uploadImage);
//                uploadImage.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
//                        AlertDialog.Builder builder = new AlertDialog.Builder(DoctorPostsActivity.this);
//                        builder.setTitle("Add Photo!");
//                        builder.setItems(items, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int item) {
//                                if (items[item].equals("Take Photo")) {
//                                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                    startActivityForResult(takePicture, 0);
//                                } else if (items[item].equals("Choose from Library")) {
//                                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                                    startActivityForResult(pickPhoto , 1);
//                                } else if (items[item].equals("Cancel")) {
//                                    dialog.dismiss();
//                                }
//                            }
//                        });
//                        builder.show();
//                    }
//                });
//                //title.setTextColor(R.color.home_highted);
//                //title.setBackgroundColor(R.color.home_button);
//                final AlertDialog dialogShow = dialog.create();
//                dialogShow.show();
//                Button okButton = (Button)dialogView.findViewById(R.id.okButton);
//                okButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialogShow.dismiss();
//                    }
//                });
                break;
            case R.id.addContct:
                try {
                    String ip = HttpUrl.COMMONURL;
                    String accessToken = SignIn.GET_ACCESS_TOKEN();
                    String url = ip + "/addContacts?token=" + accessToken;
                    int[] doctorUniqId = {doctorId};
                    JSONObject obj = new JSONObject();
                    obj.put("connIdList", doctorUniqId);
                    HttpPost post = new HttpPost(obj);
                    post.delegate = DoctorPostsActivity.this;
                    post.execute(url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
//        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//
//        Bitmap bitmap;
//        switch(requestCode) {
//            case 0:
//                if(resultCode == RESULT_OK){
//                    Uri selectedImage = imageReturnedIntent.getData();
//
//                    if(selectedImage != null) {
//                        try {
//                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
//                            //myBase64Image = encodeToBase64(bitmap, Bitmap.CompressFormat.PNG, 30);
//                            //System.out.print(myBase64Image);
////                            ob = new BitmapDrawable(getResources(), bitmap);
////                            imageView.setBackgroundDrawable(ob);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        Toast.makeText(this, "Please select image", Toast.LENGTH_LONG).show();
//                    }
//                }
//                break;
//            case 1:
//                if(resultCode == RESULT_OK){
//                    Uri selectedImage = imageReturnedIntent.getData();
//                    if(selectedImage != null) {
//                        try {
//                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
//                            //ob = new BitmapDrawable(getResources(), bitmap);
//
//                            //System.out.print(myBase64Image);
//                            //imageView.setBackgroundDrawable(ob);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        Toast.makeText(this, "Please select image", Toast.LENGTH_LONG).show();
//                    }
//                }
//                break;
//        }
//    }

    @Override
    public void response(String result) {
        System.out.println(result);
        if(result != null) {
            try {
                Object json = new JSONTokener(result).nextValue();
                if(json instanceof JSONArray) {
                    JSONArray array = new JSONArray(result);
                    if(array.length() > 0) {
                        initializeArray(array);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //finish();
        } else {
            Toast.makeText(this, "Something went wrong, please try after sometime.", Toast.LENGTH_LONG).show();
        }

    }

    private void initializeArray(JSONArray resultArray) {
        postAvailable.setVisibility(View.GONE);
        CommonAdapter adapter = new CommonAdapter(this, resultArray);
        postList.setAdapter(adapter);
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        private ImageView buttonImage;

        private LoadImage(ImageView buttonImage) {
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
                BitmapDrawable bdrawable = new BitmapDrawable(getResources(),bitmapImage);
                buttonImage.setBackground(bdrawable);
            }
        }
    }

}

