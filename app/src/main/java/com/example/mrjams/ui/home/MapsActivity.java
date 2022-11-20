package com.example.mrjams.ui.home;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mrjams.R;
import com.example.mrjams.ui.SessionClass;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements ClinicChooser.DialogListener {

    SupportMapFragment supportMapFragment;
    GoogleMap mMap;
    String CLINIC_ID;
    FusedLocationProviderClient cli;
    ArrayList<ClinicMaps> clinicMaps= new ArrayList<ClinicMaps>();
    ArrayList<String> clinicMapsAdapter = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        cli = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SessionClass.FRAGMENT = new HomeFragment();
        Intent intent = new Intent(getApplicationContext(), HomeFragment.class);
        startActivity(intent);
    }

    private void getCurrentLocation() {
        @SuppressLint("MissingPermission")
        Task<Location> task = cli.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {

                            LatLng latlang = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions options = new MarkerOptions().position(latlang).title("Here I am");
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlang, 10));
                            googleMap.addMarker(options);

                            mMap = googleMap;

                            String url = getString(R.string.URLProject) + "api/mcustomer/mcustomermap";

                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                    Request.Method.GET,
                                    url,
                                    null,
                                    response -> {
                                        try {
                                            String message = response.toString();
                                            Log.d("ClinicList", message);
                                            JSONArray jsonArray = response.getJSONArray("data");
                                            for (int x = 0; x < jsonArray.length(); x++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(x);
                                                ClinicMaps clinicMap = new ClinicMaps(
                                                        jsonObject.getString("id"),
                                                        jsonObject.getString("name"),
                                                        jsonObject.getString("phone"),
                                                        jsonObject.getString("telephone"),
                                                        jsonObject.getString("type"),
                                                        jsonObject.getString("address_line_1"),
                                                        jsonObject.getString("address_line_2"),
                                                        jsonObject.getString("longitude"),
                                                        jsonObject.getString("latitude"),
                                                        jsonObject.getString("city"),
                                                        jsonObject.getString("zip_code")
                                                );
                                                clinicMaps.add(clinicMap);
                                                String name = clinicMap.getName();
                                                String lati = clinicMap.getLati();
                                                String longi = clinicMap.getLongi();
                                                String type = clinicMap.getType();
                                                String address_line_1 = clinicMap.getAddress_line_1();
                                                String address_line_2 = clinicMap.getAddress_line_2();
                                                String phone = clinicMap.getPhone();
                                                String telephone = clinicMap.getTelephone();
                                                String city = clinicMap.getCity();
                                                String zip = clinicMap.getZipcode();

                                                Log.d(TAG, "onMapReady: " + name + " " + lati + " " + longi);

                                                LatLng clinic = new LatLng(Double.parseDouble(lati), Double.parseDouble(longi));
                                                mMap.addMarker(new MarkerOptions().position(clinic).title(name).
                                                        icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.mr_jams_logo, 110, 130))));
                                                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                                    @Override
                                                    public void onInfoWindowClick(@NonNull Marker marker) {
                                                        ClinicChooser clinicChooser = new ClinicChooser();
                                                        String id;
                                                        id = clinicMaps.get(Integer.parseInt(marker.getId().replace("m", "")) - 1).getId();
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("clinic_id", id);

                                                        clinicChooser.setArguments(bundle);
                                                        clinicChooser.show(getSupportFragmentManager(),"try");
                                                    }
                                                });
//                                                    tryM.showInfoWindow();
//                                                    .snippet(type + "\n\n" + phone  + "\n" + telephone  + "\n" + address_line_1 + address_line_2  + ", " +  city + ",\n" + zip)
                                            }
                                            Log.d("TAG", "getMapClinics: " + clinicMaps);
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
                    });
                }
            }
        });
    }


    public Bitmap resizeMapIcons(int iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(String.valueOf(iconName), "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void clinic_id(String id) {
        CLINIC_ID = id;
    }
}