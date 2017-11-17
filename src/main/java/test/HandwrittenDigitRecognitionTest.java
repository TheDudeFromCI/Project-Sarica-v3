package test;

import me.ci.sarica.agent.ClassificationDatabase;
import me.ci.sarica.agent.ClassifierExample;
import me.ci.sarica.agent.Matrix;
import me.ci.sarica.agent.NeuralNetwork;
import me.ci.sarica.terminal.history.LineGraph;
import me.ci.sarica.agent.NeuralNetworkBuilder;
import me.ci.sarica.terminal.history.BackPropTrainingTracker;
import me.ci.sarica.terminal.Terminal;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class HandwrittenDigitRecognitionTest
{
    public static void main(String[] args)
    {
		Terminal.logNormal("Tests", "Launching test 'Handwritten Digit Recognition'.");

        ClassificationDatabase database = new ClassificationDatabase(28 * 28, 10);
        loadDatabase(database);

		NeuralNetwork nn = new NeuralNetworkBuilder(28 * 28, 128, 64, 32, 10).addBias()
			.addBackPropagation(0.1f, 0.99995f, 0.5f).addBackPropListener(new BackPropTrainingTracker(database))
			.build();

        for (int gen = 0; gen < 100; gen++)
            nn.getBackPropagation().train(database, 100, 1000);
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

				Terminal.logVerbosef("Tests", "Loaded database of %d training images.", images.length);
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

				Terminal.logVerbosef("Tests", "Loaded database of %d testing images.", images.length);
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
