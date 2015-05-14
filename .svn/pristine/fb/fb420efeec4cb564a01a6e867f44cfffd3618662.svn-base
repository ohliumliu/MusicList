package com.ifun361.music.test;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ifun361.musiclist.R;
import com.ifun361.musiclist.model.ThemesList;


public class MusicThemesListAdapter extends BaseAdapter{
	private Context mContext;
	private ArrayList<ThemesList> themesLists;
	
	
	public MusicThemesListAdapter(Context context,ArrayList<ThemesList> themesSummarys) {
		this.mContext = context;
		this.themesLists = themesSummarys;
	}

	@Override
	public int getCount() {
		return themesLists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView ==null){
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_musicthemes, null);
			holder.tv_music_name = (TextView) convertView.findViewById(R.id.tv_music_name);
			holder.tv_music_intro = (TextView) convertView.findViewById(R.id.tv_music_intro);
			holder.tv_music_covers = (TextView) convertView.findViewById(R.id.tv_music_covers);
			holder.tv_music_targetType = (TextView) convertView.findViewById(R.id.tv_music_targetType);
			holder.tv_music_targetId = (TextView) convertView.findViewById(R.id.tv_music_targetId);
			holder.tv_music_position = (TextView) convertView.findViewById(R.id.tv_music_position);
			holder.tv_music_updateTime = (TextView) convertView.findViewById(R.id.tv_music_updateTime);
			holder.tv_music_playlistCount = (TextView) convertView.findViewById(R.id.tv_music_playlistCount);
			holder.tv_music_playlistDuration = (TextView) convertView.findViewById(R.id.tv_music_playlistDuration);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_music_name.setText(themesLists.get(position).getName());
		holder.tv_music_intro.setText(themesLists.get(position).getIntro());
		holder.tv_music_covers.setText(themesLists.get(position).getCovers()+"");
		holder.tv_music_targetType.setText(themesLists.get(position).getTargetType()+"");
		holder.tv_music_targetId.setText(themesLists.get(position).getTargetId()+"");
		holder.tv_music_position.setText(themesLists.get(position).getPosition()+"");
		holder.tv_music_updateTime.setText(themesLists.get(position).getUpdateTime()+"");
		holder.tv_music_playlistCount.setText(themesLists.get(position).getPlaylistCount()+"");
		holder.tv_music_playlistDuration.setText(themesLists.get(position).getPlaylistDuration()+"");
		return convertView;
	}
	private class ViewHolder{
		TextView tv_music_name
		,tv_music_intro
		,tv_music_covers
		,tv_music_targetType
		,tv_music_targetId
		,tv_music_position
		,tv_music_updateTime
		,tv_music_playlistCount
		,tv_music_playlistDuration;
		
	}
}
