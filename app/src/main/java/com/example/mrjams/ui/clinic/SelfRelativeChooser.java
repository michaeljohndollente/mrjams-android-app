package com.example.mrjams.ui.clinic;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.mrjams.R;
import com.example.mrjams.ui.SessionClass;
import com.example.mrjams.ui.appointment.AppointmentActivity;
import com.example.mrjams.ui.mail.MailAppointmentFragment;

public class SelfRelativeChooser extends AppCompatDialogFragment {
    Context context;
    private DialogListener listener;

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle saveInstancesState){

//        String clinic_id, clinic_name,  customer_fname, customer_mname,
//                customer_lname, customer_phone, customer_age, customer_gender, customer_AL1,
//                customer_AL2, user_id;
        TextView txtAppointClinic;
        Button btnMyself, btnRelative;

        AlertDialog.Builder appointmentBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View myselfOrRelative = layoutInflater.inflate(R.layout.dialog_appointment, null);

        Bundle bundle = getArguments();

        txtAppointClinic = myselfOrRelative.findViewById(R.id.appointClinicName);
        btnMyself = myselfOrRelative.findViewById(R.id.appointMyself);
        btnRelative = myselfOrRelative.findViewById(R.id.appointRelative);

        txtAppointClinic.setText(bundle.getString("clinic_name"));

        context = getActivity().getApplicationContext();

        appointmentBuilder.setView(myselfOrRelative);

        btnMyself.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent appointment = new Intent(context, AppointmentActivity.class);
                appointment.putExtra("clinic_id", bundle.getString("clinic_id"));
                appointment.putExtra("clinic_name", bundle.getString("clinic_name"));
                appointment.putExtra("user_id", bundle.getString("user_id"));
                appointment.putExtra("customer_fname", bundle.getString("customer_fname"));
                appointment.putExtra("customer_mname", bundle.getString("customer_mname"));
                appointment.putExtra("customer_lname", bundle.getString("customer_lname"));
                appointment.putExtra("customer_age", bundle.getString("customer_age"));
                appointment.putExtra("customer_gender", bundle.getString("customer_gender"));
                appointment.putExtra("customer_phone", bundle.getString("customer_phone"));
                appointment.putExtra("customer_AL1", bundle.getString("customer_AL1"));
                appointment.putExtra("customer_AL2", bundle.getString("customer_AL2"));
                Log.d(TAG, "onClick: " + appointment.getExtras());
                appointment.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(appointment);
                getDialog().dismiss();
            }
        });

        btnRelative.setOnClickListener(v -> {
                Intent appointment = new Intent(context, AppointmentActivity.class);
                appointment.putExtra("clinic_id", bundle.getString("clinic_id"));
                appointment.putExtra("clinic_name", bundle.getString("clinic_name"));
                appointment.putExtra("user_id", bundle.getString("user_id"));
                Log.d(TAG, "onClick: " + appointment.getExtras());
                appointment.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(appointment);
                getDialog().dismiss();
            });

        return appointmentBuilder.create();

    }

    public interface DialogListener {
        void selfRelative(String name);
    }
}
