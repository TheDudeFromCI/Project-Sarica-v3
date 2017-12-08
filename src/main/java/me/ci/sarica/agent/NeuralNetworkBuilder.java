package me.ci.sarica.agent;

public class NeuralNetworkBuilder
{
    private int[] layerSizes;
    private boolean bias;
    private BackPropagation backPropagation;
	private GeneticAlgorithms ga;

    public NeuralNetworkBuilder(int... layerSizes)
    {
        if (layerSizes.length < 2)
            throw new IllegalArgumentException("Cannot have less than two neural network layers!");
        for (int i : layerSizes)
            if (i < 1)
                throw new IllegalArgumentException("Cannot have a network layer with less than 1 neuron!");

        this.layerSizes = layerSizes;
    }

    public NeuralNetworkBuilder addBias()
    {
        bias = true;
        return this;
    }

    public NeuralNetworkBuilder addBackPropagation(float learningRate, float learningLoss, float trainingInputNoise)
    {
        backPropagation = new BackPropagation();
        backPropagation.setLearningRate(learningRate);
        backPropagation.setLearningLoss(learningLoss);
        backPropagation.setTrainingInputNoise(trainingInputNoise);
        return this;
    }

	public NeuralNetworkBuilder addGeneticAlgorithms(int population, float learningRate, float learningLoss)
	{
		ga = new GeneticAlgorithms(population);
		ga.setLearningRate(learningRate);
		ga.setLearningLoss(learningLoss);
		return this;
	}

	public NeuralNetworkBuilder addBackPropListener(BackPropTrainingListener listener)
	{
		if (backPropagation == null)
			throw new IllegalArgumentException("Back Propagation not yet initialized!");

		backPropagation.addListener(listener);

		return this;
	}

    public NeuralNetwork build()
    {
        NeuralNetwork nn = new NeuralNetwork(layerSizes);

		if (bias)
			nn.buildBiasNeurons();
		if (backPropagation != null)
			nn.addBackPropagationSupport(backPropagation);
		if (ga != null)
			nn.addGeneticAlgorithms(ga);

        return nn;
    }
}
