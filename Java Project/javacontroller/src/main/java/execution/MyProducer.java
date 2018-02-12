package execution;

import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;

public class MyProducer extends Thread {

    private LinkedBlockingDeque<String> queue;
    public static boolean enabled = false;

    public MyProducer(LinkedBlockingDeque<String> queue) {
        this.queue = queue;
    }


    @Override
    public void run() {
        super.run();

        Random r = new Random();

        String[] commandList = new String[]{"audioplay", "audiostop", "torchon", "torchoff"};

        while (true) {
        //System.out.println("Tick! ");
            if (enabled) {
                String cmd = commandList[r.nextInt(4)];

                queue.add(cmd);
            }

            try {
                int T;

                synchronized (MyExecutor.class) {
                    T = MyExecutor.period;
                }


                Thread.sleep(T); // Sleep until the next random Command
            } catch (InterruptedException e) {
                System.out.println("********** INT PRODUCER " );
                break;
            }

        }
    }
}
