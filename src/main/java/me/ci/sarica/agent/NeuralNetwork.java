package me.ci.sarica.agent;

public class NeuralNetwork
{
    private final Matrix[] layers;
    private final Matrix[] bias;
    private final int inputs;
    private final int outputs;

    public NeuralNetwork(int... layerSizes)
    {
        if (layerSizes.length < 2)
            throw new IllegalArgumentException("Neural Network cannot have less than two layers!");

        inputs = layerSizes[0];
        outputs = layerSizes[layerSizes.length - 1];

        bias = new Matrix[layerSizes.length - 1];
        for (int i = 0; i < bias.length; i++)
        {
            bias[i] = new Matrix(1, layerSizes[i + 1]);
            for (int j = 0; j < bias[i].getCols(); j++)
                bias[i].setValueByIndex(j, ((float)Math.random() * 2f - 1f) / (float)Math.sqrt(layerSizes[i]));
        }

        layers = new Matrix[layerSizes.length - 1];
        for (int i = 0; i < layers.length; i++)
            layers[i] = buildLayerMatrix(layerSizes[i], layerSizes[i + 1]);
    }

    public int getInputs()
    {
        return inputs;
    }

    public int getOutputs()
    {
        return outputs;
    }

    private Matrix buildLayerMatrix(int inputs, int outputs)
    {
        Matrix m = new Matrix(inputs, outputs);

        for (int i = 0; i < m.getValueCount(); i++)
            m.setValueByIndex(i, ((float)Math.random() * 2f - 1f) / (float)Math.sqrt(inputs));

        return m;
    }

    public void train(ClassificationDatabase database, int sampleCount, int iterations)
    {
        train(database, sampleCount, iterations, 0, 0.1f, 0.999f);
    }

    public float train(ClassificationDatabase database, int sampleCount, int iterations, int iterationsOffset, float
                                                                                                                  learningRate, float learningLoss)
    {
        if (database.getInputCount() != getInputs())
            throw new IllegalArgumentException("Database input count does not match network input count!");
        if (database.getOutputCount() != getOutputs())
            throw new IllegalArgumentException("Database output count does not match network output count!");

        if (sampleCount == -1)
            sampleCount = database.getExampleCount();
        sampleCount = Math.min(sampleCount, database.getExampleCount());

        Matrix x = new Matrix(sampleCount, database.getInputCount());
        Matrix y = new Matrix(sampleCount, database.getOutputCount());

        if (sampleCount == database.getExampleCount())
        {
            for (int i = 0; i < x.getRows(); i++)
            {
                ClassifierExample ex = database.getExample(i);
                for (int n = 0; n < getInputs(); n++)
                    x.setValue(i, n, ex.getInput(n));
                for (int n = 0; n < getOutputs(); n++)
                    y.setValue(i, n, ex.getOutput(n));
            }

            train(x, y, iterations, iterationsOffset, learningRate, learningLoss);
        }
        else
        {
            int itrRemain = iterations;
            int subItr = iterations / sampleCount;

            for (int j = 0; j <= subItr; j++)
            {
                for (int i = 0; i < x.getRows(); i++)
                {
                    ClassifierExample ex = database.randomExample();
                    for (int n = 0; n < getInputs(); n++)
                        x.setValue(i, n, ex.getInput(n));
                    for (int n = 0; n < getOutputs(); n++)
                        y.setValue(i, n, ex.getOutput(n));
                }

                int steps = Math.min(itrRemain, sampleCount);
                learningRate = train(x, y, steps, iterationsOffset, learningRate, learningLoss);
                itrRemain -= steps;
                iterationsOffset += steps;

                if (itrRemain == 0)
                    break;
            }
        }

        return learningRate;
    }

    public void train(Matrix x, Matrix y, int iterations)
    {
        train(x, y, iterations, 0, 0.1f, 0.999f);
    }

    public float train(Matrix x, Matrix y, int iterations, int iterationOffset, float learningRate, float learningLoss)
    {
        Matrix[] l = new Matrix[layers.length + 1];
        Matrix[] error = new Matrix[layers.length];
        Matrix[] delta = new Matrix[layers.length];
        Matrix[] delBi = new Matrix[layers.length];

        int lastLayer = layers.length - 1;
        int layerValueLayer = l.length - 1;

        for (int itr = 0; itr < iterations; itr++)
        {
            l[0] = x.add(noiseMatrix(x.getRows(), x.getCols(), 0.5f));
            for (int i = 1; i < l.length; i++)
                l[i] = l[i - 1].dot(layers[i - 1]).addRowVector(bias[i - 1]).sigmoid();

            error[lastLayer] = y.sub(l[layerValueLayer]);
            delta[lastLayer] = error[lastLayer].mul(l[layerValueLayer].sigmoidDeriv());
            delBi[lastLayer] = error[lastLayer].collapseToColVector();

            if ((itr + iterationOffset) % 100 == 0)
                System.out.println("Iteration " + (itr + iterationOffset) + ": " + error[lastLayer].meanError());

            for (int i = layers.length - 2; i >= 0; i--)
            {
                error[i] = delta[i + 1].dot(layers[i + 1].transpose());
                delta[i] = error[i].mul(l[i + 1].sigmoidDeriv());
                delBi[i] = error[i].collapseToColVector();
            }

            for (int i = 0; i < layers.length; i++)
            {
                layers[i] = layers[i].add(l[i].transpose().dot(delta[i]).mul(learningRate));
                bias[i] = bias[i].add(delBi[i].mul(learningRate));
            }

            learningRate *= learningLoss;
        }

        return learningRate;
    }

    private Matrix noiseMatrix(int rows, int cols, float strength)
    {
        Matrix m = new Matrix(rows, cols);

        for (int i = 0; i < m.getValueCount(); i++)
            m.setValueByIndex(i, ((float)Math.random() * 2f - 1f) * strength);

        return m;
    }

    public Matrix run(Matrix in)
    {
        Matrix[] l = new Matrix[layers.length + 1];

        l[0] = in;
        for (int i = 1; i < l.length; i++)
            l[i] = l[i - 1].dot(layers[i - 1]).addRowVector(bias[i - 1]).sigmoid();

        return l[l.length - 1];
    }
}
