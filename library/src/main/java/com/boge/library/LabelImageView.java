package com.boge.library;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author boge
 * @version 1.0
 * @date 2017/1/4
 */

public class LabelImageView extends ImageView {
    private LabelViewHelper mLabelViewHelper;

    public LabelImageView(Context context) {
        this(context, null);
    }

    public LabelImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mLabelViewHelper = new LabelViewHelper(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mLabelViewHelper.drawLabel(this, canvas);
    }
}
