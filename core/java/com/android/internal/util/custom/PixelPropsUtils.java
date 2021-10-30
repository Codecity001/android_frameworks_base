/*
 * Copyright (C) 2020 The Pixel Experience Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.internal.util.custom;

import android.os.Build;
import android.util.Log;

import java.util.Arrays;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class PixelPropsUtils {

    private static final String TAG = PixelPropsUtils.class.getSimpleName();
    private static final boolean DEBUG = false;

    private static final Map<String, Object> propsToChangePixelXL;
    private static final Map<String, Object> propsToChangePixel5;

    private static final String[] packagesToChangePixelXL = { 
        "com.google.android.apps.photos" 
    };

    private static final String[] packagesToChangePixel5 = { 
        "com.android.chrome", 
        "com.breel.wallpapers20",
        "com.google.android.apps.customization.pixel", 
        "com.google.android.apps.googleassistant",
        "com.google.android.apps.maps", 
        "com.google.android.apps.messaging", 
        "com.google.android.apps.nbu.files",
        "com.google.android.apps.podcasts", 
        "com.google.android.apps.safetyhub",
        "com.google.android.apps.subscriptions.red", 
        "com.google.android.apps.turbo",
        "com.google.android.apps.turboadapter", 
        "com.google.android.apps.wallpaper",
        "com.google.android.apps.wallpaper.pixel", 
        "com.google.android.apps.youtube.music", 
        "com.google.android.as",
        "com.google.android.contacts", 
        "com.google.android.deskclock", 
        "com.google.android.gms",
        "com.google.android.gms.location.history", 
        "com.google.android.inputmethod.latin",
        "com.google.pixel.dynamicwallpapers", 
        "com.google.pixel.livewallpaper",
        "com.google.android.googlequicksearchbox" 
    };

    static {
        propsToChangePixelXL = new HashMap<>();
        propsToChangePixelXL.put("BRAND", "google");
        propsToChangePixelXL.put("MANUFACTURER", "Google");
        propsToChangePixelXL.put("DEVICE", "marlin");
        propsToChangePixelXL.put("PRODUCT", "marlin");
        propsToChangePixelXL.put("MODEL", "Pixel XL");
        propsToChangePixelXL.put("ID", "QP1A.191005.007.A3");
        propsToChangePixelXL.put("SECURITY_PATCH", "2019-10-05");
        propsToChangePixelXL.put("FINGERPRINT", "google/marlin/marlin:10/QP1A.191005.007.A3/5972272:user/release-keys");
        propsToChangePixel5 = new HashMap<>();
        propsToChangePixel5.put("BRAND", "google");
        propsToChangePixel5.put("MANUFACTURER", "Google");
        propsToChangePixel5.put("DEVICE", "redfin");
        propsToChangePixel5.put("PRODUCT", "redfin");
        propsToChangePixel5.put("MODEL", "Pixel 5");
        propsToChangePixel5.put("ID", "RQ3A.211001.001");
        propsToChangePixel5.put("SECURITY_PATCH", "2021-10-01");
        propsToChangePixel5.put("FINGERPRINT", "google/redfin/redfin:11/RQ3A.211001.001/7641976:user/release-keys");
    }

    public static void setProps(String packageName) {
        if (packageName == null) {
            return;
        }
        if (Arrays.asList(packagesToChangePixelXL).contains(packageName)) {
            if (DEBUG) {
                Log.d(TAG, "Defining props for: " + packageName);
            }
            for (Map.Entry<String, Object> prop : propsToChangePixelXL.entrySet()) {
                String key = prop.getKey();
                Object value = prop.getValue();
                setPropValue(key, value);
            }
        }
        if (Arrays.asList(packagesToChangePixel5).contains(packageName)) {
            if (DEBUG) {
                Log.d(TAG, "Defining props for: " + packageName);
            }
            for (Map.Entry<String, Object> prop : propsToChangePixel5.entrySet()) {
                String key = prop.getKey();
                Object value = prop.getValue();
                // Don't set model if gms
                if (packageName.equals("com.google.android.gms") && key.equals("MODEL")) {
                    value = value + "\u200b";
                }
                setPropValue(key, value);
            }
        }
        // Set proper indexing fingerprint
        if (packageName.equals("com.google.android.settings.intelligence")) {
            setPropValue("FINGERPRINT", Build.DOT_FINGERPRINT);
        }
    }

    private static void setPropValue(String key, Object value) {
        try {
            if (DEBUG) {
                Log.d(TAG, "Defining prop " + key + " to " + value.toString());
            }
            Field field = Build.class.getDeclaredField(key);
            field.setAccessible(true);
            field.set(null, value);
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Log.e(TAG, "Failed to set prop " + key, e);
        }
    }
}