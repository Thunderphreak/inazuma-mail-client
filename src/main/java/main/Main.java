package main;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import util.Config;
import util.NamedThreadFactory;
import controller.GetMailIDsFromRandomUser;
import controller.GetRandomMails;
import controller.CreateRandomMails;


public class Main
{
	public static void main(String[] args)
	{
		final long runtime = Config.RUNTIME;

		// Configure thread pools
		ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(2, new NamedThreadFactory("ScheduledPool"));
		GetRandomMails getMails = new GetRandomMails(threadPool);
		threadPool.submit(new GetMailIDsFromRandomUser(threadPool, getMails));
		threadPool.submit(getMails);

		System.out.println("Running for " + runtime + " ms...");
		ScheduledExecutorService threadPoolCreation = Executors.newScheduledThreadPool(10, new NamedThreadFactory("MailCreation"));
		for (int i = 0; i < Config.CREATION_JOBS; ++i)
		{
			threadPoolCreation.submit(new CreateRandomMails(threadPoolCreation));
		}
		
		// Wait until runtime is over
		try
		{
			Thread.sleep(runtime);
		}
		catch (InterruptedException e)
		{
		}
		
		// Shutdown of thread pool
		System.out.println("Shutting down thread pool...");
		threadPool.shutdown();
		threadPoolCreation.shutdown();
		try
		{
			threadPool.awaitTermination(60, TimeUnit.SECONDS);
			threadPoolCreation.awaitTermination(60, TimeUnit.SECONDS);
		}
		catch (InterruptedException e)
		{
		}
		System.out.println("Done!\n");
	}
}
