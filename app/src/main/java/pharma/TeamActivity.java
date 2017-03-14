package pharma;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.db.Company;
import com.app.db.DoctorProfile;
import com.app.fragments.PharmaNavigationDrawerFragment;
import com.app.pharma.PharmaNotification;
import com.app.pojo.SignIn;
import com.app.util.HttpUrl;
import com.app.util.JSONParser;
import com.app.util.Utils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import medrep.medrep.R;

/**
 * Created by kishore on 30/10/15.
 */
public class TeamActivity extends AppCompatActivity {

    public static JSONArray jsonArray;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharma_team);

        Utils.SET_PHARMA_COMPANY_LOGO(this, (ImageView) findViewById(R.id.p_company_name));

        GetTeamsAsync getTeamsAsync = new GetTeamsAsync();
        getTeamsAsync.execute();

        TextView title = (TextView) findViewById(R.id.back_tv);
        title.setText("Team");

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);

        PharmaNavigationDrawerFragment drawerFragment = (PharmaNavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.id.fragment_navigation_drawer);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private class GetTeamsAsync extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>{
        ProgressDialog pd;
        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

            String url = HttpUrl.PHARMA_GET_MY_TEAM + SignIn.GET_ACCESS_TOKEN();

            JSONParser parser = new JSONParser();
            jsonArray = parser.getJSON_Response(url);
            System.out.println(jsonArray);
            ArrayList<HashMap<String, String>> teamMembers = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                HashMap<String, String> hm = new HashMap<>();
                try {
                    hm.put("displayName", jsonArray.getJSONObject(i).getString("displayName"));
                    teamMembers.add(hm);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

//            Gson g = new Gson();
//            try {
//
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    teamMembers.add(g.fromJson(jsonArray.getString(i), TeamMember.class));
//                    // listener.loading(i,ja.length());
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            System.out.println(teamMembers);
            //parser.jsonParser(jsonArray, TeamMember.class, teamMembers);

            return teamMembers;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(TeamActivity.this);
            pd.setTitle("Getting Team");
            pd.setMessage("Please wait, while we retrieve team.");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> teamMembers) {
            super.onPostExecute(teamMembers);
            System.out.println(teamMembers);
            if(pd != null && pd.isShowing()){
                pd.dismiss();
            }

            if(teamMembers == null || teamMembers.size() == 0){
                Toast.makeText(TeamActivity.this, "No Team Found.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            ListView teamList = (ListView) findViewById(R.id.team_lv);

            teamList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    System.out.println("id: &&&&&&&&&&&&&" + id);
                    Intent intent = new Intent(TeamActivity.this, PharmaRepDetails.class);
                    intent.putExtra("RepId", (int)id);
                    startActivity(intent);
                }
            });
            TeamRecordActivity teamAdpater = new TeamRecordActivity(teamMembers);
            teamList.setAdapter(teamAdpater);
        }
    }

    private class TeamRecordActivity extends BaseAdapter {
        private ArrayList<HashMap<String, String>> values;

        private TeamRecordActivity(ArrayList<HashMap<String, String>> values) {
            this.values = values;
        }

        @Override
        public int getCount() {
            return values.size();
        }

        @Override
        public Object getItem(int position) {
            return values.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(TeamActivity.this);
            convertView = inflater.inflate(R.layout.pharma_team_item, parent, false);
            TextView nameTV = (TextView)convertView.findViewById(R.id.name_tv);
            nameTV.setText(values.get(position).get("displayName"));
            return convertView;
        }
    }

    private class TeamAdapter extends BaseAdapter{
        ArrayList<TeamMember> teamMembers;

        public TeamAdapter(ArrayList<TeamMember> teamMembers) {
            this.teamMembers = teamMembers;
        }

        @Override
        public int getCount() {
            return teamMembers.size();
        }

        @Override
        public TeamMember getItem(int position) {
            return teamMembers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return teamMembers.get(position).getRepId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(TeamActivity.this);
                convertView = inflater.inflate(R.layout.pharma_team_item, parent, false);
                holder = new ViewHolder();
                holder.nameTV = (TextView)convertView.findViewById(R.id.name_tv);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            TeamMember teamMember = teamMembers.get(position);

            String name = teamMember.getFirstName() + teamMember.getLastName();

            if(name == null || name.trim().length() == 0){
                name = "Unknown";
            }

            holder.nameTV.setText(name);

            return convertView;
        }


        class ViewHolder{
            TextView nameTV;
        }
    }

    private class TeamMember{
        private String username;
        private String password;
        private int roleId;
        private String roleName;
        private String firstName;
        private String middleName;
        private String lastName;
        private String alias;
        private String title;
        private String phoneNo;
        private String mobileNo;
        private String emailId;
        private String alternateEmailId;
        private String profilePicture;
        private String status;
        private ArrayList<Company.Location> locations;
        private int repId;
        private int companyId;
        private String companyName;
        private String coveredArea;
        private int managerId;
        private String coveredZone;
        private int therapeuticId;
        private String therapeuticName;
        private String managerEmail;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getRoleId() {
            return roleId;
        }

        public void setRoleId(int roleId) {
            this.roleId = roleId;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public String getEmailId() {
            return emailId;
        }

        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        public String getAlternateEmailId() {
            return alternateEmailId;
        }

        public void setAlternateEmailId(String alternateEmailId) {
            this.alternateEmailId = alternateEmailId;
        }

        public String getProfilePicture() {
            return profilePicture;
        }

        public void setProfilePicture(String profilePicture) {
            this.profilePicture = profilePicture;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public ArrayList<Company.Location> getLocations() {
            return locations;
        }

        public void setLocations(ArrayList<Company.Location> locations) {
            this.locations = locations;
        }

        public int getRepId() {
            return repId;
        }

        public void setRepId(int repId) {
            this.repId = repId;
        }

        public int getCompanyId() {
            return companyId;
        }

        public void setCompanyId(int companyId) {
            this.companyId = companyId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCoveredArea() {
            return coveredArea;
        }

        public void setCoveredArea(String coveredArea) {
            this.coveredArea = coveredArea;
        }

        public int getManagerId() {
            return managerId;
        }

        public void setManagerId(int managerId) {
            this.managerId = managerId;
        }

        public String getCoveredZone() {
            return coveredZone;
        }

        public void setCoveredZone(String coveredZone) {
            this.coveredZone = coveredZone;
        }

        public int getTherapeuticId() {
            return therapeuticId;
        }

        public void setTherapeuticId(int therapeuticId) {
            this.therapeuticId = therapeuticId;
        }

        public String getTherapeuticName() {
            return therapeuticName;
        }

        public void setTherapeuticName(String therapeuticName) {
            this.therapeuticName = therapeuticName;
        }

        public String getManagerEmail() {
            return managerEmail;
        }

        public void setManagerEmail(String managerEmail) {
            this.managerEmail = managerEmail;
        }
    }
}
