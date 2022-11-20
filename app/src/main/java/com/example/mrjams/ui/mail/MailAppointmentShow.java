package com.example.mrjams.ui.mail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mrjams.MainActivity;
import com.example.mrjams.R;
import com.example.mrjams.ui.SessionClass;
import com.example.mrjams.ui.clinic.ClinicRating;
import com.example.mrjams.ui.home.MapsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MailAppointmentShow extends AppCompatActivity {

    TextView date_created, appointment_date, time, remarks, customer_name, clinic_name, clinic_type,
            phone, address, telephone, clinicServices, clinicPackages, clinicServicesPrice, clinicPackagesPrice;
    String id, clinic_id, clinicName, remark, appointmentDate, dateCreated;
    Button btnAppointmentRating, cancel, delete, accept, decline, btnViewMap;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mail_show);

        cancel = findViewById(R.id.btnMCancel);
        delete = findViewById(R.id.btnDelete);
        accept = findViewById(R.id.btnAccept);
        decline = findViewById(R.id.btnDecline);
        btnViewMap = findViewById(R.id.btnMailViewMap);

        date_created = findViewById(R.id.txtAppointmentDateCreated);
        appointment_date = findViewById(R.id.txtAppointmentDate);
        time = findViewById(R.id.txtAppointmentTime);
        remarks = findViewById(R.id.txtAppointmentStatus);

        customer_name = findViewById(R.id.txtAppointmentCustomerName);

        clinic_name = findViewById(R.id.txtAppointmentClinicName);
        clinic_type = findViewById(R.id.txtAppointmentClinicType);
        phone = findViewById(R.id.txtAppointmentPhone);
        address = findViewById(R.id.txtAppointmentAddress);
        telephone = findViewById(R.id.txtAppointmentTelephone);

        clinicServices = findViewById(R.id.txtAppointmentServices);
        clinicServicesPrice = findViewById(R.id.txtAppointmentServicesPrice);
        clinicPackages = findViewById(R.id.txtAppointmentPackages);
        clinicPackagesPrice = findViewById(R.id.txtAppointmentPackagesPrice);

        btnAppointmentRating = findViewById(R.id.btnAppointmentRating);

        id = getIntent().getStringExtra("id");
        clinicName = getIntent().getStringExtra("clinic_name");
        remark = getIntent().getStringExtra("remark");


        appointmentDate = getIntent().getStringExtra("appointment_date");
        dateCreated = getIntent().getStringExtra("date_created");

        clinic_name.setText(clinicName);
        remarks.setText(remark);

        if (remark.equals("negotiating")){
            cancel.setVisibility(View.VISIBLE);
            accept.setVisibility(View.VISIBLE);
            decline.setVisibility(View.VISIBLE);
        } else if((remark.equals("accepted")) || (remark.equals("pending"))) {
            cancel.setVisibility(View.VISIBLE);
        } else {
            delete.setVisibility(View.VISIBLE);
        }

        btnViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnAppointmentRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ClinicRating.class);
                intent.putExtra("clinic_name", clinicName);
                intent.putExtra("clinic_id", clinic_id);
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forCancel();
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forAccept();
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forDecline();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forDelete(id);
            }
        });

        appointment_date.setText(appointmentDate);
        date_created.setText(dateCreated);

        getAppointmentInfo(id);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SessionClass.FRAGMENT = new MailAppointmentFragment();
        Intent intent = new Intent(getApplicationContext(), MailAppointmentFragment.class);
        startActivity(intent);
    }

    private void getAppointmentInfo(String id) {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getApplicationContext().getString(R.string.URLProject) + "api/mcustomer/mmailappointment/" + id,
                null,
                response -> {
                    String message = response.toString();

                    Log.d("MAILLAPPOINTMENTS", id + message);

                    try {
                        ///////////////////////////////////////////////////////////////////////////////////////////
                        String stringJsonServices, min_price_ser, max_price_ser;
                        JSONArray jsonServices = response.getJSONArray("services_all");
                        if (jsonServices.length() != 0){
                            for (int j = 0; j < jsonServices.length(); j++) {
                                JSONObject jsonObject = jsonServices.getJSONObject(j);
                                stringJsonServices = jsonObject.getString("name");
                                min_price_ser = jsonObject.getString("min_price");
                                max_price_ser = jsonObject.getString("max_price");
                                clinicServices.append(stringJsonServices + "\n");
                                clinicServicesPrice.append("Price: " + min_price_ser + " - " + max_price_ser + "\n");
//                            Log.d("jsonServices", jsonObject.toString());
                            }
                        } else{
                            clinicServices.setText("No Service Availed.");
                        }
                        ///////////////////////////////////////////////////////////////////////////////////////////
                        String packageName, min_price_pack;
                        if (!(response.getString("package").equals("null"))){
                            JSONObject jsonPackages = response.getJSONObject("package");
                            packageName = jsonPackages.getString("name");
                                min_price_pack = jsonPackages.getString("min_price");
                                clinicPackages.append(packageName + "\n");
                                clinicPackagesPrice.append("Price: " + min_price_pack);
                        } else {
                            clinicPackages.setText("No Package Availed.");
                        }

                        ///////////////////////////////////////////////////////////////////////////////////////////
                        String jsonName = response.getString("name");
                        customer_name.setText(jsonName.split(",")[0].replace("[", "").replace("\"", ""));
//                        Log.d("jsonName", jsonName.split(",")[0].replace("[", ""));
                        ///////////////////////////////////////////////////////////////////////////////////////////
                        JSONObject jsonAppointmentData = response.getJSONObject("appointment_data");
                        time.setText(jsonAppointmentData.getString("time"));
                        ///////////////////////////////////////////////////////////////////////////////////////////
                        String clinic_phone, clinic_telephone;
                        JSONObject jsonClinicInfo = response.getJSONObject("clinic_info");
                        clinic_id = jsonClinicInfo.getString("id");
                        clinic_phone = jsonClinicInfo.getString("phone");
                        clinic_telephone = jsonClinicInfo.getString("telephone");
                        phone.setText(clinic_phone);
                        if (!clinic_telephone.equals("null")) {
                            telephone.setText(clinic_telephone);
                        }
//                            Log.d("jsonClinicInfo", jsonClinicInfo.toString());
                        ///////////////////////////////////////////////////////////////////////////////////////////
                        String type;
                        JSONObject jsonClinicType = response.getJSONObject("clinic_type");
                        type = jsonClinicType.getString("type_of_clinic");
                        clinic_type.setText(type);
//                        Log.d("jsonClinicType", jsonClinicType.toString());
                        ///////////////////////////////////////////////////////////////////////////////////////////
                        String clinic_address, AL1, AL2;
                        JSONObject jsonClinicAdd = response.getJSONObject("clinic_address");
                        AL1 = jsonClinicAdd.getString("address_line_1");
                        AL2 = jsonClinicAdd.getString("address_line_2");

                        address.setText(AL1);
                        if (!AL2.equals("null")){
                            address.append(" " + AL2);
                        }
//                            Log.d("jsonClinicAdd", jsonClinicAdd.toString());
                        ///////////////////////////////////////////////////////////////////////////////////////////
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//
                },
                error -> Log.e("VolleyError", error.toString()))
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserCredentials", Context.MODE_PRIVATE);
                headers.put("Authorization", "Bearer "+ sharedPreferences.getString("access_token", null));
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void forAccept(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext().getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                getApplicationContext().getString(R.string.URLProject) + "api/mcustomer/mappointment/" + id,
                null,
                response -> {
                    try {
                        Log.d("ACCEPT", "forAccept: " + response.toString());
                        Log.d("RESPONSE", "onResponse: " + response.getString("status"));
                        Toast.makeText(this, "Accept", Toast.LENGTH_SHORT).show();
                        SessionClass.FRAGMENT = new MailAppointmentFragment();
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.e("VolleyError", error.toString());
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserCredentials", Context.MODE_PRIVATE);
                headers.put("Authorization", "Bearer "+ sharedPreferences.getString("access_token", null));
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void forDecline(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext().getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                getApplicationContext().getString(R.string.URLProject) + "api/mcustomer/mcliniclist/" + id,
                null,
                response -> {
                    try {
                        Log.d("DECLINE", "forDecline: " + response.toString());
                        Log.d("RESPONSE", "onResponse: " + response.getString("status"));
                        Toast.makeText(this, "Decline", Toast.LENGTH_SHORT).show();
                        SessionClass.FRAGMENT = new MailAppointmentFragment();
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.e("VolleyError", error.toString());
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserCredentials", Context.MODE_PRIVATE);
                headers.put("Authorization", "Bearer "+ sharedPreferences.getString("access_token", null));
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void forCancel(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext().getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                getApplicationContext().getString(R.string.URLProject) + "api/mcustomer/mrating/" + id,
                null,
                response -> {
                    try {
                        Log.d("CANCEL", "forCancel: " + response.toString());
                        Log.d("RESPONSE", "onResponse: " + response.getString("status"));
                        Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
                        SessionClass.FRAGMENT = new MailAppointmentFragment();
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.e("VolleyError", error.toString());
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserCredentials", Context.MODE_PRIVATE);
                headers.put("Authorization", "Bearer "+ sharedPreferences.getString("access_token", null));
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void forDelete(String id) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext().getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                getApplicationContext().getString(R.string.URLProject) + "api/mcustomer/mmailappointment/" + id,
                null,
                response -> {
                    try {
                        Log.d("DELETE", "forDelete: " + response.toString());
                        Log.d("RESPONSE", "onResponse: " + response.getString("status"));
                        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                        SessionClass.FRAGMENT = new MailAppointmentFragment();
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.e("VolleyError", error.toString());
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserCredentials", Context.MODE_PRIVATE);
                headers.put("Authorization", "Bearer "+ sharedPreferences.getString("access_token", null));
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

}
