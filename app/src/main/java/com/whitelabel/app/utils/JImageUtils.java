package com.whitelabel.app.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.whitelabel.app.GlobalData;
import com.whitelabel.app.R;
import com.whitelabel.app.application.WhiteLabelApplication;
import com.whitelabel.app.network.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import okhttp3.HttpUrl;

/**
 * Created by imaginato on 2015/6/11.
 */
public class JImageUtils {
    private final static String TAG = "JImageUtils";

    public  static  Drawable getThemeCircle(Context context){
        GradientDrawable drawable= (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.bg_cart_number);
        drawable.setColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getKeyColor());
        return drawable;
    }
    public static Drawable getColorCircle(Context context,int color){
        GradientDrawable drawable= (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.bg_cart_number);
        drawable.setColor(color);
        return drawable;
    }

    public static Bitmap drawableToBitmap (Drawable drawable,int defaultWidth,int  defaultHeight) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }
        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : defaultWidth;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : defaultHeight;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static  Drawable getButtonBackgroudSolidDrawable(Context context){
        StateListDrawable drawable=new StateListDrawable();
        GradientDrawable  normal= new GradientDrawable();
        normal.setColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getKeyColor());
        normal.setCornerRadius(JScreenUtils.dip2px(context,2));
        GradientDrawable  pressed= new GradientDrawable();
        pressed.setCornerRadius(JScreenUtils.dip2px(context,2));
        pressed.setColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getButtonColorTapping());
        drawable.addState(new int[]{android.R.attr.state_pressed,android.R.attr.state_enabled},pressed);
        drawable.addState(new int[]{android.R.attr.state_enabled},normal);
        drawable.addState(new int[]{},normal);
        return drawable;
    }

    public static  Drawable  getbuttonBakcgroundStrokeDrawable(Context context){
        StateListDrawable drawable=new StateListDrawable();
        GradientDrawable  normal= (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.button_default_shape);
        normal.setColor(ContextCompat.getColor(context,R.color.transparent00));
        normal.setStroke(JScreenUtils.dip2px(context,1.5f), WhiteLabelApplication.getAppConfiguration().getThemeConfig().getKeyColor());
//        GradientDrawable  pressed= (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.button_default_shape);
//        pressed.setColor(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getButtonClickColor());
        drawable.addState(new int[]{},normal);
//        drawable.addState(new int[]{android.R.attr.state_pressed,android.R.attr.state_enabled},pressed);
        return drawable;
    }


    public static ColorStateList getThemeTextColorDrawable(int defaultColor){
        int[] colors = new int[] { WhiteLabelApplication.getAppConfiguration().getThemeConfig().getKeyColor(), defaultColor,defaultColor};
        int[][] states = new int[3][];
        states[0] = new int[] { android.R.attr.state_enabled, android.R.attr.state_selected };
        states[1] = new int[] { android.R.attr.state_enabled };
        states[2] = new int[] {};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }


    public static Drawable getThemeIconSelector(Drawable drawable,int defaultColor){
        int[] colors = new int[] { WhiteLabelApplication.getAppConfiguration().getThemeConfig().getKeyColor(), defaultColor,defaultColor};
        int[][] states = new int[3][];
        states[0] = new int[] { android.R.attr.state_enabled, android.R.attr.state_selected };
        states[1] = new int[] { android.R.attr.state_enabled };
        states[2] = new int[] {};
        ColorStateList colorList = new ColorStateList(states, colors);
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
         DrawableCompat.setTintList(wrappedDrawable, colorList);
        return wrappedDrawable;
    }



    public static Drawable getThemeIconTapping(Drawable drawable,int defaultColor,int  tappingColor){
        int[] colors = new int[] {tappingColor , defaultColor,defaultColor};
        int[][] states = new int[3][];
        states[0] = new int[] { android.R.attr.state_enabled, android.R.attr.state_pressed };
        states[1] = new int[] { android.R.attr.state_enabled };
        states[2] = new int[] {};
        ColorStateList colorList = new ColorStateList(states, colors);
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colorList);
        return wrappedDrawable;
    }



    public static Drawable getThemeIcon(Context context,int drawable){
        Drawable drawable1=ContextCompat.getDrawable(context, drawable);
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable1);
        DrawableCompat.setTintList(wrappedDrawable,
                ColorStateList.valueOf(WhiteLabelApplication.getAppConfiguration().getThemeConfig().getKeyColor()));
        return wrappedDrawable;
    }

    /**
     * @param context
     * @param imageUrl
     * @param destWidth  -1=>original size
     * @param destHeight -1=>original size
     * @return
     */
    public static String getImageServerUrlByWidthHeight(Context context, String imageUrl, int destWidth, int destHeight) {
        if (context == null || TextUtils.isEmpty(imageUrl)) {
            return null;
        }
        String url = "";
        try {
            if (imageUrl.contains("http") || imageUrl.contains("https")) {
                url = HttpUrl.parse(imageUrl).newBuilder().build().toString();
            } else {
                HttpUrl.Builder urlBuilder = HttpUrl.parse(GlobalData.serviceRequestUrl).newBuilder()
                        .addPathSegments(GlobalData.downloadImagePath)
                        .addQueryParameter("name", imageUrl);

                if (destWidth != -1) {
                    urlBuilder.addQueryParameter("width", Integer.toString(destWidth));
                }
                if (destHeight != -1) {
                    urlBuilder.addQueryParameter("height", Integer.toString(destHeight));
                }
                url = urlBuilder.build().toString();

            }
        } catch (Exception ex) {
            JLogUtils.e(TAG, "getImageServerUrlByWidthHeight", ex);
        }
        JLogUtils.d("image", url + "");
        return url;
    }

    /**
     * @param imageUrl http://bbs.lidroid.com/static/image/common/logo.png
     */
    public static void downloadImageFromServerByUrl(Context context, ImageLoader loader, ImageView imageView, String imageUrl) {
        loader.loadImage(getImageServerUrlByWidthHeight(context, imageUrl, -1, -1), imageView);
    }

    public static void downloadImageFromServerListener(Context context, ImageLoader loader, ImageView imageView, String imageUrl, final RequestListener<String, Bitmap> listener) {
        loader.loadImage(getImageServerUrlByWidthHeight(context, imageUrl, -1, -1), imageView, listener);
    }

    public static void downloadImageFromServerListener(Context context, ImageLoader loader, ImageView imageView, String imageUrl, int width, int height, final RequestListener<String, Bitmap> listener) {
        loader.loadImage(getImageServerUrlByWidthHeight(context, imageUrl, width, height), imageView, listener);
    }

    public static void downloadImageFromServerListener(final Context context, String imageUrl, int width, int height, final SimpleTarget<Bitmap> loadImageListener) {
        final String url = getImageServerUrlByWidthHeight(context, imageUrl, width, height);
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                Glide.with(context)
                        .load(url)
                        .asBitmap()
                        .centerCrop()
                        .into(loadImageListener);
            }
        };
        mainHandler.post(myRunnable);
    }

    public static void displayFromDrawable(Context context, int imageId, ImageLoader loader, ImageView imageView) {
        loader.loadImage(context, imageId, imageView);
    }

    /**
     * @param imageUrl http://bbs.lidroid.com/static/image/common/logo.png
     */
    public static void downloadImageFromServerByUrl(Context context, ImageLoader loader, ImageView imageView, String imageUrl, int destWidth, int destHeight) {
        loader.loadImage(getImageServerUrlByWidthHeight(context, imageUrl, destWidth, destHeight), imageView);
    }

    public static Bitmap scaleImage(String fileName, float width, float height) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inDensity = 240;
        op.inSampleSize = 2;
        Bitmap bm = BitmapFactory.decodeFile(fileName, op);
        return scaleBitmap(bm, width, height);
    }

    public static Bitmap scaleBitmap(Bitmap bm, float width, float height) {
        if (bm == null) {
            return null;
        }
        int bmWidth = bm.getWidth();
        int bmHeight = bm.getHeight();
        float scaleWidth = width / bmWidth;
        float scaleHeight = height / bmHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        if (scaleWidth == 1 && scaleHeight == 1) {
            return bm;
        } else {
            Bitmap resizeBitmap = Bitmap.createBitmap(bm, 0, 0, bmWidth, bmHeight, matrix, false);
            bm.recycle();
            bm.setDensity(240);
            return resizeBitmap;
        }
    }

    public static Bitmap zipImage(int be, String fileName, Boolean overWrite) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        Bitmap bitmap = BitmapFactory.decodeFile(fileName, options);

        options.inJustDecodeBounds = false;
        options.inSampleSize = be;
        JLogUtils.d(TAG, "inSampleSize:=" + options.inSampleSize);

        bitmap = BitmapFactory.decodeFile(fileName, options);

        if (overWrite) {
            File file = new File(fileName);
            try {
                FileOutputStream out = new FileOutputStream(file);

                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                    out.flush();
                    out.close();
                }
            } catch (FileNotFoundException ex) {
                JLogUtils.e(TAG, "zipImage", ex);
            } catch (IOException ex) {
                JLogUtils.e(TAG, "zipImage", ex);
            }
            saveBitmap(bitmap, fileName, -1);

        }
        return bitmap;
    }

    public static Bitmap zipImage(float width, float height, float maxSize, String fileName, Boolean overWrite) {
        Bitmap bitmap = scaleImage(fileName, width, height);
        if (overWrite) {
            File file = new File(fileName);
            try {
                FileOutputStream out = new FileOutputStream(file);

                if (bitmap.compress(Bitmap.CompressFormat.PNG, 50, out)) {
                    out.flush();
                    out.close();
                }
            } catch (FileNotFoundException ex) {
                JLogUtils.e(TAG, "zipImage", ex);
            } catch (IOException ex) {
                JLogUtils.e(TAG, "zipImage", ex);
            }

        }
        return bitmap;
    }

    public static Bitmap zipImage(float width, float height, String fileName, Boolean overWrite) {
        return zipImage(width, height, -1, fileName, overWrite);
    }

    public static File saveBitmap(Bitmap bitmap, String fileName, float maxSize) {
        if (bitmap == null) {
            return null;
        }

        File file = null;
        byte[] buffer;
        RandomAccessFile accessFile = null;

        file = new File(fileName);
        int quality = 100;
        ByteArrayOutputStream steam;
        steam = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, steam);
        if (maxSize != -1) {
            while (steam.size() > maxSize * 1024) {
                steam = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, quality, steam);

                quality = quality - 20;
            }
        }
        buffer = steam.toByteArray();
        try {
            accessFile = new RandomAccessFile(file, "rw");
            accessFile.write(buffer);
            steam.close();
            accessFile.close();
        } catch (Exception ex) {
            JLogUtils.e(TAG, "saveBitmap", ex);
            return null;
        }

        return file;
    }

    public static Drawable getDrawable(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(id, null);
        } else {
            return context.getResources().getDrawable(id);
        }
    }


}
