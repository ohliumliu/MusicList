package com.ifun361.musiclist.media;

import java.util.concurrent.PriorityBlockingQueue;

import android.os.Handler;
import android.os.Looper;

public class RequestQueue {

	private final PriorityBlockingQueue<RequestTask> mNetworkQueue = new PriorityBlockingQueue<RequestTask>();
	private RequestDispatcher[] mDispatchers;
	private final RequestExecutorDelivery mDelivery;

	public RequestQueue(int threadPoolSize) {
		mDispatchers = new RequestDispatcher[threadPoolSize];
		mDelivery = new RequestExecutorDelivery(new Handler(
				Looper.getMainLooper()));
	}

	public void start() {
		stop();
		for (int i = 0; i < mDispatchers.length; i++) {
			RequestDispatcher networkDispatcher = new RequestDispatcher(
					mNetworkQueue, mDelivery);
			mDispatchers[i] = networkDispatcher;
			networkDispatcher.start();
		}
	}

	public void stop() {
		for (int i = 0; i < mDispatchers.length; i++) {
			if (mDispatchers[i] != null) {
				mDispatchers[i].quit();
			}
		}
	}

	public void cancelAll() {
		if (mNetworkQueue != null)
			mNetworkQueue.clear();
	}

	public void cancelCurrentTask() {
		for (int i = 0; i < mDispatchers.length; i++) {
			mDispatchers[i].cancelCurrentTask();
		}
	}

	public RequestTask getCurrentRequestTask() {
		// TODO单线程使用，多线程时需要修改返回逻辑
		return mDispatchers[0].getCurrentTask();
	}

	public RequestTask add(RequestTask request) {
		request.setRequestQueue(this);
		request.addMarker("add-to-queue");
		System.out.println("add task");
		mNetworkQueue.add(request);
		return request;
	}

}
