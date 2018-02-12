import checking.MyValidator;
import execution.*;
import terminal.MyConsole;

import java.util.concurrent.LinkedBlockingDeque;

public class JavaController {
    public static void main(String [] args) {

        LinkedBlockingDeque<String> queue = new LinkedBlockingDeque<>();
        MyConsole console = new MyConsole();
        MyValidator validator = new MyValidator();
        MyExecutor executor = new MyExecutor(queue);
        MyConsumer myConsumer1 = new MyConsumer(queue);
//        MyConsumer myConsumer2 = new MyConsumer(queue);
//        MyConsumer myConsumer3 = new MyConsumer(queue);
        MyProducer myProducer4 = new MyProducer(queue);
        MyProducerClassification myProducerC = new MyProducerClassification(queue);
        MyAndroidReceiver myAndroidReceiver= new MyAndroidReceiver();


        myConsumer1.start();
//        myConsumer2.start();
//        myConsumer3.start();
        myProducer4.start();
        myAndroidReceiver.start();
        myProducerC.start();

        console.printMenu();

        String cmd;
        do {
            cmd = console.askUser();
            boolean okay = validator.validate(cmd);
            if (okay) {
                executor.execute(cmd);
            } else {
                System.out.println("Unknown command!!!");
            }
        } while (cmd == null || !cmd.equals("0") );

        myConsumer1.interrupt();
//        myConsumer2.interrupt();
//        myConsumer3.interrupt();
        myProducer4.interrupt();
        myAndroidReceiver.interrupt();
        myProducerC.interrupt();
        myAndroidReceiver.bye();

        System.out.println("The Program is Terminated");
    }
}
