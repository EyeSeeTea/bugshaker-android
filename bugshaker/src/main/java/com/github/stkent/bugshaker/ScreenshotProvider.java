/**
 * Copyright 2016 Stuart Kent
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.github.stkent.bugshaker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class ScreenshotProvider {

    private static final String AUTHORITY = "com.github.stkent.bugshaker.fileprovider";
    private static final String SCREENSHOTS_DIRECTORY_NAME = "bug-reports";
    private static final String SCREENSHOT_FILE_NAME = "latest-screenshot.jpg";

    @NonNull
    private final Context applicationContext;

    @NonNull
    private final Logger logger;

    public ScreenshotProvider(
            @NonNull final Context applicationContext,
            @NonNull final Logger logger) {

        this.applicationContext = applicationContext;
        this.logger = logger;
    }

    @NonNull
    public Uri getScreenshotUri(@NonNull final Activity activity) throws IOException {
        final File screenshotFile = getScreenshotFile();
        final Bitmap screenshotBitmap = getBitmapFromRootView(activity);

        final OutputStream fileOutputStream = new FileOutputStream(screenshotFile);
        screenshotBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();

        logger.d("Screenshot successfully saved to file: " + screenshotFile.getAbsolutePath());

        final Uri result = FileProvider.getUriForFile(
                applicationContext,
                AUTHORITY,
                screenshotFile);

        logger.d("URI for screenshot file successfully created: " + result);

        return result;
    }

    private Bitmap getBitmapFromRootView(@NonNull final Activity activity) {
        final View view = activity.getWindow().getDecorView().getRootView();

        Bitmap screenshotBitmap
                = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        final Canvas canvas = new Canvas(screenshotBitmap);
        view.draw(canvas);

        return screenshotBitmap;
    }

    private File getScreenshotFile() {
        final File screenshotsDir = new File(
                applicationContext.getFilesDir(), SCREENSHOTS_DIRECTORY_NAME);

        //noinspection ResultOfMethodCallIgnored
        screenshotsDir.mkdirs();

        return new File(screenshotsDir, SCREENSHOT_FILE_NAME);
    }

}