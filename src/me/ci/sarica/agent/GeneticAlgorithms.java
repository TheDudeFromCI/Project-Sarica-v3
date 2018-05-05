package me.ci.sarica.agent;

import java.util.Arrays;

public class GeneticAlgorithms
{
	private int generation;
	private float learningRate;
	private float learningLoss;
	private Matrix[] layers;
	private Matrix[] bias;
	private NeuralNetwork nn;
	private GAInstance[] instances;
	private int subGen;

	public GeneticAlgorithms(int population)
	{
		instances = new GAInstance[population];
	}

	public void initialize(NeuralNetwork nn)
	{
		this.nn = nn;

		for (int i = 0; i < instances.length; i++)
			instances[i] = new GAInstance(layers, bias, learningRate);
		instances[0].loadInstance(layers, bias);
	}

	public NeuralNetwork getNeuralNetwork()
	{
		return nn;
	}

	public void attachWeightMatrix(Matrix[] layers)
	{
		this.layers = layers;
		layers[0].getRows();
		layers[layers.length - 1].getCols();
	}

	public void attachBiasMatrix(Matrix[] bias)
	{
		this.bias = bias;
	}

	public void resetGeneration()
	{
  		generation = 0;
	subGen = 0;
	}

	public float getLearningRate()
	{
		return learningRate;
	}

	public void setLearningRate(float rate)
	{
		learningRate = rate;
	}

	public void setLearningLoss(float loss)
	{
		learningLoss = loss;
	}

	public int getGeneration()
	{
		return generation;
	}

	public int getPopulation()
	{
		return instances.length;
	}

	public void loadBest()
	{
		int best = -1;
		float score = Float.NEGATIVE_INFINITY;

		for (int i = 0; i < instances.length; i++)
		{
			if (instances[i].getScore() >= score)
			{
				best = i;
				score = instances[i].getScore();
			}
		}

		instances[best].loadInstance(layers, bias);
	}

	public void loadCurrent()
	{
		instances[subGen].loadInstance(layers, bias);
	}

	public void scoreAgent(float score)
	{
		instances[subGen++].setScore(score);

		if (subGen == instances.length)
		{
			subGen = 0;
			generation++;

			Arrays.sort(instances);

			int half = instances.length / 2;
			for (int i = 0; i < half; i++)
				instances[i] = new GAInstance(instances[i + half], learningRate);

			learningRate *= learningLoss;
		}

		instances[subGen].loadInstance(layers, bias);
	}
}
