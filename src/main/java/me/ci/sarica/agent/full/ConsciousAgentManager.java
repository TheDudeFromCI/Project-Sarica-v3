package me.ci.sarica.agent.full;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by TheDudeFromCI on 10/18/2017.
 *
 * This agent manager class is designed for the purpose of managing and evolving full neural networks, of conscious
 * agents.
 */
public class ConsciousAgentManager
{
    private final Comparator<FullNN> agentSorter;
    private final FullNN[] agents;
    private int generation = 0;

    public ConsciousAgentManager(int populationSize, int inputsNeurons, int hiddenNeurons, int outputNeurons)
    {
        agents = new FullNN[populationSize];
        for (int i = 0; i < agents.length; i++)
            agents[i] = new FullNN(inputsNeurons, hiddenNeurons, outputNeurons);

        agentSorter = new Comparator<FullNN>()
        {
            public int compare(FullNN o1, FullNN o2)
            {
                return Float.compare(o1.getScore(), o2.getScore());
            }
        };
    }

    public int getGeneration()
    {
        return generation;
    }

    public FullNN getAgent(int index)
    {
        return agents[index];
    }

    public int getPopulationSize()
    {
        return agents.length;
    }

    public void nextGeneration()
    {
        Arrays.sort(agents, agentSorter);

        int half = agents.length / 2;
        for (int i = 0; i < half; i++)
            agents[i] = agents[i + half].reproduce(0.05f, 0.4f);
        generation++;
    }
}
