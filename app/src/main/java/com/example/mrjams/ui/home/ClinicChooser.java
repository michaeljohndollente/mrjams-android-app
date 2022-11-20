package com.example.mrjams.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mrjams.R;
import com.example.mrjams.ui.clinic.ClinicListShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClinicChooser extends AppCompatDialogFragment {
    Context context;
    private DialogListener listener;
    TextView diaName, diaType, diaAdd, diaPhone;
    Button diabutton;


    private List<ClinicMaps> clinicMapsList;
    String id, name, type, phone, telephone;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder clinic = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View diaMap = layoutInflater.inflate(R.layout.dialog_marker, null);

        Bundle bundle = getArguments();
        id = bundle.getString("clinic_id");
        diaName = diaMap.findViewById(R.id.diaName);
        diaType = diaMap.findViewById(R.id.diaType);
        diaAdd = diaMap.findViewById(R.id.diaAdd);
        diaPhone = diaMap.findViewById(R.id.diaPhone);
        diabutton = diaMap.findViewById(R.id.diabutton);

        context = getActivity().getApplicationContext();

        clinic.setView(diaMap);

        diabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ClinicListShow.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("type", type);
                intent.putExtra("phone", phone);
                intent.putExtra("telephone", telephone);
                Log.d("TAG", "onClick: " + intent.getStringExtra("id") + " " + intent.getStringExtra("name") + " " + intent.getStringExtra("type") + " " + intent.getStringExtra("phone") + " " + intent.getStringExtra("telephone"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                listener.clinic_id(id);
            }
        });
        loadClinicThrow(id);
        return clinic.create();
    }

    private void loadClinicThrow(String id) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getString(R.string.URLProjectwithhttp) + "api/mcustomer/mcustomermap/" + id,
                null,
                response -> {
                    try {
                        JSONArray clinicArray = response.getJSONArray("clinic_data");
                        Log.d("TAG", "loadClinicThrow: " + clinicArray);
                        for (int i = 0; i < clinicArray.length(); i++) {
                            JSONObject jsonObject = clinicArray.getJSONObject(i);
                            ClinicMaps clinicinmaps = new ClinicMaps(
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
                            name = clinicinmaps.getName();
                            type = clinicinmaps.getType();
                            phone = clinicinmaps.getPhone();
                            telephone = clinicinmaps.getTelephone();
                            diaName.setText(clinicinmaps.getName());
                            diaType.setText(clinicinmaps.getType());
                            diaAdd.setText(clinicinmaps.getAddress_line_1() + ", " + clinicinmaps.getAddress_line_2() + ",\n" + clinicinmaps.getCity() + ", " + clinicinmaps.getZipcode());
                            diaPhone.setText(clinicinmaps.getPhone());
                        }
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
        void clinic_id(String id);

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
