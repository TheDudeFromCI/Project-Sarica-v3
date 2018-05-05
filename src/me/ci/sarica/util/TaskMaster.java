package me.ci.sarica.util;

import java.lang.Runnable;

public interface TaskMaster
{
	Runnable buildNextTask();
}
