package me.ci.sarica.games;

import java.util.UUID;

public interface GamePlayingAgent
{
    void updateAgentState();

    float[] getPixelInputData();
    float[] getSoundInputData();
    float[] getFeelingInputData();
    float[] getTextureInputData();
    float[] getCommunicationReceievedData();
    float[] getCommunicationSentData();

    UUID getUUID();
}
