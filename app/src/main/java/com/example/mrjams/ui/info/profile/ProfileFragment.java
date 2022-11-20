package com.example.mrjams.ui.info.profile;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mrjams.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private static final int REQUEST_IMAGE = 2;
    String spicture, sname, semail, saddress, sfname, smname, slname, sage, sphone, sal1, sal2, scity, szipcode;
    ImageView picture;
    TextView name, email, address;
    EditText fname, lname, age, phone, al1, al2, city, zipcode;
    Button edit;
    private Bitmap img;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        picture = view.findViewById(R.id.imgPicture);

        name = view.findViewById(R.id.txtProfileName);
        email = view.findViewById(R.id.txtProfileEmail);
        address = view.findViewById(R.id.txtProfileAddress);

        fname = view.findViewById(R.id.tboxProfileFname);
        lname = view.findViewById(R.id.tboxProfileLname);
        age = view.findViewById(R.id.tboxProfileAge);
        phone = view.findViewById(R.id.tboxProfilePhone);
        al1 = view.findViewById(R.id.tboxProfileAL1);
        al2 = view.findViewById(R.id.tboxProfileAL2);
        city = view.findViewById(R.id.tboxProfileCity);
        zipcode = view.findViewById(R.id.tboxProfileZipcode);

        edit = view.findViewById(R.id.btnEditProfile);

        getProfile();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });




    }

    private void getProfile() {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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

//                        Log.d(TAG, "jsonAddress: " + jsonAddress);
                        ///////////////////////////////////////////////////////////////////////////////////////////
                    } catch (JSONException e) {
                        Log.e("ERROR", e.toString());
                    }

                },
                error -> Log.e("VolleyError", error.toString()))
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserCredentials", Context.MODE_PRIVATE);
                headers.put("Authorization", "Bearer "+ sharedPreferences.getString("access_token", null));
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }



}