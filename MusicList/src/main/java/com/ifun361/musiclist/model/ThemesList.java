package com.ifun361.musiclist.model;

import java.util.HashMap;



// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**主题内容目录
 * Entity mapped to table THEMES_LIST.
 */
public class ThemesList extends BaseModel{
	
	public HashMap<Integer, Themesitems> targetObject;
    private Long id;
    private Integer position;
    private Integer playlistCount;
    private Integer themeId;
    private Integer updateTime;
    private Integer covers;
    private String name;
    private Integer targetId;
    private Integer playlistDuration;
    private Integer targetType;
    private String intro;

    /*public TargetObject getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(TargetObject targetObject) {
		this.targetObject = targetObject;
	}
*/
	public ThemesList() {
    }

    public ThemesList(Long id) {
        this.id = id;
    }

    public ThemesList(Long id, Integer position, Integer playlistCount, Integer themeId, Integer updateTime, Integer covers, String name, Integer targetId, Integer playlistDuration, Integer targetType, String intro) {
        this.id = id;
        this.position = position;
        this.playlistCount = playlistCount;
        this.themeId = themeId;
        this.updateTime = updateTime;
        this.covers = covers;
        this.name = name;
        this.targetId = targetId;
        this.playlistDuration = playlistDuration;
        this.targetType = targetType;
        this.intro = intro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getPlaylistCount() {
        return playlistCount;
    }

    public void setPlaylistCount(Integer playlistCount) {
        this.playlistCount = playlistCount;
    }

    public Integer getThemeId() {
        return themeId;
    }

    public void setThemeId(Integer themeId) {
        this.themeId = themeId;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getCovers() {
        return covers;
    }

    public void setCovers(Integer covers) {
        this.covers = covers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public Integer getPlaylistDuration() {
        return playlistDuration;
    }

    public void setPlaylistDuration(Integer playlistDuration) {
        this.playlistDuration = playlistDuration;
    }

    public Integer getTargetType() {
        return targetType;
    }

    public void setTargetType(Integer targetType) {
        this.targetType = targetType;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

}
