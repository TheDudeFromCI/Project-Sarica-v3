package me.ci.sarica.agent;

import me.ci.sarica.terminal.history.LineGraph;

public class BackPropagation
{
    private int iterationsCompleted;
    private float learningRate;
    private float learningLoss;
    private float trainingInputNoise;
	private Matrix[] layers;
	private Matrix[] bias;
	private int inputs;
	private int outputs;

	public void attachWeightMatrix(Matrix[] layers)
	{
		this.layers = layers;
		inputs = layers[0].getRows();
		outputs = layers[layers.length - 1].getCols();
	}

	public void attachBiasMatrix(Matrix[] bias)
	{
		this.bias = bias;
	}

    public void addIteration()
    {
        iterationsCompleted++;
        learningRate *= learningLoss;
    }

    public void setTrainingInputNoise(float noise)
    {
        trainingInputNoise = noise;
    }

    public float getTrainingInputNoise()
    {
        return trainingInputNoise;
    }

    public void resetIterations()
    {
        iterationsCompleted = 0;
    }

    public float getLearningRate()
    {
        return learningRate;
    }

    public float getLearningLoss()
    {
        return learningLoss;
    }

    public void setLearningRate(float rate)
    {
        learningRate = rate;
    }

    public void setLearningLoss(float loss)
    {
        learningLoss = loss;
    }

    public void train(ClassificationDatabase database, int sampleCount, int iterations, LineGraph graph)
    {
        if (database.getInputCount() != inputs)
            throw new IllegalArgumentException("Database input count does not match network input count!");
        if (database.getOutputCount() != outputs)
            throw new IllegalArgumentException("Database output count does not match network output count!");

        if (sampleCount == -1)
	        sampleCount = database.getExampleCount();
        sampleCount = Math.min(sampleCount, database.getExampleCount());

        Matrix x = new Matrix(sampleCount, database.getInputCount());
        Matrix y = new Matrix(sampleCount, database.getOutputCount());

        if (sampleCount == database.getExampleCount())
        {
            for (int i = 0; i < x.getRows(); i++)
            {
                ClassifierExample ex = database.getExample(i);
                for (int n = 0; n < inputs; n++)
                    x.setValue(i, n, ex.getInput(n));
                for (int n = 0; n < outputs; n++)
                    y.setValue(i, n, ex.getOutput(n));
            }

            train(x, y, iterations, graph);
        }
        else
        {
            int itrRemain = iterations;
            int subItr = iterations / sampleCount;

            for (int j = 0; j <= subItr; j++)
            {
                for (int i = 0; i < x.getRows(); i++)
                {
                    ClassifierExample ex = database.randomExample();
                    for (int n = 0; n < inputs; n++)
                        x.setValue(i, n, ex.getInput(n));
                    for (int n = 0; n < outputs; n++)
                        y.setValue(i, n, ex.getOutput(n));
                }

                int steps = Math.min(itrRemain, sampleCount);
                train(x, y, steps, graph);
                itrRemain -= steps;

                if (itrRemain == 0)
                    break;
            }
        }
    }

	public void train(Matrix x, Matrix y, int iterations)
    {
        train(x, y, iterations, null);
    }

    public void train(Matrix x, Matrix y, int iterations, LineGraph graph)
    {
        Matrix[] l = new Matrix[layers.length + 1];
        Matrix[] error = new Matrix[layers.length];
        Matrix[] delta = new Matrix[layers.length];
        Matrix[] delBi = new Matrix[layers.length];

        int lastLayer = layers.length - 1;
        int layerValueLayer = l.length - 1;

        for (int itr = 0; itr < iterations; itr++)
        {
            l[0] = x.add(noiseMatrix(x.getRows(), x.getCols(), 0.5f));
            for (int i = 1; i < l.length; i++)
			{
				l[i] = l[i - 1].dot(layers[i - 1]);

				if (bias != null)
					l[i] = l[i].addRowVector(bias[i - 1]);

				l[i] = l[i].sigmoid();
			}

            error[lastLayer] = y.sub(l[layerValueLayer]);
            delta[lastLayer] = error[lastLayer].mul(l[layerValueLayer].sigmoidDeriv());
            delBi[lastLayer] = error[lastLayer].collapseToColVector();

            float mE = error[lastLayer].meanError();
            if (graph != null) graph.addValue(mE);
            if (iterationsCompleted % 100 == 0)
                System.out.println("Iteration " + iterationsCompleted + ": " + mE);

            for (int i = layers.length - 2; i >= 0; i--)
            {
                error[i] = delta[i + 1].dot(layers[i + 1].transpose());
                delta[i] = error[i].mul(l[i + 1].sigmoidDeriv());

				if (bias != null)
                	delBi[i] = error[i].collapseToColVector();
            }

            for (int i = 0; i < layers.length; i++)
            {
                layers[i] = layers[i].add(l[i].transpose().dot(delta[i]).mul(learningRate));

				if (bias[i] != null)
                	bias[i] = bias[i].add(delBi[i].mul(learningRate));
            }

			addIteration();
        }
    }

    private Matrix noiseMatrix(int rows, int cols, float strength)
    {
        Matrix m = new Matrix(rows, cols);

        for (int i = 0; i < m.getValueCount(); i++)
            m.setValueByIndex(i, ((float)Math.random() * 2f - 1f) * strength);

        return m;
    }
}
