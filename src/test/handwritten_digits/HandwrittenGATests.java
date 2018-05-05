package test.handwritten_digits;

import me.ci.sarica.terminal.Terminal;
import me.ci.sarica.agent.ClassificationDatabase;
import me.ci.sarica.agent.NeuralNetwork;
import me.ci.sarica.agent.NeuralNetworkBuilder;
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
				.addGeneticAlgorithms(512, 1f, 0.95f).build();

			LineGraph graph = new LineGraph("Handwritten Digit Training [GA] Error", 0, 0);
			LineGraph accuracy = new LineGraph("Handwritten Digit Training [GA] Accuracy", 0, 1);

			double score = 0;
			float min = Float.POSITIVE_INFINITY, max = Float.NEGATIVE_INFINITY;
			Matrix m = new Matrix(1, 28 * 28);
			int i = 0;
			while (true)
			{
				i++;
				float error = 0f;

				for (int it = 0; it < 100; it++)
				{
					ClassifierExample ex = database.randomExample();

					for (int p = 0; p < 28 * 28; p++)
						m.setValue(0, p, ex.getInput(p));

					Matrix out = nn.run(m);

					for (int p = 0; p < 10; p++)
						error -= (out.getValue(0, p) - ex.getOutput(p)) * (out.getValue(0, p) - ex.getOutput(p));
				}

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
					min = Float.POSITIVE_INFINITY;
					max = Float.NEGATIVE_INFINITY;
				}

				if (i % (nn.getGeneticAlgorithms().getPopulation() * 10) == 0)
					test(database, nn, accuracy);
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

	private static void test(ClassificationDatabase database, NeuralNetwork nn, LineGraph accuracy)
	{
		nn.getGeneticAlgorithms().loadBest();
		Matrix x = new Matrix(database.getTestCount(), database.getInputCount());
		Matrix y = new Matrix(database.getTestCount(), database.getOutputCount());
		for (int r = 0; r < x.getRows(); r++)
		{
			for (int c = 0; c < x.getCols(); c++)
				x.setValue(r, c, database.getTest(r).getInput(c));
			for (int c = 0; c < y.getCols(); c++)
				y.setValue(r, c, database.getTest(r).getOutput(c));
		}

		Matrix e = nn.run(x);
		Matrix rounded = e.round();
		e = e.sub(y);

		int correct = 0;
		int wrong = 0;
		counter:for (int i = 0; i < rounded.getRows(); i++)
		{
			for (int c = 0; c < rounded.getCols(); c++)
				if (Math.abs(rounded.getValue(i, c) - y.getValue(i, c)) > 0.001f)
				{
					wrong++;
					continue counter;
				}
			correct++;
		}

		float percentCorrect = (float)correct / rounded.getRows();
		accuracy.addValue(percentCorrect);

		Terminal.logNormalf("GA Training Tracker", "Test accuracy: %.03f (Missed %d)", percentCorrect, wrong);
		nn.getGeneticAlgorithms().loadCurrent();
	}
}
