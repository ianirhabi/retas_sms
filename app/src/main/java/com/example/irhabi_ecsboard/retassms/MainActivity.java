package com.example.irhabi_ecsboard.retassms;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import com.example.irhabi_ecsboard.retassms.connect.AppConfig;
import com.example.irhabi_ecsboard.retassms.connect.MyApplication;


public class MainActivity extends AppCompatActivity {
    Button res, kl;
    EditText nam, pass;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    String saya, no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        res=(Button)findViewById(R.id.reset);
        kl=(Button)findViewById(R.id.kirim);
        nam=(EditText)findViewById(R.id.nama);
        pass=(EditText)findViewById(R.id.no);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //Name of Method for Calling Message
            startService(new Intent(getBaseContext(), TheService.class));

        } else {
            //TODO
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        kl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saya = nam.getText().toString();
                no = pass.getText().toString();
                kirimsms(saya, no);
                nam.setText("");
                pass.setText("");
            }
        });
        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                startService(new Intent(getBaseContext(), TheService.class));
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void kirimsms(final String na, final String bi) {
        String url_simpan = AppConfig.URL_REGISTER + "smscekingtiga.php";

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
                        Toast.makeText(MainActivity.this, pesan, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MainActivity.this, "tidak bisa update", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error JSON", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", error.getMessage());
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("name", na);// sesuaaikan dengan nama $_POST
                param.put("pass", bi);// sesuaaikan dengan nama $_POST
                return param;
            }
        };
        MyApplication.getInstance().addToRequestQueue(stringRequest, tag_json);

    }

    public void reset(){

        String url_simpan = AppConfig.URL_REGISTER + "hapussms.php";

        String tag_json = "tag_json";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_simpan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response.toString());
                try {
                    JSONObject jObject = new JSONObject(response);
                    String ceking = jObject.getString("ceking");
                    String hasil = jObject.getString("result");

                    if (hasil.equalsIgnoreCase("true")) {
                        Toast.makeText(MainActivity.this, hasil, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MainActivity.this, "tidak berhasil reset", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error JSON", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", error.getMessage());
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("name", "reset");// sesuaaikan dengan nama $_POST

                return param;
            }
        };
        MyApplication.getInstance().addToRequestQueue(stringRequest, tag_json);

    }

}
