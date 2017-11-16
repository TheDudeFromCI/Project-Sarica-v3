package me.ci.sarica.games;

public interface GameEnvironment
{
    void loadGameData();
    void disposeGameData();
    void initGameData(AgentSensorySettings settings);

    void initializeRender();

    void updatePixelData(float[] inputs);
    void updateSoundData(float[] inputs);
    void updateFeeling(float[] inputs); // 1 Pleasure, 1 Pain, 1 Health
    void updateTexture(float[] inputs);
    void updateCommunicationReceived(float[] outputs);
    void updateCommunicationSent(float[] inputs);

    void updateGameState();
    boolean isGameComplete();
    float getFinalScore();
    String getName();
}
