package org.example;

import org.apache.kafka.common.serialization.Serializer;

import com.google.gson.Gson;

public class KafkaBookingRequestSerializer implements Serializer<KafkaBookingRequest> {
    private static final Gson gson = new Gson();
    
    @Override
    public byte[] serialize(String topic, KafkaBookingRequest data) {
        String json = gson.toJson(data);
        return json.getBytes();
    }
    
    public KafkaBookingRequest deserialize(String topic, byte[] data) {
        String json = new String(data);
        return gson.fromJson(json, KafkaBookingRequest.class);
    }
    
}
