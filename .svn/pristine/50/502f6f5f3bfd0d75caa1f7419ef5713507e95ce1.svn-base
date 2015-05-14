package com.ifun361.musiclist.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ifun361.musiclist.MusicListApplication;
import com.ifun361.musiclist.R;
import com.ifun361.musiclist.constants.UIConstants;
import com.ifun361.musiclist.media.PlayManager;
import com.ifun361.musiclist.media.PlayerEngine;
import com.ifun361.musiclist.media.PlayerEngineListener;
import com.ifun361.musiclist.media.PlaylistEntry;
import com.ifun361.musiclist.ui.PlayActivity;

/**
 * 
 * 版权所有 2005-2015 中国日报社网站。 保留所有权利。<br>
 * 项目名：Android客户端<br>
 * 描述：播放服务 <br>
 * 
 * 1.播放控制，包括开始播放，暂停播放，上一首，下一首，快进后退操作。 <br>
 * 2.播放状态监听，包括播放歌曲，播放时间。 <br>
 * 3.系统状态监听，网络连接，Audio Focus，电话状态，耳机。 <br>
 * 4.通知栏信息管理。<br>
 * 5.播放记录管理。<br>
 * 
 * @author baoxinyuan
 * @version 1.0
 * @since JDK1.6
 */
public class PlayerService extends Service {

	public static final String ACTION_SERVICE = "service";
	public static final String ACTION_PLAY = "play";
	public static final String ACTION_NEXT = "next";
	public static final String ACTION_PREV = "prev";
	public static final String ACTION_STOP = "stop";
	public static final String ACTION_BIND_LISTENER = "bind_listener";
	private static final String TAG = "PlayerService";

	private AudioManager mAudioManager;
	private WifiManager mWifiManager;
	private WifiLock mWifiLock;
	private TelephonyManager mTelephonyManager;
	private NotificationManager mNotificationManager;
	private PlayerEngine mPlayerEngine;

	@Override
	public void onCreate() {
		
		// 初始化播放器
		initMediaPlayer();

		// 初始化系统状态Manager
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		// 电话状态监听
		mTelephonyManager.listen(mPhoneStateListener,
				PhoneStateListener.LISTEN_CALL_STATE);

		// 音频焦点变化监听
		int result = mAudioManager.requestAudioFocus(mAudioFocusChangeListener,
				AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
		if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
			System.out.println("AUDIOFOCUS_REQUEST_GRANTED");
		}

		// WIFI锁对象
		mWifiLock = mWifiManager.createWifiLock(getPackageName());
		mWifiLock.setReferenceCounted(false);

		// 耳机，音频输出通道变化监听
		try {
			registerReceiver(headsetDisconnected, new IntentFilter(
					Intent.ACTION_HEADSET_PLUG));
			registerReceiver(audioBecomingNoisy, new IntentFilter(
					AudioManager.ACTION_AUDIO_BECOMING_NOISY));
		} catch (Exception ex) {
			System.out.println("registerBroadcastReceiver Exception");
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		String action = "";

		if (intent != null)
			action = intent.getAction();

		updatePlaylist();

		if (action.equals(ACTION_PLAY)) {
			mPlayerEngine.play();
		} else if (action.equals(ACTION_NEXT)) {
			mPlayerEngine.next();
		} else if (action.equals(ACTION_PREV)) {
			mPlayerEngine.prev();
		} else if (action.equals(ACTION_STOP)) {
			stopSelfResult(startId);
		}

		return START_STICKY;
	}

	private void initMediaPlayer() {
		Log.i(TAG, "onCreate");
		mPlayerEngine = PlayManager.getPlayManager();
		mPlayerEngine.setListener(mServiceToImplEngineListener);
		MusicListApplication.getAPP().setPlayerEngine(mPlayerEngine);
	}

	private void updatePlaylist() {
		mPlayerEngine.openPlaylist(MusicListApplication.getAPP().getPlaylist());
	}

	@Override
	public void onDestroy() {
		mPlayerEngine.stop();
		mPlayerEngine = null;

		try {
			mTelephonyManager.listen(mPhoneStateListener,
					PhoneStateListener.LISTEN_NONE);
			mNotificationManager.cancel(UIConstants.PLAY_NOTIFICATION_ID);
			unregisterReceiver(headsetDisconnected);
			unregisterReceiver(audioBecomingNoisy);
		} catch (Exception ex) {
			System.out.println("unRegisterBroadcastReceiver Exception");
		}

		super.onDestroy();
	}

	private void notificationUpdate(PlaylistEntry playlistEntry) {
		final String title = playlistEntry.getAlbumName();
		final String name = playlistEntry.getTrackName();

		PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),
				0, new Intent(getApplicationContext(), PlayActivity.class),
				PendingIntent.FLAG_UPDATE_CURRENT);

		Notification notify = new Notification.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setTicker("iFun Player: " + playlistEntry.getTrackName())
				.setContentTitle("iFun Player")
				.setContentText("Now Playing:" + playlistEntry.getTrackName())
				.setContentIntent(pi).build();
		notify.flags |= Notification.FLAG_NO_CLEAR;

		startForeground(UIConstants.PLAY_NOTIFICATION_ID, notify);
	}

	private void notificationCancel() {
		stopForeground(true);
	}

	/**
	 * 播放器监听
	 */
	private PlayerEngineListener mServiceToImplEngineListener = new PlayerEngineListener() {

		private static final long PLAY_INTERVAL = 60000;
		private long preseconds = 0;

		@Override
		public void onTrackBuffering(int percent) {
			if (MusicListApplication.getAPP() != null
					&& MusicListApplication.getAPP().getUIEngineListener() != null) {
				MusicListApplication.getAPP().getUIEngineListener()
						.onTrackBuffering(percent);
			}
		}

		@Override
		public void onTrackChanged(PlaylistEntry playlistEntry) {
			if (MusicListApplication.getAPP() != null
					&& MusicListApplication.getAPP().getUIEngineListener() != null) {
				MusicListApplication.getAPP().getUIEngineListener()
						.onTrackChanged(playlistEntry);
			}
		}

		@Override
		public void onTrackProgress(long seconds, long duration) {
			if (preseconds == 0)
				preseconds = seconds;

			if (seconds - preseconds > PLAY_INTERVAL) {
				preseconds = seconds;
				// 记录播放位置
			}

			if (MusicListApplication.getAPP() != null
					&& MusicListApplication.getAPP().getUIEngineListener() != null) {
				MusicListApplication.getAPP().getUIEngineListener()
						.onTrackProgress((int) seconds, (int) duration);
			}
		}

		@Override
		public void onTrackStop() {
			mWifiLock.release();

			mNotificationManager.cancel(UIConstants.PLAY_NOTIFICATION_ID);
			if (MusicListApplication.getAPP() != null
					&& MusicListApplication.getAPP().getUIEngineListener() != null) {
				MusicListApplication.getAPP().getUIEngineListener()
						.onTrackStop();
			}
			notificationCancel();
		}

		@Override
		public boolean onTrackStart() {
			mWifiLock.acquire();
			notificationUpdate(mPlayerEngine.getPlaylist().getSelectedTrack());
			if (MusicListApplication.getAPP() != null
					&& MusicListApplication.getAPP().getUIEngineListener() != null
					&& !MusicListApplication.getAPP().getUIEngineListener()
							.onTrackStart()) {
				return false;
			}
			return true;
		}

		@Override
		public void onTrackPause() {
			notificationCancel();
			if (MusicListApplication.getAPP() != null
					&& MusicListApplication.getAPP().getUIEngineListener() != null) {
				MusicListApplication.getAPP().getUIEngineListener()
						.onTrackPause();
			}
		}

		@Override
		public void onTrackStreamError(String error) {
			if (MusicListApplication.getAPP() != null
					&& MusicListApplication.getAPP().getUIEngineListener() != null) {
				MusicListApplication.getAPP().getUIEngineListener()
						.onTrackStreamError(error);
			}
			notificationCancel();
		}

	};

	/**
	 * 音频输出通道变化监听
	 */
	private BroadcastReceiver audioBecomingNoisy = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (mPlayerEngine != null && mPlayerEngine.isPlaying()) {
				mPlayerEngine.pause();
			}
		}
	};

	/**
	 * 耳机监听
	 */
	private BroadcastReceiver headsetDisconnected = new BroadcastReceiver() {
		private static final int UNPLUGGED = 0;// 未插入耳机

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
				int state = intent.getIntExtra("state", -1);
				if (state != -1) {
					if (state == UNPLUGGED && mPlayerEngine != null
							&& mPlayerEngine.isPlaying()) {
						mPlayerEngine.pause();
					}
				} else {
				}
			}
		}
	};

	/**
	 * 通话监听
	 */
	private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			if (state == TelephonyManager.CALL_STATE_IDLE) {
				if (mPlayerEngine != null) {
					// mPlayerEngine.play();
				}
			} else {
				// 语言通话时停止播放
				// TelephonyManager.CALL_STATE_RINGING 响铃
				// TelephonyManager.CALL_STATE_OFFHOOK 拨打，接听
				if (mPlayerEngine != null) {
					mPlayerEngine.pause();
				}
			}
		}
	};

	/**
	 * 播放焦点监听
	 */
	private final OnAudioFocusChangeListener mAudioFocusChangeListener = new OnAudioFocusChangeListener() {
		@Override
		public void onAudioFocusChange(int focusChange) {
			switch (focusChange) {
			case AudioManager.AUDIOFOCUS_GAIN:
				// 取得焦点，恢复播放状态
				if (mPlayerEngine == null)
					initMediaPlayer();
				else if (!mPlayerEngine.isPlaying())
					mPlayerEngine.play();
				break;
			case AudioManager.AUDIOFOCUS_LOSS:
				// 失去焦点，停止播放释放播放资源
				if (mPlayerEngine != null && mPlayerEngine.isPlaying())
					mPlayerEngine.stop();
				mPlayerEngine = null;
				break;

			case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
			case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
				// 短暂失去焦点
				if (mPlayerEngine != null && mPlayerEngine.isPlaying())
					mPlayerEngine.pause();
				break;
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}