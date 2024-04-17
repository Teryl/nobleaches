package org.example;

import java.util.concurrent.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.time.Duration;

import org.apache.kafka.clients.producer.*;

public class MachineDataUpdater implements Runnable {
    private MachineList machineList;
    private Properties config;
    private String kafkaTopic;
    private Map<String, ScheduledFuture<?>> taskMap;

    public MachineDataUpdater(MachineList machineList, Properties config, String kafkaTopic) {
        this.machineList = machineList;
        this.config = config;
        this.kafkaTopic = kafkaTopic;
        this.taskMap = new HashMap<>();
    }

    @Override
    public void run() {
        Producer<String, String> producer = new KafkaProducer<>(config);
        while (true) {
            updateMachineStatus(producer);
            try {
                Thread.sleep(Duration.ofSeconds(10).toMillis()); // Update every 30 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateMachineStatus(Producer<String, String> producer) {
        // Logic to update machine status
        for (MachineData machine : machineList.getMachineList()) {
            // Example logic to update status
            String currentStatus = machine.getStatus();
            if ("Booked".equals(currentStatus)) {
                continue; // Skip updating status for booked machines
            } else if ("In Use".equals(currentStatus)) {
                // Randomly update status for available machines and update lastUsed time
                machine.lastUsed = new Date().toString();
                String newStatus = getRandomStatus();
                machine.setStatus(newStatus);
            } else {
                // Randomly update status for available machines
                String newStatus = getRandomStatus();
                machine.setStatus(newStatus);
            }

            // Serialize machine list to JSON
            String json = MachineListSerializer.serialize(machineList);

            //Generate UUID
            String key = UUID.randomUUID().toString();

            // Print what is being sent to the topic
            System.out.println("!--!");

            // Produce message to Kafka topic
            producer.send(new ProducerRecord<>(kafkaTopic, key, json));
        }
    }

    private String getRandomStatus() {
        String[] statuses = {"Available", "In Use"};
        int randomIndex = (int) (Math.random() * statuses.length);
        return statuses[randomIndex];
    }
}
