package com.example.nobleaches;
import java.util.List;
import java.util.ArrayList;

public class MachineListReader {
    private static MachineListReader instance;
    private List<MachineData> machineList;

    private MachineListReader() {
        machineList = new ArrayList<>();
    }

    public static synchronized MachineListReader getInstance() {
        if (instance == null) {
            instance = new MachineListReader();
        }
        return instance;
    }

    public List<MachineData> getMachineList() {
        return machineList;
    }

    public void setMachineList(List<MachineData> machineList) {
        this.machineList = machineList;
    }

}
