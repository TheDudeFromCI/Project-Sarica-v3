package test.handwritten_digits;

import me.ci.sarica.terminal.Terminal;
import me.ci.sarica.agent.ClassificationDatabase;
import me.ci.sarica.agent.NeuralNetwork;
import me.ci.sarica.agent.NeuralNetworkBuilder;
import me.ci.sarica.terminal.history.BackPropTrainingTracker;
import me.ci.sarica.agent.Matrix;
import me.ci.sarica.terminal.history.LineGraph;
import me.ci.sarica.agent.ClassifierExample;

public class HandwrittenGATests
{
	public static void main(String[] args)
	{
		Terminal.Instance = new Terminal();
        Terminal.Instance.setConsoleLogLevel(Terminal.VERBOSE);

		try
        {
			Terminal.logNormal("Tests", "Launching test 'Handwritten Digit Recognition, GA'.");

			ClassificationDatabase database = HandwrittenDigitBase.loadDatabase();
			NeuralNetwork nn = new NeuralNetworkBuilder(28 * 28, 128, 64, 32, 10).addBias()
				.addGeneticAlgorithms(2048, 1f).build();

			LineGraph graph = new LineGraph("Handwritten Digit Training [GA]");

			double score = 0;
			float min = Float.MAX_VALUE, max = Float.MIN_VALUE;
			Matrix m = new Matrix(1, 28 * 28);
			int i = 0;
			while (true)
			{
				i++;
				ClassifierExample ex = database.randomExample();

				for (int p = 0; p < 28 * 28; p++)
					m.setValue(0, p, ex.getInput(p));

				Matrix out = nn.run(m);

				float error = 0f;
				for (int p = 0; p < 10; p++)
					error -= (out.getValue(0, p) - ex.getOutput(p)) * (out.getValue(0, p) - ex.getOutput(p));
				nn.getGeneticAlgorithms().scoreAgent(error);
				score += error;
				min = Math.min(min, error);
				max = Math.max(max, error);

				if (i % nn.getGeneticAlgorithms().getPopulation() == 0)
				{
					score /= nn.getGeneticAlgorithms().getPopulation();
					graph.addValue((float)score);
					Terminal.logVerbosef("Tests", "Completed generation %d.", nn.getGeneticAlgorithms().getGeneration());
					Terminal.logVerbosef("Tests", "  Average Score: %.03f", score);
					Terminal.logVerbosef("Tests", "  Min/Max Score: %.03f/%.03f", min, max);

					score = 0f;
					min = Float.MAX_VALUE;
					max = Float.MIN_VALUE;
				}
			}
        }
        catch(Exception exception)
        {
            // Handle uncaught exceptions
            Terminal.logError("System", "Uncaught exception thrown!");
            Terminal.logError("System", exception);
        }
        finally
        {
            // Shutdown terminal safely
            Terminal.logDebug("System", "Exiting application.");
            Terminal.Instance.shutdown();
        }
	}
}
