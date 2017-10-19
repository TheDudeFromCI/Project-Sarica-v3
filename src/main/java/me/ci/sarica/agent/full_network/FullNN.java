package me.ci.sarica.agent.full_network;

/**
 * Created by TheDudeFromCI on 10/18/2017.
 *
 * This class represents a "Full Neural Network". A FNN is a neural network in which every neuron is connected to
 * every other neuron, including itself. This type of network has an extremely high connectivity. The number of
 * connections of this network is equal to N^2, where N is the total number of neurons.
 *
 * In this project, this type of network is used as a base for representing a consciousness. This network should be
 * updated constantly regardless of whether or not the input neurons have been effected.
 */
public class FullNN
{
    private final float[] neurons;
    private final float[] weights;
    private final float[] tempValues;
    private final int inputNeurons;
    private final int hiddenNeurons;
    private final int outputNeurons;
    private final int totalNeurons;
    private float score;

    public FullNN(int inputNeurons, int hiddenNeurons, int outputNeurons)
    {
        this(inputNeurons, hiddenNeurons, outputNeurons, true);
    }

    public FullNN(int inputNeurons, int hiddenNeurons, int outputNeurons, boolean randomWeights)
    {
        this.inputNeurons = inputNeurons;
        this.hiddenNeurons = hiddenNeurons;
        this.outputNeurons = outputNeurons;
        totalNeurons = inputNeurons + hiddenNeurons + outputNeurons;

        neurons = new float[totalNeurons];
        weights = new float[totalNeurons * totalNeurons];
        tempValues = new float[neurons.length];

        if (randomWeights)
        {
            for (int i = 0; i < neurons.length; i++)
                neurons[i] = (float) Math.random();
            for (int i = 0; i < weights.length; i++)
                weights[i] = (float) Math.random() * 2f - 1f;
        }
    }

    public float getScore()
    {
        return score;
    }

    public void setScore(float score)
    {
        this.score = score;
    }

    public int getTotalNeurons()
    {
        return totalNeurons;
    }

    public int getInputNeurons()
    {
        return inputNeurons;
    }

    public int getHiddenNeurons()
    {
        return hiddenNeurons;
    }

    public int getOutputNeurons()
    {
        return outputNeurons;
    }

    public float getNeuronValue(int index)
    {
        return neurons[index];
    }

    public void setNeuronValue(int index, float value)
    {
        neurons[index] = value;
    }

    public void setInput(int index, float value)
    {
        setNeuronValue(index, value);
    }

    public float getOutput(int index)
    {
        return getNeuronValue(inputNeurons + index);
    }

    public int getTotalWeights()
    {
        return weights.length;
    }

    public float getWeightValue(int index)
    {
        return weights[index];
    }

    public void setWeightValue(int index, float value)
    {
        weights[index] = value;
    }

    public void runIteration()
    {
        // Store neuron temp values
        for(int i = 0; i < tempValues.length; i++)
            tempValues[i] = neurons[i];

        // Retrieve new neuron values
        for (int i = 0; i < neurons.length; i++)
        {
            neurons[i] = 0f;

            int row = i * totalNeurons;
            for (int j = 0; j < neurons.length; j++)
                neurons[i] += tempValues[j] * weights[row + j];

            neurons[i] = 1f / (1f + (float)Math.pow(Math.E, -neurons[i]));
        }
    }

    public FullNN reproduce(float weightMutation, float weightPercentsMutated)
    {
        FullNN nn = new FullNN(inputNeurons, hiddenNeurons, outputNeurons, false);

        for (int i = 0; i < neurons.length; i++)
            nn.neurons[i] = neurons[i];

        for (int i = 0; i < weights.length; i++)
        {
            if (Math.random() < weightPercentsMutated)
                nn.weights[i] = weights[i] + ((float)Math.random() * 2f - 1f) * weightMutation;
            else
                nn.weights[i] = weights[i];
        }

        return nn;
    }
}
