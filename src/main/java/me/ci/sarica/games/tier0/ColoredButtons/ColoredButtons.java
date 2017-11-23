package me.ci.sarica.games.tier0.ColoredButtons;

import me.ci.sarica.games.AgentSensorySettings;
import me.ci.sarica.games.GameBase;
import me.ci.sarica.terminal.Terminal;

public class ColoredButtons extends GameBase
{
    private ColoredButtonsInstance instance;

    public void loadGameData()
    {
        Terminal.logNormal("Colored Buttons", "Loading game instance.");
        instance = new ColoredButtonsInstance(settings.joystick, settings.rgb);
    }

    public void disposeGameData()
    {
        Terminal.logNormal("Colored Buttons", "Disposing game instance.");
        instance = null;
    }

    @Override
    public void updatePixelData(float[] inputs)
    {
        if (instance == null)
            return;

        instance.updatePixelData(inputs);
    }

    @Override
    public void updateFeeling(float[] inputs)
    {
        if (instance == null)
            return;

        instance.updateFeeling(inputs);
    }

    public void updateGameState()
    {
        if (instance == null)
            return;

        instance.update();
    }

    public boolean isGameComplete()
    {
        if (instance == null)
            return true;

        return instance.isGameComplete();
    }

    public float getFinalScore()
    {
        if (instance == null)
            return 0f;

        return instance.getFinalScore();
    }

    public String getName()
    {
        return "Colored Buttons";
    }
}
