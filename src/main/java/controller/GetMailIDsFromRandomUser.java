package controller;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import util.Config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GetMailIDsFromRandomUser implements Runnable
{
	private static final Random generator = new Random();
	private static final DefaultHttpClient httpclient = new DefaultHttpClient();
	private static final ResponseHandler<String> responseHandler = new BasicResponseHandler();
	private static final Type typeOfMap = new TypeToken<HashMap<String, Long>>() {}.getType();

	private final ScheduledExecutorService threadPool;
	private final GetRandomMails getMails;

	public GetMailIDsFromRandomUser(final ScheduledExecutorService threadPool, final GetRandomMails getMails)
	{
		this.threadPool = threadPool;
		this.getMails = getMails;
	}

	@Override
	public void run()
	{
		if (threadPool.isShutdown() || threadPool.isTerminated())
		{
			return;
		}

		// Read mail keys from a random user
		final int receiverID = Config.MIN_USER + generator.nextInt(Config.MAX_USER);
		final HttpGet httpGet = new HttpGet("http://localhost:8080/inazuma-mail/api/mails/" + receiverID);
		try
		{
			final String responseBody = httpclient.execute(httpGet, responseHandler);
			final HashMap<String, Long> mailKeys = new Gson().fromJson(responseBody, typeOfMap);
			if (mailKeys.size() > 0)
			{
				getMails.addMailKeys(receiverID, new HashSet<String>(mailKeys.keySet()));
			}
		}
		catch (IOException e)
		{
		}
		finally
		{
			httpGet.releaseConnection();
		}
		
		if (!threadPool.isShutdown() && !threadPool.isTerminated())
		{
			threadPool.schedule(this, 10, TimeUnit.MILLISECONDS);
		}
	}
}
