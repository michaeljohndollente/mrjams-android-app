package com.example.mrjams.ui.announcement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ContactActivity extends AppCompatActivity {

    EditText tboxName, tboxEmail, tboxContact, tboxMessage;
    Button btnSendMes;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SessionClass.FRAGMENT = new AnnouncementFragment();
        Intent intent = new Intent(getApplicationContext(), AnnouncementFragment.class);
        startActivity(intent);
        finish();
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);


        tboxName = findViewById(R.id.tboxMesName);
        tboxEmail = findViewById(R.id.tboxMesEmail);
        tboxContact = findViewById(R.id.tboxMesContact);
        tboxMessage = findViewById(R.id.tboxMessage);
        btnSendMes = findViewById(R.id.btnSendMes);

        Intent intent = getIntent();
        tboxName.setText(intent.getStringExtra("name"));
        tboxEmail.setText(intent.getStringExtra("email"));
        tboxContact.setText(intent.getStringExtra("contact"));

        btnSendMes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String c1;

                c1 = tboxMessage.getText().toString();

                boolean checker = validicationContact(c1);
                if (checker){
                    Toast.makeText(ContactActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                    storeMessage();
                } else {
                    Toast.makeText(ContactActivity.this, "Please input your message", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean validicationContact(String c1) {
        if (c1.isEmpty()){
            tboxMessage.requestFocus();
            tboxMessage.setError("Please input your message");
            return false;
        }
        return true;
    }

    private void storeMessage() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("message", tboxMessage.getText().toString());
            Log.d("TAG", "storeAppointment: " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = getString(R.string.URLProject) + "api/mcustomer/mannouncement";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("RESPONSE", "onResponse: " + response.getString("data"));
                            SessionClass.FRAGMENT = new AnnouncementFragment();
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
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserCredentials", Context.MODE_PRIVATE);
                headers.put("Authorization", "Bearer " + sharedPreferences.getString("access_token", null));
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

}