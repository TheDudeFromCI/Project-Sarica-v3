package test;

import me.ci.sarica.agent.ClassificationDatabase;
import me.ci.sarica.agent.ClassifierExample;
import me.ci.sarica.agent.Matrix;
import me.ci.sarica.agent.NeuralNetwork;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class HandwrittenDigitRecognitionTest
{
    public static void main(String[] args)
    {
        ClassificationDatabase database = new ClassificationDatabase(28 * 28, 10);
        loadDatabase(database);
        NeuralNetwork nn = new NeuralNetwork(28 * 28, 32, 16, 12, 10);

        System.out.println("Starting training");

        float learningRate = 0.1f;
        float learningLoss = 0.9999f;
        for (int gen = 0; gen < 1000; gen++)
        {
            learningRate = nn.train(database, 100, 1000, gen * 1000, learningRate, learningLoss);

            System.out.println();
            System.out.println("Test Progress: ");
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

                System.out.println(" Test Error: " + nn.meanError(e));
                System.out.println(" Rate: " + String.format("%.3f", (float)correct / rounded.getRows() * 100f) + "%");
                System.out.println(" LR: " + learningRate);
            }
            System.out.println();
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


    private static void loadDatabase(ClassificationDatabase database)
    {
        try
        {
            String root = "C:\\Users\\TheDudeFromCI\\Documents\\Machine Learning\\MNIST Dataset";

            // Train
            {
                File trainImages = new File(root, "train-images.dat");
                File trainLabels = new File(root, "train-labels.dat");
                float[][] images = loadDigitImages(trainImages);
                byte[] labels = loadDigitLabels(trainLabels);

                for (int i = 0; i < labels.length; i++)
                {
                    ClassifierExample example = new ClassifierExample(28 * 28, 10);
                    for (int p = 0; p < 28 * 28; p++)
                        example.setInput(p, images[i][p]);
                    for (int l = 0; l < 10; l++)
                        example.setOutput(l, labels[i] == l ? 1f : 0f);
                    database.addExample(example);
                }
            }

            // Test
            {
                File testImages = new File(root, "test-images.dat");
                File testLabels = new File(root, "test-labels.dat");
                float[][] images = loadDigitImages(testImages);
                byte[] labels = loadDigitLabels(testLabels);

                for (int i = 0; i < labels.length; i++)
                {
                    ClassifierExample example = new ClassifierExample(28 * 28, 10);
                    for (int p = 0; p < 28 * 28; p++)
                        example.setInput(p, images[i][p]);
                    for (int l = 0; l < 10; l++)
                        example.setOutput(l, labels[i] == l ? 1f : 0f);
                    database.addTest(example);
                }
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private static int nextInt(BufferedInputStream in) throws Exception
    {
        int i = 0;
        i |= in.read() << 24;
        i |= in.read() << 16;
        i |= in.read() << 8;
        i |= in.read();
        return i;
    }

    private static float[][] loadDigitImages(File file) throws Exception
    {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        in.skip(4);
        int imageCount = nextInt(in);
        float[][] images = new float[imageCount][28 * 28];
        in.skip(8);

        for (int i = 0; i < imageCount; i++)
            for (int p = 0; p < 28 * 28; p++)
                images[i][p] = in.read() / 255f;

        in.close();

        return images;
    }

    private static byte[] loadDigitLabels(File file) throws Exception
    {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        in.skip(4);
        int count = nextInt(in);
        byte[] labels = new byte[count];

        for (int i = 0; i < count; i++)
            labels[i] = (byte)in.read();

        in.close();

        return labels;
    }
}
