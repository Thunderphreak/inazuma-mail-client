package main;

public class Main
{
	/*
	public static void main(String[] args)
	{
		final long runtime = Config.RUNTIME;
		final CouchbaseClient client = ConnectionManager.getConnection();

		// Startup mail storage threads
		MailStorage mailStorageQueue = new MailStorage(client, Config.STORAGE_THREADS, Config.MAX_RETRIES);
		
		// Configure thread pools
		ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(2, new NamedThreadFactory("ScheduledPool"));
		//threadPool.submit(new ClusterStatusJob(threadPool, client));

		System.out.println("Creating mails for " + runtime + " ms...");
		ScheduledExecutorService threadPoolCreation = Executors.newScheduledThreadPool(10, new NamedThreadFactory("MailCreation"));
		for (int i = 0; i < Config.CREATION_JOBS; ++i)
		{
			threadPoolCreation.submit(new MailCreationJob(threadPoolCreation, mailStorageQueue));
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
			e.printStackTrace();
		}
		System.out.println("Done!\n");
		
		// Shutdown storage threads
		System.out.println("Shutting down storage threads...");
		mailStorageQueue.shutdown();
		mailStorageQueue.awaitShutdown();
		System.out.println("Done!\n");

		// Statistics
		(new MailCheckJob()).run();
		System.out.println();
		
		// Shutdown of connection manager
		System.out.println("Shutting down ConnectionManager...");
		ConnectionManager.shutdown();
		System.out.println("Done!\n");
	}
	*/
}
