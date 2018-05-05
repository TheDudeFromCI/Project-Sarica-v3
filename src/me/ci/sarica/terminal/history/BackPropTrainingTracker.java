package me.ci.sarica.terminal.history;

import me.ci.sarica.agent.BackPropTrainingListener;
import me.ci.sarica.agent.ClassificationDatabase;
import me.ci.sarica.agent.BackPropagation;
import me.ci.sarica.agent.Matrix;
import me.ci.sarica.terminal.Terminal;

public class BackPropTrainingTracker implements BackPropTrainingListener
{
	private final LineGraph error;
	private final LineGraph accuracy;
	private final ClassificationDatabase database;
	private float smoothedMeanError;
	private int iterationIndex;

	public BackPropTrainingTracker(ClassificationDatabase database)
	{
		error = new LineGraph("Error", 0f, 0.5f);
		accuracy = new LineGraph("Accuracy", 0f, 1f);
		this.database = database;

		Terminal.logVerbose("BP Training Tracker", "Created back propagation training tracker.");
	}

	public void onTrainingIterationComplete(BackPropagation backProp)
	{
		iterationIndex++;
		float meanError = backProp.getLastMeanError();
		error.addValue(meanError);
		smoothedMeanError += meanError;

		if (iterationIndex % 25 == 0)
		{
			Terminal.logVerbosef("BP Training Tracker", "Completed iteration %d. Average Mean Error: %.03f", iterationIndex, smoothedMeanError / 25f);
			smoothedMeanError = 0f;
		}
	}

	public void onTrainingBatchComplete(BackPropagation backProp)
	{
		Matrix x = new Matrix(database.getTestCount(), database.getInputCount());
		Matrix y = new Matrix(database.getTestCount(), database.getOutputCount());
		for (int r = 0; r < x.getRows(); r++)
		{
			for (int c = 0; c < x.getCols(); c++)
				x.setValue(r, c, database.getTest(r).getInput(c));
			for (int c = 0; c < y.getCols(); c++)
				y.setValue(r, c, database.getTest(r).getOutput(c));
		}

		Matrix e = backProp.getNeuralNetwork().run(x);
		Matrix rounded = e.round();
		e = e.sub(y);

		int correct = 0;
		counter:for (int i = 0; i < rounded.getRows(); i++)
		{
			for (int c = 0; c < rounded.getCols(); c++)
				if (Math.abs(rounded.getValue(i, c) - y.getValue(i, c)) > 0.001f)
					continue counter;
			correct++;
		}

		float percentCorrect = (float)correct / rounded.getRows();
		accuracy.addValue(percentCorrect);

		Terminal.logNormalf("BP Training Tracker", "Completed training batch. Test accuracy: %.03f", percentCorrect);
	}
}
