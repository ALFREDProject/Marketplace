package com.tempos21.mymarket.sdk.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ProfileImageView extends ImageView {

    private Bitmap bitmapMutable;
    private Bitmap roundedBitmap;
    private Drawable drawable;

    public ProfileImageView(Context context) {
        super(context);
        init();
    }

    public ProfileImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProfileImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public static Bitmap getCircleCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap finalBitmap;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius) {
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        } else {
            finalBitmap = bitmap;
        }
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(), finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(), finalBitmap.getHeight());
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xFFBAB399);
        canvas.drawCircle(finalBitmap.getWidth() / 2, finalBitmap.getHeight() / 2, finalBitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);
        return output;
    }

    private void init() {
        drawable = getDrawable();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        if (drawable == null) {
            return;
        } else if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        if (roundedBitmap == null) {
            Bitmap bitmapDrawable = ((BitmapDrawable) drawable).getBitmap();
            bitmapMutable = bitmapDrawable.copy(Bitmap.Config.ARGB_8888, true);
        }
        if (roundedBitmap == null) {
            roundedBitmap = getCircleCroppedBitmap(bitmapMutable, getWidth());
        }
        canvas.drawBitmap(roundedBitmap, 0, 0, null);
    }
}
