package org.example;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MachineList that = (MachineList) obj;
        return Objects.equals(getMachineList(), that.getMachineList());
    }

    public MachineList clone() {
        MachineList clone = new MachineList();
        for (MachineData data : this.machines) {
            clone.machines.add(data.clone()); // Assuming MachineData also has a correctly implemented clone method
        }
        return clone;
    }
    public void merge(MachineList other) {
        Map<String, MachineData> otherMachinesMap = other.machines.stream()
            .collect(Collectors.toMap(
                MachineData::getMachineTopic, 
                Function.identity(),
                (existing, replacement) -> existing)); // keep the existing value
    
        // Iterate over the machines in this list
        for (MachineData thisData : this.machines) {
            // If the other list contains a machine with the same topic, update this machine
            if (otherMachinesMap.containsKey(thisData.getMachineTopic())) {
                thisData.update(otherMachinesMap.get(thisData.getMachineTopic()));
                // Remove the machine from the map to keep track of the machines that have been updated
                otherMachinesMap.remove(thisData.getMachineTopic());
            }
        }
    
        // At this point, otherMachinesMap contains only the machines that were not in the current list
        // Add these machines to the current list
        this.machines.addAll(otherMachinesMap.values());
    }

    
    public void update(MachineList other) {
        Map<String, MachineData> otherMachinesMap = other.machines.stream()
            .collect(Collectors.toMap(
                MachineData::getMachineTopic, 
                Function.identity(),
                (existing, replacement) -> existing)); // keep the existing value
    
        // Iterate over the machines in this list
        for (MachineData thisData : this.machines) {
            // If the other list contains a machine with the same topic, update this machine
            if (otherMachinesMap.containsKey(thisData.getMachineTopic())) {
                thisData.update(otherMachinesMap.get(thisData.getMachineTopic()));
                // Remove the machine from the map to keep track of the machines that have been updated
                otherMachinesMap.remove(thisData.getMachineTopic());
            }
        }
    
        // The machines that are left in the map are the machines that are not in this list
        // Add them to this list
        this.machines.addAll(otherMachinesMap.values());
    }

    public void clear() {
        this.machines.clear();
    }

}
