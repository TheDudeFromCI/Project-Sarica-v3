package me.ci.sarica.agent.standard;

public class StandardConnection
{
    private final StandardNeuron parent;
    private final StandardNeuron child;
    private float weight;
    private float lastDelta;
    private float deltaSum;

    public StandardConnection(StandardNeuron parent, StandardNeuron child)
    {
        this.parent = parent;
        this.child = child;

        // Random initial state
        weight = (float)Math.random() * 2f - 1f;
    }

    public float getLastDelta()
    {
        return lastDelta;
    }

    public void setLastDelta(float delta)
    {
        lastDelta = delta;
    }

    public void resetDelta()
    {
        deltaSum = 0f;
    }

    public void addDelta(float delta)
    {
        deltaSum += delta;
    }

    public float getDeltaSum()
    {
        return deltaSum;
    }

    public StandardNeuron getChild()
    {
         return child;
    }

    public StandardNeuron getParent()
    {
        return parent;
    }

    public float getWeight()
    {
        return weight;
    }

    public void setWeight(float weight)
    {
        this.weight = weight;
    }
}
