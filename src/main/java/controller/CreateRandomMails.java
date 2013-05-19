package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;

import model.Mail;
import model.MailTrade;
import model.MailType;
import model.MailUser;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import util.Config;

import com.google.gson.Gson;

public class CreateRandomMails implements Runnable
{
	private static final Gson gson = new Gson();
	private static final MailType[] mailTypes = MailType.values();
	private static final Random generator = new Random();
	private static final DefaultHttpClient httpclient = new DefaultHttpClient();
	private static final ResponseHandler<String> responseHandler = new BasicResponseHandler();
	
	private final ScheduledExecutorService threadPool;

	public CreateRandomMails(final ScheduledExecutorService threadPool)
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

		// Create random mail document
		final int senderID = Config.MIN_USER + generator.nextInt(Config.MAX_USER);
		final int receiverID = Config.MIN_USER + generator.nextInt(Config.MAX_USER);
		final MailType mailType = mailTypes[1 + generator.nextInt(mailTypes.length - 1)];
		Mail mail = null;
		switch (mailType)
		{
			case USER:
			{
				final int textIndex = 1 + generator.nextInt(Config.SUBJECTS.length - 1);
				mail = new MailUser(senderID, receiverID, Config.SUBJECTS[textIndex], Config.BODIES[textIndex]);
				break;
			}
			case TRADE:
			{
				final ArrayList<Long> items = new ArrayList<Long>();
				final int itemCount = 1 + generator.nextInt(5);
				for (int i = 0; i < itemCount; ++i)
				{
					items.add(Math.abs(generator.nextLong()));
				}
				mail = new MailTrade(senderID, receiverID, items);
				break;
			}
			default:
				break;
		}

		// Put mail on storage queue
		final HttpPost httpPost = new HttpPost("http://localhost:8080/inazuma-mail/api/mail/" + receiverID);
		try
		{
			final ArrayList<NameValuePair> nvps = new ArrayList<>();
			nvps.add(new BasicNameValuePair("document", gson.toJson(mail)));
			nvps.add(new BasicNameValuePair("created", String.valueOf(mail.getCreated())));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			final String responseBody = httpclient.execute(httpPost, responseHandler);
			if (responseBody == null)
			{
				System.err.println("Could not insert mail " + mail + " for receiver " + receiverID);
			}
		}
		catch (IOException e)
		{
		}
		finally
		{
			httpPost.releaseConnection();
		}

		if (!threadPool.isShutdown() && !threadPool.isTerminated())
		{
			threadPool.schedule(this, Config.CREATION_DELAY, Config.CREATION_TIMEUNIT);
		}
	}
}
