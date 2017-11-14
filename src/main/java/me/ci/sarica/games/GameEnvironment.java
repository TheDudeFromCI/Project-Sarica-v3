package me.ci.sarica.games;

public interface GameEnvironment
{
    /**
     * Load any required data to run this game.
     */
    void loadGameData();

    /**
     * Dispose of any data associated with this game.
     */
    void disposeGameData();

    /**
     * Initialize render component for this game. If this is not called, the game is assumed to run from
     * command line only. If this is called, a visual window should be rendered to the screen.
     */
    void initalizeRender();

    /**
     * This method is called to initialize the expected agent input sizes for the visual senses. The game should be
     * rendered with this screen size, and in either black and white or full color.
     *
     * @param pixelsX - The horizontal screen size in pixels.
     * @param pixelsY - The vertical screen size in pixels.
     * @param rgb - If true, the image should be rendered in full color. Otherwise it should be rendered in black and
     *            white.
     */
    void initSight(int pixelsX, int pixelsY, boolean rgb);

    /**
     * This method is called every agent frame to update the visual input data. The expected format for input
     * information is
     *
     * for black and white inputs:
     * input[pixelX * screenSizeY + pixelY] = getPixel(pixelX, pixelY)
     *
     * and for full color inputs:
     * input[(pixelX * screenSizeY + pixelY) * 3 + 0] = getPixel(pixelX, pixelY).r
     * input[(pixelX * screenSizeY + pixelY) * 3 + 1] = getPixel(pixelX, pixelY).g
     * input[(pixelX * screenSizeY + pixelY) * 3 + 2] = getPixel(pixelX, pixelY).b
     *
     * @param inputs - The float array of neural inputs for visual sensory.
     */
    void updatePixelData(float[] inputs);

    /**
     * This method is called to initialize the expected agent audio input senses. This tells the game environment the
     * format for audio inputs.
     *
     * @param audioDuration - The time in milliseconds each chunk audio is expected.
     * @param hertz - The number of audio samples to take for each millisecond.
     */
    void initSound(int audioDuration, int hertz);

    /**
     * This method is called every agent frame to update the latest audio input sames. If more time has passed since
     * the previous frame than the audio duration allows, then that information is skipped. The expected format for
     * the input information is
     *
     * input[ms * ] = getAudioSample()
     *
     * @param inputs -
     */
    void updateSoundData(float[] inputs);

    void initFeeling(int textureSampleSize);
    void updateFeeling(float[] inputs); // 1 Pleasure, 1 Pain, 1 Health
    void updateTexture(float[] inputs);

    void initCommunication(int sentenceVectorCount, int vectorSize);
    void updateCommunicationReceived(float[] inputs);
    void updateCommunicationSent(float[] outputs);

    void initAction(Joystick joystick);
}
