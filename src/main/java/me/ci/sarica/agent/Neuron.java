package me.ci.sarica.agent;

import me.ci.sarica.agent.neuron_functions.ActivationFunction;
import me.ci.sarica.agent.neuron_functions.Sigmoid;

import java.util.ArrayList;

/**
 * Created by thedudefromci on 10/14/17.
 *
 * This class represents a single neuron in an agent. This object holds a current value, has incoming
 * and outgoing connections, has a 'position' in space, has a custom activation function, and can
 * release and respond to changes in chemicals.
 */
public class Neuron
{
    private static final ActivationFunction DEFAULT_FUNCTION = new Sigmoid();
    private static float[] tempStorage = new float[256];

    private final ArrayList<Connection> incomingConnections = new ArrayList<Connection>();
    private final ArrayList<Connection> outgoingConnections = new ArrayList<Connection>();
    private float value;
    private float positionX;
    private float positionY;
    private float positionZ;
    private ActivationFunction function;

    public void solveValue()
    {
        int count = incomingConnections.size();

        if (tempStorage.length < count)
            tempStorage = new float[count];

        for (int i = 0; i < count; i++)
            tempStorage[i] = incomingConnections.get(i).getTempValue();

        if (function == null)
            value = DEFAULT_FUNCTION.runFunction(tempStorage, count);
        else
            value = function.runFunction(tempStorage, count);
    }

    public void storeValue()
    {
        int count = outgoingConnections.size();
        for (int i = 0; i < count; i++)
            outgoingConnections.get(i).setTempValue(value);
    }

    public void copyProperties(Neuron other)
    {
        incomingConnections.clear();
        outgoingConnections.clear();

        value = other.value;
        positionX = other.positionX;
        positionY = other.positionY;
        positionZ = other.positionZ;
        function = other.function;
    }

    public int getIncomingConnectionCount()
    {
        return incomingConnections.size();
    }

    public int getOutgoingConnectionCount()
    {
        return outgoingConnections.size();
    }

    public Connection getIncomingConnectionByIndex(int index)
    {
        return incomingConnections.get(index);
    }

    public Connection getOutgoingConnectionByIndex(int index)
    {
        return outgoingConnections.get(index);
    }

    public void setValue(float value)
    {
        this.value = value;
    }

    public float getValue()
    {
        return value;
    }

    public float getPositionX()
    {
        return positionX;
    }

    public float getPositionY()
    {
        return positionY;
    }

    public float getPositionZ()
    {
        return positionZ;
    }

    public void setPositionX(float positionX)
    {
        this.positionX = positionX;
    }

    public void setPositionY(float positionY)
    {
        this.positionY = positionY;
    }

    public void setPositionZ(float positionZ)
    {
        this.positionZ = positionZ;
    }

    public boolean isConnectionToChildNeuron(Neuron neuron)
    {
        int count = outgoingConnections.size();
        for (int i = 0; i < count; i++)
            if (outgoingConnections.get(i).getChild() == neuron)
                return true;
        return false;
    }

    public Connection connectToChildNeuron(Neuron neuron)
    {
        int count = outgoingConnections.size();
        for (int i = 0; i < count; i++)
            if (outgoingConnections.get(i).getChild() == neuron)
                return outgoingConnections.get(i);

        Connection connection = new Connection(this, neuron);

        outgoingConnections.add(connection);
        neuron.incomingConnections.add(connection);

        return connection;
    }

    public void clearConnectionToNeuron(Neuron neuron)
    {
        int count = outgoingConnections.size();
        for (int i = 0; i < count; i++)
            if (outgoingConnections.get(i).getChild() == neuron)
            {
                Connection connection = outgoingConnections.get(i);
                outgoingConnections.remove(connection);
                neuron.incomingConnections.remove(connection);
                return;
            }
    }

    public void safeDelete()
    {
        int count = incomingConnections.size();
        for (int i = 0; i < count; i++)
            incomingConnections.get(i).getParent().outgoingConnections.remove(incomingConnections.get(i));

        count = outgoingConnections.size();
        for (int i = 0; i < count; i++)
            outgoingConnections.get(i).getChild().incomingConnections.remove(outgoingConnections.get(i));

        incomingConnections.clear();
        outgoingConnections.clear();
    }
}
