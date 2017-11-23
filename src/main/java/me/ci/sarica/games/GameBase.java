package me.ci.sarica.games;

import me.ci.sarica.terminal.Terminal;

public abstract class GameBase implements GameEnvironment
{
    protected AgentSensorySettings settings;

    public void initGameData(AgentSensorySettings settings)
    {
        this.settings = settings;
    }

    public void updatePixelData(float[] inputs)
    {
        // Fill inputs with complete black
        for (int i = 0; i < inputs.length; i++)
            inputs[i] = 0f;
    }

    public void updateSoundData(float[] inputs)
    {
        // Fill inputs with complete silence
        for (int i = 0; i < inputs.length; i++)
            inputs[i] = 0f;
    }

    public void updateFeeling(float[] inputs)
    {
        // Fill inputs with complete numbness
        for (int i = 0; i < inputs.length; i++)
            inputs[i] = 0f;
    }

    public void updateTexture(float[] inputs)
    {
        // Fill inputs with complete numbness
        for (int i = 0; i < inputs.length; i++)
            inputs[i] = 0f;
    }

    public void updateCommunicationReceived(float[] outputs)
    {
        // Do nothing
    }

    public void updateCommunicationSent(float[] inputs)
    {
        // Fill inputs with complete silence
        for (int i = 0; i < inputs.length; i++)
            inputs[i] = 0f;
    }

    public void initializeRender()
    {
        // Game is assumed to not have a render mode
        Terminal.logVerbose("Game Base", "Game manager has requested a render window, however none are available.");
    }
}
