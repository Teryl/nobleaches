package org.example;

import com.google.gson.Gson;
import java.util.Properties;

public class GlobalConfig {
    private static GlobalConfig instance;
    private Properties config = new Properties();
    Gson gson = new Gson();
    // Creating machine lists for each block
    static MachineList block55Machines;
    static MachineList block59Machines;
    static MachineList block57Machines;
    

    private GlobalConfig() {
        // Creating machine lists for each block
        block55Machines = new MachineList();
        block59Machines = new MachineList();
        block57Machines = new MachineList();
    }

    public static synchronized MachineData findMachine(String block, String machineName) {
        MachineList machineList;
        switch (block) {
            case "55":
                machineList = block55Machines;
                break;
            case "59":
                machineList = block59Machines;
                break;
            case "57":
                machineList = block57Machines;
                break;
            default:
                return null;
        }
        for (MachineData machine : machineList.getMachineList()) {
            if (machine.getName().equals(machineName)) {
                return machine;
            }
        }
        return null;
    }

    public static synchronized GlobalConfig getInstance() {
        if (instance == null) {
            synchronized (GlobalConfig.class) {
                if (instance == null) {
                    instance = new GlobalConfig();
                }
            }
        }
        return instance;
    }

    public Properties getConfig() {
        return config;
    }

    public void setConfig(Properties config) {
        this.config = config;
    }
}

