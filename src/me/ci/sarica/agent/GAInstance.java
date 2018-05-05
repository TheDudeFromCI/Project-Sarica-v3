package me.ci.sarica.agent;

import me.ci.sarica.agent.Matrix;

public class GAInstance implements Comparable<GAInstance>
{
	private Matrix[] layers;
	private Matrix[] bias;
	private float score;

	public GAInstance(Matrix[] layers, Matrix[] bias, float mutation)
	{
		this.layers = new Matrix[layers.length];
		for (int i = 0; i < layers.length; i++)
		{
			this.layers[i] = new Matrix(layers[i].getRows(), layers[i].getCols());
			for (int j = 0; j < layers[i].getValueCount(); j++)
				this.layers[i].setValueByIndex(j, layers[i].getValueByIndex(j) + ((float)Math.random() * 2f - 1f) * mutation);
		}

		if (bias != null)
		{
			this.bias = new Matrix[bias.length];
			for (int i = 0; i < bias.length; i++)
			{
				this.bias[i] = new Matrix(bias[i].getRows(), bias[i].getCols());
				for (int j = 0; j < bias[i].getValueCount(); j++)
					this.bias[i].setValueByIndex(j, bias[i].getValueByIndex(j) + ((float)Math.random() * 2f - 1f) * mutation);
			}
		}
	}

	public GAInstance(GAInstance instance, float mutation)
	{
		this(instance.layers, instance.bias, mutation);
	}

	public Matrix[] getLayers()
	{
		return layers;
	}

	public Matrix[] getBias()
	{
		return bias;
	}

	public float getScore()
	{
		return score;
	}

	public void setScore(float score)
	{
		this.score = score;
	}

	public void loadInstance(Matrix[] layers, Matrix[] bias)
	{
		for (int i = 0; i < layers.length; i++)
			for (int j = 0; j < layers[i].getValueCount(); j++)
				layers[i].setValueByIndex(j, this.layers[i].getValueByIndex(j));

		if (bias != null)
		{
			for (int i = 0; i < bias.length; i++)
				for (int j = 0; j < bias[i].getValueCount(); j++)
					bias[i].setValueByIndex(j, this.bias[i].getValueByIndex(j));
		}
	}

	public int compareTo(GAInstance other)
	{
		return Float.compare(score, other.score);
	}
}
