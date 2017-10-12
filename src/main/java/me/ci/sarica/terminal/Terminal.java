package me.ci.sarica.terminal;

import java.io.File;
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
        while (running)
        {
            synchronized (messageQueue)
            {
                empty = messageQueue.isEmpty();
            }

            if (empty)
            {
                try
                { Thread.sleep(1); }
                catch (InterruptedException e)
                { e.printStackTrace(); }

                continue;
            }

            LogMessage message;

            synchronized (messageQueue)
            {
                message = messageQueue.removeFirst();
            }

            String levelName = getLogLevelName(message.level);
            String timeStamp = getTimeStamp(message.time);
            String text = String.format("[%s][%s][%s] %s", levelName, timeStamp, message.sender, message.message);

            if (message.level >= logConsoleLevel)
                logToConsole(text);
            if (message.level >= logAgentLevel)
                logToAgent(text);
            if (message.level >= logFileLevel)
                logToFile(text);

            discardLogMessage(message);
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
