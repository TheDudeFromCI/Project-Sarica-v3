package test;

import me.ci.sarica.agent.standard.ClassificationDatabase;
import me.ci.sarica.agent.standard.ClassifierExample;
import me.ci.sarica.agent.standard.StandardNN;
import me.ci.sarica.agent.standard.StandardNNTrainingProgress;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class HandwrittenDigitRecognitionTest
{
    public static void main(String[] args)
    {
        ClassificationDatabase database = new ClassificationDatabase(28 * 28, 10);
        loadDatabase(database);
        StandardNN nn = new StandardNN(28 * 28, new int[]{16, 16, 16}, 10);

        StandardNNTrainingProgress training = new StandardNNTrainingProgress();
        training.maxIterations = 100;
        training.learningRate = 0.1f;
        training.learningRateLoss = 0.99f;

        System.out.println("Starting training");

        int gen;
        for (gen = 0; gen < 100000; gen += 100)
        {
            System.out.println("Generation: " + gen);
            System.out.println("  Score: " + nn.test(database, 5000));

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
        System.out.println("  Score: " + nn.test(database, 5000));
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
