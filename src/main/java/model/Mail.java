package model;

public abstract class Mail
{
	private final MailType mailType;
	private final int senderID;
	private final int receiverID;
	private final long created;
	
	private transient String key;

	protected Mail(final MailType mailType, final int senderID, final int receiverID)
	{
		this.mailType = mailType;
		this.senderID = senderID;
		this.receiverID = receiverID;
		this.created = System.currentTimeMillis() / 1000;
	}
	
	public void setKey(final String key)
	{
		this.key = key;
	}

	public String getKey()
	{
		return key.toString();
	}

	public MailType getMailType()
	{
		return mailType;
	}

	public int getSenderID()
	{
		return senderID;
	}

	public int getReceiverID()
	{
		return receiverID;
	}

	public long getCreated()
	{
		return created;
	}
}
