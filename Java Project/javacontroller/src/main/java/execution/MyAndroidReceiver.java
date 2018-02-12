package execution;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MyAndroidReceiver extends Thread implements MqttCallback {

    private MqttClient sampleClient = null;
    private MemoryPersistence persistence = new MemoryPersistence();

    @Override
    public void run() {
        super.run();


        try {
            String topic        = "antant";
            int qos             = 2;
            String broker       = "tcp://127.0.0.1:1883";
            String clientId     = "JavaSample2";

            sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            sampleClient.setCallback(this);
            sampleClient.connect(connOpts);
            sampleClient.subscribe(topic, qos);
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }

    public void bye() {
        try {
            sampleClient.disconnect();
            sampleClient.close();
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }


    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        final String m = new String(message.getPayload());

        if (m.startsWith("changeperiod")) {
            String token = null;
            if (m.split(" ").length > 1) {
                token = m.split(" ")[1];

                try {
                    int T = Integer.parseInt(token);

                    synchronized (MyExecutor.class) {
                        MyExecutor.period = T * 1000;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            System.out.println("\n### Message:" + m + " OK \n\n");
        } else {
            System.out.println("\n### Message:" + m + " ??? \n\n");
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
