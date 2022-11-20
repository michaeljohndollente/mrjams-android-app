package com.example.mrjams.ui.clinic;

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

public class ClinicListFragment extends Fragment  {

    RecyclerView RecyclerViewClinicList;
    ArrayList<ClinicList> clinicLists = new ArrayList<ClinicList>();
    ClinicRVA clinicRVA;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.clinic_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerViewClinicList = view.findViewById(R.id.RecyclerViewClinicList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerViewClinicList.setLayoutManager(layoutManager);

        clinicRVA = new ClinicRVA(getActivity());
        RecyclerViewClinicList.setAdapter(clinicRVA);

//        FloatingActionButton fabSearchClinic = view.findViewById(R.id.fabSearchClinic);
//        fabSearchClinic.setOnClickListener(view1 -> {
//            //show dialogue ng search
//        });
        getClinicList();
    }

    public void getClinicList(){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getString(R.string.URLProject) + "api/mcustomer/mcliniclist",
                null,
                response -> {
                    try {
                        String message = response.toString();
                        Log.d("ClinicList", message);
                        JSONArray jsonArray = response.getJSONArray("all");
                        for (int x = 0; x < jsonArray.length(); x++) {
                            String jsonPackage = "no packages", jsonService = "no services";
                            JSONObject jsonObject = jsonArray.getJSONObject(x);

                            if (!jsonObject.isNull("packages")){
                                jsonPackage = jsonObject.getJSONObject("packages").getString("name");
                            }

                            if (!jsonObject.isNull("services")){
                                jsonService = jsonObject.getJSONObject("services").getString("name");
                            }

                            ClinicList clinicList = new ClinicList(
                                    String.valueOf(jsonObject.getInt("id")),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("phone"),
                                    jsonObject.getString("telephone"),
                                    jsonObject.getString("type_of_clinic"),
                                    jsonObject.getString("address_line_1"),
                                    jsonObject.getString("address_line_2"),
                                    jsonPackage,
                                    jsonService
                            );
                            clinicLists.add(clinicList);
                        }
                        clinicRVA.setClinics(clinicLists);
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
