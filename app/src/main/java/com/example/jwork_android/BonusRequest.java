package com.example.jwork_android;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class BonusRequest extends StringRequest {
    //Variabel yang digunakan
    private static final String URL = "http://192.168.8.102:8080/bonus/";
    private Map<String, String> params;

    /**
     * Konstruktur kelas dengan parameter referral code
     * @param referralCode
     * @param listener
     */
    public BonusRequest(String referralCode, Response.Listener<String> listener) {
        super(Method.GET, URL+referralCode, listener, null);
        params = new HashMap<>();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }}
