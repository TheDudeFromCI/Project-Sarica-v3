package test;

import me.ci.sarica.agent.Agent;
import me.ci.sarica.agent.AgentManager;
import me.ci.sarica.agent.ConsciousAgentManager;
import me.ci.sarica.agent.FullNN;

/**
 * Created by TheDudeFromCI on 10/18/2017.
 *
 * This class is here to test the learning operation of the FullNN class.
 */
public class GAFullNNTest
{
    public static void main(String[] args)
    {
        System.out.println("Starting test.\n");

        float[] randomData = new float[8];
        for (int i = 0; i < randomData.length; i++)
            randomData[i] = (float)Math.random();

        float lastBest = Float.NEGATIVE_INFINITY;
        float lastWorst = Float.POSITIVE_INFINITY;
        float lastAvg = 0f;

        ConsciousAgentManager solver = new ConsciousAgentManager(64, 1, 0, randomData.length);
        for (int i = 0; i < 1000; i++)
        {
            System.out.println("Beginning generation " + solver.getGeneration());

            float best = Float.NEGATIVE_INFINITY;
            float worst = Float.POSITIVE_INFINITY;
            float avg = 0f;

            for (int j = 0; j < solver.getPopulationSize(); j++)
            {
                FullNN agent = solver.getAgent(j);
                agent.setInput(0, 1f);
                agent.runIteration();

                float score = 0f;
                for (int k = 0; k < randomData.length; k++)
                    score -= Math.abs(agent.getOutput(k) - randomData[k]);
                agent.setScore(score);

                best = Math.max(best, score);
                worst = Math.min(worst, score);
                avg += score;
            }
            avg /= solver.getPopulationSize();
            solver.nextGeneration();

            System.out.println("  Results:");
            System.out.println("    Best: " + best + " (+" + (best - lastBest) + ")");
            System.out.println("    Worst: " + worst + " (+" + (worst - lastWorst) + ")");
            System.out.println("    Average: " + avg + " (+" + (avg - lastAvg) + ")");
            System.out.println();

            lastBest = best;
            lastWorst = worst;
            lastAvg = avg;
        }

        System.out.println("Simulation complete.");
    }
}
