package com.qualsoft.volleylibrary.multipart;

import java.io.File;

/**
 * Created by suyati on 3/6/17.
 */

public class MultipartHelper {
    public static boolean isImageFile(File file) {
        final String[] okFileExtensions = new String[]{"jpg", "png", "gif", "jpeg"};
        for (String extension : okFileExtensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isVideoFile(File file) {
        final String[] okFileExtensions = new String[]{"jpg", "png", "gif", "jpeg"};
        for (String extension : okFileExtensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

}
