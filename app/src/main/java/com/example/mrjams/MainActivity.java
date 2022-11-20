package com.example.mrjams;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mrjams.ui.SessionClass;
import com.example.mrjams.ui.login.LoginActivity;
import com.example.mrjams.ui.login.RegisterActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView name, email;
    ImageView picture;
    SharedPreferences sp;
    public GoogleSignInClient gsc;
    private AppBarConfiguration mAppBarConfiguration;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_cliniclist, R.id.nav_mail, R.id.nav_announcement, R.id.nav_profile, R.id.nav_logout)
        .setDrawerLayout(drawer)
        .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if( SessionClass.FRAGMENT != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, SessionClass.FRAGMENT).commit();
        }

        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(menuItem -> {
            sp = getApplicationContext().getSharedPreferences("UserCredentials", Context.MODE_PRIVATE);
            sp.edit().remove("access_token").apply();

            Toast.makeText(this, "Sign Out", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        });

        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);

        name = headerLayout.findViewById(R.id.txtNavName);
        email = headerLayout.findViewById(R.id.txtNavEmail);
        picture = headerLayout.findViewById(R.id.imgNavPicture);
        getProfile();

    }

    private void getProfile() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getString(R.string.URLProject) + "api/mcustomer/mprofile",
                null,
                response -> {
                    try {
                        String mpicture, mname, memail, mfname,  mlname;
                        ///////////////////////////////////////////////////////////////////////////////////////////
                        JSONObject jsonUser = response.getJSONObject("user");
                        memail = jsonUser.getString("email");
                        mpicture = jsonUser.getString("avatar");
                        ///////////////////////////////////////////////////////////////////////////////////////////
                        JSONObject jsonCustomer = response.getJSONObject("customer");
                        mfname = jsonCustomer.getString("fname");
                        mlname = jsonCustomer.getString("lname");
                        mname = mfname + " " + mlname;
                        ///////////////////////////////////////////////////////////////////////////////////////////
                        Picasso.get().load(mpicture).into(picture);
                        name.setText(mname);
                        email.setText(memail);
                        Log.d(TAG, "getProfile: " + memail + " " + mpicture + " " + mname);

                        ///////////////////////////////////////////////////////////////////////////////////////////
                    } catch (JSONException e) {
                        Log.e("ERROR", e.toString());
                    }

                },
                error -> Log.e("VolleyError", error.toString())) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}