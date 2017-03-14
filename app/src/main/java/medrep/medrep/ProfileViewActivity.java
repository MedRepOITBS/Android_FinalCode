package medrep.medrep;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adapter.CommonAdapter;
import com.app.adapter.DoctorGroupListViewAdapter;
import com.app.adapter.ExpandableListAdapter;
import com.app.interfaces.GetResponse;
import com.app.pojo.SignIn;
import com.app.task.NotificationGetTask;
import com.app.util.HttpUrl;
import com.app.util.RoundedQuickContactBadge;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

public class ProfileViewActivity extends AppCompatActivity implements GetResponse {

    private ListView workExperience;
    private ImageView changePic;
    Uri imageUri;
    private static final int PICK_IMAGE = 1;
    private static final int PICK_Camera_IMAGE = 2;
    private Bitmap bitmap;
    private ProgressDialog dialog;
    private static JSONArray map;
    public static GetResponse delegate;
    private String imageName, dPicture;
    private RoundedQuickContactBadge imageViewRound;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_profile_view);
        LinearLayout searchLayout = (LinearLayout) findViewById(R.id.searchLayout);
        searchLayout.setVisibility(View.GONE);
        HorizontalScrollView horizontalScroll = (HorizontalScrollView) findViewById(R.id.horizontalScroll);
        horizontalScroll.setVisibility(View.GONE);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("My Profile");
        workExperience = (ListView) findViewById(R.id.workExperience);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.profile_listview_header, workExperience,
                false);
        workExperience.addHeaderView(header, null, false);

        imageViewRound = (RoundedQuickContactBadge) findViewById(R.id.photoView);
        changePic = (ImageView) findViewById(R.id.change_pic);
        changePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileViewActivity.this);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo")) {
                            //define the file-name to save photo taken by Camera activity
                            Date d = new Date();
                            CharSequence s = DateFormat.format("MMddyy_hhmmss", d.getTime());
                            imageName = "IMG_" + s.toString() + ".jpg";
                            //create parameters for Intent with filename
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, imageName);
                            values.put(MediaStore.Images.Media.DESCRIPTION, "Image captured by camera");
                            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                            startActivityForResult(intent, PICK_Camera_IMAGE);
                        } else if (items[item].equals("Choose from Library")) {
                            try {
                                Intent gintent = new Intent();
                                gintent.setType("image/*");
                                gintent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(
                                        Intent.createChooser(gintent, "Select Picture"),
                                        PICK_IMAGE);
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(),
                                        e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        Button onBackClick = (Button) findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImageUri = null;
        String filePath = null;
        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    selectedImageUri = data.getData();
                    try {
                        Cursor returnCursor =
                                getContentResolver().query(selectedImageUri, null, null, null, null);
                        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        returnCursor.moveToFirst();
                        imageName = returnCursor.getString(nameIndex);
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                        new ImageUploadTask().execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case PICK_Camera_IMAGE:
                if (resultCode == RESULT_OK) {
                    //use imageUri here to access the image
                    //selectedImageUri = imageUri;

                    selectedImageUri = data.getData();
                    try {
                        Cursor returnCursor =
                                getContentResolver().query(selectedImageUri, null, null, null, null);
                        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        returnCursor.moveToFirst();
                        imageName = returnCursor.getString(nameIndex);
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                        new ImageUploadTask().execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        /*if(selectedImageUri != null){
            try {
                // OI FILE Manager
                String filemanagerstring = selectedImageUri.getPath();
                Log.d("qwerty","1 "+filemanagerstring);
                // MEDIA GALLERY
                String selectedImagePath = getPath(selectedImageUri);

                if (selectedImagePath != null) {
                    filePath = selectedImagePath;
                    Log.d("qwerty","2 "+filePath);
                } else if (filemanagerstring != null) {
                    filePath = filemanagerstring;
                    Log.d("qwerty","3 "+filePath);
                } else {
                    Toast.makeText(getApplicationContext(), "Unknown path",
                            Toast.LENGTH_LONG).show();
                }

                if (filePath != null) {
    //                decodeFile(filePath);
                    Log.d("qwerty","4");
                    new ImageUploadTask().execute();
                } else {
                    bitmap = null;
                }
            } catch (Exception e) {
                Log.d("qwerty","5");
                Toast.makeText(getApplicationContext(), "Internal error",
                        Toast.LENGTH_LONG).show();
            }
        }*/

    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ProfileView Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://medrep.medrep/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    /*public String getPath(Uri uri) {
        Log.d("qwerty","6");
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            Log.d("qwerty"," a."+cursor.toString());
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            Log.d("qwerty"," b,"+column_index);
            cursor.moveToFirst();
            Log.d("qwerty","7 "+cursor.getString(column_index));
            return cursor.getString(column_index);
        } else
            return null;




    }*/

    public class ImageUploadTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog = ProgressDialog.show((Context) delegate, "please wait", "Uploading");
//            dialog.show();
            Log.d("qwerty", "8");
        }

        @Override
        protected String doInBackground(String... params) {
            String JsonResponse = null;
            BufferedReader reader = null;
            try {

                BitmapFactory.Options bfo = new BitmapFactory.Options();
                bfo.inSampleSize = 2;
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
                byte[] ba = bao.toByteArray();
                String encodedImage = Base64.encodeToString(ba, Base64.DEFAULT);
                SharedPreferences signPrefs = getSharedPreferences(SplashScreen.SIGNIN_PREF, MODE_PRIVATE);
                String userName = signPrefs.getString("username", null);
                Log.d("123", "loginId " + userName);
                Log.d("123", "imgData " + encodedImage);
                Log.d("123", "fileName " + imageName);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("imgData", encodedImage);
                //           jsonObject.put("dpId", null);
                //           jsonObject.put("mimeType", "JPEG");
                jsonObject.put("loginId", userName);
                //           jsonObject.put("securityId", null);
                jsonObject.put("fileName", imageName);
                String data = jsonObject.toString();


              //  URL url = new URL("http://183.82.106.234:8080/medrep-web/uploadDP");
                URL url = new URL("http://122.175.50.252:8080/MedRepApplication/preapi/registration/uploadDP");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(data);
                writer.close();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine + "\n");
                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }
                JsonResponse = buffer.toString();
                //response data
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == 200) {
                    Log.d("123", "resp " + JsonResponse);
                    return JsonResponse;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("qwerty", "9");
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("qwerty", "10");
            //           dialog.dismiss();
  //          bitmap.recycle();
            Toast.makeText(getApplicationContext(), "Successfully Updated Profile Pic", Toast.LENGTH_SHORT).show();
//            delegate.response(result);
//            delegate = null;
        }
    }

    /*  public void decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 150;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);

        imgView.setImageBitmap(bitmap);

    }*/

    @Override
    protected void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        NotificationGetTask getData = new NotificationGetTask();
        getData.delegate = this;
        String accessToken = SignIn.GET_ACCESS_TOKEN();
        String ip = HttpUrl.COMMONURL;
        String url = ip + "/doctorinfo/fetch?token=" + accessToken;
        getData.execute(url);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ProfileView Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://medrep.medrep/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    private void initializeListView(JSONObject result) {

        CommonAdapter adapter = new CommonAdapter(this, result);
        workExperience.setAdapter(adapter);
    }

    @Override
    public void response(String result) {
        System.out.println(result);

        try {
            if (result != null) {
                JSONObject object = new JSONObject(result);
                String status = object.getString("status");
                if (status.equals("success")) {
                    if (object.has("message")) {
                        String message = object.getString("message");
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                        startActivity(getIntent());
                    } else {
                        JSONObject resultObject = object.getJSONObject("result");
                        JSONObject about = resultObject.getJSONObject("about");
                        String doctorName = "Dr." + about.getString("name");
                        TextView personName = (TextView) findViewById(R.id.personName);
                        personName.setText(doctorName);
                        String location = about.getString("location");
                        TextView personAddress = (TextView) findViewById(R.id.personAddress);
                        personAddress.setText(location);
                        String designation = about.getString("designation");
                        TextView specialistText = (TextView) findViewById(R.id.specialistText);
                        specialistText.setText(designation);
                        initializeListView(resultObject);

                        dPicture = resultObject.getString("dPicture");
                        /*if(dPicture != null) {
                            byte[] decodedString = Base64.decode(dPicture, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            imageViewRound.setImageBitmap(decodedByte);
                        }*/

                        new ImageLoadTask().execute();
                    }
                }
            } else {
                Toast.makeText(this, "Internal Error.. Please Try Again", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class ImageLoadTask extends AsyncTask<String, Void, String> {

        Bitmap bmp = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(ProfileViewActivity.this,"Please Wait..","");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {


            try {
                URL url = new URL(dPicture);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            dialog.dismiss();
            imageViewRound.setImageBitmap(bmp);
        }
    }


}
