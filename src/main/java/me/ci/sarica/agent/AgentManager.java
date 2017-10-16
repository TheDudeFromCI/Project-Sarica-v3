package me.ci.sarica.agent;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by thedudefromci on 10/9/17.
 *
 * This class handles all of the agents being created and taught. Saving, loading, and instancing
 * agents is also done through this class. Because of the lengthy testing time required for each
 * agent, agents are kept out or RAM until needed to save memory.
 */
public class AgentManager
{
    private final Agent[] agents;
    private final String path;
    private final int inputs;
    private final int outputs;
    private float passbilityScore = 0.75f;
    private int generation;

    public AgentManager(int populationSize, int inputs, int outputs)
    {
        this.inputs = inputs;
        this.outputs = outputs;
        agents = new Agent[populationSize];
        path = System.getProperty("user.dir") + File.separatorChar + "agents";
        generateInitalPopulation();
        generation = 1;
    }

    public int getPopulationSize()
    {
        return agents.length;
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
        for (int i = 0; i < agents.length; i++)
            agents[i] = Agent.createAgent(path, inputs, outputs);
    }

    public Agent getAgent(int index)
    {
        return agents[index];
    }

    public void nextGeneration()
    {
        Arrays.sort(agents, new Comparator<Agent>()
        {
            public int compare(Agent a, Agent b)
            {
                return Float.compare(a.getScore(), b.getScore());
            }
        });

        int half = agents.length / 2;
        for (int i = 0; i < half; i++)
            agents[i] = agents[i + half].reproduce();
        generation++;
    }

    public int getGeneration()
    {
        return generation;
    }
}
