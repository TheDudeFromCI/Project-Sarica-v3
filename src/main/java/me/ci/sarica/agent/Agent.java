package me.ci.sarica.agent;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by thedudefromci on 10/12/17.
 *
 * This class represents a single learning agent.
 */
public class Agent
{
    private static String randomUUID()
    {
        return UUID.randomUUID().toString();
    }

    public static Agent createAgent(String path, int inputs, int outputs)
    {
        Agent agent = new Agent(randomUUID(), path);
        agent.network = new Network();

        for (int i = 0; i < inputs; i++)
            agent.network.addInputNeuron();
        for (int i = 0; i < outputs; i++)
            agent.network.addOutputNeuron();

        return agent;
    }

    private final String uuid;
    private final String database;
    private final String path;
    private Network network;
    private float score;

    private Agent(String uuid, String database)
    {
        this.uuid = uuid;
        this.database = database;
        path = database + File.separatorChar + uuid;
    }

    public void setScore(float score)
    {
        this.score = score;
    }

    public float getScore()
    {
        return score;
    }

    public boolean isLoaded()
    {
        return network != null;
    }

    public void load()
    {
        // TODO Load agent from database
        // TODO Generate new network if does not exist in database
        network = new Network();
    }

    public void saveNetwork()
    {
        // TODO Save agent to database
    }

    public void unload()
    {
        network = null;
        System.gc();
    }

    public Agent reproduce()
    {
        Agent agent = new Agent(randomUUID(), database);

        if (!isLoaded())
            load();
        agent.network = network.duplicate();
        agent.network.mutate();

        return agent;
    }

    public String getUUID()
    {
        return uuid;
    }
}
