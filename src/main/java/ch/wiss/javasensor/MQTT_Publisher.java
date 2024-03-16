package ch.wiss.javasensor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Hello world!
 *
 */
public class MQTT_Publisher {
    public static void main( String[] args ) {

        System.out.println( "Hello World!" );

        String server = Optional.ofNullable(System.getenv("SERVER")).orElse("localhost");
        String topic = Optional.ofNullable(System.getenv("TOPIC")).orElse("topic");
        String pubid = Optional.ofNullable(System.getenv("PUBID")).orElse("sensor");
        String subid = Optional.ofNullable(System.getenv("SUBID")).orElse("subscriber");


        System.out.println(server.toString());
        System.out.println(topic.toString());
        System.out.println(pubid.toString());
        System.out.println(subid.toString());

        String broker = "tcp://"+ server + ":1883";
        int subqos = 1;
        int pubQos = 1;
        int i = 0;
        boolean isRunning = true;

        System.out.println(broker.toString());

        try {
            MqttClient subscriber = new MqttClient(broker, subid);
            MqttClient publisher = new MqttClient(broker, pubid);
            publisher.connect();
            subscriber.connect();

            if (subscriber.isConnected()) {
                subscriber.subscribe(topic, subqos);
            }

            while (isRunning) {
                double sinval = Math.sin(i/10.0);
                String strmsg = Double.toString(sinval);
                try {
                    i = (i + 1)%(314*2);
                    MqttMessage message = new MqttMessage(strmsg.getBytes());
                    message.setQos(pubQos);
                    publisher.publish(topic, message);
                    Thread.sleep(1000);
                }
                catch(Exception ex) {
                    System.out.println("Error: ");
                    ex.printStackTrace();
                }
            }
            publisher.disconnect();
            subscriber.disconnect();
            publisher.close();
            subscriber.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public static String topicReader(int topicNumber) {
        try {
            System.out.println(String.format("Wie sollte das %d Topic heissen (default: topic%d):", topicNumber, topicNumber));
            // Enter data using BufferReader
            BufferedReader topicReader3 = new BufferedReader(
            new InputStreamReader(System.in));
        
            // Reading data using readLine
            String topic = topicReader3.readLine();

            if (topic.length() == 0) {
                return String.format("topic%d", topicNumber);
            }
            return topic;
        }
        catch (Exception ex) {
            System.out.println("Something went wrong: ");
            ex.printStackTrace();
            return String.format("topic%d", topicNumber);
        }
    }
}