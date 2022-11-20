package com.example.mrjams.ui.appointment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
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
import java.util.List;
import java.util.Map;

public class ServicesChooser extends AppCompatDialogFragment {
    Context context;
    private DialogListener listener;

    private List<Services> services_list_data;
    private RecyclerView recyclerView;
    ServicesAdapter servicesAdapter;

    ArrayList<String> service_id;

    String id;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.layout_fragment_services_chooser, null);

        Bundle bundle = getArguments();
        id = bundle.getString("clinic_id");

        recyclerView = view.findViewById(R.id.rvServiceItem);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        services_list_data = new ArrayList<>();
        servicesAdapter = new ServicesAdapter(getActivity().getApplicationContext(), services_list_data);

        context = getActivity().getApplicationContext();

    builder.setView(view).setTitle("Choose Services")
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            })
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInter, int j) {
                    ArrayList<Integer> services_ids = servicesAdapter.serList;
                    service_id = servicesAdapter.serListID;
                    Log.d("SERVICES_IDS", "onClick: " + services_ids);

                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < services_ids.size(); i++) {

                        stringBuilder.append(services_list_data.get(i).getName());
//                        Services_Id.add(Integer.parseInt(services_list_data.get(j).getId()));

                        if ( i != services_ids.size() - 1){
                            stringBuilder.append(", ");
                        }
                    }
                    Log.d("STRING BUILDER", "onClick: " +  stringBuilder + services_ids + " SERVICE_ID: " + service_id);
                    listener.services_name(stringBuilder.toString().replace("[ ", "").replace("]",""));
                    listener.services_id(service_id.toString());
                }
            });
        loadServicesThrow(id);
        return builder.create();

    }

    private void loadServicesThrow(String id) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getString(R.string.URLProjectwithhttp) + "api/mcustomer/mappointment/" + id + "/edit",
                null,
                response -> {
                    try {
                        JSONArray clinicArray = response.getJSONArray("service");

                        for (int i = 0; i < clinicArray.length(); i++) {
                            JSONObject jsonObject = clinicArray.getJSONObject(i);
                            Services services = new Services(
                                    jsonObject.getString("id"),
                                    jsonObject.getString("name")
                            );
                            services_list_data.add(services);
                        }
                        recyclerView.setAdapter(servicesAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("volleyError", error.toString()))
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                SharedPreferences sharedPreferences = context.getSharedPreferences("UserCredentials", Context.MODE_PRIVATE);
                headers.put("Authorization", "Bearer "+ sharedPreferences.getString("access_token", null));
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public interface DialogListener {
        void services_name(String name);
        void services_id(String id);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener)context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() + "Must Implement Dialoglistener");
        }
    }


}
