package me.ci.sarica;

import me.ci.sarica.games.AgentSensorySettings;
import me.ci.sarica.games.GameManager;
import me.ci.sarica.games.Joystick;
import me.ci.sarica.terminal.Terminal;

/**
 * Created by thedudefromci on 10/9/17.
 *
 * This class is for the purpose of starting the simulation, along with processing any program
 * arguments that could effect the simulation.
 */
public class ProjectSarica
{
    @SuppressWarnings("unused")
	public static void main(String[] args)
    {
        // Initialize terminal and logging levels
        Terminal.Instance = new Terminal();
        Terminal.Instance.setConsoleLogLevel(Terminal.DEBUG);

        Terminal.logDebug("System", "Starting Project Sarica v3.");

        // Start main logic
        try
        {
            Joystick joystick = new Joystick();
            AgentSensorySettings sensorySettings = new AgentSensorySettings(20, 15, true, 0, 0, 0, 0, 0,
                                                                               joystick);
            GameManager gameManager = new GameManager(sensorySettings);

            // TODO Insert learning agent to train on.
        }
        catch(Exception exception)
        {
            // Handle uncaught exceptions
            Terminal.logError("System", "Uncaught exception thrown!");
            Terminal.logError("System", exception);
        }
        finally
        {
            // Shutdown terminal safely
            Terminal.logDebug("System", "Closing Project Sarica v3.");
            Terminal.Instance.shutdown();
        }
    }
}
