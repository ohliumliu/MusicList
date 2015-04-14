package com.ifun361.musiclist.model;

import java.io.Serializable;
import java.util.HashMap;


/**
 * 版权所有 2005-2015 中国日报社网站。 保留所有权利。<br>
 * 项目名：Android客户端<br>
 * 描述：主题对象
 * 
 * @author baoxinyuan
 * @version 1.0
 * @since JDK1.6
 */
public class ThemeModel extends BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;
	public HashMap<Integer,ThemesList> themes;
}
