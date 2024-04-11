package org.example;

import java.util.*;

public class MachineList {
    private List<MachineData> machines;
    
    public MachineList() {
        machines = new ArrayList<>();
    }

    public void addMachine(MachineData machine) {
        machines.add(machine);
    }

    public List<MachineData> getMachineList() {
        return machines;
    }
}
