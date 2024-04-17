package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.clients.consumer.*;

public class App {

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
        final Properties config = readConfig("client.properties");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "machine-demo-group");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.example.KafkaBookingRequestSerializer");
        GlobalConfig.getInstance().setConfig(config);

        // Adding test machines to each list
        GlobalConfig.block55Machines.addMachine(new MachineData("Washer 1", "Available", "55", "2022-01-01", "block-55"));
        GlobalConfig.block55Machines.addMachine(new MachineData("Dryer 1", "Available", "55", "2022-01-01", "block-55"));

        GlobalConfig.block57Machines.addMachine(new MachineData("Washer 2", "Available", "57", "2022-01-01", "block-57"));
        GlobalConfig.block57Machines.addMachine(new MachineData("Dryer 3", "Available", "57", "2022-01-01", "block-57"));
        GlobalConfig.block57Machines.addMachine(new MachineData("Dryer 3.5", "Available", "57", "2022-01-01", "block-57"));

        GlobalConfig.block59Machines.addMachine(new MachineData("Washer 3", "Available", "59", "2022-01-01", "block-59"));
        GlobalConfig.block59Machines.addMachine(new MachineData("Dryer 3", "Available", "59", "2022-01-01", "block-59"));

        // Creating and starting threads for updating machine data
        Thread block55Thread = new Thread(new MachineDataUpdater(GlobalConfig.block55Machines, config, "block-55"));
        Thread block59Thread = new Thread(new MachineDataUpdater(GlobalConfig.block59Machines, config, "block-59"));
        Thread block57Thread = new Thread(new MachineDataUpdater(GlobalConfig.block57Machines, config, "block-57"));

        Thread bookingRequestListenerThread = new Thread(new BookingRequestListener(config, "booking-requests"));

        block55Thread.start();
        block59Thread.start();
        block57Thread.start();
        bookingRequestListenerThread.start();
    }
}
