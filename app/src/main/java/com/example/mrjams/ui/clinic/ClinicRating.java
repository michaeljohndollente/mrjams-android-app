package com.example.mrjams.ui.clinic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mrjams.R;
import com.example.mrjams.ui.SessionClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ClinicRating extends AppCompatActivity {

    Button btnRate;
    RatingBar starbar;
    TextView clinic_name;
    String id;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SessionClass.FRAGMENT = new ClinicListFragment();
        Intent intent = new Intent(getApplicationContext(), ClinicListFragment.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_clinic);

        clinic_name = findViewById(R.id.txtClinicRateName);
        starbar = findViewById(R.id.ratingBarClinic);
        btnRate = findViewById(R.id.btnRateClinic);

        id = getIntent().getStringExtra("clinic_id");
        clinic_name.setText(getIntent().getStringExtra("clinic_name"));


        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String r1;

                r1 = String.valueOf(starbar.getRating());

                boolean checker = validationClinicRate(r1);

                if (checker){
                    Toast.makeText(ClinicRating.this, "Rating Successfully", Toast.LENGTH_SHORT).show();
                    submitRate();
                } else{
                    Toast.makeText(ClinicRating.this, "Please select your Rate", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean validationClinicRate(String r1) {
        if (r1.equals("0")){
            starbar.requestFocus();
            return false;
        }
        return true;
    }

    private void submitRate() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rating", starbar.getRating());
            jsonObject.put("ratee_id", id);
            Log.d("TAG", "storeAppointment: " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = getString(R.string.URLProject) + "api/mcustomer/mcliniclist";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("RESPONSE", "onResponse: " + response.toString());
                            Log.d("RESPONSE", "onResponse: " + response.getString("data"));
                            SessionClass.FRAGMENT = new ClinicListFragment();
                            Intent intent = new Intent(ClinicRating.this, ClinicListFragment.class);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("volleyError", error.toString());
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
