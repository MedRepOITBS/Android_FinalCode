package pharma;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import medrep.medrep.R;
import medrep.medrep.RegisterCategoryActivity;

/**
 * Created by admin on 9/26/2015.
 */
public class Phrama_Reg_zero extends AppCompatActivity implements View.OnClickListener{

   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pharma_reg_zero,
                container, false);
        return view;
    }*/


    LinearLayout nextButton;
    LinearLayout layout1;
    LinearLayout layout2;
    LinearLayout layout3;
    LinearLayout layout4;

    Phrama_Reg_zero _activity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharma_reg_zero);
        _activity=this;

        nextButton=(LinearLayout)findViewById(R.id.next);
        nextButton.setOnClickListener(_activity);
    }

    @Override
    public void onClick(View v) {

        if(v.getId()== R.id.next)
        {
            Intent intent=new Intent(_activity, PhramaRegistrationTwo.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        Intent intent=new Intent(_activity,RegisterCategoryActivity.class);
        startActivity(intent);


    }
}
