/*
   Copyright Sergi Martinez (@sergiandreplace)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   
   === DISCLAIMER ===
   
   This view is "strongly" based in the explanation give by Eric Burke 
   (@burke_eric) on "Taming Android UI". You can find this presentation on 
   http://www.youtube.com/watch?v=jF6Ad4GYjRU or checking his website: 
   http://tamingandroid.com/ (check the website, is really worth it) 
 
 */
package com.tempos21.market.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class RoundeCornersImage extends View {

    /*
     * These two constants control the ratio between height and width. They are
     * set for a Magic: The Gathering card :) You can change the constants if
     * you want or set the programatically via the setters. Up to you.
     */
    private static int IMAGE_WIDTH_RATIO = 100;
    private static int IMAGE_HEIGHT_RATIO = 100;

    // Controls the roundness of the border
    private static float BORDER_RADIO_RATIO = 18f;
    private static Bitmap shadow;
    private int realImageWidth = IMAGE_WIDTH_RATIO;
    private int realImageHeight = IMAGE_HEIGHT_RATIO;
    // The image assigned from the outside
    private Bitmap image;
    // The image to show in nothing is shown. Just initialize it to an image
    // of your project
    private Drawable placeholder;
    // The resulting framed image
    private Bitmap framedImage;
    // Fields for ratios
    private int imageWidthRatio = IMAGE_WIDTH_RATIO;
    private int imageHeightRatio = IMAGE_HEIGHT_RATIO;
    private float borderRadioRatio = BORDER_RADIO_RATIO;
    private boolean hasShadow;

    // Three nice constructors that make things easier. Just inherited.

    public RoundeCornersImage(Context context) {
        super(context);
    }

    public RoundeCornersImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundeCornersImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /***
     * Starting from an initial size, calculates the real size of the View to be
     * used in order to fit in the assigned space and maintain image ratio
     *
     * @param initialWidth
     * @param initialHeight
     */
    protected void calculateRealSize(int initialWidth, int initialHeight) {
        int proposedHeight = initialWidth * imageHeightRatio / imageWidthRatio;
        int proposedWidth = initialHeight * imageWidthRatio / imageHeightRatio;

        if (proposedHeight > initialHeight) {
            realImageHeight = initialHeight;
            realImageWidth = proposedWidth;
        } else {
            realImageHeight = proposedHeight;
            realImageWidth = initialWidth;
        }

    }

    /***
     * We just remove the image we obtained in order to redraw it with the new
     * size
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        framedImage = null;
    }

    /***
     * Generate the bitmap if needed and draw it
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (placeholder == null && getImage() == null)
            return;

        if (framedImage == null && getWidth() > 0 && getHeight() > 0) {
            createFramedImage(getWidth(), getHeight());
        }
        if (framedImage != null) {
            canvas.drawBitmap(framedImage, 0, 0, null);
            if (hasShadow) {
                if (shadow == null) {
                    createShadow(getWidth());
                }
                canvas.drawBitmap(shadow, 0, 0, null);
            }
            // If you need other effects, I suggest create separated
            // functions createXXX to generate bitmaps and draw them here
            // sequentially
        }

    }

    /***
     * Returns the correct size of the control when needed (Basically
     * maintaining the ratio)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = getDefaultSize(getSuggestedMinimumWidth(),
                widthMeasureSpec);
        int measuredHeight = getDefaultSize(getSuggestedMinimumHeight(),
                heightMeasureSpec);

        calculateRealSize(measuredWidth, measuredHeight);

        setMeasuredDimension(realImageWidth, realImageHeight);

    }

    private void createShadow(int size) {
        Bitmap output = Bitmap
                .createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        RectF outerRect = new RectF(0, size * 0.6f, size, size);
        RectF upperRect = new RectF(0, size * 0.6f, size, size * 0.8f);
        float outerRadius = size / 8f;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0x88000000);

        canvas.drawRoundRect(outerRect, outerRadius, outerRadius, paint);
        canvas.drawRect(upperRect, paint);
        shadow = output;
    }

    /***
     * Here is where the magic of the drawing occurs. Come with me to know a
     * phantasy world where everything is possible (in terms of graphics)
     *
     * @param initialWidth
     * @param initialHeight
     */
    private void createFramedImage(int initialWidth, int initialHeight) {
        // recalculate the appropiate size just in case
        calculateRealSize(initialWidth, initialHeight);
        // and calculate the border radius
        float outerRadius = realImageWidth / borderRadioRatio;

        // generate a new bitmap that will be used for drawing on it
        Bitmap output = Bitmap.createBitmap(realImageWidth, realImageHeight,
                Bitmap.Config.ARGB_8888);

        // Create a new canvas for the bitmap
        Canvas canvas = new Canvas(output);

        // Generat a rectangle with the size calculated
        RectF outerRect = new RectF(0, 0, realImageWidth, realImageHeight);

        // Create a paint style and config it. Notice the ANTI_ALIAS stuff
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);

        // Draw the red rectangle with rounded corners in the canvas
        canvas.drawRoundRect(outerRect, outerRadius, outerRadius, paint);

        // Setup a compositing style for the paint. Try different values. It's
        // fun!
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // We save the current layer. Not sure why.
        canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);

        // We got the appopiate drawable, image or placeholder
        Drawable imageDrawable = (getImage() != null) ? new BitmapDrawable(
                getImage()) : placeholder;
        // We set the bounds to be the same than the bitmap
        imageDrawable.setBounds(0, 0, realImageWidth, realImageHeight);
        // And draw it in the canvas
        imageDrawable.draw(canvas);

        // That restores the compositing mode to normal and do some other stuff
        canvas.restore();

        framedImage = output;

    }

    // A bunch of setters and getters
    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
        framedImage = null;
        invalidate();
    }

    public void setImage(int id) {
        setImage(BitmapFactory.decodeResource(getContext().getResources(), id));

    }

    public int getImageHeightRatio() {
        return imageHeightRatio;
    }

    public void setImageHeightRatio(int imageHeightRatio) {
        this.imageHeightRatio = imageHeightRatio;
    }

    public int getImageWidthRatio() {
        return imageWidthRatio;
    }

    public void setImageWidthRatio(int imageWidthRatio) {
        this.imageWidthRatio = imageWidthRatio;
    }

    public float getBorderRadioRatio() {
        return borderRadioRatio;
    }

    public void setBorderRadioRatio(float borderRadioRatio) {
        this.borderRadioRatio = borderRadioRatio;
    }

    // A quick call for squaring
    public void setImageSquareRatio() {
        this.imageHeightRatio = 100;
        this.imageWidthRatio = 100;
    }

    public boolean isHasShadow() {
        return hasShadow;
    }

    public void setHasShadow(boolean hasShadow) {

        this.hasShadow = hasShadow;
        framedImage = null;
        invalidate();
    }
}
