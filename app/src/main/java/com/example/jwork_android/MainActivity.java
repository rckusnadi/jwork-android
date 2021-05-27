package com.example.jwork_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Recruiter> listRecruiter = new ArrayList<>();
    private ArrayList<Job> jobIdList = new ArrayList<>();
    private HashMap<Recruiter, ArrayList<Job>> childMapping = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void refreshList(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    if (jsonResponse != null) {
                        for (int i = 0; i < jsonResponse.length(); i++){
                            JSONObject job = jsonResponse.getJSONObject(i);
                            JSONObject recruiter = job.getJSONObject("recruiter");
                            JSONObject location = recruiter.getJSONObject("location");

                            String city = location.getString("city");
                            String province = location.getString("province");
                            String description = location.getString("description");

                            Location location1 = new Location(city, province, description);

                            int recruiterId = recruiter.getInt("id");
                            String recruiterName = recruiter.getString("name");
                            String recruiterEmail = recruiter.getString("email");
                            String recruiterPhoneNumber = recruiter.getString("phoneNumber");

                            Recruiter newRecruiter = new Recruiter(recruiterId, recruiterName, recruiterEmail, recruiterPhoneNumber, location1);
                            if (listRecruiter.size() > 0) {
                                boolean success = true;
                                for (Recruiter rec : listRecruiter)
                                    if (rec.getId() == newRecruiter.getId())
                                        success = false;
                                if (success) {
                                    listRecruiter.add(newRecruiter);
                                }
                            } else {
                                listRecruiter.add(newRecruiter);
                            }

                            int jobId = job.getInt("id");
                            int jobPrice = job.getInt("price");
                            String jobName = job.getString("name");
                            String jobCategory = job.getString("category");

                            Job newJob = new Job(jobId, jobName, newRecruiter, jobPrice, jobCategory);
                            jobIdList.add(newJob);

                            for (Recruiter recr : listRecruiter) {
                                ArrayList<Job> tempRecr = new ArrayList<>();
                                for (Job jobs : jobIdList) {
                                    if (jobs.getRecruiter().getName().equals(recr.getName()) ||
                                            jobs.getRecruiter().getEmail().equals(recr.getEmail()) ||
                                            jobs.getRecruiter().getPhoneNumber().equals(recr.getPhoneNumber()))
                                    {
                                        tempRecr.add(jobs);
                                    }
                                }
                                childMapping.put(recr, tempRecr);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
