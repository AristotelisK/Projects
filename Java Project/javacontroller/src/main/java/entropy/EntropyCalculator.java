package entropy;

import structure.StructEntropy;
import structure.StructExperimentPool;

public class EntropyCalculator {

    public StructEntropy calculate(StructExperimentPool experimentPool) {
        StructEntropy e = new StructEntropy();

        int maxy = experimentPool.pool.size();

        for (int x =0;x<14;x++) {
            double[] column = new double[maxy];

            for (int y=0;y<maxy;y++) {
                double value = experimentPool.pool.get(y).array[x];

                column[y] = value;
            }

            double entropy = Entropy.calculateEntropy(column);
            e.array[x] = entropy;
        }

        return e;
    }

}
