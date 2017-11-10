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
        NeuralNetwork nn = new NeuralNetwork(28 * 28, 24, 16, 10);

        System.out.println("Starting training");
        nn.train(database, 3, 25000);

        System.out.println("Training Complete. Results:");
        {
            int randomImageSamples = 10;
            ClassifierExample[] tests = new ClassifierExample[randomImageSamples];
            for (int i = 0; i < randomImageSamples; i++)
                tests[i] = database.randomTest();

            Matrix m = new Matrix(randomImageSamples, 28 * 28);
            for (int r = 0; r < randomImageSamples; r++)
            {
                for (int c = 0; c < 28 * 28; c++)
                    m.setValue(r, c, tests[r].getInput(c));
            }
            m = nn.run(m);

            System.out.println(round(m));
            System.out.println("\nReal Answers:");
            for (int i = 0; i < randomImageSamples; i++)
            {
                for (int n = 0; n < 10; n++)
                    if (tests[i].getOutput(n) > 0)
                        System.out.println(n);
            }
        }
    }

    private static Matrix round(Matrix m)
    {
        Matrix c = new Matrix(m.getRows(), m.getCols());

        for (int i = 0; i < m.getValueCount(); i++)
            c.setValueByIndex(i, Math.round(m.getValueByIndex(i)));

        return c;
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
