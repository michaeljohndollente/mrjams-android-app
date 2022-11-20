package com.example.mrjams.ui.announcement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mrjams.R;

import java.util.ArrayList;

public class AnnouncementRVA extends RecyclerView.Adapter<AnnouncementRVA.ViewHolder> {

    Context context;
    ArrayList<Announcement> announcements = new ArrayList<Announcement>();

    public AnnouncementRVA(Context context) {
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AnnouncementRVA.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.announcement_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementRVA.ViewHolder holder, int position) {

        holder.message.setText(announcements.get(position).getMessage());
        holder.date.setText(announcements.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    public void setAnnonucements(ArrayList<Announcement> announcements) {
        this.announcements = announcements;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, message, date;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.txtAnnName);
            message = itemView.findViewById(R.id.txtAnnMessage);
            date = itemView.findViewById(R.id.txtAnnDate);
        }
    }
}
