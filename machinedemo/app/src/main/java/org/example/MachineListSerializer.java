package org.example;

import com.google.gson.Gson;

public class MachineListSerializer {
    private static final Gson gson = new Gson();
        
        public static String serialize(MachineList machineList) {
            return gson.toJson(machineList);
        }
    
        public static MachineList deserialize(String json) {
            return gson.fromJson(json, MachineList.class);
        }
}
