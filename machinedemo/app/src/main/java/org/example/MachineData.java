package org.example;

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
}