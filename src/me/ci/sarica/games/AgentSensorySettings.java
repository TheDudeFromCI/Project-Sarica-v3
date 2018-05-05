package me.ci.sarica.games;

public class AgentSensorySettings
{
    public final int pixelsX;
    public final int pixelsY;
    public final boolean rgb;
    public final int audioDuration;
    public final int hertz;
    public final int textureSampleSize;
    public final int sentenceVectorCount;
    public final int wordVectorSize;
    public final Joystick joystick;

    public AgentSensorySettings(int pixelsX, int pixelsY, boolean rgb, int audioDuration, int hertz,
                                int textureSampleSize, int sentenceVectorCount, int wordVectorSize, Joystick joystick)
    {
        this.pixelsX = pixelsX;
        this.pixelsY = pixelsY;
        this.rgb = rgb;
        this.audioDuration = audioDuration;
        this.hertz = hertz;
        this.textureSampleSize = textureSampleSize;
        this.sentenceVectorCount = sentenceVectorCount;
        this.wordVectorSize = wordVectorSize;
        this.joystick = joystick;
    }

    @Override
    public String toString()
    {
        return String.format("Agent Sensory Settings:\n  Screen Size: %d x %d; Color: %b\n  Audion Duration: %d; " +
                                 "Hertz: %d\n  Texture Sample Size: %d\n  Sentence Vector Count: %d; Word Vector " +
                                 "Size: %d", pixelsX, pixelsY, rgb, audioDuration, hertz, textureSampleSize,
                                 sentenceVectorCount, wordVectorSize);
    }
}
