package medrep.medrep;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pojo.AddressProfile;
import com.app.pojo.DoctorProfile;
import com.app.pojo.MyProfile;
import com.app.task.ImageUploadTask;
import com.app.util.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by samara on 10/10/2015.
 */
public class DoctorGetProfileWats extends AppCompatActivity implements PhotoDialogFragment.PhotoSelectedListener{

    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_profile_finalize);

        final MyProfile profile = MyProfile.getInstance();

//        com.app.db.DoctorProfile.ProfilePicture profilePicture = profile.getProfilePicture();


        com.app.db.DoctorProfile.ProfilePicture profilePicture = profile.getProfilePicture();

        String pictureData = (profilePicture == null)?null:profilePicture.getData();

//        Bitmap bmp = null;

        /*if(pictureData != null && pictureData.trim().length() > 0){
            if (pictureData != null && pictureData.trim().length() > 0) {
                bmp = Utils.decodeBase64(pictureData);
                if(bmp != null){
                    ((ImageView) findViewById(R.id.profile_picture)).setImageBitmap(bmp);
                }
            }
        }*/

        Bitmap bmp = Utils.decodeBase64(pictureData);

        if(bmp != null && (bmp.getHeight() <= 256 || bmp.getHeight() <= 256)){
            ((ImageView) findViewById(R.id.profile_picture)).setImageBitmap(bmp);
        }else if(bmp != null){
            bmp.recycle();
            bmp = null;
            bmp = Utils.decodeBase64(pictureData);
            if(bmp != null){
                ((ImageView) findViewById(R.id.profile_picture)).setImageBitmap(bmp);
            }
        }

        
       /* if(profilePicture != null && profilePicture.getData() != null && !profilePicture.getData().trim().equals("")){
            ImageView profilePicIV = (ImageView) findViewById(R.id.profile_picture);

            Bitmap bmp = Utils.decodeBase64(profilePicture.getData());

            if(bmp != null){
                profilePicIV.setImageBitmap(bmp);
            }
        }*/

        ((TextView) findViewById(R.id.tv_name)).setText(profile.getFirstname() + " " + profile.getLastname());


        /*Button update_btn = (Button) findViewById(R.id.nextButton);
        update_btn.setText("UPDATE");

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorGetProfileWats.this, RegisterActivity.class);
                intent.putExtra("myprofile", true);
//                intent.putExtra("accesstoken", Utils.GET_ACCESS_TOKEN(DoctorGetProfileWats.this));
                startActivity(intent);

                finish();
            }
        });*/

        findViewById(R.id.edit_first_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   DoctorProfile doc_reg_id = DoctorProfile.getInstance();
               // doc_reg_id.setDoctorId(profile.getDoctorId());
                DoctorProfile doctorProfile = DoctorProfile.getInstance();
                doctorProfile.setFirstName(profile.getFirstname());
                doctorProfile.setDoctorId(profile.getRegistrationNumber());
                doctorProfile.setLastName(profile.getLastname());
                doctorProfile.setMobileNumber(profile.getMobileNumber());
                doctorProfile.setAltMobileNumber(profile.getAltMobileNumber());
                doctorProfile.setEmail(profile.getEmail());
                doctorProfile.setAltEmail(profile.getAltEmail());
                doctorProfile.setAddressArrayList(profile.getAddressArrayList());
                doctorProfile.setSelectedCat(profile.getSelectedCat());
                doctorProfile.setDoctorId(profile.getDoctorId());
                doctorProfile.setTherapeuticID(profile.getTherapeuticID());
                doctorProfile.setPassword(profile.getPassword());

                Intent intent = new Intent(DoctorGetProfileWats.this, RegisterActivity.class);
                intent.putExtra("myprofile", true);
//                intent.putExtra("accesstoken", Utils.GET_ACCESS_TOKEN(DoctorGetProfileWats.this));
                startActivity(intent);

//                finish();
            }
        });

        findViewById(R.id.edit_address_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DoctorProfile doctorProfile = DoctorProfile.getInstance();
                doctorProfile.setFirstName(profile.getFirstname());
                doctorProfile.setLastName(profile.getLastname());
                doctorProfile.setMobileNumber(profile.getMobileNumber());
                doctorProfile.setAltMobileNumber(profile.getAltMobileNumber());
                doctorProfile.setEmail(profile.getEmail());
                doctorProfile.setAltEmail(profile.getAltEmail());
                doctorProfile.setAddressArrayList(profile.getAddressArrayList());
                doctorProfile.setSelectedCat(profile.getSelectedCat());
                doctorProfile.setDoctorId(profile.getDoctorId());
                doctorProfile.setPassword(profile.getPassword());
                doctorProfile.setTherapeuticID(profile.getTherapeuticID());

                Intent intent = new Intent(DoctorGetProfileWats.this, RegisterActivity.class);
                intent.putExtra("myprofile", true);
                intent.putExtra("address", true);
//                intent.putExtra("accesstoken", Utils.GET_ACCESS_TOKEN(DoctorGetProfileWats.this));
                startActivity(intent);

//                finish();
            }
        });

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((TextView) findViewById(R.id.tv_moblie)).setText(profile.getMobileNumber());

        if(profile.getAltMobileNumber() != null && profile.getAltMobileNumber().trim().length() > 0){
            ((TextView)findViewById(R.id.tv_phone_no)).setText(profile.getAltMobileNumber());
        }else{
            findViewById(R.id.tv_aler_mobile_hdr).setVisibility(View.GONE);
            findViewById(R.id.tv_phone_no).setVisibility(View.GONE);
        }

        ((TextView)findViewById(R.id.tv_email)).setText(profile.getEmail());


        if(profile.getAltEmail() != null && profile.getAltEmail().trim().length() > 0){
            ((TextView)findViewById(R.id.tv_email_alter)).setText(profile.getAltEmail());
        }else{
            findViewById(R.id.tv_aler_email_hdr).setVisibility(View.GONE);
            findViewById(R.id.tv_email_alter).setVisibility(View.GONE);
        }



        ArrayList<AddressProfile> addressList = profile.getAddressArrayList();

        if(addressList != null && addressList.size() > 0){
            ((TextView)findViewById(R.id.tv_adress1)).setText(profile.getAddressArrayList().get(0).getAddress1());
            ((TextView)findViewById(R.id.tv_adress2)).setText(profile.getAddressArrayList().get(0).getAddress2());
        }

        findViewById(R.id.edit_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoDialogFragment photoDialogFragment = new PhotoDialogFragment();
                photoDialogFragment.show(getSupportFragmentManager(), "show");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        final MyProfile profile = MyProfile.getInstance();

        ((TextView) findViewById(R.id.tv_moblie)).setText(profile.getMobileNumber());
        ((TextView)findViewById(R.id.tv_email)).setText(profile.getEmail());
        ((TextView) findViewById(R.id.tv_name)).setText(profile.getFirstname() + " " + profile.getLastname());

        ArrayList<AddressProfile> addressList = profile.getAddressArrayList();

        if(addressList != null && addressList.size() > 0){
            ((TextView)findViewById(R.id.tv_adress1)).setText(profile.getAddressArrayList().get(0).getAddress1());
            ((TextView)findViewById(R.id.tv_adress2)).setText(profile.getAddressArrayList().get(0).getAddress2());
        }

        if(profile.getAltMobileNumber() != null && profile.getAltMobileNumber().trim().length() > 0){
            ((TextView)findViewById(R.id.tv_phone_no)).setText(profile.getAltMobileNumber());
        }else{
            findViewById(R.id.tv_aler_mobile_hdr).setVisibility(View.GONE);
            findViewById(R.id.tv_phone_no).setVisibility(View.GONE);
        }

        if(profile.getAltEmail() != null && profile.getAltEmail().trim().length() > 0){
            ((TextView)findViewById(R.id.tv_email_alter)).setText(profile.getAltEmail());
        }else{
            findViewById(R.id.tv_aler_email_hdr).setVisibility(View.GONE);
            findViewById(R.id.tv_email_alter).setVisibility(View.GONE);
        }
    }

    @Override
    public void selectedGalleryApp() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
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

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 100:
                if(requestCode == 100 && resultCode == RESULT_OK){
//                    Uri selectedImage = imageReturnedIntent.getData();

                    Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");

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
                                photo = rotateImage(photo, 90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                photo = rotateImage(photo, 180);
                                break;
                            // etc.
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    /*ImageView profilePicIV = (ImageView) findViewById(R.id.profile_picture);

                    profilePicIV.setImageBitmap(photo);*/

                    final Bitmap temp = Bitmap.createScaledBitmap(photo, 256, 256, false);

                    final ImageView profilePicIV = (ImageView) findViewById(R.id.profile_picture);

                    profilePicIV.post(new Runnable() {
                        @Override
                        public void run() {
                            profilePicIV.setImageBitmap(temp);
                        }
                    });
                    

                    /*Intent intent=new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);*/

                    MyProfile profile = MyProfile.getInstance();

//                    String imageBase64 = Utils.imageAsBase64(picturePath);

//                    String imageBase64 = Utils.bitmapToBase64(Utils.decodeSampledBitmapFromResource(picturePath, 256, 256));
                    String imageBase64 = Utils.bitmapToBase64(temp);

                    new ImageUploadTask(DoctorGetProfileWats.this, imageBase64).execute(profile.getEmail());

                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    final Uri selectedImage = imageReturnedIntent.getData();

                    final ImageView profilePicIV = (ImageView) findViewById(R.id.profile_picture);

                    profilePicIV.post(new Runnable() {
                        @Override
                        public void run() {
                            profilePicIV.setImageURI(selectedImage);
                        }
                    });



                    String picturePath = getRealPathFromURI(selectedImage);
                    System.out.println(picturePath);
                    /*Intent intent=new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);*/

                    MyProfile profile = MyProfile.getInstance();

//                    String imageBase64 = Utils.imageAsBase64(picturePath);

                    String imageBase64 = Utils.bitmapToBase64(Utils.decodeSampledBitmapFromResource(picturePath, 256, 256));

                    new ImageUploadTask(DoctorGetProfileWats.this, imageBase64).execute(profile.getEmail());
                }
                break;
        }
    }
}
