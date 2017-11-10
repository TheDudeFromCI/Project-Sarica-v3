package test;

import me.ci.sarica.agent.standard.ClassificationDatabase;
import me.ci.sarica.agent.standard.ClassifierExample;
import me.ci.sarica.agent.standard.StandardNN;
import me.ci.sarica.agent.standard.StandardNNTrainingProgress;

public class BinaryDigitCountingTest
{
    public static void main(String[] args)
    {
        ClassificationDatabase database = new ClassificationDatabase(3, 3);

        // Training examples
        {
            {
                ClassifierExample example = new ClassifierExample(3, 3);
                example.setInput(0, 0f);
                example.setInput(1, 0f);
                example.setInput(2, 0f);
                example.setOutput(0, 0f);
                example.setOutput(1, 0f);
                example.setOutput(2, 1f);
                database.addExample(example);
                database.addTest(example);
            }
            {
                ClassifierExample example = new ClassifierExample(3, 3);
                example.setInput(0, 0f);
                example.setInput(1, 0f);
                example.setInput(2, 1f);
                example.setOutput(0, 0f);
                example.setOutput(1, 1f);
                example.setOutput(2, 0f);
                database.addExample(example);
                database.addTest(example);
            }
            {
                ClassifierExample example = new ClassifierExample(3, 3);
                example.setInput(0, 0f);
                example.setInput(1, 1f);
                example.setInput(2, 0f);
                example.setOutput(0, 0f);
                example.setOutput(1, 1f);
                example.setOutput(2, 1f);
                database.addExample(example);
                database.addTest(example);
            }
            {
                ClassifierExample example = new ClassifierExample(3, 3);
                example.setInput(0, 0f);
                example.setInput(1, 1f);
                example.setInput(2, 1f);
                example.setOutput(0, 1f);
                example.setOutput(1, 0f);
                example.setOutput(2, 0f);
                database.addExample(example);
                database.addTest(example);
            }
            {
                ClassifierExample example = new ClassifierExample(3, 3);
                example.setInput(0, 1f);
                example.setInput(1, 0f);
                example.setInput(2, 0f);
                example.setOutput(0, 1f);
                example.setOutput(1, 0f);
                example.setOutput(2, 1f);
                database.addExample(example);
                database.addTest(example);
            }
            {
                ClassifierExample example = new ClassifierExample(3, 3);
                example.setInput(0, 1f);
                example.setInput(1, 0f);
                example.setInput(2, 1f);
                example.setOutput(0, 1f);
                example.setOutput(1, 1f);
                example.setOutput(2, 0f);
                database.addExample(example);
                database.addTest(example);
            }
            {
                ClassifierExample example = new ClassifierExample(3, 3);
                example.setInput(0, 1f);
                example.setInput(1, 1f);
                example.setInput(2, 0f);
                example.setOutput(0, 1f);
                example.setOutput(1, 1f);
                example.setOutput(2, 1f);
                database.addExample(example);
                database.addTest(example);
            }
            {
                ClassifierExample example = new ClassifierExample(3, 3);
                example.setInput(0, 1f);
                example.setInput(1, 1f);
                example.setInput(2, 1f);
                example.setOutput(0, 0f);
                example.setOutput(1, 0f);
                example.setOutput(2, 0f);
                database.addExample(example);
                database.addTest(example);
            }
        }

        StandardNN nn = new StandardNN(3, new int[]{12}, 3);
        StandardNNTrainingProgress training = new StandardNNTrainingProgress();
        training.maxIterations = 100;
        training.learningRate = 0.01f;
        training.learningRateLoss = 0.999f;

        System.out.println("Starting training");

        int gen;
        for (gen = 0; gen < 100000; gen += 100)
        {
            System.out.println("Generation: " + gen);
            System.out.println("  Score: " + nn.test(database, 50));

            {
                // Print random test result
                ClassifierExample example = database.randomTest();
                for (int n = 0; n < nn.getInputCount(); n++)
                    nn.setInput(n, example.getInput(n));
                nn.run();
                for (int n = 0; n < nn.getOutputCount(); n++)
                    System.out.printf("  - %.2f / %.2f\n", nn.getOutput(n), example.getOutput(n));
            }

            System.out.println();

            training.iterations = 0;
            nn.train(database, training);
        }

        System.out.println("Final Results (Gen " + gen + "):");
        System.out.println("  Score: " + nn.test(database, 50));
    }
}
