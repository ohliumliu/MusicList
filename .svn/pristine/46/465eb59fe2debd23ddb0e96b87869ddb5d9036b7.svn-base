package com.ifun361.musiclist.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.ifun361.musiclist.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 版权所有 2005-2015 中国日报社网站。 保留所有权利。<br>
 * 项目名：Android客户端<br>
 * 描述：Fragment基础类
 * 
 * 1.onCreateView()方法调用 return super.onCreateView(inflater, container,
 * savedInstanceState);<br>
 * 2.onActivityCreated()方法调用setContentView(参数为Fragment inflater的根View);<br>
 * 3.setContentShown(取得数据前设false, 成功取得数据后设true);<br>
 * 4.setEmptyText(没有数据时提示信息); <br>
 * 5.setContentEmpty(是否显示没有数据时提示信息);<br>
 * 
 * @author baoxinyuan
 * @version 1.0
 * @since JDK1.6
 */
public abstract class BaseFragment extends Fragment {
	private View mProgressContainer;
	private View mContentContainer;
	private View mContentView;
	private TextView mEmptyView;
	private boolean mContentShown;
	private boolean mIsContentEmpty;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_base, container, false);
	}

	private String getLocalClassName(Object classObj) {
		final String pkg = getActivity().getPackageName();
		final String cls = classObj.getClass().getName();
		int packageLen = pkg.length();
		if (!cls.startsWith(pkg) || cls.length() <= packageLen
				|| cls.charAt(packageLen) != '.') {
			return cls;
		}
		return cls.substring(packageLen + 1);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ensureContent();
	}

	@Override
	public void onDetach() {
		mContentShown = false;
		mIsContentEmpty = false;
		mProgressContainer = null;
		mContentContainer = null;
		mContentView = null;
		mEmptyView = null;
		super.onDetach();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	/**
	 * Fragment刷新
	 */
	protected void onFragmentRefresh() {
	}

	public View getContentView() {
		return mContentView;
	}

	public void setContentView(int layoutResId) {
		if (this.getActivity() == null)
			return;
		LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
		View contentView = layoutInflater.inflate(layoutResId, null);
		setContentView(contentView);
	}

	public void setContentView(View view) {

		if (this.getActivity() == null)
			return;

		ensureContent();
		if (view == null) {
			return;
		}
		if (mContentContainer instanceof ViewGroup) {
			ViewGroup contentContainer = (ViewGroup) mContentContainer;
			if (mContentView == null) {
				contentContainer.addView(view);
			} else {
				int index = contentContainer.indexOfChild(mContentView);
				// replace content view
				contentContainer.removeView(mContentView);
				contentContainer.addView(view, index);
			}
			mContentView = view;
		} else {
			return;
		}
	}

	public void setEmptyText(int resId) {
		if (this.getActivity() == null)
			return;
		setEmptyText(getString(resId));
	}

	public void setEmptyText(CharSequence text) {
		if (this.getActivity() == null)
			return;
		ensureContent();
		if (mEmptyView != null && mEmptyView instanceof TextView) {
			((TextView) mEmptyView).setText(text);
		} else {
			return;
		}
	}

	public void setContentShown(boolean shown) {
		if (this.getActivity() == null)
			return;
		setContentShown(shown, true);
	}

	public void setContentShownNoAnimation(boolean shown) {
		if (this.getActivity() == null)
			return;
		setContentShown(shown, false);
	}

	private void setContentShown(boolean shown, boolean animate) {
		ensureContent();
		if (mContentShown == shown) {
			return;
		}
		mContentShown = shown;
		if (shown) {
			if (animate) {
				mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_out));
				mContentContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_in));
			} else {
				mProgressContainer.clearAnimation();
				mContentContainer.clearAnimation();
			}
			mProgressContainer.setVisibility(View.GONE);
			mContentContainer.setVisibility(View.VISIBLE);
		} else {
			if (animate) {
				mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_in));
				mContentContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_out));
			} else {
				mProgressContainer.clearAnimation();
				mContentContainer.clearAnimation();
			}
			mProgressContainer.setVisibility(View.VISIBLE);
			mContentContainer.setVisibility(View.GONE);
		}
	}

	public boolean isContentEmpty() {
		return mIsContentEmpty;
	}

	public void setContentEmpty(boolean isEmpty) {
		if (this.getActivity() == null)
			return;
		ensureContent();
		if (mContentView == null) {
			return;
		}
		if (isEmpty) {
			mEmptyView.setVisibility(View.VISIBLE);
			mContentView.setVisibility(View.GONE);
			mProgressContainer.setVisibility(View.GONE);
		} else {
			mEmptyView.setVisibility(View.GONE);
			mContentView.setVisibility(View.VISIBLE);
			mProgressContainer.setVisibility(View.GONE);
		}
		mIsContentEmpty = isEmpty;
	}

	/**
	 * View初始化
	 */
	private void ensureContent() {
		if (mContentContainer != null && mProgressContainer != null) {
			return;
		}
		View root = getView();
		if (root == null) {
			return;
		}
		mProgressContainer = root
				.findViewById(R.id.base_fragment_progress_container);
		if (mProgressContainer == null) {
			return;
		}
		mContentContainer = root
				.findViewById(R.id.base_fragment_content_container);
		if (mContentContainer == null) {
			return;
		}
		mEmptyView = (TextView) root.findViewById(android.R.id.empty);
		if (mEmptyView != null) {
			mEmptyView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onFragmentRefresh();
				}
			});
			mEmptyView.setVisibility(View.GONE);
		}
		mContentShown = true;
		if (mContentView == null) {
			setContentShown(false, false);
		}
	}

	/**
	 * 数据加载
	 */
	public void setFragmentLoading() {
		ensureContent();
		mProgressContainer.setVisibility(View.VISIBLE);

		mContentContainer.setVisibility(View.GONE);
		mEmptyView.setVisibility(View.GONE);
	}

	/**
	 * 数据加载出错
	 */
	public void setFragmentDataError() {
		ensureContent();
		mProgressContainer.setVisibility(View.GONE);
		mContentContainer.setVisibility(View.VISIBLE);
		mEmptyView.setVisibility(View.VISIBLE);
		mContentView.setVisibility(View.GONE);
		((TextView) mEmptyView).setText(R.string.network_success_nodata);
	}

	/**
	 * 数据加载出错
	 * 
	 * @param errorMsg
	 *            错误消息
	 */
	public void setFragmentDataError(final String errorMsg) {
		ensureContent();
		mProgressContainer.setVisibility(View.GONE);
		mContentContainer.setVisibility(View.VISIBLE);
		mEmptyView.setVisibility(View.VISIBLE);
		mContentView.setVisibility(View.GONE);

		((TextView) mEmptyView).setText(errorMsg);
	}

	/**
	 * 无数据
	 */
	public void setFragmentNoData() {
		setFragmentNoData(0);
	}

	/**
	 * 无数据
	 * 
	 * @param emptyTextId
	 *            消息提示
	 */
	public void setFragmentNoData(int emptyTextId) {
		ensureContent();
		mProgressContainer.setVisibility(View.GONE);
		mContentContainer.setVisibility(View.VISIBLE);
		mEmptyView.setVisibility(View.VISIBLE);
		mContentView.setVisibility(View.GONE);

		if (emptyTextId == 0) {
			mEmptyView.setText(R.string.network_success_nodata);
		} else {
			mEmptyView.setText(emptyTextId);
		}
	}

	/**
	 * 显示数据内容
	 */
	public void setFragmentShowData() {
		mProgressContainer.setVisibility(View.GONE);
		mContentContainer.setVisibility(View.VISIBLE);
		mContentView.setVisibility(View.VISIBLE);
		mEmptyView.setVisibility(View.GONE);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			new Handler().post(new Runnable() {
				@Override
				public void run() {
					// ListView快速滑动还未停止滑动时跳转到别的界面，会导致ImageLoader还是处于暂停状态；
					ImageLoader.getInstance().resume(); // 强制激活状态；
				}
			});
		}
		super.onHiddenChanged(hidden);
	}
}
