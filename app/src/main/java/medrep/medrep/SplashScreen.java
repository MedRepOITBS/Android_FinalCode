package medrep.medrep;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.app.pojo.RefreshToken;
import com.app.pojo.SignIn;
import com.app.util.UserRoles;

import java.util.Calendar;

import pharma.PharmaDashBoard;

public class SplashScreen extends AppCompatActivity implements Animation.AnimationListener{

    private ImageView medRepLogoImageView;

    public static final String PROFILE_NAME_KEY = "ProfileName";
    public static final String SIGNIN_PREF = "SignInPref";
    public static final String IS_LOGGED_IN_KEY = "IsLoggedIn";
    public static final String ROLE_ID = "RoleId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences signInPref = getSharedPreferences(SIGNIN_PREF, MODE_PRIVATE);

        boolean alreadyLoggedIn = signInPref.getBoolean(IS_LOGGED_IN_KEY, false);
        String profileName = signInPref.getString(PROFILE_NAME_KEY, "User");

        String refreshTokenStr = signInPref.getString("RefreshToken", null);
        long refreshExpiry = signInPref.getLong("RefreshExpiry", -1);

        RefreshToken refreshToken = new RefreshToken(refreshTokenStr, refreshExpiry);

        String accessToken = signInPref.getString("AccessToken", null);
        long accessExpiresIn = (signInPref.getLong("AccessExpiresIn", -1) - Calendar.getInstance().getTimeInMillis()) / 1000;
        long accessExpiry = signInPref.getLong("AccessExpiry", -1);

        int roleID = signInPref.getInt(ROLE_ID, -1);

        SignIn.SET_ACCESS_TOKEN(accessToken);
        SignIn.SET_EXPIRES_IN(accessExpiresIn);
        SignIn.SET_TOKEN_EXPIRY_IN_MILLIS(accessExpiry);
        SignIn.setRefreshToken(refreshToken);

        System.out.println("refreshTokenStr: " + refreshTokenStr);
        System.out.println("refreshExpiry: " + refreshExpiry);
        System.out.println("accessToken: " + accessToken);
        System.out.println("accessExpiresIn: " + accessExpiresIn);
        System.out.println("accessExpiry: " + accessExpiry);
        System.out.println("roleID: " + roleID);

        if(refreshTokenStr == null || refreshExpiry == -1 || accessToken == null || accessExpiresIn == -1 || accessExpiry == -1 || roleID == -1){
            alreadyLoggedIn = false;
        }

        System.out.println("alreadyLoggedIn: " + alreadyLoggedIn);

        if(alreadyLoggedIn){
            finish();

            Intent intent;

            System.out.println("roleID: " + roleID);


            switch (roleID){
                case UserRoles.DOCTOR:
                    intent = new Intent(SplashScreen.this, DoctorDashboard.class);
//                                intent.putExtra("signin",(SignIn)o);
                    intent.putExtra(PROFILE_NAME_KEY, profileName);
                    startActivity(intent);
                    break;
                case UserRoles.PHARMA_MEDREP:

                    System.out.println("Pharma User");

                    intent=new Intent(SplashScreen.this, PharmaDashBoard.class);
//                                intent.putExtra("signin",(SignIn)o);
                    intent.putExtra(PROFILE_NAME_KEY, profileName);
                    startActivity(intent);
                    break;
                case UserRoles.PHARMA_MANAGER_MEDREP:

                    System.out.println("Pharma Manager");

                    intent=new Intent(SplashScreen.this, PharmaDashBoard.class);
//                                intent.putExtra("signin",(SignIn)o);
                    intent.putExtra(PROFILE_NAME_KEY, profileName);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }else{
            setContentView(R.layout.activity_splash);

            medRepLogoImageView = (ImageView) this.findViewById(R.id.medrep_logo_imageview);
            Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pluse);
            pulse.setAnimationListener(this);
            medRepLogoImageView.startAnimation(pulse);
//        finish();
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Log.d("TAG","Animation ended u can continue to another screen");
       /* Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);*/
        Intent intent = new Intent(this, HomeScreenActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
