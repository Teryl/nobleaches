package com.example.nobleaches;
import android.content.Context;

import java.util.List;
import java.util.ArrayList;

public class MachineListReader {
    private static MachineListReader instance;
    private List<MachineData> machineList;
    private Context context;

    private MachineListReader(Context context) {
        this.context = context.getApplicationContext();
        machineList = new ArrayList<>();
    }

    public static synchronized MachineListReader getInstance(Context context) {
        if (instance == null || !instance.context.equals(context.getApplicationContext())) {
            instance = new MachineListReader(context);
        }
        return instance;
    }

    public MachineData getMachineDataByType(String machineType) {
        for (MachineData machineData : machineList) {
            if (machineData.getName().equals(machineType)) {
                return machineData;
            }
        }
        return null;
    }
    public List<MachineData> getMachineList() {
        return machineList;
    }

    public void setMachineList(List<MachineData> machineList) {
        this.machineList = machineList;
    }

}
