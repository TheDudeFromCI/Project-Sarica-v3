package me.ci.sarica.util;

public class ThreadOwner
{
	private TaskMaster taskMaster;
	private LinkedList<Runnable> tasks;
	private boolean running = true;
	private ThreadWorker[] workers;

	public ThreadOwner(TaskMaster taskMaster, int workers)
	{
		this.taskMaster = taskMaster;
		tasks = new LinkedList<>();
		this.workers = new ThreadWorker[workers];
		for (int i = 0; i < workers; i++)
			this.workers[i] = new ThreadWorker(this);
	}

	public Runnable next()
	{
		synchronized (tasks)
		{
			if (tasks.isEmpty())
				return null;
			return tasks.removeFirst();
		}
	}

	public void stop()
	{
		running = false;

		for (int i = 0; i < workers.length; i++)
			workers[i].stop();
		for (int i = 0; i < workers.length; i++)
			workers[i].joinThread();
	}

	public void begin()
	{
		while (running)
		{
			synchronized (tasks)
			{
				int count = tasks.size();
				while (count < 10)
				{
					count++;

					Runnable task = taskMaster.buildNextTask();

					if (task == null)
						break;

					tasks.add(task);
				}
			}

			try { Thread.sleep(1);
			}catch(Exception exception){}
		}
	}
}
