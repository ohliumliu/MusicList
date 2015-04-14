package com.ifun361.musiclist.model;

import java.io.Serializable;

import com.ifun361.musiclist.constants.UIConstants;
import com.ifun361.musiclist.media.PlayUtils;

/**
 * 版权所有 2005-2015 中国日报社网站。 保留所有权利。<br>
 * 项目名：Android客户端<br>
 * 描述：音轨对象
 * 
 * @author baoxinyuan
 * @version 1.0
 * @since JDK1.6
 */
public class TrackModel extends BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final long REAL_SIZE = 3651691L;

	// Remote Data
	public String id = "0";
	public String title = "title";
	public String name = "1_audio_lossy.m4a";
	public String type = "m4a";
	public int duration = 190;
	public String url = "http://124.127.180.196/tracks/1f/8b/1/1_audio_lossy.m4a";
	public long size = 1651691L;// 开始下载时会取得实际容量大小
	public int playlistId = 1;

	// Local Data
	public String localurl = UIConstants.MUSIC_CACHE_PATH;
	public long playposition = 0L;

	public String getPlayUrl() {
		return localurl + "/" + name;
	}

}
