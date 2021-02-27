/*
 * Copyright (C) 2014 Marco Hernaiz Cao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package id.co.datascrip.app_collector_systems.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.util.SparseArray;

import java.util.HashMap;
import java.util.Map;

/**
 * The manager of roboto typefaces.
 *
 * @author Marco Hernaiz Cao
 */
public class Fonts {

    /*
    * Available fonts
    */
    public final static int ALLER_REGULAR = 0;
    public final static int ALLER_BOLD = 1;
    public final static int OPENSANS_REGULAR = 2;
    public final static int OPENSANS_BOLD = 3;
    public final static int OPENSANS_BOLD_ITALIC = 4;
    public final static int OPENSANS_EXTRA_BOLD = 5;
    public final static int OPENSANS_EXTRA_BOLD_ITALIC = 6;
    public final static int OPENSANS_SEMI_BOLD = 7;
    public final static int OPENSANS_SEMI_BOLD_ITALIC = 8;
    public final static int SOURCESANS_REGULAR = 9;
    public final static int SOURCESANS_BOLD = 10;
    public final static int SOURCESANS_BOLD_ITALIC = 11;
    public final static int TREBUCHET_REGULAR = 12;
    public final static int TREBUCHET_BOLD = 13;
    public final static int TREBUCHET_ITALIC = 14;
    public final static int TREBUCHET_BOLD_ITALIC = 15;
    public final static int FELBRIDGE_LIGHT = 16;
    public final static int FELBRIDGE_BOLD = 17;

    private final static Map<String, Integer> typeFacesMap;

    static {
        typeFacesMap = new HashMap<>();
        typeFacesMap.put("aller_regular", 0);
        typeFacesMap.put("aller_bold", 1);
        typeFacesMap.put("opensans_regular", 2);
        typeFacesMap.put("opensans_bold", 3);
        typeFacesMap.put("opensans_bold_italic", 4);
        typeFacesMap.put("opensans_extra_bold", 5);
        typeFacesMap.put("opensans_extra_bold_italic", 6);
        typeFacesMap.put("opensans_semi_bold", 7);
        typeFacesMap.put("opensans_semi_bold_italic", 8);
        typeFacesMap.put("sourcesans_regular", 9);
        typeFacesMap.put("sourcesans_bold", 10);
        typeFacesMap.put("sourcesans_bold_italic", 11);
        typeFacesMap.put("trebuchet_regular", 12);
        typeFacesMap.put("trebuchet_bold", 13);
        typeFacesMap.put("trebuchet_italic", 14);
        typeFacesMap.put("trebuchet_bold_italic", 15);
        typeFacesMap.put("felbridge_light", 16);
        typeFacesMap.put("felbridge_bold", 17);
    }

    private final static SparseArray<Typeface> mTypefaces = new SparseArray<Typeface>(20);

    /**
     * Obtain typeface.
     *
     * @param context       The Context the widget is running in, through which it can access the current theme, resources, etc.
     * @param typefaceValue The value of "typeface" attribute
     * @return specify {@link Typeface}
     * @throws IllegalArgumentException if unknown `typeface` attribute value.
     */
    public static Typeface obtaintTypeface(Context context, int typefaceValue) throws IllegalArgumentException {
        Typeface typeface = mTypefaces.get(typefaceValue);
        if (typeface == null) {
            typeface = createTypeface(context, typefaceValue);
            mTypefaces.put(typefaceValue, typeface);
        }
        return typeface;
    }

    /**
     * Obtain typeface.
     *
     * @param context             The Context the widget is running in, through which it can access the current theme, resources, etc.
     * @param typefaceValueString The value of "typeface" attribute
     * @return specify {@link Typeface}
     * @throws IllegalArgumentException if unknown `typeface` attribute value.
     */
    public static Typeface obtaintTypefaceFromString(Context context, String typefaceValueString) throws IllegalArgumentException {
        int typefaceValue = typeFacesMap.get(typefaceValueString);
        return obtaintTypeface(context, typefaceValue);
    }

    /**
     * Create typeface from assets.
     *
     * @param context       The Context the widget is running in, through which it can
     *                      access the current theme, resources, etc.
     * @param typefaceValue The value of "typeface" attribute
     * @return Roboto {@link Typeface}
     * @throws IllegalArgumentException if unknown `typeface` attribute value.
     */
    private static Typeface createTypeface(Context context, int typefaceValue)
            throws IllegalArgumentException {

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/Trebuchet-Regular.ttf");
        try {

            switch (typefaceValue) {
                case ALLER_REGULAR:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Aller-Rg.ttf");
                    break;
                case ALLER_BOLD:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Aller-Bd.ttf");
                    break;
                case OPENSANS_REGULAR:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
                    break;
                case OPENSANS_BOLD:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");
                    break;
                case OPENSANS_BOLD_ITALIC:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-BoldItalic.ttf");
                    break;
                case OPENSANS_EXTRA_BOLD:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-ExtraBold.ttf");
                    break;
                case OPENSANS_EXTRA_BOLD_ITALIC:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-ExtraBoldItalic.ttf");
                    break;
                case OPENSANS_SEMI_BOLD:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Semibold.ttf");
                    break;
                case OPENSANS_SEMI_BOLD_ITALIC:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-SemiboldItalic.ttf");
                    break;
                case SOURCESANS_REGULAR:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/SourceSansPro-Regular.otf");
                    break;
                case SOURCESANS_BOLD:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/SourceSansPro-Semibold.otf");
                    break;
                case SOURCESANS_BOLD_ITALIC:
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/SourceSansPro-SemiboldIt.otf");
                    break;
                case TREBUCHET_REGULAR:
                    typeface = Typeface.createFromAsset(context.getAssets(),
                            "fonts/Trebuchet-Regular.ttf");
                    break;
                case TREBUCHET_BOLD:
                    typeface = Typeface.createFromAsset(context.getAssets(),
                            "fonts/Trebuchet-Bold.ttf");
                    break;
                case TREBUCHET_ITALIC:
                    typeface = Typeface.createFromAsset(context.getAssets(),
                            "fonts/Trebuchet-Italic.ttf");
                    break;
                case TREBUCHET_BOLD_ITALIC:
                    typeface = Typeface.createFromAsset(context.getAssets(),
                            "fonts/Trabuchet-BoldItalic.ttf");
                    break;
                case FELBRIDGE_LIGHT:
                    typeface = Typeface.createFromAsset(context.getAssets(),
                            "fonts/Felbridge-Light.otf");
                    break;
                case FELBRIDGE_BOLD:
                    typeface = Typeface.createFromAsset(context.getAssets(),
                            "fonts/Felbridge-Bold.otf");
                    break;
                default:
                    typeface = Typeface.createFromAsset(context.getAssets(),
                            "fonts/Aller-Rg.ttf");
                    // throw new
                    // IllegalArgumentException("Unknown `typeface` attribute value "
                    // + typefaceValue);
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("font", e.toString());
        }
        return typeface;
    }

}
