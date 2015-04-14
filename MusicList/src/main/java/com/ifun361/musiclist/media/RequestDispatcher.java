package com.ifun361.musiclist.media;

import java.util.concurrent.BlockingQueue;

import android.os.Process;
import android.os.SystemClock;

public class RequestDispatcher extends Thread {

	private final BlockingQueue<RequestTask> mQueue;
	private volatile boolean mQuit = false;
	private RequestTask mCurrentTask;
	private final RequestExecutorDelivery mDelivery;

	public RequestDispatcher(BlockingQueue<RequestTask> queue,
			RequestExecutorDelivery delivery) {
		this.mQueue = queue;
		this.mDelivery = delivery;
	}

	public void quit() {
		mQuit = true;
		interrupt();
	}

	@Override
	public void run() {
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
		while (true) {
			long startTimeMs = SystemClock.elapsedRealtime();
			RequestTask request;
			try {
				request = mQueue.take();
				mCurrentTask = request;
			} catch (InterruptedException e) {
				if (mQuit) {
					return;
				}
				continue;
			}
			try {
				request.addMarker("network-queue-take");

				if (request.isCanceled()) {
					request.addMarker("network-discard-cancelled");
					continue;
				}

				request.start();
				request.addMarker("network-queue-finish");

				mDelivery.postResponse(request.getName(), request.getCharArr());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				mCurrentTask = null;
			}
		}
	}

	public void cancelCurrentTask() {
		if (mCurrentTask != null)
			mCurrentTask.cancel();
	}
	
	public RequestTask getCurrentTask() {
		return mCurrentTask;
	}

}
