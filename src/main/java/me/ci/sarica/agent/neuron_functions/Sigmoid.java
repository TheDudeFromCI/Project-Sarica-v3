package me.ci.sarica.agent.neuron_functions;

/**
 * Created by TheDudeFromCI on 10/14/2017.
 *
 * This is the standard Sigmoid neuron activation function.
 */
public class Sigmoid implements ActivationFunction
{
    public float runFunction(float[] inputs, int count)
    {
        float x = 0f;
        for (int i = 0; i < count; i++)
            x += inputs[i];

        return 1f / (1f + (float)Math.pow(Math.E, -x));
    }
}
