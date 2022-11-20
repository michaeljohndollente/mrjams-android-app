package com.example.mrjams.ui.announcement;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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

public class RatingActivity extends AppCompatActivity {

    Button btnRateSys;
    RatingBar ratingBar;
    AutoCompleteTextView tvDetails;
    EditText message;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SessionClass.FRAGMENT = new AnnouncementFragment();
        Intent intent = new Intent(getApplicationContext(), AnnouncementFragment.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        if(getIntent().hasExtra("name")){
            String name;
            name = getIntent().getStringExtra("name");
        }

        message = findViewById(R.id.tboxRateMessage);
        ratingBar = findViewById(R.id.ratingBar);
        btnRateSys = findViewById(R.id.btnRateSys);
        tvDetails = findViewById(R.id.tvDetails);

        ArrayAdapter<CharSequence> det = ArrayAdapter.createFromResource(this, R.array.ratingfield, android.R.layout.simple_dropdown_item_1line);
        tvDetails.setAdapter(det);

        btnRateSys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String z1 ,z2;
                float z3;

                z2 = message.getText().toString();
                z3 = ratingBar.getRating();
                z1 = tvDetails.getText().toString();

                boolean checker = validateRating(z1, z2, z3);

                if (checker){
                    Toast.makeText(RatingActivity.this, "Rating Success", Toast.LENGTH_SHORT).show();
                    submitRating();
                }

            }
        });

    }

    private boolean validateRating(String z1, String z2, float z3) {
        if(z1.isEmpty()){
            tvDetails.requestFocus();
            tvDetails.setError("Please select details");
            return false;
        }

        if (z2.isEmpty()){
            message.setError("Please input your message");
            message.requestFocus();
            return false;
        }

        if (z3 == 0){
            Toast.makeText(this, "Please your rating for us", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void submitRating() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rating", ratingBar.getRating());
            jsonObject.put("area", tvDetails.getText().toString());
            jsonObject.put("message", message.getText().toString());
            Log.d("TAG", "storeAppointment: " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = getString(R.string.URLProject) + "api/mcustomer/mrating";

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

//                            AnnouncementFragment af = new AnnouncementFragment();
//                            Bundle args = new Bundle();
//                            af.setArguments(args);
//                            FragmentManager fm = getFragmentManager();
//                            FragmentTransaction fragmentTransaction = fm.beginTransaction();
//                            fragmentTransaction.replace(R.id.nav_home, af);
//                            fragmentTransaction.commit();

                            SessionClass.FRAGMENT = new AnnouncementFragment();
//                            Intent intent = new Intent(RatingActivity.this, AnnouncementFragment.class);
//                            startActivity(intent);
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
