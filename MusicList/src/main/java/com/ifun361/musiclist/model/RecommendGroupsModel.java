package com.ifun361.musiclist.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 推荐分组
 * @author pengying
 *
 */
public class RecommendGroupsModel extends BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;
	public HashMap<Integer, ListRecommendGroupsList> recommendGroups;
}
