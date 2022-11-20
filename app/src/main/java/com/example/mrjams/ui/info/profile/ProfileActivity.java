package com.example.mrjams.ui.info.profile;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.mrjams.MainActivity;
import com.example.mrjams.R;
import com.example.mrjams.ui.SessionClass;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    String spicture, sname, semail, saddress, sfname,  slname, sage, sphone, sal1, sal2, scity, szipcode, sid;

    ImageView picture;
    TextView name, email, address;
    EditText fname, lname, age, phone, al1, al2, city, zipcode;
    Button saveChanges;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        picture = findViewById(R.id.imgEditPicture);

        name = findViewById(R.id.txtEditProfileName);
        email = findViewById(R.id.txtEditProfileEmail);
        address = findViewById(R.id.txtEditProfileAddress);

        fname = findViewById(R.id.tboxEditProfileFname);
        lname = findViewById(R.id.tboxEditProfileLname);
        age = findViewById(R.id.tboxEditProfileAge);
        phone = findViewById(R.id.tboxEditProfilePhone);
        al1 = findViewById(R.id.tboxEditProfileAL1);
        al2 = findViewById(R.id.tboxEditProfileAL2);
        city = findViewById(R.id.tboxEditProfileCity);
        zipcode = findViewById(R.id.tboxEditProfileZipcode);

        saveChanges = findViewById(R.id.btnEditProfile);

        getProfile();

        saveChanges.setOnClickListener(view -> {
            String a1, a3, a4, a8, a2, a6, a7;

            a1 = fname.getText().toString();
            a2 = lname.getText().toString();
            a3 = age.getText().toString();
            a4 = al1.getText().toString();
            a6 = city.getText().toString();
            a7 = phone.getText().toString();
            a8 = zipcode.getText().toString();

            boolean checker = validationProfile(a1, a2, a3, a4, a6, a7, a8);

            if (checker){
                Toast.makeText(this, "Update Successfully", Toast.LENGTH_SHORT).show();
                updateProfile();
            } else{
                Toast.makeText(this, "Please Check information again", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private boolean validationProfile(String a1, String a2, String a3, String a4, String a6, String a7, String a8) {
        if(a1.isEmpty()){
            fname.requestFocus();
            fname.setError("First Name can't be empty");
            return false;
        } else if (!a1.matches("[^0-9\\.\\,\\\"\\?\\!\\;\\:\\#\\$\\%\\&\\(\\)\\*\\+\\-\\/\\<\\>\\=\\@\\[\\]\\\\\\^\\_\\{\\}\\|\\~]+")) {
            fname.requestFocus();
            fname.setError("Please set a valid First name");
            return false;
        }

        if(a2.isEmpty()){
            lname.requestFocus();
            lname.setError("Last Name can't be empty");
            return false;
        } else if (!a2.matches("[^0-9\\.\\,\\\"\\?\\!\\;\\:\\#\\$\\%\\&\\(\\)\\*\\+\\-\\/\\<\\>\\=\\@\\[\\]\\\\\\^\\_\\{\\}\\|\\~]+")) {
            lname.requestFocus();
            lname.setError("Please set a valid Last name");
            return false;
        }

        if(a3.isEmpty()){
            age.requestFocus();
            age.setError("Age can't be Empty");
            return false;
        } else if(Integer.parseInt(a3) <= 0 || Integer.parseInt(a3) >= 120){
            age.requestFocus();
            age.setError("Please enter a valid age");
            return false;
        }

        if(a4.isEmpty()){
            al1.requestFocus();
            al1.setError("Address Line 1 can't be Empty");
            return false;
        }

        if(a6.isEmpty()){
            city.requestFocus();
            city.setError("City can't be Empty");
            return false;
        }

        if(a7.isEmpty()){
            phone.requestFocus();
            phone.setError("Contact Number can't be Empty");
            return false;
        } else if(!a7.matches("(09)\\d{9}")){
            phone.requestFocus();
            phone.setError("Must be a valid or 11 - digits number");
            return false;
        }

        if(a8.isEmpty()){
            zipcode.requestFocus();
            zipcode.setError("Zip code can't be Empty");
            return false;
        } else if(!a8.matches("[0-9]{4}")){
            zipcode.requestFocus();
            zipcode.setError("Please enter a valid zip code");
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SessionClass.FRAGMENT = new ProfileFragment();
        Intent intent = new Intent(getApplicationContext(), ProfileFragment.class);
        startActivity(intent);
        finish();
    }

    private void getProfile() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getString(R.string.URLProject) + "api/mcustomer/mprofile",
                null,
                response -> {
                    try {
                        Log.d(TAG, "getProfile: " + response);
                        ///////////////////////////////////////////////////////////////////////////////////////////
                        JSONObject jsonUser = response.getJSONObject("user");
//                        email.setText(jsonUser.getString("email"));
                        sid = jsonUser.getString("id");
                        semail = jsonUser.getString("email");
                        spicture = jsonUser.getString("avatar");
                        ///////////////////////////////////////////////////////////////////////////////////////////
                        JSONObject jsonCustomer = response.getJSONObject("customer");
                        sfname = jsonCustomer.getString("fname");
                        slname = jsonCustomer.getString("lname");
                        sphone = jsonCustomer.getString("phone");
                        sage = jsonCustomer.getString("age");
                        sname = sfname + " " + slname;
//                        Log.d(TAG, "jsonCustomer: " + jsonCustomer);
                        ///////////////////////////////////////////////////////////////////////////////////////////
                        JSONObject jsonAddress = response.getJSONObject("address");
                        sal1 = jsonAddress.getString("address_line_1");
                        sal2 = jsonAddress.getString("address_line_2");
                        scity = jsonAddress.getString("city");
                        szipcode = jsonAddress.getString("zip_code");
                        saddress = sal1 + ", " + sal2 + ", " + scity + ", " + szipcode;

                        Picasso.get().load(spicture).into(picture);
                        name.setText(sname);
                        email.setText(semail);
                        address.setText(saddress);
                        fname.setText(sfname);
                        lname.setText(slname);
                        age.setText(sage);
                        phone.setText(sphone);
                        al1.setText(sal1);
                        al2.setText(sal2);
                        city.setText(scity);
                        zipcode.setText(szipcode);
                        ///////////////////////////////////////////////////////////////////////////////////////////
                    } catch (JSONException e) {
                        Log.e("ERROR", e.toString());
                    }

                },
                error -> Log.e("VolleyError", error.toString()))
        {
            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> headers = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserCredentials", Context.MODE_PRIVATE);
                headers.put("Authorization", "Bearer "+ sharedPreferences.getString("access_token", null));
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void updateProfile(){

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fname", fname.getText().toString());
            jsonObject.put("lname", lname.getText().toString());
            jsonObject.put("phone", phone.getText().toString());
            jsonObject.put("age", age.getText().toString());

            jsonObject.put("addline1", al1.getText().toString());
            jsonObject.put("addline2", al2.getText().toString());
            jsonObject.put("city", city.getText().toString());
            jsonObject.put("zipcode", zipcode.getText().toString());

            Log.d(TAG, "updateProfile: " + jsonObject);

        }catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                getString(R.string.URLProject) + "api/mcustomer/mprofile/" + sid,               //url na pupuntahan nung request
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("RESPONSE", "onResponse: " + response.getString("message"));
//                            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                            SessionClass.FRAGMENT = new ProfileFragment();
                            finish();
//                            startActivity(intent);
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
