package com.example.androidsummary.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.androidsummary.R;

public class SlidingListView extends ListView
{
//    private static final String TAG = "QQlistView";

    // private static final int VELOCITY_SANP = 200;
    // private VelocityTracker mVelocityTracker;
    /**
     * 用户滑动的最小距离
     */
    private int touchSlop;

    /**
     * 是否响应滑动
     */
    private boolean isSliding;

    /**
     * 手指按下时的x坐标
     */
    private int xDown;
    /**
     * 手指按下时的y坐标
     */
    private int yDown;
    /**
     * 手指移动时的x坐标
     */
    private int xMove;
    /**
     * 手指移动时的y坐标
     */
    private int yMove;

    private LayoutInflater mInflater;

    private PopupWindow mPopupWindow;
    private int mPopupWindowHeight;
    private int mPopupWindowWidth;

    private TextView mDelBtn;
    /**
     * 为删除按钮提供一个回调接口
     */
    private DelButtonClickListener mListener;

    /**
     * 当前手指触摸的View
     */
    private View mCurrentView;

    /**
     * 当前手指触摸的位置
     */
    private int mCurrentViewPos;

    public SlidingListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mInflater = LayoutInflater.from(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();// 用户滑动的最小距离???

        View view = mInflater.inflate(R.layout.delete_btn, null);
        mDelBtn = (TextView) view.findViewById(R.id.id_item_btn);

        mPopupWindow = new PopupWindow(view,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        /*
         * 先调用下measure,否则拿不到宽和高
         */
        mPopupWindow.getContentView().measure(0, 0);
        mPopupWindowHeight = mPopupWindow.getContentView().getMeasuredHeight();
        mPopupWindowWidth = mPopupWindow.getContentView().getMeasuredWidth();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        // int action = ev.getAction();
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                xDown = x;
                yDown = y;

                // 如果当前popupWindow显示，则直接隐藏，然后屏蔽ListView的touch事件的下传
                if (mPopupWindow.isShowing())
                {
                    dismissPopWindow();
                    return false;
                }

                // 获得当前手指按下时的item位置
                mCurrentViewPos = pointToPosition(xDown, yDown);
                // 获得当前手指按下时的item
                System.out.println(mCurrentViewPos);
                System.out.println(getFirstVisiblePosition());
                View view = getChildAt(mCurrentViewPos - getFirstVisiblePosition());
                mCurrentView = view;
                if (mDelBtn!=null && mCurrentView!=null){
                    mDelBtn.setLayoutParams(new LinearLayout.LayoutParams(150, mCurrentView.getHeight()));
                }
                break;

            case MotionEvent.ACTION_MOVE:
                xMove = x;
                yMove = y;
                int dx = xMove - xDown;
                int dy = yMove - yDown;

                // 判断是否从右到左滑动
                // 移动后的x坐标小于初始x坐标 且 x方向滑动的距离大于用户滑动的最小距离，y方向滑动的距离小于用户滑动的最小距离
                if (xMove < xDown && Math.abs(dx) > touchSlop*10
                        && Math.abs(dy) < touchSlop*5)
                {
                    isSliding = true;// 是否相应滑动
                }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        if (isSliding && mCurrentView!=null)
        {// 如果是从右到左的滑动才相应
            switch (ev.getAction())
            {
                case MotionEvent.ACTION_MOVE:

                    int[] location = new int[2];
                    // 获得当前item的位置 x y
                    mCurrentView.getLocationOnScreen(location);
                    // 设置popupWindow的动画
                    mPopupWindow.setAnimationStyle(R.style.anim_menu_buttombar);
                    mPopupWindow.update();
                    mPopupWindow.showAtLocation(mCurrentView, Gravity.LEFT
                                    | Gravity.TOP, location[0] + mCurrentView.getWidth(),
                            location[1]);
                    // 设置删除按钮的回调
                    mDelBtn.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            if (mListener != null)
                            {
                                mListener.clickHappend(mCurrentViewPos);
                                mPopupWindow.dismiss();
                            }
                        }
                    });
                    break;
                case MotionEvent.ACTION_UP:
                    isSliding = false;
                    break;
            }
            return true;
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 隐藏popupWindow
     */
    private void dismissPopWindow()
    {
        if (mPopupWindow != null && mPopupWindow.isShowing())
        {
            mPopupWindow.dismiss();
        }
    }

    public void setDelButtonClickListener(DelButtonClickListener listener)
    {
        mListener = listener;
    }

    public interface DelButtonClickListener
    {
        public void clickHappend(int position);
    }
}
