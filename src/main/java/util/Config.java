package util;

import java.util.concurrent.TimeUnit;

public class Config
{
	public static final long RUNTIME = TimeUnit.SECONDS.toMillis(30);
	
	public static final int CREATION_JOBS = 20;
	public static final int CREATION_DELAY = 10;
	public static final TimeUnit CREATION_TIMEUNIT = TimeUnit.MICROSECONDS;

	public static final int STORAGE_THREADS = 10;
	public static final int MAX_RETRIES = 50;
	
	public static final int MIN_USER = 1;
	public static final int MAX_USER = 1000;
	
	public static final String[] SUBJECTS = {"Hello", "Urgent!", "Test", "Couchbase [UK] 2013", "Invitation", "Plans for Dinner?"};
	public static final String[] BODIES = {"World...", "Got a package for you. Come and get it!", "Boring test mail", "See ya in London?", "It's my birthday, will you come for party?", "Huuuuuuuuuuuuuuuuungry"};
}
