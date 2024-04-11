package org.example;


import java.lang.System;
import java.util.*;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.clients.consumer.*;

class MachineDataUpdater implements Runnable {
    private MachineList machineList;
    private Properties config;
    private String kafkaTopic;

    public MachineDataUpdater(MachineList machineList, Properties config, String kafkaTopic) {
        this.machineList = machineList;
        this.config = config;
        this.kafkaTopic = kafkaTopic;
    }

    @Override
    public void run() {
        Producer<String, String> producer = new KafkaProducer<>(config);
        while (true) {
            updateMachineStatus(producer);
            try {
                Thread.sleep(10000); // Update every 10 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateMachineStatus(Producer<String, String> producer) {
        // Logic to update machine status
        for (MachineData machine : machineList.getMachineList()) {
            // Example logic to update status
            // Randomly select a status
            String[] statuses = {"Available", "Booked", "In Use", "Under Maintenance"};
            int randomIndex = (int) (Math.random() * statuses.length);
            String newStatus = statuses[randomIndex];
            machine.setStatus(newStatus);
        }

        // Serialize machine list to JSON
        String json = MachineListSerializer.serialize(machineList);

        //Generate UUID
        String key = UUID.randomUUID().toString();

        // Print what is being sent to the topic
        System.out.println("Sending message with key " + key + " to topic " + kafkaTopic + ": " + json);
        System.out.println();

        // Produce message to Kafka topic
        producer.send(new ProducerRecord<>(kafkaTopic, key, json));
    }
}
