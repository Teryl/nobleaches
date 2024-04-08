package com.example.nobleaches;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MachineList {
    private List<MachineData> machinelist;

    public MachineList() {
        machinelist = new ArrayList<>();
        machinelist.add(new MachineData("Washer 1", "Available", "Block 55", "20/3/2024 9:23PM"));
        machinelist.add(new MachineData("Washer 2", "59 mins left", "Block 57", "20/3/2024 9:23PM"));
        machinelist.add(new MachineData("Dryer 1", "38 mins left", "Block 59", "21/3/2024 10:00AM"));
        machinelist.add(new MachineData("Dryer 2", "Available", "Block 57", "21/3/2024 11:11AM"));
    }

    public List<MachineData> getMachinelist() {
        return machinelist;
    }

}
