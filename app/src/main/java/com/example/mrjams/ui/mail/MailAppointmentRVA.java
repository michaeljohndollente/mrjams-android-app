package com.example.mrjams.ui.mail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mrjams.R;

import java.util.ArrayList;

public class MailAppointmentRVA extends RecyclerView.Adapter<MailAppointmentRVA.ViewHolder> {

    Context context;
    ArrayList<MailAppointment> mailappointments = new ArrayList<MailAppointment>();

    public MailAppointmentRVA (Context context){
        this.context = context;
    }

    public void setMailappointments(ArrayList<MailAppointment> mailappointments){
        this.mailappointments = mailappointments;
        notifyDataSetChanged();
    }

    @NonNull
    public MailAppointmentRVA.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mail_item,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {

        h.textCliName.setText(mailappointments.get(position).getName());

        if(mailappointments.get(position).getRemark().equals("done")){
            h.imgStatus.setColorFilter(context.getResources().getColor(R.color.success));
            h.txtAnnMessage.setText("This clinic has completed your appointment....");
        } else if (mailappointments.get(position).getRemark().equals("pending")){
            h.imgStatus.setColorFilter(context.getResources().getColor(R.color.warning));
            h.txtAnnMessage.setText("Your appointment is scheduled on " + mailappointments.get(position).getAppointment_date() + ".");
        } else if (mailappointments.get(position).getRemark().equals("declined") || mailappointments.get(position).getRemark().equals("danger")){
            h.imgStatus.setColorFilter(context.getResources().getColor(R.color.danger));
            h.txtAnnMessage.setText("Your appointment on " + mailappointments.get(position).getAppointment_date() + "was declined.");
        } else if (mailappointments.get(position).getRemark().equals("accepted")){
            h.imgStatus.setColorFilter(context.getResources().getColor(R.color.primary));
            h.txtAnnMessage.setText("Your appointment on " + mailappointments.get(position).getAppointment_date() + " was accepted.");
        } else if (mailappointments.get(position).getRemark().equals("negotiating")){
            h.imgStatus.setColorFilter(context.getResources().getColor(R.color.muted));
            h.txtAnnMessage.setText("The clinic would like to reschedule you on");
        } else if (mailappointments.get(position).getRemark().equals("expired")){
            h.imgStatus.setColorFilter(context.getResources().getColor(R.color.dark));
            h.txtAnnMessage.setText("Your Appointment from this Clinic has been Expired.");
        } else if (mailappointments.get(position).getRemark().equals("cancelled")){
            h.imgStatus.setColorFilter(context.getResources().getColor(R.color.secondary));
            h.txtAnnMessage.setText("Your appointment on " + mailappointments.get(position).getAppointment_date() + " was cancelled.");
        }

        h.textDateCreated.setText(mailappointments.get(position).getDate_created());

        h.cardMailViewDetails.setOnClickListener(v -> {
            Intent showAppointment = new Intent(context, MailAppointmentShow.class);

            showAppointment.putExtra("id", mailappointments.get(position).getId());
            showAppointment.putExtra("clinic_name", mailappointments.get(position).getName());
            showAppointment.putExtra("remark", mailappointments.get(position).getRemark());
            showAppointment.putExtra("date_created", mailappointments.get(position).getDate_created());
            showAppointment.putExtra("appointment_date", mailappointments.get(position).getAppointment_date());

            showAppointment.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(showAppointment);
        });

    }

    @Override
    public int getItemCount() {
        return mailappointments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView textCliName, textDateCreated;
        TextView textCliName, textDateCreated, txtAnnMessage;
        ImageView imgStatus;
        CardView cardMailViewDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAnnMessage = itemView.findViewById(R.id.txtMessage);
            textCliName = itemView.findViewById(R.id.txtMClinicName);
            imgStatus = itemView.findViewById(R.id.imgStatus);
            textDateCreated = itemView.findViewById(R.id.txtMDateCreated);
            cardMailViewDetails = itemView.findViewById(R.id.cardMailViewDetails);
        }
    }
}
