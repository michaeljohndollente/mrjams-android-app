package com.example.mrjams.ui.login;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mrjams.MainActivity;
import com.example.mrjams.R;
import com.example.mrjams.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {



    public String personGivenName, personFamilyName, personEmail, personToken;
    public Uri personPhoto;

    SharedPreferences sp;

    GoogleSignInOptions gso;
    private ActivityLoginBinding binding;
    private static final int RC_SIGN_IN = 100;
    public GoogleSignInClient gsc;

    private static final String TAG = "GOOGLE_SIGN_IN_TAG";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getSharedPreferences("UserCredentials", Context.MODE_PRIVATE);

        binding = ActivityLoginBinding.inflate((getLayoutInflater()));
        setContentView(binding.getRoot());

        // configuration of Google Signin
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

//        LoginDecision();

        // Google SigninButton: Click to begin Google Signin
        binding.googleSignInBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG, "Google SignIn");
                Intent intent = gsc.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });
    }

    private void LoginDecision() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserCredentials", Context.MODE_PRIVATE);
        if(sp.getString("access_token", "").equals("")){
            Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Go login", Toast.LENGTH_SHORT).show();
            autoLogin();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignIn
        if (requestCode == RC_SIGN_IN){
            Log.d(TAG, "onActivityResult:1");
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //google success -> auth
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                if (account != null) {
                    personGivenName = account.getGivenName();
                    personFamilyName = account.getFamilyName();
                    personEmail = account.getEmail();
                    String personId = account.getId();
                    personPhoto = account.getPhotoUrl();
                    personToken = account.getIdToken();

                    Log.d("TRY",personGivenName);
                    Log.d("TRY",personFamilyName);
                    Log.d("TRY",personEmail);
                    Log.d("TRY",personId);
                    Log.d("TRY","TOKEN: "+ personToken);
                    Log.d("TRY", String.valueOf(personPhoto));

                    storeLogin();

                }
            }
            catch (Exception e){
                Log.d(TAG, "error:"+e);
            }
        }
    }

    private void storeLogin() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", personEmail+"");
            jsonObject.put("avatar", personPhoto+"");
        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.d(TAG, "2nd Log " + jsonObject);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,    //kung anong type of request
                getString(R.string.URLProject) + "api/auth/register",               //url na pupuntahan nung request
                jsonObject,               //data na ilalagay sa request
                response -> {
                    try {
                        Log.d("try:", "nakapasok ka na sa Volley");
                        String token = response.getString("token");
                        JSONObject user_name = response.getJSONObject("user");

//                        Toast.makeText(getApplicationContext(), token, Toast.LENGTH_LONG).show();
                        Log.d(TAG, "user_name: " + user_name);

                        String roleCheck = user_name.getString("role");
                        Log.d(TAG, "Rolecheck: " + roleCheck);

                        if(roleCheck.equals("null")){
                            Log.d("Regsiter", "Register ka na");
                            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                            intent.putExtra("firstname", personGivenName);
                            intent.putExtra("lastname", personFamilyName);
                            intent.putExtra("email", personEmail);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else if(roleCheck.equals("customer")){ //hindi null ang mga customer
                            Log.d("Login", "Ok na Customer ka naman pala");
                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserCredentials", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("access_token", token);
                            Log.d(TAG, "editor: "+ editor);
                            editor.apply();
                            Log.d(TAG, "storeLogin: "+ token);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Log.d("Clinic", "Di ka pwede dito Clinic user ka");
                            // TODO FRAGMENT / DISPLAY FOR CLINIC
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                (error)-> {
                    Toast.makeText(getApplicationContext(), error+"", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "error: " + error);
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void autoLogin(){
        sp = getApplicationContext().getSharedPreferences("UserCredentials", Context.MODE_PRIVATE);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", personEmail+"");
            jsonObject.put("avatar", personPhoto+"");
        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.d(TAG, "2nd Log " + jsonObject);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,    //kung anong type of request
                getString(R.string.URLProject) + "api/auth/register",               //url na pupuntahan nung request
                jsonObject,               //data na ilalagay sa request
                response -> {
                    try {
                        Log.d("try:", "nakapasok ka na sa Volley");
                        String token = response.getString("token");
                        JSONObject user_name = response.getJSONObject("user");

//                        Toast.makeText(getApplicationContext(), token, Toast.LENGTH_LONG).show();
                        Log.d(TAG, "user_name: " + user_name);

                        String roleCheck = user_name.getString("role");
                        Log.d(TAG, "Rolecheck: " + roleCheck);

                        String statusCheck = user_name.getString("status");

                        if(roleCheck.equals("null")){
                            Log.d("Regsiter", "Register ka na");
                            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                            intent.putExtra("firstname", personGivenName);
                            intent.putExtra("lastname", personFamilyName);
                            intent.putExtra("email", personEmail);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else if(roleCheck.equals("customer")){ //hindi null ang mga customer
                            Log.d("Login", "Ok na Customer ka naman pala");
                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserCredentials", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("access_token", token);
                            Log.d(TAG, "editor: "+ editor);
                            editor.apply();
                            Log.d(TAG, "storeLogin: "+ token);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Log.d("Clinic", "Di ka pwede dito Clinic user ka");
                            // TODO FRAGMENT / DISPLAY FOR CLINIC
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                (error)-> {
                    Toast.makeText(getApplicationContext(), error+"", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "error: " + error);
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
}