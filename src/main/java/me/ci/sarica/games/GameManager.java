package me.ci.sarica.games;

import me.ci.sarica.games.tier0.ColoredButtons.ColoredButtons;

/**
 * Created by thedudefromci on 10/9/17.
 *
 * This class handles loading and passing out games to various agents.
 */
public class GameManager
{
    private final GameEnvironment[][] games = {
        // Tier 0
        {
            new ColoredButtons()
        }
    };

    public GameEnvironment loadRandomGame(int tier)
    {
        return games[tier][(int)(Math.random() * games[tier].length)];
    }

    public int getTierCount()
    {
        return games.length;
    }

    public int getGameCountInTier(int tier)
    {
        return games[tier].length;
    }
}
