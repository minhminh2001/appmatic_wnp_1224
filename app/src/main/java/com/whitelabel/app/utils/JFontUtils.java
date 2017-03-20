package com.whitelabel.app.utils;

import android.content.Context;
import android.graphics.Typeface;

import com.whitelabel.app.model.TypefaceEntity;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by imaginato on 2015/6/10.
 */
public class JFontUtils {

    private static final ConcurrentHashMap<String, TypefaceEntity> typefaceMap;

    static {
        typefaceMap = new ConcurrentHashMap<String, TypefaceEntity>();

        TypefaceEntity gothamBook = new TypefaceEntity();
        gothamBook.fileName = "fonts/GothamBook.ttf";
        typefaceMap.put("0", gothamBook);

        TypefaceEntity gothamLight = new TypefaceEntity();
        gothamLight.fileName = "fonts/GothamLight.ttf";
        typefaceMap.put("1", gothamLight);

        TypefaceEntity gothamMedium = new TypefaceEntity();
        gothamMedium.fileName = "fonts/GothamMedium.otf";
        typefaceMap.put("2", gothamMedium);

        TypefaceEntity helveticalBold = new TypefaceEntity();
        helveticalBold.fileName = "fonts/HelveticaBold.Ttf";
        typefaceMap.put("3", helveticalBold);


        TypefaceEntity helveticalNeueLTProLt = new TypefaceEntity();
        helveticalNeueLTProLt.fileName = "fonts/HelveticaNeueLTPro-Lt.otf";
        typefaceMap.put("6", helveticalNeueLTProLt);

        TypefaceEntity montserratRegular = new TypefaceEntity();
        montserratRegular.fileName = "fonts/MontserratRegular.ttf";
        typefaceMap.put("7", montserratRegular);

        TypefaceEntity openSansBold = new TypefaceEntity();
        openSansBold.fileName = "fonts/OpenSans-Bold.ttf";
        typefaceMap.put("8", openSansBold);

        TypefaceEntity openSansBoldItalic = new TypefaceEntity();
        openSansBoldItalic.fileName = "fonts/OpenSans-BoldItalic.ttf";
        typefaceMap.put("9", openSansBoldItalic);

        TypefaceEntity openSansCondLight = new TypefaceEntity();
        openSansCondLight.fileName = "fonts/OpenSans-CondLight.ttf";
        typefaceMap.put("10", openSansCondLight);

        TypefaceEntity openSansCondLightItalic = new TypefaceEntity();
        openSansCondLightItalic.fileName = "fonts/OpenSans-CondLightItalic.ttf";
        typefaceMap.put("11", openSansCondLightItalic);

        TypefaceEntity openSansCondLighttalic = new TypefaceEntity();
        openSansCondLighttalic.fileName = "fonts/OpenSans-CondLighttalic.ttf";
        typefaceMap.put("12", openSansCondLighttalic);

        TypefaceEntity openSansExtraBold = new TypefaceEntity();
        openSansExtraBold.fileName = "fonts/OpenSans-ExtraBold.ttf";
        typefaceMap.put("13", openSansExtraBold);

        TypefaceEntity openSansExtraBoldItalic = new TypefaceEntity();
        openSansExtraBoldItalic.fileName = "fonts/OpenSans-ExtraBoldItalic.ttf";
        typefaceMap.put("14", openSansExtraBoldItalic);

        TypefaceEntity openSansItalic = new TypefaceEntity();
        openSansItalic.fileName = "fonts/OpenSans-Italic.ttf";
        typefaceMap.put("15", openSansItalic);

        TypefaceEntity openSansLight = new TypefaceEntity();
        openSansLight.fileName = "fonts/OpenSans-Light.ttf";
        typefaceMap.put("16", openSansLight);

        TypefaceEntity openSansLightItalic = new TypefaceEntity();
        openSansLightItalic.fileName = "fonts/OpenSans-LightItalic.ttf";
        typefaceMap.put("17", openSansLightItalic);

        TypefaceEntity openSansRegular = new TypefaceEntity();
        openSansRegular.fileName = "fonts/OpenSans-Regular.ttf";
        typefaceMap.put("18", openSansRegular);

        TypefaceEntity openSansSemibold = new TypefaceEntity();
        openSansSemibold.fileName = "fonts/OpenSans-Semibold.ttf";
        typefaceMap.put("19", openSansSemibold);

        TypefaceEntity openSansSemiboldItalic = new TypefaceEntity();
        openSansSemiboldItalic.fileName = "fonts/OpenSans-SemiboldItalic.ttf";
        typefaceMap.put("20", openSansSemiboldItalic);

        TypefaceEntity proximaNovaRegular = new TypefaceEntity();
        proximaNovaRegular.fileName = "fonts/ProximaNovaRegular.otf";
        typefaceMap.put("21", proximaNovaRegular);

        TypefaceEntity gothamBoldItalic = new TypefaceEntity();
        gothamBoldItalic.fileName = "fonts/GothamBoldItalic.ttf";
        typefaceMap.put("22", gothamBoldItalic);

        TypefaceEntity arvoBold = new TypefaceEntity();
        arvoBold.fileName = "fonts/ArvoBold.ttf";
        typefaceMap.put("23", arvoBold);

        TypefaceEntity arvoRegular = new TypefaceEntity();
        arvoRegular.fileName = "fonts/ArvoRegular.ttf";
        typefaceMap.put("26", arvoRegular);

        TypefaceEntity ptSansBold = new TypefaceEntity();
        ptSansBold.fileName = "fonts/Lato-Bold.ttf";
        typefaceMap.put("27", ptSansBold);


        TypefaceEntity LatoRegular = new TypefaceEntity();
        LatoRegular.fileName = "fonts/Lato-Regular.ttf";
        typefaceMap.put("28", LatoRegular);

        TypefaceEntity LatoBold = new TypefaceEntity();
        LatoBold.fileName = "fonts/Lato-Bold.ttf";
        typefaceMap.put("29", LatoBold);
        TypefaceEntity LatoMedium = new TypefaceEntity();
        LatoMedium.fileName = "fonts/Lato-Medium.ttf";
        typefaceMap.put("30", LatoMedium);


        TypefaceEntity ptSansRegular = new TypefaceEntity();
        ptSansRegular.fileName = "fonts/Lato-Regular.ttf";
        typefaceMap.put("34", ptSansRegular);
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