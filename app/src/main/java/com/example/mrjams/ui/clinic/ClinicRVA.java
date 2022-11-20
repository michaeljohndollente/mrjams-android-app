package com.example.mrjams.ui.clinic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mrjams.R;

import java.util.ArrayList;

public class ClinicRVA extends RecyclerView.Adapter<ClinicRVA.ViewHolder> {

    Context context;
    ArrayList<ClinicList> clinics = new ArrayList<ClinicList>();

    public ClinicRVA(Context context){
        this.context = context;
    }

    public void setClinics(ArrayList<ClinicList> clinics){
        this.clinics = clinics;
        notifyDataSetChanged();
    }

    @NonNull
    public ClinicRVA.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.clinic_list_item,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ClinicRVA.ViewHolder h, int position) {
        // holder ng mga data
        h.textClinicName.setText(clinics.get(position).getName());
        h.textClinicType.setText(clinics.get(position).getType_of_clinic());
        h.textAddress.setText("ADDRESS: " + clinics.get(position).getAddress_line_1()+" \n"+clinics.get(position).getAddress_line_2());
        h.textPackages.setText("PACKAGES: " + clinics.get(position).getPackages()+ " ...");
        h.textServices.setText("SERVICES: " + clinics.get(position).getServices()+ " ...");

        h.cardClinicInfo.setOnClickListener(view ->{
            Intent showClinic = new Intent(context, ClinicListShow.class);
            showClinic.putExtra("id", clinics.get(position).getId());
            showClinic.putExtra("name", clinics.get(position).getName());
            showClinic.putExtra("type", clinics.get(position).getType_of_clinic());
            showClinic.putExtra("phone", clinics.get(position).getPhone());
            showClinic.putExtra("telephone", clinics.get(position).getTelephone());
            showClinic.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(showClinic);
        });
    }

    @Override
    public int getItemCount() {
        return clinics.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // mga element sa layout
        TextView textClinicName, textClinicType, textAddress, textServices, textPackages;
        CardView cardClinicInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textClinicName = itemView.findViewById(R.id.txtClinicName);
            textClinicType = itemView.findViewById(R.id.txtClinicType);
            textAddress = itemView.findViewById(R.id.txtAddress);
            textServices = itemView.findViewById(R.id.txtServices);
            textPackages = itemView.findViewById(R.id.txtPackages);
            cardClinicInfo = itemView.findViewById(R.id.cardClinicInfo);
        }
    }
}
