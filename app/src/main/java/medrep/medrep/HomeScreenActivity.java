package medrep.medrep;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.app.util.GlobalVariables;

import org.w3c.dom.Text;

public class HomeScreenActivity extends AppCompatActivity implements View.OnClickListener{

//    FrameLayout frameLayout;
    private Button signInButton;
    private Button registerButton;
    private Typeface typeface;

    private TextView medRepTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setView();
    }

    private void setView(){

        typeface = GlobalVariables.getTypeface(HomeScreenActivity.this);

        signInButton = (Button) this.findViewById(R.id.signButton);
        signInButton.setTypeface(typeface);
        signInButton.setText(getString(R.string.sign_in));
        signInButton.setOnClickListener(this);

        registerButton = (Button) this.findViewById(R.id.registerButton);
        registerButton.setTypeface(typeface);
        registerButton.setText(getString(R.string.register));
        registerButton.setOnClickListener(this);

        medRepTextView = (TextView) this.findViewById(R.id.medrep_textview);
        medRepTextView.setTypeface(typeface);
    }

    @Override
    public void onClick(View v) {

        if(v == signInButton) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }else if(v == registerButton){
            Intent intent = new Intent(this, RegisterCategoryActivity.class);
            startActivity(intent);
//            Intent intent = new Intent(this, DoctorsMyContactActivity.class);
//            startActivity(intent);
//            Intent intent = new Intent(this, NoContactActivity.class);
//            startActivity(intent);
//            Intent intent = new Intent(this, CreateGroupActivity.class);
//            startActivity(intent);
        }
    }


}
