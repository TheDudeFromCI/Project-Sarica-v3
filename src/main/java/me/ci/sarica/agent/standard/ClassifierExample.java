package me.ci.sarica.agent.standard;

public class ClassifierExample
{
    private final float[] inputs;
    private final float[] outputs;

    public ClassifierExample(int inputs, int outputs)
    {
        this.inputs = new float[inputs];
        this.outputs = new float[outputs];
    }

    public void setInput(int index, float value)
    {
        inputs[index] = value;
    }

    public float getInput(int index)
    {
        return inputs[index];
    }

    public void setOutput(int index, float value)
    {
        outputs[index] = value;
    }

    public float getOutput(int index)
    {
        return outputs[index];
    }

    public int getInputCount()
    {
        return inputs.length;
    }

    public int getOutputCount()
    {
        return outputs.length;
    }
}
