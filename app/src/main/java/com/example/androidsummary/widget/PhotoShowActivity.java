package com.example.androidsummary.widget;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.androidsummary.R;

public class PhotoShowActivity extends Activity implements View.OnTouchListener {
    private ImageView iv_show;

    private RectF rectF;//代表ImageView所在的矩形区域

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_photo_show);

        byte[] photo = getIntent().getByteArrayExtra("photo");

        iv_show = (ImageView) findViewById(R.id.iv_show);
        //只有设置这个模式的ImageView才支持缩放
        iv_show.setScaleType(ImageView.ScaleType.MATRIX);
        iv_show.setImageBitmap(BitmapFactory.decodeByteArray(photo, 0, photo.length));

        iv_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoShowActivity.this.finish();
            }
        });

        iv_show.setOnTouchListener(this);


        rectF = new RectF();
        rectF.set(iv_show.getX(),iv_show.getY(),iv_show.getX()+iv_show.getWidth(),iv_show.getY()+iv_show.getHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if (event.getX()>iv_show.getX()&&event.getX()<(iv_show.getX()+iv_show.getWidth())
                    &&event.getY()>iv_show.getY() && event.getY()<iv_show.getY()+iv_show.getHeight()){
                return super.onTouchEvent(event);
            }else{
                this.finish();
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 多点触碰
     */
    private Matrix savedMatrix = new Matrix();
    private Matrix matrix = new Matrix();
    private PointF startPoint = new PointF();
    private PointF midPoint;
    private int startLength;
    private int mode;

    private final int SINGLE = 1;
    private final int DOUBLE = 2;
    private final int NONE = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView iv = (ImageView) v;
        // 进行与操作 可以判断多点触碰 不进行与操作只能判断单点触碰
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:// 第一个手指按下时
                savedMatrix.set(iv.getImageMatrix());// 保存图片当前的matrix
                //PointF类，获得手指按下的初始点
                startPoint.set(event.getX(), event.getY());
                mode = SINGLE;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //两个手指的初始距离
                startLength = (int) distance(event);
                savedMatrix.set(iv.getImageMatrix());
                //两根手指的中间点
                midPoint = middle(event);
                mode = DOUBLE;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == SINGLE) {
                    // 单手指滑动
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - startPoint.x, event.getY()
                            - startPoint.y);
                } else if (mode == DOUBLE) {
                    float newLength = distance(event);
                    matrix.set(savedMatrix);
                    float scale = newLength / startLength;
                    matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                }
        }
        iv.setImageMatrix(matrix);
        return true;
    }

    //计算中点位置
    private PointF middle(MotionEvent event){
        PointF p =new PointF();
        float x=event.getX(0)+event.getX(1);
        float y=event.getY(0)+event.getY(1);
        p.set(x / 2, y / 2);
        return p;
    }
    //滑动距离
    private float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
}

