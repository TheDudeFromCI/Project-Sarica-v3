package test;

import me.ci.sarica.agent.matrix_based.Matrix;
import me.ci.sarica.agent.matrix_based.NeuralNetwork;
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

        NeuralNetwork nn = new NeuralNetwork(3, 6, 3);
        StandardNNTrainingProgress training = new StandardNNTrainingProgress();
        training.maxIterations = 60000;
        training.learningRate = 0.01f;
        training.learningRateLoss = 0.999f;

        System.out.println("Starting training");
        nn.train(database, training);
        System.out.println();

        System.out.println("Training Complete. Results:");
        {
            Matrix m = new Matrix(8, 3);
            for (int r = 0; r < 8; r++)
                for (int c = 0; c < 3; c++)
                    m.setValue(r, 2 - c, (r & (1 << c)) > 0 ? 1f : 0f);
            m = nn.run(m);
            System.out.println(m);
        }
    }
}
