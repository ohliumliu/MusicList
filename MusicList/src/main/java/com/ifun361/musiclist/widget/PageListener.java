package com.ifun361.musiclist.widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;

import com.ifun361.musiclist.model.ThemesList;
import com.ifun361.musiclist.model.Themesitems;
import com.ifun361.musiclist.ui.BaseFragmentActivity;
/**
 * 
 * @author pengying
 *
 */
public class PageListener extends SimpleOnPageChangeListener {
	private int currentPage;
	private Context mContext;
	private ArrayList<ThemesList> themesSummaries;
	private ArrayList<Themesitems> themeitems;
	public PageListener(Context context,ArrayList<ThemesList> themesSummaries) {
		this.mContext = context;
		this.themesSummaries = themesSummaries;
		themeitems = new ArrayList<Themesitems>();
		for(int i = 0; i<themesSummaries.size();i++){
		
				Set<Entry<Integer,Themesitems>> entrySetItems = themesSummaries.get(i).targetObject.entrySet();
				for (Iterator<Map.Entry<Integer, Themesitems>> iterators = entrySetItems
						.iterator(); iterators.hasNext();) {
					Map.Entry<Integer, Themesitems> entrys = iterators
							.next();
					Themesitems themesitemsSummary = entrys.getValue();
					themeitems.add(themesitemsSummary);
				}
				
		}
	}
	
	public enum typedef  {
	    在路上 , // 在路上
	    动起来, // 动起来
	    爱生活, // 爱生活
	    去旅行, // 去旅行
	    品环境 // 品环境
	} ;

	
	
	public void onPageSelected(int position) {
           currentPage = position;
           Integer type = themeitems.get(currentPage).getType();
           switch (type) { 
		case 0:
			String name = typedef.在路上.name();
			 ((BaseFragmentActivity)mContext).setTitleViewCenterText(name);
			break;
		case 1:
			String name2 = typedef.动起来.name();  
			 ((BaseFragmentActivity)mContext).setTitleViewCenterText(name2);
			break;
		case 2:
			String name3 = typedef.爱生活.name();
			 ((BaseFragmentActivity)mContext).setTitleViewCenterText(name3);
			break;
		case 3:
			String name4 = typedef.去旅行.name();
			 ((BaseFragmentActivity)mContext).setTitleViewCenterText(name4);
			break;
		case 4:
			String name5 = typedef.品环境.name();
			 ((BaseFragmentActivity)mContext).setTitleViewCenterText(name5);
			break;
		default:
			break;
		}
          
}
}
