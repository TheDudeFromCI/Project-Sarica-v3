package test;

import me.ci.sarica.agent.Agent;
import me.ci.sarica.agent.AgentManager;

/**
 * Created by TheDudeFromCI on 10/15/2017.
 *
 * This is a test class for observing is the agent manager is capable of learning to preform a simple task through
 * genetic algorithms.
 */
public class GALearningTest
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

        AgentManager solver = new AgentManager(512, 1, randomData.length);
        for (int i = 0; i < 1000; i++)
        {
            System.out.println("Begining generation " + solver.getGeneration());

            float best = Float.NEGATIVE_INFINITY;
            float worst = Float.POSITIVE_INFINITY;
            float avg = 0f;

            for (int j = 0; j < solver.getPopulationSize(); j++)
            {
                Agent agent = solver.getAgent(j);
                agent.load();

                agent.setInput(0, 1f);
                agent.solve();

                float score = 0f;
                for (int k = 0; k < randomData.length; k++)
                    score -= Math.abs(agent.getOutput(k) - randomData[k]);

                score -= agent.getNetwork().getTotalNeuronCount() * 0.01f;

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
