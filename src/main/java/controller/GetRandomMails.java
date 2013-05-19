package controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import util.Config;

public class GetRandomMails implements Runnable
{
	private static final Random generator = new Random();
	private static final DefaultHttpClient httpclient = new DefaultHttpClient();
	private static final ResponseHandler<String> responseHandler = new BasicResponseHandler();
	
	private static final ConcurrentHashMap<Integer, HashSet<String>> mailKeys = new ConcurrentHashMap<>();

	private final ScheduledExecutorService threadPool;

	public GetRandomMails(final ScheduledExecutorService threadPool)
	{
		this.threadPool = threadPool;
	}

	@Override
	public void run()
	{
		if (threadPool.isShutdown() || threadPool.isTerminated())
		{
			return;
		}

		// Read mail by random key
		final Integer[] mailKeySets = GetRandomMails.mailKeys.keySet().toArray(new Integer[0]);
		final int mailKeySetsLength = mailKeySets.length;
		if (mailKeySetsLength > 0)
		{
			final int receiverID = mailKeySets[generator.nextInt(mailKeySetsLength)];
			final HashSet<String> mailKeys = GetRandomMails.mailKeys.get(receiverID);
			int mailKeysIndex = generator.nextInt(mailKeys.size());
			Iterator<String> mailKeysIterator = mailKeys.iterator();
			while (--mailKeysIndex > 0)
			{
				mailKeysIterator.next();
			}
			final HttpGet httpGet = new HttpGet("http://localhost:8080/inazuma-mail/api/mail/" + mailKeysIterator.next());
			try
			{
				final String responseBody = httpclient.execute(httpGet, responseHandler);
				System.out.println(responseBody);
			}
			catch (IOException e)
			{
			}
			finally
			{
				httpGet.releaseConnection();
			}
		}

		if (!threadPool.isShutdown() && !threadPool.isTerminated())
		{
			threadPool.schedule(this, Config.CREATION_DELAY, Config.CREATION_TIMEUNIT);
		}
	}

	public void addMailKeys(final int receiverID, final HashSet<String> mailKeys)
	{
		GetRandomMails.mailKeys.put(receiverID, mailKeys);
	}
}
