package com.example.mrjams.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mrjams.MainActivity;
import com.example.mrjams.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText tboxFname, tboxMname, tboxLname, tboxEmail, tboxAge,  tboxAddLine1, tboxAddLine2,tboxCity, tboxZip, tboxContactNum;
    String firstname, lastname, email;
    AutoCompleteTextView tboxGender;

    private Button BtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tboxFname = findViewById(R.id.tboxFname);
        tboxMname = findViewById(R.id.tboxMname);
        tboxLname = findViewById(R.id.tboxLname);
        tboxEmail = findViewById(R.id.tboxEmail);
        tboxAge = findViewById(R.id.tboxAge);
        tboxAddLine1 = findViewById(R.id.tboxAddLine1);
        tboxAddLine2 = findViewById(R.id.tboxAddLine2);
        tboxCity = findViewById(R.id.tboxCity);
        tboxZip = findViewById(R.id.tboxZip);
        tboxContactNum = findViewById(R.id.tboxContactNum);

        tboxGender = findViewById(R.id.tboxGender);
        ArrayAdapter<CharSequence> gender = ArrayAdapter.createFromResource(this, R.array.genders, android.R.layout.simple_dropdown_item_1line);
        tboxGender.setAdapter(gender);

        if(getIntent().hasExtra("email")) {
            firstname = getIntent().getStringExtra("firstname");
            lastname = getIntent().getStringExtra("lastname");
            email = getIntent().getStringExtra("email");
            tboxFname.setText(firstname);
            tboxLname.setText(lastname);
            tboxEmail.setText(email);
            tboxFname.setEnabled(false);
            tboxLname.setEnabled(false);
            tboxEmail.setEnabled(false);
        }

        BtnRegister = findViewById(R.id.btnRegister);

        BtnRegister.setOnClickListener(v -> {
            String e1, e3, e5, e8, e2, e6, e7;

            e1 = tboxMname.getText().toString();
            e2 = tboxAge.getText().toString();
            e3 = tboxAddLine1.getText().toString();
            e5 = tboxCity.getText().toString();
            e6 = tboxZip.getText().toString();
            e7 = tboxContactNum.getText().toString();
            e8 = tboxGender.getText().toString();

            boolean checker = validation(e1, e2, e3, e5, e6, e7, e8);

            if (checker){
                Toast.makeText(this, "Registration Successfully", Toast.LENGTH_SHORT).show();
                storeRegistration();
            } else{
                Toast.makeText(this, "Please Check information again", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private Boolean validation(String e1, String e2, String e3, String e5, String e6, String e7, String e8) {
        if(!e1.isEmpty()) {
            if (!e1.matches("[^0-9\\.\\,\\\"\\?\\!\\;\\:\\#\\$\\%\\&\\(\\)\\*\\+\\-\\/\\<\\>\\=\\@\\[\\]\\\\\\^\\_\\{\\}\\|\\~]+")) {
                tboxMname.requestFocus();
                tboxMname.setError("Please set a valid Middle Name");
                return false;
            }
        }

        if(e2.isEmpty()){
            tboxAge.requestFocus();
            tboxAge.setError("Age can't be Empty");
            return false;
        } else if(Integer.parseInt(e2) <= 0 || Integer.parseInt(e2) >= 120){
            tboxAge.requestFocus();
            tboxAge.setError("Please enter a valid age");
            return false;
        }

        if(e3.isEmpty()){
            tboxAddLine1.requestFocus();
            tboxAddLine1.setError("Address Line 1 can't be Empty");
            return false;
        }

        if(e5.isEmpty()){
            tboxCity.requestFocus();
            tboxCity.setError("City can't be Empty");
            return false;
        }

        if(e6.isEmpty()){
            tboxZip.requestFocus();
            tboxZip.setError("Zip code can't be Empty");
            return false;
        } else if(!e6.matches("[0-9]{4}")){
            tboxZip.requestFocus();
            tboxZip.setError("Please enter a valid zip code");
            return false;
        }

        if(e7.isEmpty()){
            tboxContactNum.requestFocus();
            tboxContactNum.setError("Contact Number can't be Empty");
            return false;
        } else if(!e7.matches("(09)\\d{9}")){
            tboxContactNum.requestFocus();
            tboxContactNum.setError("Must be a valid/11 - digits number");
            return false;
        }

        if (e8.isEmpty()) {
            tboxGender.requestFocus();
            tboxGender.setError("Gender can't be Empty.");
            return false;
        }

        return true;
    }

    private void storeRegistration() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", tboxEmail.getText().toString());

            jsonObject.put("fname", tboxFname.getText().toString());
            jsonObject.put("mname", tboxMname.getText().toString());
            jsonObject.put("lname", tboxLname.getText().toString());
            jsonObject.put("gender", tboxGender.getText().toString());
            jsonObject.put("phone", tboxContactNum.getText().toString());
            jsonObject.put("age", tboxAge.getText().toString());

            jsonObject.put("address_line_1", tboxAddLine1.getText().toString());
            jsonObject.put("address_line_2", tboxAddLine2.getText().toString());
            jsonObject.put("city", tboxCity.getText().toString());
            jsonObject.put("zip_code", tboxZip.getText().toString());

            jsonObject.put("latitude", tboxZip.getText().toString());
            jsonObject.put("longitude", tboxZip.getText().toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                getString(R.string.URLProject) + "api/auth/user",               //url na pupuntahan nung request
                jsonObject,
                response -> {
                    try {
                        finish();
                        Toast.makeText(RegisterActivity.this, "Register", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    } catch (Error e){
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e("VolleyError", error.toString());
                });
        requestQueue.add(jsonObjectRequest);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}