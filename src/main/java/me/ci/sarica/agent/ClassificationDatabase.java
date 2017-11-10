package me.ci.sarica.agent;

import java.util.ArrayList;

public class ClassificationDatabase
{
    private final ArrayList<ClassifierExample> examples = new ArrayList<ClassifierExample>();
    private final ArrayList<ClassifierExample> tests = new ArrayList<ClassifierExample>();
    private final int inputCount;
    private final int outputCount;
    private int sampleSize = 100;

    public ClassificationDatabase(int inputs, int outputs)
    {
        if (inputs < 1)
            throw new IllegalArgumentException("Inputs cannot have less than 1 neuron!");
        if (outputs < 1)
            throw new IllegalArgumentException("Outputs cannot have less than 1 neuron!");

        inputCount = inputs;
        outputCount = outputs;
    }

    public void addExample(ClassifierExample example)
    {
        if (example.getInputCount() != inputCount)
            throw new IllegalArgumentException("Example input count does not match database input count!");
        if (example.getOutputCount() != outputCount)
            throw new IllegalArgumentException("Example output count does not match database output count!");

        examples.add(example);
    }

    public int getSampleSize()
    {
        return sampleSize;
    }

    public void setSampleSize(int size)
    {
        if (sampleSize < 1)
            throw new IllegalArgumentException("Sample size cannot be less than 1!");

        sampleSize = size;
    }

    public int getExampleCount()
    {
        return examples.size();
    }

    public void clear()
    {
        examples.clear();
        tests.clear();
    }

    public int getTestCount()
    {
        return tests.size();
    }

    public void addTest(ClassifierExample test)
    {
        tests.add(test);
    }

    public ClassifierExample getExample(int index)
    {
        return examples.get(index);
    }

    public int getInputCount()
    {
        return inputCount;
    }

    public int getOutputCount()
    {
        return outputCount;
    }

    public ClassifierExample randomExample()
    {
        return examples.get((int)(Math.random() * examples.size()));
    }

    public ClassifierExample randomTest()
    {
        return tests.get((int)(Math.random() * tests.size()));
    }
}
