package test.handwritten_digits;

import me.ci.sarica.agent.ClassificationDatabase;
import me.ci.sarica.agent.NeuralNetwork;
import me.ci.sarica.agent.NeuralNetworkBuilder;
import me.ci.sarica.terminal.history.BackPropTrainingTracker;
import me.ci.sarica.terminal.Terminal;

public class BasicBackpropagation
{
    public static void main(String[] args)
    {
		Terminal.Instance = new Terminal();
        Terminal.Instance.setConsoleLogLevel(Terminal.DEBUG);

        try
        {
			Terminal.logNormal("Tests", "Launching test 'Handwritten Digit Recognition'.");

			ClassificationDatabase database = HandwrittenDigitBase.loadDatabase();
			NeuralNetwork nn = new NeuralNetworkBuilder(28 * 28, 128, 64, 32, 10).addBias()
				.addBackPropagation(0.1f, 0.99995f, 0.5f).addBackPropListener(new BackPropTrainingTracker(database))
				.build();

	        for (int gen = 0; gen < 100; gen++)
	            nn.getBackPropagation().train(database, 100, 1000);
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
