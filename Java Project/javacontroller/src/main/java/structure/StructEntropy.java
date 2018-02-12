package structure;

import java.util.Arrays;

public class StructEntropy {
    public double [] array = new double[14];

    public static double distance(StructEntropy a, StructEntropy b) {
        double distance = 0;

        for (int i=0;i<14;i++) {
            distance = distance + Math.pow(a.array[i] - b.array[i],2);
        }

        return Math.sqrt(distance);
    }


    @Override
    public String toString() {
        return "StructEntropy{" +
                "array=" + Arrays.toString(array) +
                '}';
    }
}
