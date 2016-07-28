package com.example.androidsummary.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.androidsummary.R;

/**
 * Created by 伦小丹 on 2015/12/25 0025.
 */
public class BloggerAddDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private EditText mUserIdView;
    private TextView mConfirmView;
    //自定义接口  监听确定事件
    private OnConfirmListener mOnConfirmListener;
    public interface OnConfirmListener {

        void onConfirm(String result);
    }


    public BloggerAddDialog(Context context, OnConfirmListener onConfirmListener) {
        super(context, R.style.Theme_Light_FullScreenDialogAct);
        this.mContext = context;
        this.mOnConfirmListener = onConfirmListener;

        setContentView(R.layout.dialog_addblogger);
        mUserIdView = (EditText) this.findViewById(R.id.et_userid);
        mConfirmView = (TextView) this.findViewById(R.id.btn_confirm);
        mConfirmView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                mOnConfirmListener.onConfirm(String.valueOf(mUserIdView.getText()));
//                KeyBoardUtils.closeKeybord(mUserIdView, mContext);
                dismiss();
                break;

            default:
                break;
        }

    }

    /**
     * 显示在底部
     */
    public void showDialogBottom(float dimAmount) {
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = dimAmount;
        window.setAttributes(lp);
        this.show();
    }
}
