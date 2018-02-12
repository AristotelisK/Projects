package execution;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.LinkedBlockingDeque;

public class MyConsumer extends Thread {

    private LinkedBlockingDeque<String> queue;

    public MyConsumer(LinkedBlockingDeque<String> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            while (true) {
                String m = queue.take();
                System.out.println("consumed message: " + m);

                String topic        = "mega";
                String content      = m;
                int qos             = 2;
                String broker       = "tcp://127.0.0.1:1883";
                String clientId     = "JavaSample";
                MemoryPersistence persistence = new MemoryPersistence();

                try {
                    MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
                    MqttConnectOptions connOpts = new MqttConnectOptions();
                    connOpts.setCleanSession(true);
                    sampleClient.connect(connOpts);
                    MqttMessage message = new MqttMessage(content.getBytes());
                    message.setQos(qos);
                    sampleClient.publish(topic, message);
                    sampleClient.disconnect();
                } catch(MqttException me) {
                    System.out.println("reason "+me.getReasonCode());
                    System.out.println("msg "+me.getMessage());
                    System.out.println("loc "+me.getLocalizedMessage());
                    System.out.println("cause "+me.getCause());
                    System.out.println("excep "+me);
                    me.printStackTrace();
                }

                if (MyProducerClassification.enabled == true) {
                    try {
                        int T;

                        synchronized (MyExecutor.class) {
                            T = MyExecutor.period;
                        }


                        Thread.sleep(T); // Sleep for time specified
                    } catch (InterruptedException e) {
                        System.out.println("********** INT PRODUCER CLASSFICIATION" );
                        break;
                    }
                }
            }
        } catch (InterruptedException e) {
            System.out.println("********** INT CONSUMER " );
            return;
        }
    }
}
