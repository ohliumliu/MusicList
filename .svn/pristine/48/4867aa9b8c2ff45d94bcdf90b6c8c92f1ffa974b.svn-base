package com.ifun361.musiclist.media;

import java.io.Serializable;

import com.ifun361.musiclist.model.TrackModel;

public class PlaylistEntry implements Serializable {

	private static final long serialVersionUID = 1L;

	private TrackModel mTrack;

	public TrackModel getTrack() {
		return mTrack;
	}

	public void setTrack(TrackModel track) {
		this.mTrack = track;
	}

	public String getTrackId() {
		return mTrack.id;
	}

	public String getTrackPath() {
		return mTrack.url;
	}

	public String getTrackRealPath() {
		return mTrack.getPlayUrl();
	}

	public String getTrackName() {
		return mTrack.name;
	}

	public String getAlbumName() {
		return mTrack.title;
	}

	public long getTrackPlayrecordPosition() {
		return mTrack.playposition;
	}

	public long getTrackSize() {
		return mTrack.size;
	}

	public String getTrackLocalUrl() {
		return mTrack.localurl;
	}
}
