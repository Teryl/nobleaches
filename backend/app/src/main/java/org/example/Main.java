package org.example;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.*;
import com.google.cloud.firestore.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.protocol.types.Field.Str;
import org.apache.kafka.common.serialization.*;

import java.util.concurrent.*;
import java.nio.file.*;
import java.io.*;
import com.google.api.core.ApiFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.*;

import java.time.Duration;

import org.example.GlobalConfig;

public class Main {
    private static final String TOPIC1 = GlobalConfig.TOPIC1;
    private static final String TOPIC2 = GlobalConfig.TOPIC2;
    private static final String TOPIC3 = GlobalConfig.TOPIC3;
    private static final String DATABASE_URL = GlobalConfig.DATABASE_URL;
    private static Properties config;

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
        try {
            // Load the service account key JSON file
            FileInputStream serviceAccount = new FileInputStream("admin-creds.json");

            // Initialize the app with a service account, granting admin privileges
            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(DATABASE_URL)
                .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            config = readConfig("client.properties");
            config.put(ConsumerConfig.GROUP_ID_CONFIG, "backend-server");
            config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
            config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.example.MachineListSerializer");

            config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
            config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.example.KafkaBookingRequestSerializer");
        } catch (IOException e) {
            e.printStackTrace();
        }
        GlobalConfig.getInstance().setConfig(config);

        // Get a reference to the database
        DatabaseReference ref = GlobalConfig.getInstance().ref;
        DatabaseReference bookingRef = GlobalConfig.getInstance().bookingRef;

        // Initialize Kafka consumer
        Properties config = GlobalConfig.getInstance().getConfig();

        // Create a ScheduledExecutorService
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

        // Schedule the polling and updating tasks for the first topic
        scheduler.scheduleAtFixedRate(() -> {
            try {
                pollTopic(TOPIC1, config);
                updateFirebase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 2, TimeUnit.SECONDS);

        // Schedule the polling and updating tasks for the second topic
        scheduler.scheduleAtFixedRate(() -> {
            try {
                pollTopic(TOPIC2, config);
                updateFirebase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 2, TimeUnit.SECONDS);

        // Schedule the polling and updating tasks for the third topic
        scheduler.scheduleAtFixedRate(() -> {
            try {
                pollTopic(TOPIC3, config);
                updateFirebase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 2, TimeUnit.SECONDS);

        // Listen for booking requests in Firebase
        listenForBookingRequests(bookingRef);
    }

    private static void pollTopic(String topic, Properties config) {
        // Create a new Kafka consumer
        KafkaConsumer<String, MachineList> consumer = new KafkaConsumer<>(config);
    
        // Subscribe to the topic
        consumer.subscribe(Collections.singletonList(topic));
    
        // Poll the topic
        ConsumerRecords<String, MachineList> records = consumer.poll(Duration.ofMillis(1000));
        for (ConsumerRecord<String, MachineList> record : records) {
            for (MachineData machineData : record.value().getMachineList()) {
                String key = topic + "-" + machineData.getName();
                machineDataMap.put(key, machineData);
            }
        }
    
        consumer.close();
    }

    private static Map<String, MachineData> machineDataMap = new HashMap<>();

    private static void updateFirebase() {
        // Get a reference to the database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Machines");
        
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
                        // System.out.println("Data saved successfully." + localMachineList);
                    }
                }
            });
        }
    }

    private static void listenForBookingRequests(DatabaseReference ref) {
        DatabaseReference bookinRef = FirebaseDatabase.getInstance().getReference("BookingRequests");
        bookinRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String json = snapshot.getValue(String.class);
                    BookingRequest bookingRequest = GlobalConfig.getInstance().gson.fromJson(json, BookingRequest.class);
                    System.out.println(bookingRequest.getBookingUUID());
                    bookingRequest.setProcessedOn();
                    if (bookingRequest != null) {
                        // Schedule message posting to Kafka topic
                        scheduleMessageToKafka(bookingRequest);
                        // Add booking request to Firestore
                        addBookingToFirestore(bookingRequest);
                        // Delete the request from Firebase Realtime Database
                        dataSnapshot.getRef().removeValue((databaseError, databaseReference) -> {
                            if (databaseError != null) {
                                System.out.println("Data could not be deleted " + databaseError.getMessage());
                            } else {
                                System.out.println("Data deleted from realtime db successfully.");
                            }
                        });
                    }
                }
            }

            private void addBookingToFirestore(BookingRequest bookingRequest) {
                Firestore db = FirestoreClient.getFirestore();

                Map<String, Object> data = new HashMap<>();
                data.put("bookingUUID", bookingRequest.getBookingUUID());
                data.put("userUUID", bookingRequest.getUserUUID());
                data.put("machine", bookingRequest.getMachine());
                data.put("requestTime", bookingRequest.getRequestTime());
                data.put("processedOn", bookingRequest.getProcessedOn());

                db.collection(GlobalConfig.FIRESTORE_COLLECTION)
                    .document(bookingRequest.getBookingUUID())
                    .set(data);
                System.out.println("Booking added to Firestore");
            }


            private void scheduleMessageToKafka(BookingRequest bookingRequest) {
                // Get the current time
                long currentTime = System.currentTimeMillis();
            
                // Convert the Time object to a Date object
                Date requestDate = new Date(bookingRequest.getRequestTime().getTime());

                // Calculate the delay until the booking request time
                long delay = TimeUnit.SECONDS.toMillis(15);
            
                // Create a new scheduled executor service
                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

                System.out.println("Scheduling message to Kafka");
            
                // Schedule the Kafka message
                executor.schedule(() -> {
                    // Create a new Kafka producer
                    KafkaProducer<String, KafkaBookingRequest> producer = new KafkaProducer<>(GlobalConfig.getInstance().getConfig());
                    
                    // Create custom safe object
                    KafkaBookingRequest kafkaBookingRequest = new KafkaBookingRequest(bookingRequest.getBookingUUID(), bookingRequest.getMachine().getBlock(), bookingRequest.getMachine().getName());

                    // Create a new producer record
                    ProducerRecord<String, KafkaBookingRequest> record = new ProducerRecord<>(GlobalConfig.KAFKA_TOPIC, kafkaBookingRequest);
            
                    // Send the record to the Kafka topic
                    producer.send(record);
                    System.out.println("Message sent to Kafka");
            
                    // Close the producer
                    producer.close();
                }, delay, TimeUnit.MILLISECONDS);
            
                // Shutdown the executor
                executor.shutdown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}