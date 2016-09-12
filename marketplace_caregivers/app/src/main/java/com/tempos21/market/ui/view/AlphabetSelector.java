package com.tempos21.market.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tempos21.market.util.TLog;

public class AlphabetSelector extends View {

    private static final String INITIAL_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int FONT_SIZE = 16;
    private static final int ROW_SIZE = FONT_SIZE + 8;
    private String letters;
    private String availableLetters = "ADJKOWXSU";
    private Paint letterPaint;
    private char selectedLetter;
    private Paint selectedPaint;
    private Paint selectedLetterPaint;
    private float currentY;
    private float centerX;
    private Paint unavailableLetterPaint;
    private OnLetterSelectedListener onLetterSelectedListener;

    public AlphabetSelector(Context context) {
        super(context);
        init();
    }

    public AlphabetSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlphabetSelector(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLetters(INITIAL_SET);

        letterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        letterPaint.setColor(0xff4d4d4d);
        letterPaint.setTextSize(FONT_SIZE);
        letterPaint.setTextAlign(Align.CENTER);
        letterPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF,
                Typeface.BOLD));
        letterPaint.setStyle(Paint.Style.STROKE);
        letterPaint.setSubpixelText(true);

        selectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedPaint.setColor(0xff4d4d4d);
        selectedPaint.setStyle(Paint.Style.FILL);

        selectedLetterPaint = new Paint(letterPaint);
        selectedLetterPaint.setColor(Color.WHITE);

        unavailableLetterPaint = new Paint(letterPaint);
        unavailableLetterPaint.setColor(0x88ffffff);
    }

    public void setLetters(String lettersSet) {
        letters = lettersSet;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE
                || event.getAction() == MotionEvent.ACTION_UP) {
            int letterIndex = (int) event.getY() / ROW_SIZE;
            if (letterIndex >= 0 && letterIndex < letters.length()) {
                TLog.e("" + letterIndex + " of " + letters);
                String letter = letters.substring(letterIndex, letterIndex + 1);
                if (availableLetters.contains(letter)) {
                    selectedLetter = letters.charAt(letterIndex);
                    if (onLetterSelectedListener != null) {
                        onLetterSelectedListener.onLetterSelected(String
                                .valueOf(selectedLetter));
                    }
                }
            }
            invalidate();
        }

        return super.onTouchEvent(event);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        currentY = ROW_SIZE;
        centerX = getWidth() / 2;
        for (char letter : letters.toCharArray()) {
            if (availableLetters.contains(String.valueOf(letter))) {

                if (selectedLetter == letter) {
                    RectF selectedRect = new RectF(centerX - 12, currentY
                            - (FONT_SIZE) - 4, centerX + 12, currentY + 8);
                    canvas.drawRoundRect(selectedRect, 8, 8, selectedPaint);
                    canvas.drawText(String.valueOf(letter), centerX, currentY,
                            selectedLetterPaint);
                } else {
                    canvas.drawText(String.valueOf(letter), centerX, currentY,
                            letterPaint);
                }
            } else {
                canvas.drawText(String.valueOf(letter), centerX, currentY,
                        unavailableLetterPaint);
            }
            currentY += ROW_SIZE;

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    public String getAvailableLetters() {
        return availableLetters;
    }

    public void setAvailableLetters(String availableLetters) {
        this.availableLetters = availableLetters;
    }

    public OnLetterSelectedListener getOnLetterSelectedListener() {
        return onLetterSelectedListener;
    }

    public void setOnLetterSelectedListener(
            OnLetterSelectedListener onLetterSelectedListener) {
        this.onLetterSelectedListener = onLetterSelectedListener;
    }

    public interface OnLetterSelectedListener {
        public void onLetterSelected(String letter);
    }
}
