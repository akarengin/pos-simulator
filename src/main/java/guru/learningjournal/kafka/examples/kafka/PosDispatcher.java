package guru.learningjournal.kafka.examples.kafka;

import org.apache.kafka.common.serialization.StringSerializer;
import guru.learningjournal.kafka.examples.datagenerator.InvoiceGenerator;
import guru.learningjournal.kafka.examples.serde.JsonSerializer;
import guru.learningjournal.kafka.examples.types.PosInvoice;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class PosDispatcher {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        Properties props = new Properties();

        props.put(ProducerConfig.CLIENT_ID_CONFIG, AppConfigs.applicationID);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        KafkaProducer<String, PosInvoice> producer = new KafkaProducer<>(props);
        Thread[] dispatchers = new Thread[3];
        for (int i = 0; i < 3; i++) {
            dispatchers[i] = new Thread(new Dispatcher(InvoiceGenerator.getInstance().getNextInvoice(), AppConfigs.topicName, producer));
            dispatchers[i].start();
        }

        for (Thread t : dispatchers) {
            try {
                t.join();
            } catch (InterruptedException e) {
                logger.info("Main Thread Interrupted");
            } finally {
                logger.info("Finished PosDispatcher");
            }
        }
    }
}
