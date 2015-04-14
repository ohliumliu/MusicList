package com.ifun361.musiclist.media;

import java.nio.ByteBuffer;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.ifun361.musiclist.media.Playlist.PlayBackMode;

public class PlayerEngineImpl implements Runnable, PlayerEngine {

	public final String LOG_TAG = "PlayerEngineImpl";
	public final int INIT_CODEC_TIMEOUT = 8 * 1000;

	private MediaExtractor extractor;
	private MediaCodec codec;
	private AudioTrack audioTrack;

	private PlayerStates state = new PlayerStates();
	private volatile boolean stop = false;
	private volatile boolean restart = false;

	Handler handler = new Handler();

	// Track MetaData
	String name = null;
	String mime = null;
	int sampleRate = 0;
	int channels = 0;
	int bitrate = 0;
	long positionTime = 0;
	long presentationTimeUs = 0;
	long duration = 0;

	// MetaData
	private Playlist mPlaylist = null;
	private PlayerEngineListener mPlayerEngineListener;
	private PlayerDownloadEngine mPlayerDownloadEngine;

	public PlayerEngineImpl() {
		state.set(PlayerStates.STOPPED);
	}

	public boolean isLive() {
		return (duration == 0);
	}

	public synchronized void syncNotify() {
		notify();
	}

	private void seek(long pos) {
		try {
			extractor.seekTo(pos, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
		} catch (Exception ex) {
			finish();
		}
	}

	public synchronized void waitPlay() {
		// if (duration == 0) return;
		while (state.get() == PlayerStates.READY_TO_PLAY) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void waitInit() {
		while (state.get() == PlayerStates.READY_TO_PREPER) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		//synchronized(this){
		try {
			android.os.Process
					.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
			playback();
		} catch (final Exception ex) {
			ex.printStackTrace();
			addMarker("=========================");
			addMarker("play exception " + ex.getMessage());
			addMarker("=========================");

			if (mPlayerEngineListener != null) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						mPlayerEngineListener.onTrackStreamError("播放异常"
								+ ex.getMessage());
					}
				});
			}
		} finally {
			finish();
		}
	}
	//}

	private void finish() {
		try {
			if (codec != null) {
				codec.stop();
				codec.release();
				codec = null;
			}
		} catch (final Exception ex) {
			addMarker("play codec finally exception " + ex.getMessage());
		}
		try {
			if (audioTrack != null) {
				audioTrack.flush();
				audioTrack.release();
				audioTrack = null;
			}
		} catch (final Exception ex) {
			addMarker("play audioTrack finally exception " + ex.getMessage());
		}
		try {
			if (extractor != null)
				extractor.release();
		} catch (final Exception ex) {
			addMarker("play extractor finally exception " + ex.getMessage());
		}
	}

	private void initCodec() throws Exception {

		final long startTime = System.currentTimeMillis();
		long endTime = startTime;
		// Read track header
		MediaFormat format = null;

		while (codec == null && endTime - startTime < INIT_CODEC_TIMEOUT) {

			if (extractor == null)
				extractor = new MediaExtractor();

			endTime = System.currentTimeMillis();
			// 设置播放源 try to set the source, this might fail
			try {
				PlaylistEntry entry = mPlaylist.getSelectedTrack();
				name = entry.getTrackName();
				if (entry != null
						&& !TextUtils.isEmpty(entry.getTrackRealPath()))
					extractor.setDataSource(entry.getTrackRealPath());
			} catch (Exception e) {
				addMarker("设置播放源 exception try to set the source "
						+ e.getMessage());
				if (extractor != null) {
					extractor = null;
				}
				Thread.sleep(400);
				continue;
			}

			try {
				format = extractor.getTrackFormat(0);
				mime = format.getString(MediaFormat.KEY_MIME);
				sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
				channels = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
				// if duration is 0, we are probably playing a live stream
				duration = format.getLong(MediaFormat.KEY_DURATION);
				bitrate = format.getInteger(MediaFormat.KEY_BIT_RATE);
			} catch (Exception e) {
				e.printStackTrace();
				addMarker("取得播放源信息 exception:" + e.getLocalizedMessage());
			}

			// check we have audio content we know
			if (format == null || !mime.startsWith("audio/")) {
				// throw new Exception("非音频格式播放源");
				addMarker("非音频格式播放源");
				Thread.sleep(400);
				continue;
			}

			// create the actual decoder, using the mime to select
			try {
				codec = MediaCodec.createDecoderByType(mime);
			} catch (Exception ex) {
				this.addMarker("创建解码器实例失败 createDecoderByType " + mime + " "
						+ ex.getLocalizedMessage());
				Thread.sleep(400);
				continue;
			}
		}

		if (codec == null || format == null) {
			throw new Exception("创建解码器实例失败 codec == null || format == null");
		} else {
			// state.set(PlayerStates.READY_TO_PLAY);
			if (mPlayerEngineListener != null)
				handler.post(new Runnable() {
					@Override
					public void run() {
						mPlayerEngineListener.onTrackStart();
					}
				});

			codec.configure(format, null, null, 0);
			codec.start();
		}
	}

	private void playback() throws Exception {
		// 创建媒体引擎
		if (extractor != null) {
			extractor.release();
			extractor = null;
		}

		// 初始化解码器
		initCodec();
		ByteBuffer[] codecInputBuffers = codec.getInputBuffers();
		ByteBuffer[] codecOutputBuffers = codec.getOutputBuffers();

		// configure AudioTrack
		// 配置音乐播放器
		int channelConfiguration = channels == 1 ? AudioFormat.CHANNEL_OUT_MONO
				: AudioFormat.CHANNEL_OUT_STEREO;
		int minSize = AudioTrack.getMinBufferSize(sampleRate,
				channelConfiguration, AudioFormat.ENCODING_PCM_16BIT);
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate,
				channelConfiguration, AudioFormat.ENCODING_PCM_16BIT, minSize,
				AudioTrack.MODE_STREAM);

		// start playing, we will feed the AudioTrack later
		audioTrack.play();
		extractor.selectTrack(0);

		// start decoding
		final long kTimeOutUs = 1000;
		MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
		boolean sawInputEOS = false;
		boolean sawOutputEOS = false;
		int noOutputCounter = 0;
		final int noOutputCounterLimit = 10;

		addMarker("playing...");
		state.set(PlayerStates.PLAYING);
		while (!sawOutputEOS && noOutputCounter < noOutputCounterLimit && !stop) {
			// pause implementation
			// 暂停控制
			waitPlay();

			// 解码播放循环控制计数变量，作用是控制解码长时间不返回数据时退出循环
			// 初版因为等待HTTP下载后播放，暂时注释掉，有更好的控制逻辑时继续使用
			// noOutputCounter++;

			// read a buffer before feeding it to the decoder
			// 读取媒体数据
			if (!sawInputEOS) {
				// 返回解码输入流有效坐标
				int inputBufIndex = codec.dequeueInputBuffer(kTimeOutUs);
				if (inputBufIndex >= 0) {
					ByteBuffer dstBuf = codecInputBuffers[inputBufIndex];
					// 填充有效坐标数据
					int sampleSize = extractor.readSampleData(dstBuf, 0);
					if (sampleSize < 0) {
						Log.d(LOG_TAG, "saw input EOS. Stopping playback");
						addMarker("saw input EOS. Stopping playback");
						sawInputEOS = true;
						sampleSize = 0;
					} else {
						// long newPresentationTimeUs =
						// extractor.getSampleTime();
						// if (mPlayerEngineListener != null
						// && newPresentationTimeUs != presentationTimeUs) {
						// handler.post(new Runnable() {
						// @Override
						// public void run() {
						// // percent,presentationTimeUs /
						// // 1000,duration / 1000
						// mPlayerEngineListener.onTrackProgress(
						// (int) presentationTimeUs,
						// (int) duration);
						// }
						// });
						// }
						presentationTimeUs = extractor.getSampleTime();
					}
					// 把填充的数据写入input
					codec.queueInputBuffer(inputBufIndex, 0, sampleSize,
							presentationTimeUs,
							sawInputEOS ? MediaCodec.BUFFER_FLAG_END_OF_STREAM
									: 0);

					if (!sawInputEOS)
						extractor.advance();

				} else {
					Log.e(LOG_TAG, "inputBufIndex " + inputBufIndex);
					addMarker("inputBufIndex " + inputBufIndex);
				}
			} // !sawInputEOS

			// decode to PCM and push it to the AudioTrack player
			// 把读取到的媒体数据给解码器解码
			// 读取output
			int res = codec.dequeueOutputBuffer(info, kTimeOutUs);

			if (res >= 0) {
				if (info.size > 0)
					noOutputCounter = 0;

				int outputBufIndex = res;
				ByteBuffer buf = codecOutputBuffers[outputBufIndex];

				final byte[] chunk = new byte[info.size];
				buf.get(chunk);
				buf.clear();
				if (chunk.length > 0) {
					// 播放解码后的PCM音频
					audioTrack.write(chunk, 0, chunk.length);
					/*
					 * if(this.state.get() != PlayerStates.PLAYING) { if (events
					 * != null) handler.post(new Runnable() { @Override public
					 * void run() { events.onPlay(); } });
					 * state.set(PlayerStates.PLAYING); }
					 */

					long newPresentationTimeUs = extractor.getSampleTime();
					if (mPlayerEngineListener != null
							&& newPresentationTimeUs != positionTime) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								mPlayerEngineListener.onTrackProgress(
										(int) positionTime, (int) duration);
							}
						});
					}
					positionTime = newPresentationTimeUs;

				}
				// 释放output
				codec.releaseOutputBuffer(outputBufIndex, false);
				if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
					Log.d(LOG_TAG, "saw output EOS.");
					addMarker("saw output EOS.");
					sawOutputEOS = true;
				}
			} else if (res == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
				codecOutputBuffers = codec.getOutputBuffers();
			} else if (res == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
				MediaFormat oformat = codec.getOutputFormat();
				addMarker("output format has changed to " + oformat);
			} else {
				addMarker("dequeueOutputBuffer returned " + res);
			}
		}

		addMarker("stopping...");

		if (codec != null) {
			codec.stop();
			codec.release();
			codec = null;
		}
		if (audioTrack != null) {
			audioTrack.flush();
			audioTrack.release();
			audioTrack = null;
		}

		// clear source and the other globals
		mime = null;
		sampleRate = 0;
		channels = 0;
		bitrate = 0;
		presentationTimeUs = 0;
		positionTime = 0;
		duration = 0;

		state.set(PlayerStates.STOPPED);
		stop = true;

		if (noOutputCounter >= noOutputCounterLimit) {
			throw new Exception("解码器超时异常");
		} else {
			if (mPlayerEngineListener != null)
				handler.post(new Runnable() {
					@Override
					public void run() {
						mPlayerEngineListener.onTrackStop();
					}
				});
		}

		if (sawOutputEOS) {
			onCompletion();
		}

		addMarker("play stop");

		if (restart)
			play();
	}

	public void onCompletion() {
		if (!mPlaylist.isLastTrackOnList()
				|| mPlaylist.getPlayListPlayBackMode() == Playlist.PlayBackMode.REPEAT
				|| mPlaylist.getPlayListPlayBackMode() == Playlist.PlayBackMode.SHUFFLE_AND_REPEAT) {

			int index = mPlaylist.getSelectedIndex();
			if (mPlayerDownloadEngine != null && index < mPlaylist.size() - 1) {
				final PlaylistEntry entry = mPlaylist.getTrack(++index);
				handler.post(new Runnable() {
					@Override
					public void run() {
						mPlayerDownloadEngine.addTask(entry);
					}
				});

				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			next();
		} else {
			stop();
		}
	}

	public void addMarker(String tag) {
		System.out.println(name + " " + tag);
	}

	@Override
	public void openPlaylist(Playlist playlist) {
		mPlaylist = playlist;
	}

	@Override
	public void play() {
		if (state.get() == PlayerStates.STOPPED) {
			stop = false;
			restart = false;
			new Thread(this).start();
		}
		if (state.get() == PlayerStates.READY_TO_PLAY) {
			state.set(PlayerStates.PLAYING);
			syncNotify();
		}
	}

	@Override
	public void stop() {
		syncNotify();
		stop = true;
	}

	@Override
	public void pause() {
		state.set(PlayerStates.READY_TO_PLAY);
		if (mPlayerEngineListener != null)
			handler.post(new Runnable() {
				@Override
				public void run() {
					mPlayerEngineListener.onTrackPause();
				}
			});
	}

	@Override
	public void seek(int percent) {
		long pos = percent * duration / 100;
		seek(pos);
	}

	@Override
	public Playlist getPlaylist() {
		return mPlaylist;
	}

	@Override
	public boolean isPlaying() {
		return state.isPlaying();
	}

	@Override
	public void next() {
		if (mPlaylist != null
				&& mPlaylist.getSelectedIndex() < mPlaylist.size() - 1) {
			mPlaylist.selectNext();
			if (isPlaying()) {
				restart = true;
				stop();
			} else {
				play();
			}
		}
	}

	@Override
	public void prev() {
		if (mPlaylist != null && mPlaylist.getSelectedIndex() > 0) {
			mPlaylist.selectPrev();
			if (isPlaying()) {
				restart = true;
				stop();
			} else {
				play();
			}
		}
	}

	@Override
	public void skipTo(int index) {
		if (mPlaylist != null) {
			mPlaylist.select(index);
			if (isPlaying()) {
				restart = true;
				stop();
			} else {
				play();
			}
		}
	}

	@Override
	public void setListener(PlayerEngineListener playerEngineListener) {
		this.mPlayerEngineListener = playerEngineListener;
	}

	@Override
	public void setPlaybackMode(PlayBackMode aMode) {
		mPlaylist.setPlayListPlayBackMode(aMode);
	}

	@Override
	public PlayBackMode getPlaybackMode() {
		return mPlaylist.getPlayListPlayBackMode();
	}

	@Override
	public void forward(int time) {
	}

	@Override
	public void rewind(int time) {
	}

	@Override
	public void setDownloadListener(PlayerDownloadEngine downloader) {
		mPlayerDownloadEngine = downloader;
	}

}
