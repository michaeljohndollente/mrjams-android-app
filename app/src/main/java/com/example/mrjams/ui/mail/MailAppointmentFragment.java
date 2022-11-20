package com.example.mrjams.ui.mail;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

public class MailAppointmentFragment extends Fragment {

    RecyclerView RecyclerViewMailAppointment;
    ArrayList<MailAppointment> mailAppointments = new ArrayList<MailAppointment>();
    MailAppointmentRVA mailAppointmentRVA;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mail_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        RecyclerViewMailAppointment = view.findViewById(R.id.RecyclerViewAnnouncement);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerViewMailAppointment.setLayoutManager(layoutManager);

        mailAppointmentRVA = new MailAppointmentRVA(getActivity());
        RecyclerViewMailAppointment.setAdapter(mailAppointmentRVA);

        getMailAppointments();
    }

    private void getMailAppointments() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getString(R.string.URLProject) + "api/mcustomer/mmailappointment",
                null,
                response -> {
                    try {
                        String message = response.toString();

                        Log.d("Mail Appointments", message);

                        JSONArray jsonArray = response.getJSONArray("all");
                        for (int x = 0; x < jsonArray.length(); x++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(x);
                            MailAppointment mailAppointment = new MailAppointment(
                                    String.valueOf(jsonObject.getInt("id")),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("remark"),
                                    jsonObject.getString("appointed_at"),
                                    jsonObject.getString("created_at")
                                    );
                            mailAppointments.add(mailAppointment);
                        }
                        mailAppointmentRVA.setMailappointments(mailAppointments);
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
