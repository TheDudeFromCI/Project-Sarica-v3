package me.ci.sarica.agent.matrix_based;

import me.ci.sarica.agent.standard.ClassificationDatabase;
import me.ci.sarica.agent.standard.ClassifierExample;
import me.ci.sarica.agent.standard.StandardNNTrainingProgress;

public class NeuralNetwork
{
    private final Matrix[] layers;
    private final int inputs;
    private final int outputs;

    public NeuralNetwork(int... layerSizes)
    {
        if (layerSizes.length < 2)
            throw new IllegalArgumentException("Neural Network cannot have less than two layers!");

        inputs = layerSizes[0];
        outputs = layerSizes[layerSizes.length - 1];

        layers = new Matrix[layerSizes.length - 1];
        for (int i = 0; i < layers.length; i++)
            layers[i] = buildLayerMatrix(layerSizes[i] + (i == 0 ? 1 : 0), layerSizes[i + 1]);
    }

    public int getInputs()
    {
        return inputs;
    }

    public int getOutputs()
    {
        return outputs;
    }

    private Matrix buildLayerMatrix(int inputs, int outputs)
    {
        Matrix m = new Matrix(inputs, outputs);

        for (int i = 0; i < m.getValueCount(); i++)
            m.setValueByIndex(i, (float)Math.random() * 2f - 1f);

        return m;
    }

    public void train(ClassificationDatabase database, StandardNNTrainingProgress training)
    {
        if (database.getInputCount() != getInputs())
            throw new IllegalArgumentException("Database input count does not match network input count!");
        if (database.getOutputCount() != getOutputs())
            throw new IllegalArgumentException("Database output count does not match network output count!");

        Matrix x = new Matrix(database.getExampleCount(), database.getInputCount());
        Matrix y = new Matrix(database.getExampleCount(), database.getOutputCount());

        for (int i = 0; i < x.getRows(); i++)
        {
            ClassifierExample ex = database.getExample(i);
            for (int n = 0; n < getInputs(); n++)
                x.setValue(i, n, ex.getInput(n));
            for (int n = 0; n < getOutputs(); n++)
                y.setValue(i, n, ex.getOutput(n));
        }

        train(x, y, training.maxIterations);
    }

    public void train(Matrix x, Matrix y, int iterations)
    {
        // Append bias neuron to X
        {
            Matrix x2 = new Matrix(x.getRows(), x.getCols() + 1);
            for (int r = 0; r < x.getRows(); r++)
            {
                for (int c = 0; c < x.getCols(); c++)
                    x2.setValue(r, c, x.getValue(r, c));
                x2.setValue(r, x.getCols(), 1f);
            }
            x = x2;
        }

        Matrix[] l = new Matrix[layers.length + 1];
        Matrix[] error = new Matrix[layers.length];
        Matrix[] delta = new Matrix[layers.length];

        int lastLayer = layers.length - 1;
        int layerValueLayer = l.length - 1;

        for (int itr = 0; itr < iterations; itr++)
        {
            l[0] = x;
            for (int i = 1; i < l.length; i++)
                l[i] = sigmoid(dot(l[i - 1], layers[i - 1]));

            error[lastLayer] = sub(y, l[layerValueLayer]);
            delta[lastLayer] = mul(error[lastLayer], sigmoidDeriv(l[layerValueLayer]));

            if (itr % 50 == 0)
                System.out.println("Iteration " + itr + ": " + meanError(error[lastLayer]));

            for (int i = layers.length - 2; i >= 0; i--)
            {
                error[i] = dot(delta[i + 1], transpose(layers[i + 1]));
                delta[i] = mul(error[i], sigmoidDeriv(l[i + 1]));
            }

            for (int i = 0; i < layers.length; i++)
                layers[i] = add(layers[i], dot(transpose(l[i]), delta[i]));
        }
    }

    public Matrix run(Matrix in)
    {
        // Append bias neuron to X
        {
            Matrix x2 = new Matrix(in.getRows(), in.getCols() + 1);
            for (int r = 0; r < in.getRows(); r++)
            {
                for (int c = 0; c < in.getCols(); c++)
                    x2.setValue(r, c, in.getValue(r, c));
                x2.setValue(r, in.getCols(), 1f);
            }
            in = x2;
        }

        Matrix[] l = new Matrix[layers.length + 1];

        l[0] = in;
        for (int i = 1; i < l.length; i++)
            l[i] = sigmoid(dot(l[i - 1], layers[i - 1]));

        return l[l.length - 1];
    }

    private float meanError(Matrix a)
    {
        double f = 0f;

        for (int i = 0; i < a.getValueCount(); i++)
            f += Math.abs(a.getValueByIndex(i));

        return (float)(f / a.getValueCount());
    }

    public Matrix add(Matrix a, Matrix b)
    {
        if (a.getRows() != b.getRows() || a.getCols() != b.getCols())
            throw new IllegalArgumentException("Matrix sizes do not match!");

        Matrix c = new Matrix(a.getRows(), a.getCols());

        for (int row = 0; row < c.getRows(); row++)
            for (int col = 0; col < c.getCols(); col++)
                c.setValue(row, col, a.getValue(row, col) + b.getValue(row, col));

        return c;
    }

    private Matrix transpose(Matrix a)
    {
        Matrix c = new Matrix(a.getCols(), a.getRows());

        for (int row = 0; row < c.getRows(); row++)
            for (int col = 0; col < c.getCols(); col++)
                c.setValue(row, col, a.getValue(col, row));

        return c;
    }

    private Matrix dot(Matrix a, Matrix b)
    {
        if (a.getCols() != b.getRows())
            throw new IllegalArgumentException("Matrix sizes do not share side dimension!");

        Matrix c = new Matrix(a.getRows(), b.getCols());

        for (int row = 0; row < c.getRows(); row++)
            for (int col = 0; col < c.getCols(); col++)
            {
                float v = 0f;
                for (int j = 0; j < a.getCols(); j++)
                    v += a.getValue(row, j) * b.getValue(j, col);
                c.setValue(row, col, v);
            }

        return c;
    }

    private Matrix sub(Matrix a, Matrix b)
    {
        if (a.getRows() != b.getRows() || a.getCols() != b.getCols())
            throw new IllegalArgumentException("Matrix sizes do not match!");

        Matrix c = new Matrix(a.getRows(), a.getCols());

        for (int row = 0; row < a.getRows(); row++)
            for (int col = 0; col < a.getCols(); col++)
                c.setValue(row, col, a.getValue(row, col) - b.getValue(row, col));

        return c;
    }

    private Matrix mul(Matrix a, Matrix b)
    {
        if (a.getRows() != b.getRows() || a.getCols() != b.getCols())
            throw new IllegalArgumentException("Matrix sizes do not match!");

        Matrix c = new Matrix(a.getRows(), a.getCols());

        for (int row = 0; row < c.getRows(); row++)
            for (int col = 0; col < c.getCols(); col++)
                c.setValue(row, col, a.getValue(row, col) * b.getValue(row, col));

        return c;
    }

    private Matrix sigmoid(Matrix m)
    {
        Matrix c = new Matrix(m.getRows(), m.getCols());

        for (int i = 0; i < m.getValueCount(); i++)
            c.setValueByIndex(i, sigmoid(m.getValueByIndex(i)));

        return c;
    }

    private float sigmoid(float x)
    {
        return 1f / (1f + (float)Math.exp(-x));
    }

    private float sigmoidDeriv(float x)
    {
        return x * (1f - x);
    }

    private Matrix sigmoidDeriv(Matrix m)
    {
        Matrix c = new Matrix(m.getRows(), m.getCols());

        for (int i = 0; i < m.getValueCount(); i++)
            c.setValueByIndex(i, sigmoidDeriv(m.getValueByIndex(i)));

        return c;
    }
}
