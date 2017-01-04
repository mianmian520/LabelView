package com.boge.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author boge
 * @version 1.0
 * @date 2017/1/4
 */

public class LabelViewHelper {

    private static final int ROTATE_LEFT = -45;
    private static final int ROTATE_RIGHT = 45;

    private static final int STYLE_NORMAL = 0;
    private static final int STYLE_ITALIC = 1;
    private static final int STYLE_BOLD = 2;

    /***标题*/
    private String mTextTitle;
    /***标题颜色*/
    private int mTextTitleColor;
    /***标题大小*/
    private float mTextTitleSize;
    /***标题风格*/
    private int mTextTitleStyle;

    private Paint mTextTitlePaint;

    private Rect mTextTitleRect;

    /***内容*/
    private String mTextContent;
    /***内容颜色*/
    private int mTextContentColor;
    /***内容大小*/
    private float mTextContentSize;
    /***内容风格*/
    private int mTextContentStyle;

    private Paint mTextContentPaint;

    private Rect mTextContentRect;

    private Paint mBgTrianglePaint;

    /***背景颜色*/
    private int mBgTriangleColor;

    private float mTopPadding;
    private float mBottomPadding;
    private float mCenterPadding;
    private float mTopDistance;

    private float mRouteDegrees;

    /***背景宽度*/
    private int mBgTriangleWidth;
    /***背景高度*/
    private int mBgTriangleHeight;

    public LabelViewHelper(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LabelView);
        mTextTitle = typedArray.getString(R.styleable.LabelView_textTitle);
        mTextTitleColor = typedArray.getColor(R.styleable.LabelView_textTitleColor, Color.WHITE);
        mTextTitleSize = typedArray.getDimension(R.styleable.LabelView_textTitleSize,
                context.getResources().getDimensionPixelSize(R.dimen.default_label_title_size));
        mTextTitleStyle = typedArray.getInt(R.styleable.LabelView_textTitleStyle, STYLE_NORMAL);
        mTextContent = typedArray.getString(R.styleable.LabelView_textContent);
        mTextContentColor = typedArray.getColor(R.styleable.LabelView_textContentColor, Color.WHITE);
        mTextContentSize = typedArray.getDimension(R.styleable.LabelView_textContentSize,
                context.getResources().getDimensionPixelSize(R.dimen.default_label_content_size));
        mTextContentStyle = typedArray.getInt(R.styleable.LabelView_textContentStyle, STYLE_NORMAL);

        mBgTriangleColor = typedArray.getColor(R.styleable.LabelView_backgroundColor, Color.BLUE);

        mTopPadding = typedArray.getDimension(R.styleable.LabelView_labelTopPadding,
                context.getResources().getDimensionPixelSize(R.dimen.default_label_top_padding));
        mCenterPadding = typedArray.getDimension(R.styleable.LabelView_labelCenterPadding, 0);
        mBottomPadding = typedArray.getDimension(R.styleable.LabelView_labelBottomPadding,
                context.getResources().getDimensionPixelSize(R.dimen.default_label_bottom_padding));
        mTopDistance = typedArray.getDimension(R.styleable.LabelView_labelTopDistance, 0);
        mRouteDegrees = typedArray.getInt(R.styleable.LabelView_direction, ROTATE_LEFT);
        typedArray.recycle();
        initAllArt();

        resetAllMeasureSize();
    }

    public void drawLabel(View view, Canvas canvas) {
        if (canvas == null || view == null) {
            throw new IllegalArgumentException("LabelViewHelper draw canvas or view cant't be null!");
        }

        canvas.save();
        if (mRouteDegrees == ROTATE_LEFT){
            canvas.translate(-mBgTriangleWidth / 2, 0);
            //以px、py为轴心进行旋转，degrees控制旋转的角度
            canvas.rotate(mRouteDegrees, mBgTriangleWidth / 2, 0);
        }else if (mRouteDegrees == ROTATE_RIGHT){
            int rotateViewWH= (int) (mBgTriangleHeight * Math.sqrt(2));
            canvas.translate(view.getMeasuredWidth() - rotateViewWH, -mBgTriangleHeight);
            canvas.rotate(mRouteDegrees, 0, mBgTriangleHeight);
        }
        Path path = new Path();
        path.moveTo(0, mBgTriangleHeight);
        if (mTopDistance < 0) {
            // mTopDistance > 0 represents a trapezoid, otherwise represents a triangle.
            mTopDistance = 0;
        }
        path.lineTo(mBgTriangleWidth / 2  - mTopDistance, mTopDistance);
        path.lineTo(mBgTriangleWidth / 2  + mTopDistance, mTopDistance);
        path.lineTo(mBgTriangleWidth, mBgTriangleHeight);
        path.close();
        canvas.drawPath(path, mBgTrianglePaint);

        if (!TextUtils.isEmpty(mTextTitle)) {
            canvas.drawText(mTextTitle, (mBgTriangleWidth) / 2, mTopDistance + mTopPadding + mTextTitleRect.height(), mTextTitlePaint);
        }
        if (!TextUtils.isEmpty(mTextContent)) {
            canvas.drawText(mTextContent, (mBgTriangleWidth) / 2, (mTopDistance + mTopPadding + mTextTitleRect.height() + mCenterPadding + mTextContentRect.height()), mTextContentPaint);
        }

        canvas.restore();
    }


    public int getBgTriangleHeight() {
        return mBgTriangleHeight;
    }

    private void initAllArt() {
        mTextTitleRect = new Rect();
        mTextContentRect = new Rect();

        //绘制时抗锯齿
        mTextTitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextTitlePaint.setColor(mTextTitleColor);
        mTextTitlePaint.setTextSize(mTextTitleSize);
        //设置绘制文字的对齐方向 居中
        mTextTitlePaint.setTextAlign(Paint.Align.CENTER);
        if (mTextTitleStyle == STYLE_ITALIC) {
            //设置字体样式
            mTextTitlePaint.setTypeface(Typeface.SANS_SERIF);
        } else if (mTextTitleStyle == STYLE_BOLD) {
            mTextTitlePaint.setTypeface(Typeface.DEFAULT_BOLD);
        }

        mTextContentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextContentPaint.setColor(mTextContentColor);
        mTextContentPaint.setTextAlign(Paint.Align.CENTER);
        mTextContentPaint.setTextSize(mTextContentSize);
        if (mTextContentStyle == STYLE_ITALIC) {
            mTextContentPaint.setTypeface(Typeface.SANS_SERIF);
        } else if (mTextContentStyle == STYLE_BOLD) {
            mTextContentPaint.setTypeface(Typeface.DEFAULT_BOLD);
        }

        mBgTrianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgTrianglePaint.setColor(mBgTriangleColor);
    }

    private void resetAllMeasureSize() {
        if (!TextUtils.isEmpty(mTextTitle)) {
            mTextTitlePaint.getTextBounds(mTextTitle, 0, mTextTitle.length(), mTextTitleRect);
        }

        if (!TextUtils.isEmpty(mTextContent)) {
            mTextContentPaint.getTextBounds(mTextContent, 0, mTextContent.length(), mTextContentRect);
        }

        mBgTriangleHeight = (int) (mTopDistance + mTopPadding + mCenterPadding + mBottomPadding + mTextTitleRect.height() + mTextContentRect.height());
        mBgTriangleWidth = 2 * mBgTriangleHeight;
    }
}
