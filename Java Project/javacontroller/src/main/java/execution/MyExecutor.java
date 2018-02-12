package execution;

import java.util.Scanner;
import java.util.concurrent.LinkedBlockingDeque;

public class MyExecutor {
    private Scanner scanner = new Scanner(System.in);
    private int number_of_sound = 1;
    public static int period = 2000;
    private LinkedBlockingDeque<String> queue;

    public MyExecutor(LinkedBlockingDeque<String> queue) {
        this.queue = queue;
    }


    public void execute(String cmd) {
        if (cmd.equals("1")) { // audioplay
            System.out.println("audioplay " + number_of_sound);
            String m = "audioplay " + number_of_sound;
            queue.add(m);
        }

        if (cmd.equals("2")) { // audiostop
            System.out.println("audiostop");
            String m = "audiostop ";
            queue.add(m);
        }

        if (cmd.equals("3")) { // torchon
            System.out.println("torchon");
            String m = "torchon";
            queue.add(m);
        }

        if (cmd.equals("4")) { // torchoff
            System.out.println("torchoff");
            String m = "torchoff ";
            queue.add(m);
        }

        if (cmd.equals("7")) { // randomon
            if (MyProducer.enabled == false) {
                if (MyProducerClassification.enabled) {
                    System.out.println("You cannot start random mode if classification mode is running");
                } else {
                    MyProducer.enabled = true;
                    System.out.println("random mode on");
                }
            } else {
                MyProducer.enabled = false;
                System.out.println("random mode off");
            }
        }

        if (cmd.equals("8")) { // randomon
            if (MyProducerClassification.enabled == false) {
                if (MyProducer.enabled) {
                    System.out.println("You cannot start classification mode if random mode is running");
                } else {
                    synchronized (MyProducerClassification.class) {
                        MyProducerClassification.enabled = true;
                        MyProducerClassification.completed = false;
                    }
                    System.out.println("classification mode on");
                }
            } else {
                MyProducerClassification.enabled = false;
                System.out.println("classification mode off");
            }
        }


        if (cmd.equals("5")) { // sound number
            System.out.println("Current sound number is (1:cat 2:cow 3:puppy): " + number_of_sound);
            System.out.print("Type the new value: ");

            try {
                int temp = scanner.nextInt();
                if (temp == 1 || temp == 2 || temp == 3) {
                    number_of_sound = temp;
                } else {
                    System.out.println("Error. If you need help press 9");
                }
            } catch (Exception ex) {
                System.out.println("Error ");
            }
        }
        if (cmd.equals("6")) { // period
            synchronized (MyExecutor.class) {
                System.out.println("Current period is: " + period);
            }

            System.out.print("Type the new value: ");

            try {
                int T = scanner.nextInt();

                synchronized (MyExecutor.class) {
                    period = T;
                }
            } catch (Exception ex) {
                System.out.println("Error ");
            }
        }
    }
}
