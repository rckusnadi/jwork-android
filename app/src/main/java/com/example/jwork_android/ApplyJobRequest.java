package com.example.jwork_android;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplyJobRequest extends StringRequest {
    //Variabel dan URL yang digunakan
    private static final String URLBank = "http://192.168.8.102:8080/invoice/createBankPayment";
    private static final String URLEWallet2 = "http://192.168.8.102:8080/invoice/createEWalletPayment";
    private Map<String, String> params;

    /**
     * Konstruktur untuk melakukan bankpayment
     * @param jobIdList
     * @param jobseekerId
     * @param listener
     */
    public ApplyJobRequest(String jobIdList, String jobseekerId, Response.Listener<String> listener) {
        super(Method.POST, URLBank, listener, null);
        params = new HashMap<>();
        params.put("jobIdList", jobIdList);
        params.put("jobseekerId", jobseekerId);
        params.put("adminFee", "100");
    }

    /**
     * Konstruktur untuk melakukan ewalletpayment
     * @param jobIdList
     * @param jobseekerId
     * @param referralCode
     * @param listener
     */
    public ApplyJobRequest(String jobIdList, String jobseekerId, String referralCode, Response.Listener<String> listener) {
        super(Method.POST, URLEWallet2, listener, null);
        params = new HashMap<>();
        params.put("jobIdList", jobIdList);
        params.put("jobseekerId", jobseekerId);
        params.put("referralCode", referralCode);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
