package com.example.nobleaches;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.ArrayList;

public class MachineAdapter extends RecyclerView.Adapter<MachineAdapter.ViewHolder> {
    private ArrayList<MachineData> machineList;
    private Context context;


    public MachineAdapter() {
        this.machineList = new ArrayList<>();
        machineList.add(new MachineData("Washer 1", "Available", "Block 55", "20/3/2024 9:23PM", "washer_1"));
        machineList.add(new MachineData("Washer 2", "59 mins left", "Block 57", "20/3/2024 9:23PM", "washer_2"));
        machineList.add(new MachineData("Dryer 1", "38 mins left", "Block 59", "21/3/2024 10:00AM", "dryer_1"));
        machineList.add(new MachineData("Dryer 2", "Available", "Block 57", "21/3/2024 11:11AM", "dryer_2"));


    }

    @NonNull
    @Override
    public MachineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.machine_adapter, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MachineData machine = machineList.get(position);
        holder.buttonWasher.setText(machine.getType());
        holder.textStatus.setText(machine.getStatus());

        //Adding machineList to global machineList
        MachineListReader.getInstance().setMachineList(machineList);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, Machine.class);
                intent.putExtra("machine_Type", machine.getType());
                intent.putExtra("machine_Block", machine.getBlock());
                intent.putExtra("machine_Status", machine.getStatus());
                intent.putExtra("machine_LastUsed", machine.getLast_used());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return machineList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Button buttonWasher;
        TextView textStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            buttonWasher = itemView.findViewById(R.id.buttonWasher);
            textStatus = itemView.findViewById(R.id.textStatus);
        }
    }
}
