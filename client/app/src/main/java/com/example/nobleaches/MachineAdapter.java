package com.example.nobleaches;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MachineAdapter extends RecyclerView.Adapter<MachineAdapter.ViewHolder> {
    private ArrayList<MachineData> machineDisplayList;
    private final Context context;

    Handler rHandler = new Handler();

    public MachineAdapter(Context context, List<MachineData> machineDataList) {
        this.context = context;
        this.machineDisplayList = new ArrayList<>(machineDataList);
    }

    @NonNull
    @Override
    public MachineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.machine_adapter, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MachineData machine = machineDisplayList.get(position);
        holder.buttonWasher.setText(machine.getName());
        holder.textStatus.setText(machine.getStatus());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, Machine.class);
                intent.putExtra("machine_Type", machine.getName());
                intent.putExtra("machine_Block", machine.getBlock());
                intent.putExtra("machine_Status", machine.getStatus());
                intent.putExtra("machine_LastUsed", machine.getLastUsed());
                context.startActivity(intent);
            }
        });

        //Adding machineList to global machineList
        MachineListReader.getInstance(context).setMachineList(machineDisplayList);

        Runnable refresh = new Runnable() {
            @Override
            public void run() {
                MachineData machine = machineDisplayList.get(position);
                holder.buttonWasher.setText(machine.getName());
                holder.textStatus.setText(machine.getStatus());
                rHandler.postDelayed(this, GlobalConfig.getInstance().getUpdateInterval());
            }
        };
        rHandler.postDelayed(refresh, GlobalConfig.getInstance().getUpdateInterval());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, Machine.class);
                intent.putExtra("machine_Type", machine.getName());
                intent.putExtra("machine_Block", machine.getBlock());
                intent.putExtra("machine_Status", machine.getStatus());
                intent.putExtra("machine_LastUsed", machine.getLastUsed());
                context.startActivity(intent);
            }
        });
    }




    @Override
    public int getItemCount() {
        return machineDisplayList.size();
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

    public void updateData(List<MachineData> newData) {
        machineDisplayList.clear();
        machineDisplayList.addAll(newData);
        notifyDataSetChanged();
    }

}
