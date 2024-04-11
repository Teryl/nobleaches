package org.example;

import java.util.Objects;

public class MachineData {
    String name;
    String status;
    String block;
    String lastUsed;
    String machineTopic;

    // Constructor
    public MachineData(String name, String status, String block, String lastUsed, String machineTopic) {
        this.name = name;
        this.status = status;
        this.block = block;
        this.lastUsed = lastUsed;
        this.machineTopic = machineTopic;
    }

    public MachineData() {
        this.name = "";
        this.status = "";
        this.block = "";
        this.lastUsed = "";
        this.machineTopic = "";
    }
    
    // Getters
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
    
    public String getMachineTopic() {
        return machineTopic;
    }
    
    // Setters
    public void setName(String name) {
        this.name = name;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public void setBlock(String block) {
        this.block = block;
    }
    
    public void setLastUsed(String lastUsed) {
        this.lastUsed = lastUsed;
    }
    
    public void setMachineTopic(String machineTopic) {
        this.machineTopic = machineTopic;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MachineData that = (MachineData) obj;
        return Objects.equals(machineTopic, that.machineTopic);
    }

    public void update(MachineData other) {
        this.name = other.name;
        this.status = other.status;
        this.block = other.block;
        this.lastUsed = other.lastUsed;
        this.machineTopic = other.machineTopic;
    }

    @Override
    public MachineData clone() {
        return new MachineData(this.name, this.status, this.block, this.lastUsed, this.machineTopic);
    }

    @Override
    public String toString() {
        return "MachineData{" +
            "name='" + name + '\'' +
            ", status=" + status +
            ", block=" + block +
            ", lastUsed=" + lastUsed +
            ", machineTopic='" + machineTopic + '\'' +
            '}';
    }
}