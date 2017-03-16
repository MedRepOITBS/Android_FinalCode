package pharma;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.db.Company;
import com.app.pojo.MyProfile;
import com.app.task.ImageUploadTask;
import com.app.util.PharmaRegBean;
import com.app.util.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import medrep.medrep.DoctorGetProfileWats;
import medrep.medrep.PhotoDialogFragment;
import medrep.medrep.R;
import medrep.medrep.SplashScreen;
import pharma.model.PharmaRepProfile;

/**
 * Created by admin on 9/26/2015.
 */
public class PharmaUpdateActivity extends AppCompatActivity implements PhotoDialogFragment.PhotoSelectedListener{

    private Uri fileUri;

    public static PharmaRepProfile REP_PROFILE;
    Button update_btn/*,back_btn*/;
//    TextView tv_mobile_number;
//    TextView tv_alternate_mobile_number;
//    TextView tv_emailr,tv_alterne_email;
//    TextView tv_address_line,tv_address_line2;
//    PharmaRegBean pharmaRegBean;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharma_profile_finalize);
        update_btn=(Button)findViewById(R.id.nextButton);
        update_btn.setText("UPDATE");

//        pharmaRegBean=new PharmaRegBean().getInstance();

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*tv_mobile_number=(TextView)findViewById(R.id.tv_moblie);
        tv_alternate_mobile_number=(TextView)findViewById(R.id.tv_phone_no);
        tv_emailr=((TextView)findViewById(R.id.tv_email)).setText(REP_PROFILE.getEmailId());;
        tv_alterne_email=(TextView)findViewById(R.id.tv_email_alter);
        tv_address_line=(TextView)findViewById(R.id.tv_adress1);
        tv_address_line2=(TextView)findViewById(R.id.tv_adress2);

        tv_emailr.setText(REP_PROFILE.getEmailId());
        tv_alternate_mobile_number.setText(REP_PROFILE.getMobileNo());
        tv_alterne_email.setText(REP_PROFILE.getAlternateEmailId());
        tv_mobile_number.setText(REP_PROFILE.getPhoneNo());
        tv_address_line.setText(REP_PROFILE.getLocations().get(0).getAddress1());*/

        ((TextView)findViewById(R.id.tv_profilename)).setText(REP_PROFILE.getFirstName() + " " + REP_PROFILE.getLastName());
        ((TextView)findViewById(R.id.tv_email)).setText(REP_PROFILE.getEmailId());
     //   ((TextView) findViewById(R.id.tv_phone_no)).setVisibility(View.GONE));
        ((TextView) findViewById(R.id.tv_phone_no)).setText(REP_PROFILE.getPhoneNo());
        ((TextView) findViewById(R.id.tv_email_alter)).setText(REP_PROFILE.getAlternateEmailId());
        ((TextView) findViewById(R.id.tv_moblie)).setText(REP_PROFILE.getMobileNo());

        ArrayList<Company.Location> tempLocations = REP_PROFILE.getLocations();

        if(tempLocations != null && tempLocations.size() > 0){
            ((TextView) findViewById(R.id.tv_adress1)).setText(tempLocations.get(0).getAddress1());
            ((TextView) findViewById(R.id.tv_adress2)).setText(tempLocations.get(0).getAddress2());
        }

        /*if(REP_PROFILE.getProfilePicture() != null && REP_PROFILE.getProfilePicture().getData() != null && !REP_PROFILE.getProfilePicture().getData().trim().equals("")){
            ImageView profilePicIV = (ImageView) findViewById(R.id.profile_picture);

            Bitmap bmp = Utils.decodeBase64(REP_PROFILE.getProfilePicture().getData());

            System.out.println("bmp.getWidth(): " + bmp.getWidth());
            System.out.println("bmp.getHeight(): " + bmp.getHeight());


            if(bmp != null){
                profilePicIV.setImageBitmap(bmp);
            }
        }*/

        /*tv_emailr.setText(pharmaRegBean.getMdEmailId().replace("null",""));
        tv_alternate_mobile_number.setText(pharmaRegBean.getMdMobileNumber().replace("null", ""));
        tv_alterne_email.setText(pharmaRegBean.getMdEmailId().replace("null", ""));
        tv_mobile_number.setText(pharmaRegBean.getMdMobileNumber().replace("null", ""));
        tv_address_line.setText(pharmaRegBean.getMdaddress1());*/
       // tv_address_line2.setText(pharmaRegBean.getMdA);


        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.isPharmaUpdate=true;
                Intent i=new Intent(getApplicationContext(),PhramaRegistrationTwo.class);
                startActivity(i);
            }
        });

        findViewById(R.id.edit_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoDialogFragment photoDialogFragment = new PhotoDialogFragment();
                photoDialogFragment.show(getSupportFragmentManager(), "show");
            }
        });

        findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.edit_first_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhramaRegistrationTwo.class);
                intent.putExtra(PhramaRegistrationTwo.EDIT_PROFILE, true);
                startActivity(intent);
            }
        });

        findViewById(R.id.edit_address_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PharmaRegistationAddress.class);
                intent.putExtra(PhramaRegistrationTwo.EDIT_PROFILE, true);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        ((TextView)findViewById(R.id.tv_profilename)).setText(REP_PROFILE.getFirstName() + " " + REP_PROFILE.getLastName());
        ((TextView)findViewById(R.id.tv_email)).setText(REP_PROFILE.getEmailId());

        if(REP_PROFILE.getPhoneNo() != null && REP_PROFILE.getPhoneNo().trim().length() > 0){
            ((TextView)findViewById(R.id.tv_phone_no)).setText(REP_PROFILE.getPhoneNo());
        }else{
            findViewById(R.id.tv_aler_mobile_hdr).setVisibility(View.GONE);
            findViewById(R.id.tv_phone_no).setVisibility(View.GONE);
        }

        if(REP_PROFILE.getAlternateEmailId() != null && REP_PROFILE.getAlternateEmailId().trim().length() > 0){
            ((TextView)findViewById(R.id.tv_email_alter)).setText(REP_PROFILE.getAlternateEmailId());
        }else{
            findViewById(R.id.tv_aler_email_hdr).setVisibility(View.GONE);
            findViewById(R.id.tv_email_alter).setVisibility(View.GONE);
        }

        ((TextView) findViewById(R.id.tv_phone_no)).setText(REP_PROFILE.getMobileNo());

        ArrayList<Company.Location> tempLocations = REP_PROFILE.getLocations();

        if(tempLocations != null && tempLocations.size() > 0){
            ((TextView) findViewById(R.id.tv_adress1)).setText(tempLocations.get(0).getAddress1());
            ((TextView) findViewById(R.id.tv_adress2)).setText(tempLocations.get(0).getAddress2());
        }

        if(REP_PROFILE.getProfilePicture() != null && REP_PROFILE.getProfilePicture().getData() != null && !REP_PROFILE.getProfilePicture().getData().trim().equals("")){
            ImageView profilePicIV = (ImageView) findViewById(R.id.profile_picture);

            Bitmap bmp = Utils.decodeBase64(REP_PROFILE.getProfilePicture().getData(), false);

            System.out.println("bmp.getWidth(): " + bmp.getWidth());
            System.out.println("bmp.getHeight(): " + bmp.getHeight());

            if(bmp != null && (bmp.getHeight() <= 256 || bmp.getHeight() <= 256)){
                profilePicIV.setImageBitmap(bmp);
            }else if(bmp != null){
                bmp.recycle();
                bmp = null;
                bmp = Utils.decodeBase64(REP_PROFILE.getProfilePicture().getData());
                if(bmp != null){
                    profilePicIV.setImageBitmap(bmp);
                }
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

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 100:
                if (requestCode == 100 && resultCode == RESULT_OK) {


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

                    final ImageView profilePicIV = (ImageView) findViewById(R.id.profile_picture);

                    profilePicIV.post(new Runnable() {
                        @Override
                        public void run() {
                            profilePicIV.setImageBitmap(temp);
                        }
                    });



                    String imageBase64 = Utils.bitmapToBase64(temp);

                    new ImageUploadTask(PharmaUpdateActivity.this, imageBase64).execute(REP_PROFILE.getEmailId());

                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    final Uri selectedImage = imageReturnedIntent.getData();

                    final ImageView profilePicIV = (ImageView) findViewById(R.id.profile_picture);

                    profilePicIV.post(new Runnable() {
                        @Override
                        public void run() {
                            profilePicIV.setImageURI(selectedImage);
                        }
                    });

                    String picturePath = getRealPathFromURI(selectedImage);

//                    String imageBase64 = Utils.imageAsBase64(picturePath);
                    String imageBase64 = Utils.bitmapToBase64(Utils.decodeSampledBitmapFromResource(picturePath, 256, 256));
                    new ImageUploadTask(PharmaUpdateActivity.this, imageBase64).execute(REP_PROFILE.getEmailId());
                }
                break;
        }
    }

}

