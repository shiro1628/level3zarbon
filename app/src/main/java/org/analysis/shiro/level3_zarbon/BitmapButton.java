package org.analysis.shiro.level3_zarbon;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.appcompat.widget.AppCompatButton; // AndroidX로 변경

public class BitmapButton extends AppCompatButton {

    public BitmapButton(Context context) {
        super(context);
        init(context);
    }

    public BitmapButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setBackgroundResource(R.drawable.zarbon3_2);

        float textSize = getResources().getDimension(R.dimen.text_size);
        setTextSize(textSize);
        setTextColor(Color.WHITE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setBackgroundResource(R.drawable.zarbon4_2);
                break;

            case MotionEvent.ACTION_UP:
                setBackgroundResource(R.drawable.zarbon3_2);
                break;
        }
        invalidate();
        return true;
    }
}
