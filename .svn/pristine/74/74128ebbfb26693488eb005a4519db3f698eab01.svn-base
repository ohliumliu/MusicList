package com.ifun361.musiclist.media;

public class PlayerStates {
	public static final int READY_TO_PREPER = 1;
	public static final int READY_TO_PLAY = 2;
	public static final int PLAYING = 3;
	public static final int STOPPED = 4;
	public int playerState = STOPPED;

	public int get() {
		return playerState;
	}

	public void set(int state) {
		playerState = state;
	}

	public synchronized boolean isReadyToPlay() {
		return playerState == PlayerStates.READY_TO_PLAY;
	}

	public synchronized boolean isPlaying() {
		return playerState == PlayerStates.PLAYING;
	}

	public synchronized boolean isStopped() {
		return playerState == PlayerStates.STOPPED;
	}

}
