package pharma;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import medrep.medrep.R;

/**
 * Created by admin on 9/27/2015.
 */
public class PharmaNewProductCampain extends AppCompatActivity implements View.OnClickListener {


    PharmaNewProductCampain _activity;

    ImageView trackCompaingn;
    ImageView pcompanyname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharma_compain_details);
        _activity=this;


        pcompanyname=(ImageView)findViewById(R.id.p_company_name);
        pcompanyname.setOnClickListener(_activity);

    }

    @Override
    public void onClick(View v) {

   if(v.getId()== R.id.p_company_name){

            Intent intent=new Intent(_activity, PharmaCampainDetails.class);
            startActivity(intent);
        }

    }
}
