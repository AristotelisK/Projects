package execution;

import entropy.EntropyCalculator;
import entropy.LabelCalculator;
import structure.StructEntropy;
import structure.StructExperimentPool;
import structure.StructTraining;
import structure.StructTrainingPool;

import java.io.*;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;

// https://stackoverflow.com/questions/1844688/how-to-read-all-files-in-a-folder-from-java

public class MyProducerClassification extends Thread {

    private LinkedBlockingDeque<String> queue;
    public static boolean enabled = false;
    public static boolean completed = false;

    public MyProducerClassification(LinkedBlockingDeque<String> queue) {
        this.queue = queue;
    }


    @Override
    public void run() {
        super.run();

        EntropyCalculator eCalculator = new EntropyCalculator();
        LabelCalculator lCalculator = new LabelCalculator();
        StructTrainingPool trainingPool = new StructTrainingPool();

        String csvFile = "./data/Training Set.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        while (true) {

            boolean started = false;
            synchronized (MyProducerClassification.class) {
                started = enabled;
            }
            if (started && !completed) {
                System.out.println("puf");
                try {
                    br = new BufferedReader(new FileReader(csvFile));

                    while ((line = br.readLine()) != null && enabled) {

                        if (line.startsWith("LabelClassName")) {
                            continue;
                        }

                        String[] line_trainingset = line.split(cvsSplitBy);

                        StructTraining t = new StructTraining();
                        t.label = line_trainingset[0];

                        for (int i = 1; i <= 14; i++) {
                            t.array[i - 1] = Double.parseDouble(line_trainingset[i]);
                        }

                        trainingPool.pool.add(t);
                    }


                    trainingPool.print();

                    System.out.println("Note: classification completed.");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                System.out.println("Note: training set load completed.");


                File dir = new File("./data");

                for (File fileEntry : dir.listFiles()) {
                    if (fileEntry.getName().startsWith("Training")) {
                        continue;
                    }

                    System.out.println("Importing ... " + fileEntry.getName());

                    StructExperimentPool experimentPool = new StructExperimentPool();

                    try {
                        br = new BufferedReader(new FileReader(fileEntry));

                        while ((line = br.readLine()) != null && enabled) {

                            if (line.startsWith("AF3")) {
                                continue;
                            }

                            String[] line_experiment = line.split(cvsSplitBy);

                            StructEntropy t = new StructEntropy();

                            for (int i = 1; i <= 14; i++) {
                                t.array[i-1] = Double.parseDouble(line_experiment[i-1]);
                            }

                            experimentPool.pool.add(t);
                        }

                        System.out.println("Lines: " + experimentPool.pool.size());

                        if (experimentPool.pool.isEmpty()) {
                            continue;
                        }

                        StructEntropy e = eCalculator.calculate(experimentPool);

                        System.out.println(e);

                        String correctLabel;

                        if (fileEntry.getName().contains("Opened")) {
                            correctLabel = "EyesOpened";
                        } else {
                            correctLabel = "EyesClosed";
                        }

                        String label = lCalculator.calculate(e, trainingPool, 11, correctLabel); // K=11

                        StructTraining t = new StructTraining();
                        t.label = label;

                        for (int i=0;i<14;i++) {
                            t.array[i] = e.array[i];
                        }

                        if (label.equals("EyesOpened")) {
                            queue.add("on");
                        } else {
                            queue.add("off");
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (br != null) {
                            try {
                                br.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                // ----------------------------------------------
                System.out.println("Note: classification completed.");

                double percent = (lCalculator.successes + 0.0)/(lCalculator.successes + lCalculator.faults + 0.0);

                System.out.println("percent: " + percent);

                completed = true;
            }
        }
    }
}
