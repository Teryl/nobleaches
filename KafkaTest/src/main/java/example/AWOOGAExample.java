package example;

import java.lang.System;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.time.*;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.*;

public class AWOOGAExample {
  public static Properties readConfig(final String configFile) throws IOException {
    // reads the client configuration from client.properties
    // and returns it as a Properties object
    if (!Files.exists(Paths.get(configFile))) {
      throw new IOException(configFile + " not found.");
    }

    final Properties config = new Properties();
    try (InputStream inputStream = new FileInputStream(configFile)) {
      config.load(inputStream);
    }

    return config;
  }

  public static void main(String[] args) {
    try {
      // producer and consumer code here
      String topic = "status";
      final Properties config = readConfig("client.properties");
      
      // sets the message serializers
      config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
      config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
      
      // creates a new producer instance and sends a sample message to the topic
      String key = "key";
      String value = "value";
      Producer<String, String> producer = new KafkaProducer<>(config);
      producer.send(new ProducerRecord<>(topic, key, value));
      System.out.println(
        String.format(
          "Produced message to topic %s: key = %s value = %s", topic, key, value
        )
      );
      
      // closes the producer connection
      producer.close();

    // sets the group ID, offset and message deserializers
    config.put(ConsumerConfig.GROUP_ID_CONFIG, "java-group-1");
    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

    // creates a new consumer instance and subscribes to messages from the topic
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(config);
    consumer.subscribe(Arrays.asList(topic));

    while (true) {
      // polls the consumer for new messages and prints them
      ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
      for (ConsumerRecord<String, String> record : records) {
        System.out.println(
          String.format(
            "Consumed message from topic %s: key = %s value = %s", topic, record.key(), record.value()
          )
        );
      }
    }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}