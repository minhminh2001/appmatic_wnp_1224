package com.whitelabel.app.task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class INITExecutor implements Executor {

	private static final int CORE_POOL_SIZE = 5;
	private static final int MAXIMUM_POOL_SIZE = 256;
	private static final int KEEP_ALIVE = 10;

	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "DBExecutor #" + mCount.getAndIncrement());
		}
	};

	private static class SingletonHolder {
		static final INITExecutor INSTANCE = new INITExecutor();
	}

	public static INITExecutor getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private final ThreadPoolExecutor mThreadPoolExecutor;

	private INITExecutor() {
		this(CORE_POOL_SIZE);
	}

	private INITExecutor(int poolSize) {
		BlockingQueue<Runnable> mPoolWorkQueue = new INITBlockingQueue<Runnable>();
		mThreadPoolExecutor = new ThreadPoolExecutor(poolSize,
				MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.MINUTES,
				mPoolWorkQueue, sThreadFactory);
	}

	public void clearTask(final Runnable r){
		mThreadPoolExecutor.remove(r);
	}

	@Override
	public void execute(final Runnable r) {
		mThreadPoolExecutor.execute(r);
	}
}
