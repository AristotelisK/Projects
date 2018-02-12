package structure;

import java.sql.Struct;
import java.util.LinkedList;
import java.util.List;

public class StructTrainingPool {
    public List<StructTraining> pool = new LinkedList<>();

    public void print() {
        for (StructTraining t : pool) {
            System.out.println(t);
        }
    }
}
