package medrep.medrep;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.interfaces.GetResponse;
import com.app.task.PostData;
import com.app.task.RefreshAccessToken;
import com.app.util.HttpUrl;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class CreateGroupActivity extends AppCompatActivity implements GetResponse{

    //private Boolean changeValue = true;
    private EditText groupName;
    private EditText shortDescription;
    private EditText longDescription;
    private Button imageView;
    private Bitmap bitmap = null;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //final LinearLayout parentLinearLayout = (LinearLayout)findViewById(R.id.parentLinearLayout);
//        final TransitionDrawable transition = (TransitionDrawable) parentLinearLayout.getBackground();
//        transition.setCrossFadeEnabled(true);

        groupName = (EditText)findViewById(R.id.groupName);
        shortDescription = (EditText)findViewById(R.id.shortDescription);
        longDescription = (EditText)findViewById(R.id.longDescription);
        imageView = (Button)findViewById(R.id.uploadImage);
        Button backButton = (Button)findViewById(R.id.onBackClick);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String groupId = getIntent().getExtras().getString("groupId");
        if(groupId != "groupId") {
            String groupTitle = getIntent().getExtras().getString("groupName");
            groupName.setText(groupTitle);
            String groupShortDesc = getIntent().getExtras().getString("shortDesc");
            shortDescription.setText(groupShortDesc);
            String groupLongDesc = getIntent().getExtras().getString("longDesc");
            longDescription.setText(groupLongDesc);
            String groupImageData = getIntent().getExtras().getString("imageData");
            if(groupImageData != null) {
                byte [] encodeByte=Base64.decode(groupImageData, Base64.DEFAULT);
                Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
                imageView.setBackground(ob);
            }
            TextView title = (TextView)findViewById(R.id.title);
            title.setText("Create Group");
        }

//        transition.startTransition(5000);
//        transition.reverseTransition(5000);
//        transition.
//        ValueAnimator animator = ObjectAnimator.ofInt(parentLinearLayout, "background", R.drawable.start_gradient_color, R.drawable.end_gradient_color);
//        animator.setDuration(3000);
//        animator.setEvaluator(new ArgbEvaluator());
//        animator.setRepeatCount(ValueAnimator.INFINITE);
//        animator.setRepeatMode(ValueAnimator.REVERSE);
//        animator.start();
//        TimerTask timerTask = new TimerTask() {
//
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        if(changeValue) {
//                            transition.startTransition(10000);
//                            changeValue = false;
//                        } else {
//                            //parentLinearLayout.setBackground(getResources().getDrawable(R.drawable.background_transition));
//                            changeValue = true;
//                            transition.reverseTransition(10000);
//                        }
//                    }
//                });
//
//            }
//        };
//        Timer timer = new Timer();
//        timer.schedule(timerTask, 5000, 10000);
    }

    public void onCreateGroup(View view) {
//        Intent intent = new Intent(this, DoctorsMyContactActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.putExtra("name", "myContacts");
//        startActivity(intent);
        //System.out.println(myBase64Image);
        String groupTitle = groupName.getText().toString();
        String groupShortDesc = shortDescription.getText().toString();
        String groupLongDesc = longDescription.getText().toString();
        String myBase64Image = encodeToBase64(bitmap, Bitmap.CompressFormat.PNG, 10);
        if(groupTitle != null && groupShortDesc != null && groupLongDesc != null && myBase64Image != null) {
            HashMap<String, String> urlParams = new HashMap();
            urlParams.put("image", myBase64Image);
            urlParams.put("groupName", groupTitle);
            urlParams.put("shortDescription", groupShortDesc);
            urlParams.put("longDescription", groupLongDesc);
            PostData data = new PostData(urlParams);
            data.delegate = this;
            String url = HttpUrl.CREATE_GROUP;
            data.execute(url);
            groupName.setText("");
            shortDescription.setText("");
            longDescription.setText("");
        } else {
         Toast.makeText(this, "Please fill all details", Toast.LENGTH_LONG).show();
        }
    }

    public void onImageClick(View view) {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } else if (items[item].equals("Choose from Library")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        BitmapDrawable ob;
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();

                    if(selectedImage != null) {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            //myBase64Image = encodeToBase64(bitmap, Bitmap.CompressFormat.PNG, 30);
                            //System.out.print(myBase64Image);
                            ob = new BitmapDrawable(getResources(), bitmap);
                            imageView.setBackgroundDrawable(ob);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(this, "Please select image", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    if(selectedImage != null) {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            ob = new BitmapDrawable(getResources(), bitmap);

                            //System.out.print(myBase64Image);
                            imageView.setBackgroundDrawable(ob);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(this, "Please select image", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

//    public void onBackButton(View view) {
//        finish();
//    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    @Override
    public void response(String result) {
        if(result != null) {
            JSONObject object = null;
            try {
                Object json = new JSONTokener(result).nextValue();
                if (json instanceof JSONObject) {
                    object = new JSONObject(result);
                    int statusCode = object.getInt("statusCode");
                    if(statusCode == 405) {
                        RefreshAccessToken token = new RefreshAccessToken(this);
                        startActivity(getIntent());
                    }
                } else {
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Something went wrong, please try after sometime.", Toast.LENGTH_LONG).show();
        }
    }
}
