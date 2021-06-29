package com.example.jwork_android;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class JobBatalRequest extends StringRequest {
    private static final String URL = "http://192.168.8.102:8080/invoice/invoiceStatus/";
    private Map<String, String> params;

    public JobBatalRequest(String id, Response.Listener<String> listener) {
        super(Method.PUT, URL + id, listener, null);
        params = new HashMap<>();
        params.put("id", id);
        params.put("status", "Cancelled");
    }

    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}
