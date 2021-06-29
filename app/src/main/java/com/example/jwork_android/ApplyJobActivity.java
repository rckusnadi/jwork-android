package com.example.jwork_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApplyJobActivity extends AppCompatActivity {
    //Variabel yang digunakan
    private int jobseekerID;
    private int jobID;
    private String jobName;
    private String jobCategory;
    private double jobFee;
    private int bonus;
    private String selectedPayment;
    private static int notFound = -1;

    /**
     * Method yang dijalankan saat activity dipanggil
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_apply_job);

        //Variabel akan diisi dengan intent dari activity sebelumnya
        jobseekerID = getIntent().getExtras().getInt("jobseekerID");
        jobID = getIntent().getExtras().getInt("jobID");
        jobName = getIntent().getExtras().getString("jobName");
        jobCategory = getIntent().getExtras().getString("jobCategory");
        jobFee = getIntent().getExtras().getInt("jobFee");

        Button btnApply = findViewById(R.id.btnApply);
        Button hitung = findViewById(R.id.hitung);
        TextView textCode = findViewById(R.id.textCode);
        EditText referralCode = findViewById(R.id.referral_code);
        TextView tjobName = findViewById(R.id.job_name);
        TextView tjobCategory = findViewById(R.id.job_category);
        TextView tjobFee = findViewById(R.id.job_fee);
        TextView totalFee = findViewById(R.id.total_fee);
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);

        btnApply.setVisibility(View.GONE);
        textCode.setVisibility(View.GONE);
        referralCode.setVisibility(View.GONE);
        tjobCategory.setText(jobCategory);
        tjobName.setText(jobName);
        tjobFee.setText(String.valueOf(jobFee));
        totalFee.setText("0");

        //Memeriksa tombol yang digunakan
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbutton = findViewById(checkedId);
                selectedPayment = rbutton.getText().toString();
                if(selectedPayment.equals("E-Wallet")){
                    textCode.setVisibility(View.VISIBLE);
                    referralCode.setVisibility(View.VISIBLE);
                }
            }
        });

        /**
         * Melakukan proses perhitungan pada ewallet dan bank
         */
        hitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                int radio = rg.getCheckedRadioButtonId();
                if (radio == notFound){
                    Toast.makeText(ApplyJobActivity.this, "Payment Belum Dipilih", Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton rbutton = findViewById(radio);
                selectedPayment = rbutton.getText().toString();
                if(selectedPayment.equals("E-Wallet"))
                {
                    if(referralCode.getText().toString().equals(""))
                    {
                        totalFee.setText(String.valueOf(jobFee));
                        hitung.setVisibility(View.GONE);
                        btnApply.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                hitung.setVisibility(View.GONE);
                                btnApply.setVisibility(View.VISIBLE);
                                try {
                                    if(response.isEmpty()){
                                        Toast.makeText(ApplyJobActivity.this, "Referral Code Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                                        totalFee.setText(String.valueOf(jobFee));
                                    }
                                    JSONObject getBonus = new JSONObject(response);
                                    System.out.println(getBonus.toString());
                                    if(referralCode.getText().toString().equals(getBonus.getString("referralCode")) && getBonus.getBoolean("active"))
                                    {
                                        if(jobFee > getBonus.getInt("minTotalFee")){
                                            bonus = getBonus.getInt("extraFee");
                                            totalFee.setText(String.valueOf(jobFee + bonus));
                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        BonusRequest bonusRequest = new BonusRequest(referralCode.getText().toString(), responseListener);
                        RequestQueue queue = Volley.newRequestQueue(ApplyJobActivity.this);
                        queue.add(bonusRequest);

                    } //referral code ada isinya
                }
                else if(selectedPayment.equals("Bank")) //bank dipilih
                {
                    totalFee.setText(String.valueOf(jobFee));
                    hitung.setVisibility(View.GONE);
                    btnApply.setVisibility(View.VISIBLE);
                }
            }
        });

        /**
         * Melakukan apply job setelah perhitungan fee
         */
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radio = rg.getCheckedRadioButtonId();
                RadioButton rbutton = findViewById(radio);
                selectedPayment = rbutton.getText().toString();
                ApplyJobRequest ajr = null;

                Response.Listener<String> responseListenerApply = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject != null){
                                Toast.makeText(ApplyJobActivity.this, "Job applied!", Toast.LENGTH_SHORT).show();
                                ApplyJobActivity.this.finish();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(ApplyJobActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                };

                if(selectedPayment.equals("E-Wallet")){
                    ajr = new ApplyJobRequest(String.valueOf(jobID), (String.valueOf(jobseekerID)), referralCode.getText().toString(), responseListenerApply);
                }
                else {
                    ajr = new ApplyJobRequest(String.valueOf(jobID), (String.valueOf(jobseekerID)), responseListenerApply);
                }

                RequestQueue queue = Volley.newRequestQueue(ApplyJobActivity.this);
                queue.add(ajr);
            }
        });
    }
}
