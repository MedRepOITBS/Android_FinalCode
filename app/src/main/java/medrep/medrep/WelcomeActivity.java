package medrep.medrep;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.task.ImageUploadTask;
import com.app.util.GlobalVariables;
import com.app.util.RoundedImageView;
import com.app.util.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener, PhotoDialogFragment.PhotoSelectedListener{

    public static final String USERNAME_KEY = "Uesrname";
    public static final String EMAIL_KEY = "Email";

    private TextView greatTextView;
    private TextView welcomeTextView;
    private TextView addPhotoTextView;
    private Button getStartedButton;
//    private ImageView photoImageView;
    private RoundedImageView photoImageView;
    private String emailid;
    private String username;
    private Uri fileUri;
    Uri selectedImage;
    String picturePath;
    Bitmap photo;
//    String ba1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        username = getIntent().getStringExtra(USERNAME_KEY);
        emailid = getIntent().getStringExtra(EMAIL_KEY);

        setView();
        getBundle();
    }

    private void getBundle(){
//        Bundle b1 = getIntent().getExtras();
//        emailid = b1.getString("email");
//        username = b1.getString("username");
    }

    private void setView() {

        Typeface typeface = GlobalVariables.getTypeface(WelcomeActivity.this);

        greatTextView = (TextView) this.findViewById(R.id.greatTextView);
        greatTextView.setTypeface(typeface);
        greatTextView.setText(getString(R.string.great));

        welcomeTextView = (TextView) this.findViewById(R.id.welcomeTextView);
        welcomeTextView.setTypeface(typeface);
        welcomeTextView.setText("Welcome Dr. " + username);

        addPhotoTextView = (TextView) this.findViewById(R.id.photoTextView);
        addPhotoTextView.setTypeface(typeface);
        addPhotoTextView.setText(getString(R.string.addphoto));
        addPhotoTextView.setOnClickListener(this);

        getStartedButton = (Button) this.findViewById(R.id.startedButton);
        getStartedButton.setTypeface(typeface);
        getStartedButton.setText(getString(R.string.started));
        getStartedButton.setOnClickListener(this);

        photoImageView = (RoundedImageView) this.findViewById(R.id.welcomeImageView);

    }

    @Override
    public void onClick(View v) {
        if(v == addPhotoTextView){
            PhotoDialogFragment photoDialogFragment = new PhotoDialogFragment();
            photoDialogFragment.show(getSupportFragmentManager(), "show");
        }else if(v == getStartedButton ){
//            upload();
            if(picturePath != null && picturePath.trim().equals("")){
//                String imageBase64 = Utils.imageAsBase64(picturePath);

                String imageBase64 = Utils.bitmapToBase64(Utils.decodeSampledBitmapFromResource(picturePath, 256, 256));

                new ImageUploadTask(WelcomeActivity.this, imageBase64).execute(emailid);
            }else if(photo != null){
                String imageBase64 = Utils.bitmapToBase64(Bitmap.createScaledBitmap(photo, 256, 256, false));
                new ImageUploadTask(WelcomeActivity.this, imageBase64).execute(emailid);
            }
            else{
                finish();
            }

        }
    }

    @Override
    public void selectedGalleryApp() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);
    }

    @Override
    public void selectedCameraApp() {

        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(takePicture, 100);
            //one can be replaced with any action code
            //zero can be replaced with any action code
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 100:
                if(requestCode == 100 && resultCode == RESULT_OK){
                    /*selectedImage = imageReturnedIntent.getData();
                    photo = (Bitmap) imageReturnedIntent.getExtras().get("data");

                    Uri tempUri = getImageUri(getApplicationContext(), photo);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
//                    File finalFile = new File(getRealPathFromURI(tempUri));

                    picturePath = getRealPathFromURI(tempUri);
                    System.out.println(getRealPathFromURI(tempUri));*/

                    /*Intent intent=new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);*/

                    picturePath = null;

                    photo = (Bitmap) imageReturnedIntent.getExtras().get("data");

                    Uri tempUri = getImageUri(getApplicationContext(), photo);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
//                    File finalFile = new File(getRealPathFromURI(tempUri));

                    String picturePath = getRealPathFromURI(tempUri);
                    System.out.println(getRealPathFromURI(tempUri));

                    try {
                        ExifInterface ei = new ExifInterface(picturePath);
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                        switch(orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                photo = DoctorGetProfileWats.rotateImage(photo, 90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                photo = DoctorGetProfileWats.rotateImage(photo, 180);
                                break;
                            // etc.
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    final Bitmap temp = Bitmap.createScaledBitmap(photo, 256, 256, false);

                    photoImageView.post(new Runnable() {
                        @Override
                        public void run() {
                            photoImageView.setImageBitmap(temp);
                        }
                    });

                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    photoImageView.setImageURI(selectedImage);

                    picturePath = getRealPathFromURI(selectedImage);
                    System.out.println(picturePath);
                    /*Intent intent=new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);*/
                }
                break;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
}
