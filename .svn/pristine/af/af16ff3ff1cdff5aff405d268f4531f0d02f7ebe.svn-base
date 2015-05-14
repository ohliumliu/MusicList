package com.ifun361.musiclist.media;

import android.os.Handler;

import java.util.concurrent.Executor;

import com.ifun361.musiclist.MusicListApplication;

public class RequestExecutorDelivery {

	private final Executor mResponsePoster;

	public RequestExecutorDelivery(final Handler handler) {
		mResponsePoster = new Executor() {
			@Override
			public void execute(Runnable command) {
				handler.post(command);
			}
		};
	}

	public void postResponse(String name, char[] charArr) {
		mResponsePoster.execute(new ResponseDeliveryRunnable(name, charArr));
	}

	private class ResponseDeliveryRunnable implements Runnable {

		private final String name;
		private final char[] charArr;

		public ResponseDeliveryRunnable(String name, char[] charArr) {
			this.name = name;
			this.charArr = charArr;
		}

		@Override
		public void run() {
			System.out.println("=============== save char[] " + name + "-"
					+ String.copyValueOf(charArr));
			PlayUtils.updateChunkDataMap(MusicListApplication.getAPP(), name,
					charArr);
		}
	}
}
