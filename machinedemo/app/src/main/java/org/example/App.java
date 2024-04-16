package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;

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

        // Creating machine lists for each block
        MachineList block55Machines = new MachineList();
        MachineList block59Machines = new MachineList();

        // Adding test machines to each list
        block55Machines.addMachine(new MachineData("Washer 1", "Available", "55", "2022-01-01", "block-55"));
        block55Machines.addMachine(new MachineData("Dryer 1", "Available", "55", "2022-01-01", "block-55"));

        block59Machines.addMachine(new MachineData("Washer 2", "Available", "59", "2022-01-01", "block-59"));
        block59Machines.addMachine(new MachineData("Dryer 2", "Available", "59", "2022-01-01", "block-59"));

        // Creating and starting threads for updating machine data
        Thread block55Thread = new Thread(new MachineDataUpdater(block55Machines, config, "block-55"));
        Thread block59Thread = new Thread(new MachineDataUpdater(block59Machines, config, "block-59"));

        block55Thread.start();
        block59Thread.start();
    }
}
