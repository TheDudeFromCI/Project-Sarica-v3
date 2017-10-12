package me.ci.sarica.terminal;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by thedudefromci on 10/9/17.
 *
 * This class oversees all input and output from the terminal. Most of the communication with
 * the agent as well as debug information is passed through this class where it is logged to
 * both console, and a log file.
 */
public class Terminal implements Runnable
{
    public static final int VERBOSE = 0;
    public static final int DEBUG = 1;
    public static final int NORMAL = 2;
    public static final int WARNING = 3;
    public static final int ERROR = 4;

    public static Terminal Instance;

    public static void logMessage(int level, String sender, String message)
    {
        if (Instance == null)
            Instance = new Terminal();
        Instance.log(level, sender, message);
    }

    public static void logVerbose(String sender, String message)
    {
        logMessage(VERBOSE, sender, message);
    }

    public static void logDebug(String sender, String message)
    {
        logMessage(DEBUG, sender, message);
    }

    public static void logNormal(String sender, String message)
    {
        logMessage(NORMAL, sender, message);
    }

    public static void logWarning(String sender, String message)
    {
        logMessage(WARNING, sender, message);
    }

    public static void logError(String sender, String message)
    {
        logMessage(ERROR, sender, message);
    }

    public static void logError(String sender, Throwable exception)
    {
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        logError(sender, sw.toString());
    }

    private final LinkedList<LogMessage> messageQueue;
    private final LinkedList<LogMessage> pool;
    private final String path;
    private int logConsoleLevel = DEBUG;
    private int logFileLevel = VERBOSE;
    private int logAgentLevel = NORMAL;
    private Thread logThread;
    private boolean running = true;
    private SimpleDateFormat format;
    private Date date;

    public Terminal()
    {
        if (Instance != null)
            Instance.shutdown();
        Instance = this;

        path = System.getProperty("user.dir") + File.separatorChar + "log";
        messageQueue = new LinkedList<LogMessage>();
        pool = new LinkedList<LogMessage>();
        format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss z");
        date = new Date();

        logThread = new Thread(this);
        logThread.setName("Logger");
        logThread.start();

        log(VERBOSE, "Logger", "Starting up logger.");
    }

    public void shutdown()
    {
        log(VERBOSE, "Logger", "Shutting down logger.");
        running = false;
        try
        {
            logThread.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        logThread = null;
    }

    public boolean isClosed()
    {
        return logThread == null;
    }

    public int getConsoleLogLevel()
    {
        return logConsoleLevel;
    }

    public int getAgentLogLevel()
    {
        return logAgentLevel;
    }

    public int getFileLogLevel()
    {
        return logFileLevel;
    }

    public void setConsoleLogLevel(int level)
    {
        logConsoleLevel = level;

        String levelName = getLogLevelName(level);
        log(WARNING, "Logger", "Changed console logging level to " + levelName + ".");
    }

    private String getLogLevelName(int level)
    {
        switch (level)
        {
            case VERBOSE:
                return "VERBOSE";
            case DEBUG:
                return "DEBUG";
            case NORMAL:
                return "NORMAL";
            case WARNING:
                return "WARNING";
            case ERROR:
                return "ERROR";

            default:
                return "UNDEFINED";
        }
    }

    public void setAgentLogLevel(int level)
    {
        logAgentLevel = level;

        String levelName = getLogLevelName(level);
        log(WARNING, "Logger", "Changed agent logging level to " + levelName + ".");
    }

    public void setFileLogLevel(int level)
    {
        logFileLevel = level;

        String levelName = getLogLevelName(level);
        log(WARNING, "Logger", "Changed file logging level to " + levelName + ".");
    }

    public void log(int level, String sender, String text)
    {
        if (level < logConsoleLevel && level < logAgentLevel && level < logFileLevel)
            return;

        LogMessage message = logMessageInstance();
        message.sender = sender;
        message.message = text;
        message.level = level;
        message.time = System.currentTimeMillis();

        queueLogMessage(message);
    }

    private void queueLogMessage(LogMessage message)
    {
        synchronized (messageQueue)
        {
            messageQueue.addLast(message);
        }
    }

    private LogMessage logMessageInstance()
    {
        synchronized (pool)
        {
            if (pool.isEmpty())
                return new LogMessage();
            return pool.removeFirst();
        }
    }

    private void discardLogMessage(LogMessage message)
    {
        synchronized (pool)
        {
            pool.addLast(message);
        }
    }

    private String getTimeStamp(long time)
    {
        date.setTime(time);
        return format.format(date);
    }

    public void run()
    {
        boolean empty;
        LogMessage message;

        while (running)
        {
            while (true)
            {
                synchronized (messageQueue)
                {
                    if (empty = messageQueue.isEmpty())
                        break;
                    message = messageQueue.removeFirst();
                }

                String levelName = getLogLevelName(message.level);
                String timeStamp = getTimeStamp(message.time);
                String text = String.format("[%s][%s][%s] %s", timeStamp, levelName, message.sender, message.message);

                if (message.level >= logConsoleLevel)
                    logToConsole(text);
                if (message.level >= logAgentLevel)
                    logToAgent(text);
                if (message.level >= logFileLevel)
                    logToFile(text);

                discardLogMessage(message);
            }

            if (empty)
            {
                try
                { Thread.sleep(1); }
                catch (InterruptedException e)
                { e.printStackTrace(); }
            }
        }
    }

    private void logToConsole(String text)
    {
        System.out.println(text);
    }

    private void logToAgent(String text)
    {
        // TODO Log to agent
    }

    private void logToFile(String text)
    {
        // TODO Log to file
    }
}
