package com.example.lewis.list_window;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lewis on 2017/9/11.
 * Description: 菜单列表工具类
 */

public class ListPopupWindow {

	private PopupWindow mPopupWindow;
	private int mContentViewHeight;
	private int mScreenHeight;
	private Activity mActivity;
	private int[] mLocations = new int[2];
	private int mViewWith;
	private boolean mIsOnDismissAnim = false;
	private OnItemClickListener mOnItemClickListener;

	private ImageView mArrowDown, mArrowUp;

	public static ListPopupWindow get(Activity activity) {
		return new ListPopupWindow(activity);
	}

	public ListPopupWindow(final Activity activity) {
		mScreenHeight = activity.getResources().getDisplayMetrics().heightPixels;
		final View contentView;
		mActivity = activity;

		contentView = View.inflate(activity, R.layout.view_popup_list, null);
		RecyclerView recyclerView = contentView.findViewById(R.id.recycler);
		recyclerView.setLayoutManager(new LinearLayoutManager(activity));
		final WindowAdapter adapter = new WindowAdapter(activity, initData());
		recyclerView.setAdapter(adapter);
		mPopupWindow = new PopupWindow(activity);
		mPopupWindow.setContentView(contentView);
		mPopupWindow.setBackgroundDrawable(null);

		mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		mPopupWindow.setTouchable(true);
//		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(null);

		mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
					dismissWithAnim();
					return false;
				}
				return false;
			}
		});

		adapter.setOnItemClickListener(new WindowAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int position) {
				dismissWithAnim();
				if (mOnItemClickListener != null) mOnItemClickListener.onItemClick(position, adapter);
			}
		});

		mArrowDown = contentView.findViewById(R.id.icon_popup_down);
		mArrowUp = contentView.findViewById(R.id.icon_popup_up);
	}

	public PopupWindow show(final View view) {
		view.getLocationInWindow(mLocations);
		mViewWith = view.getMeasuredWidth();
		setupAlpha(mActivity, 0.4f);

		mPopupWindow.showAtLocation(view, Gravity.TOP, 0, 0);
		mPopupWindow.getContentView().post(new Runnable() {
			@Override
			public void run() {
				mContentViewHeight = mPopupWindow.getContentView().getMeasuredHeight();
				if (mLocations[1] > mScreenHeight / 2) {
					// show up of view
					mPopupWindow.update(view, 0, -(mContentViewHeight + view.getHeight()),
							mPopupWindow.getWidth(), mPopupWindow.getHeight());
//                    mPopupWindow.setAnimationStyle(R.style.PopupWindowAnimTop);

					setupArrowIcon(view,true);
				} else {
					// show down of view
					mPopupWindow.update(view, 0, 0, mPopupWindow.getWidth(),
							mPopupWindow.getHeight());
//                    mPopupWindow.setAnimationStyle(R.style.PopupWindowAnimBottom);

					setupArrowIcon(view, false);
				}
				AnimatorSet anim = showAnim(0.2f, 0.03f, 150);
				anim.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						super.onAnimationEnd(animation);
					}
				});
			}
		});

		view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
			@Override
			public void onViewAttachedToWindow(View view) {

			}

			@Override
			public void onViewDetachedFromWindow(View view) {
				if (isShowing()) {
					mPopupWindow.dismiss();
				}
			}
		});
		return mPopupWindow;
	}

	/**
	 * 展示动画
	 * @param start 开始的缩放系数
	 * @param end 结束的缩放系数
	 * @param duration 持续时间
	 */
	private AnimatorSet startAnim(float start, float end, int duration) {
		setupPivotXY();

		ObjectAnimator scaleX = ObjectAnimator.ofFloat(mPopupWindow.getContentView(),
				"scaleX", start, end);
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(mPopupWindow.getContentView(),
				"scaleY", start, end);

		AnimatorSet anim = new AnimatorSet();
		anim.play(scaleX).with(scaleY);
		anim.setDuration(duration);
		anim.start();
		return anim;
	}

	private void setupPivotXY() {
		mPopupWindow.getContentView().setPivotX(mLocations[0] + mViewWith / 2);
		if (mLocations[1] > mScreenHeight / 2) {
			mPopupWindow.getContentView().setPivotY(mPopupWindow.getContentView().getMeasuredHeight());
		} else {
			mPopupWindow.getContentView().setPivotY(0);
		}
	}

	public AnimatorSet showAnim(float start, float expand, int duration) {
		setupPivotXY();

		ObjectAnimator scaleX = ObjectAnimator.ofFloat(mPopupWindow.getContentView(), "scaleX", start, 1 + expand);
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(mPopupWindow.getContentView(), "scaleY", start, 1 + expand);
		ObjectAnimator restoreX = ObjectAnimator.ofFloat(mPopupWindow.getContentView(), "scaleX", 1 + expand, 1f);
		ObjectAnimator restoreY = ObjectAnimator.ofFloat(mPopupWindow.getContentView(), "scaleY", 1 + expand, 1f);

		AnimatorSet anim = new AnimatorSet();
		// 先扩大到超过菜单原先大小，在回复原本大小
		anim.play(scaleX).with(scaleY);
		anim.play(restoreX).with(restoreY).after(scaleX);
		anim.setDuration(duration);
		anim.start();
		return anim;
	}

	private List<String> initData() {
		List<String> result = new ArrayList<>();
		result.add("Item 1");
		result.add("Item 2");
		return result;
	}

	private void setupAlpha(Activity activity, float alpha) {
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.alpha = alpha;
		activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		activity.getWindow().setAttributes(lp);
	}

	public void dismissWithAnim() {
		if (!isShowing() || mIsOnDismissAnim) return;

		mIsOnDismissAnim = true;
		AnimatorSet anim = startAnim(1f, 0.2f, 200);
		anim.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				mIsOnDismissAnim = false;
				mPopupWindow.dismiss();
				setupAlpha(mActivity, 1);
			}
		});
	}

	public boolean isShowing() {
		return mPopupWindow != null && mPopupWindow.isShowing();
	}

	public void setupArrowIcon(View view, boolean isDownArrow) {
		if (!isDownArrow) {
			mArrowUp.setVisibility(View.VISIBLE);
			mArrowDown.setVisibility(View.GONE);
			setupMargin(view,mArrowUp);
		} else {
			mArrowUp.setVisibility(View.GONE);
			mArrowDown.setVisibility(View.VISIBLE);
			setupMargin(view, mArrowDown);
		}
	}

	private void setupMargin(View view, ImageView arrowIcon) {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) arrowIcon.getLayoutParams();
		lp.setMargins(mLocations[0] - (view.getMeasuredWidth() - arrowIcon.getMeasuredWidth()) / 2 ,
				0, 0 ,0);
		arrowIcon.setLayoutParams(lp);
	}

	public interface OnItemClickListener{
		void onItemClick(int position, WindowAdapter adapter);
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		mOnItemClickListener = onItemClickListener;
	}
}
