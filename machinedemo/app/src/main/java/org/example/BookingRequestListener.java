package org.example;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BookingRequestListener implements Runnable{
    private Properties config;
    private String kafkaTopic;

    public BookingRequestListener(Properties config, String kafkaTopic) {
        this.config = config;
        this.kafkaTopic = kafkaTopic;
    }

    @Override
    public void run() {
        Consumer<String, KafkaBookingRequest> consumer = new KafkaConsumer<>(config);
        consumer.subscribe(Collections.singletonList(kafkaTopic));

        while (true) {
            ConsumerRecords<String, KafkaBookingRequest> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, KafkaBookingRequest> record : records) {
                // Process each record
                processBookingRequest(record.value());
            }
        }
    }

    private void processBookingRequest(KafkaBookingRequest bookingRequest) {
        // Extract necessary information from the booking request
        String block = bookingRequest.getBlock();
        String machineName = bookingRequest.getName();

        // Logic to find and update the machine's status
        MachineData machineToUpdate = GlobalConfig.findMachine(block, machineName);
        if (machineToUpdate != null) {
            // Set the status to "Booked" for the next 2 minutes
            machineToUpdate.setStatus("Booked");
            long delay = TimeUnit.SECONDS.toMillis(30);
            System.out.println("Machine " + machineName + " in block " + block + " has been booked for "+ delay + " seconds.");
            try {
                Thread.sleep(delay); // Booked for 30 secs
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // After 2 minutes, change the status back to "Available"
            machineToUpdate.setStatus("Available");
            System.out.println("Machine " + machineName + " in block " + block + " is now available.");
        }
    }
}
