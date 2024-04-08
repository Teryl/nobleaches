package com.example.nobleaches;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MachineAdapter extends RecyclerView.Adapter<MachineAdapter.ViewHolder> {
    private final MachineList machinelist;

    public MachineAdapter() {
        this.machinelist = new MachineList();
    }

    @NonNull
    @Override
    public MachineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.machine_adapter, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MachineData machine = machinelist.getMachinelist().get(position);
        holder.buttonWasher.setText(machine.getType());
        holder.textStatus.setText(machine.getStatus());

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
        return machinelist.getMachinelist().size();
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
