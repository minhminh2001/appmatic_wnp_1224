package com.common.utils;

import android.content.Context;
import android.graphics.Typeface;

import com.common.model.TypefaceEntity;

import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by imaginato on 2015/6/10.
 */
public class JFontUtils {

    private static final ConcurrentHashMap<String, TypefaceEntity> typefaceMap;

    static {
        typefaceMap = new ConcurrentHashMap<>();
        //expalme
        TypefaceEntity gothamMedium = new TypefaceEntity();
        gothamMedium.fileName = "fonts/GothamMedium.otf";
        typefaceMap.put("2", gothamMedium);

    }

    public static Typeface getTypeface(Context context, int fontIndex) {
        if (context == null || context.getAssets() == null) {
            return null;
        }

        if (typefaceMap == null || typefaceMap.isEmpty()) {
            return null;
        }

        TypefaceEntity entity = typefaceMap.get(fontIndex + "");
        if (entity == null) {
            return null;
        }

        if (entity.typeface != null) {
            return entity.typeface;
        }

        String fileName = entity.fileName;
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), fileName);
        entity.typeface = typeface;

        return typeface;
    }
}