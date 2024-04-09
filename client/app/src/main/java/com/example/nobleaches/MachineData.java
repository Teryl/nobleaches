package com.example.nobleaches;

import android.util.Log;

import java.util.List;

public class MachineData {
    private String name;
    private String status;
    private String block;
    private String lastUsed;
    private String machineTopic;

    public MachineData(String name, String Status, String Block, String lastUsed, String machineTopic) {
        this.name = name;
        this.status = Status;
        this.block = Block;
        this.lastUsed = lastUsed;
        this.machineTopic = machineTopic;
    }

    public String getMachineTopic() {
        return machineTopic;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getBlock() {
        return block;
    }

    public String getLastUsed() {
        return lastUsed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
        Log.d("Status Update", "Status updated to: "+ status);
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public void setLastUsed(String lastUsed) {
        this.lastUsed = lastUsed;
    }

    public void setMachineTopic(String machineTopic) { this.machineTopic = machineTopic; }

    public static MachineData searchByName(String name, List<MachineData> machineList) {
        for (MachineData machine : machineList) {
            if (machine.getName().equals(name)) {
                return machine;
            }
        }

        //Log error otherwise
        Log.d("Machine Error", "Machine not found");
        return null;
    }

}
