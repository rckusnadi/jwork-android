package com.example.jwork_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SelesaiJobActivity extends AppCompatActivity {
    //Variabel yang digunakan
    private int invoiceID;
    private int jobseekerID;


    /**
     * Method yang dijalankan saat activity dipanggil
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_selesai_job);
        TextView id = findViewById(R.id.invoice_id);
        TextView jobseeker = findViewById(R.id.jobseeker);
        TextView invoiceDate = findViewById(R.id.invoice_date);
        TextView invoiceStatus = findViewById(R.id.invoice_status);
        TextView paymentType = findViewById(R.id.payment_type);
        TextView referralCode = findViewById(R.id.refCode);
        TextView jobName = findViewById(R.id.jobName);
        TextView jobFee = findViewById(R.id.jobFee);
        TextView totalFee = findViewById(R.id.totalFee);
        Button finish = findViewById(R.id.btnFinish);
        Button cancel = findViewById(R.id.btnCancel);

        jobseekerID = getIntent().getExtras().getInt("jobseekerID");

        id.setVisibility(View.GONE);
        jobseeker.setVisibility(View.GONE);
        invoiceDate.setVisibility(View.GONE);
        invoiceStatus.setVisibility(View.GONE);
        paymentType.setVisibility(View.GONE);
        referralCode.setVisibility(View.GONE);
        jobName.setVisibility(View.GONE);
        jobFee.setVisibility(View.GONE);
        totalFee.setVisibility(View.GONE);
        finish.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.VISIBLE);


        ProgressDialog pDialog = ProgressDialog.show(SelesaiJobActivity.this, "Getting Invoice", "Please wait", true, false);
        pDialog.setIndeterminate(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    fetchJob();
                    Thread.sleep(200);
                    pDialog.dismiss();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        //Akan mengubah status invoice menjadi finished
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject invoice = new JSONObject(response);
                            if (invoice.getString("invoiceStatus").equals("Finished")) {
                                Toast.makeText(SelesaiJobActivity.this, "Invoice berhasil diselesaikan", Toast.LENGTH_SHORT).show();
                                SelesaiJobActivity.this.finish();
                            }
                            else {
                                Toast.makeText(SelesaiJobActivity.this, "Invoice telah selesai", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(SelesaiJobActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                };
                JobSelesaiRequest jsr = new JobSelesaiRequest(String.valueOf(invoiceID), responseListener);
                RequestQueue queue = Volley.newRequestQueue(SelesaiJobActivity.this);
                queue.add(jsr);
            }
        });

        //Akan mengubah status invoice menjadi cancelled
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject invoice = new JSONObject(response);
                            //Akan memeriksa status invoice
                            if (invoice.getString("invoiceStatus").equals("Cancelled")) {
                                Toast.makeText(SelesaiJobActivity.this, "Invoice berhasil di cancel", Toast.LENGTH_SHORT).show();
                                SelesaiJobActivity.this.finish();
                            }
                            else {
                                Toast.makeText(SelesaiJobActivity.this, "Invoice telah dicancel", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(SelesaiJobActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                };
                JobBatalRequest jbr = new JobBatalRequest(String.valueOf(invoiceID), responseListener);
                RequestQueue queue = Volley.newRequestQueue(SelesaiJobActivity.this);
                queue.add(jbr);
            }
        });
    }

    /**
     * Akan menampilkan pekerjaan yang diapply
     */
    protected void fetchJob(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                TextView id = findViewById(R.id.invoice_id);
                TextView jobseeker = findViewById(R.id.jobseeker);
                TextView invoiceDate = findViewById(R.id.invoice_date);
                TextView invoiceStatus = findViewById(R.id.invoice_status);
                TextView paymentType = findViewById(R.id.payment_type);
                TextView referralCode = findViewById(R.id.refCode);
                TextView jobName = findViewById(R.id.jobName);
                TextView jobFee = findViewById(R.id.jobFee);
                TextView totalFee = findViewById(R.id.totalFee);
                Button finish = findViewById(R.id.btnFinish);
                Button cancel = findViewById(R.id.btnCancel);

                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    if(!jsonResponse.isNull(0)){

                        JSONObject invoice = jsonResponse.getJSONObject(jsonResponse.length()-1);
                        JSONObject j = invoice.getJSONArray("jobs").getJSONObject(0);
                        JSONObject js = invoice.getJSONObject("jobseeker");

                        invoiceID = invoice.getInt("id");
                        id.setText(String.valueOf(invoiceID));
                        jobseeker.setText(js.getString("name"));
                        jobName.setText(j.getString("name"));
                        invoiceDate.setText(invoice.getString("date"));
                        invoiceStatus.setText(invoice.getString("invoiceStatus"));
                        paymentType.setText(invoice.getString("paymentType"));
                        jobName.setText(j.getString("name"));
                        jobFee.setText(String.valueOf(j.getInt("fee")));
                        totalFee.setText(String.valueOf(invoice.getInt("totalFee")));
                        if(invoice.getString("invoiceStatus").equals("OnGoing")){
                            finish.setVisibility(View.VISIBLE);
                            cancel.setVisibility(View.VISIBLE);
                        } else
                        {
                            finish.setVisibility(View.GONE);
                            cancel.setVisibility(View.GONE);
                        }

                        if(invoice.getString("paymentType").equals("EwalletPayment")){
                            if(!invoice.isNull("bonus")){
                                JSONObject bonus = invoice.getJSONObject("bonus");
                                referralCode.setText(bonus.getString("referralCode"));
                            }
                        }else{
                            referralCode.setText("");
                        }
                        id.setVisibility(View.VISIBLE);
                        jobseeker.setVisibility(View.VISIBLE);
                        invoiceDate.setVisibility(View.VISIBLE);
                        invoiceStatus.setVisibility(View.VISIBLE);
                        paymentType.setVisibility(View.VISIBLE);
                        referralCode.setVisibility(View.VISIBLE);
                        jobName.setVisibility(View.VISIBLE);
                        jobFee.setVisibility(View.VISIBLE);
                        totalFee.setVisibility(View.VISIBLE);

                    } else {
                        Intent intent = new Intent(SelesaiJobActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
        };
        JobFetchRequest jfr = new JobFetchRequest(String.valueOf(jobseekerID), responseListener);
        RequestQueue queue = Volley.newRequestQueue(SelesaiJobActivity.this);
        queue.add(jfr);
    }

}