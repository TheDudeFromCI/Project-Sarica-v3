package me.ci.sarica.agent;

/**
 * Created by thedudefromci on 10/14/17.
 *
 * This class represents the connection between two neurons.
 */
public class Connection
{
    private final Neuron parent;
    private final Neuron child;
    private float weight;
    private float tempValue;

    public Connection(Neuron parent, Neuron child)
    {
        this.parent = parent;
        this.child = child;
    }

    public float getWeight()
    {
        return weight;
    }

    public void setWeight(float weight)
    {
        this.weight = weight;
    }

    public void setTempValue(float tempValue)
    {
        this.tempValue = tempValue;
    }

    public float getTempValue()
    {
        return tempValue;
    }

    public Neuron getParent()
    {
        return parent;
    }

    public Neuron getChild()
    {
        return child;
    }
}
