package structure;

import java.util.LinkedList;
import java.util.List;

public class StructExperimentPool {
    public List<StructEntropy> pool = new LinkedList<>();

    public void print() {
        for (StructEntropy t : pool) {
            System.out.println(t);
        }
    }
}
