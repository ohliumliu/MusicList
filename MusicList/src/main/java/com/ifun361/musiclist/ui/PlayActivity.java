package com.ifun361.musiclist.ui;

import java.util.ArrayList;
import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.ifun361.musiclist.MusicListApplication;
import com.ifun361.musiclist.R;
import com.ifun361.musiclist.constants.ApiConstants;
import com.ifun361.musiclist.constants.UIConstants;
import com.ifun361.musiclist.controller.PlaylistController;
import com.ifun361.musiclist.controller.PlaylistController.PlaylistCallback;
import com.ifun361.musiclist.media.PlayerEngine;
import com.ifun361.musiclist.media.PlayerEngineListener;
import com.ifun361.musiclist.media.Playlist;
import com.ifun361.musiclist.media.PlaylistEntry;
import com.ifun361.musiclist.model.ApiResult;
import com.ifun361.musiclist.model.Playlists;
import com.ifun361.musiclist.utils.BlurToggleUtils;
import com.ifun361.musiclist.utils.CompressImg;
import com.ifun361.musiclist.utils.Utils;
import com.ifun361.musiclist.widget.MyOnClickListener;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenu.OnMenuListener;
import com.special.ResideMenu.ResideMenuItem;

/**
 * 版权所有 2005-2015 中国日报社网站。 保留所有权利。<br>
 * 项目名：Android客户端<br>
 * 描述：播放界面
 * 
 * @author baoxinyuan
 * @version 1.0
 * @since JDK1.6
 */
public class PlayActivity extends BaseFragmentActivity implements
		OnClickListener, PlaylistCallback {

	protected static final String TAG = "PlayActivity";
	// 数据部分
	private ArrayList<Playlists> playlistSummaries;
	private PlaylistController mPlaylistController;

	private ResideMenu mResideMenu;// 侧边Menu
	private ArrayList<ResideMenuItem> mResideItemList = new ArrayList<ResideMenuItem>();// 播放列表
	private ImageView mPlayView;// 播放
	private ImageView mPreView;// 前一首
	private ImageView mNextView;// 下一首
	private SeekBar mPlaySeekBar;// 拖动条
	private ImageView mPlusModeView;// 播放模式
	private ImageView mPlusShareView;// 分享
	private ImageView mPlusCollectView;// 收藏

	private TextView mPlayInfoTitle;// 当前播放信息
	private TextView mPlayInfoText;// 当前播放信息
	private TextView mPlayPositionTime;// 当前播放时间
	private TextView mPlayDurationTime;// 歌曲曲长
	private ImageView mTrackCoverImage;// 歌曲封面

	private PlayerEngine mPlayerEngine;// 播放控制器引用

	private RelativeLayout mRelativeLayout;// 需要忽视手势的界面

	private LinkedList<Integer> linkedListIndex; // 播放每个列表第一首歌的index
	private LinkedList<Integer> linkedListLast; // 播放每个列表最后一首歌的index
	
	private BlurToggleUtils mBlurToggleUtils;
	private CompressImg compressImg;
	// "http://musicimg.ifun361.com/listimg/w750_h1334_c0/playlists/1/1_background_1.jpg"
	private Handler mDataSetHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (null == mPlaylistController) {
					// 外键关联themeId
					Bundle bundle = msg.getData();
					String arg1 = bundle.getString("themeid");
					// String arg2 = bundle.getString("playlistid");
					RequestParams params = new RequestParams();
					params.add("themeid", arg1);
					// params.add("playlistid", arg2);
					mPlaylistController = new PlaylistController(
							PlayActivity.this, PlayActivity.this, params);
					mPlaylistController.getPlaylistDataSet(PlayActivity.this);
				}
				break;
			case 1:
				Bundle bundle = msg.getData();
				linkedListIndex = (LinkedList<Integer>) bundle.get("index");
				linkedListLast = (LinkedList<Integer>) bundle.get("last");
				Playlist list = MusicListApplication.getAPP().getPlaylist();
				ResideMenuItem item = null;
				for (int i = 0; i < playlistSummaries.size(); i++) {
					item = new ResideMenuItem(PlayActivity.this,
							R.drawable.ic_music, playlistSummaries.get(i)
									.getName());
					((TextView) (item.findViewById(R.id.tv_title)))
							.setTextSize(10f);
					// 曲目条目的点击跳转监听
					item.setTag(i);
					item.setOnClickListener(new MyOnClickListener(
							linkedListIndex) {
						public void onClick(View v) {
							if (v.getTag() != null) {
								Integer value = null;
								int index = (Integer) v.getTag();
								for (int i = 0; i <= index; i++) {
									value = linkedListIndex.get(index);
								}
								mPlayerEngine.skipTo(value);
							}
						};
					});
					mResideItemList.add(item);
					mResideMenu.addMenuItem(item, ResideMenu.DIRECTION_RIGHT);
					//imageLoader.displayImage(ApiConstants.URL_THEME_LIST_BACKGROUND+(i+1)+"/"+(i+1)+"_background_1.jpg", mResideMenu.getBackgroundImageView());
				}
				int index = MusicListApplication.getAPP().getPlaylist()
						.getSelectedIndex();
				for (int i = 0; i < mResideItemList.size(); i++) {
					Integer listIndex = linkedListIndex.get(i);
					Integer listLast = linkedListLast.get(i);
					if (listIndex <= index && index < listLast) {
						//imageLoader.displayImage(ApiConstants.URL_THEME_LIST_BACKGROUND+(i+1)+"/"+(i+1)+"_background_1.jpg", mResideMenu.getBackgroundImageView(),new );
						//new CustomBlurView(PlayActivity.this, bitmap);
						//同步处理图片
						//Bitmap bmp = imageLoader.loadImageSync(ApiConstants.URL_THEME_LIST_BACKGROUND+(i+1)+"/"+(i+1)+"_background_1.jpg");
						imageLoader.loadImage(ApiConstants.URL_THEME_LIST_BACKGROUND+(i+1)+"/"+(i+1)+"_background_1.jpg", new ImageLoadingListener(){

							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								compressImg = new CompressImg();
								Bitmap comp = compressImg.comp(loadedImage);
								mBlurToggleUtils = new BlurToggleUtils(PlayActivity.this, comp,
										 mResideMenu.getBackgroundImageView());
							}

							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {
								// TODO Auto-generated method stub
								
							}
							
				        });
						/*imageLoader.displayImage(ApiConstants.URL_THEME_LIST_BACKGROUND+(i+1)+"/"+(i+1)+"_background_1.jpg", mResideMenu.getBackgroundImageView(),new ImageLoadingListener() {
							
							@Override
							public void onLoadingStarted(String imageUri, View view) {
								mResideMenu.getBackgroundImageView().setImageResource(R.drawable.ic_launcher);
							}
							
							@Override
							public void onLoadingFailed(String imageUri, View view,
									FailReason failReason) {
								
							}
							
							@Override
							public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
								//mResideMenu.getBackgroundImageView().setImageBitmap(null);
								//mCoverView.setScaleType(ScaleType.FIT_XY);
								//Bitmap bitmap = loadedImage.copy(loadedImage.getConfig(), true);
								//模糊初始化
								//CustomBlurView customBlurView = new CustomBlurView(PlayActivity.this);
								//customBlurView.setImageBitmap(loadedImage);
								compressImg = new CompressImg();
								Bitmap comp = compressImg.comp(loadedImage);
								mBlurToggleUtils = new BlurToggleUtils(PlayActivity.this, comp,
										 mResideMenu.getBackgroundImageView());
							}
							
							@Override
							public void onLoadingCancelled(String imageUri, View view) {
								
							}
						});*/
					} 
					
				}
				firstPlay();

				break;
			case 4:
				if (mPlayerEngine != null && mPlayerEngine.isPlaying()) {
					// mPlayerEngine.pause();
					// mPlayView.setImageResource(R.drawable.btn_play);
				} else if (mPlayerEngine != null) {
					mPlayerEngine.play();
					mPlayView.setImageResource(R.drawable.btn_pause);
				}
				break;
			default:
				
				
				
				
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		getPlaylistsData();
		initView();
	}

	private void firstPlay() {
		mDataSetHandler.sendEmptyMessageDelayed(4, 100);
	}

	private void getPlaylistsData() {
		// 外键主题ID
		Intent intent = getIntent();
		String themeid = intent.getStringExtra("themeid");
		// String playlistid = intent.getStringExtra("playlistid");
		Message msg = Message.obtain();
		msg.what = 0;
		Bundle bundle = new Bundle();
		bundle.putString("themeid", themeid);
		// bundle.putString("playlistid", playlistid);
		msg.setData(bundle);
		mDataSetHandler.sendMessageDelayed(msg, 100);

		mDataSetHandler.sendEmptyMessageDelayed(0, 100);
	}

	// 截图

	class CustomBlurView extends ImageView {
		private Bitmap bitmap;

		public CustomBlurView(Context context) {
			super(context);
		}


		@Override
		public void setImageBitmap(Bitmap bm) {
			super.setImageBitmap(bm);
			invalidate();
		}
		
		
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			canvas.drawBitmap(bitmap, new Rect(0,0,100,100), new RectF(0,0,100,50), null);
		}
	}

	private void initView() {

		mResideMenu = new ResideMenu(this);
		// 动态改变
		mResideMenu.setScaleValue(0.7f);
		mResideMenu.attachToActivity(this);
		mResideMenu.setShadowVisible(false);
		mResideMenu.setMenuListener(new OnMenuListener() {

			@Override
			public void openMenu() {
				ResideMenuItem item = null;
				final int index = MusicListApplication.getAPP().getPlaylist()
						.getSelectedIndex();
				for (int i = 0; i < mResideItemList.size(); i++) {
					Integer listIndex = linkedListIndex.get(i);
					Integer listLast = linkedListLast.get(i);
					item = mResideItemList.get(i);
					if (listIndex <= index && index < listLast) {
						((TextView) (item.findViewById(R.id.tv_title)))
								.setTextColor(Color.RED);
						//imageLoader.displayImage(ApiConstants.URL_THEME_LIST_BACKGROUND+(i+1)+"/"+(i+1)+"_background_1.jpg", mResideMenu.getBackgroundImageView());
					} else {
						((TextView) (item.findViewById(R.id.tv_title)))
								.setTextColor(Color.WHITE);
					}
					
				}

			}

			@Override
			public void closeMenu() {

			}

		});

		topTitleLeftIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		topTitleRightIcon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mResideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
			}
		});

		mPlusModeView = (ImageView) this.findViewById(R.id.player_plus_mode);
		mPlusShareView = (ImageView) this.findViewById(R.id.player_plus_share);
		mPlusCollectView = (ImageView) this
				.findViewById(R.id.player_plus_collect);

		mTrackCoverImage = (ImageView) this
				.findViewById(R.id.player_track_cover);
		mPlayInfoTitle = (TextView) this.findViewById(R.id.player_track_info);
		mPlayInfoText = (TextView) this
				.findViewById(R.id.player_control_play_info);
		mPlayView = (ImageView) this.findViewById(R.id.player_control_playback);
		mPreView = (ImageView) this.findViewById(R.id.player_control_play_pre);
		mNextView = (ImageView) this
				.findViewById(R.id.player_control_play_next);

		mPlayPositionTime = (TextView) this
				.findViewById(R.id.player_time_position);
		mPlayDurationTime = (TextView) this
				.findViewById(R.id.player_time_duration);
		mPlaySeekBar = (SeekBar) this.findViewById(R.id.player_seekbar);

		mPlayView.setOnClickListener(this);
		mPreView.setOnClickListener(this);
		mNextView.setOnClickListener(this);

		mPlaySeekBar.setSecondaryProgress(0);
		mPlaySeekBar.setMax(100);
		mPlaySeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (mPlayerEngine != null && fromUser) {
					mPlayerEngine.seek(progress);
				}
			}
		});

		mRelativeLayout = (RelativeLayout) this
				.findViewById(R.id.player_main_layout);
		mResideMenu.addIgnoredView(mRelativeLayout);

		topTitleLeftIcon.setImageResource(R.drawable.btn_back);
		topTitleRightIcon.setImageResource(R.drawable.btn_album);
	}

	@Override
	protected void onResume() {
		super.onResume();

		mPlayerEngine = MusicListApplication.getAPP().getPlayerEngine();
		MusicListApplication.getAPP()
				.setUIEngineListener(mPlayerEngineListener);
		if (mPlayerEngine != null && mPlayerEngine.isPlaying()) {
			mPlayView.setImageResource(R.drawable.btn_pause);
			mPlayInfoText.setText("Playing");
		} else if (mPlayerEngine != null) {
		}
		
		Log.i(TAG + "onResume", "onResume" + mPlayerEngine);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return mResideMenu.dispatchTouchEvent(ev);
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		switch (id) {
		case R.id.player_control_playback:
			if (mPlayerEngine != null && mPlayerEngine.isPlaying()) {
				mPlayerEngine.pause();
				mPlayView.setImageResource(R.drawable.btn_play);
			} else if (mPlayerEngine != null) {
				mPlayerEngine.play();
				mPlayView.setImageResource(R.drawable.btn_pause);
				mPlayInfoText.setText("Playing");
			}
			break;
		case R.id.player_control_play_pre:
			if (mPlayerEngine != null) {
				mPlayerEngine.prev();
			}
			break;
		case R.id.player_control_play_next:
			if (mPlayerEngine != null) {
				mPlayerEngine.next();
			}
			break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	// TODO 服务器排序混乱

	private PlayerEngineListener mPlayerEngineListener = new PlayerEngineListener() {

		@Override
		public boolean onTrackStart() {
			mPlayInfoText.setText("Playing");
			mPlayInfoTitle.setText(MusicListApplication.getAPP().getPlaylist()
					.getSelectedTrack().getTrackName());
			mPlayView.setImageResource(R.drawable.btn_pause);

			ResideMenuItem item = null;
			final int index = MusicListApplication.getAPP().getPlaylist()
					.getSelectedIndex();
			for (int i = 0; i < mResideItemList.size(); i++) {
				Integer listIndex = linkedListIndex.get(i);
				Integer listLast = linkedListLast.get(i);
				Log.i(TAG, listIndex + "~" + listLast);
				item = mResideItemList.get(i);
				if (listIndex <= index && index < listLast) {
					((TextView) (item.findViewById(R.id.tv_title)))
							.setTextColor(Color.RED);
					imageLoader.loadImage(ApiConstants.URL_THEME_LIST_BACKGROUND+(i+1)+"/"+(i+1)+"_background_1.jpg", new ImageLoadingListener(){

						@Override
						public void onLoadingStarted(String imageUri,
								View view) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onLoadingFailed(String imageUri,
								View view, FailReason failReason) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							compressImg = new CompressImg();
							Bitmap comp = compressImg.comp(loadedImage);
							mBlurToggleUtils = new BlurToggleUtils(PlayActivity.this, comp,
									 mResideMenu.getBackgroundImageView());
						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
							// TODO Auto-generated method stub
							
						}
						
			        });
					/*imageLoader.displayImage(ApiConstants.URL_THEME_LIST_BACKGROUND+(i+1)+"/"+(i+1)+"_background_1.jpg", mResideMenu.getBackgroundImageView(),new ImageLoadingListener() {
						
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							mResideMenu.getBackgroundImageView().setImageResource(R.drawable.ic_launcher);
						}
						
						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							
						}
						
						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							
							//mCoverView.setScaleType(ScaleType.FIT_XY);
							//Bitmap bitmap = loadedImage.copy(loadedImage.getConfig(), true);
							//模糊初始化
							//CustomBlurView customBlurView = new CustomBlurView(PlayActivity.this);
							//customBlurView.setImageBitmap(loadedImage);
							compressImg = new CompressImg();
							Bitmap comp = compressImg.comp(loadedImage);
							mBlurToggleUtils = new BlurToggleUtils(PlayActivity.this, comp,
									 mResideMenu.getBackgroundImageView());
						}
						
						@Override
						public void onLoadingCancelled(String imageUri, View view) {
							
						}
					});*/
				} else {
					((TextView) (item.findViewById(R.id.tv_title)))
							.setTextColor(Color.WHITE);
				}
				
			}
			
			
			
			return true;
		}

		@Override
		public void onTrackChanged(PlaylistEntry playlistEntry) {
		}

		@Override
		public void onTrackProgress(long seconds, long duration) {
			final int percent = (duration == 0) ? 0
					: (int) (100 * seconds / duration);
			mPlaySeekBar.setProgress(percent);
			String s = Utils.secondsToString((int) (seconds / 1000000));
			String d = Utils
					.secondsToString((int) ((duration - seconds) / 1000000));

			mPlayPositionTime.setText(s);
			mPlayDurationTime.setText(d);
		}

		@Override
		public void onTrackBuffering(int percent) {
		}

		@Override
		public void onTrackStop() {
			mPlayInfoText.setText("Playback Stopped.");
			mPlayView.setImageResource(R.drawable.btn_play);
		}

		@Override
		public void onTrackPause() {
			mPlayInfoText.setText("Playback Pause.");
		}

		@Override
		public void onTrackStreamError(String error) {
			mPlayInfoText.setText(error);
			mPlayView.setImageResource(R.drawable.btn_play);
		}

	};

	public static void readyToPlayWithNotify(Context context, String themeid) {
		Intent intent = new Intent(context, PlayActivity.class);
		intent.putExtra("themeid", themeid);
		intent.setAction(UIConstants.PLAY_ACTION_DISPLAY);
		context.startActivity(intent);
	}

	@Override
	public void onGetPlaylist(ApiResult<Playlists> result) {
		// TODO Auto-generated method stub
		playlistSummaries = result.listModel;

		// playlistSummaries.get;
		Log.i("playlistssss", MusicListApplication.getAPP().getPlaylist()
				.getAllTracks().length
				+ "");
		// 判断是否有数据
		if ((MusicListApplication.getAPP().getPlaylist().getAllTracks().length) == 0) {
			// 给播放器添加数据
			MusicListApplication.getAPP().setInitPlaylistsData(
					playlistSummaries);
		}
		Message msg = Message.obtain();
		Bundle bundle = new Bundle();
		msg.what = 1;
		LinkedList<Integer> index = MusicListApplication.getAPP()
				.getResideMenuPlayListsDataIndex();
		LinkedList<Integer> last = MusicListApplication.getAPP()
				.getResideMenuPlayListsDataLast();
		bundle.putSerializable("index", index);
		bundle.putSerializable("last", last);
		msg.setData(bundle);
		mDataSetHandler.sendMessage(msg);

	}

}