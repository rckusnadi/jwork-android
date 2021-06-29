package com.example.jwork_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
{

    private ArrayList<Recruiter> listRecruiter = new ArrayList<>();
    private ArrayList<Job> jobIdList = new ArrayList<>();
    private HashMap<Recruiter, ArrayList<Job>> childMapping = new HashMap<>();
    private int jobseekerID;
    ExpandableListAdapter listAdapter;
    ExpandableListView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Variabel jobseeker akan diisi dengan intent dari loginactivity
        jobseekerID = getIntent().getExtras().getInt("jobseekerID");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        view = (ExpandableListView) findViewById(R.id.lvExp);
        refreshList();

        //Memeriksa jika salah satu pekerjaan dipilih
        view.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Job selectedJob = childMapping.get(listRecruiter.get(groupPosition)).get(childPosition);
                Intent intent = new Intent(MainActivity.this, ApplyJobActivity.class);
                intent.putExtra("jobseekerID", jobseekerID);
                intent.putExtra("jobID", selectedJob.getId());
                intent.putExtra("jobName", selectedJob.getName());
                intent.putExtra("jobCategory", selectedJob.getCategory());
                intent.putExtra("jobFee", selectedJob.getFee());
                startActivity(intent);
                return true;
            }
        });
    }

    protected void refreshList() {
        // buat request rest controller
        MenuRequest menuRequest = new MenuRequest(this::onResponse);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(menuRequest);
    }

    private void onResponse(String response) {
        // proses json response
        try{
            JSONArray jsonResponse = new JSONArray(response);
            if(jsonResponse != null){
                for (int i = 0; i < jsonResponse.length(); i++){
                    JSONObject job = jsonResponse.getJSONObject(i);
                    JSONObject recruiter = job.getJSONObject("recruiter");
                    JSONObject location = recruiter.getJSONObject("location");

                    String city = location.getString("city");
                    String province = location.getString("province");
                    String description = location.getString("description");

                    Location l1 = new Location(city, province, description);

                    int recruiterId = recruiter.getInt("id");
                    String rctrName = recruiter.getString("name");
                    String rctrEmail = recruiter.getString("email");
                    String rctrPhoneNumber = recruiter.getString("phoneNumber");

                    Recruiter r1 = new Recruiter(recruiterId, rctrName, rctrEmail, rctrPhoneNumber, l1);
                    if (listRecruiter.size() > 0) {
                        boolean success = true;
                        for (Recruiter rec : listRecruiter)
                            if (rec.getId() == r1.getId())
                                success = false;
                        if (success) {
                            listRecruiter.add(r1);
                        }
                    } else {
                        listRecruiter.add(r1);
                    }

                    int jobId = job.getInt("id");
                    int jobPrice = job.getInt("fee");
                    String jobName = job.getString("name");
                    String jobCategory = job.getString("category");

                    Job j1 = new Job(jobId, jobName, r1, jobPrice, jobCategory);
                    jobIdList.add(j1);

                    for (Recruiter rctr : listRecruiter) {
                        ArrayList<Job> tempRecr = new ArrayList<>();
                        for (Job jobs : jobIdList) {
                            if (jobs.getRecruiter().getId() == rctr.getId())
                            {
                                tempRecr.add(jobs);
                            }
                        }
                        childMapping.put(rctr, tempRecr);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        };
        // buat adapter untuk tampilan layar
        MainListAdapter adapter = new MainListAdapter(this, listRecruiter, childMapping);
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.lvExp);
        listView.setAdapter(adapter);
    }

    public void onAppliedJobButtonClick(View view) {
        Intent i = new Intent(MainActivity.this, SelesaiJobActivity.class);
        i.putExtra("jobseekerID", jobseekerID);
        startActivity(i);

    }
}