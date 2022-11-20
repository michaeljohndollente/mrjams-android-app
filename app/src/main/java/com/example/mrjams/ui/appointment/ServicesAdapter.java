package com.example.mrjams.ui.appointment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mrjams.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.MyViewHolder>{
    Context context;
    List<Services> service_list;
    ArrayList<Integer> serList = new ArrayList<>();
    ArrayList<String> serListID = new ArrayList<>();

    public ServicesAdapter(Context applicationContext, List<Services> services_list_data) {
        this.service_list = services_list_data;
        this.context = applicationContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_services_chooser_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Services servicesList = service_list.get(position);

        holder.tvServiceItem.setText(servicesList.getName());

        holder.cboxServiceItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    serListID.add(service_list.get(holder.getAdapterPosition()).getId());
                    serList.add(holder.getAdapterPosition());
                    Collections.sort(serList);
//                    Intent intent = new Intent(context, AppointmentActivity.class);
                    Bundle bundle1 = new Bundle();
                    Bundle bundle2 = new Bundle();
                    bundle1.putString("services_ids", serList.toString());
                    bundle2.putString("service_id", String.valueOf(serListID));
                    ServicesChooser servicesChooser = new ServicesChooser();
                    servicesChooser.setArguments(bundle1);
                    servicesChooser.setArguments(bundle2);
                }
                else{
                    serList.remove(holder.getAdapterPosition());
                    Log.d("TAG", "onCheckedChanged: " + serList);
                }
//                Log.d("INTEEEEEEEEEEENT", "onCheckedChanged: " + intent.getExtras());
//                servicesChooser.setArguments(bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return service_list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvServiceItem;
        private CheckBox cboxServiceItem;
//        SharedPreferences sp;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceItem = itemView.findViewById(R.id.tvServiceItem);
            cboxServiceItem = itemView.findViewById(R.id.cboxServiceItem);
//            sp = itemView.getContext().getSharedPreferences("UserCredentials", Context.MODE_PRIVATE);
        }
    }
}
