package com.example.jwork_android;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class JobBatalRequest extends StringRequest {
    //Variabel yang digunakan
    private static final String URLInvoice = "http://192.168.8.102:8080/invoice/InvoiceStatus";
    private Map<String, String> params;

    /**
     * Konstruktur kelas dengan parameter id invoice
     * @param id
     * @param listener
     */
    public JobBatalRequest(String id, Response.Listener<String> listener) {
        super(Method.PUT, URLInvoice, listener, null);
        params = new HashMap<>();
        params.put("id", id);
        params.put("invoiceStatus", "Cancelled");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return this.params;
    }
}
