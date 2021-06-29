package com.example.jwork_android;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class BonusRequest extends StringRequest {
    private Map<String,String> params;
    private static final String URL = "http://192.168.8.102:8080/bonus/";

    public BonusRequest(String promoCode, Response.Listener<String> listener) {
        super(Method.GET, URL + promoCode, listener, null);
        params = new HashMap<>();
    }

    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}