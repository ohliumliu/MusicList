package com.ifun361.musiclist.media;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

public class RequestTask implements Comparable<RequestTask> {

	private static final int MAX_BUFFLE_SIZE = 2048;

	private RequestQueue mRequestQueue;
	private DownloadTaskListener mDownloadTaskListener;
	private StringBuilder mMarkerTag = new StringBuilder();
	private String mTag = "";

	// 下载相关
	private final String mSourceUrl;
	private final String mDestDir;
	private final String mDestFileName;
	private long mP1 = 0l;
	private long mP2 = 0l;
	private char[] mCharArr;
	private int mCurrentProgress = 0;
	private int mCurrentIndex = 0;
	public volatile boolean isCancel = false;

	/**
	 * 
	 * @param url (下载地址)
	 * @param destDir 音乐缓存路径(下载的文件的存储路径)
	 * @param destFileName (下载的文件的存储的名称)
	 * @param p1 取得当前下载位置
	 * @param p2 开始下载时会取得实际容量大小
	 * @param arr 音轨文件对应名字key的分块对象
	 */
	public RequestTask(String url, String destDir, String destFileName,
			long p1, long p2, char[] arr) {
		super();
		this.mTag = destFileName;

		this.mSourceUrl = url;
		this.mDestDir = destDir;
		this.mDestFileName = destFileName;
		this.mP1 = p1;
		this.mP2 = p2;
		this.mCharArr = arr;

		this.mCurrentIndex = (int) (p1 / PlayUtils.CHUNK_UNIT);
	}

	public void setDownloadTaskListener(DownloadTaskListener l) {
		this.mDownloadTaskListener = l;
	}

	public void addMarker(String tag) {
		this.mMarkerTag.setLength(0);
		this.mMarkerTag.append(tag);
		// System.out.println(mTag + " " + tag);
	}

	public RequestTask setRequestQueue(RequestQueue requestQueue) {
		this.mRequestQueue = requestQueue;
		return this;
	}

	public void start() {
		File file = null;
		RandomAccessFile randomAccessFile = null;
		InputStream is = null;
		DownloaderError error = null;
		// 下载任务开始
		if (this.mDownloadTaskListener != null) {
			this.mDownloadTaskListener.onTaskStart();
		}
		// 尝试建立网络连接
		try {
			isCancel = false;
			file = new File(mDestDir);
			if (file != null && !file.exists()) {
				file.mkdirs();
			}
			file = new File(mDestDir + "/" + mDestFileName);
			if (file != null && !file.exists()) {
				file.createNewFile();
				randomAccessFile = new RandomAccessFile(mDestDir + "/"
						+ mDestFileName, "rw");
				randomAccessFile.setLength(mP2);
			} else {
				randomAccessFile = new RandomAccessFile(mDestDir + "/"
						+ mDestFileName, "rw");
			}

			// 设置Range 发起请求
			RequestHttpUtil requestUtil = new RequestHttpUtil();
			requestUtil.setHeader("Range", "bytes=" + mP1 + "-");
			HttpResponse response = requestUtil.request(mSourceUrl);
			int statusCode = requestUtil.getRequestCode();

			// 服务器没有正常返回
			if (statusCode >= 300) {
				DownloaderError e = new DownloaderError(
						DownloaderError.ErrorServer, null);
				e.mAssistErrorCode = statusCode;
				throw e;
			}

			// 服务器正常响应，但是没有返回任何内容
			HttpEntity entity = response.getEntity();
			if (entity == null) {
				throw new DownloaderError(DownloaderError.ErrorEmptyContent,
						null);
			}

			// 网络连接建立成功，获取到要下载的文件的基本信息
			final long totalLength = entity.getContentLength();
			// final long totalLength = mP2;
			final String fileType;
			final Header typeHeader = entity.getContentType();
			if (typeHeader != null) {
				fileType = typeHeader.getValue();
			} else {
				fileType = null;
			}

			// 取得真实文件大小
			if (totalLength != file.length()) {
				randomAccessFile.setLength(totalLength);
			}
			randomAccessFile.seek(mP1);

			if (this.mDownloadTaskListener != null) {
				this.mDownloadTaskListener.onDownloadBegin(totalLength,
						fileType);
			}
			System.out.println(" =========== RequestTask start " + totalLength);
			// 网络文件内容为空的时候则直接抛出无内容的异常信息
			is = entity.getContent();
			if (is == null) {
				throw new DownloaderError(DownloaderError.ErrorEmptyContent,
						null);
			}

			byte[] buf = new byte[MAX_BUFFLE_SIZE];

			int ch = -1;
			double progress = 0;// 下载的进度信息
			long count = mP1; // 已经下载到的内容长度
			long nextP1 = 0l;
			mCurrentProgress = 0;

			// 开始读取网络文件信息
			if (this.mDownloadTaskListener != null) {
				this.mDownloadTaskListener.onDownloading(mCurrentProgress,
						count);
			}
			// 没有取消下载的情况下才写文件
			while (!isCancel && (ch = is.read(buf)) != -1) {

				// 出现已下载部分，退出当前下载开启新下载
				nextP1 = checkIndex(count);
				if (nextP1 > 0) {
					addNextTask(nextP1);
					System.out.println(" =========== RequestTask break "
							+ count);
					break;
				} else if (nextP1 == -1) {
					System.out.println(" =========== RequestTask all done "
							+ count);
					break;
				}

				randomAccessFile.write(buf, 0, ch);

				count += ch;
				if (count == totalLength) {
					progress = 100;
				} else {
					progress = (count * 100.0 / totalLength);
				}
				int newProgress = (int) progress;
				if (newProgress > mCurrentProgress) {
					mCurrentProgress = newProgress;
					// 更新下载进度条信息
					if (this.mDownloadTaskListener != null) {
						this.mDownloadTaskListener.onDownloading(
								mCurrentProgress, count);
					}
				}
			}

			error = null;

			if (isCancel)
				System.out.println(" =========== RequestTask cancel " + count);
			else
				System.out.println(" =========== RequestTask out while");

		} catch (ClientProtocolException e) {
			error = new DownloaderError(DownloaderError.ErrorIO, e);
		} catch (IOException e) {
			error = new DownloaderError(DownloaderError.ErrorIO, e);
		} catch (DownloaderError e) {
			error = e;
		} catch (Throwable e) {
			error = new DownloaderError(DownloaderError.ErrorUnkown, e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					System.out.println("流关闭异常InputStream");
				}
			}
			if (randomAccessFile != null) {
				try {
					randomAccessFile.close();
				} catch (IOException e) {
					System.out.println("文件关闭异常randomAccessFile");
				}
			}
			if (error != null) {
				error.printStackTrace();
				System.out.println(" =========== RequestTask finally "
						+ error.getMessage());
			}
		}

		if (this.mDownloadTaskListener != null) {
			// 如果文件下载取消
			if (isCancel) {
				this.mDownloadTaskListener.onDownloadCancel();
			} else if (error != null) {// 下载过程中出现异常
				this.mDownloadTaskListener.onDownloadError(error);
			} else { // 文件下载完成
				this.mDownloadTaskListener.onDownloadFinished(mDestDir,
						mDestFileName);
			}
		}

		mCurrentProgress = 0;
		System.out.println(" =========== RequestTask end ");

	}

	private long getDownloadSize() throws DownloaderError {
		long size = 0;
		RequestHttpUtil requestUtil = new RequestHttpUtil();
		HttpResponse response = requestUtil.request(mSourceUrl);

		// 服务器没有正常返回
		if (response == null || requestUtil.getRequestCode() >= 300) {
			DownloaderError e = new DownloaderError(
					DownloaderError.ErrorServer, null);
			throw e;
		} else {
			size = requestUtil.getFullSize();
		}
		return size;
	}

	/**
	 * 取消下载
	 * 
	 * @param rest
	 *            取消当前下载后，是否开启新的下载任务
	 */
	public void cancel() {
		if (!isCancel) {
			isCancel = true;
		}
	}

	public boolean isCanceled() {
		return isCancel;
	}

	public String getName() {
		return this.mDestFileName;
	}

	public char[] getCharArr() {
		return this.mCharArr;
	}

	/**
	 * 判断当前下载位置的数据是否已经下载过
	 * 
	 * @param p
	 *            当前下载位置
	 * @return 0:继续当前下载， 大于0:中止当前下载在新位置开始下载， -1:已经下载完成停止下载
	 * 
	 */
	private long checkIndex(long p) {
		long p1 = 0;
		int nci = mCurrentIndex + 1;
		if (p > nci * PlayUtils.CHUNK_UNIT && mCurrentIndex < mCharArr.length) {

			mCharArr[mCurrentIndex] = PlayUtils.CHUNK_NOT_NULL;
			mCurrentIndex++;

			System.out.println(mCurrentIndex + "---"
					+ String.copyValueOf(mCharArr));

			if (mCurrentIndex < mCharArr.length
					&& mCharArr[mCurrentIndex] == PlayUtils.CHUNK_NOT_NULL) {
				p1 = -1l;
				for (int i = mCurrentIndex; i < mCharArr.length; i++) {
					if (mCharArr[i] == PlayUtils.CHUNK_NULL) {
						p1 = i * PlayUtils.CHUNK_UNIT;
						break;
					}
				}
			}
		}
		return p1;
	}

	private void addNextTask(long p1) {
		if (mRequestQueue != null && p1 < mP2) {
			RequestTask task = new RequestTask(mSourceUrl, mDestDir,
					mDestFileName, p1, mP2, mCharArr);
			mRequestQueue.add(task);
		}
	}

	/**
	 * 文件下载的监听
	 */
	public static interface DownloadTaskListener {

		/**
		 * 文件下载任务执行开始（建立网络之前的一些处理）
		 */
		public void onTaskStart();

		/**
		 * 文件下载开始，网络连接建立成功，并且已经成功获取到要下载的文件的具体信息，例如：文件的大小，文件的类型，名称等等
		 * 
		 * @param totalLength
		 *            要下载的文件的总大小
		 * @param fileType
		 *            要下载的文件的类型
		 */
		public void onDownloadBegin(long totalLength, String fileType);

		/**
		 * 文件下载中
		 * 
		 * @param progress
		 *            当前下载的进度信息（百分比）
		 * @param length
		 *            当前已经下载的文件的长度
		 */
		public void onDownloading(int progress, long length);

		/**
		 * 文件下载完成
		 * 
		 * @param fileDir
		 *            下载的文件的存储路径
		 * @param fileName
		 *            下载的文件的存储的名称
		 */
		public void onDownloadFinished(String fileDir, String fileName);

		/**
		 * 文件下载出错
		 * 
		 * @param tr
		 */
		public void onDownloadError(DownloaderError error);

		/**
		 * 下载被取消
		 */
		public void onDownloadCancel();

	}

	/**
	 * 文件下载出错
	 */
	public static class DownloaderError extends Exception {
		public static final int INVALID_ERROR_CODE = -999; // 无效的错误
		public static final int ErrorUnkown = -1;
		public static final int ErrorEmptyContent = 0;
		public static final int ErrorIO = 1; // IO读写
		public static final int ErrorServer = 2; // 建立连接后服务器响应出错

		private static final long serialVersionUID = 1L;
		private int errorCode = ErrorUnkown;
		private int mAssistErrorCode = INVALID_ERROR_CODE;

		public DownloaderError(int code, Throwable throwable) {
			super(throwable);
			this.errorCode = code;
		}

		public int getErrorCode() {
			return this.errorCode;
		}

		/**
		 * 辅助代码
		 * 
		 * @return
		 */
		public int getAssistantErrorCode() {
			return this.mAssistErrorCode;
		}
	}

	@Override
	public int compareTo(RequestTask another) {
		return 0;
	}
}
