package me.ci.sarica.util;

public class ThreadWorker implements Runnable
{
	private ThreadOwner owner;
	private Thread thread;
	private boolean running = true;

	public ThreadWorker(ThreadOwner owner)
	{
		this.owner = owner;

		thread = new Thread(this);
		thread.start();
	}

	public void stop()
	{
		running = false;
	}

	public void joinThread()
	{
		thread.join();
		thread = null;
	}

	public void run()
	{
		while (running)
		{
			Runnable task = owner.next();
			if (task != null)
				task.run();
			else
			{
				try { Thread.sleep(1);
				}catch(Exception exception){}
			}
		}
	}
}
