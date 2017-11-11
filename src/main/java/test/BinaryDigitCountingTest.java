package test;

import me.ci.sarica.agent.Matrix;
import me.ci.sarica.agent.NeuralNetwork;
import me.ci.sarica.agent.ClassificationDatabase;
import me.ci.sarica.agent.ClassifierExample;

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

        NeuralNetwork nn = new NeuralNetwork(3, 12, 3);

        System.out.println("Starting training");
        nn.train(database, -1, 60000);
        System.out.println();

        System.out.println("Training Complete. Results:");
        {
            Matrix x = new Matrix(database.getTestCount(), database.getInputCount());
            Matrix y = new Matrix(database.getTestCount(), database.getOutputCount());
            for (int r = 0; r < x.getRows(); r++)
            {
                for (int c = 0; c < x.getCols(); c++)
                    x.setValue(r, c, database.getTest(r).getInput(c));
                for (int c = 0; c < y.getCols(); c++)
                    y.setValue(r, c, database.getTest(r).getOutput(c));
            }

            Matrix e = nn.run(x);
            Matrix rounded = round(e);
            e = sub(e, y);

            int correct = 0;
            counter:for (int i = 0; i < rounded.getRows(); i++)
            {
                for (int c = 0; c < rounded.getCols(); c++)
                    if (Math.abs(rounded.getValue(i, c) - y.getValue(i, c)) > 0.001f)
                        continue counter;
                correct++;
            }

            System.out.println(" Test Error: " + e.meanError());
            System.out.println(" Rate: " + String.format("%.3f", (float)correct / rounded.getRows() * 100f) + "%");

            System.out.println();
            System.out.println(rounded);
        }
    }

    private static Matrix sub(Matrix a, Matrix b)
    {
        if (a.getRows() != b.getRows() || a.getCols() != b.getCols())
            throw new IllegalArgumentException("Matrix sizes do not match!");

        Matrix c = new Matrix(a.getRows(), a.getCols());

        for (int row = 0; row < a.getRows(); row++)
            for (int col = 0; col < a.getCols(); col++)
                c.setValue(row, col, a.getValue(row, col) - b.getValue(row, col));

        return c;
    }

    private static Matrix round(Matrix a)
    {
        Matrix m = new Matrix(a.getRows(), a.getCols());

        for (int i = 0; i < a.getValueCount(); i++)
            m.setValueByIndex(i, Math.round(a.getValueByIndex(i)));

        return m;
    }
}
