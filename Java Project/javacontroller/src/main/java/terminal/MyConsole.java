package terminal;

import java.util.Scanner;

public class MyConsole {
    private Scanner scanner = new Scanner(System.in);

    public void printMenu() {
        System.out.println("----------------------------------------------------------");
        System.out.println("                    C O N T R O L L E R ");
        System.out.println("----------------------------------------------------------\n\n");

        System.out.println("        1. audio play");
        System.out.println("        2. audio stop");
        System.out.println("        3. torch on");
        System.out.println("        4. torch off");
        System.out.println("        5. set number of sound");
        System.out.println("        6. set period ");
        System.out.println("        7. start/stop");
        System.out.println("        8. start/stop classification");
        System.out.println("        9. Help (print menu)");
        System.out.println("        0. Exit");
    }

    public String askUser() {
        String cmd;
        System.out.print(" Type your choice:");
        cmd = scanner.nextLine();

        if (cmd != null && cmd.equals("9")) {
            printMenu();
        }
        return cmd;
    }
}
