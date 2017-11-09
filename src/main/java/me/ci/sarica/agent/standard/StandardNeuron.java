package me.ci.sarica.agent.standard;

import java.util.ArrayList;

public class StandardNeuron
{
    private final ArrayList<StandardConnection> outgoingConnections = new ArrayList<StandardConnection>();
    private float value;
    private float bias;
    private float lastDelta;

    public StandardNeuron()
    {
        // Randomize initial state
        bias = (float)Math.random() * 2f - 1f;
    }

    public float getLastDelta()
    {
        return lastDelta;
    }

    public void setLastDelta(float delta)
    {
        lastDelta = delta;
    }

    public float getValue()
    {
        return value;
    }

    public void setValue(float value)
    {
        this.value = value;
    }

    public float getBias()
    {
        return bias;
    }

    public void clear()
    {
        value = 0f;
    }

    public void setBias(float bias)
    {
        this.bias = bias;
    }

    public void sendValues()
    {
        for (StandardConnection con : outgoingConnections)
            con.getChild().value += value * con.getWeight();
    }

    public void receiveValues()
    {
        value = activationFunction(value + bias);
    }

    private float activationFunction(float x)
    {
        // ReLU
        return Math.max(0, x);
    }

    public void connectToChild(StandardNeuron child)
    {
        outgoingConnections.add(new StandardConnection(this, child));
    }

    public float getWeightToChild(int index)
    {
        return outgoingConnections.get(index).getWeight();
    }

    public StandardConnection getChildConnection(int index)
    {
        return outgoingConnections.get(index);
    }

    public void setWeightToChild(int index, float weight)
    {
        outgoingConnections.get(index).setWeight(weight);
    }
}
