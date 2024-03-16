package ch.wiss.javasensor;
 
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;
 
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
 
/**
* Hello world!
*
*/
public class App
{
    public static void main( String[] args )
    {
        String server = Optional.ofNullable(System.getenv("SERVER")).orElse("localhost");
        String topic = Optional.ofNullable(System.getenv("TOPIC")).orElse("none");
 
        System.out.println( "Hello World!" );
 
        String broker = "tcp://"+server+":1883";
        String pubid = "demo_publisher";
        String subid1 = "rooms/"+topic;
        int subqos = 1;
        int pubQos = 1;
        int i = 0;
        boolean isRunning = true;
 
 
        try {
            MqttClient subscriber1 = new MqttClient(broker, subid1);
            MqttClient publisher = new MqttClient(broker, pubid);
            MqttConnectOptions options = new MqttConnectOptions();
            publisher.connect(options);
            subscriber1.connect(options);
 
            if (subscriber1.isConnected()) {
                subscriber1.subscribe("help", subqos);
            }
            while (isRunning) {
                double sinval = Math.sin(i/10.0);
                String strmsg = Double.toString(sinval);
                try {
                    i = (i + 1)%(314*2);
                    MqttMessage message = new MqttMessage(strmsg.getBytes());
                    message.setQos(pubQos);
                    publisher.publish(subid1, message);
                    Thread.sleep(1000);
                }
                catch(Exception ex) {
                    System.out.println("Error: ");
                    //ex.printStackTrace();
                }
            }
            publisher.disconnect();
            subscriber1.disconnect();
            publisher.close();
            subscriber1.close();
 
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