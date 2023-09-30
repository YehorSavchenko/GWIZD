package com.centarius.gwizd.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

public class ImageUtils {

    public static Bitmap loadAndPreprocessImage(Context context, Uri imageUri, int inputSize) {
        try {
            // Load the image from the URI
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageUri), null, options);

            // Calculate the inSampleSize to resize the image while loading
            options.inSampleSize = calculateInSampleSize(options, inputSize, inputSize);

            // Decode the image with the calculated inSampleSize
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageUri), null, options);

            // Resize the bitmap to the input size expected by the model
            bitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, false);

            // Perform any additional preprocessing steps here, such as normalizing pixel values

            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}