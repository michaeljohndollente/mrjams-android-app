package com.example.mrjams.ui.clinic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mrjams.R;
import com.example.mrjams.ui.SessionClass;
import com.example.mrjams.ui.announcement.RatingActivity;
import com.example.mrjams.ui.home.MapsActivity;
import com.example.mrjams.ui.mail.MailAppointmentFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ClinicListShow extends AppCompatActivity implements SelfRelativeChooser.DialogListener{

    String clinic_id, name, type, phone, telephone, customer_fname, customer_mname,
            customer_lname, customer_phone, customer_age, customer_gender, customer_AL1,
            customer_AL2, user_id, clinicAvail;
    TextView clinicName, clinicType, addLine, clinicPhone, clinicTelephone,
                clinicDoctors, clinicServices, clinicPackage, clinicSerPrice,
                clinicPackPrice, idSunday, idMonday, idTuesday, idWednesday, idThursday, idFriday, idSaturday,
                rating;
    Button btnSetAppointment, btnViewMap;
    String selfRelative;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clinic_list_show);

        clinicName = findViewById(R.id.txtShowClinicName);
        clinicType = findViewById(R.id.txtShowClinicType);
        addLine = findViewById(R.id.txtShowAddress);
        clinicPhone = findViewById(R.id.txtShowPhone);
        clinicTelephone = findViewById(R.id.txtShowTelephone);
        clinicDoctors = findViewById(R.id.txtShowDoctors);
        clinicServices = findViewById(R.id.txtShowServices);
        clinicPackage = findViewById(R.id.txtShowPackages);
        clinicSerPrice = findViewById(R.id.txtShowServicesPrice);
        clinicPackPrice = findViewById(R.id.txtShowPackagesPrice);
        rating = findViewById(R.id.txtShowRate);

        idSunday = findViewById(R.id.idSunday);
        idMonday = findViewById(R.id.idMonday);
        idTuesday = findViewById(R.id.idTuesday);
        idWednesday = findViewById(R.id.idWednesday);
        idThursday = findViewById(R.id.idThursday);
        idFriday = findViewById(R.id.idFriday);
        idSaturday = findViewById(R.id.idSaturday);

        btnViewMap = findViewById(R.id.btnShowViewMap);
        btnSetAppointment = findViewById(R.id.btnShowSetAppointment);

        clinic_id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        type = getIntent().getStringExtra("type");
        phone = getIntent().getStringExtra("phone");
        telephone = getIntent().getStringExtra("telephone");
        if (getIntent().hasExtra("telephone")){
            if (!telephone.equals("null")) {
                clinicTelephone.setText(telephone);
            } else{
                clinicTelephone.setText("No Telephone");
            }
        }
//            address = getIntent().getStringExtra("address");

        getInfo(clinic_id);

        clinicName.setText(name);
        clinicType.setText(type);
//        addLine.setText(address);
        clinicPhone.setText(phone);

        btnSetAppointment.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    SelfRelativeChooser selfRelativeChooser = new SelfRelativeChooser();
                    Bundle bundle = new Bundle();
                    bundle.putString("clinic_id", clinic_id);
                    bundle.putString("clinic_name", name);
                    bundle.putString("user_id", user_id);
                    bundle.putString("customer_fname", customer_fname);
                    bundle.putString("customer_mname", customer_mname);
                    bundle.putString("customer_lname", customer_lname);
                    bundle.putString("customer_age", customer_age);
                    bundle.putString("customer_gender", customer_gender);
                    bundle.putString("customer_phone", customer_phone);
                    bundle.putString("customer_AL1", customer_AL1);
                    bundle.putString("customer_AL2", customer_AL2);
//                    bundle.putString("clinicAvail", clinicAvail);
                    selfRelativeChooser.setArguments(bundle);
                    selfRelativeChooser.show(getSupportFragmentManager(), null);
                }
            });

        btnViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SessionClass.FRAGMENT = new ClinicListFragment();
        Intent intent = new Intent(getApplicationContext(), ClinicListFragment.class);
        startActivity(intent);
    }

    private void getInfo(String id) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getApplicationContext().getString(R.string.URLProject) + "api/mcustomer/mappointment/" + id,
                null,
                response -> {
                    String message = response.toString();

//                    Log.d("ClinicListShow", message);

                    try {
                        ///////////////////////////////////////////////////////////////////////////////////////////
                        String stringJsonDoctor = "";
                        JSONArray jsonDoctor = response.getJSONArray("doctor");

                        for (int j = 0; j < jsonDoctor.length(); j++) {
                            JSONObject jsonObject = jsonDoctor.getJSONObject(j);
//                            Log.d("JSONOBJECT", jsonObject.toString());
                            if (!jsonDoctor.equals("")){
                                stringJsonDoctor += jsonObject.getString("fullname") + "\n";
                            }
                            Log.d("stringJsonDoctor", stringJsonDoctor);
                            clinicDoctors.setText(stringJsonDoctor);
                        }

                        ////////////////////////////////////////////////////////////////////////////////////////////////
                        JSONArray jsonAvail = response.getJSONArray("avail");
                        for (int i = 0; i < jsonAvail.length(); i++) {
                            String availStatus = "Close";
                            String availMin_time = "", availMax_time = "";

                            JSONObject jsonObject = jsonAvail.getJSONObject(i);
//                            Log.d("JSONOBJECT", jsonObject.toString());

                            String availDay = jsonObject.getString("day");
                            availMin_time = jsonObject.getString("min");
                            availMax_time = jsonObject.getString("max");
                            if (jsonObject.getString("status").equals("on")){
                                availStatus = "Open";
                            }

                            String theDay = "id"+availDay;

                            if (theDay.equals("idSunday")){
                                idSunday.setText(availDay  + ": " + availMin_time + " - " + availMax_time + "\n" + "Status: " + availStatus);
//                                clinicAvail += availStatus;
                            }

                            if (theDay.equals("idMonday")){
                                idMonday.setText(availDay  + ": " + availMin_time + " - " + availMax_time + "\n" + "Status: " + availStatus);
//                                clinicAvail += availStatus;
                            }

                            if (theDay.equals("idTuesday")){
                                idTuesday.setText(availDay  + ": " + availMin_time + " - " + availMax_time + "\n" + "Status: " + availStatus);
//                                clinicAvail += availStatus;
                            }

                            if (theDay.equals("idWednesday")){
                                idWednesday.setText(availDay  + ": " + availMin_time + " - " + availMax_time + "\n" + "Status: " + availStatus);
//                                clinicAvail += availStatus;
                            }

                            if (theDay.equals("idThursday")){
                                idThursday.setText(availDay  + ": " + availMin_time + " - " + availMax_time + "\n" + "Status: " + availStatus);
//                                clinicAvail += availStatus;
                            }

                            if (theDay.equals("idFriday")){
                                idFriday.setText(availDay  + ": " + availMin_time + " - " + availMax_time + "\n" + "Status: " + availStatus);
//                                clinicAvail += availStatus;
                            }

                            if (theDay.equals("idSaturday")){
                                idSaturday.setText(availDay  + ": " + availMin_time + " - " + availMax_time + "\n" + "Status: " + availStatus);
//                                clinicAvail += availStatus;
                            }

//                            Log.d("JSONAVAIL", availDay + " " + availMin_time + " " + availMax_time + " " + availStatus);
                        }
                        ////////////////////////////////////////////////////////////////////////////////////////////////
                        JSONArray jsonServices = response.getJSONArray("services");
//                        Log.d("asdas", jsonServices.toString());
                        for (int i = 0; i < jsonServices.length(); i++) {
                            JSONObject jsonObject = jsonServices.getJSONObject(i);

                            String name, min_price, max_price;

                            name = jsonObject.getString("name");
                            min_price = jsonObject.getString("min_price");
                            max_price = jsonObject.getString("max_price");

                            clinicServices.append(name + "\n");
                            clinicSerPrice.append("Price: " + min_price + " - " + max_price + "\n");
//                            Log.d("JSONSERVICES", name + " " + min_price + " - " + max_price);
                        }
                        ////////////////////////////////////////////////////////////////////////////////////////////////
                        JSONArray jsonPackages = response.getJSONArray("packages");
                        if (jsonPackages.length() != 0) {
                            for (int i = 0; i < jsonPackages.length(); i++) {
                                JSONObject jsonObject = jsonPackages.getJSONObject(i);

                                String name, min;
                                name = jsonObject.getString("name");
                                min = jsonObject.getString("min_price");

                                clinicPackage.append(name + "\n");
                                clinicPackPrice.append("Price: " + min + "\n");

//                            Log.d("JSONPACKAGES", name + " " + min + " - " + max);
                            }
                        }
                        ////////////////////////////////////////////////////////////////////////////////////////////////
                        if(!response.isNull("rate")) {
                            Double jsonRate = response.getDouble("rate");
//                            Log.d("rating", jsonRate.toString());
                            rating.setText(String.format("%.2f", jsonRate));
                        }else{
                            rating.setText("No rating");
                        }
                        ////////////////////////////////////////////////////////////////////////////////////////////////
                        JSONObject jsonAddress = response.getJSONObject("clinic_address");
                        String addressLine1 = "", addressLine2 = "", city = "", zipCode = "", lat, lang, address = "";

                        if (!jsonAddress.isNull("address_line_1")){
                            addressLine1 = jsonAddress.getString("address_line_1");
                            address += addressLine1 + ",\n";
                        }

                        if (!jsonAddress.isNull("address_line_2")){
                            addressLine2 = jsonAddress.getString("address_line_2");
                            address += addressLine2 + ",\n";
                        }

                        if (!jsonAddress.isNull("city")){
                            city = jsonAddress.getString("city");
                            address += city + ", ";

                        }

                        if (!jsonAddress.isNull("zip_code")){
                            zipCode = String.valueOf(jsonAddress.getInt("zip_code"));
                            address += zipCode;

                        }

                        lat = jsonAddress.getString("latitude");
                        lang = jsonAddress.getString("longitude");

//                        Log.d("address", address + ", " + lat + ", " + lang);

                        addLine.setText(address);
                        ////////////////////////////////////////////////////////////////////////////////////////////////
                        JSONObject jsonCustomer = response.getJSONObject("customer");

                        String fname = "", mname = "", lname = "", gender = "", phone = "", age = "";

                        fname = jsonCustomer.getString("fname");
                        if (!jsonCustomer.getString("mname").equals("null")){
                            mname = jsonCustomer.getString("mname");
                        }
                        lname = jsonCustomer.getString("lname");
                        gender = jsonCustomer.getString("gender");
                        phone = jsonCustomer.getString("phone");
                        age = jsonCustomer.getString("age");

//                        Log.d("JSONCUSTOMER", fname + " " + mname+ " " + lname+ " " + gender+ " " + phone+ " " + age);
                        user_id = jsonCustomer.getString("id");
                        customer_fname = fname;
                        customer_mname = mname;
                        customer_lname = lname;
                        customer_age = age;
                        customer_gender = gender;
                        customer_phone = phone;
//                        Log.d("", "getInfo: " + jsonCustomer + " " + user_id);
                        ////////////////////////////////////////////////////////////////////////////////////////////////
                        JSONObject jsonCustomerAdd = response.getJSONObject("customer_address");

                        String AL1 = "", AL2 = "";

                        AL1 = jsonCustomerAdd.getString("address_line_1");
                        if (!jsonCustomerAdd.getString("address_line_2").equals("null")){
                            AL2 = jsonCustomerAdd.getString("address_line_2");
                        }
                        customer_AL1 = AL1;
                        customer_AL2 = AL2;
//                        Log.d("CUSTOMER ADDRESS", "getInfo: " + AL1 + " " + AL2);
                        ////////////////////////////////////////////////////////////////////////////////////////////////
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

    @Override
    public void selfRelative(String name) {
        selfRelative = name;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
