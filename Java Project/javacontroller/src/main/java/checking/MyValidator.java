package checking;

public class MyValidator {
    public boolean validate(String command) {
        if (command == null) {
            return false;
        }
        // Validate the number of the Command for the Controller
        if (command.equals("1") ||
                command.equals("2") ||
                command.equals("3") ||
                command.equals("4") ||
                command.equals("5") ||
                command.equals("6") ||
                command.equals("7") ||
                command.equals("8") ||
                command.equals("9") ||
                command.equals("0")) {
            return true;
        } else {
            return false;
        }
    }
}
