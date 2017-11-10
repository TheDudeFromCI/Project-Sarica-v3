package me.ci.sarica.agent.standard;

public class StandardNN
{
    private final StandardNeuron[][] neurons;

    public StandardNN(int inputs, int[] layers, int outputs)
    {
        // Confirm layer sizes within range
        if (inputs < 1)
            throw new IllegalArgumentException("Inputs cannot have less than 1 neuron!");
        for (int i : layers)
            if (i < 1)
                throw new IllegalArgumentException("Hidden layers cannot have less than 1 neuron!");
        if (outputs < 1)
            throw new IllegalArgumentException("Outputs cannot have less than 1 neuron!");

        // Build network topology
        neurons = new StandardNeuron[layers.length + 2][];
        for (int layer = 0; layer < neurons.length; layer++)
        {
            int layerSize;

            if (layer == 0)
                layerSize = inputs;
            else if (layer == neurons.length - 1)
                layerSize = outputs;
            else
                layerSize = layers[layer - 1];

            neurons[layer] = new StandardNeuron[layerSize];
            for (int index = 0; index < layerSize; index++)
                neurons[layer][index] = new StandardNeuron();
        }

        // Fully connection neuron layers
        for (int i = 0; i < neurons.length - 1; i++)
            for (int j = 0; j < neurons[i].length; j++)
                for (int k = 0; k < neurons[i + 1].length; k++)
                    neurons[i][j].connectToChild(neurons[i + 1][k]);
    }

    public void setInput(int index, float value)
    {
        neurons[0][index].setValue(value);
    }

    public float getOutput(int index)
    {
        return neurons[neurons.length - 1][index].getValue();
    }

    public void run()
    {
        // Clear neurons (except input)
        for (int i = 1; i < neurons.length; i++)
            for (int j = 0; j < neurons[i].length; j++)
                neurons[i][j].clear();

        // Feed forward values layer by layer
        for (int i = 0; i < neurons.length; i++)
        {
            for (int j = 0; j < neurons[i].length; j++)
                neurons[i][j].receiveValues();

            for (int j = 0; j < neurons[i].length; j++)
                neurons[i][j].sendValues();
        }
    }

    public int getInputCount()
    {
        return neurons[0].length;
    }

    public int getOutputCount()
    {
        return neurons[neurons.length - 1].length;
    }

    public void train(ClassificationDatabase database, StandardNNTrainingProgress training)
    {
        if (database.getInputCount() != getInputCount())
            throw new IllegalArgumentException("Database input count does not match network input count!");
        if (database.getOutputCount() != getOutputCount())
            throw new IllegalArgumentException("Database output count does not match network output count!");

        for (; training.iterations < training.maxIterations; training.iterations++)
        {
            trainIteration(database, training.learningRate, training.momentum);
            training.learningRate *= training.learningRateLoss;
        }
    }

    public float test(ClassificationDatabase database, float iterations)
    {
        double error = 0f;

        for (int i = 0; i < iterations; i++)
        {
            ClassifierExample example = database.randomTest();
            for (int n = 0; n < getInputCount(); n++)
                setInput(n, example.getInput(n));
            run();
            for (int n = 0; n < getOutputCount(); n++)
                error += (getOutput(n) - example.getOutput(n)) * (getOutput(n) - example.getOutput(n));
        }

        return (float) (error / iterations);
    }

    private void trainIteration(ClassificationDatabase database, float sampleWeight, float momentum)
    {
        sampleWeight *= 1f / database.getSampleSize();
        ClassifierExample example;
        for (int i = 0; i < database.getSampleSize(); i++)
        {
            example = database.randomExample();

            for (int n = 0; n < getInputCount(); n++)
                setInput(n, example.getInput(n));
            run();

            float[] lastLayerError = new float[getOutputCount()];
            for (int n = 0; n < lastLayerError.length; n++)
                lastLayerError[n] = (example.getOutput(n) - getOutput(n)) * (getOutput(n) > 0 ? 1f : 0f);

            float[] currentLayerError;
            for (int layer = neurons.length - 2; layer >= 0; layer--)
            {
                currentLayerError = new float[neurons[layer].length];
                for (int n = 0; n < currentLayerError.length; n++)
                {
                    for (int j = 0; j < lastLayerError.length; j++)
                    {
                        StandardConnection con = neurons[layer][n].getChildConnection(j);

                        float w = con.getWeight();
                        currentLayerError[n] += lastLayerError[j] * w;
                        con.addDelta(lastLayerError[j] * neurons[layer][n].getValue());
                    }

                    currentLayerError[n] *= (neurons[layer][n].getValue() > 0 ? 1f : 0f);
                }

//                for (int n = 0; n < lastLayerError.length; n++)
//                {
//                    float bias = neurons[layer + 1][n].getBias();
//
//                    float delta = sampleWeight * lastLayerError[n];
//                    bias -= delta + momentum * neurons[layer + 1][n].getLastDelta();
//
//                    neurons[layer + 1][n].setLastDelta(delta);
//                    neurons[layer + 1][n].setBias(bias);
//                }

                lastLayerError = currentLayerError;
            }
        }

        for (int layer = 0; layer < neurons.length - 2; layer++)
        {
            for (int n = 0; n < neurons[layer].length; n++)
            {
                for (int j = 0; j < neurons[layer + 1].length; j++)
                {
                    StandardConnection con = neurons[layer][n].getChildConnection(j);

                    float delta = con.getDeltaSum();
                    float w = con.getWeight() + delta + momentum * con.getLastDelta();
                    con.setLastDelta(delta);

                    con.setWeight(sampleWeight * w);
                }
            }
        }
    }
}
