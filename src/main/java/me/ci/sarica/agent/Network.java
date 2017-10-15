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

    public Neuron addInputNeuron()
    {
        Neuron neuron = new Neuron();
        inputNeurons.add(neuron);
        return neuron;
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
}
