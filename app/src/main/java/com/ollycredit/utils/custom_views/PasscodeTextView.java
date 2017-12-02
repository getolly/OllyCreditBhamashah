package com.ollycredit.utils.custom_views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.ollycredit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dilpreet on 24/10/17.
 */

public class PasscodeTextView  extends View {

    private Paint emptyCirclePaint, fillCirclePaint;
    private final int PASSWORD_LENGTH = 4;
    private List<String> passwordList;
    private boolean isPasscodeVisible;
    private PassCodeListener passCodeListener;

    public PasscodeTextView(Context context) {
        super(context);
        init();
    }

    public PasscodeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PasscodeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setPassCodeListener(PassCodeListener passCodeListener) {
        this.passCodeListener = passCodeListener;
    }

    private void init() {
        emptyCirclePaint = new Paint();
        emptyCirclePaint.setColor(ContextCompat.getColor(getContext(), R.color.white_60));
        emptyCirclePaint.setAntiAlias(true);
        emptyCirclePaint.setStyle(Paint.Style.FILL);
        emptyCirclePaint.setStrokeWidth(1f);

        fillCirclePaint = new Paint();
        fillCirclePaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        fillCirclePaint.setAntiAlias(true);
        fillCirclePaint.setStyle(Paint.Style.FILL);
        fillCirclePaint.setStrokeWidth(1f);

        passwordList = new ArrayList<>();
        isPasscodeVisible = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int stackSize = passwordList.size();
        int xPosition = getWidth() / (PASSWORD_LENGTH * 2);
        for (int i = 1; i <= PASSWORD_LENGTH; i++) {
            if (stackSize >= i) {
                if (!isPasscodeVisible) {
                    canvas.drawCircle(xPosition, getHeight() / 2, 10f, fillCirclePaint);
                } else {
                    canvas.drawText(passwordList.get(i - 1), xPosition , getHeight() / 2 +
                            getHeight() / 8, fillCirclePaint);
                }
            } else {
                canvas.drawCircle(xPosition , getHeight() / 2, 10f, emptyCirclePaint);
            }
            xPosition += getWidth() / PASSWORD_LENGTH;
        }
    }

    public void enterCode(String character) {
        if (passwordList.size() < PASSWORD_LENGTH) {
            passwordList.add(character);
            invalidate();
        }

        if (passwordList.size() == PASSWORD_LENGTH && passCodeListener != null) {
            passCodeListener.passCodeEntered(getPasscode());
        }

    }

    public String getPasscode() {
        StringBuilder builder = new StringBuilder();
        for (String character : passwordList) {
            builder.append(character);
        }
        return builder.toString();
    }

    public void clearPasscodeField() {
        passwordList.clear();
        invalidate();
    }

    public void backSpace() {
        if (passwordList.size() > 0) {
            passwordList.remove(passwordList.size() - 1);
            invalidate();
        }
    }

    public void revertPassCodeVisibility() {
        isPasscodeVisible = !isPasscodeVisible;
        invalidate();
    }

    public boolean passcodeVisible() {
        return isPasscodeVisible;
    }

    public interface PassCodeListener {
        void passCodeEntered(String passcode);
    }
}
