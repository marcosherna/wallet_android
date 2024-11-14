package com.example.wallet.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ImgHelper {
    public static String getImgNameUri(Uri uri, Context context) {
        String result = "";
        if (Objects.equals(uri.getScheme(), "content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (result.isEmpty()) {
            result = uri.getPath();
            int cut = result != null ? result.lastIndexOf('/') : 0;
            if (cut != -1) {
                result = result != null ? result.substring(cut + 1) : null;
            }
        }
        return result;
    }

    public static String convertUriToBase64(Uri imageUri, Context context) throws IOException {
        String base64Image;
        ContentResolver contentResolver = context.getContentResolver();

        InputStream inputStream = contentResolver.openInputStream(imageUri);
        if (inputStream == null) {
            return null;
        }

        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        inputStream.close();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return base64Image;
    }
}
