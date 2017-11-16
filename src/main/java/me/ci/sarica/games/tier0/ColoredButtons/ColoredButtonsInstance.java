package me.ci.sarica.games.tier0.ColoredButtons;

import me.ci.sarica.games.Joystick;
import me.ci.sarica.terminal.Terminal;

public class ColoredButtonsInstance
{
    private final int RED = 0;
    private final int GREEN = 1;
    private final int BLUE = 2;
    private final int YELLOW = 3;

    private final Joystick joystick;
    private final boolean rgb;
    private int color;
    private int timer;
    private boolean rewardAgent;
    private boolean hurtAgent;
    private int tries = 0;
    private int correct = 0;

    ColoredButtonsInstance(Joystick joystick, boolean rgb)
    {
        this.joystick = joystick;
        this.rgb = rgb;
        randomColor();
    }

    private void randomColor()
    {
        color = (int)(Math.random() * 4);
    }

    public void update()
    {
        rewardAgent = false;
        hurtAgent = false;

        boolean a = joystick.holdingAButton();
        boolean b = joystick.holdingBButton();
        boolean x = joystick.holdingXButton();
        boolean y = joystick.holdingYButton();

        int held = 0;
        if (a) held++;
        if (b) held++;
        if (x) held++;
        if (y) held++;

        if (held == 0)
        {
            timer++;

            if (timer >= 30)
            {
                hurtAgent = true;
                timer = 0;
                tries++;
                Terminal.logNormal("Colored Buttons", "Agent has failed to choose an option for 30 frames. " +
                                                          "Punishing agent.");
            }

            return;
        }

        tries++;
        timer = 0;

        if (held != 1)
        {
            Terminal.logNormal("Colored Buttons", "Agent has attempted to cheat by choosing multiple options. " +
                                                      "Punishing agent.");
            hurtAgent = true;
            return;
        }

        String option = "";
        switch (color)
        {
            case RED:
                if (a) rewardAgent = true;
                else hurtAgent = true;
                break;
            case GREEN:
                if (b) rewardAgent = true;
                else hurtAgent = true;
                break;
            case BLUE:
                if (x) rewardAgent = true;
                else hurtAgent = true;
                break;
            case YELLOW:
                if (y) rewardAgent = true;
                else hurtAgent = true;
                break;
        }

        if (rewardAgent)
            correct++;

        String action = rewardAgent ? "correctly" : "incorrectly";
        Terminal.logNormal("Colored Buttons", String.format("Agent has %s chosen %s.", action, option));
    }

    public void updateFeeling(float[] inputs)
    {
        inputs[0] = rewardAgent ? 1f : 0f;
        inputs[1] = hurtAgent ? 1f : 0f;
        inputs[2] = 1f;

        rewardAgent = false;
        hurtAgent = false;
    }

    public void updatePixelData(float[] inputs)
    {
        if (rgb)
        {
            for (int i = 0; i < inputs.length; i += 3)
            {
                inputs[i * 3 + 0] = (color == RED || color == YELLOW) ? 1f : 0f;
                inputs[i * 3 + 1] = (color == GREEN || color == YELLOW) ? 1f : 0f;
                inputs[i * 3 + 2] = (color == BLUE) ? 1f : 0f;
            }
        }
        else
        {
            for (int i = 0; i < inputs.length; i++)
                inputs[i] = color / 3f;
        }
    }

    public boolean isGameComplete()
    {
        return tries >= 300;
    }

    public float getFinalScore()
    {
        return (float) correct / tries;
    }
}
