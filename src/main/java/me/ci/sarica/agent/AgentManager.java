package me.ci.sarica.agent;

import java.io.File;

/**
 * Created by thedudefromci on 10/9/17.
 *
 * This class handles all of the agents being created and taught. Saving, loading, and instancing
 * agents is also done through this class. Because of the lengthy testing time required for each
 * agent, agents are kept out or RAM until needed to save memory.
 */
public class AgentManager
{
    private final int populationSize;
    private final String path;
    private float passbilityScore = 0.75f;
    private int agentIndex;

    public AgentManager()
    {
        this(100);
    }

    public AgentManager(int populationSize)
    {
        this.populationSize = populationSize;
        path = System.getProperty("user.dir") + File.separatorChar + "agents";
        generateInitalPopulation();
    }

    public int getPopulationSize()
    {
        return populationSize;
    }

    public float getPassbilityScore()
    {
        return passbilityScore;
    }

    public void setPassbilityScore(float score)
    {
        passbilityScore = score;
    }

    private void generateInitalPopulation()
    {
        // TODO Generate population
    }
}
