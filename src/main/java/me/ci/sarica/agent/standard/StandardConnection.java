package me.ci.sarica.agent.standard;

public class StandardConnection
{
    private final StandardNeuron parent;
    private final StandardNeuron child;
    private float weight;
    private float lastDelta;

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
