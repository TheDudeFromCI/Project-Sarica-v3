package me.ci.sarica.agent;

public class NeuralNetwork
{
	private final int[] layerSizes;
    private final Matrix[] layers;
    private final int inputs;
    private final int outputs;
    private Matrix[] bias;
	private BackPropagation backProp;
	private GeneticAlgorithms ga;

    public NeuralNetwork(int... layerSizes)
    {
        if (layerSizes.length < 2)
            throw new IllegalArgumentException("Neural Network cannot have less than two layers!");

		this.layerSizes = layerSizes;

        inputs = layerSizes[0];
        outputs = layerSizes[layerSizes.length - 1];

        layers = new Matrix[layerSizes.length - 1];
        for (int i = 0; i < layers.length; i++)
            layers[i] = buildLayerMatrix(layerSizes[i], layerSizes[i + 1]);
    }

    public void buildBiasNeurons()
    {
        bias = new Matrix[layerSizes.length - 1];
        for (int i = 0; i < bias.length; i++)
        {
            bias[i] = new Matrix(1, layerSizes[i + 1]);
            for (int j = 0; j < bias[i].getCols(); j++)
                bias[i].setValueByIndex(j, ((float)Math.random() * 2f - 1f) / (float)Math.sqrt(layerSizes[i]));
        }

		if (backProp != null)
			backProp.attachBiasMatrix(bias);
    }

    public void addBackPropagationSupport(BackPropagation backProp)
    {
		this.backProp = backProp;
		backProp.attachWeightMatrix(layers);

		if (bias != null)
			backProp.attachBiasMatrix(bias);

		backProp.initialize(this);
    }

	public void addGeneticAlgorithms(GeneticAlgorithms ga)
	{
		ga.attachWeightMatrix(layers);
		if (bias != null)
			ga.attachBiasMatrix(bias);

		this.ga = ga;
		ga.initialize(this);
	}

	public GeneticAlgorithms getGeneticAlgorithms()
	{
		return ga;
	}

    public int getInputs()
    {
        return inputs;
    }

    public int getOutputs()
    {
        return outputs;
    }

	public BackPropagation getBackPropagation()
	{
		return backProp;
	}

    private Matrix buildLayerMatrix(int inputs, int outputs)
    {
        Matrix m = new Matrix(inputs, outputs);

        for (int i = 0; i < m.getValueCount(); i++)
            m.setValueByIndex(i, ((float)Math.random() * 2f - 1f) / (float)Math.sqrt(inputs));

        return m;
    }

    public Matrix run(Matrix in)
    {
        Matrix[] l = new Matrix[layers.length + 1];

        l[0] = in;
        for (int i = 1; i < l.length; i++)
		{
			l[i] = l[i - 1].dot(layers[i - 1]);

			if (bias != null)
				l[i] = l[i].addRowVector(bias[i - 1]);

			l[i] = l[i].sigmoid();
		}

        return l[l.length - 1];
    }
}
