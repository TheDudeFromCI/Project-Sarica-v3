package me.ci.sarica.games;

import me.ci.sarica.games.tier0.ColoredButtons.ColoredButtons;
import me.ci.sarica.terminal.Terminal;

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
    private int gamesPerTier = 10;
    private float scoreToPass = 0.8f;

    public GameManager(AgentSensorySettings settings)
    {
        Terminal.logVerbose("Game Manager", "Initialized game agent sensory settings.\n" + settings);
        for (int i = 0; i < games.length; i++)
            for (int j = 0; j < games[i].length; j++)
                games[i][j].initGameData(settings);
    }

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

    public float runLoop(GameEnvironment game, GamePlayingAgent agent)
    {
        game.loadGameData();

        while(!game.isGameComplete())
        {
            game.updateGameState();

            game.updatePixelData(agent.getPixelInputData());
            game.updateSoundData(agent.getSoundInputData());
            game.updateFeeling(agent.getFeelingInputData());
            game.updateTexture(agent.getTextureInputData());
            game.updateCommunicationReceived(agent.getCommunicationReceievedData());
            game.updateCommunicationSent(agent.getCommunicationSentData());

            agent.updateAgentState();
        }

        float score = game.getFinalScore();
        game.disposeGameData();

        return score;
    }

    public int getGamesPerTier()
    {
        return gamesPerTier;
    }

    public void setGamesPerTier(int gamesPerTier)
    {
        Terminal.logNormal("Game Manager", String.format("Adjusted games per tier to %d.", gamesPerTier));
        this.gamesPerTier = gamesPerTier;
    }

    public float getScoreToPass()
    {
        return scoreToPass;
    }

    public void setScoreToPass(float scoreToPass)
    {
        Terminal.logNormal("Game Manager", String.format("Adjusted score to pass to %f.", scoreToPass));
        this.scoreToPass = scoreToPass;
    }

    public float fullTestAgent(GamePlayingAgent agent)
    {
        Terminal.logNormal("Game Manager", String.format("Began full game tier test for agent %s.", agent.getUUID()));
        float fullScore = 0f;
        for (int i = 0; i < games.length; i++)
        {
            Terminal.logNormal("Game Manager", String.format("Starting tier %d.", i));
            float totalScore = 0f;
            for (int j = 0; j < gamesPerTier; j++)
            {
                GameEnvironment game = loadRandomGame(i);
                Terminal.logNormalf("Game Manager", "Starting game %d/$d, %s.", j+1, gamesPerTier, game.getName());
                float s = runLoop(loadRandomGame(i), agent);

                Terminal.logNormalf("Game Manager", "Completed game with a final score of %.02f.", s);
                totalScore += s;
                fullScore += s;

                Terminal.logVerbosef("Game Manager", "Current passing score: %.02f. (Required: %.02f)",
                    totalScore / (j + 1), scoreToPass);
                Terminal.logVerbosef("Game Manager", "Current full score: %.02f.", fullScore);
            }
            totalScore /= gamesPerTier;

            if (totalScore < scoreToPass)
            {
                Terminal.logVerbose("Game Manager", "Agent has failed to meet minimum tier requirements.");
                break;
            }
            Terminal.logNormal("Game Manager", "Agent has passed mimimum tire requirements. Advancing to next tier" +
                                                    ".");
        }

        Terminal.logNormal("Game Manager", "All tiers completed.");
        return fullScore;
    }
}
