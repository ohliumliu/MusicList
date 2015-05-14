package com.ifun361.musiclist.media;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import com.ifun361.musiclist.model.TrackModel;

public class Playlist implements Serializable {

	private static final long serialVersionUID = 1L;
	public ArrayList<PlaylistEntry> playlist = null;
	public int selected = -1;

	public enum PlayBackMode {
		NORMAL, SHUFFLE, REPEAT, SHUFFLE_AND_REPEAT
	}

	private ArrayList<Integer> mPlayOrder = new ArrayList<Integer>();
	private PlayBackMode mPlayListPlayBackMode = PlayBackMode.NORMAL;

	public PlayBackMode getPlayListPlayBackMode() {
		return mPlayListPlayBackMode;
	}

	public void setPlayListPlayBackMode(PlayBackMode aPlayListPlayBackMode) {
		boolean force = false;
		switch (aPlayListPlayBackMode) {
		case NORMAL:
		case REPEAT:
			if (mPlayListPlayBackMode == PlayBackMode.SHUFFLE
					|| mPlayListPlayBackMode == PlayBackMode.SHUFFLE_AND_REPEAT) {
				force = true;
			}
			break;
		case SHUFFLE:
		case SHUFFLE_AND_REPEAT:
			if (mPlayListPlayBackMode == PlayBackMode.NORMAL
					|| mPlayListPlayBackMode == PlayBackMode.REPEAT) {
				force = true;
			}
			break;
		}
		mPlayListPlayBackMode = aPlayListPlayBackMode;
		calculateOrder(force);
	}

	public Playlist() {
		playlist = new ArrayList<PlaylistEntry>();
		calculateOrder(true);
	}

	public void addTrack(TrackModel track) {
		PlaylistEntry entry = new PlaylistEntry();
		entry.setTrack(track);
		playlist.add(entry);
		mPlayOrder.add(size() - 1);
	}

	public boolean isEmpty() {
		return playlist.size() == 0;
	}

	public void selectNext() {
		if (!isEmpty()) {
			selected++;
			selected %= playlist.size();
		}
	}

	public void selectPrev() {
		if (!isEmpty()) {
			selected--;
			if (selected < 0)
				selected = playlist.size() - 1;
		}
	}

	public void select(int index) {
		if (!isEmpty()) {
			if (index >= 0 && index < playlist.size())
				selected = mPlayOrder.indexOf(index);
		}
	}

	public void selectOrAdd(TrackModel track) {
		for (int i = 0; i < playlist.size(); i++) {
			if (playlist.get(i).getTrack().id == track.id) {
				select(i);
				return;
			}
		}
		addTrack(track);
		select(playlist.size() - 1);
	}

	public int getSelectedIndex() {
		if (isEmpty()) {
			selected = -1;
		}
		if (selected == -1 && !isEmpty()) {
			selected = 0;
		}
		return selected;
	}

	public PlaylistEntry getSelectedTrack() {
		PlaylistEntry entry = null;

		int index = getSelectedIndex();
		if (index == -1) {
			return null;
		}
		index = mPlayOrder.get(index);
		if (index == -1) {
			return null;
		}
		entry = playlist.get(index);

		return entry;

	}

	public void addPlayListEntry(PlaylistEntry entry) {
		if (entry != null) {
			playlist.add(entry);
			mPlayOrder.add(size() - 1);
		}
	}

	public int size() {
		return playlist == null ? 0 : playlist.size();
	}

	public PlaylistEntry getTrack(int index) {
		return playlist.get(index);
	}

	public PlaylistEntry[] getAllTracks() {
		PlaylistEntry[] out = new PlaylistEntry[playlist.size()];
		playlist.toArray(out);
		return out;
	}

	public void remove(int position) {
		if (playlist != null && position < playlist.size() && position >= 0) {

			if (selected >= position) {
				selected--;
			}

			playlist.remove(position);
			mPlayOrder.remove(position);
		}
	}

	private void calculateOrder(boolean force) {
		if (mPlayOrder.isEmpty() || force) {
			int oldSelected = 0;

			if (!mPlayOrder.isEmpty()) {
				oldSelected = mPlayOrder.get(selected);
				mPlayOrder.clear();
			}

			for (int i = 0; i < size(); i++) {
				mPlayOrder.add(i, i);
			}

			if (mPlayListPlayBackMode == null) {
				mPlayListPlayBackMode = PlayBackMode.NORMAL;
			}

			switch (mPlayListPlayBackMode) {
			case NORMAL:
			case REPEAT:
				selected = oldSelected;
				break;
			case SHUFFLE:
			case SHUFFLE_AND_REPEAT:
				Collections.shuffle(mPlayOrder);
				selected = mPlayOrder.indexOf(selected);
				break;
			}
		}
	}

	public boolean isLastTrackOnList() {
		if (selected == size() - 1)
			return true;
		else
			return false;
	}

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		if (mPlayOrder == null) {
			mPlayOrder = new ArrayList<Integer>();
			calculateOrder(true);
		}
	}
}
