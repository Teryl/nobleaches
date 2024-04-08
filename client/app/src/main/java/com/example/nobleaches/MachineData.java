package com.example.nobleaches;

public class MachineData {
    private String Type;
    private String Status;
    private String Block;
    private String Last_used;

    public MachineData(String Type, String Status, String Block,String Last_used) {
        this.Type = Type;
        this.Status = Status;
        this.Block = Block;
        this.Last_used = Last_used;
    }


    public String getType() {
        System.out.println(Type);
        return Type;
    }

    public String getStatus() {
        System.out.println(Status);
        return Status;
    }

    public String getBlock() {
        return Block;
    }

    public String getLast_used() {
        return Last_used;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setBlock(String block) {
        Block = block;
    }

    public void setLast_used(String last_used) {
        Last_used = last_used;
    }
}
