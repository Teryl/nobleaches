package org.example;

import org.apache.kafka.common.serialization.Deserializer;

import com.google.gson.Gson;

public class KafkaBookingRequestSerializer implements Deserializer<KafkaBookingRequest> {
    private static final Gson gson = new Gson();
    
    public byte[] serialize(String topic, KafkaBookingRequest data) {
        String json = gson.toJson(data);
        return json.getBytes();
    }
    
    @Override
    public KafkaBookingRequest deserialize(String topic, byte[] data) {
        String json = new String(data);
        return gson.fromJson(json, KafkaBookingRequest.class);
    }
    
}
