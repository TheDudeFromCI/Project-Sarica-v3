package me.ci.sarica;

import me.ci.sarica.agent.AgentManager;
import me.ci.sarica.terminal.Terminal;

/**
 * Created by thedudefromci on 10/9/17.
 *
 * This class is for the purpose of starting the simulation, along with processing any program
 * arguments that could effect the simulation.
 */
public class ProjectSarica
{
    public static void main(String[] args)
    {
        Terminal.Instance = new Terminal();
        Terminal.Instance.setConsoleLogLevel(Terminal.DEBUG);

        Terminal.logDebug("System", "Starting Project Sarica v3.");

        try
        {
            Terminal.logVerbose("System", "Creating agent manager.");
            AgentManager manager = new AgentManager();
        }
        catch(Exception exception)
        {
            Terminal.logError("System", "Uncaught exception thrown!");
            Terminal.logError("System", exception);
        }
        finally
        {
            Terminal.logDebug("System", "Closing Project Sarica v3.");
            Terminal.Instance.shutdown();
        }
    }
}
