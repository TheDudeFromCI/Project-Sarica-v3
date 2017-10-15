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
    private final String uuid;
    private final String path;
    private Network network;

    public Agent(String uuid, String database)
    {
        this.uuid = uuid;
        path = database + File.separatorChar + uuid;
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

    public String getUUID()
    {
        return uuid;
    }
}
