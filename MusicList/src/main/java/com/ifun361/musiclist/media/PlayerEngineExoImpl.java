///*
// * 创建日期：2015-1-14 上午10:49:17
// */
//package com.ifun361.musiclist.media;
//
//import java.io.File;
//import java.nio.ByteBuffer;
//
//import android.content.Context;
//import android.media.MediaCodec;
//import android.media.MediaCodec.BufferInfo;
//import android.media.MediaCodec.CryptoException;
//import android.net.Uri;
//import android.os.Handler;
//
//import com.google.android.exoplayer.ExoPlaybackException;
//import com.google.android.exoplayer.ExoPlayer;
//import com.google.android.exoplayer.FrameworkSampleSource;
//import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
//import com.google.android.exoplayer.MediaCodecTrackRenderer.DecoderInitializationException;
//import com.google.android.exoplayer.SampleSource;
//import com.google.android.exoplayer.audio.AudioTrack.InitializationException;
//import com.ifun361.musiclist.media.Playlist.PlayBackMode;
//
///**
// * 版权所有 2005-2015 中国日报社网站。 保留所有权利。<br>
// * 项目名：Android客户端<br>
// * 描述：
// * 
// * @author baoxinyuan
// * @version 1.0
// * @since JDK1.6
// */
//public class PlayerEngineExoImpl implements PlayerEngine {
//
//	public final String LOG_TAG = "PlayerEngineExoImpl";
//
//	private final Context mContext;
//	private final ExoPlayer mPlayer;
//	private final Handler mMainHandler;
//
//	private Playlist mPlaylist;
//	private PlayerEngineListener mPlayerEngineListener;
//	private PlayerDownloadEngine mPlayerDownloadEngine;
//	private InnerAudioTrackRenderer mCurrentAudioRender;
//	private long duration = 0;
//
//	public PlayerEngineExoImpl(Context context) {
//		mPlayer = ExoPlayer.Factory.newInstance(4, 1000, 5000);
//		mPlayer.addListener(mExoPlayerListener);
//		mMainHandler = new Handler();
//		mContext = context;
//	}
//
//	private void readyPlaySource(boolean retry) {
//
//		PlaylistEntry entry = mPlaylist.getSelectedTrack();
//		File file = new File(entry.getTrackRealPath());
//		Uri uri = Uri.fromFile(file);
//		FrameworkSampleSource sampleSource = new FrameworkSampleSource(
//				mContext, uri, null, 2);
//		mCurrentAudioRender = new InnerAudioTrackRenderer(sampleSource,
//				mMainHandler, mMediaCodecAudioTrackRendererEventListener);
//		mPlayer.prepare(mCurrentAudioRender);
//
//		if (mPlayer.getPlaybackState() == ExoPlayer.STATE_READY)
//			mPlayer.setPlayWhenReady(true);
//	}
//
//	@Override
//	public void openPlaylist(Playlist playlist) {
//		mPlaylist = playlist;
//	}
//
//	@Override
//	public Playlist getPlaylist() {
//		return mPlaylist;
//	}
//
//	@Override
//	public void play() {
//		readyPlaySource(false);
//	}
//
//	@Override
//	public boolean isPlaying() {
//		return mPlayer.getPlayWhenReady();
//	}
//
//	@Override
//	public void stop() {
//		mPlayer.setPlayWhenReady(false);
//	}
//
//	@Override
//	public void pause() {
//		mPlayer.setPlayWhenReady(false);
//		mPlayer.release();
//	}
//
//	@Override
//	public void next() {
//		if (mPlaylist != null
//				&& mPlaylist.getSelectedIndex() < mPlaylist.size() - 1) {
//			mPlaylist.selectNext();
//			if (isPlaying()) {
//				stop();
//			} else {
//				play();
//			}
//		}
//	}
//
//	@Override
//	public void prev() {
//		if (mPlaylist != null && mPlaylist.getSelectedIndex() > 0) {
//			mPlaylist.selectPrev();
//			if (isPlaying()) {
//				stop();
//			} else {
//				play();
//			}
//		}
//	}
//
//	@Override
//	public void skipTo(int index) {
//		if (mPlaylist != null) {
//			mPlaylist.select(index);
//			if (isPlaying()) {
//				stop();
//			} else {
//				play();
//			}
//		}
//	}
//
//	@Override
//	public void forward(int time) {
//	}
//
//	@Override
//	public void rewind(int time) {
//	}
//
//	@Override
//	public void seek(int time) {
//		long pos = time * mPlayer.getDuration() / 100;
//		mPlayer.seekTo(pos);
//	}
//
//	@Override
//	public void setDownloadListener(PlayerDownloadEngine downloader) {
//		mPlayerDownloadEngine = downloader;
//	}
//
//	@Override
//	public void setListener(PlayerEngineListener playerEngineListener) {
//		this.mPlayerEngineListener = playerEngineListener;
//	}
//
//	@Override
//	public void setPlaybackMode(PlayBackMode aMode) {
//		mPlaylist.setPlayListPlayBackMode(aMode);
//	}
//
//	@Override
//	public PlayBackMode getPlaybackMode() {
//		return mPlaylist.getPlayListPlayBackMode();
//	}
//
//	public void addMarker(String tag) {
//		System.out.println(LOG_TAG + " " + tag);
//	}
//
//	private ExoPlayer.Listener mExoPlayerListener = new ExoPlayer.Listener() {
//		@Override
//		public void onPlayerStateChanged(boolean playWhenReady,
//				int playbackState) {
//			addMarker("onPlayerStateChanged");
//		}
//
//		@Override
//		public void onPlayWhenReadyCommitted() {
//			addMarker("onPlayWhenReadyCommitted");
//		}
//
//		@Override
//		public void onPlayerError(ExoPlaybackException error) {
//			addMarker("onPlayerError " + error.getMessage());
//			mPlayer.release();
//			readyPlaySource(true);
//		}
//	};
//
//	private MediaCodecAudioTrackRenderer.EventListener mMediaCodecAudioTrackRendererEventListener = new MediaCodecAudioTrackRenderer.EventListener() {
//
//		@Override
//		public void onDecoderInitializationError(
//				DecoderInitializationException e) {
//			addMarker("onDecoderInitializationError");
//		}
//
//		@Override
//		public void onCryptoError(CryptoException e) {
//			addMarker("onCryptoError");
//		}
//
//		@Override
//		public void onAudioTrackInitializationError(InitializationException e) {
//			addMarker("onAudioTrackInitializationError");
//		}
//
//	};
//
//	class InnerAudioTrackRenderer extends MediaCodecAudioTrackRenderer {
//
//		long presentationTimeUs = 0;
//
//		public InnerAudioTrackRenderer(SampleSource source,
//				Handler eventHandler, EventListener eventListener) {
//			super(source, null, true, eventHandler, eventListener);
//		}
//
//		@Override
//		protected boolean processOutputBuffer(long positionUs,
//				long elapsedRealtimeUs, MediaCodec codec, ByteBuffer buffer,
//				BufferInfo bufferInfo, int bufferIndex, boolean shouldSkip)
//				throws ExoPlaybackException {
//			boolean flag = super.processOutputBuffer(positionUs,
//					elapsedRealtimeUs, codec, buffer, bufferInfo, bufferIndex,
//					shouldSkip);
//
//			if (!flag) {
//				if (mPlayerEngineListener != null
//						&& Math.abs(positionUs - presentationTimeUs) > 1000) {
//					mMainHandler.post(new Runnable() {
//						@Override
//						public void run() {
//							// percent,presentationTimeUs /
//							// 1000,duration / 1000
//							mPlayerEngineListener.onTrackProgress(
//									(int) presentationTimeUs,
//									(int) getDurationUs());
//						}
//					});
//				}
//				presentationTimeUs = positionUs;
//			}
//
//			return flag;
//		}
//
//	}
//
//}
