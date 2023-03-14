package guru.learningjournal.kafka.examples.kafka;


import guru.learningjournal.kafka.examples.serde.JsonSerializer;
import guru.learningjournal.kafka.examples.types.PosInvoice;
import javafx.geometry.Pos;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class Dispatcher implements Runnable{

    private static Logger logger = LogManager.getLogger();
    private PosInvoice invoce;
    private String topicName;
    private KafkaProducer<String, PosInvoice> producer;


    public Dispatcher(PosInvoice invoce, String topicName, KafkaProducer<String, PosInvoice> producer) {
        this.invoce = invoce;
        this.topicName = topicName;
        this.producer = producer;
    }


    @Override
    public void run() {
        logger.info("Start processing...");
        producer.send(new ProducerRecord<>(topicName, invoce.getStoreID(), invoce));
    }
}
