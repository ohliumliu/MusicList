package com.ifun361.musiclist.media;

import com.special.ResideMenu.ResideMenu;

public interface PlayerEngineListener {

	public boolean onTrackStart();

	public void onTrackChanged(PlaylistEntry playlistEntry);

	public void onTrackProgress(long seconds, long duration);

	public void onTrackBuffering(int percent);

	public void onTrackStop();

	public void onTrackPause();

	public void onTrackStreamError(String error);

}
