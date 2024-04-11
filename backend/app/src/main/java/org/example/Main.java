package org.example;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.*;
import java.util.function.Function;

import java.nio.file.Files;
import java.util.*;
import java.util.function.Consumer;

import javax.xml.crypto.Data;
import java.time.Duration;
import java.time.Duration;
import java.util.stream.Collectors;
import org.example.MachineList;

public class Main {
    private static final String TOPIC1 = "block-55";
    private static final String TOPIC2 = "block-59";
    private static final String UUID = "Hp9rUAG4kXO5NvrCjqBBHPkKqaq2";
    private static final String DATABASE_URL = "https://nobleaches-80ab2-default-rtdb.asia-southeast1.firebasedatabase.app";

    public static Properties readConfig(final String configFile) throws IOException {
        if (!Files.exists(Paths.get(configFile))) {
            throw new IOException(configFile + " not found.");
        }

        final Properties config = new Properties();
        try(InputStream inputStream = new FileInputStream(configFile)) {
            config.load(inputStream);
        }

        return config;
    }

    public static void main(String[] args) throws IOException {
        // Initialize Firebase
        FileInputStream serviceAccount = new FileInputStream("admin-creds.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(DATABASE_URL)
                .build();
        FirebaseApp.initializeApp(options);

        // Get a reference to the database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Machines");

        // Initialize Kafka consumer
        Properties props = readConfig("client.properties");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "backend-server");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.example.MachineListSerializer");

        // Create a thread pool
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Define the topics
        String topic1 = "block-55";
        String topic2 = "block-59";

        // Create a ScheduledExecutorService
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        // Schedule the polling and updating tasks for the first topic
        scheduler.scheduleAtFixedRate(() -> {
            try {
                pollTopic(topic1, props);
                updateFirebase(machineDataMap, ref);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 2, TimeUnit.SECONDS);

        // Schedule the polling and updating tasks for the second topic
        scheduler.scheduleAtFixedRate(() -> {
            try {
                pollTopic(topic2, props);
                updateFirebase(machineDataMap, ref);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    private static void pollTopic(String topic, Properties props) {
        // Create a new Kafka consumer
        KafkaConsumer<String, MachineList> consumer = new KafkaConsumer<>(props);
    
        // Subscribe to the topic
        consumer.subscribe(Collections.singletonList(topic));
    
        // Poll the topic
        ConsumerRecords<String, MachineList> records = consumer.poll(1000);
        for (ConsumerRecord<String, MachineList> record : records) {
            for (MachineData machineData : record.value().getMachineList()) {
                machineDataMap.put(machineData.getName(), machineData);
            }
        }
    
        consumer.close();
    }

    private static Map<String, MachineData> machineDataMap = new HashMap<>();

    private static MachineList processRecords(ConsumerRecords<String, MachineList> records) {
        for (ConsumerRecord<String, MachineList> record : records) {
            for (MachineData machineData : record.value().getMachineList()) {
                // Update the MachineData in the map
                machineDataMap.put(machineData.getName(), machineData);
            }
        }

        // Create a new MachineList from the map values
        MachineList machineList = new MachineList();
        machineList.getMachineList().addAll(machineDataMap.values());

        return machineList;
    }

    private static void updateFirebase(Map<String, MachineData> machineDataMap, DatabaseReference ref) {
        // Synchronize on machineDataMap to prevent concurrent modifications
        synchronized (machineDataMap) {
            // Create a local copy of the machine data map values
            List<MachineData> localMachineList = new ArrayList<>(machineDataMap.values());
    
            ref.setValue(localMachineList, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        System.out.println("Data could not be saved " + databaseError.getMessage());
                    } else {
                        System.out.println("Data saved successfully." + localMachineList);
                    }
                }
            });
        }
    }
}
