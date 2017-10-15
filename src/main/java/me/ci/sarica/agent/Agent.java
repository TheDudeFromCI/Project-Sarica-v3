package me.ci.sarica.agent;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by thedudefromci on 10/12/17.
 *
 * This class represents a single learning agent.
 */
public class Agent
{
    private final ArrayList<Neuron> hiddenNeurons = new ArrayList<Neuron>();
    private final ArrayList<Neuron> inputNeurons = new ArrayList<Neuron>();
    private final ArrayList<Neuron> outputNeurons = new ArrayList<Neuron>();
    private final String uuid;
    private final String path;
    private boolean loaded;

    public Agent(String uuid, String database)
    {
        this.uuid = uuid;
        path = database + File.separatorChar + uuid;
    }

    public boolean isLoaded()
    {
        return loaded;
    }

    public void load()
    {
        // TODO Load agent from database.
        loaded = true;
    }

    public void unload()
    {
        // TODO Unload agent from database.
        loaded = false;
    }

    public String getUUID()
    {
        return uuid;
    }
}
