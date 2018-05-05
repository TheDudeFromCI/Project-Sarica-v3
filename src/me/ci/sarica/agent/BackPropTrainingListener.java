package me.ci.sarica.agent;

public interface BackPropTrainingListener
{
	void onTrainingIterationComplete(BackPropagation backProp);
	void onTrainingBatchComplete(BackPropagation backProp);
}
