package me.ci.sarica.games;

public abstract class GameBase implements GameEnvironment
{
    protected int pixelsX;
    protected int pixelsY;
    protected boolean rgb;
    protected int audioDuration;
    protected int hertz;
    protected int textureSampleSize;

    public void initSight(int pixelsX, int pixelsY, boolean rgb)
    {
        this.pixelsX = pixelsX;
        this.pixelsY = pixelsY;
        this.rgb = rgb;
    }

    public void updatePixelData(float[] inputs)
    {
        // Fill inputs with complete black
        for (int i = 0; i < inputs.length; i++)
            inputs[i] = 0f;
    }

    public void initSound(int audioDuration, int hertz)
    {
        this.audioDuration = audioDuration;
        this.hertz = hertz;
    }

    public void updateSoundData(float[] inputs)
    {
        // Fill inputs with complete silence
        for (int i = 0; i < inputs.length; i++)
            inputs[i] = 0f;
    }

    public void initFeeling(int textureSampleSize)
    {
        this.textureSampleSize = textureSampleSize;
    }

    public void updateFeeling(float[] inputs)
    {
        // Fill inputs with complete numbness
        for (int i = 0; i < inputs.length; i++)
            inputs[i] = 0f;
    }

    public void updateTexture(float[] inputs)
    {
        // Fill inputs with complete numbness
        for (int i = 0; i < inputs.length; i++)
            inputs[i] = 0f;
    }

    public void initCommunication(int sentenceVectorCount, int vectorSize)
    {

    }

    public void updateCommunicationReceived(float[] inputs)
    {
        // Fill inputs with complete numbness
        for (int i = 0; i < inputs.length; i++)
            inputs[i] = 0f;
    }

    public void updateCommunicationSent(float[] outputs)
    {
        // Do nothing
    }

    public void initAction(Joystick joystick)
    {

    }

    public void loadGameData()
    {

    }

    public void disposeGameData()
    {

    }

    public void initalizeRender()
    {

    }
}
