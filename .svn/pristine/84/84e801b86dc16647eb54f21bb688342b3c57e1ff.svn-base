package com.ifun361.musiclist.media;

public interface PlayerEngine {

	public void openPlaylist(Playlist playlist);

	public Playlist getPlaylist();

	public void play();

	public boolean isPlaying();

	public void stop();

	public void pause();

	public void next();

	public void prev();

	public void skipTo(int index);

	public void setListener(PlayerEngineListener playerEngineListener);

	public void setDownloadListener(PlayerDownloadEngine downloader);

	public void setPlaybackMode(Playlist.PlayBackMode aMode);

	public Playlist.PlayBackMode getPlaybackMode();

	public void forward(int time);

	public void rewind(int time);

	public void seek(int time);

}
