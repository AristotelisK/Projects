package entropy;

import com.sun.scenario.effect.Merge;
import structure.MergeSort;
import structure.StructEntropy;
import structure.StructLabelDistance;
import structure.StructTrainingPool;

public class LabelCalculator {
    public int successes = 0;
    public int faults = 0;

    public String calculate(StructEntropy e, StructTrainingPool pool, int k, String correctLabel) {
        StructLabelDistance [] samples = new StructLabelDistance[pool.pool.size()];

        for (int i=0;i<pool.pool.size();i++) {
            samples[i] = new StructLabelDistance();
            samples[i].label = pool.pool.get(i).label;
            samples[i].distance = StructEntropy.distance(e, pool.pool.get(i));
        }


//
//        for (StructLabelDistance s : samples) {
//            System.out.println(s.label + " " + s.distance);
//        }

        MergeSort.sort(samples);

//        System.out.println("====================================");
//        for (StructLabelDistance s : samples) {
//            System.out.println(s.label + " " + s.distance);
//        }

//        System.exit(0);

        int EyesClosed = 0;
        int EyesOpened = 0;
        double wEyesOpened = 0;
        double wEyesClosed = 0;

        for (int i=0;i<k;i++) {
            if (samples[i].label.equals("EyesOpened")) {
                EyesOpened++;
                wEyesOpened = wEyesOpened + (1/samples[i].distance);
            }

            if (samples[i].label.equals("EyesClosed")) {
                EyesClosed++;
                wEyesClosed = wEyesClosed + (1/samples[i].distance);
            }
        }

        double scoreEyesClosed = EyesClosed*wEyesClosed;
        double scoreEyesOpened = EyesOpened*wEyesOpened;

        if (scoreEyesOpened > scoreEyesClosed) {
            if (correctLabel.equals("EyesOpened")) {
                successes++;
            } else {
                faults ++;
            }
            return "EyesOpened";
        } else {
            if (correctLabel.equals("EyesClosed")) {
                successes++;
            } else {
                faults ++;
            }
            return "EyesClosed";
        }
    }
}
