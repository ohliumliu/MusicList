package com.ifun361.musiclist.model;

import java.util.ArrayList;

import org.json.JSONObject;

/**
 * 版权所有 2005-2015 中国日报社网站。 保留所有权利。<br>
 * 项目名：Android客户端<br>
 * 描述：控制层返回数据基类
 * 
 * @param <T>
 * @author baoxinyuan
 * @version 1.0
 * @since JDK1.6
 */
public class ApiResult<T extends BaseModel> extends BaseModel {

	public final static String RESULT_CODE_OK = "0";
	public final static String RESULT_CODE_ERROR = "-1";

	public T model = null;// 内容数据
	public ArrayList<T> listModel = null;// 内容数据

	public String resCode = RESULT_CODE_OK;// 返回状态码
	public String resMsg = "";// 返回状态消息
	public String resData = "";// 返回数据消息
	public JSONObject jsonData = null; // JSON数据
	public Exception ex = null;// 异常信息

}
