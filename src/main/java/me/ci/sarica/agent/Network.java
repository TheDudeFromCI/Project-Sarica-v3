package me.ci.sarica.agent;

import java.util.ArrayList;

/**
 * Created by TheDudeFromCI on 10/14/2017.
 *
 * This class represents the entire topology and connectivity of an agent.
 */
public class Network
{
    private final ArrayList<Neuron> inputNeurons = new ArrayList<Neuron>();
    private final ArrayList<Neuron> hiddenNeurons = new ArrayList<Neuron>();
    private final ArrayList<Neuron> outputNeurons = new ArrayList<Neuron>();
    private float advancedMutationRate = 0.01f;
    private float weightMutation = 0.1f;
    private float connectionMax = 4;

    public Neuron addInputNeuron()
    {
        Neuron neuron = new Neuron();
        inputNeurons.add(neuron);
        return neuron;
    }

    public float getAdvancedMutationRate()
    {
        return advancedMutationRate;
    }

    public void setAdvancedMutationRate(float advancedMutationRate)
    {
        this.advancedMutationRate = advancedMutationRate;
    }

    public void removeInputNeuron(Neuron neuron)
    {
        neuron.safeDelete();
        inputNeurons.remove(neuron);
    }

    public Neuron addHiddenNeuron()
    {
        Neuron neuron = new Neuron();
        hiddenNeurons.add(neuron);
        return neuron;
    }

    public void removeHiddenNeuron(Neuron neuron)
    {
        neuron.safeDelete();
        hiddenNeurons.remove(neuron);
    }

    public Neuron addOutputNeuron()
    {
        Neuron neuron = new Neuron();
        outputNeurons.add(neuron);
        return neuron;
    }

    public void removeOutputNeuron(Neuron neuron)
    {
        neuron.safeDelete();
        outputNeurons.remove(neuron);
    }

    public Neuron getInputNeuronById(int index)
    {
        return inputNeurons.get(index);
    }

    public Neuron getHiddenNeuronById(int index)
    {
        return hiddenNeurons.get(index);
    }

    public Neuron getOutputNeuronById(int index)
    {
        return outputNeurons.get(index);
    }

    public int getInputNeuronCount()
    {
        return inputNeurons.size();
    }

    public int getHiddenNeuronCount()
    {
        return hiddenNeurons.size();
    }

    public int getOutputNeuronCount()
    {
        return outputNeurons.size();
    }

    public int getGlobalIndexOf(Neuron neuron)
    {
        int a = inputNeurons.indexOf(neuron);
        if (a > -1) return a;

        a = hiddenNeurons.indexOf(neuron);
        if (a > -1) return a + inputNeurons.size();

        a = outputNeurons.indexOf(neuron);
        if (a > -1) return a + inputNeurons.size() + hiddenNeurons.size();

        return -1;
    }

    public Neuron getGlobalNeuronByIndex(int index)
    {
        if (index < inputNeurons.size())
            return inputNeurons.get(index);

        index -= inputNeurons.size();

        if (index < hiddenNeurons.size())
            return hiddenNeurons.get(index);

        index -= hiddenNeurons.size();
        return outputNeurons.get(index);
    }

    public int getTotalNeuronCount()
    {
        return inputNeurons.size() + hiddenNeurons.size() + outputNeurons.size();
    }

    public Network duplicate()
    {
        Network network = new Network();

        // Clone all neurons
        for (int i = 0; i < inputNeurons.size(); i++)
            network.addInputNeuron().copyProperties(inputNeurons.get(i));
        for (int i = 0; i < hiddenNeurons.size(); i++)
            network.addHiddenNeuron().copyProperties(hiddenNeurons.get(i));
        for (int i = 0; i < outputNeurons.size(); i++)
            network.addOutputNeuron().copyProperties(outputNeurons.get(i));

        // Clone all neuron connections
        for (int i = 0; i < getTotalNeuronCount(); i++)
        {
            Neuron n = getGlobalNeuronByIndex(i);
            for (int j = 0; j < n.getOutgoingConnectionCount(); j++)
            {
                Connection c = n.getOutgoingConnectionByIndex(j);
                network.getInputNeuronById(i).connectToChildNeuron(network.getGlobalNeuronByIndex(getGlobalIndexOf(c.getChild()))).setWeight(c.getWeight());
            }
        }

        return network;
    }

    public void mutate()
    {
        if (Math.random() < advancedMutationRate)
            mutateAdvanced();
        else
            mutateSimple();
    }

    private void mutateSimple()
    {
        int fails = 0;
        int count = (int)(Math.random() * connectionMax) + 1;
        for (int i = 0; i < count; i++)
        {
            Neuron n = getGlobalNeuronByIndex((int)(Math.random() * getTotalNeuronCount()));
            if (n.getOutgoingConnectionCount() == 0)
            {
                fails++;
                continue;
            }
            Connection c = n.getOutgoingConnectionByIndex((int)(Math.random() * n.getOutgoingConnectionCount()));
            c.setWeight(c.getWeight() + ((float)Math.random() * 2f - 1f) * weightMutation);
        }

        if (fails == count)
            mutateAdvanced();
    }

    private void mutateAdvanced()
    {
        float action = (float)Math.random();

        if (action < 0.4f)
        {
            // Grow connection
            Neuron n1 = getGlobalNeuronByIndex((int)(Math.random() * getTotalNeuronCount()));
            Neuron n2 = getGlobalNeuronByIndex((int)(Math.random() * getTotalNeuronCount()));

            if (n1 != n2)
                n1.connectToChildNeuron(n2);
        }
        else if (action < 0.4f + 0.35f)
        {
            // Destory connection
            Neuron n = getGlobalNeuronByIndex((int)(Math.random() * getTotalNeuronCount()));
            if (n.getOutgoingConnectionCount() > 0)
            {
                Connection c = n.getOutgoingConnectionByIndex((int)(Math.random() * n.getOutgoingConnectionCount()));
                n.clearConnectionToNeuron(c.getChild());
            }
        }
        else if (action < 0.4f + 0.35f + 0.15f)
        {
            // Add neuron
            Neuron n1 = getGlobalNeuronByIndex((int)(Math.random() * getTotalNeuronCount()));
            Neuron n2 = addHiddenNeuron();
            n1.connectToChildNeuron(n2);
        }
        else
        {
            // Remove neuron
            Neuron n = getHiddenNeuronById((int)(Math.random() * getHiddenNeuronCount()));
            n.safeDelete();
            hiddenNeurons.remove(n);
        }
    }
}
