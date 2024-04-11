package org.example;
import org.apache.kafka.common.serialization.Deserializer;

import com.google.gson.Gson;

public class MachineListSerializer implements Deserializer<MachineList> {
    private static final Gson gson = new Gson();

        public static String serialize(MachineList machineList) {
            return gson.toJson(machineList);
        }
        
        @Override
        public MachineList deserialize(String topic, byte[] data) {
            String json = new String(data);
            return gson.fromJson(json, MachineList.class);
        }
}
