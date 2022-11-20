package com.example.mrjams.ui.announcement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mrjams.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnnouncementFragment extends Fragment {

    RecyclerView RecyclerViewAnnouncement;
    ArrayList<Announcement> announcements = new ArrayList<Announcement>();
    AnnouncementRVA announcementRVA;
    String name, email, contact;

    public static AnnouncementFragment newInstance() {
        return new AnnouncementFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.announcement_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerViewAnnouncement = view.findViewById(R.id.RecyclerViewAnnouncement);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerViewAnnouncement.setLayoutManager(layoutManager);

        announcementRVA = new AnnouncementRVA(getActivity());
        RecyclerViewAnnouncement.setAdapter(announcementRVA);

        getAnnouncement();

        Button sendMessage = view.findViewById(R.id.btnSendMessage);
        Button sysRating = view.findViewById(R.id.btnSysRating);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        sysRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                giveRating();
            }
        });

    }

    private void giveRating(){
        Intent intent = new Intent(getActivity(), RatingActivity.class);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    private void sendMessage(){
        Intent intent = new Intent(getActivity(), ContactActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("email", email);
        intent.putExtra("contact", contact);
        startActivity(intent);
    }

    private void getAnnouncement() {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getString(R.string.URLProject) + "api/mcustomer/mannouncement",
                null,
                response -> {
                    try {
                        String message = response.toString();
                        String mname;
                        Log.d("Mail Appointments", message);
                        JSONArray jsonAnnounce = response.getJSONArray("announceP");
                        for (int i = 0; i < jsonAnnounce.length(); i++) {
                            JSONObject jsonObject = jsonAnnounce.getJSONObject(i);
                            Announcement announcement = new Announcement(
                                    jsonObject.getString("created_at"),
                                    jsonObject.getString("message")
                                );
                            announcements.add(announcement);
                        }
                        announcementRVA.setAnnonucements(announcements);
                        /////////////////////////////////////////////////////////////////
                        JSONObject jsonCustomer = response.getJSONObject("customer");
                        mname = jsonCustomer.getString("mname");
                        if (mname.equals("null")){
                            mname = "";
                        }
                        name = jsonCustomer.getString("fname") + " " + mname + " " + jsonCustomer.getString("lname");
                        contact = jsonCustomer.getString("phone");
                        JSONObject jsonUser = response.getJSONObject("user");
                        email = jsonUser.getString("email");

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