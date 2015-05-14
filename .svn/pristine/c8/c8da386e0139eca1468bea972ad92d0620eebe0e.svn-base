package com.ifun361.musiclist.media;

import java.util.HashMap;

import android.text.TextUtils;

import com.ifun361.musiclist.MusicListApplication;
import com.ifun361.musiclist.media.Playlist.PlayBackMode;

public class PlayManager implements PlayerEngine, PlayerDownloadEngine {

	private RequestQueue mRequestQueue;
	private HashMap<String, char[]> chunkMap;
	private volatile char[] charArr;
	private PlayerEngine mPlayerEngine;
	private int mCurrentProgress = 0;

	private static PlayManager mPlayManager;

	private PlayManager() {
		mPlayerEngine = new PlayerEngineImpl();
		mPlayerEngine.setDownloadListener(this);
		// 初始化下载数据
		chunkMap = PlayUtils.getChunkDataMap(MusicListApplication.getAPP());
		mRequestQueue = new RequestQueue(1);
		mRequestQueue.start();
	}

	public static PlayManager getPlayManager() {
		if (mPlayManager == null)
			mPlayManager = new PlayManager();
		return mPlayManager;
		
		
		
		
		
	}

	public void onDestroy() {
	}

	private void addTask(long p, PlaylistEntry entry) {
		mRequestQueue.cancelCurrentTask();
		PlayUtils.checkChunkData(MusicListApplication.getAPP(), chunkMap,
				entry.getTrackName(), entry.getTrackSize());
		charArr = chunkMap.get(entry.getTrackName());
		long p1 = PlayUtils.getChunkDownloadStartSize(charArr);
		if (p1 >= 0) {
			RequestTask task = new RequestTask(entry.getTrackPath(),
					entry.getTrackLocalUrl(), entry.getTrackName(),
					p == -1 ? p1 : p, entry.getTrackSize(), charArr);
			mRequestQueue.add(task);
		}
	}

	@Override
	public void addTask(PlaylistEntry entry) {
		mRequestQueue.cancelCurrentTask();
		PlayUtils.checkChunkData(MusicListApplication.getAPP(), chunkMap,
				entry.getTrackName(), entry.getTrackSize());
		charArr = chunkMap.get(entry.getTrackName());
		long p1 = PlayUtils.getChunkDownloadStartSize(charArr);
		if (p1 >= 0) {
			RequestTask task = new RequestTask(entry.getTrackPath(),
					entry.getTrackLocalUrl(), entry.getTrackName(), p1,
					entry.getTrackSize(), charArr);
			mRequestQueue.add(task);
		}
	}

	@Override
	public Playlist getPlaylist() {
		return mPlayerEngine.getPlaylist();
	}

	@Override
	public void openPlaylist(Playlist playlist) {
		mPlayerEngine.openPlaylist(playlist);
	}

	@Override
	public void play() {
		PlaylistEntry entry = mPlayerEngine.getPlaylist().getSelectedTrack();
		RequestTask rt = mRequestQueue.getCurrentRequestTask();
		if (rt == null || !TextUtils.equals(rt.getName(), entry.getTrackName())) {
			long p = -1;
			addTask(p, entry);
		}
		mPlayerEngine.play();
	}

	@Override
	public boolean isPlaying() {
		return mPlayerEngine.isPlaying();
	}

	@Override
	public void stop() {
		mPlayerEngine.stop();
		mRequestQueue.cancelCurrentTask();
	}

	@Override
	public void pause() {
		mPlayerEngine.pause();
	}

	@Override
	public void next() {
		final int position = mPlayerEngine.getPlaylist().getSelectedIndex();
		if (position + 1 < mPlayerEngine.getPlaylist().size()) {
			addTask(-1, mPlayerEngine.getPlaylist().getTrack(position + 1));
			mPlayerEngine.next();
		}
	}

	@Override
	public void prev() {
		final int position = mPlayerEngine.getPlaylist().getSelectedIndex();
		if (position - 1 >= 0) {
			addTask(-1, mPlayerEngine.getPlaylist().getTrack(position - 1));
			mPlayerEngine.prev();
		}
	}

	@Override
	public void skipTo(int index) {
		if (index >= 0 && index < mPlayerEngine.getPlaylist().size()) {
			addTask(-1, mPlayerEngine.getPlaylist().getTrack(index));
			mPlayerEngine.skipTo(index);
		}
	}

	@Override
	public void setListener(PlayerEngineListener playerEngineListener) {
		mPlayerEngine.setListener(playerEngineListener);
	}

	@Override
	public void setPlaybackMode(PlayBackMode aMode) {
		mPlayerEngine.setPlaybackMode(aMode);
	}

	@Override
	public PlayBackMode getPlaybackMode() {
		return mPlayerEngine.getPlaybackMode();
	}

	@Override
	public void forward(int time) {
		mPlayerEngine.forward(time);
	}

	@Override
	public void rewind(int time) {
		mPlayerEngine.rewind(time);
	}

	@Override
	public void seek(int progress) {
		if (!PlayUtils.isChunkDone(progress, charArr)) {
			long p1 = PlayUtils.getRangeChunkIndex(progress, charArr);
			addTask(p1, mPlayerEngine.getPlaylist().getSelectedTrack());
		}
		mCurrentProgress = progress;
		mPlayerEngine.seek(progress);
	}

	@Override
	public void setDownloadListener(PlayerDownloadEngine downloader) {
		mPlayerEngine.setDownloadListener(downloader);
	}

}
