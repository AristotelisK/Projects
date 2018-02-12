package structure;

import java.util.Arrays;

public class StructTraining extends StructEntropy {
    public String label;

    @Override
    public String toString() {
        return  label + " {" +
                "array=" + Arrays.toString(array) +
                '}';
    }
}
