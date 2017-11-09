package me.ci.sarica.agent;

import me.ci.sarica.agent.full.FullNN;

import java.util.ArrayList;

/**
 * Created by TheDudeFromCI on 10/19/2017.
 *
 * This class allows a conscious agent to receive and send information through a terminal.
 */
public class TerminalAgent
{
    private final ArrayList<String> log = new ArrayList<String>();
    private int agentBufferSize = 256;
    private int agentScrollPosition = 0;

    public void logMessage(String message)
    {
        log.add(message);

        if (agentScrollPosition == log.size() - 2 || log.size() == 1)
            agentScrollPosition++;
    }

    public String getLogMessageAt(int pos)
    {
        return log.get(pos);
    }

    public int getLogCount()
    {
        return log.size();
    }

    public int getAgentBufferSize()
    {
        return agentBufferSize;
    }

    public void setAgentBufferSize(int size)
    {
        agentBufferSize = size;
    }

    public int getRequiredInputCount()
    {
        return agentBufferSize * 8 + 1;
    }

    public int getRequiredOutputCount()
    {
        return agentBufferSize * 8 + 2;
    }

    public void fillAgentInputs(FullNN agent)
    {
        String entry = getLogMessageAt(agentScrollPosition);

        for (int i = 0; i < agentBufferSize; i++)
        {
            if (i >= entry.length())
            {
                for (int j = 0; j < 8; j++)
                    agent.setInput(i * 8 + j, 0f);
            }
            else
            {
                char c = entry.charAt(i);
                agent.setInput(i * 8 + 0, (c & 0x01) > 0 ? 1f : 0f);
                agent.setInput(i * 8 + 1, (c & 0x02) > 0 ? 1f : 0f);
                agent.setInput(i * 8 + 2, (c & 0x04) > 0 ? 1f : 0f);
                agent.setInput(i * 8 + 3, (c & 0x08) > 0 ? 1f : 0f);
                agent.setInput(i * 8 + 4, (c & 0x10) > 0 ? 1f : 0f);
                agent.setInput(i * 8 + 5, (c & 0x20) > 0 ? 1f : 0f);
                agent.setInput(i * 8 + 6, (c & 0x40) > 0 ? 1f : 0f);
                agent.setInput(i * 8 + 7, (c & 0x80) > 0 ? 1f : 0f);
            }
        }

        agent.setInput(agentBufferSize * 8, agentScrollPosition / (float)(log.size() - 1));
    }

    public void resetAgentPosition()
    {
        agentScrollPosition = Math.max(log.size() - 1, 0);
    }

    public String getAgentOutput(FullNN agent)
    {
        String s = "";

        for (int i = 0; i < agentBufferSize; i++)
        {
            char c = 0;
            c |= agent.getOutput(i * 8 + 0) >= 0.5f ? 1 : 0;
            c |= agent.getOutput(i * 8 + 1) >= 0.5f ? 2 : 0;
            c |= agent.getOutput(i * 8 + 2) >= 0.5f ? 4 : 0;
            c |= agent.getOutput(i * 8 + 3) >= 0.5f ? 8 : 0;
            c |= agent.getOutput(i * 8 + 4) >= 0.5f ? 16 : 0;
            c |= agent.getOutput(i * 8 + 5) >= 0.5f ? 32 : 0;
            c |= agent.getOutput(i * 8 + 6) >= 0.5f ? 64 : 0;
            c |= agent.getOutput(i * 8 + 7) >= 0.5f ? 128 : 0;

            if (c == 0)
                c = ' ';

            s += c;
        }

        boolean scrollUp = agent.getOutput(agentBufferSize * 8 + 0) >= 0.5f;
        boolean scrollDown = agent.getOutput(agentBufferSize * 8 + 1) >= 0.5f;

        if (scrollUp)
            agentScrollPosition = Math.max(0, agentScrollPosition - 1);
        if (scrollDown)
            agentScrollPosition = Math.min(log.size() - 1, agentScrollPosition + 1);

        s = s.trim();
        s = s.replace("\\s+", " ");

        return s;
    }
}
